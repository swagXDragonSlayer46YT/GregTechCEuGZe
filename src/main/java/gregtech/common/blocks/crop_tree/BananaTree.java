package gregtech.common.blocks.crop_tree;

import gregtech.api.GTValues;
import gregtech.api.util.GTUtility;
import gregtech.api.util.function.TriConsumer;
import gregtech.common.items.MetaItems;
import gregtech.common.worldgen.BiomeCondition;

import gregtech.common.worldgen.TemperatureRainfallCondition;

import net.minecraft.block.BlockLog;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Biomes;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.Random;

public class BananaTree extends CropTree {

    //TODO: make the generation here use the actual trunk height rather than the overall height`
    public static int LEAVES_COLOR = 0x396A2E;

    public BananaTree() {
        super("banana", 0);
        this.addCondition(new BiomeCondition(Biomes.JUNGLE, 5, 0.35));
        this.addCondition(new BiomeCondition(Biomes.JUNGLE_EDGE, 5, 0.3));
        this.addCondition(new BiomeCondition(Biomes.JUNGLE_HILLS, 5, 0.35));
        this.addCondition(new BiomeCondition(Biomes.MUTATED_JUNGLE, 5, 0.3));
        this.addCondition(new BiomeCondition(Biomes.MUTATED_JUNGLE_EDGE, 5, 0.15));
        this.addCondition(new TemperatureRainfallCondition(5, 1.5, 0.8, 0.8, 0.4));
    }

    protected void generateLeaves(World world, BlockPos.MutableBlockPos pos, int height, Random random, TriConsumer<World, BlockPos, IBlockState> notifier) {
        // Generate top
        {
            BlockPos.MutableBlockPos posCopy = GTUtility.copy(pos.up(height - 1));
            for (int i = 0; i < 3; i++) {
                posCopy.move(EnumFacing.UP);
                notifier.accept(world, posCopy, getNaturalLeavesState());
                if (i == 1) {
                    posCopy.move(EnumFacing.byHorizontalIndex(random.nextInt(4)));
                    notifier.accept(world, posCopy, getNaturalLeavesState());
                }
            }
        }

        // Generate sideways leaves
        for (int i = 0; i < 4; i++) {
            int leafOffset = random.nextInt(2);
            BlockPos.MutableBlockPos posCopy = GTUtility.copy(pos.up(height - 2 + leafOffset));

            for (int j = 0; j < 3; j++) {
                posCopy.move(EnumFacing.byHorizontalIndex(i));
                notifier.accept(world, posCopy, getNaturalLeavesState());
                if (j == 0) {
                    posCopy.move(EnumFacing.UP);
                    notifier.accept(world, posCopy, getNaturalLeavesState());
                }
            }
        }

        // Generate ring at height - 3 for extra fullness
        for (int i = 0; i < 4; i++) {
            notifier.accept(world, pos.up(height - 1).offset(EnumFacing.byHorizontalIndex(i)), getNaturalLeavesState());
            notifier.accept(world, pos.up(height - 1).offset(EnumFacing.byHorizontalIndex(i)).offset(EnumFacing.byHorizontalIndex(i).rotateY()), getNaturalLeavesState());
        }
    }

    protected void generateTrunk(World world, BlockPos.MutableBlockPos pos, int maxHeight, Random random, TriConsumer<World, BlockPos, IBlockState> notifier) {
        BlockPos.MutableBlockPos upN = GTUtility.copy(pos);
        for (int height = 0; height < maxHeight; ++height) {
            IBlockState state = world.getBlockState(upN);

            if (state.getBlock().isAir(state, world, upN) || state.getBlock().isLeaves(state, world, upN)) {
                notifier.accept(world, pos.up(height), logState.withProperty(BlockLog.LOG_AXIS, height == maxHeight - 1 ? BlockLog.EnumAxis.NONE : BlockLog.EnumAxis.Y));
            }
            upN.move(EnumFacing.UP);
        }
    }

    @Override
    public ItemStack getLeafDrop(int chance) {
        if (GTValues.RNG.nextInt(chance / 8) == 0) {
            return MetaItems.BANANA.getStackForm(GTValues.RNG.nextInt(4) + 3);
        }
        return ItemStack.EMPTY;
    }


    @Override
    public ItemStack getLeafDrop() {
        return MetaItems.BANANA.getStackForm();
    }

    @Override
    public int getBlockColor(IBlockState state, IBlockAccess worldIn, BlockPos pos, int tintIndex) {
        return LEAVES_COLOR;
    }

    @Override
    public int getItemColor(ItemStack stack, int tintIndex) {
        return LEAVES_COLOR;
    }

    @Override
    public int getMinTrunkHeight(Random random) {
        return 3 + random.nextInt(1);
    }

}
