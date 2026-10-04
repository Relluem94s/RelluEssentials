package de.relluem94.minecraft.server.spigot.essentials.managers;

import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_NAME_CONSOLE;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.managers.Disable;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.managers.Enable;
import de.relluem94.minecraft.server.spigot.essentials.services.TranslationService;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.Plugin;

/**
 * Manages the configuration lifecycle of the plugin. Handles the creation of the plugin data folder, saving of default
 * configuration files, and persisting configuration state on shutdown.
 *
 * @author rellu
 */
public class ConfigManager implements Enable, Disable {

  @Override
  public void enable(Plugin plugin) {
    RelluEssentials relluEssentialsPlugin = (RelluEssentials) plugin;
    ServiceContext serviceContext = relluEssentialsPlugin.getServiceContext();
    TranslationService translationService = serviceContext.getTranslationService();

    ConsoleCommandSender consoleCommandSender = serviceContext
        .getServerService()
        .getConsoleSender();

    consoleCommandSender.sendMessage(
        PLUGIN_NAME_CONSOLE + translationService.get(MessageKey.PLUGIN_MANAGER_LOADING_CONFIGS));

    if (plugin
        .getDataFolder()
        .exists()) {
      return;
    }

    if (!plugin
        .getDataFolder()
        .mkdir()) {
      consoleCommandSender.sendMessage(
          PLUGIN_NAME_CONSOLE + translationService.get(MessageKey.PLUGIN_FOLDER_MKDIR_ERROR));
    }

    RelluEssentials
        .getInstance()
        .saveDefaultConfig();

    consoleCommandSender.sendMessage(
        PLUGIN_NAME_CONSOLE + translationService.get(MessageKey.PLUGIN_MANAGER_CONFIGS_LOADED));
  }

  @Override
  public void disable(Plugin plugin) {
    plugin.saveConfig();
  }
}