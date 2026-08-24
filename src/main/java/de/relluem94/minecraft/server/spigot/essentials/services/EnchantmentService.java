package de.relluem94.minecraft.server.spigot.essentials.services;

import de.relluem94.minecraft.server.spigot.essentials.helpers.EnchantmentHelper;
import de.relluem94.minecraft.server.spigot.essentials.models.RelluEssentialsNamespacedKey;
import de.relluem94.minecraft.server.spigot.essentials.registries.EnchantmentRegistry;
import java.util.List;
import java.util.Optional;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.jspecify.annotations.NonNull;

/**
 * Service for managing enchantments.
 */
public class EnchantmentService {

  private final EnchantmentRegistry registry;

  /**
   * Creates a new EnchantmentService.
   *
   * @param enchantmentRegistry the registry used to store and retrieve enchantments
   */
  public EnchantmentService(EnchantmentRegistry enchantmentRegistry) {
    this.registry = enchantmentRegistry;
  }

  /**
   * Registers a new enchantment.
   *
   * @param plugin the plugin responsible for the enchantment
   * @param namespacedKey the namespaced key of the enchantment
   * @param enchantment the enchantment helper to register
   */
  public void register(Plugin plugin, String namespacedKey, EnchantmentHelper enchantment) {
    registry.register(plugin, namespacedKey, enchantment);
  }

  /**
   * Finds an enchantment by its namespaced key.
   *
   * @param key the namespaced key to look for
   * @return an {@link Optional} containing the found enchantment, or empty if not found
   */
  public @NonNull Optional<EnchantmentHelper> find(RelluEssentialsNamespacedKey key) {
    return registry.find(key);
  }

  /**
   * Retrieves all registered enchantments.
   *
   * @return a list of all registered {@link EnchantmentHelper} instances
   */
  public @NonNull List<EnchantmentHelper> findAll() {
    return registry.findAll();
  }

  /**
   * Finds an enchantment from a book item stack.
   *
   * @param itemStack the item stack to check
   * @return an {@link Optional} containing the enchantment found in the book or empty if none found
   */
  public @NonNull Optional<EnchantmentHelper> findByBookItemStack(@NonNull ItemStack itemStack) {
    return registry.findByBookItemStack(itemStack);
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
   * @return the total count of enchantments
   */
  public int count() {
    return registry.count();
  }
}