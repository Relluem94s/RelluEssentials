package de.relluem94.minecraft.server.spigot.essentials.items;

import org.bukkit.Material;

import de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants;
import de.relluem94.minecraft.server.spigot.essentials.helpers.ItemHelper;

public class GrapplingHook extends ItemHelper{

    public GrapplingHook() {
        super(Material.FISHING_ROD, 1, ItemConstants.PLUGIN_ITEM_GRAPPLINGHOCK, Type.GADGET, Rarity.UNCOMMON);
    }
}