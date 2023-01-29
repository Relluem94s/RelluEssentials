package de.relluem94.minecraft.server.spigot.essentials.NPC;

import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import de.relluem94.minecraft.server.spigot.essentials.CustomItems;
import de.relluem94.minecraft.server.spigot.essentials.Strings;
import de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants;
import de.relluem94.minecraft.server.spigot.essentials.helpers.InventoryHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.ItemHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.ItemHelper.Rarity;
import de.relluem94.minecraft.server.spigot.essentials.helpers.ItemHelper.Type;

public class Farmer {
    public static final String MAIN_GUI = Strings.PLUGIN_PREFIX + Strings.PLUGIN_SPACER + ItemConstants.PLUGIN_ITEM_NPC_FARMER;
    public static ItemHelper npc = new ItemHelper(Material.VILLAGER_SPAWN_EGG, 1, ItemConstants.PLUGIN_ITEM_NPC_FARMER, Type.NPC, Rarity.LEGENDARY, Arrays.asList(new String[]{ItemConstants.PLUGIN_ITEM_NPC_LORE1}));


    public static Inventory getMainGUI(){
        Inventory inv = InventoryHelper.fillInventory(InventoryHelper.createInventory(54, MAIN_GUI), CustomItems.npc_gui_disabled.getCustomItem());
    
        inv.setItem(10, new ItemStack(Material.SWEET_BERRIES, 1));
        inv.setItem(11, new ItemStack(Material.GLOW_BERRIES, 1));
        inv.setItem(12, new ItemStack(Material.APPLE, 1));
        inv.setItem(13, new ItemStack(Material.CARROT, 1));
        inv.setItem(14, new ItemStack(Material.POTATO, 1));
        inv.setItem(15, new ItemStack(Material.BEETROOT, 1));
        inv.setItem(16, new ItemStack(Material.SUGAR_CANE, 1));
        
        inv.setItem(19, new ItemStack(Material.PUMPKIN, 1));
        inv.setItem(20, new ItemStack(Material.MELON_SLICE, 1));
        inv.setItem(21, new ItemStack(Material.KELP, 1));
        inv.setItem(22, new ItemStack(Material.CHORUS_FRUIT, 1));
        inv.setItem(23, new ItemStack(Material.EGG, 1));
        inv.setItem(24, new ItemStack(Material.DRIED_KELP, 1));
        inv.setItem(25, new ItemStack(Material.WHEAT, 1));

        inv.setItem(28, new ItemStack(Material.WHEAT_SEEDS, 1));
        inv.setItem(29, new ItemStack(Material.BEETROOT_SEEDS, 1));
        inv.setItem(30, new ItemStack(Material.MELON_SEEDS, 1));
        inv.setItem(31, new ItemStack(Material.PUMPKIN_SEEDS, 1));
        inv.setItem(32, new ItemStack(Material.COCOA_BEANS, 1));
        inv.setItem(33, new ItemStack(Material.NETHER_WART, 1));
        inv.setItem(34, new ItemStack(Material.SUGAR, 1));

        inv.setItem(37, new ItemStack(Material.BROWN_MUSHROOM, 1));
        inv.setItem(38, new ItemStack(Material.RED_MUSHROOM, 1));
        inv.setItem(39, new ItemStack(Material.CRIMSON_FUNGUS, 1));
        inv.setItem(40, new ItemStack(Material.WARPED_FUNGUS, 1));
        inv.setItem(41, new ItemStack(Material.HONEYCOMB, 1));

        inv.setItem(53, CustomItems.npc_gui_close.getCustomItem());

        return inv;
    }
}