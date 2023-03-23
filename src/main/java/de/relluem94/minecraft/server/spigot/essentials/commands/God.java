package de.relluem94.minecraft.server.spigot.essentials.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.relluem94.minecraft.server.spigot.essentials.permissions.Permission;
import de.relluem94.minecraft.server.spigot.essentials.permissions.Groups;

import static de.relluem94.minecraft.server.spigot.essentials.Strings.*;
import static de.relluem94.minecraft.server.spigot.essentials.constants.CommandNameConstants.PLUGIN_COMMAND_NAME_GOD;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.TypeHelper.isPlayer;

public class God implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!command.getName().equalsIgnoreCase(PLUGIN_COMMAND_NAME_GOD)) {
            return false;
        }

        if (!isPlayer(sender)) {
            sender.sendMessage(PLUGIN_COMMAND_NOT_A_PLAYER);
            return true;
        }

        Player p = (Player) sender;
            
        if (!Permission.isAuthorized(p, Groups.getGroup("mod").getId())) {
            p.sendMessage(PLUGIN_COMMAND_PERMISSION_MISSING);
            return true;
            
        }

        if (args.length > 0) {
            p.sendMessage(PLUGIN_COMMAND_TO_MANY_ARGUMENTS);
            return true;   
        }

        p.sendMessage(!p.isInvulnerable() ? PLUGIN_COMMAND_GOD_ON : PLUGIN_COMMAND_GOD_OFF);
        p.setInvulnerable(!p.isInvulnerable());
        return true;
    }
}