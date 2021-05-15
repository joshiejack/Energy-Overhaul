//package uk.joshiejack.energyoverhaul.client;
//
//import com.mojang.blaze3d.systems.RenderSystem;
//import net.minecraft.client.Minecraft;
//import net.minecraft.client.gui.Gui;
//import net.minecraft.client.gui.ScaledResolution;
//import net.minecraft.client.gui.screen.Screen;
//import net.minecraft.entity.player.PlayerEntity;
//import net.minecraftforge.api.distmarker.Dist;
//import net.minecraftforge.api.distmarker.OnlyIn;
//import net.minecraftforge.client.event.RenderGameOverlayEvent;
//import net.minecraftforge.eventbus.api.EventPriority;
//import net.minecraftforge.eventbus.api.SubscribeEvent;
//import net.minecraftforge.fml.common.Mod;
//import uk.joshiejack.energyoverhaul.EOEffects;
//import uk.joshiejack.energyoverhaul.EnergyOverhaul;
//
//import java.awt.*;
//
//@OnlyIn(Dist.CLIENT)
//@Mod.EventBusSubscriber(modid = EnergyOverhaul.MODID, value = Dist.CLIENT)
//public class TiredEvents {
//    private static Color color = new Color(0, 0, 0, 0);
//    private static int FADE_IN_OUT;
//    private static boolean INCREASE = true;
//    private static boolean BLINK = false;
//
//    @SuppressWarnings("ConstantConditions")
//    @SubscribeEvent(priority = EventPriority.HIGHEST)
//    public static void renderTiredness(RenderGameOverlayEvent.Pre event) {
//        if (event.getType() == RenderGameOverlayEvent.ElementType.ALL) {
//            Minecraft mc = Minecraft.getInstance();
//            PlayerEntity player = mc.player;
//            if (!player.isCreative() && player.hasEffect(EOEffects.TIRED.get())) {
//                if (player.level.getGameTime() %200 == 0) BLINK = true;
//
//                if (BLINK) {
//                    if (INCREASE) {
//                        FADE_IN_OUT++;
//                    } else FADE_IN_OUT--;
//
//
//                    color = new Color(0, 0, 0, Math.min(255, FADE_IN_OUT / 2));
//                    if (FADE_IN_OUT > (255 * 2.5) || FADE_IN_OUT < 0) {
//                        INCREASE = !INCREASE;
//                        if (FADE_IN_OUT <= 0) {
//                            BLINK = false;
//                        }
//                    }
//
//                    RenderSystem.pushMatrix();
//                    RenderSystem.depthMask(false);
//                    Screen.blit(event.getMatrixStack(), 0, 0, event.getWindow().getGuiScaledWidth(), event.getWindow().getGuiScaledHeight(), color.getRGB());
//                    RenderSystem.depthMask(true);
//                    //mc.getTextureManager().bindTexture(Gui.ICONS); //rebind the old texture
//                    RenderSystem.popMatrix();
//                }
//            }
//        }
//    }
//}
