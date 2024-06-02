package gregtech.common.worldgen;

import gregtech.common.blocks.crops.GTBerry;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class GTBerryGen extends GTFeatureGen {
    public GTBerryGen(GTBerry berry) {
        super(true, berry);
    }

    @Override
    public boolean configOption() {
        return true;
    }

    @Override
    public boolean generateImpl(World world, Random random, BlockPos.MutableBlockPos pos) {
        return feature.generate(world, pos, random, this::setBlockSafely);
    }
}
