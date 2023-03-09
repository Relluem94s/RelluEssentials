package de.relluem94.minecraft.server.spigot.essentials.managers;

import static de.relluem94.minecraft.server.spigot.essentials.Strings.*;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.ChatHelper.consoleSendMessage;

import java.util.Locale;
import java.util.ResourceBundle;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;

public class ConfigManager implements IEnable, IDisable {

    @Override
    public void enable() {
        consoleSendMessage(PLUGIN_NAME_CONSOLE, PLUGIN_COMMAND_COLOR + LANG_LOADING_CONFIGS);

        if (!RelluEssentials.getInstance().getDataFolder().exists()) {
            RelluEssentials.getInstance().getDataFolder().mkdir();
        }
        
        RelluEssentials.getInstance().saveDefaultConfig();
        RelluEssentials.language = RelluEssentials.getInstance().getConfig().getString("language");

        RelluEssentials.germanProperties = ResourceBundle.getBundle("lang", new Locale("de", "DE"));
        RelluEssentials.englishProperties = ResourceBundle.getBundle("lang", new Locale("en", "US"));

        consoleSendMessage(PLUGIN_NAME_CONSOLE, PLUGIN_COMMAND_COLOR + LANG_CONFIGS_LOADED);
    }

    @Override
    public void disable() {
        RelluEssentials.getInstance().saveConfig();
    }
    
}
