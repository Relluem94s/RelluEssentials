package de.relluem94.minecraft.server.spigot.essentials.constants;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import org.bukkit.NamespacedKey;

public interface NamespacedKeyConstants {
    NamespacedKey itemCoins = new NamespacedKey(RelluEssentials.getInstance(), "coins");

    NamespacedKey itemSellPrice = new NamespacedKey(RelluEssentials.getInstance(), "itemSellPrice");
    NamespacedKey itemBuyPrice = new NamespacedKey(RelluEssentials.getInstance(), "itemBuyPrice");

}
