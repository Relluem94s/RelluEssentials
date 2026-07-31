package de.relluem94.minecraft.server.spigot.essentials.managers;

import static de.relluem94.minecraft.server.spigot.essentials.RelluEssentials.languageHelper;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_NAME_CONSOLE;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.ChatHelper.consoleSendMessage;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.managers.Enable;

public class EventManager implements Enable {

  @Override
  public void enable() {
    consoleSendMessage(PLUGIN_NAME_CONSOLE,
        languageHelper.get(MessageKey.PLUGIN_MANAGER_REGISTER_EVENTS));
    RelluEssentials.getEventWrapperList()
        .forEach(eventWrapper -> eventWrapper.init(RelluEssentials.getInstance()));
    consoleSendMessage(PLUGIN_NAME_CONSOLE,
        languageHelper.get(MessageKey.PLUGIN_MANAGER_EVENTS_REGISTERED,
            RelluEssentials.getEventWrapperList().size()));
  }

}
