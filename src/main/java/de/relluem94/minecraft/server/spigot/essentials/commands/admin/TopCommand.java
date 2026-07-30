package de.relluem94.minecraft.server.spigot.essentials.commands.admin;

import de.relluem94.minecraft.server.spigot.essentials.commands.Admin;
import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.SubCommand;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;

import static de.relluem94.minecraft.server.spigot.essentials.RelluEssentials.languageHelper;

public class TopCommand implements SubCommand {

    @Override
    public void execute(Player player, String[] args) {
        Location l = player.getWorld().getHighestBlockAt(player.getLocation()).getLocation().add(0, 1, 0);
        player.sendMessage(languageHelper.getWithPrefix(MessageKey.COMMAND_ADMIN_TOP));
        player.teleport(l);
    }

    @Override
    public boolean matches(String @NonNull [] args) {
        return args.length == 1 && Admin.Commands.TOP.getName().equalsIgnoreCase(args[0]);
    }
}