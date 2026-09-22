package de.relluem94.minecraft.server.spigot.essentials.persistence.bukkit;

import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.entity.Villager.Profession;

/**
 * Adapter that wraps Bukkit {@link Registry} lookups to allow decoupling and testability.
 *
 * <p>All access to Bukkit registries should go through this class
 * so that tests can mock it without triggering Bukkit server initialization.</p>
 */
public class BukkitRegistryAdapter {

  /**
   * Resolves a {@link Profession} by its lowercase key name.
   *
   * @param key the lowercase profession key (e.g. {@code "farmer"})
   * @return the matching {@link Profession}, or {@code null} if not found
   */
  public Profession resolveProfession(String key) {
    return Registry.VILLAGER_PROFESSION.get(NamespacedKey.minecraft(key));
  }
}