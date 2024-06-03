package gregtech.common.potion;

import gregtech.api.GTValues;

import net.minecraft.potion.Potion;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = GTValues.MODID)
public class GTPotions {
    public static final List<GTPotion> POTIONS = new ArrayList<>();

    public static void initPotionInstances()
    {
        new CyanidePoisoningPotion();
    }

    @SubscribeEvent
    public static void registerPotionEffects(RegistryEvent.Register<Potion> event)
    {
        for (GTPotion potion : POTIONS)
        {
            event.getRegistry().register(potion);
        }
    }
}
