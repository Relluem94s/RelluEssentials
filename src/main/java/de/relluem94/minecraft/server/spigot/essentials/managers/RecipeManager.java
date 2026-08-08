package de.relluem94.minecraft.server.spigot.essentials.managers;

import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_NAME_CONSOLE;
import static de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants.PLUGIN_ITEM_NAMESPACE_CLOUD_BOOTS;
import static de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants.PLUGIN_ITEM_NAMESPACE_CLOUD_SAILOR;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.ChatHelper.consoleSendMessage;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.helpers.ItemHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.RecipeHelper;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.managers.Enable;
import de.relluem94.minecraft.server.spigot.essentials.models.RegistryKey;
import de.relluem94.minecraft.server.spigot.essentials.models.recipe.Shaped;
import de.relluem94.minecraft.server.spigot.essentials.registries.ItemRegistry;
import de.relluem94.minecraft.server.spigot.essentials.services.TranslationService;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.jspecify.annotations.NonNull;

public class RecipeManager implements Enable {

  @Override
  public void enable(Plugin plugin) {
    RelluEssentials relluEssentialsPlugin = (RelluEssentials) plugin;
    TranslationService translationService = relluEssentialsPlugin.getServiceContext().getTranslationService();

    consoleSendMessage(PLUGIN_NAME_CONSOLE,
        translationService.get(MessageKey.PLUGIN_MANAGER_REGISTER_RECIPE));
    int recipeCount = 0;
    Bukkit.addRecipe(buildCloudBootsRecipe(relluEssentialsPlugin).getRecipe());
    recipeCount++;
    consoleSendMessage(PLUGIN_NAME_CONSOLE,
        translationService.get(MessageKey.PLUGIN_MANAGER_RECIPE_REGISTERED, recipeCount));
  }

  private @NonNull RecipeHelper buildCloudBootsRecipe(RelluEssentials plugin) {
    ItemHelper cloudSailorItem = ItemRegistry.find(
        RegistryKey.of(plugin, PLUGIN_ITEM_NAMESPACE_CLOUD_SAILOR)).orElseThrow();
    ItemHelper cloudBootsItem = ItemRegistry.find(
            RegistryKey.of(plugin, PLUGIN_ITEM_NAMESPACE_CLOUD_BOOTS))
        .orElseThrow();

    return new RecipeHelper(
        PLUGIN_ITEM_NAMESPACE_CLOUD_BOOTS,
        new Shaped(
            new String[]{
                "F F",
                "F F",
            },
            Map.of('F', cloudSailorItem.getMaterial())
        ),
        cloudBootsItem.getCustomItem()
    );
  }
}
