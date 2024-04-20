package de.relluem94.minecraft.server.spigot.essentials.commands;

import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_COMMAND_PERMISSION_MISSING;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_COMMAND_SETGROUP;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_COMMAND_SETGROUP_GROUP_NOT_FOUND;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_COMMAND_TARGET_NOT_A_PLAYER;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_COMMAND_TO_LESS_ARGUMENTS;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_COMMAND_TO_MANY_ARGUMENTS;
import static de.relluem94.minecraft.server.spigot.essentials.constants.CommandNameConstants.PLUGIN_COMMAND_NAME_SETGROUP;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.TypeHelper.isCMDBlock;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.TypeHelper.isConsole;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.TypeHelper.isPlayer;

import lombok.NonNull;
import org.bukkit.Bukkit;
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

import java.util.Objects;

public class PermissionsGroup implements CommandExecutor {

    @Override
    public boolean onCommand(@NonNull CommandSender sender, Command command, @NonNull String label, String[] args) {
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
            GroupEntry g = checkGroupExists(args[1], p);
            setGroupForTarget(p, Objects.requireNonNull(g), target);
            return true;
        }
        else if (isCMDBlock(sender) || isConsole(sender)) {
            GroupEntry g = Groups.getGroup(args[1]);
            setGroupForTarget(sender, Objects.requireNonNull(g), target);
            return true;
        }
        return false;
    }

    private static GroupEntry checkGroupExists(String groupName, Player p) {
        GroupEntry g = Groups.getGroup(groupName);

        if(Groups.groupExists(groupName)){
            p.sendMessage(PLUGIN_COMMAND_SETGROUP_GROUP_NOT_FOUND);
            return null;
        }

        if (!Permission.isAuthorized(p, Objects.requireNonNull(Groups.getGroup("mod")).getId())) {
            p.sendMessage(PLUGIN_COMMAND_PERMISSION_MISSING);
            return null;
        }
        return g;
    }

    private static void setGroupForTarget(CommandSender s, GroupEntry g, OfflinePlayer target) {
        s.sendMessage(String.format(PLUGIN_COMMAND_SETGROUP, g.getPrefix() + g.getName(), target.getName()));
        if(target.isOnline() && Bukkit.getPlayer(target.getUniqueId()) != null){
            Objects.requireNonNull(Bukkit.getPlayer(target.getUniqueId())).sendMessage(String.format(PLUGIN_COMMAND_SETGROUP, g.getPrefix() + g.getName(), target.getName()));
        }
        PlayerHelper.updateGroup(target, g);
    }
}