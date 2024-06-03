package gregtech.loaders.recipe;

import com.cleanroommc.groovyscript.compat.vanilla.Crafting;

import gregtech.api.fluids.ICoolant;
import gregtech.api.fluids.ICryoGas;
import gregtech.api.metatileentity.multiblock.CleanroomType;
import gregtech.api.metatileentity.multiblock.IBatteryData;
import gregtech.api.recipes.RecipeMaps;
import gregtech.api.recipes.ingredients.GTRecipeItemInput;
import gregtech.api.recipes.ingredients.nbtmatch.NBTCondition;
import gregtech.api.recipes.ingredients.nbtmatch.NBTMatcher;
import gregtech.api.unification.material.MarkerMaterials.Tier;
import gregtech.api.unification.material.Material;
import gregtech.api.unification.material.Materials;
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


        class IPBFFuel {
            private Material fuel;
            //public static ArrayList<ICryoGas> cryo_gases = new ArrayList<ICryoGas>();
            private int duration;
            private int amount;
            private Material material;

            public static void main(String[] args){
                //this is needed for some reason, otherwise it will error
            }

            public IPBFFuel() {
                //this is needed for some reason, otherwise it will error
            }

            public IPBFFuel(Material fuel) {
                material = fuel;
            }

            public void setDuration(int time) {
                duration = time;
            }

            public int getDuration() {
                return duration;
            }

            public void setAmount(int number) {
                amount = number;
            }

            public int getAmount() {
                return amount;
            }

            public Material getMaterial() {
                return material;
            }

        }

        //do NOT set duration as a number less than 201
        IPBFFuel Coal = new IPBFFuel(Materials.Coal);
        Coal.setDuration(80);
        Coal.setAmount(3);
        IPBFFuel Coke = new IPBFFuel(Materials.Coke);
        Coke.setDuration(40);
        Coke.setAmount(1);
        IPBFFuel[] PBFFuels = {
            Coal,
            Coke
        };

        for (Material material : IronOres) {
            for (IPBFFuel fuel : PBFFuels) {
                PRIMITIVE_BLAST_FURNACE_RECIPES.recipeBuilder()
                        .input(ore, material, 2)
                        .input(gem, fuel.getMaterial(), fuel.getAmount())
                        .fluidOutputs(Iron.getFluid(432))
                        .fluidOutputs(Slag.getFluid(50))
                        .duration(fuel.getDuration())
                        .buildAndRegister();
            }
        }
    }
}
