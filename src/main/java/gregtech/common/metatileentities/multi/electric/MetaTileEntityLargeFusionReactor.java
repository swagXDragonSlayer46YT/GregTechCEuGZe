package gregtech.common.metatileentities.multi.electric;

import gregtech.api.GTValues;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.multiblock.MultiblockAbility;
import gregtech.api.pattern.BlockPattern;
import gregtech.api.pattern.FactoryBlockPattern;
import gregtech.api.unification.material.Materials;
import gregtech.common.blocks.MetaBlocks;
import gregtech.common.metatileentities.MetaTileEntities;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

import static gregtech.api.util.RelativeDirection.*;

public class MetaTileEntityLargeFusionReactor extends MetaTileEntityFusionReactor {

    public MetaTileEntityLargeFusionReactor(ResourceLocation metaTileEntityId, int tier) {
        super(metaTileEntityId, tier);

        this.tier = tier;
    }

    @NotNull
    @Override
    protected BlockPattern createStructurePattern() {

        return FactoryBlockPattern.start(RIGHT, FRONT, UP)
                .aisle( "                                               ",
                        "                                               ", "                    FCCCCCF                    ",
                        "                    FCIBICF                    ", "                    FCCCCCF                    ",
                        "                                               ", "                                               ",
                        "                                               ", "                                               ",
                        "                                               ", "                                               ",
                        "                                               ", "                                               ",
                        "                                               ", "                                               ",
                        "                                               ", "                                               ",
                        "                                               ", "                                               ",
                        "                                               ", "  FFF                                     FFF  ",
                        "  CCC                                     CCC  ", "  CIC                                     CIC  ",
                        "  CBC                                     CBC  ", "  CIC                                     CIC  ",
                        "  CCC                                     CCC  ", "  FFF                                     FFF  ",
                        "                                               ", "                                               ",
                        "                                               ", "                                               ",
                        "                                               ", "                                               ",
                        "                                               ", "                                               ",
                        "                                               ", "                                               ",
                        "                                               ", "                                               ",
                        "                                               ", "                                               ",
                        "                                               ", "                    FCCCCCF                    ",
                        "                    FCIBICF                    ", "                    FCCCCCF                    ",
                        "                                               ", "                                               ")
                .aisle( "                                               ",
                        "                    FCBBBCF                    ", "                   CC     CC                   ",
                        "                CCCCC     CCCCC                ", "              CCCCCCC     CCCCCCC              ",
                        "            CCCCCCC FCBBBCF CCCCCCC            ", "           CCCCC               CCCCC           ",
                        "          CCCC                   CCCC          ", "         CCC                       CCC         ",
                        "        CCC                         CCC        ", "       CCC                           CCC       ",
                        "      CCC                             CCC      ", "     CCC                               CCC     ",
                        "     CCC                               CCC     ", "    CCC                                 CCC    ",
                        "    CCC                                 CCC    ", "   CCC                                   CCC   ",
                        "   CCC                                   CCC   ", "   CCC                                   CCC   ",
                        "  CCC                                     CCC  ", " FCCCF                                   FCCCF ",
                        " C   C                                   C   C ", " B   B                                   B   B ",
                        " B   B                                   B   B ", " B   B                                   B   B ",
                        " C   C                                   C   C ", " FCCCF                                   FCCCF ",
                        "  CCC                                     CCC  ", "   CCC                                   CCC   ",
                        "   CCC                                   CCC   ", "   CCC                                   CCC   ",
                        "    CCC                                 CCC    ", "    CCC                                 CCC    ",
                        "     CCC                               CCC     ", "     CCC                               CCC     ",
                        "      CCC                             CCC      ", "       CCC                           CCC       ",
                        "        CCC                         CCC        ", "         CCC                       CCC         ",
                        "          CCCC                   CCCC          ", "           CCCCC               CCCCC           ",
                        "            CCCCCCC FCBBBCF CCCCCCC            ", "              CCCCCCC     CCCCCCC              ",
                        "                CCCCC     CCCCC                ", "                   CC     CC                   ",
                        "                    FCBBBCF                    ", "                                               ")
                .aisle("                    FCCCCCF                    ",
                        "                   CC     CC                   ", "                CCCCC     CCCCC                ",
                        "              CCCCCHHHHHHHHHCCCCC              ", "            CCCCHHHCC     CCHHHCCCC            ",
                        "           CCCHHCCCCC     CCCCCHHCCC           ", "          ECHHCCCCC FCCCCCF CCCCCHHCE          ",
                        "         CCHCCCC               CCCCHCC         ", "        CCHCCC                   CCCHCC        ",
                        "       CCHCE                       ECHCC       ", "      ECHCC                         CCHCE      ",
                        "     CCHCE                           ECHCC     ", "    CCHCC                             CCHCC    ",
                        "    CCHCC                             CCHCC    ", "   CCHCC                               CCHCC   ",
                        "   CCHCC                               CCHCC   ", "  CCHCC                                 CCHCC  ",
                        "  CCHCC                                 CCHCC  ", "  CCHCC                                 CCHCC  ",
                        " CCHCC                                   CCHCC ", "FCCHCCF                                 FCCHCCF",
                        "C  H  C                                 C  H  C", "C  H  C                                 C  H  C",
                        "C  H  C                                 C  H  C", "C  H  C                                 C  H  C",
                        "C  H  C                                 C  H  C", "FCCHCCF                                 FCCHCCF",
                        " CCHCC                                   CCHCC ", "  CCHCC                                 CCHCC  ",
                        "  CCHCC                                 CCHCC  ", "  CCHCC                                 CCHCC  ",
                        "   CCHCC                               CCHCC   ", "   CCHCC                               CCHCC   ",
                        "    CCHCC                             CCHCC    ", "    CCHCC                             CCHCC    ",
                        "     CCHCE                           ECHCC     ", "      ECHCC                         CCHCE      ",
                        "       CCHCE                       ECHCC       ", "        CCHCCC                   CCCHCC        ",
                        "         CCHCCCC               CCCCHCC         ", "          ECHHCCCCC FCCCCCF CCCCCHHCE          ",
                        "           CCCHHCCCCC     CCCCCHHCCC           ", "            CCCCHHHCC     CCHHHCCCC            ",
                        "              CCCCCHHHHHHHHHCCCCC              ", "                CCCCC     CCCCC                ",
                        "                   CC     CC                   ", "                    FCCCCCF                    ")
                .aisle("                    FCIBICF                    ",
                        "                   CC     CC                   ", "                CCCHHHHHHHHHCCC                ",
                        "              CCHHHHHHHHHHHHHHHCC              ", "            CCHHHHHHHHHHHHHHHHHHHCC            ",
                        "           CHHHHHHHCC     CCHHHHHHHC           ", "          CHHHHHCCC FCIBICF CCCHHHHHC          ",
                        "         CHHHHCC               CCHHHHC         ", "        CHHHCC                   CCHHHC        ",
                        "       CHHHC                       CHHHC       ", "      CHHHC                         CHHHC      ",
                        "     CHHHC                           CHHHC     ", "    CHHHC                             CHHHC    ",
                        "    CHHHC                             CHHHC    ", "   CHHHC                               CHHHC   ",
                        "   CHHHC                               CHHHC   ", "  CHHHC                                 CHHHC  ",
                        "  CHHHC                                 CHHHC  ", "  CHHHC                                 CHHHC  ",
                        " CHHHC                                   CHHHC ", "FCHHHCF                                 FCHHHCF",
                        "C HHH C                                 C HHH C", "I HHH I                                 I HHH I",
                        "B HHH B                                 B HHH B", "I HHH I                                 I HHH I",
                        "C HHH C                                 C HHH C", "FCHHHCF                                 FCHHHCF",
                        " CHHHC                                   CHHHC ", "  CHHHC                                 CHHHC  ",
                        "  CHHHC                                 CHHHC  ", "  CHHHC                                 CHHHC  ",
                        "   CHHHC                               CHHHC   ", "   CHHHC                               CHHHC   ",
                        "    CHHHC                             CHHHC    ", "    CHHHC                             CHHHC    ",
                        "     CHHHC                           CHHHC     ", "      CHHHC                         CHHHC      ",
                        "       CHHHC                       CHHHC       ", "        CHHHCC                   CCHHHC        ",
                        "         CHHHHCC               CCHHHHC         ", "          CHHHHHCCC FCISICF CCCHHHHHC          ",
                        "           CHHHHHHHCC     CCHHHHHHHC           ", "            CCHHHHHHHHHHHHHHHHHHHCC            ",
                        "              CCHHHHHHHHHHHHHHHCC              ", "                CCCHHHHHHHHHCCC                ",
                        "                   CC     CC                   ", "                    FCIBICF                    ")
                .aisle("                    FCCCCCF                    ",
                        "                   CC     CC                   ", "                CCCCC     CCCCC                ",
                        "              CCCCCHHHHHHHHHCCCCC              ", "            CCCCHHHCC     CCHHHCCCC            ",
                        "           CCCHHCCCCC     CCCCCHHCCC           ", "          ECHHCCCCC FCCCCCF CCCCCHHCE          ",
                        "         CCHCCCC               CCCCHCC         ", "        CCHCCC                   CCCHCC        ",
                        "       CCHCE                       ECHCC       ", "      ECHCC                         CCHCE      ",
                        "     CCHCE                           ECHCC     ", "    CCHCC                             CCHCC    ",
                        "    CCHCC                             CCHCC    ", "   CCHCC                               CCHCC   ",
                        "   CCHCC                               CCHCC   ", "  CCHCC                                 CCHCC  ",
                        "  CCHCC                                 CCHCC  ", "  CCHCC                                 CCHCC  ",
                        " CCHCC                                   CCHCC ", "FCCHCCF                                 FCCHCCF",
                        "C  H  C                                 C  H  C", "C  H  C                                 C  H  C",
                        "C  H  C                                 C  H  C", "C  H  C                                 C  H  C",
                        "C  H  C                                 C  H  C", "FCCHCCF                                 FCCHCCF",
                        " CCHCC                                   CCHCC ", "  CCHCC                                 CCHCC  ",
                        "  CCHCC                                 CCHCC  ", "  CCHCC                                 CCHCC  ",
                        "   CCHCC                               CCHCC   ", "   CCHCC                               CCHCC   ",
                        "    CCHCC                             CCHCC    ", "    CCHCC                             CCHCC    ",
                        "     CCHCE                           ECHCC     ", "      ECHCC                         CCHCE      ",
                        "       CCHCE                       ECHCC       ", "        CCHCCC                   CCCHCC        ",
                        "         CCHCCCC               CCCCHCC         ", "          ECHHCCCCC FCCCCCF CCCCCHHCE          ",
                        "           CCCHHCCCCC     CCCCCHHCCC           ", "            CCCCHHHCC     CCHHHCCCC            ",
                        "              CCCCCHHHHHHHHHCCCCC              ", "                CCCCC     CCCCC                ",
                        "                   CC     CC                   ", "                    FCCCCCF                    ")
                .aisle( "                                               ",
                        "                    FCBBBCF                    ", "                   CC     CC                   ",
                        "                CCCCC     CCCCC                ", "              CCCCCCC     CCCCCCC              ",
                        "            CCCCCCC FCBBBCF CCCCCCC            ", "           CCCCC               CCCCC           ",
                        "          CCCC                   CCCC          ", "         CCC                       CCC         ",
                        "        CCC                         CCC        ", "       CCC                           CCC       ",
                        "      CCC                             CCC      ", "     CCC                               CCC     ",
                        "     CCC                               CCC     ", "    CCC                                 CCC    ",
                        "    CCC                                 CCC    ", "   CCC                                   CCC   ",
                        "   CCC                                   CCC   ", "   CCC                                   CCC   ",
                        "  CCC                                     CCC  ", " FCCCF                                   FCCCF ",
                        " C   C                                   C   C ", " B   B                                   B   B ",
                        " B   B                                   B   B ", " B   B                                   B   B ",
                        " C   C                                   C   C ", " FCCCF                                   FCCCF ",
                        "  CCC                                     CCC  ", "   CCC                                   CCC   ",
                        "   CCC                                   CCC   ", "   CCC                                   CCC   ",
                        "    CCC                                 CCC    ", "    CCC                                 CCC    ",
                        "     CCC                               CCC     ", "     CCC                               CCC     ",
                        "      CCC                             CCC      ", "       CCC                           CCC       ",
                        "        CCC                         CCC        ", "         CCC                       CCC         ",
                        "          CCCC                   CCCC          ", "           CCCCC               CCCCC           ",
                        "            CCCCCCC FCBBBCF CCCCCCC            ", "              CCCCCCC     CCCCCCC              ",
                        "                CCCCC     CCCCC                ", "                   CC     CC                   ",
                        "                    FCBBBCF                    ", "                                               ")
                .aisle("                                               ",
                        "                                               ", "                    FCCCCCF                    ",
                        "                    FCIBICF                    ", "                    FCCCCCF                    ",
                        "                                               ", "                                               ",
                        "                                               ", "                                               ",
                        "                                               ", "                                               ",
                        "                                               ", "                                               ",
                        "                                               ", "                                               ",
                        "                                               ", "                                               ",
                        "                                               ", "                                               ",
                        "                                               ", "  FFF                                     FFF  ",
                        "  CCC                                     CCC  ", "  CIC                                     CIC  ",
                        "  CBC                                     CBC  ", "  CIC                                     CIC  ",
                        "  CCC                                     CCC  ", "  FFF                                     FFF  ",
                        "                                               ", "                                               ",
                        "                                               ", "                                               ",
                        "                                               ", "                                               ",
                        "                                               ", "                                               ",
                        "                                               ", "                                               ",
                        "                                               ", "                                               ",
                        "                                               ", "                                               ",
                        "                                               ", "                    FCCCCCF                    ",
                        "                    FCIBICF                    ", "                    FCCCCCF                    ",
                        "                                               ", "                                               ")
                .where('S', selfPredicate())
                .where('B', states(getGlassState()))
                .where('E',
                        states(getCasingState(), getGlassState()).or(metaTileEntities(Arrays
                                .stream(MetaTileEntities.ENERGY_INPUT_HATCH)
                                .filter(mte -> mte != null && tier <= mte.getTier() && mte.getTier() <= GTValues.UV)
                                .toArray(MetaTileEntity[]::new))
                                .setMinGlobalLimited(1).setPreviewCount(16)))
                .where('C', states(getCasingState()))
                .where('H', states(getCoilState()))
                .where('I', states(getCasingState(), getGlassState()).or(abilities(MultiblockAbility.EXPORT_FLUIDS)))
                .where('A', air())
                .where('F', states(getFrameState()))
                .where('I',
                        states(getCasingState()).or(abilities(MultiblockAbility.IMPORT_FLUIDS).setMinGlobalLimited(2)))
                .where(' ', any())
                .build();
    }

    IBlockState getFrameState() {
        if (tier == GTValues.LuV)
            return MetaBlocks.FRAMES.get(Materials.HSSS).getBlock(Materials.HSSS);
        if (tier == GTValues.ZPM)
            return MetaBlocks.FRAMES.get(Materials.NaquadahAlloy).getBlock(Materials.NaquadahAlloy);

        return MetaBlocks.FRAMES.get(Materials.Neutronium).getBlock(Materials.Neutronium);
    }

}
