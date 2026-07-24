package de.relluem94.minecraft.server.spigot.essentials.events;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;

import de.relluem94.minecraft.server.spigot.essentials.CustomEnchants;
import de.relluem94.minecraft.server.spigot.essentials.helpers.EnchantmentHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.ItemHelper;

import static de.relluem94.minecraft.server.spigot.essentials.helpers.EnchantmentHelper.hasEnchant;

/**
 *
 * @author rellu
 */
public class CustomEnchantment implements Listener {

    /**
     * Checks whether itemStackSlotOne is an enchanted book
     * that carries the desired custom enchantment in its PersistentDataContainer.
     */
    private boolean isBookWithEnchant(ItemStack book, EnchantmentHelper enchant) {
        if (book == null) return false;
        if (book.getType() != Material.ENCHANTED_BOOK) return false;
        if (!(book.getItemMeta() instanceof EnchantmentStorageMeta meta)) return false;
        return meta.getPersistentDataContainer().has(enchant.getKey());
    }

    @EventHandler
    public void enchantApply(PrepareAnvilEvent e) {
        ItemStack itemStackSlotZero = e.getInventory().getItem(0);
        ItemStack itemStackSlotOne = e.getInventory().getItem(1);
        @SuppressWarnings("all")
        String renameText = e.getView().getRenameText();

        if (itemStackSlotZero == null) return;
        if (itemStackSlotOne == null) return;
        if (renameText == null) return;

        boolean slotOneIsCustomBook =
            isBookWithEnchant(itemStackSlotOne, CustomEnchants.autosmelt)    ||
            isBookWithEnchant(itemStackSlotOne, CustomEnchants.telekinesis)   ||
            isBookWithEnchant(itemStackSlotOne, CustomEnchants.replenishment) ||
            isBookWithEnchant(itemStackSlotOne, CustomEnchants.delicate)      ||
            isBookWithEnchant(itemStackSlotOne, CustomEnchants.thunderstrike);

        if (!slotOneIsCustomBook) {
            if (!renameText.equals(ItemHelper.getItemName(itemStackSlotZero))) {
                e.setResult(null);
                return;
            }
        }

        if (
            isBookWithEnchant(itemStackSlotZero, CustomEnchants.autosmelt)    ||
            isBookWithEnchant(itemStackSlotZero, CustomEnchants.telekinesis)   ||
            isBookWithEnchant(itemStackSlotZero, CustomEnchants.replenishment) ||
            isBookWithEnchant(itemStackSlotZero, CustomEnchants.delicate)      ||
            isBookWithEnchant(itemStackSlotZero, CustomEnchants.thunderstrike)
        ) {
            e.setResult(null);
            return;
        }
        
        try {
            if (e.getResult() != null) {
                for (EnchantmentHelper enchant : CustomEnchants.customEnchantments) {
                    if (itemStackSlotZero.hasItemMeta() && hasEnchant(itemStackSlotZero, enchant)) {
                    ItemStack is = e.getResult().clone();
                        enchant.removeFrom(is);
                        enchant.addTo(is);
                    e.setResult(is);
                }
                }
            }

            for (EnchantmentHelper enchant : CustomEnchants.customEnchantments) {
                if (isBookWithEnchant(itemStackSlotOne, enchant)
                        && !hasEnchant(itemStackSlotZero, enchant)
                        && enchant.getItemTarget().includes(itemStackSlotZero)) {
                    ItemStack is = itemStackSlotZero.clone();
                    enchant.addTo(is);
                    e.setResult(is);
                    break;
                }
            }

        } catch (IllegalArgumentException ex) {
            Logger.getLogger(CustomEnchantment.class.getName()).log(Level.SEVERE, ex.getMessage());
        }
    }
}