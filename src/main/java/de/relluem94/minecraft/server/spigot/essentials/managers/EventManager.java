package de.relluem94.minecraft.server.spigot.essentials.managers;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.constants.MessageKey;

import static de.relluem94.minecraft.server.spigot.essentials.RelluEssentials.languageHelper;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_NAME_CONSOLE;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.ChatHelper.consoleSendMessage;

public class EventManager implements IEnable{
    
    @Override
    public void enable() {
        consoleSendMessage(PLUGIN_NAME_CONSOLE, languageHelper.get(MessageKey.PLUGIN_MANAGER_REGISTER_EVENTS));
        RelluEssentials.getEventWrapperList().forEach(eventWrapper -> eventWrapper.init(RelluEssentials.getInstance()));
        consoleSendMessage(PLUGIN_NAME_CONSOLE, languageHelper.get(MessageKey.PLUGIN_MANAGER_EVENTS_REGISTERED, RelluEssentials.getEventWrapperList().size()));
    }

}
