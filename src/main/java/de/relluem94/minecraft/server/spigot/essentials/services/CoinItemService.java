package de.relluem94.minecraft.server.spigot.essentials.services;

import static de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants.PLUGIN_ITEM_COINS_LORE;
import static de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants.PLUGIN_ITEM_NAMESPACE_COINS;
import static de.relluem94.minecraft.server.spigot.essentials.constants.NamespacedKeyConstants.itemCoins;

import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.helpers.StringHelper;
import de.relluem94.minecraft.server.spigot.essentials.models.RelluEssentialsNamespacedKey;
import de.relluem94.minecraft.server.spigot.essentials.models.items.CustomItem;
import java.util.Collections;
import lombok.NonNull;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

/**
 * Service responsible for building coin {@link ItemStack} instances from the registered coin
 * {@link CustomItem}.
 */
public class CoinItemService {

  private final ServiceContext serviceContext;

  /**
   * Creates a new {@code CoinItemService}.
   *
   * @param serviceContext the context providing access to all necessary services
   */
  public CoinItemService(@NonNull ServiceContext serviceContext) {
    this.serviceContext = serviceContext;
  }

  /**
   * Retrieves the registered coin {@link CustomItem}, builds an {@link ItemStack} from it, and
   * applies the given coin value to its lore and persistent data.
   *
   * @param coins the coin value to assign to the item
   * @return a fully configured coin {@link ItemStack}
   */
  public ItemStack getCoin(int coins) {
    CustomItem coinItem = serviceContext.getItemService().find(
        new RelluEssentialsNamespacedKey(serviceContext.getPluginMetadataService().getName(),
            PLUGIN_ITEM_NAMESPACE_COINS)).orElseThrow();

    ItemStack coin = coinItem.toItemStack();

    applyCoinsToItemMeta(coin, coins);

    return coin;
  }

  private void applyCoinsToItemMeta(ItemStack coin, int coins) {
    var meta = coin.getItemMeta();
    if (meta == null) {
      return;
    }
    meta.setLore(Collections.singletonList(
        String.format(PLUGIN_ITEM_COINS_LORE, StringHelper.formatInt(coins))));
    meta.getPersistentDataContainer().set(itemCoins(), PersistentDataType.INTEGER, coins);
    coin.setItemMeta(meta);
  }
}