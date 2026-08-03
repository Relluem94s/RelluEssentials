package de.relluem94.minecraft.server.spigot.essentials.managers;

import static de.relluem94.minecraft.server.spigot.essentials.RelluEssentials.translationService;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_NAME_CONSOLE;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.ChatHelper.consoleSendMessage;

import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.managers.Enable;
import org.bukkit.plugin.Plugin;

public class SkillManager implements Enable {

  @Override
  public void enable(Plugin plugin) {
    consoleSendMessage(PLUGIN_NAME_CONSOLE,
        translationService.get(MessageKey.PLUGIN_MANAGER_REGISTER_SKILLS));
    consoleSendMessage(PLUGIN_NAME_CONSOLE,
        translationService.get(MessageKey.PLUGIN_MANAGER_SKILLS_REGISTERED));
  }
}