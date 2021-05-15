package uk.joshiejack.energyoverhaul.network;

import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.network.NetworkDirection;
import uk.joshiejack.energyoverhaul.EnergyStats;
import uk.joshiejack.penguinlib.network.PenguinPacket;
import uk.joshiejack.penguinlib.util.PenguinLoader;

@PenguinLoader.Packet(NetworkDirection.PLAY_TO_CLIENT)
public class SyncEnergyStats extends PenguinPacket {
    private int energyLevel;
    private int energyMaximum;
    private double maxHealth;

    public SyncEnergyStats() {}
    public SyncEnergyStats(EnergyStats stats) {
        energyLevel = stats.getEnergyLevel();
        energyMaximum = stats.getMaxEnergy();
        maxHealth = stats.maxHearts;
    }

    @Override
    public void encode(PacketBuffer to) {
        to.writeShort(energyLevel);
        to.writeShort(energyMaximum);
        to.writeDouble(maxHealth);
    }

    @Override
    public void decode(PacketBuffer from) {
        energyLevel = from.readShort();
        energyMaximum = from.readShort();
        maxHealth = from.readDouble();
    }

    @Override
    public void handle(PlayerEntity player) {
        EnergyStats stats = player.getFoodData() instanceof EnergyStats ? (EnergyStats) player.getFoodData() : new EnergyStats(player);
        if (player.getFoodData() != stats)
            ObfuscationReflectionHelper.setPrivateValue(PlayerEntity.class, player, stats, "field_71100_bB");
        stats.set(energyLevel, energyMaximum, maxHealth);
        player.getAttribute(Attributes.MAX_HEALTH).setBaseValue(stats.maxHearts);
    }
}
