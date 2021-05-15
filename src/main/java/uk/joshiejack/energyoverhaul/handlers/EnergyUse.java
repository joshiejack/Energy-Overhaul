package uk.joshiejack.energyoverhaul.handlers;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.HoeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.event.entity.player.ItemFishedEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import uk.joshiejack.energyoverhaul.EnergyStats;
import uk.joshiejack.energyoverhaul.EnergyOverhaul;
import uk.joshiejack.penguinlib.util.PenguinTags;

import java.util.Set;

@SuppressWarnings("WeakerAccess, unused")
@Mod.EventBusSubscriber(modid = EnergyOverhaul.MODID)
public class EnergyUse {
    private static final int LOW = 1;
    private static final int MEDIUM = 4;
    private static final int HIGH = 8;

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onBlockBroken(BlockEvent.BreakEvent event) {
        PlayerEntity player = event.getPlayer();
        if (player != null)
            if (isHoldingBlockBreakingTool(player)) {
                if (Tags.Blocks.ORES.contains(event.getState().getBlock()))
                    consumeEnergy(player, HIGH);
                else if (Tags.Blocks.STONE.contains(event.getState().getBlock()) || Tags.Blocks.DIRT.contains(event.getState().getBlock()))
                    consumeEnergy(player, LOW);
                else
                    consumeEnergy(player, MEDIUM);
            } else
                consumeEnergy(player, LOW);
    }

    private static boolean isHoldingBlockBreakingTool(PlayerEntity player) {
        ItemStack stack = player.getMainHandItem();
        Item item = stack.getItem();
        Set<ToolType> tools = stack.getToolTypes();
        return tools.contains(ToolType.AXE) || tools.contains(ToolType.SHOVEL) || tools.contains(ToolType.PICKAXE) ||
                PenguinTags.HAMMERS.contains(item) || PenguinTags.SICKLES.contains(item) || PenguinTags.SCYTHES.contains(item);
     }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onBlockPlaced(BlockEvent.EntityPlaceEvent event) {
        PlayerEntity player = event.getEntity() instanceof PlayerEntity ? (PlayerEntity) event.getEntity() : null;
        if (player != null && isHoldingWateringCanOrHoe(player))
            consumeEnergy(player, MEDIUM);
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onMultiPlaced(BlockEvent.EntityMultiPlaceEvent event) {
        PlayerEntity player = event.getEntity() instanceof PlayerEntity ? (PlayerEntity) event.getEntity() : null;
        if (player != null && isHoldingWateringCanOrHoe(player))
            consumeEnergy(player, MEDIUM * event.getReplacedBlockSnapshots().size());
    }

    private static boolean isHoldingWateringCanOrHoe(PlayerEntity player) {
        return isWateringCanOrHoe(player.getMainHandItem().getItem()) || isWateringCanOrHoe(player.getOffhandItem().getItem());
    }

    private static boolean isWateringCanOrHoe(Item item) {
        return item instanceof HoeItem || PenguinTags.HOES.contains(item) || PenguinTags.WATERING_CANS.contains(item);
    }

    @SubscribeEvent
    public static void onFishing(ItemFishedEvent event) {
        consumeEnergy(event.getPlayer(), HIGH);
    }

    private static void consumeEnergy(PlayerEntity player, int amount) {
        if (!player.isCreative())
            ((EnergyStats)player.getFoodData()).useEnergy(amount);
    }

    public static void updateTiredness(PlayerEntity player) {
        if (player.isCreative() && player.hasEffect(EnergyOverhaul.Effects.TIRED.get())) {
            player.removeEffect(EnergyOverhaul.Effects.TIRED.get());
        } else {
            //Add the last slept tag
            if (!player.getPersistentData().contains("LastSlept")) {
                player.getPersistentData().putLong("LastSlept", player.level.getGameTime());
            }

            long slept = player.getPersistentData().getLong("LastSlept"); //If we've been awake for over 16 hours, make us tired
            if (player.level.getGameTime() - slept > 16000L && !player.hasEffect(EnergyOverhaul.Effects.BUZZED.get())) {
                player.addEffect(new EffectInstance(EnergyOverhaul.Effects.TIRED.get(), Integer.MAX_VALUE)); //Unlimited
            } else if (player.hasEffect(EnergyOverhaul.Effects.TIRED.get())) player.removeEffect(EnergyOverhaul.Effects.TIRED.get());
        }
    }

    public static void updateFatigue(PlayerEntity player, boolean expiring) {
        boolean fatigued = player.hasEffect(EnergyOverhaul.Effects.FATIGUE.get());
        boolean exhausted = player.hasEffect(EnergyOverhaul.Effects.EXHAUSTION.get());
        if (!player.hasEffect(EnergyOverhaul.Effects.BUZZED.get())) {
            int energy = ((EnergyStats)player.getFoodData()).getEnergyLevel();
            if (energy <= 2 * EnergyStats.ENERGY_PER_HUNGER) {
                if (!fatigued && !exhausted && energy > 0)
                    player.addEffect(new EffectInstance(EnergyOverhaul.Effects.FATIGUE.get(), 600));
                if ((expiring && energy <= 1) || (!fatigued && energy <= 0))
                    player.addEffect(new EffectInstance(EnergyOverhaul.Effects.EXHAUSTION.get(), Integer.MAX_VALUE));
            } else if (fatigued) {
                player.removeEffect(EnergyOverhaul.Effects.FATIGUE.get());
            } else if (exhausted) {
                player.removeEffect(EnergyOverhaul.Effects.EXHAUSTION.get());
            }
        } else {
            if (fatigued) player.removeEffect(EnergyOverhaul.Effects.FATIGUE.get());
            if (exhausted) player.removeEffect(EnergyOverhaul.Effects.EXHAUSTION.get());
        }
    }

    public static void updateEffectInstances(PlayerEntity player) {
        if (player.isCreative()) {
            player.removeEffect(EnergyOverhaul.Effects.TIRED.get());
            player.removeEffect(EnergyOverhaul.Effects.FATIGUE.get());
            player.removeEffect(EnergyOverhaul.Effects.EXHAUSTION.get());
        } else {
            updateTiredness(player);
            updateFatigue(player, false);
        }
    }
}
