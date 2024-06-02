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

public class OliveTree extends CropTree {
    public static int LEAVES_COLOR = 0x828E5A;

    public OliveTree() {
        super("olive", 6);
        this.addCondition(new BiomeCondition(Biomes.BIRCH_FOREST, 5, 0.55));
        this.addCondition(new BiomeCondition(Biomes.FOREST, 2, 0.65));
        this.addCondition(new BiomeCondition(Biomes.PLAINS, 1, 0.88));
        this.addCondition(new TemperatureRainfallCondition(3, 1.5, 0.6, 0.6, 0.3));

    }

    @Override
    protected void generateTrunk(World world, BlockPos.MutableBlockPos pos, int maxHeight, Random random, TriConsumer<World, BlockPos, IBlockState> notifier) {
        BlockPos.MutableBlockPos upN = GTUtility.copy(pos);
        BlockPos.MutableBlockPos upNSplit = GTUtility.copy(upN);
        EnumFacing splitDirection = EnumFacing.byHorizontalIndex(random.nextInt(4));
        int splittingHeight = maxHeight - 1 - random.nextInt(3);
        for (int height = 0; height < maxHeight; ++height) {
            IBlockState state = world.getBlockState(upN);

            if (state.getBlock().isAir(state, world, upN) || state.getBlock().isLeaves(state, world, upN)) {
                notifier.accept(world, upN, logState.withProperty(BlockLog.LOG_AXIS, BlockLog.EnumAxis.Y));
            }

            if (height == splittingHeight) {
                upNSplit.move(splitDirection);
            }
            if (height >= splittingHeight) {
                notifier.accept(world, upNSplit, logState.withProperty(BlockLog.LOG_AXIS, BlockLog.EnumAxis.Y));
                if (random.nextInt(2) == 0)
                    upNSplit.move(splitDirection);
            }

            upN.move(EnumFacing.UP);
            upNSplit.move(EnumFacing.UP);
        }
    }

    @Override
    protected void generateLeaves(World world, BlockPos.MutableBlockPos pos, int trunkHeight, Random random, TriConsumer<World, BlockPos, IBlockState> notifier) {
        BlockPos.MutableBlockPos currentYPos = GTUtility.copy(pos);
        currentYPos.move(EnumFacing.UP, trunkHeight);
        for (int i = 25; i > 0; i -= (random.nextInt(8) + 13)) {
            int layerSize = (int) Math.ceil(Math.sqrt(i));
            Iterable<BlockPos> iterator = BlockPos.getAllInBox(
                    currentYPos.offset(EnumFacing.NORTH, layerSize).offset(EnumFacing.WEST, layerSize),
                    currentYPos.offset(EnumFacing.SOUTH, layerSize).offset(EnumFacing.EAST, layerSize));
            int finalI = i;
            iterator.forEach(leavesPos -> {
                if (Math.abs(leavesPos.getX() - currentYPos.getX()) + Math.abs(leavesPos.getZ() - currentYPos.getZ()) <= Math.sqrt(finalI))
                    notifier.accept(world, leavesPos, getNaturalLeavesState());
            });
            currentYPos.move(EnumFacing.UP);
        }
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
    protected int getMooreRadiusAtHeight(int height, int trunkHeight) {
        if (height < trunkHeight - 3)
            return 0;
        if (height < trunkHeight)
            return 4 - (trunkHeight - height);
        return 0;
    }

    @Override
    public ItemStack getLeafDrop(int chance) {
        if (GTValues.RNG.nextInt(chance / 15) == 0) {
            return MetaItems.OLIVE.getStackForm(GTValues.RNG.nextInt(4) + 1);
        }
        return ItemStack.EMPTY;
    }


    @Override
    public ItemStack getLeafDrop() {
        return MetaItems.OLIVE.getStackForm();
    }
}
