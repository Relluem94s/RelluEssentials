package de.relluem94.minecraft.server.spigot.essentials.managers;

import static de.relluem94.minecraft.server.spigot.essentials.RelluEssentials.languageHelper;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_NAME_CONSOLE;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.ChatHelper.consoleSendMessage;

import de.relluem94.minecraft.server.spigot.essentials.constants.MessageKey;
import org.bukkit.command.PluginCommandYamlParser;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;

public class CommandManager implements IEnable {

    @Override
    public void enable() {
        consoleSendMessage(PLUGIN_NAME_CONSOLE, languageHelper.get(MessageKey.PLUGIN_MANAGER_REGISTER_COMMANDS));

        RelluEssentials.commandWrapperList.forEach(wrapper -> wrapper.init(RelluEssentials.getInstance()));

        int commands = PluginCommandYamlParser.parse(RelluEssentials.getInstance()).size();
        consoleSendMessage(PLUGIN_NAME_CONSOLE, languageHelper.get(MessageKey.PLUGIN_MANAGER_COMMANDS_REGISTERED, commands));
    }
}