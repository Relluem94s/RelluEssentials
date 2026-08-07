package de.relluem94.minecraft.server.spigot.essentials.npcs.trader;

import de.relluem94.minecraft.server.spigot.essentials.services.BuyBackService;
import java.util.List;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class BuyBackSlotResolver {

  private final BuyBackService buyBackService;
  private final ItemStack fallbackItem;

  public BuyBackSlotResolver(BuyBackService buyBackService, ItemStack fallbackItem) {
    this.buyBackService = buyBackService;
    this.fallbackItem = fallbackItem;
  }

  public ItemStack resolveForPlayer(Player player) {
    if(!buyBackService.hasBuyBackItems(player)){
      return fallbackItem;
    }

    List<ItemStack> buyBackItems = buyBackService.getBuyBackItems(player);
    if (buyBackItems.isEmpty()) {
      return fallbackItem;
    }
    return buyBackItems.getLast();
  }
}