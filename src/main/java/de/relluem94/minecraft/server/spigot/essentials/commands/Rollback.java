package de.relluem94.minecraft.server.spigot.essentials.commands;

import static de.relluem94.minecraft.server.spigot.essentials.RelluEssentials.blockHistoryList;
import static de.relluem94.minecraft.server.spigot.essentials.RelluEssentials.dBH;
import static de.relluem94.minecraft.server.spigot.essentials.RelluEssentials.playerEntryList;
import static de.relluem94.minecraft.server.spigot.essentials.Strings.*;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.BlockHistoryEntry;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.PlayerEntry;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.relluem94.minecraft.server.spigot.essentials.permissions.Permission;
import de.relluem94.minecraft.server.spigot.essentials.permissions.enums.Groups;
import java.util.List;
import java.util.UUID;
import org.bukkit.Bukkit;

public class Rollback implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("rollback")) {
            if (args.length == 1) {
                if (sender instanceof Player) {
                    Player p = (Player) sender;
                    if (Permission.isAuthorized(p, Groups.ADMIN.getId())) {
                        UUID targetUUID = Bukkit.getOfflinePlayer(args[0]).getUniqueId();
                        PlayerEntry pe = dBH.getPlayer(targetUUID.toString());

                        if (pe != null) {
                            int id = playerEntryList.get(p.getUniqueId()).getId();
                            List<BlockHistoryEntry> list = dBH.getBlockHistoryByPlayer(pe);
                            for(BlockHistoryEntry bh : list){
                                bh.setDeletedby(id);
                                blockHistoryList.add(bh);
                            }
                            p.sendMessage("Added: " + list.size());
                        }
                        return true;
                    } else {
                        p.sendMessage(PLUGIN_COMMAND_PERMISSION_MISSING);
                        return true;
                    }
                }
            }
            else if(args.length == 2){
                // Player Rollback with 2Y 8M 60d 10h 30m 
            }
        }
        return false;
    }
    
    
}
