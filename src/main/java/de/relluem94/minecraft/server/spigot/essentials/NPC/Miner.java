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

public class Miner {
    public static final String MAIN_GUI = Strings.PLUGIN_PREFIX + Strings.PLUGIN_SPACER + ItemConstants.PLUGIN_ITEM_NPC_MINER;
    public static ItemHelper npc = new ItemHelper(Material.VILLAGER_SPAWN_EGG, 1, ItemConstants.PLUGIN_ITEM_NPC_MINER, Type.NPC, Rarity.LEGENDARY, Arrays.asList(new String[]{ItemConstants.PLUGIN_ITEM_NPC_LORE1}));


    public static Inventory getMainGUI(){
        Inventory inv = InventoryHelper.fillInventory(InventoryHelper.createInventory(54, MAIN_GUI), CustomItems.npc_gui_disabled.getCustomItem());
    
        inv.setItem(10, new ItemStack(Material.STONE, 1));
        inv.setItem(11, new ItemStack(Material.COBBLESTONE, 1));
        inv.setItem(12, new ItemStack(Material.GRANITE, 1));
        inv.setItem(13, new ItemStack(Material.DIORITE, 1));
        inv.setItem(14, new ItemStack(Material.ANDESITE, 1));
        inv.setItem(15, new ItemStack(Material.DEEPSLATE, 1));
        inv.setItem(16, new ItemStack(Material.BASALT, 1));
        
        inv.setItem(19, new ItemStack(Material.NETHERRACK, 1));
        inv.setItem(20, new ItemStack(Material.END_STONE, 1));
        inv.setItem(21, new ItemStack(Material.MOSSY_COBBLESTONE, 1));
        inv.setItem(22, new ItemStack(Material.QUARTZ_BLOCK, 1));

        inv.setItem(31, new ItemStack(Material.COAL, 1));
        inv.setItem(32, new ItemStack(Material.RAW_COPPER, 1));
        inv.setItem(33, new ItemStack(Material.RAW_IRON, 1));
        inv.setItem(34, new ItemStack(Material.RAW_GOLD, 1));
        inv.setItem(37, new ItemStack(Material.AMETHYST_SHARD, 1));
        inv.setItem(38, new ItemStack(Material.LAPIS_LAZULI, 1));
        inv.setItem(39, new ItemStack(Material.REDSTONE, 1));
        inv.setItem(40, new ItemStack(Material.QUARTZ, 1));
        inv.setItem(41, new ItemStack(Material.DIAMOND, 1));
        inv.setItem(42, new ItemStack(Material.EMERALD, 1));
        inv.setItem(43, new ItemStack(Material.ANCIENT_DEBRIS, 1));

        inv.setItem(49, CustomItems.npc_gui_sell.getCustomItem());
        inv.setItem(53, CustomItems.npc_gui_close.getCustomItem());

        return inv;
    }
}