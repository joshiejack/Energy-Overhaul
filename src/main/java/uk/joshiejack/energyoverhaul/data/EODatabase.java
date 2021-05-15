package uk.joshiejack.energyoverhaul.data;

import net.minecraft.data.DataGenerator;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import uk.joshiejack.energyoverhaul.EnergyOverhaul;
import uk.joshiejack.penguinlib.data.database.CSVUtils;
import uk.joshiejack.penguinlib.data.generators.AbstractDatabaseProvider;

import java.util.Objects;

public class EODatabase extends AbstractDatabaseProvider {
    public EODatabase(DataGenerator gen) {
        super(gen, EnergyOverhaul.MODID);
    }

    @Override
    protected void addDatabaseEntries() {
        addNutritionOverride(Items.BAKED_POTATO, 3);
        addNutritionOverride(Items.CARROT, 2);
        addNutritionOverride(Items.APPLE, 2);
        addNutritionOverride(Items.BREAD, 3);
        addNutritionOverride(Items.COOKIE, 1);
        addNutritionOverride(Items.MELON, 1);
        addNutritionOverride(Items.COOKED_CHICKEN, 5);
        addNutritionOverride(Items.COOKED_BEEF, 6);
        addNutritionOverride(Items.COOKED_PORKCHOP, 6);
        addNutritionOverride(Items.SPIDER_EYE, 1);
        addNutritionOverride(Items.ROTTEN_FLESH, 2);
        addNutritionOverride(Items.PUMPKIN_PIE, 6);
        addNutritionOverride(Items.RABBIT, 2);
        addNutritionOverride(Items.RABBIT_STEW, 8);
        addNutritionOverride(Items.BEETROOT_SOUP, 5);
        addNutritionOverride(Items.CHORUS_FRUIT, 2);
    }

    protected void addNutritionOverride(Item item, int nutrition) {
        addFoodOverride(item, nutrition, -1F);
    }

    protected void addFoodOverride(Item item, int nutrition, float saturation) {
        addEntry("food_overrides", "Item,Nutrition,Saturation",
                CSVUtils.join(Objects.requireNonNull(item.getRegistryName()).toString(), nutrition, saturation));
    }
}
