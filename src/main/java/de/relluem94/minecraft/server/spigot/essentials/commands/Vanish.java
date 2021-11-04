package de.relluem94.minecraft.server.spigot.essentials.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.relluem94.minecraft.server.spigot.essentials.permissions.Permission;
import de.relluem94.minecraft.server.spigot.essentials.permissions.Groups;

import static de.relluem94.minecraft.server.spigot.essentials.Strings.*;
import static de.relluem94.minecraft.server.spigot.essentials.constants.CommandNameConstants.PLUGIN_COMMAND_NAME_VANISH;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.ChatHelper.sendMessage;

public class Vanish implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase(PLUGIN_COMMAND_NAME_VANISH)) {
            if (args.length == 0) {
                if (sender instanceof Player) {
                    Player p = (Player) sender;
                    if (Permission.isAuthorized(p, Groups.getGroup("mod").getId())) {
                        sendMessage(p, PLUGIN_COMMAND_VANISH);
                        boolean canSee = false;

                        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                            if (onlinePlayer.canSee(p)) {
                                onlinePlayer.hidePlayer(p);
                            } else {
                                onlinePlayer.showPlayer(p);
                                canSee = true;
                            }
                        }

                        sendMessage(p, String.format(canSee ? PLUGIN_COMMAND_VANISH_ENABLE : PLUGIN_COMMAND_VANISH_DISABLE, p.getCustomName()));

                        return true;
                    } else {
                        p.sendMessage(PLUGIN_COMMAND_PERMISSION_MISSING);
                        return true;
                    }
                }
            } else {
                Player target = Bukkit.getPlayer(args[0]);
                if (target != null) {
                    if (sender instanceof Player) {
                        Player p = (Player) sender;
                        if (Permission.isAuthorized(p, Groups.getGroup("mod").getId())) {
                            sendMessage(target, PLUGIN_COMMAND_VANISH);

                            boolean canSee = false;
                            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                                if (onlinePlayer.canSee(target)) {
                                    onlinePlayer.hidePlayer(target);
                                } else {
                                    onlinePlayer.showPlayer(target);
                                    canSee = true;
                                }
                            }

                            sendMessage(p, String.format(canSee ? PLUGIN_COMMAND_VANISH_ENABLE : PLUGIN_COMMAND_VANISH_DISABLE, p.getCustomName()));

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