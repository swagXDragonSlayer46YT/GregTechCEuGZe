package gregtech.common.metatileentities.electric;

import gregtech.api.GTValues;
import gregtech.api.capability.IEnergyContainer;
import gregtech.api.capability.impl.RecipeLogicEnergy;
import gregtech.api.damagesources.DamageSources;
import gregtech.api.damagesources.GTFODamageSources;
import gregtech.api.gui.GuiTextures;
import gregtech.api.gui.ModularUI;
import gregtech.api.gui.widgets.CycleButtonWidget;
import gregtech.api.gui.widgets.GhostCircuitSlotWidget;
import gregtech.api.gui.widgets.ImageWidget;
import gregtech.api.gui.widgets.LabelWidget;
import gregtech.api.gui.widgets.SlotWidget;
import gregtech.api.gui.widgets.ToggleButtonWidget;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.SimpleMachineMetaTileEntity;
import gregtech.api.metatileentity.interfaces.IGregTechTileEntity;
import gregtech.api.recipes.Recipe;
import gregtech.api.recipes.RecipeMap;
import gregtech.api.recipes.RecipeMaps;
import gregtech.api.recipes.recipeproperties.CauseDamageProperty;
import gregtech.api.recipes.recipeproperties.MobOnTopProperty;
import gregtech.client.renderer.ICubeRenderer;
import gregtech.client.renderer.texture.Textures;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.items.IItemHandlerModifiable;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public class MetaTileEntityMobExtractor extends SimpleMachineMetaTileEntity  {
    private AxisAlignedBB boundingBox;
    private EntityLivingBase attackableTarget;
    private List<Entity> nearbyEntities;

    public MetaTileEntityMobExtractor(ResourceLocation metaTileEntityId, RecipeMap<?> recipeMap, ICubeRenderer renderer, int tier, boolean hasFrontFacing,
                                      Function<Integer, Integer> tankScalingFunction) {
        super(metaTileEntityId, recipeMap, renderer, tier, hasFrontFacing, tankScalingFunction);
    }

    @Override
    public MetaTileEntity createMetaTileEntity(IGregTechTileEntity holder) {
        return new MetaTileEntityMobExtractor(this.metaTileEntityId, RecipeMaps.MOB_EXTRACTOR_RECIPES,
                Textures.MOB_EXTRACTOR_OVERLAY, this.getTier(), this.hasFrontFacing(), this.getTankScalingFunction());
    }

    protected RecipeLogicEnergy createWorkable(RecipeMap<?> recipeMap) {
        return new MobExtractorRecipeLogic(this, recipeMap, () -> energyContainer);
    }

    protected boolean checkRecipe(@NotNull Recipe recipe) {
        ResourceLocation entityRequired = recipe.getProperty(MobOnTopProperty.getInstance(), null);
        if (this.nearbyEntities == null || this.getOffsetTimer() % 5 == 0)
            this.nearbyEntities = getEntitiesInProximity();
        for (Entity entity : nearbyEntities) {
            if (EntityList.isMatchingName(entity, entityRequired)) {
                if (entity instanceof EntityLivingBase) // Prepare to cause damage if needed.
                    attackableTarget = (EntityLivingBase) entity;
                else
                    attackableTarget = null;
                return true;
            }
        }
        return false;
    }

    protected List<Entity> getEntitiesInProximity() {
        if (boundingBox == null)
            boundingBox = new AxisAlignedBB(this.getPos().up());
        return this.getWorld().getEntitiesWithinAABB(Entity.class, boundingBox);
    }

    protected void damageEntity(Recipe recipe) {
        if (attackableTarget != null && recipe.hasProperty(CauseDamageProperty.getInstance())) {
            float damage = recipe.getProperty(CauseDamageProperty.getInstance(), 0f);
            if (damage > 0) {
                attackableTarget.attackEntityFrom(DamageSources.EXTRACTION, damage);
            }
        }
    }

    private static class MobExtractorRecipeLogic extends RecipeLogicEnergy {
        public MobExtractorRecipeLogic(MetaTileEntity metaTileEntity, RecipeMap<?> recipeMap, Supplier<IEnergyContainer> energyContainer) {
            super(metaTileEntity, recipeMap, energyContainer);
        }

        @Override
        protected boolean checkPreviousRecipe() {
            return super.checkPreviousRecipe() && this.checkRecipe(this.previousRecipe);
        }

        @Override
        public boolean checkRecipe(Recipe recipe) {
            return ((MetaTileEntityMobExtractor) metaTileEntity).checkRecipe(recipe);
        }

        @Override
        protected boolean setupAndConsumeRecipeInputs(Recipe recipe, IItemHandlerModifiable importInventory) {
            ((MetaTileEntityMobExtractor) metaTileEntity).damageEntity(recipe);
            return super.setupAndConsumeRecipeInputs(recipe, importInventory);
        }
    }

    @Override
    protected ModularUI.Builder createGuiTemplate(EntityPlayer player) {
        RecipeMap<?> workableRecipeMap = workable.getRecipeMap();
        int yOffset = 0;
        if (workableRecipeMap.getMaxInputs() >= 6 || workableRecipeMap.getMaxFluidInputs() >= 6 ||
                workableRecipeMap.getMaxOutputs() >= 6 || workableRecipeMap.getMaxFluidOutputs() >= 6) {
            yOffset = 9;
        }

        ModularUI.Builder builder = workableRecipeMap.createUITemplate(workable::getProgressPercent, importItems, exportItems, importFluids, exportFluids, yOffset)
                .widget(new LabelWidget(5, 5, getMetaFullName()))
                .widget(new SlotWidget(chargerInventory, 0, 79, 62 + yOffset, true, true, false)
                        .setBackgroundTexture(GuiTextures.SLOT, GuiTextures.CHARGER_OVERLAY)
                        .setTooltipText("gregtech.gui.charger_slot.tooltip", GTValues.VNF[getTier()], GTValues.VNF[getTier()]))
                .widget(new ImageWidget(79, 42 + yOffset, 18, 18, GuiTextures.INDICATOR_NO_ENERGY).setIgnoreColor(true)
                        .setPredicate(workable::isHasNotEnoughEnergy))
                .bindPlayerInventory(player.inventory, GuiTextures.SLOT, yOffset);

        int leftButtonStartX = 7;

        if (exportItems.getSlots() > 0) {
            builder.widget(new ToggleButtonWidget(leftButtonStartX, 62 + yOffset, 18, 18,
                    GuiTextures.BUTTON_ITEM_OUTPUT, this::isAutoOutputItems, this::setAutoOutputItems)
                    .setTooltipText("gregtech.gui.item_auto_output.tooltip")
                    .shouldUseBaseBackground());
            leftButtonStartX += 18;
        }
        if (exportFluids.getTanks() > 0) {
            builder.widget(new ToggleButtonWidget(leftButtonStartX, 62 + yOffset, 18, 18,
                    GuiTextures.BUTTON_FLUID_OUTPUT, this::isAutoOutputFluids, this::setAutoOutputFluids)
                    .setTooltipText("gregtech.gui.fluid_auto_output.tooltip")
                    .shouldUseBaseBackground());
            leftButtonStartX += 18;
        }

        builder.widget(new CycleButtonWidget(leftButtonStartX, 62 + yOffset, 18, 18,
                workable.getAvailableOverclockingTiers(), workable::getOverclockTier, workable::setOverclockTier)
                .setTooltipHoverString("gregtech.gui.overclock.description")
                .setButtonTexture(GuiTextures.BUTTON_OVERCLOCK));

        if (exportItems.getSlots() + exportFluids.getTanks() <= 9) {
            if (this.circuitInventory != null) {
                SlotWidget circuitSlot = new GhostCircuitSlotWidget(this.circuitInventory, 0, 124, 62 + yOffset)
                        .setBackgroundTexture(GuiTextures.SLOT, this.getCircuitSlotOverlay());
                this.getCircuitSlotTooltip(circuitSlot);
            }
        }
        return builder;
    }
}
