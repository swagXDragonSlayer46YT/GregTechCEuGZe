package gregtech.common.metatileentities.multi.primitive;

import gregtech.api.GTValues;
import gregtech.api.block.IHeatingCoilBlockStats;
import gregtech.api.block.IRefractoryBrickBlockStats;
import gregtech.api.capability.GregtechDataCodes;
import gregtech.api.capability.impl.BlastFurnaceRecipeLogic;
import gregtech.api.capability.impl.BoilerRecipeLogic;
import gregtech.api.capability.impl.CommonFluidFilters;
import gregtech.api.capability.impl.FluidTankList;
import gregtech.api.capability.impl.ItemHandlerList;
import gregtech.api.capability.impl.ItemHandlerProxy;
import gregtech.api.capability.impl.NotifiableItemStackHandler;
import gregtech.api.gui.GuiTextures;
import gregtech.api.gui.Widget;
import gregtech.api.gui.Widget.ClickData;
import gregtech.api.gui.resources.TextureArea;
import gregtech.api.gui.widgets.ClickButtonWidget;
import gregtech.api.gui.widgets.WidgetGroup;
import gregtech.api.metatileentity.MTETrait;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.interfaces.IGregTechTileEntity;
import gregtech.api.metatileentity.multiblock.IMultiblockPart;
import gregtech.api.metatileentity.multiblock.IProgressBarMultiblock;
import gregtech.api.metatileentity.multiblock.MultiblockAbility;
import gregtech.api.metatileentity.multiblock.MultiblockDisplayText;
import gregtech.api.metatileentity.multiblock.MultiblockWithDisplayBase;
import gregtech.api.metatileentity.multiblock.RecipeMapMultiblockController;
import gregtech.api.metatileentity.multiblock.RecipeMapPrimitiveMultiblockController;
import gregtech.api.pattern.BlockPattern;
import gregtech.api.pattern.FactoryBlockPattern;
import gregtech.api.pattern.PatternMatchContext;
import gregtech.api.recipes.RecipeMaps;
import gregtech.api.util.GTLog;
import gregtech.api.util.GTTransferUtils;
import gregtech.api.util.GTUtility;
import gregtech.api.util.TextComponentUtil;
import gregtech.api.util.TextFormattingUtil;
import gregtech.client.renderer.ICubeRenderer;
import gregtech.client.renderer.texture.Textures;
import gregtech.client.utils.TooltipHelper;
import gregtech.common.blocks.BlockRefractoryBrick;
import gregtech.common.blocks.BlockWireCoil;
import gregtech.core.sound.GTSoundEvents;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.IItemHandlerModifiable;

import codechicken.lib.render.CCRenderState;
import codechicken.lib.render.pipeline.IVertexOperation;
import codechicken.lib.vec.Matrix4;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

public class MetaTileEntityBlastFurnace extends RecipeMapPrimitiveMultiblockController implements IProgressBarMultiblock {

    private int tier = 1;
    private AxisAlignedBB areaBoundingBox;
    private BlockPos areaCenterPos;

    private int maximumHeat;

    public MetaTileEntityBlastFurnace(ResourceLocation metaTileEntityId) {
        super(metaTileEntityId, RecipeMaps.PRIMITIVE_BLAST_FURNACE_RECIPES);
        this.recipeMapWorkable = new BlastFurnaceRecipeLogic(this);
    }

    @Override
    public MetaTileEntity createMetaTileEntity(IGregTechTileEntity tileEntity) {
        return new MetaTileEntityBlastFurnace(metaTileEntityId);
    }

    @Override
    protected void formStructure(PatternMatchContext context) {
        super.formStructure(context);

        Object type = context.get("RefractoryBrickType");

        if (type instanceof IRefractoryBrickBlockStats) {
            this.maximumHeat = ((IRefractoryBrickBlockStats) type).getRefractoryBrickTemperature();
            this.tier = ((IRefractoryBrickBlockStats) type).getTier();

            writeCustomData(GregtechDataCodes.UPDATE_TIER, buf -> {
                buf.writeInt(this.tier);
            });
        } else {
            this.maximumHeat = BlockRefractoryBrick.RefractoryBrickType.TIER1.getRefractoryBrickTemperature();
        }

        initializeAbilities();
    }

    @Override
    public void invalidateStructure() {
        super.invalidateStructure();
        resetTileAbilities();
        this.recipeMapWorkable.invalidate();
    }

    @Override
    protected void initializeAbilities() {
        super.initializeAbilities();
        this.importItems = new NotifiableItemStackHandler(this, 25, this, false);
        this.exportItems = new ItemHandlerList(getAbilities(MultiblockAbility.EXPORT_ITEMS));
    }

    private void resetTileAbilities() {
        this.importItems = new ItemHandlerList(Collections.emptyList());
        this.exportItems = new ItemHandlerList(Collections.emptyList());
    }

    @Override
    protected void addDisplayText(List<ITextComponent> textList) {
        MultiblockDisplayText.builder(textList, isStructureFormed())
                .setWorkingStatus(recipeMapWorkable.isWorkingEnabled(), recipeMapWorkable.isActive())
                .addWorkingStatusLine();
    }

