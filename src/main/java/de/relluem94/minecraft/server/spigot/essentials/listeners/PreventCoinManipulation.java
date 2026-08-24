package de.relluem94.minecraft.server.spigot.essentials.listeners;

import static de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants.PLUGIN_ITEM_NAMESPACE_COINS;

import de.relluem94.minecraft.server.spigot.essentials.annotations.ListenerName;
import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.ListenerConstruct;
import de.relluem94.minecraft.server.spigot.essentials.models.RelluEssentialsNamespacedKey;
import de.relluem94.minecraft.server.spigot.essentials.models.items.CustomItem;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

@ListenerName("PreventCoinManipulation")
public class PreventCoinManipulation implements ListenerConstruct {

  private CustomItem coinItem = null;

  @Override
  public void injectContext(ServiceContext context) {
    coinItem = context.getItemService().find(new RelluEssentialsNamespacedKey(context.getPluginMetadataService().getName(), PLUGIN_ITEM_NAMESPACE_COINS))
        .orElseThrow();
  }

  @EventHandler
  public void preventCoinCrafting(@NotNull PrepareItemCraftEvent e) {
    CraftingInventory inventory = e.getInventory();
    for (ItemStack item : inventory.getMatrix()) {
      if (item != null && coinItem.toItemStack().isSimilar(item)) {
        inventory.setResult(new ItemStack(Material.AIR));
        return;
      }
    }
  }

  @EventHandler
  public void preventCoinAnvilRename(@NotNull PrepareAnvilEvent e) {
    for (ItemStack item : e.getInventory().getContents()) {
      if (item != null && coinItem.toItemStack().isSimilar(item)) {
        e.setResult(new ItemStack(Material.AIR));
        return;
      }
    }
  }
}
