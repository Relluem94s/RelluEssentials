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

    /*
    
    /rollback -> Info
    /rollback player <Player> --> Rolls back Player last to first block.
    /rollback player <Player> <Time> --> Rolls back Player last to first block in the last 2Y8M60d10h30m etc
  * /rollback location (or pos) (needs selected Area) next update or so..
    /rollback undo player <Player> --> Undos rollback Player first to last  block.
    /rollback undo player <Player> <Time> --> Undos rollback Player first to last block in the last 2Y8M60D10h30m etc
  * /rollback undo location (needs selected Area)
    /rollback preview player <Player> --> Shows 
    /rollback preview player <Player> <Time> 
  * /rollback preview location (needs selected Area)
    /rollback preview undo player <Player>
    /rollback preview undo player <Player> <Time> 
  * /rollback preview undo location (needs selected Area)
    
    
        
    
    Maybe also as an Inventory Menu.
    Undo reverts last action. (looks for timeframe but ignores the deleted)
    Preview sends blockchange to player and reverts after 60 seconds (if possible else no preview)    
    
    */
    
    
    
    
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("rollback")) {
            switch (args.length) {
                case 2:
                    if (sender instanceof Player) {
                        Player p = (Player) sender;
                        if (Permission.isAuthorized(p, Groups.ADMIN.getId())) {
                            UUID targetUUID = Bukkit.getOfflinePlayer(args[1]).getUniqueId();
                            PlayerEntry pe = dBH.getPlayer(targetUUID.toString());
                            if (pe != null && args[0].equalsIgnoreCase("player")) {
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
                    break;
                case 3:
                    if (sender instanceof Player) {
                        Player p = (Player) sender;
                        if (Permission.isAuthorized(p, Groups.ADMIN.getId())) {
                            if(args[0].equalsIgnoreCase("player")){
                                UUID targetUUID = Bukkit.getOfflinePlayer(args[1]).getUniqueId();
                                PlayerEntry pe = dBH.getPlayer(targetUUID.toString());

                                if (pe != null) {
                                    int id = playerEntryList.get(p.getUniqueId()).getId();
                                    List<BlockHistoryEntry> list = dBH.getBlockHistoryByPlayerAndTime(pe, args[2]);
                                    for(BlockHistoryEntry bh : list){
                                        bh.setDeletedby(id);
                                        blockHistoryList.add(bh);
                                    }
                                    p.sendMessage("Added: " + list.size());
                                }
                            }
                            
                            
                            return true;
                        } else {
                            p.sendMessage(PLUGIN_COMMAND_PERMISSION_MISSING);
                            return true;
                        }
                    }
                    break;
                case 4:
                    break;
                case 5:
                    break;
                default:
                    // INFO
                    break;
            }
        }
        return false;
    }
    
    
}
