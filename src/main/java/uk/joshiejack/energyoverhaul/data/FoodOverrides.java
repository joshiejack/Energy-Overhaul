package uk.joshiejack.energyoverhaul.data;

import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.item.Item;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import uk.joshiejack.energyoverhaul.EnergyOverhaul;
import uk.joshiejack.energyoverhaul.events.EatFoodEvent;
import uk.joshiejack.penguinlib.events.DatabaseLoadedEvent;

@Mod.EventBusSubscriber(modid = EnergyOverhaul.MODID)
public class FoodOverrides {
    private static final Object2ObjectMap<Item, FoodStats> REGISTRY = new Object2ObjectOpenHashMap<>();

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onFoodConsumed(EatFoodEvent event) {
        if (REGISTRY.containsKey(event.getStack().getItem())) {
            FoodStats stats = REGISTRY.get(event.getStack().getItem());
            if (stats.nutrition >= 0)
                event.setNewNutrition(stats.nutrition);
            if (stats.saturation >= 0)
                event.setNewSaturation(stats.saturation);
        }
    }

    @SubscribeEvent
    public static void onDatabaseLoad(DatabaseLoadedEvent event) {
        REGISTRY.clear();
        event.table("food_overrides").rows()
                .forEach(row -> {
                    Item item = row.item();
                    if (item != null)
                        REGISTRY.put(item, new FoodStats(row.getAsInt("nutrition"), row.getAsFloat("saturation")));
                });
    }

    private static class FoodStats {
        private int nutrition;
        private float saturation;

        private FoodStats(int nutrition, float saturation) {
            this.nutrition = nutrition;
            this.saturation = saturation;
        }
    }
}
