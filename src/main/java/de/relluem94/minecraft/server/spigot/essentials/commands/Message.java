package de.relluem94.minecraft.server.spigot.essentials.commands;

import java.util.HashMap;
import java.util.Map;

import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import de.relluem94.minecraft.server.spigot.essentials.permissions.Permission;
import de.relluem94.minecraft.server.spigot.essentials.permissions.Groups;
import org.jetbrains.annotations.NotNull;

import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.*;
import static de.relluem94.minecraft.server.spigot.essentials.constants.CommandNameConstants.PLUGIN_COMMAND_NAME_MSG;
import static de.relluem94.minecraft.server.spigot.essentials.constants.CommandNameConstants.PLUGIN_COMMAND_NAME_REPLY;
import static de.relluem94.rellulib.utils.StringUtils.*;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.StringHelper.replaceColor;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.TypeHelper.isPlayer;

public class Message implements CommandExecutor {

    public static final Map<Player, Player> reply = new HashMap<>();

    @Override
    public boolean onCommand(@NonNull CommandSender sender, @NotNull Command command, @NonNull String label, String[] args) {
        if (command.getName().equalsIgnoreCase(PLUGIN_COMMAND_NAME_MSG)) {
            if (isPlayer(sender)) {
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
        } else if (command.getName().equalsIgnoreCase(PLUGIN_COMMAND_NAME_REPLY)) {
            if (isPlayer(sender)) {
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
        if (isPlayer(sender)) {
            Player p = (Player) sender;
            if (Permission.isAuthorized(p, Groups.getGroup("user").getId())) {
                String message = implode(start, args);

                reply.remove(p);
                reply.remove(target);

                reply.put(p, target);
                reply.put(target, p);
                if (Permission.isAuthorized(p, Groups.getGroup("vip").getId())) {
                    message = replaceSymbols(replaceColor(message));
                }

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
