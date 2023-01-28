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

public class Baker {
    public static final String MAIN_GUI = Strings.PLUGIN_PREFIX + Strings.PLUGIN_SPACER + ItemConstants.PLUGIN_ITEM_NPC_BAKER;
    public static ItemHelper npc = new ItemHelper(Material.VILLAGER_SPAWN_EGG, 1, ItemConstants.PLUGIN_ITEM_NPC_BAKER, Type.NPC, Rarity.LEGENDARY, Arrays.asList(new String[]{ItemConstants.PLUGIN_ITEM_NPC_LORE1}));


    public static Inventory getMainGUI(){
        Inventory inv = InventoryHelper.fillInventory(InventoryHelper.createInventory(54, MAIN_GUI), CustomItems.npc_gui_disabled.getCustomItem());
    
        inv.setItem(10, new ItemStack(Material.COOKIE, 1));
        inv.setItem(11, new ItemStack(Material.BREAD, 1));
        inv.setItem(12, new ItemStack(Material.PUMPKIN_PIE, 1));
        inv.setItem(13, new ItemStack(Material.CAKE, 1));
        inv.setItem(14, new ItemStack(Material.BAKED_POTATO, 1));


        inv.setItem(28, new ItemStack(Material.MUSHROOM_STEW, 1));
        inv.setItem(29, new ItemStack(Material.BEETROOT_SOUP, 1));

        inv.setItem(31, new ItemStack(Material.MILK_BUCKET, 1));
        inv.setItem(32, new ItemStack(Material.SUGAR, 1));
  

        inv.setItem(49, CustomItems.npc_gui_sell.getCustomItem());
        inv.setItem(53, CustomItems.npc_gui_close.getCustomItem());

        return inv;
    }
}