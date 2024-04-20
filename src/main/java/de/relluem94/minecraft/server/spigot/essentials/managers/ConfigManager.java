package de.relluem94.minecraft.server.spigot.essentials.managers;

import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.*;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.ChatHelper.consoleSendMessage;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import org.bukkit.plugin.InvalidPluginException;

public class ConfigManager implements IEnable, IDisable {

    @Override
    public void enable()  {
        consoleSendMessage(PLUGIN_NAME_CONSOLE, PLUGIN_COLOR_COMMAND + PLUGIN_MANAGER_LOADING_CONFIGS);

        if (RelluEssentials.getInstance().getDataFolder().exists()) {
            return;
        }

        if(!RelluEssentials.getInstance().getDataFolder().mkdir()){
            consoleSendMessage(PLUGIN_NAME_CONSOLE, PLUGIN_FOLDER_MKDIR_ERROR);
        }
        
        RelluEssentials.getInstance().saveDefaultConfig();

        consoleSendMessage(PLUGIN_NAME_CONSOLE, PLUGIN_COLOR_COMMAND + PLUGIN_MANAGER_CONFIGS_LOADED);
    }

    @Override
    public void disable() {
        RelluEssentials.getInstance().saveConfig();
    }
    
}
