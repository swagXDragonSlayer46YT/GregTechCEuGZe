package gregtech.common.pipelike.fluidpipe;

import gregtech.api.pipenet.block.material.IMaterialPipeType;
import gregtech.api.unification.material.properties.FluidPipeProperties;
import gregtech.api.unification.ore.OrePrefix;

import org.jetbrains.annotations.NotNull;

public enum FluidPipeType implements IMaterialPipeType<FluidPipeProperties> {

    NORMAL("normal", 0.5f, 6, OrePrefix.pipeFluid, true),
    QUADRUPLE("quadruple", 0.95f, 2, OrePrefix.pipeQuadrupleFluid, true, 4),
    NONUPLE("nonuple", 0.95f, 2, OrePrefix.pipeNonupleFluid, true, 9);

    public static final FluidPipeType[] VALUES = values();

    public final String name;
    public final float thickness;
    public final int capacityMultiplier;
    public final OrePrefix orePrefix;
    public final boolean opaque;
    public final int channels;

    FluidPipeType(String name, float thickness, int capacityMultiplier, OrePrefix orePrefix, boolean opaque) {
        this(name, thickness, capacityMultiplier, orePrefix, opaque, 1);
    }

    FluidPipeType(String name, float thickness, int capacityMultiplier, OrePrefix orePrefix, boolean opaque,
                  int channels) {
        this.name = name;
        this.thickness = thickness;
        this.capacityMultiplier = capacityMultiplier;
        this.orePrefix = orePrefix;
        this.opaque = opaque;
        this.channels = channels;
    }

    @NotNull
    @Override
    public String getName() {
        return name;
    }

    @Override
    public float getThickness() {
        return thickness;
    }

    @Override
    public OrePrefix getOrePrefix() {
        return orePrefix;
    }

    @Override
    public FluidPipeProperties modifyProperties(FluidPipeProperties baseProperties) {
        return new FluidPipeProperties(
                baseProperties.getMaxFluidTemperature(),
                baseProperties.getThroughput() * capacityMultiplier,
                baseProperties.isGasProof(),
                baseProperties.isAcidProof(),
                baseProperties.isCryoProof(),
                baseProperties.isPlasmaProof(),
                channels);
    }

    @Override
    public boolean isPaintable() {
        return true;
    }
}
