package de.relluem94.minecraft.server.spigot.essentials;

import static de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants.PLUGIN_ITEM_NAMESPACE_CLOUD_BOOTS;

import java.util.Map;

import de.relluem94.minecraft.server.spigot.essentials.helpers.RecipeHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.recipe.Shaped;

public class CustomRecipes {

    private CustomRecipes() {
        throw new IllegalStateException(Strings.PLUGIN_INTERNAL_UTILITY_CLASS);
    }

    public static final RecipeHelper cloudBoots = new RecipeHelper(
        PLUGIN_ITEM_NAMESPACE_CLOUD_BOOTS,
        new Shaped(
            new String[]{
                "F F",
                "F F",
            },
            Map.of(
                'F', CustomItems.cloudSailor.getMaterial()
            )),
            CustomItems.cloudBoots.getCustomItem()
    );
}