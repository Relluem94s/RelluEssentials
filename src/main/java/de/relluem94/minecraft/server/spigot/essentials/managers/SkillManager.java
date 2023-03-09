package de.relluem94.minecraft.server.spigot.essentials.managers;

import static de.relluem94.minecraft.server.spigot.essentials.Strings.*;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.ChatHelper.consoleSendMessage;

public class SkillManager implements IManager {

    @Override
    public void manage() {
        consoleSendMessage(PLUGIN_NAME_CONSOLE, PLUGIN_COMMAND_COLOR + LANG_REGISTER_SKILLS);
        consoleSendMessage(PLUGIN_NAME_CONSOLE, PLUGIN_COMMAND_COLOR + LANG_SKILLS_REGISTERED);
    }
    
}
