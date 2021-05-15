package uk.joshiejack.energyoverhaul;

import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import uk.joshiejack.energyoverhaul.data.EOBlockTags;
import uk.joshiejack.energyoverhaul.data.EODatabase;
import uk.joshiejack.energyoverhaul.data.EOItemTags;
import uk.joshiejack.energyoverhaul.data.EOLanguage;
import uk.joshiejack.penguinlib.potion.IncurableEffect;
import uk.joshiejack.penguinlib.potion.IncurableExpiringEffect;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
@Mod(EnergyOverhaul.MODID)
public class EnergyOverhaul {
    public static final String MODID = "energyoverhaul";

    public EnergyOverhaul() {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        Effects.EFFECTS.register(eventBus);
        Sounds.SOUNDS.register(eventBus);
    }

    @SubscribeEvent
    public static void onDataGathering(final GatherDataEvent event) {
        final DataGenerator generator = event.getGenerator();
        if (event.includeServer()) {
            generator.addProvider(new EODatabase(generator));
            BlockTagsProvider blockTags = new EOBlockTags(generator, event.getExistingFileHelper());
            generator.addProvider(blockTags);
            generator.addProvider(new EOItemTags(generator, blockTags, event.getExistingFileHelper()));
        }

        if (event.includeClient())
            generator.addProvider(new EOLanguage(generator));
    }

    public static class Sounds {
        public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, MODID);
        public static final RegistryObject<SoundEvent> YAWN = createSoundEvent("yawn");

        private static RegistryObject<SoundEvent> createSoundEvent(String name) {
            return SOUNDS.register(name, () -> new SoundEvent(new ResourceLocation(MODID, name)));
        }
    }

    public static class Effects {
        public static final DeferredRegister<Effect> EFFECTS = DeferredRegister.create(ForgeRegistries.POTIONS, MODID);
        public static final RegistryObject<Effect> TIRED = EFFECTS.register("tired", () -> new IncurableEffect(EffectType.NEUTRAL, 0x666666)
                .addAttributeModifier(Attributes.ATTACK_DAMAGE, "14A10B84-0FE6-4ED3-AB73-E8D1001E41E3", -0.2, AttributeModifier.Operation.ADDITION)
                .addAttributeModifier(Attributes.ATTACK_SPEED, "7B08B09B-CBDB-4188-9DF2-5F459A0F1B54", -0.2, AttributeModifier.Operation.MULTIPLY_TOTAL));

        public static final RegistryObject<Effect> FATIGUE = EFFECTS.register("fatigue", () -> new IncurableExpiringEffect(EffectType.NEUTRAL, 0xD9D900)
                .addAttributeModifier(Attributes.ATTACK_DAMAGE, "CF71B5F5-172F-45C1-A5AD-E7102734319E", -0.1, AttributeModifier.Operation.ADDITION)
                .addAttributeModifier(Attributes.ATTACK_SPEED, "076C5F29-43EA-4C6B-A30E-1F4316ECFA54", -0.15, AttributeModifier.Operation.MULTIPLY_TOTAL));

        public static final RegistryObject<Effect> EXHAUSTION = EFFECTS.register("exhaustion", () -> new IncurableEffect(EffectType.NEUTRAL, 0xD9D900)
                .addAttributeModifier(Attributes.ATTACK_DAMAGE, "9DA5E6D1-5B23-4270-B66A-1F8511B34FF4", -0.5, AttributeModifier.Operation.ADDITION)
                .addAttributeModifier(Attributes.ATTACK_SPEED, "ACCC3F91-F5C0-4B9E-83AC-ADBEE6639702", -0.5, AttributeModifier.Operation.MULTIPLY_TOTAL));

        public static final RegistryObject<Effect> BUZZED = EFFECTS.register("buzzed", () -> new IncurableExpiringEffect(EffectType.BENEFICIAL, 0xA5A29C)
                .addAttributeModifier(Attributes.ATTACK_SPEED, "6BB34E5D-1312-411E-A45C-EA289BEE3D23", 0.1, AttributeModifier.Operation.MULTIPLY_TOTAL)
                .addAttributeModifier(Attributes.MOVEMENT_SPEED, "2A4EE23F-FC15-4DD6-A841-1EFCC7420CAC", 0.2, AttributeModifier.Operation.MULTIPLY_TOTAL));
    }
}
