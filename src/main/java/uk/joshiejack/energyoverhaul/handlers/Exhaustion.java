package uk.joshiejack.energyoverhaul.handlers;

import net.minecraft.item.Item;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import uk.joshiejack.energyoverhaul.EnergyOverhaul;

@Mod.EventBusSubscriber(modid = EnergyOverhaul.MODID)
public class Exhaustion {
    public static final ITag.INamedTag<Item> REQUIRES_ENERGY = ItemTags.createOptional(new ResourceLocation(EnergyOverhaul.MODID, "requires_energy"));

    @SubscribeEvent
    public static void onInteract(PlayerInteractEvent.RightClickBlock event) {
        if (event.getPlayer().hasEffect(EnergyOverhaul.Effects.EXHAUSTION.get()) && REQUIRES_ENERGY.contains(event.getItemStack().getItem()))
            event.setCanceled(true);
    }

    @SubscribeEvent
    public static void onInteract(PlayerInteractEvent.RightClickItem event) {
        if (event.getPlayer().hasEffect(EnergyOverhaul.Effects.EXHAUSTION.get())  && !event.getItemStack().getItem().isEdible()
                && REQUIRES_ENERGY.contains(event.getItemStack().getItem()))
            event.setCanceled(true);
    }

    @SubscribeEvent
    public static void onInteract(PlayerInteractEvent.EntityInteract event) {
        if (event.getPlayer().hasEffect(EnergyOverhaul.Effects.EXHAUSTION.get()) && REQUIRES_ENERGY.contains(event.getItemStack().getItem()))
            event.setCanceled(true);
    }
}
