package uk.joshiejack.energyoverhaul.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.Effects;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.gui.ForgeIngameGui;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import uk.joshiejack.energyoverhaul.EnergyStats;
import uk.joshiejack.energyoverhaul.EnergyOverhaul;

import static net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType.FOOD;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = EnergyOverhaul.MODID, value = Dist.CLIENT)
public class HungerRender {
    private static EnergyStats stats;

    @SuppressWarnings("unused")
    @SubscribeEvent
    public static void onHungerRender(RenderGameOverlayEvent.Pre event) {
        if (event.getType() == RenderGameOverlayEvent.ElementType.FOOD) {
            Minecraft minecraft = Minecraft.getInstance();
            minecraft.getProfiler().push("food");
            PlayerEntity player = (PlayerEntity)minecraft.getCameraEntity();
            int width = event.getWindow().getGuiScaledWidth();
            int height = event.getWindow().getGuiScaledHeight();
            RenderSystem.enableBlend();
            int left = width / 2 + 91;
            int top = height -ForgeIngameGui.right_height;
            ForgeIngameGui.right_height += 10;
            boolean unused = false;// Unused flag in vanilla, seems to be part of a 'fade out' mechanic

            stats = minecraft.player.getFoodData() instanceof EnergyStats ? (EnergyStats) minecraft.player.getFoodData() : stats;
            int level = (int) Math.min(stats.getMaxEnergy(), (double)stats.getEnergyLevel() / (double) stats.getMaxEnergy() * stats.getMaxEnergyAsFood());

            for (int i = 0; i < stats.getMaxEnergyAsFood() / 2; ++i)
            {
                int idx = i * 2 + 1;
                int x = left - i * 8 - 9;
                int y = top;
                int icon = 16;
                byte background = 0;

                if (minecraft.player.hasEffect(Effects.HUNGER))
                {
                    icon += 36;
                    background = 13;
                }
                if (unused) background = 1; //Probably should be a += 1 but vanilla never uses this

                if (player.getFoodData().getSaturationLevel() <= 0.0F && minecraft.gui.getGuiTicks() % (level * 3 + 1) == 0)
                {
                    y = top + (player.getRandom().nextInt(3) - 1);
                }

                minecraft.gui.blit(event.getMatrixStack(), x, y, 16 + background * 9, 27, 9, 9);

                if (idx < level)
                    minecraft.gui.blit(event.getMatrixStack(), x, y, icon + 36, 27, 9, 9);
                else if (idx == level)
                    minecraft.gui.blit(event.getMatrixStack(), x, y, icon + 45, 27, 9, 9);
            }
            RenderSystem.disableBlend();
            minecraft.getProfiler().pop();
            MinecraftForge.EVENT_BUS.post(new RenderGameOverlayEvent.Post(event.getMatrixStack(),
                    new RenderGameOverlayEvent(event.getMatrixStack(), event.getPartialTicks(), minecraft.getWindow()), FOOD));
            event.setCanceled(true);
        }

//
//
//        //minecraft.getProfiler().push("food");
//        PlayerEntity player = (PlayerEntity) minecraft.getCameraEntity();
//        MatrixStack mStack = event.getMatrixStack();
//        int width = event.getWindow().getGuiScaledWidth();
//        int height = event.getWindow().getGuiScaledHeight();
//        stats = minecraft.player.getFoodData() instanceof EOFoodStats ? (EOFoodStats) minecraft.player.getFoodData() : stats;
//        if (stats != null) {
//            RenderSystem.enableBlend();
//            int left = width / 2 + 91;
//            int top = height;// - right_height;
//            //right_height += 10;
//            boolean unused = false;// Unused flag in vanilla, seems to be part of a 'fade out' mechanic
//
//            int level = stats.getEnergyLevel();
//            for (int i = 0; i < stats.getMaxFoodLevel() / 2; ++i) {
//                int idx = i * 2 + 1;
//                int x = left - i * 8 - 9;
//                int y = top;
//                int icon = 16;
//                byte background = 0;
//
//                if (minecraft.player.hasEffect(Effects.HUNGER)) {
//                    icon += 36;
//                    background = 13;
//                }
//
//                if (unused) background = 1; //Probably should be a += 1 but vanilla never uses this
//
//                if (player.getFoodData().getSaturationLevel() <= 0.0F && minecraft.gui.getGuiTicks() % (level * 3 + 1) == 0) {
//                    y = top + (player.getRandom().nextInt(3) - 1);
//                }
//
//                minecraft.gui.blit(mStack, x, y, 16 + background * 9, 27, 9, 9);
//
//                if (idx < level)
//                    minecraft.gui.blit(mStack, x, y, icon + 36, 27, 9, 9);
//                else if (idx == level)
//                    minecraft.gui.blit(mStack, x, y, icon + 45, 27, 9, 9);
//            }
//
//            RenderSystem.disableBlend();
            //minecraft.getProfiler().pop();
            //event.setCanceled(true);
        }
//
//        if (event.getType() == ElementType.FOOD) {
//            Minecraft mc = Minecraft.getMinecraft();
//            int width = event.getWindow().getGuiScaledWidth();
//            int height = event.getWindow().getGuiScaledHeight();
//            mc.getProfiler().push("food");
//            stats = mc.player.getFoodStats() instanceof EOFoodStats ? (EOFoodStats) mc.player.getFoodStats() : stats;
//            if (stats != null) {
//                PlayerEntity player = (EntityPlayer) mc.getRenderViewEntity();
//                RenderSystem.enableBlend();
//                int left = width / 2 + 91;
//                int top = height - right_height;
//                //right_height += 10;
//
//                int level = stats.getFoodLevel();
//                for (int i = 0; i < stats.maxFoodDisplay / 2; ++i) {
//                    int idx = i * 2 + 1;
//                    int x = left - i * 8 - 9;
//                    int y = top;
//                    int icon = 16;
//                    byte background = 0;
//
//                    if (mc.player.isPotionActive(MobEffects.HUNGER)) {
//                        icon += 36;
//                        background = 13;
//                    }
//
//                    rand.setSeed(mc.ingameGUI.getUpdateCounter() * 312871L);
//                    if (stats.getSaturationLevel() <= 0.0F && mc.ingameGUI.getUpdateCounter() % (level * 3 + 1) == 0) {
//                        y = top + (rand.nextInt(3) - 1);
//                    }
//
//                    mc.ingameGUI.drawTexturedModalRect(x, y, 16 + background * 9, 27, 9, 9);
//
//                    if (idx < level)
//                        mc.ingameGUI.drawTexturedModalRect(x, y, icon + 36, 27, 9, 9);
//                    else if (idx == level)
//                        mc.ingameGUI.drawTexturedModalRect(x, y, icon + 45, 27, 9, 9);
//                }
//
//                RenderSystem.disableBlend();
//                mc.getProfiler().pop();
//                MinecraftForge.EVENT_BUS.post(new RenderGameOverlayEvent.Post(event.getMatrixStack(),
//                        new RenderGameOverlayEvent(event.getMatrixStack(), event.getPartialTicks(), event.getWindow()), ElementType.FOOD));
//            }
//        }

}
