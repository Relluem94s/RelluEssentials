package de.relluem94.minecraft.server.spigot.essentials.services;

import de.relluem94.minecraft.server.spigot.essentials.helpers.ItemHelper;
import de.relluem94.minecraft.server.spigot.essentials.models.RegistryKey;
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
   * Registers a new {@link ItemHelper} using a {@link RegistryKey}.
   *
   * @param key  the key to register the item under
   * @param item the item helper instance
   */
  public void register(@NonNull RegistryKey key, @NonNull ItemHelper item) {
    itemRegistry.register(key, item);
  }

  /**
   * Finds an item by its {@link RegistryKey}.
   *
   * @param key the registry key
   * @return an {@link Optional} containing the item, or empty if not found
   */
  public Optional<ItemHelper> find(@NonNull RegistryKey key) {
    return itemRegistry.find(key);
  }

  /**
   * Finds an item by its string identifier (namespace).
   *
   * @param identifier the string ID to search for
   * @return an {@link Optional} containing the item, or empty if not found
   */
  public Optional<ItemHelper> findByIdentifier(@NonNull String identifier) {
    return itemRegistry.findByIdentifier(identifier);
  }

  /**
   * Finds an item by its item stack.
   *
   * @param itemStack the item stack to check
   * @return an {@link Optional} containing the item, or empty if not found
   */
  public Optional<ItemHelper> findByItemStack(@NonNull ItemStack itemStack) {
    return itemRegistry.findByItemStack(itemStack);
  }

  /**
   * Checks if the given item stack matches the item registered with the provided identifier.
   *
   * @param identifier the string ID to check against
   * @param itemStack the item stack to verify
   * @return true if the item stack matches the registered item, false otherwise
   */
  public boolean isItemStack(@NonNull String identifier, @NonNull ItemStack itemStack) {
    return findByIdentifier(identifier)
        .map(itemHelper -> itemHelper.almostEquals(itemStack))
        .isPresent();
  }

  /**
   * Returns all registered items of a specific type.
   *
   * @param type the type to filter by
   * @return a list of items matching the type
   */
  public List<ItemHelper> getAllByType(@NonNull ItemHelper.Type type) {
    return itemRegistry.getAllByType(type);
  }

  /**
   * Returns all registered items.
   *
   * @return a map of all registered items
   */
  public Map<String, ItemHelper> getAll() {
    return itemRegistry.getAll();
  }
}