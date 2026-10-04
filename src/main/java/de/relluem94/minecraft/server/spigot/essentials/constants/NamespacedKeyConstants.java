package de.relluem94.minecraft.server.spigot.essentials.constants;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import org.bukkit.NamespacedKey;

/**
 * Provides namespaced keys for item-related persistent data.
 *
 * @author rellu
 */
public final class NamespacedKeyConstants {

  private NamespacedKeyConstants() {
    throw new IllegalStateException();
  }

  /**
   * Provides namespaced keys for item-related persistent data.
   *
   * @author rellu
   */
  public static NamespacedKey itemCoins() {
    return createKey("coins");
  }

  /**
   * Returns the namespaced key used to store an item's coin value.
   *
   * @return the coin value key
   */
  public static NamespacedKey itemSellPrice() {
    return createKey("itemSellPrice");
  }

  /**
   * Returns the namespaced key used to store an item's sell price.
   *
   * @return the sell price key
   */
  public static NamespacedKey itemBuyPrice() {
    return createKey("itemBuyPrice");
  }

  /**
   * Returns the namespaced key used to store an item's cost.
   *
   * @return the item cost key
   */
  public static NamespacedKey itemCost() {
    return createKey("item_cost");
  }

  private static NamespacedKey createKey(String key) {
    return new NamespacedKey(RelluEssentials.getInstance(), key);
  }
}