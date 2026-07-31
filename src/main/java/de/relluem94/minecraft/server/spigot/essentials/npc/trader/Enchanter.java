package de.relluem94.minecraft.server.spigot.essentials.npc.trader;

import de.relluem94.minecraft.server.spigot.essentials.CustomEnchants;
import de.relluem94.minecraft.server.spigot.essentials.CustomItems;
import de.relluem94.minecraft.server.spigot.essentials.helpers.InventoryHelper;
import de.relluem94.minecraft.server.spigot.essentials.model.RegistryKey;
import de.relluem94.minecraft.server.spigot.essentials.registry.ItemRegistry;
import org.bukkit.entity.Villager.Profession;
import org.bukkit.inventory.Inventory;

import static de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants.PLUGIN_ITEM_NAMESPACE_AUTOSELL_HOPPER;
import static de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants.PLUGIN_ITEM_NAMESPACE_MAGIC_WATER_BUCKET;

public class Enchanter extends TraderNPC {

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

        int finalSlot = slot;
        ItemRegistry.find(RegistryKey.of(PLUGIN_ITEM_NAMESPACE_MAGIC_WATER_BUCKET))
                .ifPresent(item -> inv.setItem(finalSlot, item.getCustomItem()));
        slot++;
        int autoSellSlot = slot;
        ItemRegistry.find(RegistryKey.of(PLUGIN_ITEM_NAMESPACE_AUTOSELL_HOPPER))
                .ifPresent(item -> inv.setItem(autoSellSlot, item.getCustomItem()));

        return inv;
    }    
}