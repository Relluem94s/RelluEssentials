package main.java.de.relluem94.minecraft.server.spigot.essentials.commands;

import java.util.HashMap;
import java.util.Map;
import static main.java.de.relluem94.minecraft.server.spigot.essentials.Strings.*;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.de.relluem94.minecraft.server.spigot.essentials.permissions.Permission;
import main.java.de.relluem94.minecraft.server.spigot.essentials.permissions.enums.Groups;
import org.bukkit.command.ConsoleCommandSender;

public class Message implements CommandExecutor {

    public static Map<Player, Player> reply = new HashMap<>();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("msg")) {
            if (sender instanceof Player) {
                Player p = (Player) sender;
                if (args.length > 1) {
                    Player target = Bukkit.getPlayer(args[0]);
                    if (target != null) {
                        return msg(sender, target, args, 1);
                    } else {
                        sender.sendMessage(PLUGIN_COMMAND_MSG_PLAYER_OFFLINE);
                        return true;
                    }
                } else {
                    p.sendMessage(PLUGIN_COMMAND_MSG_INFO);
                    return true;
                }
            }
        } else if (command.getName().equalsIgnoreCase("r")) {
            if (sender instanceof Player) {
                Player p = (Player) sender;
                if (reply.containsKey(p)) {
                    Player target = reply.get(p);
                    if (target.isOnline()) {
                        if (args.length > 0) {
                            return msg(sender, target, args, 0);
                        } else {
                            p.sendMessage(PLUGIN_COMMAND_TO_LESS_ARGUMENTS);
                            return true;
                        }
                    } else {
                        p.sendMessage(PLUGIN_COMMAND_MSG_PLAYER_OFFLINE);
                        return true;
                    }
                } else {
                    p.sendMessage(PLUGIN_COMMAND_MSG_NO_ONE_TO_REPLY);
                    return true;
                }
            }
        }
        return false;
    }

    private boolean msg(CommandSender sender, Player target, String[] args, int start) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (Permission.isAuthorized(p, Groups.USER.getId())) {
                String message = "";
                for (int i = start; args.length > i; i++) {
                    if (args[i] == null) {
                        break;
                    }
                    message += args[i] + " ";
                }

                if (reply.containsKey(p)) {
                    reply.remove(p);
                }
                if (reply.containsKey(target)) {
                    reply.remove(target);
                }

                reply.put(p, target);
                reply.put(target, p);

                target.sendMessage(p.getCustomName() + PLUGIN_COMMAND_MSG_SPACER_IN + message);
                p.sendMessage(target.getCustomName() + PLUGIN_COMMAND_MSG_SPACER_OUT + message);
                return true;
            } else {
                p.sendMessage(PLUGIN_COMMAND_PERMISSION_MISSING);
                return true;
            }
        } else {
            if (sender instanceof ConsoleCommandSender) {
                sender.sendMessage(PLUGIN_COMMAND_NOT_A_PLAYER);
                return true;
            } else {
                return false;
            }
        }
    }
}
