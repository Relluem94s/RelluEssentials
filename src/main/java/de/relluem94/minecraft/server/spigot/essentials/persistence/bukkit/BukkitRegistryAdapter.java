package de.relluem94.minecraft.server.spigot.essentials.persistence.bukkit;

import de.relluem94.minecraft.server.spigot.essentials.annotations.Generated;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.GameRule;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Villager.Profession;

/**
 * Adapter that wraps Bukkit {@link Registry} lookups to allow decoupling and testability.
 *
 * <p>All access to Bukkit registries should go through this class
 * so that tests can mock it without triggering Bukkit server initialization.</p>
 */
@Generated
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

  /**
   * Resolves an {@link Enchantment} by its {@link NamespacedKey}.
   *
   * @param key the {@link NamespacedKey} of the enchantment to resolve
   * @return the matching {@link Enchantment}, or {@code null} if not found
   */
  public Enchantment resolveEnchantment(NamespacedKey key) {
    return Registry.ENCHANTMENT.get(key);
  }

  /**
   * Resolves all {@link NamespacedKey}s from a map keyed by {@link Enchantment}.
   *
   * @param enchantments the map of enchantments to resolve keys from
   * @return a list of resolved {@link NamespacedKey}s
   */
  public List<NamespacedKey> resolveEnchantmentKeys(
      Map<Enchantment, Integer> enchantments) {
    return enchantments.keySet().stream()
        .map(Enchantment::getKeyOrThrow)
        .toList();
  }

  private static final Map<String, GameRule<?>> GAME_RULE_CACHE = new HashMap<>();

  /**
   * Resolves and caches all available {@link GameRule}s from the Bukkit registry.
   * Must be called once after the Bukkit server is fully initialized.
   *
   * @param server the {@link Server} instance used to access the game rule registry
   */
  @SuppressWarnings("rawtypes")
  public void initializeGameRuleCache(Server server) {
    Registry<GameRule> registry = server.getRegistry(GameRule.class);
    if (registry == null) {
      return;
    }
    for (GameRule<?> rule : registry) {
      GAME_RULE_CACHE.put(rule.getKeyOrThrow().getKey(), rule);
    }
  }

  /**
   * Applies a game rule by its string key with the given boolean value to the provided world.
   *
   * @param world the {@link World} to apply the game rule to
   * @param key   the string key of the game rule (e.g. {@code "fireDamage"})
   * @param value the boolean value to set
   */
  @SuppressWarnings("unchecked")
  public void applyGameRule(World world, String key, boolean value) {
    GameRule<Boolean> gameRule = (GameRule<Boolean>) GAME_RULE_CACHE.get(key);
    if (gameRule == null) {
      return;
    }
    world.setGameRule(gameRule, value);
  }
}