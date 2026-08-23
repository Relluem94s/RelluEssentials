package de.relluem94.minecraft.server.spigot.essentials.registries;

import static de.relluem94.minecraft.server.spigot.essentials.constants.ExceptionConstants.PLUGIN_EXCEPTION_INVENTORY_REGISTRY;

import de.relluem94.minecraft.server.spigot.essentials.models.RelluEssentialsNamespacedKey;
import de.relluem94.minecraft.server.spigot.essentials.models.items.CustomItem;
import de.relluem94.minecraft.server.spigot.essentials.registries.model.RegisteredInventory;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.jetbrains.annotations.Unmodifiable;
import org.jspecify.annotations.NonNull;

/**
 * Registry responsible for the storage and retrieval of registered inventories.
 */
public class InventoryRegistry {

  private final Map<String, RegisteredInventory> registeredInventories = new LinkedHashMap<>();

  /**
   * Registers a new inventory into the storage.
   *
   * @param key        The unique registry key.
   * @param title      The display title.
   * @param size       The inventory size.
   * @param itemFilter The item filter type.
   * @return The registered {@link RegisteredInventory}.
   * @throws IllegalArgumentException if the key is already registered.
   */
  public @NonNull RegisteredInventory register(@NonNull RelluEssentialsNamespacedKey key,
      @NonNull String title, int size, CustomItem.Type itemFilter) {
    if (registeredInventories.containsKey(key.toString())) {
      throw new IllegalArgumentException(String.format(PLUGIN_EXCEPTION_INVENTORY_REGISTRY, key));
    }
    RegisteredInventory inventory = new RegisteredInventory(key, title, size, itemFilter);
    registeredInventories.put(key.toString(), inventory);
    return inventory;
  }

  /**
   * Finds an inventory by its key.
   *
   * @param key The key to search for.
   * @return An {@link Optional} containing the inventory.
   */
  public @NonNull Optional<RegisteredInventory> find(@NonNull RelluEssentialsNamespacedKey key) {
    return Optional.ofNullable(registeredInventories.get(key.toString()));
  }

  /**
   * Finds all inventories belonging to a namespace.
   *
   * @param namespace The namespace to filter by.
   * @return An unmodifiable list of inventories.
   */
  public @NonNull @Unmodifiable List<RegisteredInventory> findAllByNamespace(
      @NonNull String namespace) {
    return registeredInventories.entrySet().stream()
        .filter(entry -> entry.getKey().toLowerCase()
            .startsWith(namespace.toLowerCase() + ":"))
        .map(Map.Entry::getValue)
        .toList();
  }
}