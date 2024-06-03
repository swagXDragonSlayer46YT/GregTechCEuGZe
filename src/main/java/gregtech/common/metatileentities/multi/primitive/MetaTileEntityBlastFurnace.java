package gregtech.common.metatileentities.multi.primitive;

import codechicken.lib.texture.TextureUtils;
import codechicken.lib.vec.Cuboid6;

import gregtech.api.block.IRefractoryBrickBlockStats;
import gregtech.api.capability.GregtechDataCodes;
import gregtech.api.capability.impl.BlastFurnaceRecipeLogic;
import gregtech.api.capability.impl.FluidHandlerProxy;
import gregtech.api.capability.impl.FluidTankList;
import gregtech.api.capability.impl.ItemHandlerList;
import gregtech.api.capability.impl.ItemHandlerProxy;
import gregtech.api.capability.impl.NotifiableItemStackHandler;
import gregtech.api.gui.GuiTextures;
import gregtech.api.gui.ModularUI;
import gregtech.api.gui.widgets.LabelWidget;
import gregtech.api.gui.widgets.SlotWidget;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.interfaces.IGregTechTileEntity;
import gregtech.api.metatileentity.multiblock.IMultiblockPart;
import gregtech.api.metatileentity.multiblock.MultiblockAbility;
import gregtech.api.metatileentity.multiblock.MultiblockDisplayText;
import gregtech.api.metatileentity.multiblock.RecipeMapPrimitiveMultiblockController;
import gregtech.api.pattern.BlockPattern;
import gregtech.api.pattern.FactoryBlockPattern;
import gregtech.api.pattern.PatternMatchContext;
import gregtech.api.pattern.TraceabilityPredicate;
import gregtech.api.recipes.RecipeMaps;
import gregtech.api.util.GTTransferUtils;
import gregtech.api.util.GTUtility;
import gregtech.client.particle.VanillaParticleEffects;
import gregtech.client.renderer.CubeRendererState;
import gregtech.client.renderer.ICubeRenderer;
import gregtech.client.renderer.cclop.ColourOperation;
import gregtech.client.renderer.cclop.LightMapOperation;
import gregtech.client.renderer.texture.Textures;
import gregtech.client.utils.BloomEffectUtil;
import gregtech.common.blocks.BlockRefractoryBrick;
import gregtech.core.sound.GTSoundEvents;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import codechicken.lib.render.CCRenderState;
import codechicken.lib.render.pipeline.IVertexOperation;
import codechicken.lib.vec.Matrix4;
import org.apache.commons.lang3.ArrayUtils;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MetaTileEntityBlastFurnace extends RecipeMapPrimitiveMultiblockController {

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
            this.maximumHeat = BlockRefractoryBrick.RefractoryBrickType.NORMAL.getRefractoryBrickTemperature();
        }

        this.exportFluids = new FluidTankList(true, getAbilities(MultiblockAbility.EXPORT_FLUIDS));
        this.fluidInventory = new FluidHandlerProxy(this.importFluids, this.exportFluids);
    }

    @Override
    protected void initializeAbilities() {
        this.importItems = new NotifiableItemStackHandler(this, 27, this, false);
        this.itemInventory = new ItemHandlerProxy(this.importItems, this.exportItems);
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
                .aisle(" XXX ", " XXX ", " XXX ", "  X  ", "     ", "     ", "     ")
                .aisle("XBBBX", "XBBBX", "XBBBX", " BBB ", " BBB ", " BBB ", "  B  ")
                .aisle("XBBBX", "XB&BX", "XBABX", "XBABX", " BAB ", " BAB ", " BAB ")
                .aisle("XBBBX", "XBBBX", "XBBBX", " BBB ", " BBB ", " BBB ", "  B  ")
                .aisle(" XXX ", " XSX ", " XXX ", "  X  ", "     ", "     ", "     ")
                .where('S', selfPredicate())
                .where('B', refractoryBricks())
                .where('X', refractoryBricks()
                        .or(abilities(MultiblockAbility.EXPORT_FLUIDS)).setMinGlobalLimited(2))
                .where('A', air())
                .where('&', air().or(SNOW_PREDICATE)) // this won't stay in the structure, and will be broken while running
                .build();
    }

    private static final TraceabilityPredicate SNOW_PREDICATE = new TraceabilityPredicate(
            bws -> GTUtility.isBlockSnow(bws.getBlockState()));

    @Override
    public String[] getDescription() {
        return new String[] { I18n.format("gregtech.multiblock.blast_furnace.description") };
    }

    @Override
    public void renderMetaTileEntity(CCRenderState renderState, Matrix4 translation, IVertexOperation[] pipeline) {
        super.renderMetaTileEntity(renderState, translation, pipeline);
        this.getFrontOverlay().renderOrientedState(renderState, translation, pipeline, getFrontFacing(), isActive(),
                recipeMapWorkable.isWorkingEnabled());

        if (recipeMapWorkable.isActive() && isStructureFormed()) {
            EnumFacing back = getFrontFacing().getOpposite();
            Matrix4 offset = translation.copy().translate(back.getXOffset() * 2, 0.5f, back.getZOffset() * 2);
            CubeRendererState op = Textures.RENDER_STATE.get();
            Textures.RENDER_STATE.set(new CubeRendererState(op.layer, CubeRendererState.PASS_MASK, op.world));
            Textures.renderFace(renderState, offset,
                    ArrayUtils.addAll(pipeline, new LightMapOperation(240, 240), new ColourOperation(0xFFFFFFFF)),
                    EnumFacing.UP, Cuboid6.full, TextureUtils.getBlockTexture("lava_still"),
                    BloomEffectUtil.getEffectiveBloomLayer());
            Textures.RENDER_STATE.set(op);
        }
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
    public void updateFormedValid() {
        super.updateFormedValid();

        BlockPos selfPos = getPos();
        if (areaCenterPos == null || areaBoundingBox == null) {
            this.areaCenterPos = selfPos.offset(this.getFrontFacing().getOpposite(), 2);
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
    protected ModularUI.Builder createUITemplate(EntityPlayer entityPlayer) {
        //Allow players to see what's inside the furnace without interacting with it
        ModularUI.Builder builder = ModularUI.builder(GuiTextures.PRIMITIVE_BACKGROUND, 176, 166).shouldColor(false)
                .widget(new LabelWidget(7, 8, "Temperature: " + ((BlastFurnaceRecipeLogic) recipeMapWorkable).getCurrentHeat() + "K"));

        for (int i = 0; i < 9; i++){
            builder.widget(new SlotWidget(importItems, i, 7 + i * 18, 20, false, false)
                    .setBackgroundTexture(GuiTextures.PRIMITIVE_SLOT, GuiTextures.PRIMITIVE_FURNACE_OVERLAY));
            builder.widget(new SlotWidget(importItems, i + 9, 7 + i * 18, 38, false, false)
                    .setBackgroundTexture(GuiTextures.PRIMITIVE_SLOT, GuiTextures.PRIMITIVE_FURNACE_OVERLAY));
            builder.widget(new SlotWidget(importItems, i + 18, 7 + i * 18, 56, false, false)
                    .setBackgroundTexture(GuiTextures.PRIMITIVE_SLOT, GuiTextures.PRIMITIVE_FURNACE_OVERLAY));
        }

        return builder.bindPlayerInventory(entityPlayer.inventory, GuiTextures.PRIMITIVE_SLOT, 0);
    }

    @Override
    public void update() {
        super.update();

        if (this.isActive()) {
            if (getWorld().isRemote) {
                VanillaParticleEffects.PBF_SMOKE.runEffect(this);
            } else {
                damageEntitiesAndBreakSnow();
            }
        }
    }

    private void damageEntitiesAndBreakSnow() {
        BlockPos middlePos = this.getPos();
        middlePos = middlePos.offset(getFrontFacing().getOpposite()).offset(getFrontFacing().getOpposite());
        this.getWorld().getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(middlePos))
                .forEach(entity -> entity.attackEntityFrom(DamageSource.LAVA, 3.0f));

        if (getOffsetTimer() % 10 == 0) {
            IBlockState state = getWorld().getBlockState(middlePos);
            GTUtility.tryBreakSnow(getWorld(), middlePos, state, true);
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
