package uk.joshiejack.energyoverhaul.data;

import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import uk.joshiejack.energyoverhaul.EnergyOverhaul;

import javax.annotation.Nullable;

public final class EOBlockTags extends BlockTagsProvider {
    public EOBlockTags(DataGenerator generator, @Nullable ExistingFileHelper existingFileHelper) {
        super(generator, EnergyOverhaul.MODID, existingFileHelper);
    }

    @Override
    protected void addTags() {}
}
