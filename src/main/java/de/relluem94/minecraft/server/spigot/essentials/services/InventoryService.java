package de.relluem94.minecraft.server.spigot.essentials.services;

import de.relluem94.minecraft.server.spigot.essentials.helpers.ItemHelper;
import de.relluem94.minecraft.server.spigot.essentials.models.RegistryKey;
import de.relluem94.minecraft.server.spigot.essentials.registries.InventoryRegistry;
import de.relluem94.minecraft.server.spigot.essentials.registries.model.RegisteredInventory;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Unmodifiable;

/**
 * Service responsible for managing and accessing registered inventories via an internal registry.
 */
@AllArgsConstructor
public class InventoryService {

  private final InventoryRegistry registry;

  /**
   * Creates a new inventory using a plugin and an ID.
   *
   * @param plugin      The plugin instance.
   * @param inventoryId The unique ID.
   * @param title       The inventory title.
   * @param size        The inventory size.
   * @param itemFilter  The item filter type.
   * @return The created {@link RegisteredInventory}.
   */
  public @NonNull RegisteredInventory create(@NonNull Plugin plugin,
      @NonNull String inventoryId, @NonNull String title, int size,
      @NonNull ItemHelper.Type itemFilter) {
    RegistryKey key = RegistryKey.of(plugin, inventoryId);
    return create(key, title, size, itemFilter);
  }

  /**
   * Creates a new inventory using a specific registry key.
   *
   * @param key        The unique registry key.
   * @param title      The inventory title.
   * @param size       The inventory size.
   * @param itemFilter The item filter type.
   * @return The created {@link RegisteredInventory}.
   */
  public @NonNull RegisteredInventory create(@NonNull RegistryKey key, @NonNull String title,
      int size, @NonNull ItemHelper.Type itemFilter) {
    return registry.register(key, title, size, itemFilter);
  }

  /**
   * Finds an inventory by its key.
   *
   * @param key The key to search for.
   * @return An {@link Optional} containing the inventory.
   */
  public @NonNull Optional<RegisteredInventory> find(@NonNull RegistryKey key) {
    return registry.find(key);
  }

  /**
   * Retrieves all inventories belonging to a specific namespace.
   *
   * @param namespace The namespace to filter by.
   * @return An unmodifiable list of inventories.
   */
  public @NonNull @Unmodifiable List<RegisteredInventory> getAllByNamespace(
      @NonNull String namespace) {
    return registry.findAllByNamespace(namespace);
  }
}