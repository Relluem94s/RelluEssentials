package de.relluem94.minecraft.server.spigot.essentials.services;

import de.relluem94.minecraft.server.spigot.essentials.npcs.trader.BankerNpc;
import de.relluem94.minecraft.server.spigot.essentials.npcs.trader.TraderNpc;
import de.relluem94.minecraft.server.spigot.essentials.registries.TraderNpcRegistry;
import de.relluem94.minecraft.server.spigot.essentials.repositories.TraderNpcRepository;
import java.util.List;
import lombok.Getter;
import org.bukkit.inventory.ItemStack;

/**
 * Service layer for trader NPC operations.
 *
 * <p>Coordinates loading of NPC configurations via {@link TraderNpcRepository},
 * populates the {@link TraderNpcRegistry}, and exposes NPC data
 * to other parts of the application.</p>
 */
public class TraderNpcService {

  private final TraderNpcRegistry traderNpcRegistry;
  private final TraderNpcRepository traderNpcRepository;
  @Getter
  private final BankerNpc bankerNpc;

  /**
   * Constructs a new {@link TraderNpcService}.
   *
   * @param traderNpcRegistry the registry used to store and manage NPCs
   * @param traderNpcRepository the repository used to load NPC data
   * @param bankerNpc the specialized banker NPC instance
   */
  public TraderNpcService(TraderNpcRegistry traderNpcRegistry,
      TraderNpcRepository traderNpcRepository, BankerNpc bankerNpc) {
    this.traderNpcRegistry = traderNpcRegistry;
    this.traderNpcRepository = traderNpcRepository;
    this.bankerNpc = bankerNpc;
  }

  /**
   * Loads all trader NPC entries from the repository and initializes the registry.
   */
  public void loadAndInitialiseNpcs() {
    traderNpcRegistry.init(traderNpcRepository.loadAll());
  }

  /**
   * Returns all registered trader NPCs.
   *
   * @return list of all {@link TraderNpc} instances
   */
  public List<TraderNpc> getAllNpcs() {
    return traderNpcRegistry.getNpcs();
  }

  /**
   * Returns the trader NPC at the given registry index.
   *
   * @param index zero-based registry index
   * @return the {@link TraderNpc} at the specified index
   */
  public TraderNpc getNpc(int index) {
    return traderNpcRegistry.getNpc(index);
  }

  /**
   * Returns the spawn-egg {@link ItemStack} for each registered NPC.
   *
   * @return list of spawn-egg item stacks
   */
  public List<ItemStack> getNpcSpawnEggs() {
    return traderNpcRegistry.getNpcItemStackList();
  }

  /**
   * Returns the display names of all registered NPCs.
   *
   * @return list of NPC names
   */
  public List<String> getNpcNames() {
    return traderNpcRegistry.getNpcNameList();
  }

  /**
   * Returns the GUI titles of all trader-type NPCs
   * (traders, enchanters, beekeepers).
   *
   * @return list of trader NPC GUI titles
   */
  public List<String> getTraderNpcTitles() {
    return traderNpcRegistry.getNpcTraderTitleList();
  }
}