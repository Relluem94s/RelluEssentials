package de.relluem94.minecraft.server.spigot.essentials.managers;

import static de.relluem94.minecraft.server.spigot.essentials.Strings.*;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.ChatHelper.consoleSendMessage;

import org.bukkit.Bukkit;

import de.relluem94.minecraft.server.spigot.essentials.CustomRecipes;

public class RecipeManager implements IEnable {

    @Override
    public void enable() {
        consoleSendMessage(PLUGIN_NAME_CONSOLE, PLUGIN_COLOR_COMMAND + PLUGIN_MANAGER_REGISTER_RECIPE);
        int recipeCount = 0;
        Bukkit.addRecipe(CustomRecipes.smelterFurnace.getRecipe());             recipeCount++;
        Bukkit.addRecipe(CustomRecipes.smelterFuel.getRecipe());                recipeCount++;
        Bukkit.addRecipe(CustomRecipes.cloudBoots.getRecipe());                 recipeCount++;
        Bukkit.addRecipe(CustomRecipes.smelterTank.getRecipe());                recipeCount++;
        Bukkit.addRecipe(CustomRecipes.autoSmeltFurnace.getRecipe());           recipeCount++;
        consoleSendMessage(PLUGIN_NAME_CONSOLE, PLUGIN_COLOR_COMMAND + String.format(PLUGIN_MANAGER_RECIPE_REGISTERED, recipeCount));
    }
    
}
