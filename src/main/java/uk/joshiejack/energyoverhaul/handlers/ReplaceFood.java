package uk.joshiejack.energyoverhaul.handlers;

import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.world.GameRules;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import uk.joshiejack.energyoverhaul.EOConfig;
import uk.joshiejack.energyoverhaul.EnergyStats;
import uk.joshiejack.energyoverhaul.EnergyOverhaul;
import uk.joshiejack.energyoverhaul.network.SyncEnergyStats;
import uk.joshiejack.penguinlib.network.PenguinNetwork;

@SuppressWarnings("WeakerAccess")
@Mod.EventBusSubscriber(modid = EnergyOverhaul.MODID)
public class ReplaceFood {
    @SuppressWarnings("unused")
    @SubscribeEvent
    public static void onWorldCreated(WorldEvent.Load event) {
        if (EOConfig.forceNaturalRegenDisabling && !event.getWorld().isClientSide())
            ((ServerWorld)event.getWorld()).getGameRules().getRule(GameRules.RULE_NATURAL_REGENERATION)
                    .set(false, ((ServerWorld)event.getWorld()).getServer());
    }

    @SubscribeEvent
    public static void respawnEvent(PlayerEvent.PlayerRespawnEvent event) {
        if (!event.getPlayer().level.isClientSide)
            replaceFoodStats(event.getPlayer());
    }

    @SuppressWarnings("unused")
    @SubscribeEvent
    public static void replaceFoodStats(EntityJoinWorldEvent event) {
        if (!event.getEntity().level.isClientSide && event.getEntity() instanceof PlayerEntity)
            replaceFoodStats((PlayerEntity) event.getEntity());
    }

    protected static void replaceFoodStats(PlayerEntity player) {
        if (!(player.getFoodData() instanceof EnergyStats)) {
            EnergyStats stats = new EnergyStats(player);
            stats.readAdditionalSaveData(player.getPersistentData()); //Read in the instance we saved
            player.getAttribute(Attributes.MAX_HEALTH).setBaseValue(stats.maxHearts);
            ObfuscationReflectionHelper.setPrivateValue(PlayerEntity.class, player, stats, "field_71100_bB");
        }

        PenguinNetwork.sendToClient(new SyncEnergyStats((EnergyStats)player.getFoodData()), (ServerPlayerEntity) player);
    }

    @SuppressWarnings("unused")
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onCloning(PlayerEvent.Clone event) {
        ObfuscationReflectionHelper.setPrivateValue(PlayerEntity.class, event.getPlayer(), event.getOriginal().getFoodData(), "field_71100_bB");
        if (event.getPlayer().getFoodData() instanceof EnergyStats) {
            ((EnergyStats)event.getPlayer().getFoodData()).setPlayer(event.getPlayer());
        }
    }

    @SuppressWarnings("unused")
    @SubscribeEvent
    public static void saveFoodStats(PlayerEvent.PlayerLoggedOutEvent event) {
        if (event.getPlayer().getFoodData() instanceof EnergyStats) {
            event.getPlayer().getFoodData().addAdditionalSaveData(event.getPlayer().getPersistentData());
        }
    }

    @SubscribeEvent
    public static void onPlayerTakenDamage(LivingAttackEvent event) {
        if (event.getSource() == DamageSource.STARVE) event.setCanceled(true);
    }
}
