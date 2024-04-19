package de.relluem94.minecraft.server.spigot.essentials.commands;

import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_COMMAND_NOT_A_PLAYER;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_COMMAND_PERMISSION_MISSING;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_COMMAND_TARGET_NOT_A_PLAYER;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_COMMAND_WHERE;
import static de.relluem94.minecraft.server.spigot.essentials.constants.CommandNameConstants.PLUGIN_COMMAND_NAME_WHERE;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.StringHelper.locationToString;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.TypeHelper.isPlayer;

import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.relluem94.minecraft.server.spigot.essentials.permissions.Groups;
import de.relluem94.minecraft.server.spigot.essentials.permissions.Permission;

public class Where implements CommandExecutor {

    @Override
    public boolean onCommand(@NonNull CommandSender sender, Command command, @NonNull String label, String[] args) {
        if (!command.getName().equalsIgnoreCase(PLUGIN_COMMAND_NAME_WHERE)) {
            return false;
        }

        if (args.length > 0) {
            if (!Permission.isAuthorized(sender, Groups.getGroup("mod").getId())) {
                sender.sendMessage(PLUGIN_COMMAND_PERMISSION_MISSING);
                return true;
            }

            where(sender, args[0]);
            return true;
        } 

        if (!isPlayer(sender)) {
            sender.sendMessage(PLUGIN_COMMAND_NOT_A_PLAYER);
            return true;
        }

        Player p = (Player) sender;

        if (!Permission.isAuthorized(p, Groups.getGroup("user").getId())) {
            p.sendMessage(PLUGIN_COMMAND_PERMISSION_MISSING);
            return true;
        }

        where(sender, p);
        return true;
    }

    private void where(CommandSender commandSender, String targetArg){
        Player target = Bukkit.getPlayer(targetArg);
        if (target == null) {
            commandSender.sendMessage(String.format(PLUGIN_COMMAND_TARGET_NOT_A_PLAYER, targetArg));
            return;
        }

        where(commandSender, target);
    }

    private void where(CommandSender sender, Player target){
        sender.sendMessage(String.format(PLUGIN_COMMAND_WHERE, target.getCustomName(), locationToString(target.getLocation())));
    }
}
