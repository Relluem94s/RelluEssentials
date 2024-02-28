package de.relluem94.minecraft.server.spigot.essentials.enchantment.interfaces;

import org.bukkit.inventory.ItemStack;

/**
 *
 * @author rellu
 */
public interface IEnchantment {

    void addTo(ItemStack i);

    void removeFrom(ItemStack i);
}
