package uk.joshiejack.energyoverhaul;

import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Food;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.FoodStats;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import uk.joshiejack.energyoverhaul.events.EatFoodEvent;
import uk.joshiejack.energyoverhaul.handlers.EnergyUse;
import uk.joshiejack.energyoverhaul.network.SyncEnergyLevel;
import uk.joshiejack.energyoverhaul.network.SyncEnergyStats;
import uk.joshiejack.penguinlib.network.PenguinNetwork;

import javax.annotation.Nonnull;
import java.util.Objects;

public class EnergyStats extends FoodStats {
    public static final int ENERGY_PER_HUNGER = 64;
    public static final int FOOD_LEVEL_TO_ENERGY = 8;
    public static final int MAX_OVERALL = ENERGY_PER_HUNGER * 20;
    private int energyMaximum = ENERGY_PER_HUNGER * EOConfig.startingEnergy;
    private int energyLevel = ENERGY_PER_HUNGER * EOConfig.startingEnergy;
    public double maxHearts = EOConfig.startingHealth;
    private PlayerEntity player;

    public EnergyStats(PlayerEntity player) {
        this.player = player;
    }

    public void setPlayer(PlayerEntity player) {
        this.player = player;
    }

    public int getEnergyLevel() {
        return energyLevel;
    }

    @Override
    public int getFoodLevel() { //Scale it back to 20, for other pruposed
        return (int) Math.min(20, (double)energyLevel / (double) energyMaximum * 20);
    }

    public void useEnergy(int energy) {
        energyLevel = Math.max(0, energyLevel - energy);
        if (!player.level.isClientSide)
            PenguinNetwork.sendToClient(new SyncEnergyLevel(energyLevel), (ServerPlayerEntity) player);
    }

    @Override
    public float getSaturationLevel() {
        return getFoodLevel();
    }

    public int getMaxEnergy() {
        return energyMaximum;
    }

    public int getMaxEnergyAsFood() {
        return (int) (((double)energyMaximum / (double)MAX_OVERALL) * 20);
    }

    public boolean increaseMaxHealth() {
        if (maxHearts < 20) {
            maxHearts += 2D;
            if (!player.level.isClientSide) {
                PenguinNetwork.sendToClient(new SyncEnergyStats(this), (ServerPlayerEntity) player);
                Objects.requireNonNull(player.getAttribute(Attributes.MAX_HEALTH)).setBaseValue(maxHearts);
            }

            return true;
        }

        return false;
    }

    public boolean increaseMaxEnergy() {
        if (energyMaximum < MAX_OVERALL) {
            energyMaximum = Math.min(MAX_OVERALL, energyLevel + ENERGY_PER_HUNGER * 2);
            if (!player.level.isClientSide) {
                PenguinNetwork.sendToClient(new SyncEnergyStats(this), (ServerPlayerEntity) player);
            }

            return true;
        }

        return false;
    }

    @Override
    public void eat(Item item, @Nonnull ItemStack stack) {
        if (item.isEdible()) {
            Food food = item.getFoodProperties();
            EatFoodEvent event = new EatFoodEvent(player, stack, food.getNutrition(), food.getSaturationModifier());
            MinecraftForge.EVENT_BUS.post(event);
            eat(event.getNewNutrition(), event.getNewSaturation());
        }
    }

    @Override
    public void eat(int foodLevelIn, float foodSaturationModifier) {
        boolean healing = player.getHealth() < player.getMaxHealth();
        float health = player.getHealth();
        if (healing) player.heal(foodLevelIn * (1F + foodSaturationModifier));
        float newHealth = player.getHealth();

        float totalRestore = ((foodLevelIn * FOOD_LEVEL_TO_ENERGY) * foodSaturationModifier) - (newHealth - health);
        if (totalRestore > 0) {
            energyLevel = (int) Math.min(energyMaximum, energyLevel + totalRestore);
            EnergyUse.updateFatigue(player, false);
        }
    }

    @OnlyIn(Dist.CLIENT)
    public void set(int energyLevel, int energyMaximum, double maxHealth) {
        this.energyLevel = energyLevel;
        this.energyMaximum = Math.max(ENERGY_PER_HUNGER, energyMaximum);
        this.maxHearts = maxHealth;
    }

    @OnlyIn(Dist.CLIENT)
    public void setEnergyLevel(int energyLevel) {
        this.energyLevel = energyLevel;
    }

    @Override
    public void tick(@Nonnull PlayerEntity player) {
    } //No natural loss

    @Override
    public void addExhaustion(float exhaustion) {
        if (EOConfig.ENERGY_FROM_EXHAUSTION)
            useEnergy((int) (exhaustion * 5));
    }

    @Override
    public boolean needsFood() {
        return energyLevel < energyMaximum || player.getHealth() < maxHearts;
    }

    @Override
    public void readAdditionalSaveData(@Nonnull CompoundNBT compound) {
        if (compound.contains("energyLevel", 99))
            energyLevel = compound.getShort("energyLevel");
        if (compound.contains("energyMaximum"))
            energyMaximum = Math.max(ENERGY_PER_HUNGER, compound.getShort("energyMaximum"));
        if (compound.contains("maxHearts"))
            maxHearts = compound.getDouble("maxHearts");
    }

    @Override
    public void addAdditionalSaveData(@Nonnull CompoundNBT compound) {
        compound.putShort("energyLevel", (short) energyLevel);
        compound.putShort("energyMaximum", (short) energyMaximum);
        compound.putDouble("maxHearts", maxHearts);
    }
}
