package gregtech.common.blocks.crop_tree;

import gregtech.api.GTValues;
import gregtech.api.util.GTUtility;
import gregtech.api.util.function.TriConsumer;
import gregtech.common.items.MetaItems;
import gregtech.common.worldgen.BiomeCondition;

import gregtech.common.worldgen.TemperatureRainfallCondition;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Biomes;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.Random;

public class ApricotTree extends CropTree {
    public static int LEAVES_COLOR = 0x87A92C;
    public ApricotTree() {
        super("apricot", 3);
        this.addCondition(new BiomeCondition(Biomes.MUTATED_SAVANNA, 4, 0.40));
        this.addCondition(new BiomeCondition(Biomes.SAVANNA, 2, 0.55));
        this.addCondition(new TemperatureRainfallCondition(2, 1.20, 1.2, 0.05, 0.2));
    }

    @Override
    protected void generateLeaves(World world, BlockPos.MutableBlockPos pos, int trunkHeight, Random random, TriConsumer<World, BlockPos, IBlockState> notifier) {
        BlockPos.MutableBlockPos currentYPos = GTUtility.copy(pos);
        currentYPos.move(EnumFacing.UP, trunkHeight - 4);
        boolean atBottom = true;
        for (int i = 8; i > 0; i -= (random.nextInt(3) + 3)) {
            int layerSize = (int) Math.ceil(Math.sqrt(i));
            Iterable<BlockPos> iterator = BlockPos.getAllInBox(
                    currentYPos.offset(EnumFacing.NORTH, layerSize).offset(EnumFacing.WEST, layerSize),
                    currentYPos.offset(EnumFacing.SOUTH, layerSize).offset(EnumFacing.EAST, layerSize));
            int finalI = i;
            iterator.forEach(leavesPos -> {
                if (Math.abs(leavesPos.getX() - currentYPos.getX()) + Math.abs(leavesPos.getZ() - currentYPos.getZ()) <= Math.sqrt(finalI))
                    notifier.accept(world, leavesPos, getNaturalLeavesState());
            });
            if (atBottom) {
                i += 7;
                atBottom = false;
            }
            currentYPos.move(EnumFacing.UP);
        }
        notifier.accept(world, GTUtility.copy(pos).move(EnumFacing.UP, trunkHeight), getNaturalLeavesState()); // In case the top isn't covered.
    }

    @Override
    public int getMinTrunkHeight(Random random) {
        return super.getMinTrunkHeight(random);
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
    public ItemStack getLeafDrop(int chance) {
        if (GTValues.RNG.nextInt(chance / 15) == 0) {
            return MetaItems.APRICOT.getStackForm();
        }
        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack getLeafDrop() {
        return MetaItems.APRICOT.getStackForm();
    }
}