    @Override
    protected BlockPattern createStructurePattern() {
        return FactoryBlockPattern.start()
                .aisle("XXX", "XXX", "XXX", "XXX")
                .aisle("XXX", "XAX", "XAX", "XAX")
                .aisle("XXX", "XSX", "XXX", "XXX")
                .where('S', selfPredicate())
                .where('X', refractoryBricks()
                        .or(abilities(MultiblockAbility.EXPORT_ITEMS).setMaxGlobalLimited(1)))
                .where('A', air())
                .build();
    }

    @Override
    public String[] getDescription() {
        return new String[] { I18n.format("gregtech.multiblock.blast_furnace.description") };
    }

    @Override
    public void renderMetaTileEntity(CCRenderState renderState, Matrix4 translation, IVertexOperation[] pipeline) {
        super.renderMetaTileEntity(renderState, translation, pipeline);
        this.getFrontOverlay().renderOrientedState(renderState, translation, pipeline, getFrontFacing(), isActive(),
                recipeMapWorkable.isWorkingEnabled());
    }

    @SideOnly(Side.CLIENT)
    @NotNull
    @Override
    protected ICubeRenderer getFrontOverlay() {
        return Textures.PRIMITIVE_BLAST_FURNACE_OVERLAY;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public ICubeRenderer getBaseTexture(IMultiblockPart sourcePart) {
        if (tier == 1) {
            return Textures.TIER_1_REFRACTORY_BRICKS;
        }
        if (tier == 2) {
            return Textures.TIER_2_REFRACTORY_BRICKS;
        }
        if (tier == 3) {
            return Textures.TIER_3_REFRACTORY_BRICKS;
        }

        //Default to 1 if needed for some reason
        return Textures.TIER_1_REFRACTORY_BRICKS;
    }

    @Override
    public boolean hasMaintenanceMechanics() {
        return false;
    }

    public int getMaximumheat() {
        return maximumHeat;
    }

    @Override
    public SoundEvent getSound() {
        return GTSoundEvents.FURNACE;
    }

    @Override
    public void update() {
        super.update();

        BlockPos selfPos = getPos();
        if (areaCenterPos == null || areaBoundingBox == null) {
            this.areaCenterPos = selfPos.offset(this.getFrontFacing().getOpposite(), 1);
            this.areaBoundingBox = new AxisAlignedBB(areaCenterPos).grow(1, 0, 1);
        }

        List<EntityItem> items = this.getWorld().getEntitiesWithinAABB(EntityItem.class, areaBoundingBox);

        if (!items.isEmpty()) {
            for (EntityItem item : items) {
                GTTransferUtils.insertItem(this.importItems, item.getItem(), false);
                item.setDead();
            }
        }
    }

    @Override
    protected boolean shouldShowVoidingModeButton() {
        return false;
    }

    @Override
    public double getFillPercentage(int index) {
        if (!isStructureFormed()) return 0;
        return (1.0 * ((BlastFurnaceRecipeLogic) recipeMapWorkable).getCurrentHeat() )/ maximumHeat;
    }

    @Override
    public TextureArea getProgressBarTexture(int index) {
        return GuiTextures.PROGRESS_BAR_FLUID_RIG_DEPLETION;
    }

    @Override
    public void addBarHoverText(List<ITextComponent> hoverList, int index) {
        if (!isStructureFormed()) {
            hoverList.add(TextComponentUtil.translationWithColor(TextFormatting.GRAY,
                    "gregtech.multiblock.invalid_structure"));
        } else {
            ITextComponent heatInfo = TextComponentUtil.translationWithColor(
                    TextFormatting.BLUE,
                    "%s / %s K",
                    ((BlastFurnaceRecipeLogic) recipeMapWorkable).getCurrentHeat(), this.maximumHeat);
            hoverList.add(TextComponentUtil.translationWithColor(
                    TextFormatting.GRAY,
                    "Heat: %s",
                    heatInfo));
        }
    }

    @Override
    public void receiveCustomData(int dataId, PacketBuffer buf) {
        super.receiveCustomData(dataId, buf);
        if (dataId == GregtechDataCodes.UPDATE_TIER) {
            this.tier = buf.readInt();
        }
    }

    @Override
    public NBTTagCompound writeToNBT(@NotNull NBTTagCompound data) {
        super.writeToNBT(data);
        data.setInteger("tier", this.tier);
        return data;
    }

    @Override
    public void readFromNBT(NBTTagCompound data) {
        super.readFromNBT(data);
        this.tier = data.hasKey("tier") ? data.getInteger("tier") : this.tier;
        reinitializeStructurePattern();
    }

    @Override
    public void writeInitialSyncData(PacketBuffer buf) {
        super.writeInitialSyncData(buf);
        buf.writeInt(this.tier);
    }

    @Override
    public void receiveInitialSyncData(PacketBuffer buf) {
        super.receiveInitialSyncData(buf);
        this.tier = buf.readInt();
    }
}
