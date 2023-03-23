package de.relluem94.minecraft.server.spigot.essentials.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.attribute.Attribute;

import de.relluem94.minecraft.server.spigot.essentials.permissions.Permission;
import de.relluem94.minecraft.server.spigot.essentials.permissions.Groups;

import static de.relluem94.minecraft.server.spigot.essentials.Strings.*;
import static de.relluem94.minecraft.server.spigot.essentials.constants.CommandNameConstants.PLUGIN_COMMAND_NAME_HEAL;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.TypeHelper.isPlayer;

public class Heal implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase(PLUGIN_COMMAND_NAME_HEAL)) {
            return false;
        }

        if (!isPlayer(sender)) {
            sender.sendMessage(PLUGIN_COMMAND_NOT_A_PLAYER);
            return true;
        }

        Player p = (Player) sender;
        
        if (Permission.isAuthorized(p, Groups.getGroup("mod").getId())) {
            p.sendMessage(PLUGIN_COMMAND_PERMISSION_MISSING);
            return true;
        }

        if (args.length > 0) {
            p.sendMessage(PLUGIN_COMMAND_TO_MANY_ARGUMENTS);
            return true;   
        }

        p.setHealth(p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getDefaultValue());
        p.setFoodLevel(20);
        p.sendMessage(PLUGIN_COMMAND_HEAL);
        return true;
    }
}
