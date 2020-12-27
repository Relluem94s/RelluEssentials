package main.java.de.relluem94.minecraft.server.spigot.essentials.commands;

import static main.java.de.relluem94.minecraft.server.spigot.essentials.Strings.*;
import static main.java.de.relluem94.minecraft.server.spigot.essentials.helpers.StringHelper.implode;
import static main.java.de.relluem94.minecraft.server.spigot.essentials.helpers.StringHelper.replaceColor;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.de.relluem94.minecraft.server.spigot.essentials.permissions.Permission;
import main.java.de.relluem94.minecraft.server.spigot.essentials.permissions.enums.Groups;

public class Title implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("title")) {
            if (args.length > 2) {
                Player target = Bukkit.getOfflinePlayer(args[0]).getPlayer();
                if (target != null) {
                    if (sender instanceof Player) {
                        Player p = (Player) sender;
                        if (Permission.isAuthorized(p, Groups.MOD.getId())) {
                            target.sendTitle(replaceColor(args[1]), replaceColor(implode(2, args)), 5, 80, 5);
                            return true;
                        } else {
                            p.sendMessage(PLUGIN_COMMAND_PERMISSION_MISSING);
                            return true;
                        }
                    }

                }
            } else {
               
            }
        }
        return false;
    }
}
