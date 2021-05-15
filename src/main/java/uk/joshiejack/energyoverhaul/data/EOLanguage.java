package uk.joshiejack.energyoverhaul.data;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.LanguageProvider;
import uk.joshiejack.energyoverhaul.EnergyOverhaul;

public class EOLanguage extends LanguageProvider {
    public EOLanguage(DataGenerator gen) {
        super(gen, EnergyOverhaul.MODID, "en_us");
    }

    @Override
    protected void addTranslations() {}
}
