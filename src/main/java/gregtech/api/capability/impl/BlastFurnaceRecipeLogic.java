package gregtech.api.capability.impl;

import gregtech.api.GTValues;
import gregtech.api.capability.IMultiblockController;
import gregtech.api.capability.IMultipleTankHandler;
import gregtech.api.metatileentity.multiblock.RecipeMapMultiblockController;
import gregtech.api.recipes.Recipe;
import gregtech.api.recipes.RecipeMap;
import gregtech.api.recipes.RecipeMaps;
import gregtech.api.unification.material.Materials;
import gregtech.api.util.GTLog;
import gregtech.common.ConfigHolder;

import gregtech.common.metatileentities.multi.primitive.MetaTileEntityBlastFurnace;

import gregtech.common.metatileentities.multi.primitive.MetaTileEntityLargeBoiler;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fluids.*;
import net.minecraftforge.fluids.capability.IFluidHandler;

import net.minecraftforge.items.IItemHandlerModifiable;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

import static gregtech.api.capability.GregtechDataCodes.MULTIBLOCK_HEAT;
import static gregtech.api.capability.GregtechDataCodes.BOILER_LAST_TICK_STEAM;

public class BlastFurnaceRecipeLogic extends PrimitiveRecipeLogic {

    //Start off at room temperature
    private int currentHeat = 293;

    private int ticksUntilNextHeat = 0;

    public BlastFurnaceRecipeLogic(MetaTileEntityBlastFurnace tileEntity) {
        super(tileEntity, RecipeMaps.PRIMITIVE_BLAST_FURNACE_RECIPES);
    }

    @Override
    public void update() {
        super.update();

        if ((!isActive() || !canProgressRecipe() || !isWorkingEnabled()) && currentHeat > 293) {
            setHeat(currentHeat - 1);
        }
    }

    @Override
    protected void updateRecipeProgress() {
        if (canRecipeProgress) {
            if (currentHeat < getMaximumHeat()) {
                ticksUntilNextHeat++;

                int ticksPerHeat = 10;

                if (ticksUntilNextHeat % ticksPerHeat == 0) {
                    ticksUntilNextHeat = 0;
                    setHeat(currentHeat + 1);
                }
            }

            progressTime += (int) (GTValues.RNG.nextFloat() * currentHeat / 600f);

            if (progressTime > maxProgressTime) {
                completeRecipe();
            }
        }
    }

    public int getCurrentHeat() {
        return currentHeat;
    }

    private int getMaximumHeat() {
        return getMetaTileEntity().getMaximumheat();
    }

    public void setHeat(int heat) {
        if (heat != this.currentHeat && !metaTileEntity.getWorld().isRemote) {
            writeCustomData(MULTIBLOCK_HEAT, b -> b.writeVarInt(heat));
        }
        this.currentHeat = heat;
    }

    @Override
    public boolean consumesEnergy() {
        return false;
    }

    @NotNull
    @Override
    public MetaTileEntityBlastFurnace getMetaTileEntity() {
        return (MetaTileEntityBlastFurnace) super.getMetaTileEntity();
    }

    @NotNull
    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound compound = super.serializeNBT();
        compound.setInteger("Heat", currentHeat);
        return compound;
    }

    @Override
    public void deserializeNBT(@NotNull NBTTagCompound compound) {
        super.deserializeNBT(compound);
        this.currentHeat = compound.getInteger("Heat");
    }

    @Override
    public void writeInitialSyncData(@NotNull PacketBuffer buf) {
        super.writeInitialSyncData(buf);
        buf.writeVarInt(currentHeat);
    }

    @Override
    public void receiveInitialSyncData(@NotNull PacketBuffer buf) {
        super.receiveInitialSyncData(buf);
        this.currentHeat = buf.readVarInt();
    }

    @Override
    public void receiveCustomData(int dataId, @NotNull PacketBuffer buf) {
        super.receiveCustomData(dataId, buf);
        if (dataId == MULTIBLOCK_HEAT) {
            this.currentHeat = buf.readVarInt();
        }
    }
}
