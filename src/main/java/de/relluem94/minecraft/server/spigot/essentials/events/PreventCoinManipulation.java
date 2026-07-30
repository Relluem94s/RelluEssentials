package de.relluem94.minecraft.server.spigot.essentials.events;

import de.relluem94.minecraft.server.spigot.essentials.helpers.ItemHelper;
import de.relluem94.minecraft.server.spigot.essentials.model.RegistryKey;
import de.relluem94.minecraft.server.spigot.essentials.registry.ItemRegistry;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import static de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants.PLUGIN_ITEM_NAMESPACE_COINS;

public class PreventCoinManipulation implements Listener {

    private final ItemHelper coinItem = ItemRegistry.find(RegistryKey.of(PLUGIN_ITEM_NAMESPACE_COINS)).orElseThrow();

    @EventHandler
    public void preventCoinCrafting(@NotNull PrepareItemCraftEvent e) {
        CraftingInventory inventory = e.getInventory();
        for (ItemStack item : inventory.getMatrix()) {
            if (item != null && coinItem.almostEquals(item)) {
                inventory.setResult(new ItemStack(Material.AIR));
                return;
            }
        }
    }

    @EventHandler
    public void preventCoinAnvilRename(@NotNull PrepareAnvilEvent e) {
        for (ItemStack item : e.getInventory().getContents()) {
            if (item != null && coinItem.almostEquals(item)) {
                e.setResult(new ItemStack(Material.AIR));
                return;
            }
        }
    }
}
