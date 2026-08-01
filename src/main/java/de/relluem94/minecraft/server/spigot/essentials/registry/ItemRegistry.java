package de.relluem94.minecraft.server.spigot.essentials.registry;

import static de.relluem94.minecraft.server.spigot.essentials.constants.ExceptionConstants.PLUGIN_EXCEPTION_ITEM_REGISTRY;

import de.relluem94.minecraft.server.spigot.essentials.constants.NamespacedKeyConstants;
import de.relluem94.minecraft.server.spigot.essentials.helpers.ItemHelper;
import de.relluem94.minecraft.server.spigot.essentials.model.RegistryKey;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.NonNull;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

public class ItemRegistry {

  private static final String REGISTRY_KEY_NAME = "registry_key";
  private static final Map<String, ItemHelper> registeredItems = new LinkedHashMap<>();
  private static NamespacedKey persistentDataKey;

  private ItemRegistry() {
  }

  public static void initialize(@NonNull Plugin plugin) {
    if (persistentDataKey != null) {
      return;
    }
    persistentDataKey = new NamespacedKey(plugin, REGISTRY_KEY_NAME);
    RegistryKey.initializeInternalPlugin(plugin);
  }

  public static void register(@NonNull Plugin plugin, @NonNull String itemId,
      @NonNull ItemHelper item) {
    RegistryKey key = RegistryKey.of(plugin, itemId);
    registerWithKey(key, item);
  }

  public static void register(@NonNull RegistryKey key, @NonNull ItemHelper item) {
    registerWithKey(key, item);
  }

  private static void registerWithKey(@NonNull RegistryKey key, @NonNull ItemHelper item) {
    ensureInitialized();
    if (registeredItems.containsKey(key.toString())) {
      throw new IllegalArgumentException(String.format(PLUGIN_EXCEPTION_ITEM_REGISTRY, key));
    }
    item.setData(persistentDataKey, key.toString());
    item.applyCostToItemStack(NamespacedKeyConstants.itemCost());
    registeredItems.put(key.toString(), item);
  }

  private static void ensureInitialized() {
    if (persistentDataKey == null) {
      throw new IllegalStateException(
          "ItemRegistry has not been initialized. Call ItemRegistry.initialize(plugin) first.");
    }
  }

  public static Optional<ItemHelper> find(@NonNull RegistryKey key) {
    return Optional.ofNullable(registeredItems.get(key.toString()));
  }

  public static Optional<RegistryKey> identifyFromItemStack(@NonNull ItemStack itemStack) {
    ItemMeta meta = itemStack.getItemMeta();
      if (meta == null) {
          return Optional.empty();
      }

    String rawKey = meta.getPersistentDataContainer()
        .get(persistentDataKey, PersistentDataType.STRING);
      if (rawKey == null) {
          return Optional.empty();
      }

    String[] parts = rawKey.split(":", 2);
      if (parts.length != 2) {
          return Optional.empty();
      }

    return Optional.of(RegistryKey.of(parts[0], parts[1]));
  }

  public static Optional<ItemHelper> findByItemStack(@NonNull ItemStack itemStack) {
    return identifyFromItemStack(itemStack)
        .flatMap(key -> Optional.ofNullable(registeredItems.get(key.toString())));
  }

  public static boolean isRegisteredItem(@NonNull ItemStack itemStack) {
    return identifyFromItemStack(itemStack).isPresent();
  }

  public static List<ItemHelper> getAllByType(@NonNull ItemHelper.Type type) {
    return registeredItems.values().stream()
        .filter(item -> item.getItemType() == type)
        .toList();
  }

  public static List<ItemHelper> getAllByNamespace(@NonNull String namespace) {
    return registeredItems.entrySet().stream()
        .filter(entry -> entry.getKey().startsWith(namespace.toLowerCase() + ":"))
        .map(Map.Entry::getValue)
        .toList();
  }

  public static Collection<ItemHelper> getAll() {
    return Collections.unmodifiableCollection(registeredItems.values());
  }
}