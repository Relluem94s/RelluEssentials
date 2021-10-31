package de.relluem94.minecraft.server.spigot.essentials.commands;

import static de.relluem94.minecraft.server.spigot.essentials.RelluEssentials.dBH;
import static de.relluem94.minecraft.server.spigot.essentials.RelluEssentials.playerEntryList;
import static de.relluem94.minecraft.server.spigot.essentials.Strings.*;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.PlayerEntry;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.relluem94.minecraft.server.spigot.essentials.permissions.Permission;
import de.relluem94.minecraft.server.spigot.essentials.permissions.Groups;

public class Nick implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase(PLUGIN_COMMAND_NAME_NICK)) {
            if (args.length == 2) {
                if (sender instanceof Player) {
                    Player p = (Player) sender;
                    if (Permission.isAuthorized(p, Groups.getGroup("admin").getId())) {
                        Player target = Bukkit.getOfflinePlayer(args[0]).getPlayer();
                        if (target != null) {
                            PlayerEntry pe = playerEntryList.get(target.getUniqueId());
                            pe.setCustomname(args[1]);
                            pe.setUpdatedby(playerEntryList.get(p.getUniqueId()).getId());
                            dBH.updatePlayer(pe);
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
