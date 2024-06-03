package gregtech.common.metatileentities.primitive.multiblockpart;

import com.cleanroommc.modularui.widgets.FluidSlot;

import gregtech.api.gui.GuiTextures;
import gregtech.api.gui.ModularUI;
import gregtech.api.gui.widgets.TankWidget;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.interfaces.IGregTechTileEntity;
import gregtech.api.metatileentity.multiblock.MultiblockAbility;
import gregtech.api.metatileentity.multiblock.MultiblockControllerBase;
import gregtech.api.mui.GTGuiTheme;
import gregtech.api.mui.GTGuis;
import gregtech.client.renderer.ICubeRenderer;
import gregtech.client.renderer.texture.Textures;
import gregtech.client.renderer.texture.cube.SimpleOverlayRenderer;
import gregtech.client.utils.TooltipHelper;
import gregtech.common.ConfigHolder;
import gregtech.common.metatileentities.multi.multiblockpart.MetaTileEntityFluidHatch;
import gregtech.common.metatileentities.multi.multiblockpart.MetaTileEntityItemBus;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.items.IItemHandlerModifiable;

import codechicken.lib.render.CCRenderState;
import codechicken.lib.render.pipeline.IVertexOperation;
import codechicken.lib.vec.Matrix4;
import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.value.sync.GuiSyncManager;
import com.cleanroommc.modularui.value.sync.SyncHandlers;
import com.cleanroommc.modularui.widgets.ItemSlot;
import com.cleanroommc.modularui.widgets.layout.Grid;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class MetaTileEntityPrimitiveFluidHatch extends MetaTileEntityFluidHatch {

    public MetaTileEntityPrimitiveFluidHatch(ResourceLocation metaTileEntityId, boolean isExportHatch) {
        super(metaTileEntityId, 0, isExportHatch);
        initializeInventory();
    }

    @Override
    public MetaTileEntity createMetaTileEntity(IGregTechTileEntity tileEntity) {
        return new MetaTileEntityPrimitiveFluidHatch(metaTileEntityId, isExportHatch);
    }

    // Override base texture to have a bus with 4 slots, but ULV textures
    @Override
    public ICubeRenderer getBaseTexture() {
        MultiblockControllerBase controller = getController();
        if (controller == null)
            return Textures.TIER_1_REFRACTORY_BRICKS;
        return controller.getBaseTexture(this);
    }

    @Override
    protected ModularUI createUI(EntityPlayer entityPlayer) {
        ModularUI.Builder builder = ModularUI.builder(GuiTextures.PRIMITIVE_BACKGROUND, 176,
                        130)
                .label(10, 5, getMetaFullName());


        if (isExportHatch) {
            builder.widget(
                    new TankWidget(exportFluids.getTankAt(0), 80, 18, 18, 18)
                            .setBackgroundTexture(GuiTextures.FLUID_SLOT)
                            .setContainerClicking(true, false)
                            .setAlwaysShowFull(true));
        }
        else {
            builder.widget(
                    new TankWidget(importFluids.getTankAt(0), 80, 18, 18, 18)
                            .setBackgroundTexture(GuiTextures.FLUID_SLOT)
                            .setContainerClicking(false, true)
                            .setAlwaysShowFull(true));
        }

        builder.bindPlayerInventory(entityPlayer.inventory, GuiTextures.PRIMITIVE_SLOT, 7, 48);
        return builder.build(getHolder(), entityPlayer);
    }

    @Override
    public GTGuiTheme getUITheme() {
        return GTGuiTheme.PRIMITIVE;
    }

    @Override
    public int getDefaultPaintingColor() {
        return 0xFFFFFF;
    }
}
