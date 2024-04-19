package de.relluem94.minecraft.server.spigot.essentials.managers;

import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_COLOR_COMMAND;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_MANAGER_RECIPE_REGISTERED;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_MANAGER_REGISTER_RECIPE;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_NAME_CONSOLE;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.ChatHelper.consoleSendMessage;

import org.bukkit.Bukkit;

import de.relluem94.minecraft.server.spigot.essentials.CustomRecipes;

public class RecipeManager implements IEnable {

    @Override
    public void enable() {
        consoleSendMessage(PLUGIN_NAME_CONSOLE, PLUGIN_COLOR_COMMAND + PLUGIN_MANAGER_REGISTER_RECIPE);
        int recipeCount = 0;
        Bukkit.addRecipe(CustomRecipes.cloudBoots.getRecipe());                 recipeCount++;
        consoleSendMessage(PLUGIN_NAME_CONSOLE, PLUGIN_COLOR_COMMAND + String.format(PLUGIN_MANAGER_RECIPE_REGISTERED, recipeCount));
    }
    
}
