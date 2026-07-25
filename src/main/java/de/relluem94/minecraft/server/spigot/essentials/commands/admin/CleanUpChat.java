package de.relluem94.minecraft.server.spigot.essentials.commands.admin;

import de.relluem94.minecraft.server.spigot.essentials.constants.MessageKey;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import static de.relluem94.minecraft.server.spigot.essentials.RelluEssentials.languageHelper;

public class CleanUpChat {

    public static void cleanChat(Player p) {
        for (Player bp : Bukkit.getOnlinePlayers()) {
            for (int i = 0; i < 100; i++) {
                bp.sendMessage("");
            }
        }
        p.sendMessage(languageHelper.getWithPrefix(MessageKey.COMMAND_ADMIN_CHAT_CLEARED));
    }
}
