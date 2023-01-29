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

public class Adventurer {
    public static final String MAIN_GUI = Strings.PLUGIN_PREFIX + Strings.PLUGIN_SPACER + ItemConstants.PLUGIN_ITEM_NPC_ADVENTURER;
    public static ItemHelper npc = new ItemHelper(Material.VILLAGER_SPAWN_EGG, 1, ItemConstants.PLUGIN_ITEM_NPC_ADVENTURER, Type.NPC, Rarity.LEGENDARY, Arrays.asList(new String[]{ItemConstants.PLUGIN_ITEM_NPC_LORE1}));


    public static Inventory getMainGUI(){
        Inventory inv = InventoryHelper.fillInventory(InventoryHelper.createInventory(54, MAIN_GUI), CustomItems.npc_gui_disabled.getCustomItem());
    
        inv.setItem(10, new ItemStack(Material.ARROW, 1));
        inv.setItem(11, new ItemStack(Material.BONE, 1));
        inv.setItem(12, new ItemStack(Material.ROTTEN_FLESH, 1));
        inv.setItem(13, new ItemStack(Material.SPIDER_EYE, 1));
        inv.setItem(14, new ItemStack(Material.STRING, 1));
        inv.setItem(15, new ItemStack(Material.GUNPOWDER, 1));
        inv.setItem(16, new ItemStack(Material.SLIME_BALL, 1));

        inv.setItem(28, new ItemStack(Material.GHAST_TEAR, 1));
        inv.setItem(29, new ItemStack(Material.BLAZE_ROD, 1));

        inv.setItem(31, new ItemStack(Material.ENDER_PEARL, 1));
        inv.setItem(32, new ItemStack(Material.SHULKER_SHELL, 1));
        inv.setItem(33, new ItemStack(Material.PHANTOM_MEMBRANE, 1));

        inv.setItem(34, new ItemStack(Material.EXPERIENCE_BOTTLE, 1));

        inv.setItem(53, CustomItems.npc_gui_close.getCustomItem());

        return inv;
    }
}