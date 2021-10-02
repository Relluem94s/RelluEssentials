package de.relluem94.minecraft.server.spigot.essentials.commands;

import static de.relluem94.minecraft.server.spigot.essentials.RelluEssentials.dBH;
import static de.relluem94.minecraft.server.spigot.essentials.RelluEssentials.playerEntryList;
import static de.relluem94.minecraft.server.spigot.essentials.Strings.*;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.PlayerEntry;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.relluem94.minecraft.server.spigot.essentials.permissions.Permission;
import de.relluem94.minecraft.server.spigot.essentials.permissions.Groups;

public class Fly implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            if (sender instanceof Player) {
                Player p = (Player) sender;
                if (Permission.isAuthorized(p, Groups.getGroup("vip").getId())) {
                    return flyMode(command, p);
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
                        p.sendMessage(String.format(PLUGIN_COMMAND_FLYMODE, target.getCustomName(), !target.getAllowFlight() ? PLUGIN_COMMAND_FLYMODE_ACTIVATED : PLUGIN_COMMAND_FLYMODE_DEACTIVATED));
                        return flyMode(command, target);
                    } else {
                        p.sendMessage(PLUGIN_COMMAND_PERMISSION_MISSING);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean flyMode(Command command, Player p) {
        if (command.getName().equalsIgnoreCase("fly")) {
            PlayerEntry pe = playerEntryList.get(p.getUniqueId());
            pe.setFly(!pe.isFlying());
            dBH.updatePlayer(pe);
            p.setAllowFlight(pe.isFlying());
            p.sendMessage(String.format(PLUGIN_COMMAND_FLYMODE, p.getCustomName(), pe.isFlying() ? PLUGIN_COMMAND_FLYMODE_ACTIVATED : PLUGIN_COMMAND_FLYMODE_DEACTIVATED));
            return true;
        } else {
            return false;
        }
    }

}
