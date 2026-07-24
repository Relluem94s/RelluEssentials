package de.relluem94.minecraft.server.spigot.essentials.helpers.interfaces;

import org.bukkit.inventory.ItemStack;

/**
 * Defines the contract for item helper implementations that handle
 * the creation and configuration of Minecraft {@link ItemStack} objects.
 *
 * @author rellu
 */
public interface IItemHelper {

    void init();

    ItemStack postInit(ItemStack is);
}
