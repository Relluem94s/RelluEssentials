package de.relluem94.minecraft.server.spigot.essentials.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.relluem94.minecraft.server.spigot.essentials.permissions.Permission;
import de.relluem94.minecraft.server.spigot.essentials.CustomItems;
import de.relluem94.minecraft.server.spigot.essentials.Strings;
import de.relluem94.minecraft.server.spigot.essentials.NPC.Banker;
import de.relluem94.minecraft.server.spigot.essentials.helpers.InventoryHelper;
import de.relluem94.minecraft.server.spigot.essentials.permissions.Groups;

import static de.relluem94.minecraft.server.spigot.essentials.Strings.*;
import static de.relluem94.minecraft.server.spigot.essentials.constants.CommandNameConstants.PLUGIN_COMMAND_NAME_ADMIN;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.TypeHelper.isPlayer;

public class Admin implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase(PLUGIN_COMMAND_NAME_ADMIN)) {
            if (isPlayer(sender)) {
                Player p = (Player) sender;
                if (Permission.isAuthorized(p, Groups.getGroup("admin").getId())) {
                    if (args.length == 0) {
                        // INFO
                        p.sendMessage("TEST 88");
                    }
                    else if (args.length == 1) {
                        if(args[0].equals("npc")){
                            org.bukkit.inventory.Inventory inv = InventoryHelper.fillInventory(InventoryHelper.createInventory(9, Strings.PLUGIN_PREFIX + Strings.PLUGIN_SPACER +"§dNPCs"), CustomItems.npc_gui_disabled.getCustomItem());
                            
                            inv.setItem(0, Banker.npc.getCustomItem());
                            inv.setItem(1, CustomItems.npcFarmer.getCustomItem());
                            inv.setItem(2, CustomItems.npcFisher.getCustomItem());
                            inv.setItem(3, CustomItems.npcSmith.getCustomItem());
                            inv.setItem(4, CustomItems.npcAdventurer.getCustomItem());
                            inv.setItem(5, CustomItems.npcMiner.getCustomItem());
        
                            InventoryHelper.openInventory(sender, inv);
                            p.sendMessage("TEST 66");
                        }
                        else{
                            p.sendMessage("TEST 44");
                        }
                    }
                    else{
                        p.sendMessage("TEST 22");
                    }
                    return true;
                } else {
                    p.sendMessage(PLUGIN_COMMAND_PERMISSION_MISSING);
                    return true;
                }
            }
        }
        return false;
    }
}
