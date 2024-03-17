package gregtech.loaders.recipe;

import gregtech.api.fluids.ICoolant;
import gregtech.api.metatileentity.multiblock.CleanroomType;
import gregtech.api.recipes.RecipeMaps;
import gregtech.api.recipes.ingredients.nbtmatch.NBTCondition;
import gregtech.api.recipes.ingredients.nbtmatch.NBTMatcher;
import gregtech.api.unification.material.MarkerMaterials.Tier;
import gregtech.api.unification.material.Material;
import gregtech.common.ConfigHolder;
import gregtech.common.blocks.BlockComputerCasing;
import gregtech.common.blocks.BlockGlassCasing;

import net.minecraft.item.ItemStack;

import org.apache.logging.log4j.core.pattern.AbstractStyleNameConverter;

import java.util.ArrayList;
import java.util.List;

import static gregtech.api.GTValues.*;
import static gregtech.api.recipes.RecipeMaps.*;
import static gregtech.api.unification.material.Materials.*;
import static gregtech.api.unification.ore.OrePrefix.*;
import static gregtech.common.blocks.MetaBlocks.*;
import static gregtech.common.items.MetaItems.*;
import static gregtech.common.metatileentities.MetaTileEntities.*;
public class PBFRecipes {
    public static void init() {
        List<Material> IronOres = new ArrayList<Material>();
        IronOres.add(Magnetite);
        IronOres.add(BandedIron);
        IronOres.add(BrownLimonite);
        IronOres.add(YellowLimonite);
        IronOres.add(Pyrite);

        PRIMITIVE_BLAST_FURNACE_RECIPES.recipeBuilder().input(ingot, Iron).input(gem, Coal, 2).output(ingot, Steel)
                .output(dust, DarkAsh, 2).duration(1800).buildAndRegister();
        PRIMITIVE_BLAST_FURNACE_RECIPES.recipeBuilder().input(ingot, Iron).input(dust, Coal, 2).output(ingot, Steel)
                .output(dust, DarkAsh, 2).duration(1800).buildAndRegister();
        PRIMITIVE_BLAST_FURNACE_RECIPES.recipeBuilder().input(ingot, Iron).input(gem, Charcoal, 2).output(ingot, Steel)
                .output(dust, DarkAsh, 2).duration(1800).buildAndRegister();
        PRIMITIVE_BLAST_FURNACE_RECIPES.recipeBuilder().input(ingot, Iron).input(dust, Charcoal, 2).output(ingot, Steel)
                .output(dust, DarkAsh, 2).duration(1800).buildAndRegister();
        PRIMITIVE_BLAST_FURNACE_RECIPES.recipeBuilder().input(ingot, Iron).input(gem, Coke).output(ingot, Steel)
                .output(dust, Ash).duration(1500).buildAndRegister();
        PRIMITIVE_BLAST_FURNACE_RECIPES.recipeBuilder().input(ingot, Iron).input(dust, Coke).output(ingot, Steel)
                .output(dust, Ash).duration(1500).buildAndRegister();

        PRIMITIVE_BLAST_FURNACE_RECIPES.recipeBuilder().input(ingot, WroughtIron).input(gem, Coal, 2)
                .output(ingot, Steel).output(dust, DarkAsh, 2).duration(800).buildAndRegister();
        PRIMITIVE_BLAST_FURNACE_RECIPES.recipeBuilder().input(ingot, WroughtIron).input(dust, Coal, 2)
                .output(ingot, Steel).output(dust, DarkAsh, 2).duration(800).buildAndRegister();
        PRIMITIVE_BLAST_FURNACE_RECIPES.recipeBuilder().input(ingot, WroughtIron).input(gem, Charcoal, 2)
                .output(ingot, Steel).output(dust, DarkAsh, 2).duration(800).buildAndRegister();
        PRIMITIVE_BLAST_FURNACE_RECIPES.recipeBuilder().input(ingot, WroughtIron).input(dust, Charcoal, 2)
                .output(ingot, Steel).output(dust, DarkAsh, 2).duration(800).buildAndRegister();
        PRIMITIVE_BLAST_FURNACE_RECIPES.recipeBuilder().input(ingot, WroughtIron).input(gem, Coke)
                .output(ingot, Steel).output(dust, Ash).duration(600).buildAndRegister();
        PRIMITIVE_BLAST_FURNACE_RECIPES.recipeBuilder().input(ingot, WroughtIron).input(dust, Coke).output(ingot, Steel)
                .output(dust, Ash).duration(600).buildAndRegister();

        for (Material material : IronOres) {
            PRIMITIVE_BLAST_FURNACE_RECIPES.recipeBuilder().input(ore, material, 2).input(gem, Coal, 2)
                    .output(ingot, PigIron, 3).output(dust, DarkAsh, 2).duration(800).buildAndRegister();
            PRIMITIVE_BLAST_FURNACE_RECIPES.recipeBuilder().input(ore, material, 2).input(dust, Coal, 2)
                    .output(ingot, PigIron, 3).output(dust, DarkAsh, 2).duration(800).buildAndRegister();
            PRIMITIVE_BLAST_FURNACE_RECIPES.recipeBuilder().input(ore, material, 2).input(gem, Charcoal, 2)
                    .output(ingot, PigIron, 3).output(dust, DarkAsh, 2).duration(800).buildAndRegister();
            PRIMITIVE_BLAST_FURNACE_RECIPES.recipeBuilder().input(ore, material, 2).input(dust, Charcoal, 2)
                    .output(ingot, PigIron, 3).output(dust, DarkAsh, 2).duration(800).buildAndRegister();
            PRIMITIVE_BLAST_FURNACE_RECIPES.recipeBuilder().input(ore, material, 2).input(gem, Coke)
                    .output(ingot, PigIron, 3).output(dust, Ash).duration(600).buildAndRegister();
            PRIMITIVE_BLAST_FURNACE_RECIPES.recipeBuilder().input(ore, material, 2).input(dust, Coke).output(ingot, Steel)
                    .output(dust, Ash).duration(600).buildAndRegister();
        }

        FORGE_HAMMER_RECIPES.recipeBuilder()
                .input(ingot, PigIron)
                .output(ingot, WroughtIron)
                .EUt(16)
                .duration(5)
                .buildAndRegister();
    }
}
