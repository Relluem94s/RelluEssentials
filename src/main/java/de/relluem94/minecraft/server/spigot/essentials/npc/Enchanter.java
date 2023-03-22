package de.relluem94.minecraft.server.spigot.essentials.npc;

import org.bukkit.entity.Villager.Profession;
import org.bukkit.inventory.Inventory;

import de.relluem94.minecraft.server.spigot.essentials.CustomEnchants;
import de.relluem94.minecraft.server.spigot.essentials.CustomItems;
import de.relluem94.minecraft.server.spigot.essentials.helpers.InventoryHelper;

public class Enchanter extends NPC {

    public Enchanter() {
        super("§dEnchanter", Profession.LIBRARIAN, Type.ENCHANTER);
    }

    @Override
    public Inventory getMainGUI() {
        Inventory inv = InventoryHelper.fillInventory(InventoryHelper.createInventory(54, getTitle()), CustomItems.npc_gui_disabled.getCustomItem());

        int slot = 0;
        for(int i = 0; i < CustomEnchants.customEnchantments.size(); i++){
            slot = InventoryHelper.getNextSlot(slot);
            inv.setItem(slot,CustomEnchants.customEnchantments.get(i).getBook().getCustomItem());
            slot++;
        }

        
        return inv;
    }    
}
