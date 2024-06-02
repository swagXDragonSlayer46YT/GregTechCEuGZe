package gregtech.common.blocks.crop_tree;

import com.google.common.collect.Lists;

import gregtech.common.blocks.IVariantNamed;
import gregtech.common.blocks.MetaBlocks;
import gregtech.core.CoreModule;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Random;

public class CropLeaves extends BlockLeaves implements IVariantNamed {

    public static final PropertyInteger VARIANT = PropertyInteger.create("variant", 0, 3);
    private final int offset;

    public CropLeaves(int offset) {
        this.offset = offset;
        setTranslationKey("crop.leaves_" + offset);
        setHardness(0.2F);
        this.setLightOpacity(1);
        setDefaultState(this.blockState.getBaseState()
                .withProperty(CHECK_DECAY, true)
                .withProperty(DECAYABLE, true)
                .withProperty(VARIANT, 0));
        MetaBlocks.CROP_LEAVES.add(this);
        this.setCreativeTab(CreativeTabs.DECORATIONS);
        Blocks.FIRE.setFireInfo(this, 30, 60);
    }

    @Override
    public BlockPlanks.EnumType getWoodType(int i) {
        return null;
    }

    public CropTree getTreeFromState(IBlockState state) {
        if (state.getValue(VARIANT) + (offset * 4) >= CropTree.TREES.size()) {
            return CropTrees.LEMON_TREE; // Buildcraft again being awful
        }
        return CropTree.TREES.get(state.getValue(VARIANT) + (offset * 4));
    }

    @NotNull
    @Override
    public Item getItemDropped(@NotNull IBlockState state, @NotNull Random rand, int fortune) {
        return Item.getItemFromBlock(getTreeFromState(state).saplingState.getBlock());
    }

    @NotNull
    @Override
    @SuppressWarnings("deprecation")
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState()
                .withProperty(DECAYABLE, (meta & 1) == 1)
                .withProperty(CHECK_DECAY, (meta & 2) == 2)
                .withProperty(VARIANT, (meta & 12) >> 2);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        int meta = 0;
        if (state.getValue(DECAYABLE)) {
            meta |= 1;
        }
        if (state.getValue(CHECK_DECAY)) {
            meta |= 2;
        }
        meta |= state.getValue(VARIANT) << 2;
        return meta;
    }

    @NotNull
    @Override
    public List<ItemStack> onSheared(@NotNull ItemStack item, IBlockAccess world, BlockPos pos, int fortune) {
        return Lists.newArrayList(new ItemStack(this, 1, this.getMetaFromState(world.getBlockState(pos))));
    }

    @Override
    public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> items) {
        for (int i = 0; i < 4; i++) {
            if (CropTree.TREES.size() <= i + offset * 4)
                break;
            items.add(new ItemStack(this, 1, i << 2));
        }
    }

    @NotNull
    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, DECAYABLE, CHECK_DECAY, VARIANT);
    }

    @SideOnly(Side.CLIENT)
    public void registerColors() {
        Minecraft.getMinecraft().getBlockColors().registerBlockColorHandler((state, worldIn, pos, tintIndex) -> this.getTreeFromState(state).getBlockColor(state, worldIn, pos, tintIndex), this);
        Minecraft.getMinecraft().getItemColors().registerItemColorHandler((stack, tintIndex) -> this.getTreeFromState(this.getStateFromMeta(stack.getItemDamage())).getItemColor(stack, tintIndex), this);
    }

    @Override
    public String getVariantTranslationKey(IBlockState state) {
        try {
            return "gregtech.leaves." + this.getTreeFromState(state).name;
        } catch (IndexOutOfBoundsException e) {
            return "gregtech.hello_buildcraft";
        }
    }

    // The following code mostly taken from GregTechCEu's BlockRubberLeaves.java

    @Override
    @NotNull
    public BlockRenderLayer getRenderLayer() {
        if (!fancyLeaves()) {
            return super.getRenderLayer();
        }
        return BlockRenderLayer.CUTOUT_MIPPED;
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean isOpaqueCube(@NotNull IBlockState state) {
        if (!fancyLeaves()) {
            return super.isOpaqueCube(state);
        }
        return false;
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean shouldSideBeRendered(@NotNull IBlockState blockState, @NotNull IBlockAccess blockAccess, @NotNull BlockPos pos, @NotNull EnumFacing side) {
        if (!fancyLeaves()) {
            return super.shouldSideBeRendered(blockState, blockAccess, pos, side);
        }
        return true;
    }

    private boolean fancyLeaves() {
        return CoreModule.proxy.isFancyGraphics();
    }

    @Override
    protected void dropApple(World worldIn, BlockPos pos, IBlockState state, int chance) {
        spawnAsEntity(worldIn, pos, ((CropLeaves)state.getBlock()).getTreeFromState(state).getLeafDrop(chance));
    }

    // Primarily for getting the correct sapling type.
    @Override
    public int damageDropped(IBlockState state) {
        return (state.getValue(VARIANT) << 1) + ((offset % 2) * 8);
    }

    @Override
    @SuppressWarnings("deprecation")
    public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {
        return new ItemStack(Item.getItemFromBlock(this), 1, state.getValue(VARIANT) << 2);
    }

    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        LeafDecayHelper.leafDecay(this, worldIn, pos);
    }
}
