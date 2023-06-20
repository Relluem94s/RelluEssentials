package de.relluem94.minecraft.server.spigot.essentials.commands;

import static de.relluem94.minecraft.server.spigot.essentials.Strings.PLUGIN_COMMAND_PERMISSION_MISSING;
import static de.relluem94.minecraft.server.spigot.essentials.Strings.PLUGIN_COMMAND_SETGROUP;
import static de.relluem94.minecraft.server.spigot.essentials.Strings.PLUGIN_COMMAND_SETGROUP_GROUP_NOT_FOUND;
import static de.relluem94.minecraft.server.spigot.essentials.Strings.PLUGIN_COMMAND_TARGET_NOT_A_PLAYER;
import static de.relluem94.minecraft.server.spigot.essentials.Strings.PLUGIN_COMMAND_TO_LESS_ARGUMENTS;
import static de.relluem94.minecraft.server.spigot.essentials.Strings.PLUGIN_COMMAND_TO_MANY_ARGUMENTS;
import static de.relluem94.minecraft.server.spigot.essentials.constants.CommandNameConstants.PLUGIN_COMMAND_NAME_SETGROUP;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.TypeHelper.isCMDBlock;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.TypeHelper.isConsole;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.TypeHelper.isPlayer;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.helpers.PlayerHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.GroupEntry;
import de.relluem94.minecraft.server.spigot.essentials.permissions.Groups;
import de.relluem94.minecraft.server.spigot.essentials.permissions.Permission;

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

        OfflinePlayer target = PlayerHelper.getOfflinePlayer(args[0]);
        
        if (target == null) {
            sender.sendMessage(String.format(PLUGIN_COMMAND_TARGET_NOT_A_PLAYER, args[0]));
            return true;
        }

        if(RelluEssentials.getInstance().getPlayerAPI().getPlayerEntry(target.getUniqueId()) == null){
            sender.sendMessage(String.format(PLUGIN_COMMAND_TARGET_NOT_A_PLAYER, args[0]));
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

            p.sendMessage(String.format(PLUGIN_COMMAND_SETGROUP, g.getPrefix() + g.getName(), target.getName()));
            PlayerHelper.updateGroup(target, g);
            return true;
        }
        else if (isCMDBlock(sender) || isConsole(sender)) {
            GroupEntry g = Groups.getGroup(args[1]);

            if(g == null){
                sender.sendMessage(PLUGIN_COMMAND_SETGROUP_GROUP_NOT_FOUND);
                return true;
            }

            sender.sendMessage(String.format(PLUGIN_COMMAND_SETGROUP, g.getPrefix() + g.getName(), target.getName()));
            PlayerHelper.updateGroup(target, g);
            return true;
        }
        return false;
    }
}