package gregtech.common.blocks.crop_tree;

import gregtech.common.blocks.IVariantNamed;

import gregtech.common.blocks.MetaBlocks;

import net.minecraft.block.BlockBush;
import net.minecraft.block.IGrowable;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;

import org.jetbrains.annotations.NotNull;

import java.util.Random;

import static net.minecraft.block.BlockSapling.STAGE;

public class CropSapling extends BlockBush implements IGrowable, IVariantNamed {

    protected static final AxisAlignedBB SAPLING_AABB = new AxisAlignedBB(0.1, 0.0D, 0.1, 0.9, 0.8, 0.9);

    public static final PropertyInteger VARIANT = PropertyInteger.create("variant", 0, 7);
    public final int offset;

    public CropSapling(int offset) {
        super(Material.LEAVES);
        this.offset = offset;
        setTranslationKey("crop.sapling_" + offset);
        this.setTickRandomly(true);
        this.setHardness(0.0F);
        this.setLightOpacity(1);
        this.setSoundType(SoundType.PLANT);
        MetaBlocks.CROP_SAPLINGS.add(this);
        this.setCreativeTab(CreativeTabs.DECORATIONS);
    }

    @NotNull
    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, STAGE, VARIANT);
    }

    @Override
    @NotNull
    @SuppressWarnings("deprecation")
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(STAGE, (meta & 1)).withProperty(VARIANT, (meta & 14) >> 1);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        int i = 0;
        i |= state.getValue(STAGE);
        i |= state.getValue(VARIANT) << 1;
        return i;
    }

    @Override
    public String getVariantTranslationKey(IBlockState state) {
        try {
            return "gregtech.sapling." + this.getTreeFromState(state).name;
        } catch (IndexOutOfBoundsException e) {
            return "gregtech.hello_buildcraft";
        }
    }

    public CropTree getTreeFromState(IBlockState state) {
        return CropTree.TREES.get(state.getValue(VARIANT) + (offset * 8));
    }

    @NotNull
    @Override
    @SuppressWarnings("deprecation")
    public AxisAlignedBB getBoundingBox(@NotNull IBlockState state, @NotNull IBlockAccess source, @NotNull BlockPos pos) {
        return SAPLING_AABB;
    }

    @Override
    public boolean canGrow(@NotNull World world, @NotNull BlockPos blockPos, @NotNull IBlockState iBlockState, boolean b) {
        return true;
    }

    @Override
    public boolean canUseBonemeal(@NotNull World world, @NotNull Random random, @NotNull BlockPos blockPos, @NotNull IBlockState iBlockState) {
        return true;
    }

    @Override
    public boolean canBeReplacedByLeaves(@NotNull IBlockState state, @NotNull IBlockAccess world, @NotNull BlockPos pos) {
        return true;
    }

    @Override
    public void grow(@NotNull World worldIn, @NotNull Random rand, @NotNull BlockPos pos, @NotNull IBlockState state) {
        this.getTreeFromState(state).getTreeGrowInstance().generate(worldIn, rand, pos);
    }

    @Override
    @NotNull
    public EnumPlantType getPlantType(@NotNull IBlockAccess world, @NotNull BlockPos pos) {
        return EnumPlantType.Plains;
    }

    @Override
    public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> items) {
        for (int i = 0; i < 8; i++) {
            if (CropTree.TREES.size() <= i + offset * 8)
                break;
            items.add(new ItemStack(this, 1, i * 2));
        }
    }

    @Override
    public int damageDropped(IBlockState state) {
        return state.getValue(VARIANT) << 1;
    }

    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
    {
        if (!worldIn.isRemote)
        {
            super.updateTick(worldIn, pos, state, rand);

            if (rand.nextInt(7) != 0) return; // Short-circuit the rest of this (looking at you, BlockSapling)
            if (!worldIn.isAreaLoaded(pos, 1)) return; // Forge: prevent loading unloaded chunks when checking neighbor's light
            if (worldIn.getLightFromNeighbors(pos.up()) >= 9)
            {
                this.grow(worldIn, rand, pos, state);
            }
        }
    }
}
