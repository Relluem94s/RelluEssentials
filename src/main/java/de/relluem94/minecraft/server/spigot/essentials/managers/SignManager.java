package de.relluem94.minecraft.server.spigot.essentials.managers;

import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_NAME_CONSOLE;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.ChatHelper.consoleSendMessage;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.managers.Enable;
import de.relluem94.minecraft.server.spigot.essentials.models.SignAction;
import de.relluem94.minecraft.server.spigot.essentials.registries.SignRegistry;
import de.relluem94.minecraft.server.spigot.essentials.services.TranslationService;
import org.bukkit.plugin.Plugin;

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
    TranslationService translationService = relluEssentialsPlugin.getServiceContext().getTranslationService();
    int signCount = SignRegistry.getAllByNamespace(plugin.getName()).size();
    consoleSendMessage(PLUGIN_NAME_CONSOLE,
        translationService.get(MessageKey.PLUGIN_MANAGER_SIGNS_REGISTERED, signCount));
  }
}