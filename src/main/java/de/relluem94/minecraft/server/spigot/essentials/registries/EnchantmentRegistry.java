package de.relluem94.minecraft.server.spigot.essentials.registries;

import de.relluem94.minecraft.server.spigot.essentials.constants.Constants;
import de.relluem94.minecraft.server.spigot.essentials.helpers.EnchantmentHelper;
import de.relluem94.minecraft.server.spigot.essentials.models.RegistryKey;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.jspecify.annotations.NonNull;

/**
 * Static registry for managing custom {@link EnchantmentHelper} instances,
 * identified by a {@link RegistryKey}.
 */
public class EnchantmentRegistry {

  private static final Map<RegistryKey, EnchantmentHelper> registry = new HashMap<>();

  private EnchantmentRegistry() {
    throw new IllegalStateException(Constants.PLUGIN_INTERNAL_UTILITY_CLASS);
  }

  /**
   * Registers an enchantment under a key derived from the given plugin and namespaced key.
   *
   * @param plugin        the plugin that owns the enchantment
   * @param namespacedKey the unique namespaced key identifying the enchantment
   * @param enchantment   the {@link EnchantmentHelper} instance to register
   */
  public static void register(Plugin plugin, String namespacedKey, EnchantmentHelper enchantment) {
    registry.put(RegistryKey.of(plugin, namespacedKey), enchantment);
  }

  /**
   * Looks up a registered enchantment by its {@link RegistryKey}.
   *
   * @param key the registry key to look up
   * @return an {@link Optional} containing the enchantment if found, or empty if not registered
   */
  public static @NonNull Optional<EnchantmentHelper> find(RegistryKey key) {
    return Optional.ofNullable(registry.get(key));
  }

  public static @NonNull List<EnchantmentHelper> findAll() {
    return List.copyOf(registry.values());
  }

  public static @NonNull Optional<EnchantmentHelper> findByBookItemStack(@NonNull ItemStack itemStack) {
    if (!(itemStack.getItemMeta() instanceof EnchantmentStorageMeta meta)) {
      return Optional.empty();
    }

    return registry.values().stream()
        .filter(enchantment -> meta.getPersistentDataContainer()
            .has(enchantment.getKey(), PersistentDataType.INTEGER))
        .filter(enchantment -> meta.getStoredEnchants().keySet().stream()
            .noneMatch(storedEnchant -> storedEnchant.getKey().equals(enchantment.getKey())))
        .findFirst();
  }

  public static int count() {
    return registry.size();
  }
}