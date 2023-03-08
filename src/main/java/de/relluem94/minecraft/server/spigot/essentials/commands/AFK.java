package de.relluem94.minecraft.server.spigot.essentials.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.relluem94.minecraft.server.spigot.essentials.permissions.Permission;
import de.relluem94.minecraft.server.spigot.essentials.permissions.Groups;
import de.relluem94.minecraft.server.spigot.essentials.helpers.PlayerHelper;

import static de.relluem94.minecraft.server.spigot.essentials.Strings.*;
import static de.relluem94.minecraft.server.spigot.essentials.constants.CommandNameConstants.PLUGIN_COMMAND_NAME_AFK;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.ChatHelper.sendMessage;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.TypeHelper.isPlayer;

public class AFK implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!command.getName().equalsIgnoreCase(PLUGIN_COMMAND_NAME_AFK)) {
            return false;
        }

        Player p = null;

        if (isPlayer(sender)) {
            p = (Player) sender;
        }

        if(p == null){
            return false;
        }

        if (!Permission.isAuthorized(p, Groups.getGroup("user").getId())) {
            sendMessage(p, PLUGIN_COMMAND_PERMISSION_MISSING);
            return true;
        }

        if (args.length == 0) {
            PlayerHelper.setAFK(p, false);
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);

        if (target == null) {
            return false;
        }

        if (!Permission.isAuthorized(p, Groups.getGroup("mod").getId())) {
            sendMessage(p, PLUGIN_COMMAND_PERMISSION_MISSING);
            return true;
        }

        if (args.length == 1) {
            PlayerHelper.setAFK(target, false);
            return true;
        }

        return false;
    }
}