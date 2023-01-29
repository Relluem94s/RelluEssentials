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

public class Lumberjack {
    public static final String MAIN_GUI = Strings.PLUGIN_PREFIX + Strings.PLUGIN_SPACER + ItemConstants.PLUGIN_ITEM_NPC_LUMBERJACK;
    public static ItemHelper npc = new ItemHelper(Material.VILLAGER_SPAWN_EGG, 1, ItemConstants.PLUGIN_ITEM_NPC_LUMBERJACK, Type.NPC, Rarity.LEGENDARY, Arrays.asList(new String[]{ItemConstants.PLUGIN_ITEM_NPC_LORE1}));


    public static Inventory getMainGUI(){
        Inventory inv = InventoryHelper.fillInventory(InventoryHelper.createInventory(54, MAIN_GUI), CustomItems.npc_gui_disabled.getCustomItem());
    
        inv.setItem(10, new ItemStack(Material.OAK_LOG, 1));
        inv.setItem(11, new ItemStack(Material.BIRCH_LOG, 1));
        inv.setItem(12, new ItemStack(Material.JUNGLE_LOG, 1));
        inv.setItem(13, new ItemStack(Material.ACACIA_LOG, 1));
        inv.setItem(14, new ItemStack(Material.DARK_OAK_LOG, 1));
        inv.setItem(15, new ItemStack(Material.SPRUCE_LOG, 1));
        inv.setItem(16, new ItemStack(Material.MANGROVE_LOG, 1));

        inv.setItem(19, new ItemStack(Material.OAK_SAPLING, 1));
        inv.setItem(20, new ItemStack(Material.BIRCH_SAPLING, 1));
        inv.setItem(21, new ItemStack(Material.JUNGLE_SAPLING, 1));
        inv.setItem(22, new ItemStack(Material.ACACIA_SAPLING, 1));
        inv.setItem(23, new ItemStack(Material.DARK_OAK_SAPLING, 1));
        inv.setItem(24, new ItemStack(Material.SPRUCE_SAPLING, 1));
        inv.setItem(25, new ItemStack(Material.MANGROVE_PROPAGULE, 1));

        inv.setItem(37, new ItemStack(Material.STICK, 1));

        inv.setItem(42, new ItemStack(Material.WARPED_STEM, 1));
        inv.setItem(43, new ItemStack(Material.CRIMSON_STEM, 1));

        inv.setItem(53, CustomItems.npc_gui_close.getCustomItem());

        return inv;
    }
}