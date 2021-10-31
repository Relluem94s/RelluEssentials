package de.relluem94.minecraft.server.spigot.essentials.commands;

import static de.relluem94.minecraft.server.spigot.essentials.Strings.*;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.ChatHelper.sendMessage;
import de.relluem94.minecraft.server.spigot.essentials.helpers.PlayerHelper;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.TypeHelper.isPlayer;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.relluem94.minecraft.server.spigot.essentials.permissions.Permission;
import de.relluem94.minecraft.server.spigot.essentials.permissions.Groups;

public class AFK implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase(PLUGIN_COMMAND_NAME_AFK)) {
            if (args.length == 0) {
                if (isPlayer(sender)) {
                    Player p = (Player) sender;
                    if (Permission.isAuthorized(p, Groups.getGroup("user").getId())) {
                        return PlayerHelper.setAFK(p, false);
                    } else {
                        sendMessage(p, PLUGIN_COMMAND_PERMISSION_MISSING);
                        return true;
                    }
                }
            } else {
                Player target = Bukkit.getPlayer(args[0]);
                if (target != null) {
                    if (isPlayer(sender)) {
                        Player p = (Player) sender;
                        if (Permission.isAuthorized(p, Groups.getGroup("mod").getId())) {
                            return PlayerHelper.setAFK(target, false);
                        } else {
                            sendMessage(p, PLUGIN_COMMAND_PERMISSION_MISSING);
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
}
