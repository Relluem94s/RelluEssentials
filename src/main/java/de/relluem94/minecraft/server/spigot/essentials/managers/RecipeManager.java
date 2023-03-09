package de.relluem94.minecraft.server.spigot.essentials.managers;

import static de.relluem94.minecraft.server.spigot.essentials.Strings.*;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.ChatHelper.consoleSendMessage;

import org.bukkit.Bukkit;

import de.relluem94.minecraft.server.spigot.essentials.CustomRecipes;

public class RecipeManager implements IEnable {

    @Override
    public void enable() {
        consoleSendMessage(PLUGIN_NAME_CONSOLE, PLUGIN_COMMAND_COLOR + LANG_REGISTER_RECIPE);
        int recipeCount = 0;
        Bukkit.addRecipe(CustomRecipes.smelterFurnace.getRecipe());             recipeCount++;
        Bukkit.addRecipe(CustomRecipes.smelterFuel.getRecipe());                recipeCount++;
        Bukkit.addRecipe(CustomRecipes.cloudBoots.getRecipe());                 recipeCount++;
        Bukkit.addRecipe(CustomRecipes.smelterTank.getRecipe());                recipeCount++;
        Bukkit.addRecipe(CustomRecipes.autoSmeltFurnace.getRecipe());           recipeCount++;
        consoleSendMessage(PLUGIN_NAME_CONSOLE, PLUGIN_COMMAND_COLOR + String.format(LANG_RECIPE_REGISTERED, recipeCount));
    }
    
}
