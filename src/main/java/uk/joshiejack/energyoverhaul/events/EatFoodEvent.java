package uk.joshiejack.energyoverhaul.events;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerEvent;

public class EatFoodEvent extends PlayerEvent {
    private final ItemStack stack;
    private final int nutrition;
    private final float saturation;
    private int newNutrition;
    private float newSaturation;

    public EatFoodEvent(PlayerEntity player, ItemStack stack, int nutritionAmount, float saturationModifier) {
        super(player);
        this.stack = stack;
        this.nutrition = nutritionAmount;
        this.newNutrition = nutritionAmount;
        this.saturation = saturationModifier;
        this.newSaturation = saturationModifier;
    }

    public ItemStack getStack() {
        return stack;
    }

    public int getNutrition() {
        return nutrition;
    }

    public float getSaturation() {
        return saturation;
    }

    public int getNewNutrition() {
        return newNutrition;
    }

    public float getNewSaturation() {
        return newSaturation;
    }

    public void setNewNutrition(int newHealth) {
        this.newNutrition = newHealth;
    }

    public void setNewSaturation(float newSaturation) {
        this.newSaturation = newSaturation;
    }
}

