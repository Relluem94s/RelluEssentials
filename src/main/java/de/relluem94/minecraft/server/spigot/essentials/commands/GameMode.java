package de.relluem94.minecraft.server.spigot.essentials.commands;

import static de.relluem94.minecraft.server.spigot.essentials.RelluEssentials.getText;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.relluem94.minecraft.server.spigot.essentials.permissions.Permission;
import de.relluem94.minecraft.server.spigot.essentials.permissions.Groups;
import de.relluem94.minecraft.server.spigot.essentials.helpers.PlayerHelper;

import static de.relluem94.minecraft.server.spigot.essentials.Strings.*;
import static de.relluem94.minecraft.server.spigot.essentials.constants.CommandNameConstants.PLUGIN_COMMAND_NAME_GAMEMODE_0;
import static de.relluem94.minecraft.server.spigot.essentials.constants.CommandNameConstants.PLUGIN_COMMAND_NAME_GAMEMODE_0_NAME;
import static de.relluem94.minecraft.server.spigot.essentials.constants.CommandNameConstants.PLUGIN_COMMAND_NAME_GAMEMODE_1;
import static de.relluem94.minecraft.server.spigot.essentials.constants.CommandNameConstants.PLUGIN_COMMAND_NAME_GAMEMODE_1_NAME;
import static de.relluem94.minecraft.server.spigot.essentials.constants.CommandNameConstants.PLUGIN_COMMAND_NAME_GAMEMODE_2;
import static de.relluem94.minecraft.server.spigot.essentials.constants.CommandNameConstants.PLUGIN_COMMAND_NAME_GAMEMODE_2_NAME;
import static de.relluem94.minecraft.server.spigot.essentials.constants.CommandNameConstants.PLUGIN_COMMAND_NAME_GAMEMODE_3;
import static de.relluem94.minecraft.server.spigot.essentials.constants.CommandNameConstants.PLUGIN_COMMAND_NAME_GAMEMODE_3_NAME;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.TypeHelper.isPlayer;

public class GameMode implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            if (isPlayer(sender)) {
                Player p = (Player) sender;
                if (Permission.isAuthorized(p, Groups.getGroup("mod").getId())) {
                    return gameMode(command, p);
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
        if (command.getName().equalsIgnoreCase(PLUGIN_COMMAND_NAME_GAMEMODE_0)) {
            p.setGameMode(org.bukkit.GameMode.SURVIVAL);
            PlayerHelper.setFlying(p);
            p.sendMessage(PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + String.format(getText("PLUGIN_COMMAND_GAMEMODE"), p.getCustomName() + PLUGIN_COMMAND_COLOR, PLUGIN_COMMAND_NAME_COLOR + PLUGIN_COMMAND_NAME_GAMEMODE_0_NAME + PLUGIN_COMMAND_COLOR));
            return true;
        } else if (command.getName().equalsIgnoreCase(PLUGIN_COMMAND_NAME_GAMEMODE_1)) {
            p.setGameMode(org.bukkit.GameMode.CREATIVE);
            p.sendMessage(PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + String.format(getText("PLUGIN_COMMAND_GAMEMODE"), p.getCustomName() + PLUGIN_COMMAND_COLOR, PLUGIN_COMMAND_NAME_COLOR + PLUGIN_COMMAND_NAME_GAMEMODE_1_NAME + PLUGIN_COMMAND_COLOR));
            return true;
        } else if (command.getName().equalsIgnoreCase(PLUGIN_COMMAND_NAME_GAMEMODE_2)) {
            p.setGameMode(org.bukkit.GameMode.ADVENTURE);
            p.sendMessage(PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + String.format(getText("PLUGIN_COMMAND_GAMEMODE"), p.getCustomName() + PLUGIN_COMMAND_COLOR, PLUGIN_COMMAND_NAME_COLOR + PLUGIN_COMMAND_NAME_GAMEMODE_2_NAME + PLUGIN_COMMAND_COLOR));
            return true;
        } else if (command.getName().equalsIgnoreCase(PLUGIN_COMMAND_NAME_GAMEMODE_3)) {
            p.setGameMode(org.bukkit.GameMode.SPECTATOR);
            p.sendMessage(PLUGIN_PREFIX + PLUGIN_SPACER + PLUGIN_COMMAND_COLOR + String.format(getText("PLUGIN_COMMAND_GAMEMODE"), p.getCustomName() + PLUGIN_COMMAND_COLOR, PLUGIN_COMMAND_NAME_COLOR + PLUGIN_COMMAND_NAME_GAMEMODE_3_NAME + PLUGIN_COMMAND_COLOR));
            return true;
        } else {
            return false;
        }
    }
}