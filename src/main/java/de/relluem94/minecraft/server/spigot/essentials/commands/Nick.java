package de.relluem94.minecraft.server.spigot.essentials.commands;

import static de.relluem94.minecraft.server.spigot.essentials.RelluEssentials.players;
import static de.relluem94.minecraft.server.spigot.essentials.Strings.*;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.relluem94.minecraft.server.spigot.essentials.permissions.Permission;
import de.relluem94.minecraft.server.spigot.essentials.permissions.User;
import de.relluem94.minecraft.server.spigot.essentials.permissions.enums.Groups;

public class Nick implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("nick")) {
            if (args.length == 2) {
                if (sender instanceof Player) {
                    Player p = (Player) sender;
                    if (Permission.isAuthorized(p, Groups.ADMIN.getId())) {
                        Player target = Bukkit.getOfflinePlayer(args[0]).getPlayer();
                        if (target != null) {
                            target.setCustomName(User.getUserByPlayerName(target.getName()).getGroup().getPrefix() + args[1]);
                            p.sendMessage(String.format(PLUGIN_COMMAND_NICK, target.getName()));
                            players.getConfig().set("player." + target.getUniqueId() + ".customname", args[1]);
                            return true;
                        }
                    } else {
                        p.sendMessage(PLUGIN_COMMAND_PERMISSION_MISSING);
                        return true;
                    }
                }
            }
        }

        return false;
    }

}
