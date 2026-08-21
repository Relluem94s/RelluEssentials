package de.relluem94.minecraft.server.spigot.essentials.registries;

import static de.relluem94.minecraft.server.spigot.essentials.constants.ExceptionConstants.PLUGIN_EXCEPTION_ITEM_REGISTRY;

import de.relluem94.minecraft.server.spigot.essentials.models.RelluEssentialsNamespacedKey;
import de.relluem94.minecraft.server.spigot.essentials.models.items.CustomItem;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.jspecify.annotations.NonNull;

/**
 * Internal registry for managing {@link CustomItem} instances.
 *
 * @author rellu
 */
public class ItemRegistry {

  private static final String REGISTRY_KEY_NAME = "registry_key";
  private final Map<String, CustomItem> registeredItems = new LinkedHashMap<>();
  private final NamespacedKey persistentDataKey;

  /**
   * Creates a new {@code ItemRegistry}.
   *
   * @param plugin the plugin instance
   */
  public ItemRegistry(@NonNull Plugin plugin) {
    this.persistentDataKey = new NamespacedKey(plugin, REGISTRY_KEY_NAME);
  }

  /**
   * Registers a new {@link CustomItem} using a {@link RelluEssentialsNamespacedKey}.
   *
   * @param key  the key to register the item under
   * @param item the item helper instance
   * @throws IllegalArgumentException if the item is already registered
   */
  public void register(@NonNull RelluEssentialsNamespacedKey key, @NonNull CustomItem item) {
    if (registeredItems.containsKey(key.toString())) {
      throw new IllegalArgumentException(String.format(PLUGIN_EXCEPTION_ITEM_REGISTRY, key));
    }
    registeredItems.put(key.toString(), item);
  }

  /**
   * Finds an item by its {@link RelluEssentialsNamespacedKey}.
   *
   * @param key the key to search for
   * @return an {@link Optional} containing the item, or empty if not found
   */
  public Optional<CustomItem> find(@NonNull RelluEssentialsNamespacedKey key) {
    return Optional.ofNullable(registeredItems.get(key.toString()));
  }

  /**
   * Finds an item by its string identifier.
   *
   * @param identifier the string ID to search for
   * @return an {@link Optional} containing the item, or empty if not found
   */
  public Optional<CustomItem> findByIdentifier(@NonNull String identifier) {
    return Optional.ofNullable(registeredItems.get(identifier));
  }

  /**
   * Finds an item by inspecting its {@link ItemStack}.
   *
   * @param itemStack the item stack to check
   * @return an {@link Optional} containing the item, or empty if not found
   */
  public Optional<CustomItem> findByItemStack(@NonNull ItemStack itemStack) {
    return registeredItems.values().stream()
        .filter(item -> item.toItemStack().isSimilar(itemStack))
        .findFirst();
  }

  /**
   * Returns all registered items.
   *
   * @return a map of all registered items
   */
  public Map<String, CustomItem> getAll() {
    return registeredItems;
  }

  /**
   * Returns all registered items of a specific type.
   *
   * @param type the type to filter by
   * @return a list of items matching the type
   */
  public List<CustomItem> getAllByType(CustomItem.Type type) {
    return registeredItems.values().stream()
        .filter(item -> item.type().equals(type))
        .toList();
  }
}