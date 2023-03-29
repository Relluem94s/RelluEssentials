package de.relluem94.minecraft.server.spigot.essentials.commands;

import static de.relluem94.minecraft.server.spigot.essentials.Strings.PLUGIN_COMMAND_NICK;
import static de.relluem94.minecraft.server.spigot.essentials.Strings.PLUGIN_COMMAND_PERMISSION_MISSING;
import static de.relluem94.minecraft.server.spigot.essentials.Strings.PLUGIN_COMMAND_TARGET_NOT_A_PLAYER;
import static de.relluem94.minecraft.server.spigot.essentials.Strings.PLUGIN_COMMAND_TO_LESS_ARGUMENTS;
import static de.relluem94.minecraft.server.spigot.essentials.constants.CommandNameConstants.PLUGIN_COMMAND_NAME_NICK;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.TypeHelper.isPlayer;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.PlayerEntry;
import de.relluem94.minecraft.server.spigot.essentials.permissions.Groups;
import de.relluem94.minecraft.server.spigot.essentials.permissions.Permission;

public class Nick implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!command.getName().equalsIgnoreCase(PLUGIN_COMMAND_NAME_NICK)) {
            return false;
        }

        if (args.length < 2) {
           sender.sendMessage(PLUGIN_COMMAND_TO_LESS_ARGUMENTS);
           return true;
        }

        if (!Permission.isAuthorized(sender, Groups.getGroup("admin").getId())) {
            sender.sendMessage(PLUGIN_COMMAND_PERMISSION_MISSING);
            return true;
        }
        
        Player target = Bukkit.getPlayer(args[0]).getPlayer();

        if (target == null) {
            sender.sendMessage(String.format(PLUGIN_COMMAND_TARGET_NOT_A_PLAYER, args[0]));
            return true;
        }

        PlayerEntry pe = RelluEssentials.getInstance().getPlayerAPI().getPlayerEntry(target.getUniqueId());
        pe.setCustomName(args[1]);
        pe.setUpdatedBy(isPlayer(sender) ? RelluEssentials.getInstance().getPlayerAPI().getPlayerEntry(((Player)sender).getUniqueId()).getID() : 1);
        pe.setToBeUpdated(true);
        target.setCustomName(pe.getGroup().getPrefix() + args[1]);
        target.setPlayerListName(pe.getGroup().getPrefix() + args[1]);
        sender.sendMessage(String.format(PLUGIN_COMMAND_NICK, pe.getGroup().getPrefix() + target.getName()));
        return true;
    }
}