package gregtech.common.blocks.crop_tree;

import gregtech.common.blocks.IVariantNamed;

import gregtech.common.blocks.MetaBlocks;

import net.minecraft.block.BlockLog;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

import org.jetbrains.annotations.NotNull;

public class CropLog extends BlockLog implements IVariantNamed {
    public static final PropertyInteger VARIANT = PropertyInteger.create("variant", 0, 3);
    private final int offset;

    public CropLog(int offset) {
        this.offset = offset;
        setTranslationKey("crop.log_" + offset);
        setHarvestLevel("axe", 0);
        setDefaultState(this.blockState.getBaseState()
                .withProperty(LOG_AXIS, EnumAxis.Y));
        MetaBlocks.CROP_LOGS.add(this);
        Blocks.FIRE.setFireInfo(this, 5, 5);
        this.setCreativeTab(CreativeTabs.DECORATIONS);
        setHarvestLevel("axe", 0);
    }

    @Override
    public String getVariantTranslationKey(IBlockState state) {
        try {
            return "gregtech.log." + this.getTreeFromState(state).name;
        } catch (IndexOutOfBoundsException e) {
            return "gregtech.hello_buildcraft";
        }
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        int meta = 0;
        meta |= state.getValue(LOG_AXIS).ordinal();
        meta |= state.getValue(VARIANT) << 2;
        return meta;
    }

    public CropTree getTreeFromState(IBlockState state) {
        return CropTree.TREES.get(state.getValue(VARIANT) + (offset * 4));
    }

    @NotNull
    @Override
    @SuppressWarnings("deprecation")
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState()
                .withProperty(LOG_AXIS, EnumAxis.values()[(meta & 3)])
                .withProperty(VARIANT, (meta & 12) >> 2);
    }

    @NotNull
    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, LOG_AXIS, VARIANT);
    }

    @Override
    public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> items) {
        for (int i = 0; i < 4; i++) {
            if (CropTree.TREES.size() <= i + offset * 4)
                break;
            items.add(new ItemStack(this, 1, i << 2));
        }
    }

    @Override
    public int damageDropped(IBlockState state) {
        return state.getValue(VARIANT) << 2;
    }
}
