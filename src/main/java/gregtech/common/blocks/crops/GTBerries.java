package gregtech.common.blocks.crops;

import gregtech.common.worldgen.GTFeature;
import gregtech.common.worldgen.TemperatureRainfallCondition;

public class GTBerries {
    public static GTFeature BUSH_BLUEBERRY = new GTBerry(1000, GTCrops.BUSH_BLUEBERRY)
            .addCondition(new TemperatureRainfallCondition(3, 1.2, 0.7, 0.5, 0.5));
    public static GTFeature BUSH_BLACKBERRY = new GTBerry(1001, GTCrops.BUSH_BLACKBERRY)
            .addCondition(new TemperatureRainfallCondition(3, 1.2, 0.5, 0.4, 0.5));
    public static GTFeature BUSH_RASPBERRY = new GTBerry(1002, GTCrops.BUSH_RASPBERRY)
            .addCondition(new TemperatureRainfallCondition(3, 1.2, 0.5, 0.5, 0.4));
    public static GTFeature BUSH_STRAWBERRY = new GTBerry(1003, GTCrops.BUSH_STRAWBERRY)
            .addCondition(new TemperatureRainfallCondition(3, 1.2, 0.7, 0.8, 0.5));
    public static GTFeature BUSH_RED_CURRANT = new GTBerry(1004, GTCrops.BUSH_RED_CURRANT)
            .addCondition(new TemperatureRainfallCondition(3, 0.9, 0.3, 0.75, 0.5));
    public static GTFeature BUSH_BLACK_CURRANT = new GTBerry(1005, GTCrops.BUSH_BLACK_CURRANT)
            .addCondition(new TemperatureRainfallCondition(3, 0.9, 0.3, 0.75, 0.5));
    public static GTFeature BUSH_WHITE_CURRANT = new GTBerry(1006, GTCrops.BUSH_WHITE_CURRANT)
            .addCondition(new TemperatureRainfallCondition(3, 0.9, 0.3, 0.75, 0.5));
    public static GTFeature BUSH_LINGONBERRY = new GTBerry(1007, GTCrops.BUSH_LINGONBERRY)
            .addCondition(new TemperatureRainfallCondition(3, 0.9, 0.25, 0.7, 0.6));
    public static GTFeature BUSH_ELDERBERRY = new GTBerry(1008, GTCrops.BUSH_ELDERBERRY)
            .addCondition(new TemperatureRainfallCondition(3, 0.9, 0.2, 0.4, 0.6));
    public static GTFeature BUSH_CRANBERRY = new GTBerry(1009, GTCrops.BUSH_CRANBERRY)
            .addCondition(new TemperatureRainfallCondition(3, 1.2, 0.2, 0.4, 0.6));

    public static void init() {

    }
}
