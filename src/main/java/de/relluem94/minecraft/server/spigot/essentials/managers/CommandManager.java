package de.relluem94.minecraft.server.spigot.essentials.managers;

import static de.relluem94.minecraft.server.spigot.essentials.RelluEssentials.languageHelper;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_NAME_CONSOLE;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.ChatHelper.consoleSendMessage;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.context.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.managers.Enable;
import org.bukkit.command.PluginCommandYamlParser;

public class CommandManager implements Enable {

  @Override
  public void enable(RelluEssentials plugin) {
    consoleSendMessage(PLUGIN_NAME_CONSOLE,
        languageHelper.get(MessageKey.PLUGIN_MANAGER_REGISTER_COMMANDS));

    ServiceContext serviceContext = new ServiceContext(plugin.getGroupService(), plugin.getPlayerService());

    RelluEssentials.getCommandWrapperList()
        .forEach(wrapper -> wrapper.init(plugin, serviceContext));

    int commands = PluginCommandYamlParser.parse(plugin).size();
    consoleSendMessage(PLUGIN_NAME_CONSOLE,
        languageHelper.get(MessageKey.PLUGIN_MANAGER_COMMANDS_REGISTERED, commands));
  }
}