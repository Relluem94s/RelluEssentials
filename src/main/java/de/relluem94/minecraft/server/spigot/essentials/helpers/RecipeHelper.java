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
            Recipe recipe = new ShapedRecipe(nameSpacedKey, result);

            recipe = ((ShapedRecipe) recipe).shape(shape.getRows());
            for(Character cr : shape.getIngredients().keySet()){
                Material mat = shape.getIngredients().get(cr);
                ((ShapedRecipe) recipe).setIngredient(cr, mat);
            }
            
            return recipe;
        }
        
        Recipe recipe = new ShapelessRecipe(nameSpacedKey, result);
        for(Material m : ingredients){
            ((ShapelessRecipe) recipe).addIngredient(m);
        }

        return recipe;
    }
}
