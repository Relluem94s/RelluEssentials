package de.relluem94.minecraft.server.spigot.essentials.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import de.relluem94.minecraft.server.spigot.essentials.permissions.Permission;
import de.relluem94.minecraft.server.spigot.essentials.CustomItems;
import de.relluem94.minecraft.server.spigot.essentials.constants.CustomHeads;
import de.relluem94.minecraft.server.spigot.essentials.helpers.InventoryHelper;
import de.relluem94.minecraft.server.spigot.essentials.permissions.Groups;

import static de.relluem94.minecraft.server.spigot.essentials.Strings.*;
import static de.relluem94.minecraft.server.spigot.essentials.constants.CommandNameConstants.PLUGIN_COMMAND_NAME_HEAD;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.ChatHelper.sendMessage;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.TypeHelper.isPlayer;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.PlayerHeadHelper.createSkull;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.PlayerHeadHelper.getCustomSkull;;

public class Head implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase(PLUGIN_COMMAND_NAME_HEAD)) {
                if (isPlayer(sender)) {
                    Player p = (Player) sender;
                    if (Permission.isAuthorized(p, Groups.getGroup("mod").getId())) {
                        if (args.length == 1) {
                            String owner = args[0];
                            ItemStack is = createSkull(owner);

                            if(is != null){
                                p.getInventory().addItem(is);
                                p.updateInventory();
                                sendMessage(p, String.format(PLUGIN_COMMAND_HEAD, owner));
                            }
                            else{
                                sendMessage(p, PLUGIN_COMMAND_HEAD_NOT_FOUND);
                            }
                        }
                        if (args.length == 3) {
                            org.bukkit.inventory.Inventory inv = InventoryHelper.createInventory(54, "Heads");
                            inv = InventoryHelper.fillInventory(inv, CustomItems.npc_gui_disabled.getCustomItem());

                            inv.setItem(10, getCustomSkull(CustomHeads.BOOK1));
                            inv.setItem(11, getCustomSkull(CustomHeads.BOOKS1));
                            inv.setItem(12, getCustomSkull(CustomHeads.BOOKS2));
                            inv.setItem(13, getCustomSkull(CustomHeads.MONEY_BAG));
                            inv.setItem(14, getCustomSkull(CustomHeads.COMPUTER));
                            inv.setItem(15, getCustomSkull(CustomHeads.ASTRONAUT_HELMET));
                            inv.setItem(16, getCustomSkull(CustomHeads.BOOK1));
                            
                            inv.setItem(19, getCustomSkull(CustomHeads.DARK_OAK_LOG));
                            inv.setItem(20, getCustomSkull(CustomHeads.PRISMARINE));
                            inv.setItem(21, getCustomSkull(CustomHeads.QUARTZ));
                            inv.setItem(22, getCustomSkull(CustomHeads.BUSH));
                            inv.setItem(23, getCustomSkull(CustomHeads.DIAMOND_ORE));
                            inv.setItem(24, getCustomSkull(CustomHeads.PUMPKIN));
                            inv.setItem(25, getCustomSkull(CustomHeads.OAK_WOOD_PLANKS));
                            
                            inv.setItem(28, getCustomSkull(CustomHeads.SHARK));
                            inv.setItem(29, getCustomSkull(CustomHeads.SKULL1));
                            inv.setItem(30, getCustomSkull(CustomHeads.SKULL2));
                            inv.setItem(31, getCustomSkull(CustomHeads.ENDERMANN));
                            inv.setItem(32, getCustomSkull(CustomHeads.LUCKY_BLOCK));
                            inv.setItem(33, getCustomSkull(CustomHeads.GREEN_ORB));
                            inv.setItem(34, getCustomSkull(CustomHeads.GOLDEN_CHEST));
                            
                            inv.setItem(37, getCustomSkull(CustomHeads.BREAD));
                            inv.setItem(38, getCustomSkull(CustomHeads.TELEVISON));
                            inv.setItem(39, getCustomSkull(CustomHeads.GUARDIAN));
                            inv.setItem(40, getCustomSkull(CustomHeads.ROBOT));
                            inv.setItem(41, getCustomSkull(CustomHeads.HAMBURGER));
                            inv.setItem(42, getCustomSkull(CustomHeads.CUPCAKE));
                            inv.setItem(43, getCustomSkull(CustomHeads.COMPANION_CUBE));
                            
                            p.openInventory(inv);
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