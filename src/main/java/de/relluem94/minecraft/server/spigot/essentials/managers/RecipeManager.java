package de.relluem94.minecraft.server.spigot.essentials.managers;

import static de.relluem94.minecraft.server.spigot.essentials.Strings.*;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.ChatHelper.consoleSendMessage;

import org.bukkit.Bukkit;

import de.relluem94.minecraft.server.spigot.essentials.CustomRecipes;

public class RecipeManager implements Manager {

    @Override
    public void manage() {
        consoleSendMessage(PLUGIN_NAME_CONSOLE, PLUGIN_COMMAND_COLOR + LANG_REGISTER_RECIPE);
        Bukkit.addRecipe(CustomRecipes.smelterFurnace.getRecipe());
        Bukkit.addRecipe(CustomRecipes.smelterFuel.getRecipe());
        Bukkit.addRecipe(CustomRecipes.cloudBoots.getRecipe());
        Bukkit.addRecipe(CustomRecipes.smelterTank.getRecipe());
        Bukkit.addRecipe(CustomRecipes.autoSmeltFurnace.getRecipe());
        consoleSendMessage(PLUGIN_NAME_CONSOLE, PLUGIN_COMMAND_COLOR + LANG_RECIPE_REGISTERED);
    }
    
}
