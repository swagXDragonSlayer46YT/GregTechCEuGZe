package gregtech.common.blocks.crop_tree;

import gregtech.api.GTValues;
import gregtech.api.util.GTUtility;
import gregtech.api.util.function.TriConsumer;
import gregtech.common.items.MetaItems;

import gregtech.common.worldgen.TemperatureRainfallCondition;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.Random;

public class CoconutTree extends CropTree {
    public static int LEAVES_COLOR = 0x657F1C;

    public CoconutTree() {
        super("coconut", 8);
        this.addCondition(new TemperatureRainfallCondition(5, 1.5, 0.8, 0.9, 0.5));
    }

    @Override
    public int getBlockColor(IBlockState state, IBlockAccess worldIn, BlockPos pos, int tintIndex) {
        return LEAVES_COLOR;
    }

    @Override
    public int getItemColor(ItemStack stack, int tintIndex) {
        return LEAVES_COLOR;
    }

    protected void generateLeaves(World world, BlockPos.MutableBlockPos pos, int height, Random random, TriConsumer<World, BlockPos, IBlockState> notifier) {
        // Generate top
        {
            BlockPos.MutableBlockPos posCopy = GTUtility.copy(pos.up(height - 1));
            int sideVariance = random.nextInt(4);
            for (int i = 0; i < 3; i++) {
                posCopy.move(EnumFacing.UP);
                notifier.accept(world, posCopy, getNaturalLeavesState());

                if (i == 1 || i == 2) {
                    posCopy.move(EnumFacing.byHorizontalIndex(sideVariance));
                    notifier.accept(world, posCopy, getNaturalLeavesState());
                }
            }
            posCopy.move(EnumFacing.byHorizontalIndex(sideVariance));
            notifier.accept(world, posCopy, getNaturalLeavesState());
        }

        // Generate sideways leaves
        for (int i = 0; i < 4; i++) {
            int leafOffset = random.nextInt(2);
            BlockPos.MutableBlockPos posCopy = GTUtility.copy(pos.up(height - 2 + leafOffset));

            int sideVariance = random.nextInt(2);
            for (int j = 0; j < 3; j++) {
                posCopy.move(EnumFacing.byHorizontalIndex(i));
                notifier.accept(world, posCopy, getNaturalLeavesState());
                if (j == 0) {
                    posCopy.move(EnumFacing.UP);
                    notifier.accept(world, posCopy, getNaturalLeavesState());
                }
                if (random.nextInt(3) == 0) {
                    if (sideVariance == 0) {
                        posCopy.move(EnumFacing.byHorizontalIndex(i).rotateY());
                        notifier.accept(world, posCopy, getNaturalLeavesState());
                    } else {
                        posCopy.move(EnumFacing.byHorizontalIndex(i).rotateYCCW());
                        notifier.accept(world, posCopy, getNaturalLeavesState());
                    }
                }
            }
        }

        // Generate ring at height - 3 for extra fullness
        for (int i = 0; i < 4; i++) {
            notifier.accept(world, pos.up(height - 1).offset(EnumFacing.byHorizontalIndex(i)), getNaturalLeavesState());
            notifier.accept(world, pos.up(height - 1).offset(EnumFacing.byHorizontalIndex(i)).offset(EnumFacing.byHorizontalIndex(i).rotateY()), getNaturalLeavesState());
        }
    }

    @Override
    public ItemStack getLeafDrop(int chance) {
        if (GTValues.RNG.nextInt(chance / 7) == 0) {
            return MetaItems.COCONUT.getStackForm(GTValues.RNG.nextInt(2));
        }
        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack getLeafDrop() {
        return MetaItems.COCONUT.getStackForm();
    }
}
