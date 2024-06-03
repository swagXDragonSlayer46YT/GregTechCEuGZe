package gregtech.api.items.metaitem;

import gregtech.api.GTValues;
import gregtech.api.items.metaitem.stats.IFoodBehavior;
import gregtech.api.items.metaitem.stats.IItemBehaviour;
import gregtech.api.util.GTUtility;
import gregtech.api.util.RandomPotionEffect;

import gregtech.common.potion.LacingEntry;

import it.unimi.dsi.fastutil.objects.Object2FloatArrayMap;
import it.unimi.dsi.fastutil.objects.Object2FloatMap;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;

import net.minecraft.util.text.TextComponentTranslation;

import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

/**
 * Simple {@link gregtech.api.items.metaitem.stats.IFoodBehavior} implementation
 *
 * @see gregtech.api.items.metaitem.MetaItem
 */
public class FoodStats implements IFoodBehavior, IItemBehaviour {

    public final int foodLevel;
    public final float saturation;
    public final boolean isDrink;
    public final boolean alwaysEdible;
    public RandomPotionEffect[] potionEffects;
    public Supplier<ItemStack> stackSupplier;
    public Object2FloatMap<String> nutrients = new Object2FloatArrayMap<>();
    protected int eatingDuration = 32;

    @Nullable
    public ItemStack containerItem;

    public FoodStats(int foodLevel, float saturation, boolean isDrink, boolean alwaysEdible, ItemStack containerItem,
                     RandomPotionEffect... potionEffects) {
        this.foodLevel = foodLevel;
        this.saturation = saturation;
        this.isDrink = isDrink;
        this.alwaysEdible = alwaysEdible;
        if (containerItem != null) {
            this.containerItem = containerItem.copy();
        }
        this.potionEffects = potionEffects;
    }

    public FoodStats(int foodLevel, float saturation, boolean isDrink) {
        this(foodLevel, saturation, isDrink, false, null);
    }

    public FoodStats(int foodLevel, float saturation) {
        this(foodLevel, saturation, false);
    }

    @Override
    public int getFoodLevel(ItemStack itemStack, EntityPlayer player) {
        return foodLevel;
    }

    @Override
    public float getSaturation(ItemStack itemStack, EntityPlayer player) {
        return saturation;
    }

    @Override
    public boolean alwaysEdible(ItemStack itemStack, EntityPlayer player) {
        return alwaysEdible;
    }

    @Override
    public EnumAction getFoodAction(ItemStack itemStack) {
        return isDrink ? EnumAction.DRINK : EnumAction.EAT;
    }

    public FoodStats setEatingDuration(int duration) {
        this.eatingDuration = duration;
        return this;
    }

    public FoodStats setPotionEffects(RandomPotionEffect... effects) {
        this.potionEffects = effects;
        return this;
    }

    public int getEatingDuration() {
        return eatingDuration;
    }

    public RandomPotionEffect[] getPotionEffects() {
        return potionEffects;
    }

    public Supplier<ItemStack> getStackSupplier() {
        return stackSupplier;
    }

    public FoodStats setReturnStack(ItemStack stack) {
        this.stackSupplier = () -> stack;
        return this;
    }

    @Override
    public ItemStack onFoodEaten(ItemStack itemStack, EntityPlayer player) {
        if (!player.world.isRemote) {
            for (RandomPotionEffect potionEffect : potionEffects) {
                if (GTValues.RNG.nextDouble() * 100 > potionEffect.chance) {
                    player.addPotionEffect(new PotionEffect(potionEffect.effect));
                }
            }

            if (containerItem != null) {
                ItemStack containerItemCopy = containerItem.copy(); // Get the copy
                if (!player.capabilities.isCreativeMode) {
                    if (itemStack.isEmpty()) {
                        return containerItemCopy;
                    }

                    if (!player.inventory.addItemStackToInventory(containerItemCopy))
                        player.dropItem(containerItemCopy, false, false);
                }
            }

            NBTTagCompound nbtStats = itemStack.getSubCompound("gtfoStats");

            if (nbtStats != null) {
                LacingEntry.LACING_REGISTRY.forEach(lacingEntry -> {
                    if (nbtStats.getBoolean(lacingEntry.getNbtKey())) {
                        player.addPotionEffect(lacingEntry.getAppliedEffect());
                    }
                });
            }
        }
        return itemStack;
    }

    @Override
    public void addInformation(ItemStack itemStack, List<String> lines) {
        lines.add(new TextComponentTranslation("gregtech.tooltip.food.lacing").getFormattedText());

        if (this.eatingDuration != 32) {
            lines.add(new TextComponentTranslation("gregtech.tooltip.food.duration", this.eatingDuration).getFormattedText());
        }

        if (potionEffects.length > 0) {
            PotionEffect[] effects = new PotionEffect[potionEffects.length];
            for (int i = 0; i < potionEffects.length; i++) {
                effects[i] = potionEffects[i].effect;
            }

            GTUtility.addPotionTooltip(Arrays.asList(potionEffects), lines);
        }
    }

    public FoodStats nutrients(float dairy, float fruit, float grain, float protein, float vegetable) {
        if (dairy > 0) {
            this.nutrients.put("dairy", dairy);
        }
        if (fruit > 0) {
            this.nutrients.put("fruit", fruit);
        }
        if (grain > 0) {
            this.nutrients.put("grain", grain);
        }
        if (protein > 0) {
            this.nutrients.put("protein", protein);
        }
        if (vegetable > 0) {
            this.nutrients.put("vegetable", vegetable);
        }
        return this;
    }
}
