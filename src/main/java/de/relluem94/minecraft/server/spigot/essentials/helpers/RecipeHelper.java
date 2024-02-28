package de.relluem94.minecraft.server.spigot.essentials.helpers;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.helpers.recipe.Shaped;

public class RecipeHelper {
    private final NamespacedKey nameSpacedKey;
    private final ItemStack result;
    private Material[] ingredients;

    Shaped shape;

    /**
     * 
     * @param name String
     * @param shape Shaped
     * @param result ItemStack
     */
    public RecipeHelper(String name, Shaped shape, ItemStack result){
        this.shape = shape;
        this.result = result;
        nameSpacedKey = new NamespacedKey(RelluEssentials.getInstance(), name);
    }

    /**
     * 
     * @param name String
     * @param ingredients Material[]
     * @param result ItemStack
     */
    public RecipeHelper(String name, Material[] ingredients, ItemStack result){
        this.result = result;
        this.ingredients = ingredients;
        nameSpacedKey = new NamespacedKey(RelluEssentials.getInstance(), name);
    }

    /**
     * creates Recipe from Helper
     * @return Recipe
     */
    public Recipe getRecipe(){
        if(shape != null){
            ShapedRecipe recipe = new ShapedRecipe(nameSpacedKey, result);

            recipe = recipe.shape(shape.rows());
            for(Character cr : shape.ingredients().keySet()){
                Material mat = shape.ingredients().get(cr);
                recipe.setIngredient(cr, mat);
            }
            
            return recipe;
        }

        ShapelessRecipe recipe = new ShapelessRecipe(nameSpacedKey, result);
        for(Material m : ingredients){
            recipe.addIngredient(m);
        }

        return recipe;
    }
}
