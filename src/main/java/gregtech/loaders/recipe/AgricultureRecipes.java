package gregtech.loaders.recipe;

import gregtech.api.fluids.ICoolant;
import gregtech.api.fluids.ICryoGas;
import gregtech.api.GTValues;
import gregtech.api.items.armor.ArmorMetaItem;
import gregtech.api.items.metaitem.MetaItem.MetaValueItem;
import gregtech.api.recipes.ModHandler;
import gregtech.api.recipes.RecipeBuilder;
import gregtech.api.recipes.RecipeMaps;
import gregtech.api.recipes.category.RecipeCategories;
import gregtech.api.recipes.ingredients.nbtmatch.NBTCondition;
import gregtech.api.recipes.ingredients.nbtmatch.NBTMatcher;
import gregtech.api.unification.OreDictUnifier;
import gregtech.api.unification.material.MarkerMaterials;
import gregtech.api.unification.material.MarkerMaterials.Color;
import gregtech.api.unification.material.Material;
import gregtech.api.unification.material.Materials;
import gregtech.api.unification.material.properties.DustProperty;
import gregtech.api.unification.material.properties.PropertyKey;
import gregtech.api.unification.ore.OrePrefix;
import gregtech.api.unification.stack.MaterialStack;
import gregtech.api.unification.stack.UnificationEntry;
import gregtech.api.util.GTUtility;
import gregtech.common.blocks.BlockGlassCasing;
import gregtech.common.blocks.BlockMetalCasing;
import gregtech.common.blocks.MetaBlocks;
import gregtech.common.blocks.crop_tree.CropTree;
import gregtech.common.blocks.crop_tree.CropTrees;
import gregtech.common.blocks.crops.BlockGTCrop;
import gregtech.common.items.MetaItems;
import gregtech.common.metatileentities.MetaTileEntities;

import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityMooshroom;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;

import static gregtech.api.GTValues.*;
import static gregtech.api.recipes.RecipeMaps.*;
import static gregtech.api.unification.material.Materials.*;
import static gregtech.api.unification.ore.OrePrefix.*;
import static gregtech.common.items.MetaItems.*;

