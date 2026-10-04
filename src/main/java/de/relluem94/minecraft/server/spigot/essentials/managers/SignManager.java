package de.relluem94.minecraft.server.spigot.essentials.managers;

import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_NAME_CONSOLE;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.managers.Enable;
import de.relluem94.minecraft.server.spigot.essentials.models.SignAction;
import de.relluem94.minecraft.server.spigot.essentials.registries.SignRegistry;
import de.relluem94.minecraft.server.spigot.essentials.services.TranslationService;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.Plugin;

/**
 * Manager responsible for registering all available sign actions during plugin startup.
 *
 * @author rellu
 */
public class SignManager implements Enable {

  @Override
  public void enable(Plugin plugin) {

    SignRegistry.register(plugin, "spawn", new SignAction("Spawn", false));
    SignRegistry.register(plugin, "up", new SignAction("Up", false));
    SignRegistry.register(plugin, "down", new SignAction("Down", false));
    SignRegistry.register(plugin, "command", new SignAction("Command", true));
    SignRegistry.register(plugin, "teleport", new SignAction("Teleport", true));
    SignRegistry.register(plugin, "home", new SignAction("Home", true));

    RelluEssentials relluEssentialsPlugin = (RelluEssentials) plugin;
    ServiceContext serviceContext = relluEssentialsPlugin.getServiceContext();

    TranslationService translationService = serviceContext.getTranslationService();
    ConsoleCommandSender consoleCommandSender = serviceContext
        .getServerService()
        .getConsoleSender();

    int signCount = SignRegistry
        .getAllByNamespace(plugin.getName())
        .size();
    consoleCommandSender.sendMessage(
        PLUGIN_NAME_CONSOLE + translationService.get(MessageKey.PLUGIN_MANAGER_SIGNS_REGISTERED, signCount));
  }
}