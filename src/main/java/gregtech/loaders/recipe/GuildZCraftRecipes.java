package gregtech.loaders.recipe;

import gregtech.api.GTValues;
import gregtech.api.GregTechAPI;
import gregtech.api.items.OreDictNames;
import gregtech.api.items.metaitem.MetaItem;
import gregtech.api.metatileentity.multiblock.CleanroomType;
import gregtech.api.recipes.ModHandler;
import gregtech.api.recipes.RecipeMaps;
import gregtech.api.recipes.ingredients.GTRecipeInput;
import gregtech.api.recipes.ingredients.GTRecipeItemInput;
import gregtech.api.recipes.ingredients.IntCircuitIngredient;
import gregtech.api.unification.OreDictUnifier;
import gregtech.api.unification.material.MarkerMaterial;
import gregtech.api.unification.material.MarkerMaterials;
import gregtech.api.unification.material.Material;
import gregtech.api.unification.material.Materials;
import gregtech.api.unification.material.properties.PropertyKey;
import gregtech.api.unification.ore.OrePrefix;
import gregtech.api.unification.stack.MaterialStack;
import gregtech.api.unification.stack.UnificationEntry;
import gregtech.api.util.Mods;
import gregtech.common.ConfigHolder;
import gregtech.common.blocks.*;
import gregtech.common.blocks.BlockMachineCasing.MachineCasingType;
import gregtech.common.blocks.BlockMetalCasing.MetalCasingType;
import gregtech.common.blocks.BlockTurbineCasing.TurbineCasingType;
import gregtech.common.blocks.BlockWireCoil.CoilType;
import gregtech.common.blocks.StoneVariantBlock.StoneVariant;
import gregtech.common.items.MetaItems;
import gregtech.common.metatileentities.MetaTileEntities;
import gregtech.common.metatileentities.storage.MetaTileEntityQuantumChest;
import gregtech.common.metatileentities.storage.MetaTileEntityQuantumTank;
import gregtech.loaders.recipe.chemistry.AssemblerRecipeLoader;
import gregtech.loaders.recipe.chemistry.ChemistryRecipes;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.oredict.OreDictionary;

import org.apache.commons.lang3.ArrayUtils;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.stream.Collectors;

import static gregtech.api.GTValues.*;
import static gregtech.api.recipes.RecipeMaps.*;
import static gregtech.api.unification.material.Materials.*;
import static gregtech.api.unification.ore.OrePrefix.*;
import static gregtech.common.blocks.BlockMetalCasing.MetalCasingType.BRONZE_BRICKS;
import static gregtech.common.blocks.BlockMetalCasing.MetalCasingType.MAGNALIUM_FROSTPROOF;
import static gregtech.common.blocks.BlockMetalCasing2.MetalCasingType2.CERAMIC_MILL;
import static gregtech.common.blocks.MetaBlocks.METAL_CASING;
import static gregtech.common.blocks.MetaBlocks.METAL_CASING_2;
import static gregtech.common.items.MetaItems.*;
import static gregtech.common.metatileentities.MetaTileEntities.*;
import static gregtech.loaders.OreDictionaryLoader.OREDICT_BLOCK_FUEL_COKE;
import static gregtech.loaders.OreDictionaryLoader.OREDICT_FUEL_COKE;

public class GuildZCraftRecipes {
    public static void init() {
        controllerRecipes();
        casingRecipes();
        chemRecipes();
    }

    public static void controllerRecipes() {
        ASSEMBLER_RECIPES.recipeBuilder()
                .input(HULL[MV])
                .input(ELECTRIC_PUMP_MV, 16)
                .input(ELECTRIC_MOTOR_MV, 16)
                .input(LOGIC_CONTROLLER_MV, 16)
                .input(gear, Steel, 16)
                .fluidInputs(Lubricant.getFluid(10000))
                .output(BEDROCK_DRILL)
                .EUt(VA[MV])
                .duration(500)
                .buildAndRegister();

        ASSEMBLER_RECIPES.recipeBuilder()
                .input(HULL[LV])
                .inputs(METAL_CASING.getItemVariant(MAGNALIUM_FROSTPROOF))
                .input(ELECTRIC_PUMP_LV, 8)
                .input(pipeFluid, Magnalium, 8)
                .input(screw, Magnalium, 16)
                .input(circuit, MarkerMaterials.Tier.LV, 4)
                .output(INDUSTRIAL_REFRIGERATOR)
                .EUt(VA[LV])
                .duration(200)
                .buildAndRegister();

        ASSEMBLER_RECIPES.recipeBuilder()
                .input(HULL[HV])
                .inputs(METAL_CASING_2.getItemVariant(CERAMIC_MILL))
                .input(gear, StainlessSteel, 16)
                .input(ELECTRIC_MOTOR_HV, 16)
                .input(ring, Rubber, 8)
                .fluidInputs(Materials.Lubricant.getFluid(4000))
                .output(BALL_MILL)
                .EUt(VA[HV])
                .duration(200)
                .buildAndRegister();
    }

    public static void casingRecipes() {
        WELDING_RECIPES.recipeBuilder()
                .input(toolHeadDrill, Steel, 4)
                .input(gear, Steel, 1)
                .outputs(MetaBlocks.UNIQUE_CASING.getItemVariant(BlockUniqueCasing.UniqueCasingType.DRILL))
                .EUt(VA[MV])
                .duration(50)
                .buildAndRegister();

        WELDING_RECIPES.recipeBuilder()
                .input(plate, Magnalium, 6)
                .input(frameGt, Aluminium)
                .outputs(MetaBlocks.METAL_CASING.getItemVariant(BlockMetalCasing.MetalCasingType.MAGNALIUM_FROSTPROOF))
                .EUt(VA[LV])
                .duration(50)
                .buildAndRegister();

        WELDING_RECIPES.recipeBuilder()
                .input(plate, BlackSteel, 6)
                .input(OrePrefix.block, Materials.Concrete)
                .outputs(MetaBlocks.METAL_CASING_2.getItemVariant(BlockMetalCasing2.MetalCasingType2.STRUCTURAL))
                .EUt(VA[LV])
                .duration(50)
                .buildAndRegister();

        BLAST_RECIPES.recipeBuilder()
                .inputs(MetaBlocks.METAL_CASING.getItemVariant(MetalCasingType.STAINLESS_CLEAN))
                .fluidInputs(Materials.CeramicGlaze.getFluid(1000))
                .outputs(METAL_CASING_2.getItemVariant(CERAMIC_MILL))
                .EUt(VA[LV])
                .duration(100)
                .buildAndRegister();

    }

    public static void chemRecipes() {
        MIXER_RECIPES.recipeBuilder()
                .input(dust, Stone,5)
                .input(dust, SiliconDioxide, 5)
                .input(dust, SodaAsh, 1)
                .input(dust, Clay, 1)
                .fluidInputs(Materials.Water.getFluid(8000))
                .fluidOutputs(CeramicGlaze.getFluid(8000))
                .EUt(VA[LV])
                .duration(200)
                .buildAndRegister();
    }
}
