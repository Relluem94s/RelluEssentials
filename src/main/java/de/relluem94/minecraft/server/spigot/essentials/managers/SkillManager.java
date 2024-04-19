package de.relluem94.minecraft.server.spigot.essentials.managers;

import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.*;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.ChatHelper.consoleSendMessage;

public class SkillManager implements IEnable {

    @Override
    public void enable() {
        consoleSendMessage(PLUGIN_NAME_CONSOLE, PLUGIN_COLOR_COMMAND + PLUGIN_MANAGER_REGISTER_SKILLS);
        consoleSendMessage(PLUGIN_NAME_CONSOLE, PLUGIN_COLOR_COMMAND + PLUGIN_MANAGER_SKILLS_REGISTERED);
    }
    
}
