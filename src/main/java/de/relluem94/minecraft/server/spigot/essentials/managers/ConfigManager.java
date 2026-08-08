package de.relluem94.minecraft.server.spigot.essentials.managers;

import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_NAME_CONSOLE;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.ChatHelper.consoleSendMessage;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.managers.Disable;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.managers.Enable;
import de.relluem94.minecraft.server.spigot.essentials.services.TranslationService;
import org.bukkit.plugin.Plugin;

public class ConfigManager implements Enable, Disable {

  @Override
  public void enable(Plugin plugin) {
    RelluEssentials relluEssentialsPlugin = (RelluEssentials) plugin;

    TranslationService translationService = relluEssentialsPlugin.getServiceContext().getTranslationService();
    consoleSendMessage(PLUGIN_NAME_CONSOLE,
        translationService.get(MessageKey.PLUGIN_MANAGER_LOADING_CONFIGS));

    if (plugin.getDataFolder().exists()) {
      return;
    }

    if (!plugin.getDataFolder().mkdir()) {
      consoleSendMessage(PLUGIN_NAME_CONSOLE,
          translationService.get(MessageKey.PLUGIN_FOLDER_MKDIR_ERROR));
    }

    RelluEssentials.getInstance().saveDefaultConfig();

    consoleSendMessage(PLUGIN_NAME_CONSOLE,
        translationService.get(MessageKey.PLUGIN_MANAGER_CONFIGS_LOADED));
  }

  @Override
  public void disable(Plugin plugin) {
    plugin.saveConfig();
  }
}