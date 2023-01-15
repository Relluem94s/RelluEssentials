package de.relluem94.minecraft.server.spigot.essentials.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.relluem94.minecraft.server.spigot.essentials.permissions.Permission;
import de.relluem94.minecraft.server.spigot.essentials.permissions.Groups;

import static de.relluem94.minecraft.server.spigot.essentials.Strings.*;
import static de.relluem94.minecraft.server.spigot.essentials.constants.CommandNameConstants.PLUGIN_COMMAND_NAME_ENDERCHEST;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.TypeHelper.isPlayer;

public class Enderchest implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase(PLUGIN_COMMAND_NAME_ENDERCHEST)) {
            if (args.length == 0) {
                if (isPlayer(sender)) {
                    Player p = (Player) sender;
                    if (Permission.isAuthorized(p, Groups.getGroup("vip").getId())) {
                        p.openInventory(p.getEnderChest());
                        p.sendMessage(PLUGIN_COMMAND_ENDERCHEST);
                        return true;
                    } else {
                        p.sendMessage(PLUGIN_COMMAND_PERMISSION_MISSING);
                        return true;
                    }
                }
            } else {
                Player target = Bukkit.getPlayer(args[0]).getPlayer();
                if (target != null) {
                    if (isPlayer(sender)) {
                        Player p = (Player) sender;
                        if (Permission.isAuthorized(p, Groups.getGroup("mod").getId())) {
                            p.openInventory(target.getEnderChest());
                            p.sendMessage(String.format(PLUGIN_COMMAND_ENDERCHEST_PLAYER, target.getCustomName()));
                            return true;
                        } else {
                            p.sendMessage(PLUGIN_COMMAND_PERMISSION_MISSING);
                            return true;
                        }
                    }

                }
            }
        }
        return false;
    }
}
