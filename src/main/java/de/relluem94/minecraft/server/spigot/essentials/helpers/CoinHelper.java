package de.relluem94.minecraft.server.spigot.essentials.helpers;

import static de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants.PLUGIN_ITEM_NAMESPACE_COINS;
import static de.relluem94.minecraft.server.spigot.essentials.constants.NamespacedKeyConstants.itemCoins;

import de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants;
import de.relluem94.minecraft.server.spigot.essentials.models.RegistryKey;
import de.relluem94.minecraft.server.spigot.essentials.registries.ItemRegistry;
import java.util.Collections;
import java.util.Objects;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

/**
 * Utility class for building coin-related {@link ItemStack} instances.
 */
public class CoinHelper {

  /**
   * Builds a coin {@link ItemStack} with the given amount stored in its lore and persistent data.
   *
   * @param coins the coin value to assign to the item
   * @return a configured coin {@link ItemStack}
   */
  public static ItemStack buildCoinItem(int coins) {
    ItemHelper coinItem = ItemRegistry.find(RegistryKey.of(PLUGIN_ITEM_NAMESPACE_COINS))
        .orElseThrow();
    ItemStack coin = coinItem.getCustomItem();
    ItemMeta im = Objects.requireNonNull(coin.getItemMeta());
    im.setLore(Collections.singletonList(
        String.format(ItemConstants.PLUGIN_ITEM_COINS_LORE, StringHelper.formatInt(coins))));
    im.getPersistentDataContainer().set(itemCoins(), PersistentDataType.INTEGER, coins);
    coin.setItemMeta(im);
    return coin;
  }
}
