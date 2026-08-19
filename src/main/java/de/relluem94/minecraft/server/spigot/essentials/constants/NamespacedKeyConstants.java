package de.relluem94.minecraft.server.spigot.essentials.constants;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import org.bukkit.NamespacedKey;

public final class NamespacedKeyConstants {

  private NamespacedKeyConstants() {
    throw new IllegalStateException();
  }

  public static NamespacedKey itemCoins() {
    return createKey("coins");
  }

  public static NamespacedKey itemSellPrice() {
    return createKey("itemSellPrice");
  }

  public static NamespacedKey itemBuyPrice() {
    return createKey("itemBuyPrice");
  }

  public static NamespacedKey itemCost() {
    return createKey("item_cost");
  }

  private static NamespacedKey createKey(String key) {
    return new NamespacedKey(RelluEssentials.getInstance(), key);
  }
}