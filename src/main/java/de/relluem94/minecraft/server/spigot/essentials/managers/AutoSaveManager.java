package de.relluem94.minecraft.server.spigot.essentials.managers;

import static de.relluem94.minecraft.server.spigot.essentials.RelluEssentials.languageHelper;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_NAME_CONSOLE;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.ChatHelper.consoleSendMessage;

import de.relluem94.minecraft.server.spigot.essentials.constants.MessageKey;
import org.bukkit.scheduler.BukkitRunnable;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.helpers.BagHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.PlayerHelper;

public class AutoSaveManager implements IEnable, IDisable {

    public static final long AUTO_SAVE_MINUTES = 2;

    @Override
    public void enable() {
        consoleSendMessage(PLUGIN_NAME_CONSOLE, languageHelper.get(MessageKey.PLUGIN_MANAGER_REGISTER_AUTOSAVE));
        new BukkitRunnable() {
            @Override
            public void run() {
                BagHelper.saveBags();
            }
        }.runTaskTimer(RelluEssentials.getInstance(), 0L,  20 * 60 * AUTO_SAVE_MINUTES);

        new BukkitRunnable() {
            @Override
            public void run() {               
                PlayerHelper.savePlayers();
            }
        }.runTaskTimer(RelluEssentials.getInstance(), 0L,  20 *  60 * AUTO_SAVE_MINUTES);

        new BukkitRunnable() {
            @Override
            public void run() {               
                PlayerHelper.savePlayersInv();
            }
        }.runTaskTimer(RelluEssentials.getInstance(), 0L,  20 *  60 * AUTO_SAVE_MINUTES);

        consoleSendMessage(PLUGIN_NAME_CONSOLE, languageHelper.get(MessageKey.PLUGIN_MANAGER_AUTOSAVE_REGISTERED));
    }

    @Override
    public void disable() {
        BagHelper.saveBags();
        PlayerHelper.savePlayers();
        PlayerHelper.savePlayersInv();
    }
}