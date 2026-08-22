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

  public EnchantmentService(EnchantmentRegistry enchantmentRegistry) {
    this.registry = enchantmentRegistry;
  }

  public void register(Plugin plugin, String namespacedKey, EnchantmentHelper enchantment) {
    registry.register(plugin, namespacedKey, enchantment);
  }

  public @NonNull Optional<EnchantmentHelper> find(RelluEssentialsNamespacedKey key) {
    return registry.find(key);
  }

  public @NonNull List<EnchantmentHelper> findAll() {
    return registry.findAll();
  }

  public @NonNull Optional<EnchantmentHelper> findByBookItemStack(@NonNull ItemStack itemStack) {
    return registry.findByBookItemStack(itemStack);
  }

  public void clear() {
    registry.clear();
  }

  public int count() {
    return registry.count();
  }
}