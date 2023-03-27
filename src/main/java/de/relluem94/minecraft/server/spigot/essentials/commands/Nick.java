package de.relluem94.minecraft.server.spigot.essentials.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.relluem94.minecraft.server.spigot.essentials.permissions.Permission;
import de.relluem94.minecraft.server.spigot.essentials.permissions.Groups;
import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.PlayerEntry;

import static de.relluem94.minecraft.server.spigot.essentials.Strings.*;
import static de.relluem94.minecraft.server.spigot.essentials.constants.CommandNameConstants.PLUGIN_COMMAND_NAME_NICK;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.TypeHelper.isPlayer;

public class Nick implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase(PLUGIN_COMMAND_NAME_NICK)) {
            if (args.length == 2) {
                if (isPlayer(sender)) {
                    Player p = (Player) sender;
                    if (Permission.isAuthorized(p, Groups.getGroup("admin").getId())) {
                        Player target = Bukkit.getPlayer(args[0]).getPlayer();
                        if (target != null) {
                            PlayerEntry pe = RelluEssentials.getInstance().getPlayerAPI().getPlayerEntry(target.getUniqueId());
                            pe.setCustomName(args[1]);
                            pe.setUpdatedBy(RelluEssentials.getInstance().getPlayerAPI().getPlayerEntry(p.getUniqueId()).getID());
                            pe.setToBeUpdated(true);
                            target.setCustomName(pe.getGroup().getPrefix() + args[1]);
                            target.setPlayerListName(pe.getGroup().getPrefix() + args[1]);
                            p.sendMessage(String.format(PLUGIN_COMMAND_NICK, pe.getGroup().getPrefix() + target.getName()));

                            return true;
                        }
                    } else {
                        p.sendMessage(PLUGIN_COMMAND_PERMISSION_MISSING);
                        return true;
                    }
                }
            }
        }

        return false;
    }

}
