package gregtech.common.metatileentities.multi.electric;

import com.google.common.collect.Lists;

import gregtech.api.capability.IEnergyContainer;
import gregtech.api.capability.IMultipleTankHandler;
import gregtech.api.capability.impl.EnergyContainerList;
import gregtech.api.capability.impl.FluidTankList;
import gregtech.api.gui.ModularUI;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.interfaces.IGregTechTileEntity;
import gregtech.api.metatileentity.multiblock.IMultiblockPart;
import gregtech.api.metatileentity.multiblock.IPrimitivePump;
import gregtech.api.metatileentity.multiblock.MultiblockAbility;
import gregtech.api.metatileentity.multiblock.MultiblockControllerBase;
import gregtech.api.pattern.BlockPattern;
import gregtech.api.pattern.FactoryBlockPattern;
import gregtech.api.pattern.PatternMatchContext;
import gregtech.api.unification.material.Materials;
import gregtech.api.util.GTUtility;
import gregtech.api.util.LocalizationUtils;
import gregtech.client.renderer.ICubeRenderer;
import gregtech.client.renderer.texture.Textures;
import gregtech.common.blocks.BlockMetalCasing;
import gregtech.common.blocks.BlockSteamCasing;
import gregtech.common.blocks.MetaBlocks;
import gregtech.common.metatileentities.MetaTileEntities;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import codechicken.lib.render.CCRenderState;
import codechicken.lib.render.pipeline.IVertexOperation;
import codechicken.lib.vec.Matrix4;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class MetaTileEntityElectricWaterPump extends MultiblockControllerBase implements IPrimitivePump {

    private IEnergyContainer energyContainer;
    protected IMultipleTankHandler outputFluidInventory;

    private int biomeModifier = 0;
    private int tier = 0;

    public MetaTileEntityElectricWaterPump(ResourceLocation metaTileEntityId) {
        super(metaTileEntityId);
        resetTileAbilities();
    }

    @Override
    public MetaTileEntity createMetaTileEntity(IGregTechTileEntity tileEntity) {
        return new MetaTileEntityElectricWaterPump(metaTileEntityId);
    }

    @Override
    public void update() {
        super.update();
        if (!getWorld().isRemote && getOffsetTimer() % 20 == 0 && isStructureFormed()) {
            if (biomeModifier == 0) {
                biomeModifier = getAmount();
            } else if (biomeModifier > 0) {
                outputFluidInventory.fill(Materials.Water.getFluid(getFluidProduction()), true);
            }
        }
    }

    private int getAmount() {
        WorldProvider provider = getWorld().provider;
        if (provider.isNether() || provider.doesWaterVaporize()) {
            return -1; // Disabled
        }
        Biome biome = getWorld().getBiome(getPos());
        Set<BiomeDictionary.Type> biomeTypes = BiomeDictionary.getTypes(biome);
        if (biomeTypes.contains(BiomeDictionary.Type.NETHER)) {
            return -1; // Disabled
        }
        if (biomeTypes.contains(BiomeDictionary.Type.WATER)) {
            return 6000;
        } else if (biomeTypes.contains(BiomeDictionary.Type.SWAMP) || biomeTypes.contains(BiomeDictionary.Type.WET)) {
            return 5400;
        } else if (biomeTypes.contains(BiomeDictionary.Type.JUNGLE)) {
            return 2100;
        } else if (biomeTypes.contains(BiomeDictionary.Type.SNOWY)) {
            return 1800;
        } else
            if (biomeTypes.contains(BiomeDictionary.Type.PLAINS) || biomeTypes.contains(BiomeDictionary.Type.FOREST)) {
                return 1500;
            } else if (biomeTypes.contains(BiomeDictionary.Type.COLD)) {
                return 1050;
            } else if (biomeTypes.contains(BiomeDictionary.Type.BEACH)) {
                return 1020;
            }
        return 600;
    }

    @Override
    protected ModularUI createUI(EntityPlayer entityPlayer) {
        return null;
    }

    @Override
    protected boolean openGUIOnRightClick() {
        return false;
    }

    @Override
    protected void updateFormedValid() {}

    @Override
    protected void formStructure(PatternMatchContext context) {
        super.formStructure(context);
        initializeAbilities();
    }

    @Override
    public void invalidateStructure() {
        super.invalidateStructure();
        resetTileAbilities();
    }

    private void initializeAbilities() {
        this.outputFluidInventory = new FluidTankList(true, getAbilities(MultiblockAbility.EXPORT_FLUIDS));
        this.energyContainer = new EnergyContainerList(getAbilities(MultiblockAbility.INPUT_ENERGY));
        this.tier = GTUtility.getTierByVoltage(this.energyContainer.getInputVoltage());
    }

    private void resetTileAbilities() {
        this.outputFluidInventory = new FluidTankList(true);
        this.energyContainer = new EnergyContainerList(Lists.newArrayList());
    }

    @Override
    protected BlockPattern createStructurePattern() {
        return FactoryBlockPattern.start()
                .aisle("XXXX", "**F*", "**F*")
                .aisle("XXXX", "F**F", "FFFF")
                .aisle("SXXX", "**F*", "**F*")
                .where('S', selfPredicate())
                .where('X', states(MetaBlocks.METAL_CASING.getState(BlockMetalCasing.MetalCasingType.STEEL_SOLID))
                        .or(abilities(MultiblockAbility.INPUT_ENERGY).setMaxGlobalLimited(2))
                        .or(abilities(MultiblockAbility.EXPORT_FLUIDS).setExactLimit(1)))
                .where('F', frames(Materials.Steel))
                .where('*', any())
                .build();
    }

    @SideOnly(Side.CLIENT)
    @Override
    public ICubeRenderer getBaseTexture(IMultiblockPart sourcePart) {
        return Textures.SOLID_STEEL_CASING;
    }

    @SideOnly(Side.CLIENT)
    @NotNull
    @Override
    protected ICubeRenderer getFrontOverlay() {
        return Textures.PRIMITIVE_PUMP_OVERLAY;
    }

    @Override
    public void renderMetaTileEntity(CCRenderState renderState, Matrix4 translation, IVertexOperation[] pipeline) {
        super.renderMetaTileEntity(renderState, translation, pipeline);
        this.getFrontOverlay().renderOrientedState(renderState, translation, pipeline, getFrontFacing(), true, true);
    }

    @Override
    public String[] getDescription() {
        List<String> list = new ArrayList<>();
        list.add(I18n.format("gregtech.multiblock.electric_water_pump.description"));
        Collections.addAll(list, LocalizationUtils.formatLines("gregtech.multiblock.electric_water_pump.extra1"));
        Collections.addAll(list, LocalizationUtils.formatLines("gregtech.multiblock.electric_water_pump.extra2"));
        return list.toArray(new String[0]);
    }

    private boolean isRainingInBiome() {
        World world = getWorld();
        if (!world.isRaining()) {
            return false;
        }
        return world.getBiome(getPos()).canRain();
    }

    @Override
    public int getFluidProduction() {
        return (int) (biomeModifier * tier * (isRainingInBiome() ? 1.5 : 1));
    }

    @Override
    public boolean allowsExtendedFacing() {
        return false;
    }
}
