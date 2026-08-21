package de.relluem94.minecraft.server.spigot.essentials.listeners.protect;

import static de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants.PLUGIN_ITEM_AUTOSELLHOPER;
import static de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants.PLUGIN_ITEM_NAMESPACE_COINS;

import de.relluem94.minecraft.server.spigot.essentials.annotations.ListenerName;
import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.enums.ItemPrice;
import de.relluem94.minecraft.server.spigot.essentials.enums.ProtectionFlags;
import de.relluem94.minecraft.server.spigot.essentials.helpers.ProtectionHelper;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.ListenerConstruct;
import de.relluem94.minecraft.server.spigot.essentials.models.RelluEssentialsNamespacedKey;
import de.relluem94.minecraft.server.spigot.essentials.models.items.CustomItem;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.ProtectionEntry;
import java.util.Objects;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Nameable;
import org.bukkit.block.BlockState;
import org.bukkit.block.DoubleChest;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * Handles item movement between inventories and enforces hopper protection and auto-sell logic.
 */
@ListenerName("InventoryMoveItemProtect")
public class InventoryMoveItemProtect implements ListenerConstruct {

  private static CustomItem coinItem = null;
  private ServiceContext serviceContext;

  public InventoryMoveItemProtect() {
    InventoryMoveItemProtect.coinItem = serviceContext.getItemService().find(
        new RelluEssentialsNamespacedKey(serviceContext.getPluginMetadataService().getName(),
            PLUGIN_ITEM_NAMESPACE_COINS)).orElseThrow();
  }

  private boolean sellItem(Inventory inventory, ItemStack is, boolean isSource,
      @NotNull Location location) {
    BlockState state = location.getBlock().getState();
    if ((state instanceof Nameable)) {
      String name = ((Nameable) state).getCustomName();

      if (name != null && name.contains(PLUGIN_ITEM_AUTOSELLHOPER)) {
        ItemPrice itemPrice = ItemPrice.from(is.getType());
        int sellPriceItem = itemPrice.getSellPrice() * is.getAmount();

        int size = 0;
        for (ItemStack itemStack : inventory.getStorageContents()) {
          if (itemStack == null) {
            continue;
          }

          if (!itemStack.getType().equals(Material.AIR)) {
            size++;
          }
        }

        if (coinItem.toItemStack().isSimilar(is) || sellPriceItem == 0) {
          return false;
        }

        if (!isSource && (inventory.firstEmpty() != -1 && size < 4)) {
          CustomItem coinItem = serviceContext.getItemService().find(
              new RelluEssentialsNamespacedKey(serviceContext.getPluginMetadataService().getName(),
                  PLUGIN_ITEM_NAMESPACE_COINS)).orElseThrow();
          inventory.addItem(coinItem, sellPriceItem);

          final ItemStack toRemove = is.clone();
          serviceContext.getSchedulerService()
              .runTaskLater(() -> inventory.removeItem(toRemove), 1L);
          return false;
        } else {
          return true;
        }
      }
    }
    return false;
  }

  @Override
  public void injectContext(ServiceContext context) {
    this.serviceContext = context;
  }

  /**
   * Cancels item movement if hopper protection is active or processes auto-sell hopper logic.
   */
  @EventHandler(ignoreCancelled = true)
  public void onMoveItem(@NotNull InventoryMoveItemEvent e) {
    if (handleMoveItemEvent(e.getSource(), e.getItem(), true) || handleMoveItemEvent(
        e.getDestination(), e.getItem(), false)) {
      e.setCancelled(true);
    }
  }

  private boolean handleMoveItemEvent(Inventory inventory, ItemStack is, boolean isSource) {
    Location location;
    Location locationOtherSide = null;
    InventoryHolder holder;
    if (inventory != null) {
      holder = inventory.getHolder();

      if (inventory.getType().equals(InventoryType.HOPPER)) {
        return sellItem(inventory, is, isSource,
            ((BlockState) Objects.requireNonNull(holder)).getLocation());
      }

      try {
        if (holder instanceof BlockState) {
          location = ((BlockState) holder).getLocation();
        } else if (holder instanceof DoubleChest doubleChest) {
          location = Objects.requireNonNull(
                  Objects.requireNonNull(doubleChest.getRightSide()).getInventory().getLocation())
              .getBlock().getLocation();
          locationOtherSide = Objects.requireNonNull(
                  Objects.requireNonNull(doubleChest.getLeftSide()).getInventory().getLocation())
              .getBlock().getLocation();
        } else {
          return false;
        }
      } catch (Exception e) {
        return false;
      }

      ProtectionEntry protection = serviceContext.getProtectionService()
          .getProtectionEntry(location);

      if (protection == null) {
        if (locationOtherSide == null) {
          return false;
        } else {
          protection = serviceContext.getProtectionService().getProtectionEntry(locationOtherSide);
        }

        if (protection == null) {
          return false;
        } else {
          if (isSource) {
            return !ProtectionHelper.hasFlag(protection, ProtectionFlags.ALLOW_HOPPER);
          } else {
            return true;
          }
        }
      } else {
        return !ProtectionHelper.hasFlag(protection, ProtectionFlags.ALLOW_HOPPER);
      }
    } else {
      return false;
    }
  }
}