package de.relluem94.minecraft.server.spigot.essentials.registry;

import static de.relluem94.minecraft.server.spigot.essentials.constants.ExceptionConstants.PLUGIN_EXCEPTION_INVENTORY_REGISTRY;

import de.relluem94.minecraft.server.spigot.essentials.helpers.ItemHelper;
import de.relluem94.minecraft.server.spigot.essentials.model.RegistryKey;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.NonNull;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Unmodifiable;

public class InventoryRegistry {

  private static final Map<String, RegisteredInventory> registeredInventories = new LinkedHashMap<>();

  private InventoryRegistry() {
  }

  public static @NonNull RegisteredInventory create(@NonNull Plugin plugin,
      @NonNull String inventoryId, @NonNull String title, int size,
      @NonNull ItemHelper.Type itemFilter) {
    RegistryKey key = RegistryKey.of(plugin, inventoryId);
    return createWithKey(key, title, size, itemFilter);
  }

  public static @NonNull RegisteredInventory create(@NonNull RegistryKey key, @NonNull String title,
      int size, @NonNull ItemHelper.Type itemFilter) {
    return createWithKey(key, title, size, itemFilter);
  }

  private static @NonNull RegisteredInventory createWithKey(@NonNull RegistryKey key,
      @NonNull String title, int size, @NonNull ItemHelper.Type itemFilter) {
    if (registeredInventories.containsKey(key.toString())) {
      throw new IllegalArgumentException(String.format(PLUGIN_EXCEPTION_INVENTORY_REGISTRY, key));
    }
    RegisteredInventory inventory = new RegisteredInventory(key, title, size, itemFilter);
    registeredInventories.put(key.toString(), inventory);
    return inventory;
  }

  public static @NonNull Optional<RegisteredInventory> find(@NonNull RegistryKey key) {
    return Optional.ofNullable(registeredInventories.get(key.toString()));
  }

  public static @NonNull @Unmodifiable List<RegisteredInventory> getAllByNamespace(
      @NonNull String namespace) {
    return registeredInventories.entrySet().stream()
        .filter(entry -> entry.getKey().startsWith(namespace.toLowerCase() + ":"))
        .map(Map.Entry::getValue)
        .toList();
  }
}