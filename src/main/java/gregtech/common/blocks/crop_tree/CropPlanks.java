package gregtech.common.blocks.crop_tree;

import gregtech.common.blocks.IVariantNamed;

import gregtech.common.blocks.MetaBlocks;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class CropPlanks extends Block implements IVariantNamed {
    public static final PropertyInteger VARIANT = PropertyInteger.create("variant", 0, 15);

    private final int offset;

    public CropPlanks(int offset) {
        super(Material.WOOD);
        this.offset = offset;
        setTranslationKey("crop.planks_" + offset);
        setHardness(2.0F);
        setResistance(5.0F);
        setHarvestLevel("axe", 0);
        this.setSoundType(SoundType.WOOD);
        Blocks.FIRE.setFireInfo(this, 5, 20);
        MetaBlocks.CROP_PLANKS.add(this);
        this.setCreativeTab(CreativeTabs.DECORATIONS);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, VARIANT);
    }

    @Override
    public String getVariantTranslationKey(IBlockState state) {
        try {
            return "gregtech.planks." + this.getTreeFromState(state).name;
        } catch (IndexOutOfBoundsException e) {
            return "gregtech.hello_buildcraft";
        }
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(VARIANT);
    }

    @Override
    @SuppressWarnings("deprecation")
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(VARIANT, meta);
    }

    public CropTree getTreeFromState(IBlockState state) {
        return CropTree.TREES.get(state.getValue(VARIANT) + (offset * 16));
    }

    @Override
    public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> items) {
        for (int i = 0; i < 16; i++) {
            if (CropTree.TREES.size() <= i + offset * 16)
                break;
            items.add(new ItemStack(this, 1, i));
        }
    }

    @Override
    public int damageDropped(IBlockState state) {
        return getMetaFromState(state);
    }
}
