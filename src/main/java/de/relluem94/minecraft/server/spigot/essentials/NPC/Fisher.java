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

public class Fisher {
    public static final String MAIN_GUI = Strings.PLUGIN_PREFIX + Strings.PLUGIN_SPACER + ItemConstants.PLUGIN_ITEM_NPC_FISHER;
    public static ItemHelper npc = new ItemHelper(Material.VILLAGER_SPAWN_EGG, 1, ItemConstants.PLUGIN_ITEM_NPC_FISHER, Type.NPC, Rarity.LEGENDARY, Arrays.asList(new String[]{ItemConstants.PLUGIN_ITEM_NPC_LORE1}));


    public static Inventory getMainGUI(){
        Inventory inv = InventoryHelper.fillInventory(InventoryHelper.createInventory(54, MAIN_GUI), CustomItems.npc_gui_disabled.getCustomItem());
    
        inv.setItem(10, new ItemStack(Material.COD, 1));
        inv.setItem(11, new ItemStack(Material.SALMON, 1));
        inv.setItem(12, new ItemStack(Material.PUFFERFISH, 1));
        inv.setItem(13, new ItemStack(Material.TROPICAL_FISH, 1));
        
        inv.setItem(19, new ItemStack(Material.COD_BUCKET, 1));
        inv.setItem(20, new ItemStack(Material.SALMON_BUCKET, 1));
        inv.setItem(21, new ItemStack(Material.PUFFERFISH_BUCKET, 1));
        inv.setItem(22, new ItemStack(Material.TROPICAL_FISH_BUCKET, 1));
        inv.setItem(23, new ItemStack(Material.AXOLOTL_BUCKET, 1));
        inv.setItem(24, new ItemStack(Material.TADPOLE_BUCKET, 1));
        inv.setItem(25, new ItemStack(Material.WATER_BUCKET, 1));

        inv.setItem(37, new ItemStack(Material.OAK_BOAT, 1));
        inv.setItem(38, new ItemStack(Material.FISHING_ROD, 1));
        inv.setItem(39, new ItemStack(Material.CLAY_BALL, 1));
        inv.setItem(40, new ItemStack(Material.SEA_PICKLE, 1));
        inv.setItem(41, new ItemStack(Material.SEAGRASS, 1));
        inv.setItem(42, new ItemStack(Material.SCUTE, 1));
        inv.setItem(43, new ItemStack(Material.TURTLE_EGG, 1));

        inv.setItem(53, CustomItems.npc_gui_close.getCustomItem());

        return inv;
    }
}