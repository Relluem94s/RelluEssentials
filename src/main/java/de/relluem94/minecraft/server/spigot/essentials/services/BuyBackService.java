package de.relluem94.minecraft.server.spigot.essentials.services;

import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.models.items.CustomItem;
import de.relluem94.minecraft.server.spigot.essentials.registries.EnchantmentRegistry;
import de.relluem94.minecraft.server.spigot.essentials.repositories.BuyBackRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@AllArgsConstructor
public class BuyBackService {

  private final BuyBackRepository buyBackRepository;
  private final ServiceContext serviceContext;

  public void recordSoldItems(Player player, ItemStack item, int totalAmount) {
    ItemStack resolvedItem = resolveCanonicalItem(item);
    List<ItemStack> stacks = splitIntoStacks(resolvedItem, totalAmount);
    buyBackRepository.addItems(player, stacks);
  }

  public List<ItemStack> getBuyBackItems(Player player) {
    return buyBackRepository.findByPlayer(player);
  }

  public boolean hasBuyBackItems(Player player) {
    return !buyBackRepository.findByPlayer(player).isEmpty();
  }

  public void clearBuyBackHistory(Player player) {
    buyBackRepository.deleteByPlayer(player);
  }

  public void removeBuyBackItem(Player player) {
    buyBackRepository.removeLastEntry(player);
  }


  private ItemStack resolveCanonicalItem(ItemStack item) {
    return EnchantmentRegistry.findByBookItemStack(item)
        .map(enchantment -> enchantment.getBook().getCustomItem())
        .orElseGet(() -> serviceContext.getItemService().findByItemStack(item)
            .map(CustomItem::toItemStack)
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