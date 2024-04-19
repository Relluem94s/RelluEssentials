package de.relluem94.minecraft.server.spigot.essentials.items;

import org.bukkit.Material;

import de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants;
import de.relluem94.minecraft.server.spigot.essentials.helpers.ItemHelper;

public class AutoSellHopper extends ItemHelper{

    public AutoSellHopper() {
        super(Material.HOPPER, 1, ItemConstants.PLUGIN_ITEM_AUTOSELLHOPER, Type.TOOL, Rarity.LEGENDARY);
    }
}