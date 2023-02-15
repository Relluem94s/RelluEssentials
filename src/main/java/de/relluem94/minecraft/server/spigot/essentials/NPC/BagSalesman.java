package de.relluem94.minecraft.server.spigot.essentials.NPC;

import org.bukkit.entity.Villager.Profession;
import org.bukkit.inventory.Inventory;

import de.relluem94.minecraft.server.spigot.essentials.CustomItems;
import de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants;
import de.relluem94.minecraft.server.spigot.essentials.helpers.BagHelper;

public class BagSalesman extends NPC {

    public BagSalesman() {
        super(ItemConstants.PLUGIN_ITEM_NPC_BAGSALESMAN, Profession.LEATHERWORKER, Type.TRADER);
    }

    @Override
    public Inventory getMainGUI() {
        Inventory inv = BagHelper.getBags(true, getTitle());
        inv.setItem(53, CustomItems.npc_gui_close.getCustomItem());
        return inv;
    }
}
