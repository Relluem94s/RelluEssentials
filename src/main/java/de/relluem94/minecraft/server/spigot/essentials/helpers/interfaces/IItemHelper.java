package de.relluem94.minecraft.server.spigot.essentials.helpers.interfaces;

import org.bukkit.inventory.ItemStack;

/**
 *
 * @author rellu
 */
public interface IItemHelper {

    void init();

    ItemStack postInit(ItemStack is);
}
