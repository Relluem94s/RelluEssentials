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

    private static final String LANG_KEY = "PLUGIN_COMMAND_GAMEMODE";

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!isPlayer(sender)) {
            sender.sendMessage(PLUGIN_COMMAND_NOT_A_PLAYER);
            return true;
        }

        Player p = (Player) sender;

        if (!Permission.isAuthorized(p, Groups.getGroup("mod").getId())) {
            p.sendMessage(PLUGIN_COMMAND_PERMISSION_MISSING);
            return true;
        }

        if (args.length == 0) {
            return gameMode(command, p);
        }

        Player target = Bukkit.getPlayer(args[0]);

        if (target == null) {
            p.sendMessage(PLUGIN_COMMAND_TARGET_NOT_A_PLAYER);
            return true;
        }

        return gameMode(command, target);
    }

    private boolean gameMode(Command command, Player p) {
        if (command.getName().equalsIgnoreCase(PLUGIN_COMMAND_NAME_GAMEMODE_0)) {
            p.setGameMode(org.bukkit.GameMode.SURVIVAL);
            PlayerHelper.setFlying(p);
            p.sendMessage(PLUGIN_FORMS_COMMAND_PREFIX + String.format(getText(LANG_KEY), p.getCustomName() + PLUGIN_COLOR_COMMAND, PLUGIN_COLOR_COMMAND_NAME + PLUGIN_COMMAND_NAME_GAMEMODE_0_NAME + PLUGIN_COLOR_COMMAND));
            return true;
        } else if (command.getName().equalsIgnoreCase(PLUGIN_COMMAND_NAME_GAMEMODE_1)) {
            p.setGameMode(org.bukkit.GameMode.CREATIVE);
            p.sendMessage(PLUGIN_FORMS_COMMAND_PREFIX + String.format(getText(LANG_KEY), p.getCustomName() + PLUGIN_COLOR_COMMAND, PLUGIN_COLOR_COMMAND_NAME + PLUGIN_COMMAND_NAME_GAMEMODE_1_NAME + PLUGIN_COLOR_COMMAND));
            return true;
        } else if (command.getName().equalsIgnoreCase(PLUGIN_COMMAND_NAME_GAMEMODE_2)) {
            p.setGameMode(org.bukkit.GameMode.ADVENTURE);
            p.sendMessage(PLUGIN_FORMS_COMMAND_PREFIX + String.format(getText(LANG_KEY), p.getCustomName() + PLUGIN_COLOR_COMMAND, PLUGIN_COLOR_COMMAND_NAME + PLUGIN_COMMAND_NAME_GAMEMODE_2_NAME + PLUGIN_COLOR_COMMAND));
            return true;
        } else if (command.getName().equalsIgnoreCase(PLUGIN_COMMAND_NAME_GAMEMODE_3)) {
            p.setGameMode(org.bukkit.GameMode.SPECTATOR);
            p.sendMessage(PLUGIN_FORMS_COMMAND_PREFIX + String.format(getText(LANG_KEY), p.getCustomName() + PLUGIN_COLOR_COMMAND, PLUGIN_COLOR_COMMAND_NAME + PLUGIN_COMMAND_NAME_GAMEMODE_3_NAME + PLUGIN_COLOR_COMMAND));
            return true;
        } else {
            return false;
        }
    }
}
