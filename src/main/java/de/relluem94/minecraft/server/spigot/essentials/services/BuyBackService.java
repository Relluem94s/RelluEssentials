package de.relluem94.minecraft.server.spigot.essentials.services;

import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.helpers.EnchantmentHelper;
import de.relluem94.minecraft.server.spigot.essentials.models.items.CustomItem;
import de.relluem94.minecraft.server.spigot.essentials.repositories.BuyBackRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Service responsible for managing the buy-back history of players.
 * It allows recording items sold by players, retrieving their history,
 * and managing the removal or clearing of these records.
 */
@AllArgsConstructor
public class BuyBackService {

  private final BuyBackRepository buyBackRepository;
  private final ServiceContext serviceContext;

  /**
   * Records the items sold by a player into the buy-back repository.
   *
   * @param player      the player who sold the items
   * @param item        the item that was sold
   * @param totalAmount the total quantity of the item sold
   */
  public void recordSoldItems(Player player, ItemStack item, int totalAmount) {
    ItemStack resolvedItem = resolveCanonicalItem(item);
    List<ItemStack> stacks = splitIntoStacks(resolvedItem, totalAmount);
    buyBackRepository.addItems(player, stacks);
  }

  /**
   * Retrieves the list of items available in the player's buy-back history.
   *
   * @param player the player whose items are being retrieved
   * @return a list of {@link ItemStack}s available for buy-back
   */
  public List<ItemStack> getBuyBackItems(Player player) {
    return buyBackRepository.findByPlayer(player);
  }

  /**
   * Checks if the player has any items in their buy-back history.
   *
   * @param player the player to check
   * @return true if the player has buy-back items, false otherwise
   */
  public boolean hasBuyBackItems(Player player) {
    return !buyBackRepository.findByPlayer(player).isEmpty();
  }

  /**
   * Clears the entire buy-back history for the specified player.
   *
   * @param player the player whose history should be cleared
   */
  public void clearBuyBackHistory(Player player) {
    buyBackRepository.deleteByPlayer(player);
  }

  /**
   * Removes the most recent entry from the player's buy-back history.
   *
   * @param player the player whose history should be updated
   */
  public void removeBuyBackItem(Player player) {
    buyBackRepository.removeLastEntry(player);
  }

  private ItemStack resolveCanonicalItem(ItemStack item) {
    return serviceContext.getEnchantmentService().findByBookItemStack(item)
        .map(EnchantmentHelper::createEnchantedBook).orElseGet(
            () -> serviceContext.getItemService().findByItemStack(item).map(CustomItem::toItemStack)
                .orElse(item));
  }

  private List<ItemStack> splitIntoStacks(ItemStack item, int totalAmount) {
    List<ItemStack> stacks = new ArrayList<>();
    int maxStackSize = item.getType().getMaxStackSize();
    int remaining = totalAmount;
    while (remaining > 0) {
      int stackSize = Math.min(remaining, maxStackSize);
      ItemStack stack = new ItemStack(item);
      stack.setAmount(stackSize);
      stacks.add(stack);
      remaining -= stackSize;
    }
    return stacks;
  }
}