package de.relluem94.minecraft.server.spigot.essentials.services;

import de.relluem94.minecraft.server.spigot.essentials.models.RelluEssentialsNamespacedKey;
import de.relluem94.minecraft.server.spigot.essentials.models.items.CustomItem;
import de.relluem94.minecraft.server.spigot.essentials.registries.ItemRegistry;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.NonNull;
import org.bukkit.inventory.ItemStack;

/**
 * Service for managing and accessing registered items.
 *
 * @author rellu
 */
public class ItemService {

  private final ItemRegistry itemRegistry;

  /**
   * Creates a new {@code ItemService}.
   *
   * @param itemRegistry the registry instance
   */
  public ItemService(@NonNull ItemRegistry itemRegistry) {
    this.itemRegistry = itemRegistry;
  }

  /**
   * Registers a new {@link CustomItem} using a {@link RelluEssentialsNamespacedKey}.
   *
   * @param customItem the item helper instance
   */
  public void register(@NonNull CustomItem customItem) {
    itemRegistry.register(customItem.relluEssentialsNamespacedKey(), customItem);
  }

  /**
   * Finds an item by its {@link RelluEssentialsNamespacedKey}.
   *
   * @param key the registry key
   * @return an {@link Optional} containing the item, or empty if not found
   */
  public Optional<CustomItem> find(@NonNull RelluEssentialsNamespacedKey key) {
    return itemRegistry.find(key);
  }

  /**
   * Finds an item by its item stack.
   *
   * @param itemStack the item stack to check
   * @return an {@link Optional} containing the item, or empty if not found
   */
  public Optional<CustomItem> findByItemStack(@NonNull ItemStack itemStack) {
    return itemRegistry.findByItemStack(itemStack);
  }

  /**
   * Checks if the given item stack matches the item registered with the provided identifier.
   *
   * @param identifier the key to check against
   * @param itemStack  the item stack to verify
   * @return true if the item stack matches the registered item, false otherwise
   */
  public boolean isItemStack(@NonNull RelluEssentialsNamespacedKey identifier,
      @NonNull ItemStack itemStack) {
    return find(identifier).map(customItem -> customItem.toItemStack().isSimilar(itemStack))
        .isPresent();
  }

  /**
   * Returns all registered items of a specific type.
   *
   * @param type the type to filter by
   * @return a list of items matching the type
   */
  public List<CustomItem> getAllByType(@NonNull CustomItem.Type type) {
    return itemRegistry.getAllByType(type);
  }

  /**
   * Returns all registered items.
   *
   * @return a map of all registered items
   */
  public Map<String, CustomItem> getAll() {
    return itemRegistry.getAll();
  }
}