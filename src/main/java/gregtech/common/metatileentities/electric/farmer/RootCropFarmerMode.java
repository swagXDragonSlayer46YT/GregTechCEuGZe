package gregtech.common.metatileentities.electric.farmer;

import gregtech.common.blocks.crops.BlockGTRootCrop;
import gregtech.common.metatileentities.electric.MetaTileEntityFarmer;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class RootCropFarmerMode extends GTCropFarmerMode {

    @Override
    public boolean canOperate(IBlockState state, MetaTileEntityFarmer farmer, BlockPos pos, World world) {
        return state.getBlock() instanceof BlockGTRootCrop &&
                (((BlockGTRootCrop) state.getBlock()).isMaxAge(state) ||
                        hasNoSeeds(farmer, (BlockGTRootCrop) state.getBlock()) ? ((BlockGTRootCrop) state.getBlock()).seedHarvestable(state) : ((BlockGTRootCrop) state.getBlock()).cropHarvestable(state));
    }

    private boolean hasNoSeeds(MetaTileEntityFarmer farmer, BlockGTRootCrop crop) {
        ItemStack seed = crop.getSeedStack();
        for (int i = 0; i < farmer.getImportItems().getSlots(); i++) {
            if (seed.isItemEqual(farmer.getImportItems().getStackInSlot(i))) {
                return false;
            }
        }
        return true;
    }
}
