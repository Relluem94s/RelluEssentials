package de.relluem94.minecraft.server.spigot.essentials.managers;

import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_NAME_CONSOLE;
import static de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants.PLUGIN_ITEM_NAMESPACE_CLOUD_BOOTS;
import static de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants.PLUGIN_ITEM_NAMESPACE_CLOUD_SAILOR;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.ChatHelper.consoleSendMessage;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.helpers.RecipeHelper;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.managers.Enable;
import de.relluem94.minecraft.server.spigot.essentials.models.RelluEssentialsNamespacedKey;
import de.relluem94.minecraft.server.spigot.essentials.models.items.CustomItem;
import de.relluem94.minecraft.server.spigot.essentials.models.recipe.Shaped;
import de.relluem94.minecraft.server.spigot.essentials.services.TranslationService;
import java.util.Map;
import org.bukkit.plugin.Plugin;
import org.jspecify.annotations.NonNull;

/**
 * Manages the registration of custom crafting recipes for the plugin.
 * Registers all custom recipes with the server during the plugin enable phase.
 */
public class RecipeManager implements Enable {

  @Override
  public void enable(Plugin plugin) {
    RelluEssentials relluEssentialsPlugin = (RelluEssentials) plugin;
    TranslationService translationService = relluEssentialsPlugin.getServiceContext()
        .getTranslationService();

    consoleSendMessage(PLUGIN_NAME_CONSOLE,
        translationService.get(MessageKey.PLUGIN_MANAGER_REGISTER_RECIPE));
    int recipeCount = 0;
    plugin.getServer().addRecipe(buildCloudBootsRecipe(relluEssentialsPlugin).getRecipe());
    recipeCount++;
    consoleSendMessage(PLUGIN_NAME_CONSOLE,
        translationService.get(MessageKey.PLUGIN_MANAGER_RECIPE_REGISTERED, recipeCount));
  }

  private @NonNull RecipeHelper buildCloudBootsRecipe(RelluEssentials plugin) {
    ServiceContext serviceContext = plugin.getServiceContext();
    CustomItem cloudSailorItem = serviceContext.getItemService().find(
        new RelluEssentialsNamespacedKey(serviceContext.getPluginMetadataService().getName(),
            PLUGIN_ITEM_NAMESPACE_CLOUD_SAILOR)).orElseThrow();
    CustomItem cloudBootsItem = serviceContext.getItemService().find(
        new RelluEssentialsNamespacedKey(serviceContext.getPluginMetadataService().getName(),
            PLUGIN_ITEM_NAMESPACE_CLOUD_BOOTS)).orElseThrow();

    return new RecipeHelper(PLUGIN_ITEM_NAMESPACE_CLOUD_BOOTS,
        new Shaped(new String[]{"F F", "F F", }, Map.of('F', cloudSailorItem.material())),
        cloudBootsItem.toItemStack());
  }
}
