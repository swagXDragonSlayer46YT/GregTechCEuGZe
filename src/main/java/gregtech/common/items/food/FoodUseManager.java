package gregtech.common.items.food;

import gregtech.api.items.metaitem.FoodStats;

import net.minecraft.item.ItemStack;

public class FoodUseManager extends gregtech.api.items.metaitem.FoodUseManager {
    public FoodUseManager(FoodStats foodStats) {
        super(foodStats);
    }

    @Override
    public int getMaxItemUseDuration(ItemStack itemStack) {
        return ((FoodStats) this.getFoodStats()).getEatingDuration() * 5 / 4; // You need to extend this so that the animation works correctly :P
    }
}