public class AgricultureRecipes {
    public static void init() {
        CropTree.TREES.forEach(CropTree::initRecipes);
        registerVanillaTreeRecipes(new ItemStack(Blocks.SAPLING, 1, 0), new ItemStack(Blocks.LOG, 1, 0),
                new ItemStack(Blocks.LEAVES, 1, 0), new ItemStack(Items.APPLE));
        registerVanillaTreeRecipes(new ItemStack(Blocks.SAPLING, 1, 1), new ItemStack(Blocks.LOG, 1, 1),
                new ItemStack(Blocks.LEAVES, 1, 1), new ItemStack(Items.STICK));
        registerVanillaLargeTreeRecipe(new ItemStack(Blocks.SAPLING, 1, 1), new ItemStack(Blocks.LOG, 1, 1),
                new ItemStack(Items.STICK));
        registerVanillaTreeRecipes(new ItemStack(Blocks.SAPLING, 1, 2), new ItemStack(Blocks.LOG, 1, 2),
                new ItemStack(Blocks.LEAVES, 1, 2), new ItemStack(Items.STICK));
        registerVanillaTreeRecipes(new ItemStack(Blocks.SAPLING, 1, 3), new ItemStack(Blocks.LOG, 1, 3),
                new ItemStack(Blocks.LEAVES, 1, 3), new ItemStack(Items.STICK));
        registerVanillaTreeRecipes(new ItemStack(Blocks.SAPLING, 1, 4), new ItemStack(Blocks.LOG2, 1, 0),
                new ItemStack(Blocks.LEAVES2, 1, 0), new ItemStack(Items.STICK));
        registerVanillaTreeRecipes(new ItemStack(Blocks.SAPLING, 1, 5), new ItemStack(Blocks.LOG2, 1, 1),
                new ItemStack(Blocks.LEAVES2, 1, 1), new ItemStack(Items.STICK));
        registerVanillaLargeTreeRecipe(new ItemStack(Blocks.SAPLING, 1, 5), new ItemStack(Blocks.LOG2, 1, 1),
                new ItemStack(Items.STICK));
        registerTappingRecipes(new ItemStack(MetaBlocks.RUBBER_SAPLING, 1, 0), new ItemStack(MetaBlocks.RUBBER_LOG, 1, 0),
                new ItemStack(MetaBlocks.RUBBER_LEAVES, 1, 0), Latex.getFluid());

        // Sap processing
        RecipeMaps.FLUID_SOLIDFICATION_RECIPES.recipeBuilder().EUt(8).duration(160)
                .fluidInputs(Latex.getFluid(100))
                .notConsumable(MetaItems.SHAPE_MOLD_BALL)
                .output(MetaItems.STICKY_RESIN)
                .buildAndRegister();

        //Ungenerify seeds
        ItemStack[] crops = new ItemStack[]{
                LEMON.getStackForm(),
                LIME.getStackForm(),
                TOMATO.getStackForm(),
                CUCUMBER.getStackForm(),
                OLIVE.getStackForm(),
                ONION.getStackForm(),
                BANANA.getStackForm(),
                ORANGE.getStackForm(),
                GRAPES.getStackForm(),
                MANGO.getStackForm(),
                APRICOT.getStackForm(),
                PEA_POD.getStackForm(),
                SOYBEAN.getStackForm(),
                BEANS.getStackForm(),
                COFFEE_CHERRY.getStackForm(),
                CORN_EAR.getStackForm(),
                RICE.getStackForm(),
                HORSERADISH.getStackForm(),
                OREGANO.getStackForm(),
                GARLIC_BULB.getStackForm(),
                BASIL.getStackForm(),
                AUBERGINE.getStackForm(),
                ARTICHOKE_HEART.getStackForm(),
                BLACK_PEPPERCORN.getStackForm()
        };

        for (ItemStack seed : crops) {
            FERMENTING_RECIPES.recipeBuilder().EUt(3).duration(1600)
                    .inputs(seed)
                    .fluidInputs(Water.getFluid(100))
                    .fluidOutputs(Biomass.getFluid(100))
                    .buildAndRegister();
            ItemStack eight = seed.copy();
            eight.setCount(8);
            RecipeMaps.COMPRESSOR_RECIPES.recipeBuilder().EUt(2).duration(300)
                    .inputs(eight)
                    .outputs(MetaItems.PLANT_BALL.getStackForm())
                    .buildAndRegister();

        }

        for (BlockGTCrop crop : BlockGTCrop.CROP_BLOCKS) {
            RecipeMaps.EXTRACTOR_RECIPES.recipeBuilder().duration(32).EUt(2)
                    .inputs(crop.getSeedStack())
                    .fluidOutputs(SeedOil.getFluid(8))
                    .buildAndRegister();
        }

        ModHandler.addShapedRecipe("seed_soy_ungenerify", SOYBEAN_SEED.getStackForm(),
                "S  ", "   ", "   ",
                'S', UNKNOWN_SEED);
        ModHandler.addShapedRecipe("seed_tomato_ungenerify",TOMATO_SEED.getStackForm(),
                " S ", "   ", "   ",
                'S', UNKNOWN_SEED);
        ModHandler.addShapedRecipe("seed_cucumber_ungenerify", CUCUMBER_SEED.getStackForm(),
                "   ", "S  ", "   ",
                'S', UNKNOWN_SEED);
        ModHandler.addShapedRecipe("seed_onion_ungenerify", ONION_SEED.getStackForm(),
                "  S", "   ", "   ",
                'S', UNKNOWN_SEED);
        ModHandler.addShapedRecipe("seed_grapes_ungenerify", GRAPE_SEED.getStackForm(),
                "   ", " S ", "   ",
                'S', UNKNOWN_SEED);
        ModHandler.addShapedRecipe("seed_coffee_ungenerify", COFFEE_SEED.getStackForm(),
                "   ", "   ", "S  ",
                'S', UNKNOWN_SEED);
        ModHandler.addShapedRecipe("seed_pea_ungenerify", PEAS.getStackForm(),
                "   ", "  S", "   ",
                'S', UNKNOWN_SEED);
        ModHandler.addShapedRecipe("seed_bean_ungenerify", BEANS.getStackForm(),
                "   ", "   ", " S ",
                'S', UNKNOWN_SEED);
        ModHandler.addShapedRecipe("seed_oregano_ungenerify", OREGANO_SEED.getStackForm(),
                "   ", "   ", "  S",
                'S', UNKNOWN_SEED);
        ModHandler.addShapedRecipe("seed_horseradish_ungenerify", HORSERADISH_SEED.getStackForm(2),
                "SS ", "   ", "   ",
                'S', UNKNOWN_SEED);
        ModHandler.addShapedRecipe("seed_garlic_ungenerify", GARLIC_CLOVE.getStackForm(2),
                "S S", "   ", "   ",
                'S', UNKNOWN_SEED);
        ModHandler.addShapedRecipe("seed_basil_ungenerify", BASIL_SEED.getStackForm(2),
                " SS", "   ", "   ",
                'S', UNKNOWN_SEED);
        ModHandler.addShapedRecipe("seed_aubergine_ungenerify", AUBERGINE_SEED.getStackForm(2),
                "S  ", "S  ", "   ",
                'S', UNKNOWN_SEED);
        ModHandler.addShapedRecipe("seed_corn_ungenerify", CORN_EAR.getStackForm(2),
                " S ", "S  ", "   ",
                'S', UNKNOWN_SEED);
        ModHandler.addShapedRecipe("seed_artichoke_ungenerify", ARTICHOKE_SEED.getStackForm(2),
                "  S", "S  ", "   ",
                'S', UNKNOWN_SEED);
        ModHandler.addShapedRecipe("seed_black_pepper_ungenerify", BLACK_PEPPERCORN.getStackForm(2),
                "S  ", " S ", "   ",
                'S', UNKNOWN_SEED);
        ModHandler.addShapedRecipe("seed_rice_ungenerify", RICE.getStackForm(2),
                " S ", " S ", "   ",
                'S', UNKNOWN_SEED);
        ModHandler.addShapedRecipe("seed_white_grapes_ungenerify", WHITE_GRAPE_SEED.getStackForm(2),
                "  S", " S ", "   ",
                'S', UNKNOWN_SEED);

        ModHandler.addShapelessRecipe("seed_soy_extraction", SOYBEAN_SEED.getStackForm(),
                SOYBEAN);
        ModHandler.addShapelessRecipe("seed_tomato_extraction", TOMATO_SEED.getStackForm(),
                TOMATO);
        ModHandler.addShapelessRecipe("seed_cucumber_extraction", CUCUMBER_SEED.getStackForm(),
                CUCUMBER);
        ModHandler.addShapelessRecipe("seed_grapes_extraction", GRAPE_SEED.getStackForm(),
                GRAPES);
        ModHandler.addShapelessRecipe("seed_coffee_extraction", COFFEE_SEED.getStackForm(),
                COFFEE_CHERRY);
        ModHandler.addShapelessRecipe("seed_aubergine_extraction", AUBERGINE_SEED.getStackForm(),
                AUBERGINE);
        ModHandler.addShapelessRecipe("seed_artichoke_extraction", ARTICHOKE_SEED.getStackForm(),
                ARTICHOKE_HEART);
        ModHandler.addShapelessRecipe("clove_garlic_extraction", GARLIC_CLOVE.getStackForm(3),
                GARLIC_BULB.getStackForm());

        EXTRACTOR_RECIPES.recipeBuilder().EUt(8).duration(40)
                .inputs(GARLIC_BULB.getStackForm())
                .outputs(GARLIC_CLOVE.getStackForm(8))
                .buildAndRegister();

        ModHandler.addShapelessRecipe("gtfo_seed_pea_extraction", PEAS.getStackForm(),
                PEA_POD);

        GREENHOUSE_RECIPES.recipeBuilder().EUt(15).duration(500)
                .inputs(NUTMEG_SEED.getStackForm())
                .fluidInputs(Water.getFluid(1000))
                .chancedOutput(CropTrees.NUTMEG_TREE.getSaplingStack(), 5000, 0)
                .buildAndRegister();

        MOB_EXTRACTOR_RECIPES.recipeBuilder()
                .circuitMeta(3)
                .mob(EntityCow.class)
                .fluidOutputs(Milk.getFluid(10))
                .EUt(16)
                .duration(20)
                .buildAndRegister();

        MOB_EXTRACTOR_RECIPES.recipeBuilder()
                .circuitMeta(4)
                .mob(EntityCow.class)
                .EUt(16)
                .duration(20)
                .fluidOutputs(Blood.getFluid(10))
                .causeDamage(0.5f)
                .buildAndRegister();

        MOB_EXTRACTOR_RECIPES.recipeBuilder()
                .circuitMeta(5)
                .mob(EntityChicken.class)
                .EUt(16)
                .duration(20)
                .fluidOutputs(Blood.getFluid(1))
                .causeDamage(0.5f)
                .buildAndRegister();

        MOB_EXTRACTOR_RECIPES.recipeBuilder()
                .circuitMeta(6)
                .mob(EntitySheep.class)
                .EUt(16)
                .duration(20)
                .fluidOutputs(Blood.getFluid(5))
                .causeDamage(0.5f)
                .buildAndRegister();

        MOB_EXTRACTOR_RECIPES.recipeBuilder()
                .circuitMeta(7)
                .mob(EntityPig.class)
                .EUt(16)
                .duration(20)
                .fluidOutputs(Blood.getFluid(5))
                .causeDamage(0.5f)
                .buildAndRegister();

        MOB_EXTRACTOR_RECIPES.recipeBuilder()
                .circuitMeta(8)
                .mob(EntityVillager.class)
                .EUt(64)
                .duration(20)
                .fluidOutputs(Blood.getFluid(100))
                .causeDamage(0.5f)
                .buildAndRegister();

        MOB_EXTRACTOR_RECIPES.recipeBuilder()
                .circuitMeta(9)
                .mob(new ResourceLocation("player"))
                .EUt(16)
                .duration(20)
                .fluidOutputs(Blood.getFluid(200))
                .causeDamage(1.5f)
                .buildAndRegister();
    }

