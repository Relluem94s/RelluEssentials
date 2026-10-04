package de.relluem94.minecraft.server.spigot.essentials.npcs.trader;

import de.relluem94.minecraft.server.spigot.essentials.services.BuyBackService;
import java.util.List;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Resolves the item to display in the buy-back slot of a trader inventory for a given player.
 *
 * <p>If the player has no buy-back items available, a fallback item is returned instead.</p>
 *
 * @author rellu
 */
public class BuyBackSlotResolver {

  private final BuyBackService buyBackService;
  private final ItemStack fallbackItem;

  /**
   * Creates a new {@link BuyBackSlotResolver} with the given service and fallback item.
   *
   * @param buyBackService the service used to retrieve buy-back items for a player
   * @param fallbackItem the item to display when no buy-back items are available
   */
  public BuyBackSlotResolver(BuyBackService buyBackService, ItemStack fallbackItem) {
    this.buyBackService = buyBackService;
    this.fallbackItem = fallbackItem;
  }

  /**
   * Resolves the item to display in the buy-back slot for the given player.
   *
   * <p>Returns the most recently added buy-back item if available, otherwise returns the fallback item.</p>
   *
   * @param player the player for whom the buy-back slot item is resolved
   * @return the last buy-back {@link ItemStack} for the player, or the fallback item if none exist
   */
  public ItemStack resolveForPlayer(Player player) {
    if (!buyBackService.hasBuyBackItems(player)) {
      return fallbackItem;
    }

    List<ItemStack> buyBackItems = buyBackService.getBuyBackItems(player);
    if (buyBackItems.isEmpty()) {
      return fallbackItem;
    }
    return buyBackItems.getLast();
  }
}