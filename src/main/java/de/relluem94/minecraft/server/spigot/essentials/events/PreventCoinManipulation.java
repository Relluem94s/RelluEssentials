package de.relluem94.minecraft.server.spigot.essentials.events;

import de.relluem94.minecraft.server.spigot.essentials.CustomItems;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class PreventCoinManipulation implements Listener {

    @EventHandler
    public void preventCoinCrafting(@NotNull PrepareItemCraftEvent e) {
        CraftingInventory inventory = e.getInventory();
        for (ItemStack item : inventory.getMatrix()) {
            if (item != null && CustomItems.coins.almostEquals(item)) {
                inventory.setResult(new ItemStack(Material.AIR));
                return;
            }
        }
    }

    @EventHandler
    public void preventCoinAnvilRename(@NotNull PrepareAnvilEvent e) {
        for (ItemStack item : e.getInventory().getContents()) {
            if (item != null && CustomItems.coins.almostEquals(item)) {
                e.setResult(new ItemStack(Material.AIR));
                return;
            }
        }
    }
}
