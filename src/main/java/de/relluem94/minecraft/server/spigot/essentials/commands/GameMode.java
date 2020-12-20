package main.java.de.relluem94.minecraft.server.spigot.essentials.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.de.relluem94.minecraft.server.spigot.essentials.permissions.Permission;

import static main.java.de.relluem94.minecraft.server.spigot.essentials.Strings.*;
import main.java.de.relluem94.minecraft.server.spigot.essentials.permissions.enums.Groups;

public class GameMode implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            if (sender instanceof Player) {
                Player p = (Player) sender;
                if (Permission.isAuthorized(p, Groups.MOD.getId())) {
                    return gameMode(command, p);
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
                    if (Permission.isAuthorized(p, Groups.MOD.getId())) {
                        return gameMode(command, target);
                    } else {
                        p.sendMessage(PLUGIN_COMMAND_PERMISSION_MISSING);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean gameMode(Command command, Player p) {
        if (command.getName().equalsIgnoreCase("0")) {
            p.setGameMode(org.bukkit.GameMode.SURVIVAL);
            p.sendMessage(String.format(PLUGIN_COMMAND_GAMEMODE, p.getCustomName(), "Survival"));
            return true;
        } else if (command.getName().equalsIgnoreCase("1")) {
            p.setGameMode(org.bukkit.GameMode.CREATIVE);
            p.sendMessage(String.format(PLUGIN_COMMAND_GAMEMODE, p.getCustomName(), "Creative"));
            return true;
        } else if (command.getName().equalsIgnoreCase("2")) {
            p.setGameMode(org.bukkit.GameMode.ADVENTURE);
            p.sendMessage(String.format(PLUGIN_COMMAND_GAMEMODE, p.getCustomName(), "Adventure"));
            return true;
        } else if (command.getName().equalsIgnoreCase("3")) {
            p.setGameMode(org.bukkit.GameMode.SPECTATOR);
            p.sendMessage(String.format(PLUGIN_COMMAND_GAMEMODE, p.getCustomName(), "Spectator"));
            return true;
        } else {
            return false;
        }
    }

}
