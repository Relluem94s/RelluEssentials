package de.relluem94.minecraft.server.spigot.essentials.commands;

import static de.relluem94.minecraft.server.spigot.essentials.RelluEssentials.getText;
import static de.relluem94.minecraft.server.spigot.essentials.Strings.PLUGIN_COLOR_COMMAND;
import static de.relluem94.minecraft.server.spigot.essentials.Strings.PLUGIN_COLOR_COMMAND_NAME;
import static de.relluem94.minecraft.server.spigot.essentials.Strings.PLUGIN_COMMAND_NOT_A_PLAYER;
import static de.relluem94.minecraft.server.spigot.essentials.Strings.PLUGIN_COMMAND_PERMISSION_MISSING;
import static de.relluem94.minecraft.server.spigot.essentials.Strings.PLUGIN_COMMAND_TARGET_NOT_A_PLAYER;
import static de.relluem94.minecraft.server.spigot.essentials.Strings.PLUGIN_FORMS_COMMAND_PREFIX;
import static de.relluem94.minecraft.server.spigot.essentials.constants.CommandNameConstants.PLUGIN_COMMAND_NAME_GAMEMODE_0;
import static de.relluem94.minecraft.server.spigot.essentials.constants.CommandNameConstants.PLUGIN_COMMAND_NAME_GAMEMODE_0_NAME;
import static de.relluem94.minecraft.server.spigot.essentials.constants.CommandNameConstants.PLUGIN_COMMAND_NAME_GAMEMODE_1;
import static de.relluem94.minecraft.server.spigot.essentials.constants.CommandNameConstants.PLUGIN_COMMAND_NAME_GAMEMODE_1_NAME;
import static de.relluem94.minecraft.server.spigot.essentials.constants.CommandNameConstants.PLUGIN_COMMAND_NAME_GAMEMODE_2;
import static de.relluem94.minecraft.server.spigot.essentials.constants.CommandNameConstants.PLUGIN_COMMAND_NAME_GAMEMODE_2_NAME;
import static de.relluem94.minecraft.server.spigot.essentials.constants.CommandNameConstants.PLUGIN_COMMAND_NAME_GAMEMODE_3;
import static de.relluem94.minecraft.server.spigot.essentials.constants.CommandNameConstants.PLUGIN_COMMAND_NAME_GAMEMODE_3_NAME;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.TypeHelper.isPlayer;

import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.relluem94.minecraft.server.spigot.essentials.helpers.PlayerHelper;
import de.relluem94.minecraft.server.spigot.essentials.permissions.Groups;
import de.relluem94.minecraft.server.spigot.essentials.permissions.Permission;

public class GameMode implements CommandExecutor {

    private static final String LANG_KEY = "PLUGIN_COMMAND_GAMEMODE";

    @Override
    public boolean onCommand(@NonNull CommandSender sender, @NonNull Command command, @NonNull String label, String[] args) {
        if (!Permission.isAuthorized(sender, Groups.getGroup("mod").getId())) {
            sender.sendMessage(PLUGIN_COMMAND_PERMISSION_MISSING);
            return true;
        }

        if (args.length == 1) {
            Player target = Bukkit.getPlayer(args[0]);

            if (target == null) {
                sender.sendMessage(PLUGIN_COMMAND_TARGET_NOT_A_PLAYER);
                return true;
            }
            return gameMode(command, target);
        }

        if (!isPlayer(sender)) {
            sender.sendMessage(PLUGIN_COMMAND_NOT_A_PLAYER);
            return true;
        }

        Player p = (Player) sender;

        return gameMode(command, p);
    }

    private boolean gameMode(Command command, Player p) {
        if (command.getName().equalsIgnoreCase(PLUGIN_COMMAND_NAME_GAMEMODE_0)) {           
            p.setGameMode(org.bukkit.GameMode.SURVIVAL);
            PlayerHelper.setFlying(p);
            p.sendMessage(PLUGIN_FORMS_COMMAND_PREFIX + String.format(getText(p.getLocale(), LANG_KEY), p.getCustomName() + PLUGIN_COLOR_COMMAND, PLUGIN_COLOR_COMMAND_NAME + PLUGIN_COMMAND_NAME_GAMEMODE_0_NAME + PLUGIN_COLOR_COMMAND));
            return true;
        } else if (command.getName().equalsIgnoreCase(PLUGIN_COMMAND_NAME_GAMEMODE_1)) {
            p.setGameMode(org.bukkit.GameMode.CREATIVE);
            p.sendMessage(PLUGIN_FORMS_COMMAND_PREFIX + String.format(getText(p.getLocale(), LANG_KEY), p.getCustomName() + PLUGIN_COLOR_COMMAND, PLUGIN_COLOR_COMMAND_NAME + PLUGIN_COMMAND_NAME_GAMEMODE_1_NAME + PLUGIN_COLOR_COMMAND));
            return true;
        } else if (command.getName().equalsIgnoreCase(PLUGIN_COMMAND_NAME_GAMEMODE_2)) {
            p.setGameMode(org.bukkit.GameMode.ADVENTURE);
            p.sendMessage(PLUGIN_FORMS_COMMAND_PREFIX + String.format(getText(p.getLocale(), LANG_KEY), p.getCustomName() + PLUGIN_COLOR_COMMAND, PLUGIN_COLOR_COMMAND_NAME + PLUGIN_COMMAND_NAME_GAMEMODE_2_NAME + PLUGIN_COLOR_COMMAND));
            return true;
        } else if (command.getName().equalsIgnoreCase(PLUGIN_COMMAND_NAME_GAMEMODE_3)) {
            p.setGameMode(org.bukkit.GameMode.SPECTATOR);
            p.sendMessage(PLUGIN_FORMS_COMMAND_PREFIX + String.format(getText(p.getLocale(), LANG_KEY), p.getCustomName() + PLUGIN_COLOR_COMMAND, PLUGIN_COLOR_COMMAND_NAME + PLUGIN_COMMAND_NAME_GAMEMODE_3_NAME + PLUGIN_COLOR_COMMAND));
            return true;
        } else {
            return false;
        }
    }
}
