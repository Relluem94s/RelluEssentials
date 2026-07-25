package de.relluem94.minecraft.server.spigot.essentials.commands.admin;

import de.relluem94.minecraft.server.spigot.essentials.constants.MessageKey;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import static de.relluem94.minecraft.server.spigot.essentials.RelluEssentials.languageHelper;

public class Ping {
    public static void ping(String @NonNull [] args, Player p) {
        Player target = Bukkit.getPlayer(args[1]);
        if (target == null) {
            p.sendMessage(languageHelper.getWithPrefix(MessageKey.COMMAND_ADMIN_PING_OTHER_NOT_FOUND, args[1]));
            return;
        }

        p.sendMessage(languageHelper.getWithPrefix(MessageKey.COMMAND_ADMIN_PING_OTHER,
                target.getCustomName(), target.getPing()));
    }
}
