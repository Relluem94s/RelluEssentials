package de.relluem94.minecraft.server.spigot.essentials.managers;

import de.relluem94.minecraft.server.spigot.essentials.constants.MessageKey;

import static de.relluem94.minecraft.server.spigot.essentials.RelluEssentials.languageHelper;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.*;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.ChatHelper.consoleSendMessage;

public class SkillManager implements IEnable {

    @Override
    public void enable() {
        consoleSendMessage(PLUGIN_NAME_CONSOLE, languageHelper.get(MessageKey.PLUGIN_MANAGER_REGISTER_SKILLS));
        consoleSendMessage(PLUGIN_NAME_CONSOLE, languageHelper.get(MessageKey.PLUGIN_MANAGER_SKILLS_REGISTERED));
    }
    
}
