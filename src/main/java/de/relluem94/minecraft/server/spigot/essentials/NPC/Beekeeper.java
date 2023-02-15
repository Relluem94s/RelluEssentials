package de.relluem94.minecraft.server.spigot.essentials.NPC;

import org.bukkit.Material;
import org.bukkit.entity.Villager.Profession;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import de.relluem94.minecraft.server.spigot.essentials.CustomItems;
import de.relluem94.minecraft.server.spigot.essentials.constants.CustomHeads;
import de.relluem94.minecraft.server.spigot.essentials.helpers.InventoryHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.PlayerHeadHelper;

public class Beekeeper extends NPC {

    public Beekeeper(){
        super("§dBeekeeper", Profession.NONE, Type.TRADER);
    }

    @Override
    public Inventory getMainGUI(){
        Inventory inv = InventoryHelper.fillInventory(InventoryHelper.createInventory(54, getTitle()), CustomItems.npc_gui_disabled.getCustomItem());
    
        inv.setItem(10, new ItemStack(Material.BEEHIVE, 1));
        inv.setItem(11, new ItemStack(Material.HONEYCOMB_BLOCK, 1));
        inv.setItem(12, new ItemStack(Material.HONEY_BLOCK, 1));
        inv.setItem(13, new ItemStack(Material.HONEYCOMB, 1));
        inv.setItem(14, new ItemStack(Material.HONEY_BOTTLE, 1));
        inv.setItem(15, new ItemStack(Material.CANDLE, 1));

        inv.setItem(28, PlayerHeadHelper.getCustomSkull(CustomHeads.WHITE_CANDLE));
        inv.setItem(29, PlayerHeadHelper.getCustomSkull(CustomHeads.CYAN_CANDLE));
        inv.setItem(30, PlayerHeadHelper.getCustomSkull(CustomHeads.RED_CANDLE));
        inv.setItem(31, PlayerHeadHelper.getCustomSkull(CustomHeads.BLUE_CANDLE));
        inv.setItem(32, PlayerHeadHelper.getCustomSkull(CustomHeads.GRAY_CANDLE));
        inv.setItem(33, PlayerHeadHelper.getCustomSkull(CustomHeads.LIME_CANDLE));
        inv.setItem(34, PlayerHeadHelper.getCustomSkull(CustomHeads.MAGENTA_CANDLE));

        inv.setItem(37, PlayerHeadHelper.getCustomSkull(CustomHeads.PINK_CANDLE));
        inv.setItem(38, PlayerHeadHelper.getCustomSkull(CustomHeads.BLACK_CANDLE));
        inv.setItem(39, PlayerHeadHelper.getCustomSkull(CustomHeads.GREEN_CANDLE));
        inv.setItem(40, PlayerHeadHelper.getCustomSkull(CustomHeads.ORANGE_CANDLE));
        inv.setItem(41, PlayerHeadHelper.getCustomSkull(CustomHeads.BROWN_CANDLE));
        inv.setItem(42, PlayerHeadHelper.getCustomSkull(CustomHeads.LIGHT_BLUE_CANDLE));
        inv.setItem(43, PlayerHeadHelper.getCustomSkull(CustomHeads.LIGHT_GRAY_CANDLE));

        inv.setItem(53, CustomItems.npc_gui_close.getCustomItem());

        return inv;
    }
}