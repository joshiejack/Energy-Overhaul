package uk.joshiejack.energyoverhaul.handlers;

import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerWakeUpEvent;
import net.minecraftforge.event.entity.player.SleepingTimeCheckEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import uk.joshiejack.energyoverhaul.EOConfig;
import uk.joshiejack.energyoverhaul.EnergyStats;
import uk.joshiejack.energyoverhaul.EnergyOverhaul;

@Mod.EventBusSubscriber(modid = EnergyOverhaul.MODID)
public class Sleep {
    @SubscribeEvent
    public static void onSleepTimeCheck(SleepingTimeCheckEvent event) {
        if (EOConfig.sleepAnytime)
            event.setResult(Event.Result.ALLOW);
    }

    @SubscribeEvent
    public static void onWakeup(PlayerWakeUpEvent event) {
        event.getPlayer().getPersistentData().putLong("LastSlept", event.getPlayer().level.getGameTime()); //Update the last sleeping timer
        EnergyUse.updateEffectInstances(event.getPlayer());
        if (EOConfig.sleepRestoresEnergy) {
            World world = event.getPlayer().level;
            if (!world.isClientSide && world.getGameTime() % 24000 == 0) {
                EnergyStats stats = (EnergyStats) event.getPlayer().getFoodData();
                boolean fatigued = event.getPlayer().hasEffect(EnergyOverhaul.Effects.FATIGUE.get());
                boolean exhausted = event.getPlayer().hasEffect(EnergyOverhaul.Effects.EXHAUSTION.get());
                int restored = (int) (stats.getMaxEnergyAsFood() * (fatigued ? 0.75 : exhausted ? 0.5 : 1));
                event.getPlayer().getFoodData().eat(restored, restored * 5);
            }
        }
    }
}
