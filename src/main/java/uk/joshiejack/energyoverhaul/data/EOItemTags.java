package uk.joshiejack.energyoverhaul.data;

import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.ItemTagsProvider;
import net.minecraft.data.TagsProvider;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.tags.ItemTags;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import uk.joshiejack.energyoverhaul.EnergyOverhaul;
import uk.joshiejack.energyoverhaul.handlers.Exhaustion;
import uk.joshiejack.penguinlib.util.PenguinTags;

import javax.annotation.Nullable;

public final class EOItemTags extends ItemTagsProvider {
    public EOItemTags(DataGenerator generator, BlockTagsProvider blockTagProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(generator, blockTagProvider, EnergyOverhaul.MODID, existingFileHelper);
    }

    @Override
    public void addTags() {
        TagsProvider.Builder<Item> required = tag(Exhaustion.REQUIRES_ENERGY);
        required.add(Items.CACTUS, Items.SHEARS, Items.BOW, Items.CROSSBOW);
        required.addTags(PenguinTags.WATERING_CANS, PenguinTags.HOES, Tags.Items.SEEDS,
                Tags.Items.CROPS, ItemTags.SAPLINGS);
        ForgeRegistries.ITEMS.getValues().stream()
                .filter(item -> item instanceof BucketItem)
                .forEach(required::add);
    }
}
