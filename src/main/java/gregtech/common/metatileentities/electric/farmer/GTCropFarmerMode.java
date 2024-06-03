package gregtech.common.metatileentities.electric.farmer;

import gregtech.api.items.metaitem.MetaItem;
import gregtech.common.blocks.crops.BlockGTCrop;

import gregtech.common.items.behaviors.CropSeedBehaviour;
import gregtech.common.metatileentities.electric.MetaTileEntityFarmer;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class GTCropFarmerMode implements FarmerMode {

    @Override
    public boolean canOperate(IBlockState state, MetaTileEntityFarmer farmer, BlockPos pos, World world) {
        return state.getBlock() instanceof BlockGTCrop && ((BlockGTCrop) state.getBlock()).isMaxAge(state);
    }

    @Override
    public boolean canPlaceItem(ItemStack stack) {
        return stack.getItem() instanceof MetaItem<?> && ((MetaItem<?>) stack.getItem()).getItem(stack).getBehaviours().stream().anyMatch(behaviour -> behaviour instanceof CropSeedBehaviour);
    }
}
