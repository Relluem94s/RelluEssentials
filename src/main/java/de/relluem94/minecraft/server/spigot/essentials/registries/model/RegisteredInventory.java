package de.relluem94.minecraft.server.spigot.essentials.registries.model;

import de.relluem94.minecraft.server.spigot.essentials.helpers.InventoryHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.ItemHelper;
import de.relluem94.minecraft.server.spigot.essentials.models.CustomInventory;
import de.relluem94.minecraft.server.spigot.essentials.models.RegistryKey;
import de.relluem94.minecraft.server.spigot.essentials.services.ItemService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;

/**
 * Represents a configuration for a specific type of inventory that can be opened.
 */
@Getter
public class RegisteredInventory {

  private final RegistryKey registryKey;
  private final String title;
  private final int size;
  private final ItemHelper.Type itemFilter;
  private final List<ItemHelper> fixedItems;

  /**
   * Creates a new RegisteredInventory instance.
   *
   * @param registryKey the unique registry key for this inventory
   * @param title       the title displayed in the inventory
   * @param size        the number of slots in the inventory
   * @param itemFilter  the type of items allowed in this inventory
   */
  public RegisteredInventory(@NonNull RegistryKey registryKey, @NonNull String title, int size,
      ItemHelper.@NonNull Type itemFilter) {
    this.registryKey = registryKey;
    this.title = title;
    this.size = size;
    this.itemFilter = itemFilter;
    this.fixedItems = new ArrayList<>();
  }

  /**
   * Adds a fixed item to the inventory configuration.
   *
   * @param item the item to be added as a fixed item
   * @return this instance for method chaining
   */
  public RegisteredInventory withFixedItem(@NonNull ItemHelper item) {
    fixedItems.add(item);
    return this;
  }

  /**
   * Opens the inventory for the specified player, including fixed items and additional items.
   *
   * @param player     the player to open the inventory for
   * @param extraItems additional items to include in the inventory
   */
  public void openFor(@NonNull Player player, ItemHelper... extraItems) {
    List<ItemHelper> items = new ArrayList<>(fixedItems);
    items.addAll(Arrays.asList(extraItems));

    CustomInventory customInventory = new CustomInventory(itemFilter, size, title);
    customInventory.setCustomItems(items);

    InventoryHelper.openInventory(player, InventoryHelper.getCustomItemInventory(customInventory));
  }

  /**
   * Opens the inventory for the specified player, including all items of the specified type from
   * the item service, fixed items, and additional items.
   *
   * @param itemService the service used to retrieve items by type
   * @param player      the player to open the inventory for
   * @param extraItems  additional items to include in the inventory
   */
  public void openForWithTypeFilter(@NonNull ItemService itemService, @NonNull Player player,
      ItemHelper... extraItems) {
    List<ItemHelper> items = new ArrayList<>(itemService.getAllByType(itemFilter));
    items.addAll(fixedItems);
    items.addAll(Arrays.asList(extraItems));

    CustomInventory customInventory = new CustomInventory(itemFilter, size, title);
    customInventory.setCustomItems(items);

    InventoryHelper.openInventory(player,
        InventoryHelper.getCustomItemInventory(customInventory, itemFilter));
  }

  /**
   * Returns an unmodifiable view of the fixed items.
   *
   * @return a list containing the fixed items
   */
  public List<ItemHelper> getFixedItems() {
    return Collections.unmodifiableList(fixedItems);
  }
}