package de.relluem94.minecraft.server.spigot.essentials.constants;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import org.bukkit.NamespacedKey;

public final class NamespacedKeyConstants {

  private NamespacedKeyConstants() {
    throw new IllegalStateException();
  }

  public static NamespacedKey itemCoins() {
    return new NamespacedKey(RelluEssentials.getInstance(), "coins");
  }

  public static NamespacedKey itemSellPrice() {
    return new NamespacedKey(RelluEssentials.getInstance(), "itemSellPrice");
  }

  public static NamespacedKey itemBuyPrice() {
    return new NamespacedKey(RelluEssentials.getInstance(), "itemBuyPrice");
  }

  public static NamespacedKey itemCost() {
    return new NamespacedKey(RelluEssentials.getInstance(), "item_cost");
  }
}