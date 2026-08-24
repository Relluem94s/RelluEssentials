package de.relluem94.minecraft.server.spigot.essentials.repositories;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class BuyBackRepository {

  private final Map<Player, List<ItemStack>> buyBackHistory = new HashMap<>();

  public void addItems(Player player, List<ItemStack> items) {
    buyBackHistory.putIfAbsent(player, new ArrayList<>());
    buyBackHistory.get(player).addAll(items);
  }

  public List<ItemStack> findByPlayer(Player player) {
    return buyBackHistory.getOrDefault(player, new ArrayList<>());
  }

  public void removeLastEntry(Player player) {
    List<ItemStack> history = buyBackHistory.get(player);
    if (history != null && !history.isEmpty()) {
      history.remove(history.size() - 1);
    }
  }

  public void deleteByPlayer(Player player) {
    buyBackHistory.remove(player);
  }
}