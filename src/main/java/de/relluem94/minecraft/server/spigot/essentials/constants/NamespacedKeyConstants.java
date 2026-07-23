package de.relluem94.minecraft.server.spigot.essentials.constants;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import org.bukkit.NamespacedKey;

public final class NamespacedKeyConstants {

    public static NamespacedKey itemCoins() {
        return new NamespacedKey(RelluEssentials.getInstance(), "coins");
    }

    public static NamespacedKey itemSellPrice() {
        return new NamespacedKey(RelluEssentials.getInstance(), "itemSellPrice");
    }

    public static NamespacedKey itemBuyPrice() {
        return new NamespacedKey(RelluEssentials.getInstance(), "itemBuyPrice");
    }

    private NamespacedKeyConstants() {
        throw new IllegalStateException();
    }
}