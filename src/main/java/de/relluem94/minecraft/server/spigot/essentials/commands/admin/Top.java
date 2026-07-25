package de.relluem94.minecraft.server.spigot.essentials.commands.admin;

import de.relluem94.minecraft.server.spigot.essentials.constants.MessageKey;
import lombok.NonNull;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import static de.relluem94.minecraft.server.spigot.essentials.RelluEssentials.languageHelper;

public class Top {
    public static void top(@NonNull Player p) {
        Location l = p.getWorld().getHighestBlockAt(p.getLocation()).getLocation().add(0, 1, 0);
        p.sendMessage(languageHelper.getWithPrefix(MessageKey.COMMAND_ADMIN_TOP));
        p.teleport(l);
    }
}
