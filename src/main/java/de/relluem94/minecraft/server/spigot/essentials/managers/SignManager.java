package de.relluem94.minecraft.server.spigot.essentials.managers;

import static de.relluem94.minecraft.server.spigot.essentials.RelluEssentials.languageHelper;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_NAME_CONSOLE;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.ChatHelper.consoleSendMessage;

import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.managers.Enable;
import de.relluem94.minecraft.server.spigot.essentials.model.SignAction;
import de.relluem94.minecraft.server.spigot.essentials.registry.SignRegistry;
import org.bukkit.plugin.Plugin;

public class SignManager implements Enable {

  private final Plugin plugin;

  public SignManager(Plugin plugin) {
    this.plugin = plugin;
  }

  @Override
  public void enable() {
    SignRegistry.register(plugin, "spawn", new SignAction("Spawn", false));
    SignRegistry.register(plugin, "up", new SignAction("Up", false));
    SignRegistry.register(plugin, "down", new SignAction("Down", false));
    SignRegistry.register(plugin, "command", new SignAction("Command", true));
    SignRegistry.register(plugin, "teleport", new SignAction("Teleport", true));
    SignRegistry.register(plugin, "home", new SignAction("Home", true));

    int signCount = SignRegistry.getAllByNamespace(plugin.getName()).size();
    consoleSendMessage(PLUGIN_NAME_CONSOLE,
        languageHelper.get(MessageKey.PLUGIN_MANAGER_SIGNS_REGISTERED, signCount));
  }
}