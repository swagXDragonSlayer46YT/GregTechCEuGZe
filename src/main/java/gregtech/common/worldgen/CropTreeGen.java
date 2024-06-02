package gregtech.common.worldgen;

import gregtech.common.blocks.crop_tree.CropTree;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.terraingen.SaplingGrowTreeEvent;
import net.minecraftforge.fml.common.eventhandler.Event;

import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class CropTreeGen extends GTFeatureGen {
    public CropTreeGen(boolean notify, CropTree tree) {
        super(notify, tree);
    }

    public boolean generateImpl(@NotNull World world, @NotNull Random random, BlockPos.MutableBlockPos pos) {
        SaplingGrowTreeEvent event = new SaplingGrowTreeEvent(world, random, pos);
        MinecraftForge.TERRAIN_GEN_BUS.post(event);
        if (event.getResult() == Event.Result.DENY) {
            return false;
        }
        return feature.generate(world, pos, random, this::setBlockSafely);
    }

    @Override
    public boolean configOption() {
        return true;
    }
}
