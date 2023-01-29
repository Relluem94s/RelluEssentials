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

public class Butcher {
    public static final String MAIN_GUI = Strings.PLUGIN_PREFIX + Strings.PLUGIN_SPACER + ItemConstants.PLUGIN_ITEM_NPC_BUTCHER;
    public static ItemHelper npc = new ItemHelper(Material.VILLAGER_SPAWN_EGG, 1, ItemConstants.PLUGIN_ITEM_NPC_BUTCHER, Type.NPC, Rarity.LEGENDARY, Arrays.asList(new String[]{ItemConstants.PLUGIN_ITEM_NPC_LORE1}));


    public static Inventory getMainGUI(){
        Inventory inv = InventoryHelper.fillInventory(InventoryHelper.createInventory(54, MAIN_GUI), CustomItems.npc_gui_disabled.getCustomItem());
    
        inv.setItem(10, new ItemStack(Material.RABBIT, 1));
        inv.setItem(11, new ItemStack(Material.PORKCHOP, 1));
        inv.setItem(12, new ItemStack(Material.BEEF, 1));
        inv.setItem(13, new ItemStack(Material.CHICKEN, 1));
        inv.setItem(14, new ItemStack(Material.MUTTON, 1));


        inv.setItem(19, new ItemStack(Material.COOKED_RABBIT, 1));
        inv.setItem(20, new ItemStack(Material.COOKED_PORKCHOP, 1));
        inv.setItem(21, new ItemStack(Material.COOKED_BEEF, 1));
        inv.setItem(22, new ItemStack(Material.COOKED_CHICKEN, 1));
        inv.setItem(23, new ItemStack(Material.COOKED_MUTTON, 1));

        inv.setItem(28, new ItemStack(Material.RABBIT_FOOT, 1));
        inv.setItem(30, new ItemStack(Material.LEATHER, 1));
        inv.setItem(31, new ItemStack(Material.FEATHER, 1));

        inv.setItem(37, new ItemStack(Material.RABBIT_HIDE, 1));
  
        inv.setItem(53, CustomItems.npc_gui_close.getCustomItem());

        return inv;
    }
}