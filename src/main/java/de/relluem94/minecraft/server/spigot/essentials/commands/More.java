package main.java.de.relluem94.minecraft.server.spigot.essentials.commands;

import static main.java.de.relluem94.minecraft.server.spigot.essentials.Strings.*;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.de.relluem94.minecraft.server.spigot.essentials.permissions.Permission;

public class More implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("more")) {
            if (args.length == 0) {
                if (sender instanceof Player) {
                    Player p = (Player) sender;
                    if (Permission.isAuthorized(p, 4)) {
                        p.getInventory().getItemInMainHand().setAmount(64);
                        p.sendMessage(String.format(PLUGIN_COMMAND_MORE, p.getInventory().getItemInMainHand().getType().toString()));
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
                        if (Permission.isAuthorized(p, 4)) {
                            target.getInventory().getItemInMainHand().setAmount(64);
                            p.sendMessage(String.format(PLUGIN_COMMAND_MORE, target.getInventory().getItemInMainHand().getType().toString()));
                            target.sendMessage(String.format(PLUGIN_COMMAND_MORE_PLAYER, target.getInventory().getItemInMainHand().getType().toString()));
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
