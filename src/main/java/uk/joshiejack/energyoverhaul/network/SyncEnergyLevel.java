package uk.joshiejack.energyoverhaul.network;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.network.NetworkDirection;
import uk.joshiejack.energyoverhaul.EnergyStats;
import uk.joshiejack.penguinlib.network.PenguinPacket;
import uk.joshiejack.penguinlib.util.PenguinLoader;

@PenguinLoader.Packet(NetworkDirection.PLAY_TO_CLIENT)
public class SyncEnergyLevel extends PenguinPacket {
    private int energyLevel;

    public SyncEnergyLevel() {}
    public SyncEnergyLevel(int energyLevel) {
        this.energyLevel = energyLevel;
    }

    @Override
    public void encode(PacketBuffer to) {
        to.writeShort(energyLevel);
    }

    @Override
    public void decode(PacketBuffer from) {
        energyLevel = from.readShort();
    }

    @Override
    public void handle(PlayerEntity player) {
        EnergyStats stats = player.getFoodData() instanceof EnergyStats ? (EnergyStats) player.getFoodData() : new EnergyStats(player);
        if (player.getFoodData() != stats)
            ObfuscationReflectionHelper.setPrivateValue(PlayerEntity.class, player, stats, "field_71100_bB");
        stats.setEnergyLevel(energyLevel);
    }
}
