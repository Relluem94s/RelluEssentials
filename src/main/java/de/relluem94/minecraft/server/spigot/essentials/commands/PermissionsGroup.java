package de.relluem94.minecraft.server.spigot.essentials.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.relluem94.minecraft.server.spigot.essentials.permissions.Permission;
import de.relluem94.minecraft.server.spigot.essentials.permissions.Groups;
import de.relluem94.minecraft.server.spigot.essentials.helpers.PlayerHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.GroupEntry;

import static de.relluem94.minecraft.server.spigot.essentials.Strings.*;
import static de.relluem94.minecraft.server.spigot.essentials.constants.CommandNameConstants.PLUGIN_COMMAND_NAME_SETGROUP;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.TypeHelper.isCMDBlock;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.TypeHelper.isConsole;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.TypeHelper.isPlayer;

public class PermissionsGroup implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!command.getName().equalsIgnoreCase(PLUGIN_COMMAND_NAME_SETGROUP)) {
            return false;
        }

        if (args.length < 2) {
            sender.sendMessage(PLUGIN_COMMAND_TO_LESS_ARGUMENTS);
            return true;
        }

        if (args.length > 2) {
            sender.sendMessage(PLUGIN_COMMAND_TO_MANY_ARGUMENTS);
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);
        
        if (target == null) {
            sender.sendMessage(PLUGIN_COMMAND_NOT_A_PLAYER);
            return true;
        }

        if (isPlayer(sender)) {
            Player p = (Player) sender;
            GroupEntry g = Groups.getGroup(args[1]);
            
            if(g == null){
                p.sendMessage(PLUGIN_COMMAND_SETGROUP_GROUP_NOT_FOUND);
                return true;
            }

            if (!Permission.isAuthorized(p, Groups.getGroup("mod").getId())) {
                p.sendMessage(PLUGIN_COMMAND_PERMISSION_MISSING);
                return true;
            }

            p.sendMessage(PLUGIN_COMMAND_SETGROUP);
            PlayerHelper.updateGroup(target, g);
            return true;
        }
        else if (isCMDBlock(sender) || isConsole(sender)) {
            GroupEntry g = Groups.getGroup(args[1]);

            if(g == null){
                sender.sendMessage(PLUGIN_COMMAND_SETGROUP_GROUP_NOT_FOUND);
                return true;
            }

            sender.sendMessage(PLUGIN_COMMAND_SETGROUP);
            PlayerHelper.updateGroup(target, g);
            return true;
        }
        return false;
    }
}