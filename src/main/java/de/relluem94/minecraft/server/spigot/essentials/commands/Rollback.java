package de.relluem94.minecraft.server.spigot.essentials.commands;

import static de.relluem94.minecraft.server.spigot.essentials.RelluEssentials.blockHistoryList;
import static de.relluem94.minecraft.server.spigot.essentials.RelluEssentials.dBH;
import static de.relluem94.minecraft.server.spigot.essentials.RelluEssentials.playerEntryList;
import static de.relluem94.minecraft.server.spigot.essentials.Strings.*;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.BlockHistoryEntry;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.relluem94.minecraft.server.spigot.essentials.permissions.Permission;
import de.relluem94.minecraft.server.spigot.essentials.permissions.enums.Groups;
import java.util.List;
import org.bukkit.Bukkit;

public class Rollback implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("rollback")) {
            if (args.length == 1) {
                if (sender instanceof Player) {
                    Player p = (Player) sender;
                    if (Permission.isAuthorized(p, Groups.ADMIN.getId())) {
                        Player target = Bukkit.getPlayer(args[0]);
                        if (target != null) {
                            int id = playerEntryList.get(p.getUniqueId()).getId();
                            List<BlockHistoryEntry> list = dBH.getBlockHistoryByPlayer(playerEntryList.get(target.getUniqueId()));
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
        }
        return false;
    }
    
    
}
