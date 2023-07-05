package de.relluem94.minecraft.server.spigot.essentials.helpers.interfaces;

import org.bukkit.inventory.ItemStack;

/**
 *
 * @author rellu
 */
public interface IItemHelper {

    public void init();

    public ItemStack postInit(ItemStack is);
}
