package de.relluem94.minecraft.server.spigot.essentials.registries;

import de.relluem94.minecraft.server.spigot.essentials.helpers.EnchantmentHelper;
import de.relluem94.minecraft.server.spigot.essentials.models.RelluEssentialsNamespacedKey;
import de.relluem94.minecraft.server.spigot.essentials.persistence.bukkit.BukkitRegistryAdapter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.jspecify.annotations.NonNull;

/**
 * Registry for managing custom {@link EnchantmentHelper} instances, identified by a
 * {@link RelluEssentialsNamespacedKey}.
 */
public class EnchantmentRegistry {

  private final Map<RelluEssentialsNamespacedKey, EnchantmentHelper> registry = new HashMap<>();
  private final BukkitRegistryAdapter registryAdapter;

  /**
   * Creates a new {@link EnchantmentRegistry} with the given {@link BukkitRegistryAdapter}.
   *
   * @param registryAdapter the adapter used to resolve Bukkit registry entries
   */
  public EnchantmentRegistry(BukkitRegistryAdapter registryAdapter) {
    this.registryAdapter = registryAdapter;
  }

  /**
   * Registers an enchantment under a key derived from the given plugin and namespaced key.
   *
   * @param plugin        the plugin that owns the enchantment
   * @param namespacedKey the unique namespaced key identifying the enchantment
   * @param enchantment   the {@link EnchantmentHelper} instance to register
   */
  public void register(Plugin plugin, String namespacedKey, EnchantmentHelper enchantment) {
    registry.put(new RelluEssentialsNamespacedKey(plugin.getName(), namespacedKey), enchantment);
  }

  /**
   * Looks up a registered enchantment by its {@link RelluEssentialsNamespacedKey}.
   *
   * @param key the registry key to look up
   * @return an {@link Optional} containing the enchantment if found, or empty if not registered
   */
  public @NonNull Optional<EnchantmentHelper> find(RelluEssentialsNamespacedKey key) {
    return Optional.ofNullable(registry.get(key));
  }

  /**
   * Returns a list of all currently registered enchantments.
   *
   * @return an unmodifiable {@link List} of all registered {@link EnchantmentHelper} instances
   */
  public @NonNull List<EnchantmentHelper> findAll() {
    return List.copyOf(registry.values());
  }

  /**
   * Attempts to find a registered enchantment that is present in the persistent data of a book item
   * stack but not yet applied as a standard enchantment.
   *
   * @param itemStack the item stack to check
   * @return an {@link Optional} containing the matching enchantment, or empty if no match is found
   */
  public @NonNull Optional<EnchantmentHelper> findByBookItemStack(
      @NonNull ItemStack itemStack) {
    if (!(itemStack.getItemMeta() instanceof EnchantmentStorageMeta meta)) {
      return Optional.empty();
    }

    List<NamespacedKey> storedEnchantmentKeys = registryAdapter.resolveEnchantmentKeys(
        meta.getStoredEnchants());

    return registry.values().stream()
        .filter(enchantment -> meta.getPersistentDataContainer()
            .has(enchantment.getKey(), PersistentDataType.INTEGER))
        .filter(enchantment -> storedEnchantmentKeys.stream()
            .noneMatch(storedKey -> storedKey.equals(enchantment.getKey())))
        .findFirst();
  }

  /**
   * Clears all registered enchantments.
   */
  public void clear() {
    registry.clear();
  }

  /**
   * Returns the number of registered enchantments.
   *
   * @return the size of the registry
   */
  public int count() {
    return registry.size();
  }
}