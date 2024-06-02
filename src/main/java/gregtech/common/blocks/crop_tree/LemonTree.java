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

public class LemonTree extends CropTree {
    public static int LEAVES_COLOR = 0x87A92C;
    public LemonTree() {
        super("lemon", 4);
        this.addCondition(new BiomeCondition(Biomes.JUNGLE_EDGE, 3, 0.4));
        this.addCondition(new BiomeCondition(Biomes.FOREST, 1, 0.65));
        this.addCondition(new TemperatureRainfallCondition(5, 1.2, 0.7, 0.7, 0.3));

    }

    @Override
    protected void generateLeaves(World world, BlockPos.MutableBlockPos pos, int trunkHeight, Random random, TriConsumer<World, BlockPos, IBlockState> notifier) {
        BlockPos.MutableBlockPos currentYPos = GTUtility.copy(pos);
        currentYPos.move(EnumFacing.UP, trunkHeight - 4);
        for (int i = 1; i < 12; i += (random.nextInt(3) + 2)) {
            int layerSize = (int) Math.ceil(Math.sqrt(i));
            Iterable<BlockPos> iterator = BlockPos.getAllInBox(
                    currentYPos.offset(EnumFacing.NORTH, layerSize).offset(EnumFacing.WEST, layerSize),
                    currentYPos.offset(EnumFacing.SOUTH, layerSize).offset(EnumFacing.EAST, layerSize));
            int finalI = i;
            iterator.forEach(leavesPos -> {
                if (Math.abs(leavesPos.getX() - currentYPos.getX()) + Math.abs(leavesPos.getZ() - currentYPos.getZ()) <= Math.sqrt(finalI) && random.nextInt(16 - finalI) != 0)
                    notifier.accept(world, leavesPos, getNaturalLeavesState());
            });
            currentYPos.move(EnumFacing.UP);
        }
        notifier.accept(world, GTUtility.copy(pos).move(EnumFacing.UP, trunkHeight), getNaturalLeavesState()); // In case the top isn't covered.
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
        if (GTValues.RNG.nextInt(chance / 10) == 0) {
            return MetaItems.LEMON.getStackForm(GTValues.RNG.nextInt(2) + 1);
        }
        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack getLeafDrop() {
        return MetaItems.LEMON.getStackForm();
    }
    @Override
    public int getMinTrunkHeight(Random random) {
        return 6 + random.nextInt(3);
    }

}
