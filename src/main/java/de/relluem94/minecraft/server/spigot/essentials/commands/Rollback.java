package de.relluem94.minecraft.server.spigot.essentials.commands;

import java.util.List;
import java.util.UUID;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.Bukkit;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.BlockHistoryEntry;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.PlayerEntry;
import de.relluem94.minecraft.server.spigot.essentials.permissions.Permission;
import de.relluem94.minecraft.server.spigot.essentials.permissions.Groups;

import static de.relluem94.minecraft.server.spigot.essentials.RelluEssentials.blockHistoryList;
import static de.relluem94.minecraft.server.spigot.essentials.Strings.*;
import static de.relluem94.minecraft.server.spigot.essentials.constants.CommandNameConstants.PLUGIN_COMMAND_NAME_ROLLBACK;
import static de.relluem94.minecraft.server.spigot.essentials.constants.CommandNameConstants.PLUGIN_COMMAND_NAME_ROLLBACK_PLAYER;
import static de.relluem94.minecraft.server.spigot.essentials.constants.CommandNameConstants.PLUGIN_COMMAND_NAME_ROLLBACK_UNDO;
import static de.relluem94.minecraft.server.spigot.essentials.constants.CommandNameConstants.PLUGIN_COMMAND_NAME_ROLLBACK_UNDO_PLAYER;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.TypeHelper.isPlayer;

public class Rollback implements CommandExecutor {

    /*
    
    /rollback -> Info
    /rollback player <Player> --> Rolls back Player last to first block.                                                    // DONE
    /rollback player <Player> <Time> --> Rolls back Player last to first block in the last 2Y8M60d10h30m etc                // DONE
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
        if (command.getName().equalsIgnoreCase(PLUGIN_COMMAND_NAME_ROLLBACK)) {
            switch (args.length) {
                case 2:
                    if (isPlayer(sender)) {
                        Player p = (Player) sender;
                        if (Permission.isAuthorized(p, Groups.getGroup("admin").getId())) {
                            UUID targetUUID = Bukkit.getPlayer(args[1]).getUniqueId();
                            PlayerEntry pe = RelluEssentials.getInstance().getDatabaseHelper().getPlayer(targetUUID.toString());
                            if (pe != null && args[0].equalsIgnoreCase(PLUGIN_COMMAND_NAME_ROLLBACK_PLAYER)) {
                                int id = RelluEssentials.getInstance().getPlayerAPI().getPlayerEntry(p.getUniqueId()).getID();
                                List<BlockHistoryEntry> list = RelluEssentials.getInstance().getDatabaseHelper().getBlockHistoryByPlayer(pe);
                                for (BlockHistoryEntry bh : list) {
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
                    if (isPlayer(sender)) {
                        Player p = (Player) sender;
                        if (Permission.isAuthorized(p, Groups.getGroup("admin").getId())) {
                            if (args[0].equalsIgnoreCase(PLUGIN_COMMAND_NAME_ROLLBACK_PLAYER)) {
                                UUID targetUUID = Bukkit.getPlayer(args[1]).getUniqueId();
                                PlayerEntry pe = RelluEssentials.getInstance().getDatabaseHelper().getPlayer(targetUUID.toString());

                                if (pe != null) {
                                    int id = RelluEssentials.getInstance().getPlayerAPI().getPlayerEntry(p.getUniqueId()).getID();
                                    List<BlockHistoryEntry> list = RelluEssentials.getInstance().getDatabaseHelper().getBlockHistoryByPlayerAndTime(pe, args[2], false);
                                    for (BlockHistoryEntry bh : list) {
                                        bh.setDeletedby(id);
                                        blockHistoryList.add(bh);
                                    }
                                    p.sendMessage("Added: " + list.size());
                                }
                            } else if (args[0].equalsIgnoreCase(PLUGIN_COMMAND_NAME_ROLLBACK_UNDO) && args[1].equalsIgnoreCase(PLUGIN_COMMAND_NAME_ROLLBACK_UNDO_PLAYER)) {

                            }

                            return true;
                        } else {
                            p.sendMessage(PLUGIN_COMMAND_PERMISSION_MISSING);
                            return true;
                        }
                    }
                    break;
                case 4:
                    if (isPlayer(sender)) {
                        Player p = (Player) sender;
                        if (Permission.isAuthorized(p, Groups.getGroup("admin").getId())) {
                            if (args[0].equalsIgnoreCase(PLUGIN_COMMAND_NAME_ROLLBACK_UNDO) && args[1].equalsIgnoreCase(PLUGIN_COMMAND_NAME_ROLLBACK_UNDO_PLAYER)) {
                                UUID targetUUID = Bukkit.getPlayer(args[2]).getUniqueId();
                                PlayerEntry pe = RelluEssentials.getInstance().getDatabaseHelper().getPlayer(targetUUID.toString());

                                if (pe != null) {
                                    int id = RelluEssentials.getInstance().getPlayerAPI().getPlayerEntry(p.getUniqueId()).getID();
                                    List<BlockHistoryEntry> list = RelluEssentials.getInstance().getDatabaseHelper().getBlockHistoryByPlayerAndTime(pe, args[3], true);
                                    for (BlockHistoryEntry bh : list) {
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