    public static void registerTappingRecipes(ItemStack sapling, ItemStack log, ItemStack leaves, Fluid sap) {
        GREENHOUSE_RECIPES.recipeBuilder().EUt(60).duration(2000)
                .inputs(sapling)
                .circuitMeta(1)
                .fluidInputs(Materials.Water.getFluid(10000))
                .outputs(GTUtility.copy(6, log), sapling)
                .chancedOutput(sapling, 2000, 1000)
                .buildAndRegister();
        GREENHOUSE_RECIPES.recipeBuilder().EUt(60).duration(2000)
                .inputs(sapling)
                .circuitMeta(2)
                .fluidInputs(Materials.Water.getFluid(10000))
                .outputs(GTUtility.copy(5, log))
                .chancedOutput(sapling, 1000, 1000)
                .outputs(GTUtility.copy(20, leaves))
                .buildAndRegister();
        GREENHOUSE_RECIPES.recipeBuilder().EUt(90).duration(3000)
                .inputs(sapling)
                .notConsumable(OrePrefix.toolHeadChainsaw, Steel)
                .circuitMeta(3)
                .fluidInputs(Materials.Water.getFluid(10000))
                .outputs(GTUtility.copy(5, log))
                .chancedOutput(sapling, 8000, 200)
                .fluidOutputs(new FluidStack(sap, 4000))
                .buildAndRegister();
        GREENHOUSE_RECIPES.recipeBuilder().EUt(90).duration(4000)
                .inputs(sapling, MetaItems.FERTILIZER.getStackForm(1))
                .notConsumable(OrePrefix.toolHeadChainsaw, Steel)
                .circuitMeta(4)
                .fluidInputs(Materials.Water.getFluid(10000))
                .outputs(GTUtility.copy(8, log))
                .chancedOutput(sapling, 8000, 200)
                .fluidOutputs(new FluidStack(sap, 16000))
                .buildAndRegister();
    }

