package de.relluem94.minecraft.server.spigot.essentials.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.relluem94.minecraft.server.spigot.essentials.permissions.Permission;
import de.relluem94.minecraft.server.spigot.essentials.permissions.Groups;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.PlayerEntry;

import static de.relluem94.minecraft.server.spigot.essentials.RelluEssentials.playerEntryList;
import static de.relluem94.minecraft.server.spigot.essentials.Strings.*;
import static de.relluem94.minecraft.server.spigot.essentials.constants.CommandNameConstants.PLUGIN_COMMAND_NAME_PURSE;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.TypeHelper.isPlayer;

public class Purse implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            if (isPlayer(sender)) {
                Player p = (Player) sender;
                if (Permission.isAuthorized(p, Groups.getGroup("user").getId())) {
                    if (command.getName().equalsIgnoreCase(PLUGIN_COMMAND_NAME_PURSE)) {
                        PlayerEntry pe = playerEntryList.get(p.getUniqueId());
                        p.sendMessage(String.format(PLUGIN_COMMAND_PURSE_TOTAL, String.format("%.2f", pe.getPurse())));
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    p.sendMessage(PLUGIN_COMMAND_PERMISSION_MISSING);
                    return true;
                }
            }
        } else {
            Player target = Bukkit.getPlayer(args[0]);
            if (target != null) {
                if (isPlayer(sender)) {
                    Player p = (Player) sender;
                    if (Permission.isAuthorized(p, Groups.getGroup("mod").getId())) {
                        
                        if (command.getName().equalsIgnoreCase(PLUGIN_COMMAND_NAME_PURSE)) {
                            PlayerEntry pe = playerEntryList.get(target.getUniqueId());
                            p.sendMessage(String.format(PLUGIN_COMMAND_PURSE_TOTAL_OTHER, String.format("%.2f", pe.getPurse())));
                            return true;
                        } else {
                            return false;
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
