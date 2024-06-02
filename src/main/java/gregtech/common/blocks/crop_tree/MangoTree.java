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
import net.minecraft.world.biome.Biome;

import java.util.Random;

public class MangoTree extends CropTree {
    public static int LEAVES_COLOR = 0x7D921E;
    public MangoTree() {
        super("mango", 2);
        this.addCondition(new BiomeCondition(new Biome[]{Biomes.MUTATED_JUNGLE_EDGE, Biomes.JUNGLE_EDGE}, 4, 0.2));
        this.addCondition(new TemperatureRainfallCondition(2, 1.5, 0.9, 0.9, 0.3));
    }

    @Override
    protected void generateLeaves(World world, BlockPos.MutableBlockPos pos, int trunkHeight, Random random, TriConsumer<World, BlockPos, IBlockState> notifier) {
        BlockPos.MutableBlockPos currentYPos = GTUtility.copy(pos);
        currentYPos.move(EnumFacing.UP, trunkHeight - 2);
        for (int i = 13; i > 0; i -= (random.nextInt(4) + 4)) {
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
            return MetaItems.MANGO.getStackForm(GTValues.RNG.nextInt(3));
        }
        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack getLeafDrop() {
        return MetaItems.MANGO.getStackForm();
    }
}
