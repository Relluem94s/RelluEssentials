package de.relluem94.minecraft.server.spigot.essentials.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.relluem94.minecraft.server.spigot.essentials.permissions.Permission;

import static de.relluem94.minecraft.server.spigot.essentials.Strings.*;
import de.relluem94.minecraft.server.spigot.essentials.permissions.enums.Groups;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;

public class Rellu implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        switch (args.length) {
            case 1:
                if (sender instanceof Player) {
                    Player p = (Player) sender;
                    if (Permission.isAuthorized(p, Groups.ADMIN.getId())) {
                        if (args[0].equalsIgnoreCase("ping")) {
                            int ping = ((CraftPlayer) p).getHandle().ping;
                            p.sendMessage(String.format(PLUGIN_COMMAND_RELLU_PING, ping));
                            return true;
                            
                        } else {
                            p.sendMessage(PLUGIN_COMMAND_RELLU_WRONG_COMMAND);
                            return true;
                        }
                    } else {
                        p.sendMessage(PLUGIN_COMMAND_PERMISSION_MISSING);
                        return true;
                    }
                }   break;
            case 2:
                if (args[0].equalsIgnoreCase("ping")) {
                    Player target = Bukkit.getPlayer(args[0]);
                    if (target != null) {
                        if (sender instanceof Player) {
                            Player p = (Player) sender;
                            int ping = ((CraftPlayer) target).getHandle().ping;
                            p.sendMessage(String.format(PLUGIN_COMMAND_RELLU_PING_OTHER, target.getCustomName(), ping));
                            return true;
                        }
                    }
                }   break;
            default:
                if (sender instanceof Player) {
                    Player p = (Player) sender;
                    if (Permission.isAuthorized(p, Groups.ADMIN.getId())) {
                        p.sendMessage(PLUGIN_COMMAND_RELLU_OPTIONS);
                        return true;
                    } else {
                        p.sendMessage(PLUGIN_COMMAND_PERMISSION_MISSING);
                        return true;
                    }
                }   break;
        }
        return false;
    }
}
