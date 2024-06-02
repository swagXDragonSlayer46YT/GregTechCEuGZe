package gregtech.common.blocks;

import net.minecraft.block.state.IBlockState;

public interface IVariantNamed {
    String getVariantTranslationKey(IBlockState state);
}
