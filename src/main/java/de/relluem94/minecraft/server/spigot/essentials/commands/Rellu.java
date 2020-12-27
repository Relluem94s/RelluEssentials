package main.java.de.relluem94.minecraft.server.spigot.essentials.commands;

import main.java.de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.de.relluem94.minecraft.server.spigot.essentials.permissions.Permission;

import static main.java.de.relluem94.minecraft.server.spigot.essentials.Strings.*;
import main.java.de.relluem94.minecraft.server.spigot.essentials.helpers.PlayerHelper;
import main.java.de.relluem94.minecraft.server.spigot.essentials.permissions.enums.Groups;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;

public class Rellu implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1) {
            if (sender instanceof Player) {
                Player p = (Player) sender;
                if (Permission.isAuthorized(p, Groups.ADMIN.getId())) {
                    if (args[0].equalsIgnoreCase("save")) {
                        RelluEssentials.saveConfigs();
                        p.sendMessage(PLUGIN_COMMAND_RELLU_SAVE);
                        return true;
                    } else if (args[0].equalsIgnoreCase("reload")) {
                        RelluEssentials.reloadConfigs();
                        p.sendMessage(PLUGIN_COMMAND_RELLU_RELOAD);
                        return true;
                    } else if (args[0].equalsIgnoreCase("tab")) {
                        PlayerHelper.sendTablist(p, "§aTest §dHeader", "§cTest §5footer");
                        return true;
                    } else if (args[0].equalsIgnoreCase("ping")) {
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
            }
        } else if (args.length == 2) {
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
            }
        } else {
            if (sender instanceof Player) {
                Player p = (Player) sender;
                if (Permission.isAuthorized(p, Groups.ADMIN.getId())) {
                    p.sendMessage(PLUGIN_COMMAND_RELLU_OPTIONS);
                    return true;
                } else {
                    p.sendMessage(PLUGIN_COMMAND_PERMISSION_MISSING);
                    return true;
                }
            }
        }
        return false;
    }
}
