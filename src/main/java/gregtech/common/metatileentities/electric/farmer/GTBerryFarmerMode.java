package gregtech.common.metatileentities.electric.farmer;

import gregtech.common.blocks.crops.BlockGTBush;
import gregtech.common.blocks.crops.BlockGTCrop;
import gregtech.common.metatileentities.electric.MetaTileEntityFarmer;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class GTBerryFarmerMode implements FarmerMode {
    @Override
    public boolean canOperate(IBlockState state, MetaTileEntityFarmer farmer, BlockPos pos, World world) {
        return state.getBlock() instanceof BlockGTBush && ((BlockGTBush) state.getBlock()).isMaxAge(state);
    }

    @Override
    public boolean canPlaceItem(ItemStack stack) {
        return false;
    }

    @Override
    public void harvest(IBlockState state, World world, BlockPos.MutableBlockPos pos, MetaTileEntityFarmer farmer) {
        BlockGTCrop crop = (BlockGTCrop) state.getBlock();
        world.setBlockState(pos, state.withProperty(crop.AGE_GTFO, Integer.valueOf(crop.getMaxAge() - 1)), 3);
    }
}
