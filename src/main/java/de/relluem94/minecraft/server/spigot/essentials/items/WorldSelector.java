package de.relluem94.minecraft.server.spigot.essentials.items;

import de.relluem94.minecraft.server.spigot.essentials.constants.CustomHeads;
import de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants;
import de.relluem94.minecraft.server.spigot.essentials.helpers.ItemHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.PlayerHeadHelper;

public class WorldSelector extends ItemHelper{
    public WorldSelector() {
        super(PlayerHeadHelper.getCustomSkull(CustomHeads.GLOBE), ItemConstants.PLUGIN_ITEM_WORLDSELECTOR, Type.GADGET, Rarity.RARE);
    }
}