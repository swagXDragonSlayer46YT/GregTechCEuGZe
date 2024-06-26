package gregtech.integration.jei.basic;

import gregtech.api.GTValues;
import gregtech.api.gui.GuiTextures;
import gregtech.api.recipes.recipeproperties.TemperatureProperty;
import gregtech.api.unification.OreDictUnifier;
import gregtech.api.unification.material.Materials;
import gregtech.api.unification.ore.OrePrefix;
import gregtech.integration.jei.utils.render.DrawableRegistry;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import com.google.common.collect.ImmutableList;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiFluidStackGroup;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class MaterialTreeCategory extends BasicRecipeCategory<MaterialTree, MaterialTree> {

    protected String materialName;
    protected String materialFormula;
    protected int materialBFTemp;
    protected String materialAvgM;
    protected String materialAvgP;
    protected String materialAvgN;

    protected final IDrawable slot;
    protected final IDrawable icon;

    protected List<Boolean> itemExists = new ArrayList<>();
    protected List<Boolean> fluidExists = new ArrayList<>();
    // XY positions of ingredients
    protected final static ImmutableList<Integer> ITEM_LOCATIONS = ImmutableList.of(
            // corresponds pair-to-one with PREFIXES in MaterialTree.java
            4, 101,     // dust
            29, 55,     // cableGtSingle
            29, 85,     // ingotHot
            29, 117,    // ingot
            29, 117,    // gem
            29, 147,    // block
            54, 55,     // wireGtSingle
            54, 85,     // stick
            54, 117,    // nugget
            54, 147,    // plate
            79, 55,     // wireFine
            79, 85,     // frameGt
            79, 117,    // round
            79, 147,    // pipeNormalFluid
            79, 147,    // pipeNormalItem
            104, 55,    // screw
            104, 85,    // bolt
            104, 117,   // gear
            104, 147,   // plateDouble
            129, 55,    // spring
            129, 85,    // stickLong
            129, 117,   // gearSmall
            129, 147,   // plateDense
            154, 55,    // springSmall
            154, 78,    // ring
            154, 124,   // lens
            154, 147    // foil
    );
    protected ImmutableList<Integer> FLUID_LOCATIONS = ImmutableList.of(
            154, 101    // fluid
    );

    public MaterialTreeCategory(IGuiHelper guiHelper) {
        super("material_tree",
                "recipemap.materialtree.name",
                guiHelper.createBlankDrawable(176, 166),
                guiHelper);

        this.slot = guiHelper.drawableBuilder(GuiTextures.SLOT.imageLocation, 0, 0, 18, 18).setTextureSize(18, 18)
                .build();
        this.icon = guiHelper.createDrawableIngredient(OreDictUnifier.get(OrePrefix.ingot, Materials.Aluminium));

        /*
         * couldn't think of a better way to register all these
         * generated with bash, requires imagemagick and sed
         * for file in ./*.png; do
         * dimstring=$(identify -ping -format '%w, %h' "$file")
         * basename "$file" .png | sed "s/\(.*\)/registerArrow(guiHelper, \"\1\", $dimstring);/"
         * done
         */
        registerArrow(guiHelper, "2d12", 5, 12);
        registerArrow(guiHelper, "2d16", 5, 16);
        registerArrow(guiHelper, "2r16d37", 18, 40);
        registerArrow(guiHelper, "d14", 5, 14);
        registerArrow(guiHelper, "d7r25u6", 28, 7);
        registerArrow(guiHelper, "d7r50d7", 53, 14);
        registerArrow(guiHelper, "d7r50u6", 53, 7);
        registerArrow(guiHelper, "d7r75d7", 78, 14);
        registerArrow(guiHelper, "d7r75u6", 78, 7);
        registerArrow(guiHelper, "d7r87u22r4", 92, 25);
        registerArrow(guiHelper, "d7r87u46r4", 92, 49);
        registerArrow(guiHelper, "l7", 7, 5);
        registerArrow(guiHelper, "r3d16r4", 7, 19);
        registerArrow(guiHelper, "r3d26r4", 7, 29);
        registerArrow(guiHelper, "r3u15r4", 7, 18);
        registerArrow(guiHelper, "r3u32r4", 7, 35);
        registerArrow(guiHelper, "r3u57r4", 7, 60);
        registerArrow(guiHelper, "r7", 7, 5);
        registerArrow(guiHelper, "u12", 5, 12);
        registerArrow(guiHelper, "u7r25d6", 28, 7);
        registerArrow(guiHelper, "u7r50d6", 53, 7);
        registerArrow(guiHelper, "u7r50u5", 53, 12);
        registerArrow(guiHelper, "u7r75d6", 78, 7);
        registerArrow(guiHelper, "u7r75u5", 78, 12);
        registerArrow(guiHelper, "u7r87d15r4", 92, 18);
        registerArrow(guiHelper, "u7r87u8r4", 92, 17);
        registerArrow(guiHelper, "r3u62r29", 32, 65);
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, @NotNull MaterialTree recipeWrapper, IIngredients ingredients) {
        // place and check existence of items
        IGuiItemStackGroup itemStackGroup = recipeLayout.getItemStacks();
        List<List<ItemStack>> itemInputs = ingredients.getInputs(VanillaTypes.ITEM);
        itemExists.clear();
        for (int i = 0; i < ITEM_LOCATIONS.size(); i += 2) {
            itemStackGroup.init(i, true, ITEM_LOCATIONS.get(i), ITEM_LOCATIONS.get(i + 1));
            itemExists.add(itemInputs.get(i / 2).size() > 0);
        }
        itemStackGroup.set(ingredients);

        // place and check existence of fluid(s)
        IGuiFluidStackGroup fluidStackGroup = recipeLayout.getFluidStacks();
        List<List<FluidStack>> fluidInputs = ingredients.getInputs(VanillaTypes.FLUID);
        fluidExists.clear();
        for (int i = 0; i < FLUID_LOCATIONS.size(); i += 2) {
            // fluids annoyingly need to be offset by 1 to fit in the slot graphic
            fluidStackGroup.init(0, true, FLUID_LOCATIONS.get(i) + 1, FLUID_LOCATIONS.get(i + 1) + 1);
            fluidExists.add(fluidInputs.get(i / 2).size() > 0);
        }
        fluidStackGroup.set(ingredients);

        // set info of current material
        materialName = recipeWrapper.getMaterialName();
        materialFormula = recipeWrapper.getMaterialFormula();
        materialBFTemp = recipeWrapper.getBlastTemp();
        materialAvgM = I18n.format("gregtech.jei.materials.average_mass", recipeWrapper.getAvgM());
        materialAvgP = I18n.format("gregtech.jei.materials.average_protons", recipeWrapper.getAvgP());
        materialAvgN = I18n.format("gregtech.jei.materials.average_neutrons", recipeWrapper.getAvgN());
    }

    @NotNull
    @Override
    public IRecipeWrapper getRecipeWrapper(@NotNull MaterialTree recipe) {
        return recipe;
    }

    @Nullable
    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public void drawExtras(@NotNull Minecraft minecraft) {
        // item slot rendering
        for (int i = 0; i < ITEM_LOCATIONS.size(); i += 2) {
            if (itemExists.get(i / 2))
                this.slot.draw(minecraft, ITEM_LOCATIONS.get(i), ITEM_LOCATIONS.get(i + 1));
        }

        // fluid slot rendering
        for (int i = 0; i < FLUID_LOCATIONS.size(); i += 2) {
            if (fluidExists.get(i / 2))
                this.slot.draw(minecraft, FLUID_LOCATIONS.get(i), FLUID_LOCATIONS.get(i + 1));
        }

        // arrow rendering, aka hardcoded jank
        // indeces are from ITEM_LOCATIONS / MaterialTree.PREFIXES
        // dust <-> block (if no ingot or gem)
        drawArrow(minecraft, "2r16d37", 22, 107, !itemExists.get(3) &&
                !itemExists.get(4) && itemExists.get(0) && itemExists.get(5));
        // dust -> ingotHot
        drawArrow(minecraft, "r3u15r4", 22, 92, itemExists.get(0) && itemExists.get(2));
        // dust -> ingot/gem (if no ingotHot)
        drawArrow(minecraft, "r3d16r4", 22, 109, !itemExists.get(2) &&
                itemExists.get(0) && (itemExists.get(3) || itemExists.get(4)));
        // ingotHot -> ingot
        drawArrow(minecraft, "d14", 35, 103, itemExists.get(2) && itemExists.get(3));
        // ingot/gem <-> block
        drawArrow(minecraft, "2d12", 35, 135, itemExists.get(5) &&
                (itemExists.get(3) || itemExists.get(4)));
        // ingot -> wireGtSingle
        drawArrow(minecraft, "r3u57r4", 47, 66, itemExists.get(3) && itemExists.get(6));
        // ingot/gem -> stick
        drawArrow(minecraft, "r3u32r4", 47, 91, itemExists.get(7) &&
                (itemExists.get(3) || itemExists.get(4)));
        // ingot -> nugget
        drawArrow(minecraft, "r7", 47, 123, itemExists.get(3) && itemExists.get(8));
        // ingot -> plate
        drawArrow(minecraft, "r3d26r4", 47, 125, itemExists.get(3) && itemExists.get(9));
        // ingot -> wireFine (if no wireGtSingle)
        drawArrow(minecraft, "r3u62r29", 47, 61, !itemExists.get(6) &&
                itemExists.get(3) && itemExists.get(10));
        // block -> plate
        drawArrow(minecraft, "r7", 47, 158, itemExists.get(5) && itemExists.get(9));
        // wireGtSingle -> cableGtSingle
        drawArrow(minecraft, "l7", 47, 57, itemExists.get(6) && itemExists.get(1));
        // wireGtSingle -> wireFine
        drawArrow(minecraft, "r7", 72, 61, itemExists.get(6) && itemExists.get(10));
        // stick -> frameGt
        drawArrow(minecraft, "d7r25u6", 62, 103, itemExists.get(7) && itemExists.get(11));
        // stick -> bolt
        drawArrow(minecraft, "d7r50u6", 62, 103, itemExists.get(7) && itemExists.get(16));
        // stick -> gear
        drawArrow(minecraft, "d7r50d7", 62, 103, itemExists.get(7) && itemExists.get(17));
        // stick -> stickLong
        drawArrow(minecraft, "d7r75u6", 62, 103, itemExists.get(7) && itemExists.get(20));
        // stick -> gearSmall
        drawArrow(minecraft, "d7r75d7", 62, 103, itemExists.get(7) && itemExists.get(21));
        // stick -> springSmall
        drawArrow(minecraft, "d7r87u46r4", 62, 61, itemExists.get(7) && itemExists.get(23));
        // stick -> ring
        drawArrow(minecraft, "d7r87u22r4", 62, 85, itemExists.get(7) && itemExists.get(24));
        // nugget -> round
        drawArrow(minecraft, "r7", 72, 123, itemExists.get(8) && itemExists.get(12));
        // plate -> pipeNormalFluid/pipeNormalItem
        drawArrow(minecraft, "u7r25d6", 62, 140, itemExists.get(9) &&
                (itemExists.get(13) || itemExists.get(14)));
        // plate -> gear
        drawArrow(minecraft, "u7r50u5", 62, 135, itemExists.get(9) && itemExists.get(17));
        // plate -> plateDouble
        drawArrow(minecraft, "u7r50d6", 62, 140, itemExists.get(9) && itemExists.get(18));
        // plate -> gearSmall
        drawArrow(minecraft, "u7r75u5", 62, 135, itemExists.get(9) && itemExists.get(21));
        // plate -> plateDense
        drawArrow(minecraft, "u7r75d6", 62, 140, itemExists.get(9) && itemExists.get(22));
        // plate -> lens
        drawArrow(minecraft, "u7r87u8r4", 62, 130, itemExists.get(9) && itemExists.get(25));
        // plate -> foil
        drawArrow(minecraft, "u7r87d15r4", 62, 140, itemExists.get(9) && itemExists.get(26));
        // bolt -> screw
        drawArrow(minecraft, "u12", 110, 73, itemExists.get(16) && itemExists.get(15));
        // stickLong -> spring
        drawArrow(minecraft, "u12", 135, 73, itemExists.get(20) && itemExists.get(19));

        // material info rendering
        int linesDrawn = 0;
        if (minecraft.fontRenderer.getStringWidth(materialName) > 176) {
            minecraft.fontRenderer.drawString(minecraft.fontRenderer.trimStringToWidth(materialName, 171) + "...",
                    0, 0, 0x111111);
            linesDrawn++;
        } else if (materialName.length() != 0) {
            minecraft.fontRenderer.drawString(materialName, 0, 0, 0x111111);
            linesDrawn++;
        }
        if (minecraft.fontRenderer.getStringWidth(materialFormula) > 176) {
            minecraft.fontRenderer.drawString(minecraft.fontRenderer.trimStringToWidth(materialFormula, 171) + "...",
                    0, FONT_HEIGHT * linesDrawn, 0x111111);
            linesDrawn++;
        } else if (materialFormula.length() != 0) {
            minecraft.fontRenderer.drawString(materialFormula, 0, FONT_HEIGHT * linesDrawn, 0x111111);
            linesDrawn++;
        }
        // don't think theres a good way to get the coil tier other than this
        if (materialBFTemp != 0) {
            TemperatureProperty.getInstance().drawInfo(minecraft, 0, FONT_HEIGHT * linesDrawn, 0x111111,
                    materialBFTemp);
            linesDrawn++;
        }
        minecraft.fontRenderer.drawString(materialAvgM, 0, FONT_HEIGHT * linesDrawn, 0x111111);
        linesDrawn++;
        minecraft.fontRenderer.drawString(materialAvgN, 0, FONT_HEIGHT * linesDrawn, 0x111111);
        linesDrawn++;
        minecraft.fontRenderer.drawString(materialAvgP, 0, FONT_HEIGHT * linesDrawn, 0x111111);
    }

    // a couple wrappers to make the code look less terrible
    private static void registerArrow(IGuiHelper guiHelper, String name, int width, int height) {
        DrawableRegistry.initDrawable(guiHelper, GTValues.MODID + ":textures/gui/arrows/" + name + ".png", width,
                height, name);
    }

    private static void drawArrow(Minecraft minecraft, String name, int x, int y, boolean shown) {
        if (shown) DrawableRegistry.drawDrawable(minecraft, name, x, y);
    }
}