    public static void registerVanillaTreeRecipes(ItemStack sapling, ItemStack log, ItemStack leaves, ItemStack crop) {
        GREENHOUSE_RECIPES.recipeBuilder().EUt(60).duration(2000)
                .inputs(sapling)
                .circuitMeta(1)
                .fluidInputs(Materials.Water.getFluid(10000))
                .outputs(GTUtility.copy(6, log), crop, sapling)
                .chancedOutput(sapling, 2000, 1000)
                .buildAndRegister();
        GREENHOUSE_RECIPES.recipeBuilder().EUt(60).duration(2000)
                .inputs(sapling)
                .circuitMeta(2)
                .fluidInputs(Materials.Water.getFluid(10000))
                .outputs(GTUtility.copy(5, log), crop)
                .chancedOutput(sapling, 1000, 1000)
                .outputs(GTUtility.copy(20, leaves))
                .buildAndRegister();
        GREENHOUSE_RECIPES.recipeBuilder().EUt(60).duration(2000)
                .inputs(sapling)
                .circuitMeta(3)
                .fluidInputs(Materials.Water.getFluid(20000))
                .outputs(GTUtility.copy(5, log))
                .chancedOutput(sapling, 8000, 200)
                .outputs(GTUtility.copy(3, crop))
                .chancedOutput(GTUtility.copy(2, crop), 4000, 500)
                .buildAndRegister();
    }

    public static void registerVanillaLargeTreeRecipe(ItemStack sapling, ItemStack log, ItemStack crop) {
        GREENHOUSE_RECIPES.recipeBuilder().EUt(60).duration(6000)
                .inputs(GTUtility.copy(4, sapling), MetaItems.FERTILIZER.getStackForm(4))
                .circuitMeta(4)
                .fluidInputs(Materials.Water.getFluid(40000))
                .outputs(GTUtility.copy(64, log))
                .outputs(GTUtility.copy(6, sapling))
                .chancedOutput(GTUtility.copy(4, sapling), 1000, 500)
                .outputs(GTUtility.copy(4,  crop))
                .buildAndRegister();
    }
}
