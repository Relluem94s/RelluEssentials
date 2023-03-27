package de.relluem94.minecraft.server.spigot.essentials.managers;

import static de.relluem94.minecraft.server.spigot.essentials.Strings.*;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.ChatHelper.consoleSendMessage;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;

public class ConfigManager implements IEnable, IDisable {

    @Override
    public void enable() {
        consoleSendMessage(PLUGIN_NAME_CONSOLE, PLUGIN_COLOR_COMMAND + PLUGIN_MANAGER_LOADING_CONFIGS);

        if (!RelluEssentials.getInstance().getDataFolder().exists()) {
            RelluEssentials.getInstance().getDataFolder().mkdir();
        }
        
        RelluEssentials.getInstance().saveDefaultConfig();

        consoleSendMessage(PLUGIN_NAME_CONSOLE, PLUGIN_COLOR_COMMAND + PLUGIN_MANAGER_CONFIGS_LOADED);
    }

    @Override
    public void disable() {
        RelluEssentials.getInstance().saveConfig();
    }
    
}
