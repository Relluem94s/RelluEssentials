package de.relluem94.minecraft.server.spigot.essentials.managers;

import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_NAME_CONSOLE;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.managers.Enable;
import de.relluem94.minecraft.server.spigot.essentials.services.TranslationService;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.Plugin;

/**
 * Manages the registration and lifecycle of skills within the plugin.
 *
 * @author rellu
 */
public class SkillManager implements Enable {

  @Override
  public void enable(Plugin plugin) {
    RelluEssentials relluEssentialsPlugin = (RelluEssentials) plugin;
    ServiceContext serviceContext = relluEssentialsPlugin.getServiceContext();

    TranslationService translationService = serviceContext.getTranslationService();
    ConsoleCommandSender consoleCommandSender = serviceContext
        .getServerService()
        .getConsoleSender();

    consoleCommandSender.sendMessage(
        PLUGIN_NAME_CONSOLE + translationService.get(MessageKey.PLUGIN_MANAGER_REGISTER_SKILLS));
    consoleCommandSender.sendMessage(
        PLUGIN_NAME_CONSOLE + translationService.get(MessageKey.PLUGIN_MANAGER_SKILLS_REGISTERED));
  }
}