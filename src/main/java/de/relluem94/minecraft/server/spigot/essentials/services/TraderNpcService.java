package de.relluem94.minecraft.server.spigot.essentials.services;

import de.relluem94.minecraft.server.spigot.essentials.npcs.trader.BankerNpc;
import de.relluem94.minecraft.server.spigot.essentials.npcs.trader.TraderNpc;
import de.relluem94.minecraft.server.spigot.essentials.registries.TraderNpcRegistry;
import java.util.List;
import lombok.Getter;
import org.bukkit.inventory.ItemStack;

public class TraderNpcService {

  private final TraderNpcRegistry traderNpcRegistry;
  @Getter
  private final BankerNpc bankerNpc;

  public TraderNpcService(TraderNpcRegistry traderNpcRegistry, BankerNpc bankerNpc) {
    this.traderNpcRegistry = traderNpcRegistry;
    this.bankerNpc = bankerNpc;
  }

  public List<TraderNpc> getAllNpcs() {
    return traderNpcRegistry.getNPCs();
  }

  public TraderNpc getNpc(int index) {
    return traderNpcRegistry.getNPC(index);
  }

  public List<ItemStack> getNpcSpawnEggs() {
    return traderNpcRegistry.getNPCItemStackList();
  }

  public List<String> getNpcNames() {
    return traderNpcRegistry.getNPCNameList();
  }

  public List<String> getTraderNpcTitles() {
    return traderNpcRegistry.getNPCTraderTitleList();
  }
}