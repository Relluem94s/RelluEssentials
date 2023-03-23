package de.relluem94.minecraft.server.spigot.essentials;

import java.util.Map;

import org.bukkit.Material;

import de.relluem94.minecraft.server.spigot.essentials.helpers.RecipeHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.recipe.Shaped;

import static de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants.*;

public class CustomRecipes {

    private CustomRecipes() {
        throw new IllegalStateException(Strings.PLUGIN_INTERNAL_UTILITY_CLASS);
    }

    public static final RecipeHelper smelterFurnace = new RecipeHelper(
        PLUGIN_ITEM_NAMESPACE_SMELTER_FURNACE,
        new Shaped(
            new String[]{
                "AAA",
                "BCB",
                "AGA"
            },
            Map.of(
                'A', Material.OBSIDIAN,
                'B', Material.COMPARATOR,
                'C', Material.FURNACE,
                'G', Material.GLASS
            )),
            CustomItems.autoSmeltFurnace.getCustomItem()
        
    );

    public static final RecipeHelper smelterFuel = new RecipeHelper(
        PLUGIN_ITEM_NAMESPACE_SMELTER_FUEL,
        new Material[]{CustomItems.autoSmeltNetheritePickAxe.getMaterial(), Material.LAVA_BUCKET},
        CustomItems.autoSmeltNetheritePickAxe.getCustomItem()
    );

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

    public static final RecipeHelper smelterTank =  new RecipeHelper(
        PLUGIN_ITEM_NAMESPACE_SMELTER_TANK,
        new Shaped(
            new String[]{
                "GGG",
                "GCG",
                "GGG"
            },
            Map.of(
                'G', Material.GLASS,
                'C', Material.CAULDRON
            )),
            CustomItems.autoSmeltTank.getCustomItem()
    );

    public static final RecipeHelper autoSmeltFurnace = new RecipeHelper(
        PLUGIN_ITEM_NAMESPACE_SMELTER_PICKAXE,
        new Material[]{Material.NETHERITE_PICKAXE, CustomItems.autoSmeltTank.getMaterial(), CustomItems.autoSmeltFurnace.getMaterial()},
        CustomItems.autoSmeltNetheritePickAxe.getCustomItem()
    );
}