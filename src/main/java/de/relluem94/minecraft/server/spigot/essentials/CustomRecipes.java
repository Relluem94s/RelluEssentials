package de.relluem94.minecraft.server.spigot.essentials;

import java.util.HashMap;

import org.bukkit.Material;

import de.relluem94.minecraft.server.spigot.essentials.helpers.RecipeHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.recipe.Shaped;

import static de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants.*;

public class CustomRecipes {

    private CustomRecipes() {
        throw new IllegalStateException(Strings.PLUGIN_INTERNAL_UTILITY_CLASS);
    }

    public static RecipeHelper smelterFurnace = new RecipeHelper(
        PLUGIN_ITEM_NAMESPACE_SMELTER_FURNACE,
        new Shaped(
            new String[]{
                "AAA",
                "BCB",
                "AGA"
            },
            new HashMap<Character, Material>() {
                {
                    put('A', Material.OBSIDIAN);
                    put('B', Material.COMPARATOR);
                    put('C', Material.FURNACE);
                    put('G', Material.GLASS);
                }
            }),
            CustomItems.autoSmeltFurnace.getCustomItem()
    );

    public static RecipeHelper smelterFuel = new RecipeHelper(
        PLUGIN_ITEM_NAMESPACE_SMELTER_FUEL,
        new Material[]{CustomItems.autoSmeltNetheritePickAxe.getMaterial(), Material.LAVA_BUCKET},
        CustomItems.autoSmeltNetheritePickAxe.getCustomItem()
    );

    public static RecipeHelper cloudBoots = new RecipeHelper(
        PLUGIN_ITEM_NAMESPACE_CLOUD_BOOTS,
        new Shaped(
            new String[]{
                "F F",
                "F F",
            },
            new HashMap<Character, Material>() {
                {
                    put('F', CustomItems.cloudSailor.getMaterial());
                }
            }),
            CustomItems.cloudBoots.getCustomItem()
    );

    public static RecipeHelper smelterTank =  new RecipeHelper(
        PLUGIN_ITEM_NAMESPACE_SMELTER_TANK,
        new Shaped(
            new String[]{
                "GGG",
                "GCG",
                "GGG"
            },
            new HashMap<Character, Material>() {
                {
                    put('G', Material.GLASS);
                    put('C', Material.CAULDRON);
                }
            }),
            CustomItems.autoSmeltTank.getCustomItem()
    );

    public static RecipeHelper autoSmeltFurnace = new RecipeHelper(
        PLUGIN_ITEM_NAMESPACE_SMELTER_PICKAXE,
        new Material[]{Material.NETHERITE_PICKAXE, CustomItems.autoSmeltTank.getMaterial(), CustomItems.autoSmeltFurnace.getMaterial()},
        CustomItems.autoSmeltNetheritePickAxe.getCustomItem()
    );
}