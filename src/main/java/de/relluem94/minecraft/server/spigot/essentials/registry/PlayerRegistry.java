package de.relluem94.minecraft.server.spigot.essentials.registry;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import de.relluem94.minecraft.server.spigot.essentials.model.pojo.BagEntry;
import de.relluem94.minecraft.server.spigot.essentials.model.pojo.PlayerEntry;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PlayerRegistry {

  @Getter
  private final Map<UUID, PlayerEntry> playerEntryMap = new HashMap<>();
  private final Multimap<Integer, BagEntry> playerBagEntryMap = ArrayListMultimap.create();

  public PlayerRegistry(List<BagEntry> bagEntries) {
    for (BagEntry b : bagEntries) {
      playerBagEntryMap.put(b.getPlayerId(), b);
    }
  }

  public void putPlayerEntry(UUID uuid, PlayerEntry playerEntry) {
    playerEntryMap.put(uuid, playerEntry);
  }

  public PlayerEntry getPlayerEntry(UUID uuid) {
    return playerEntryMap.get(uuid);
  }

  public PlayerEntry getPlayerEntry(int id) {
    for (PlayerEntry playerEntry : playerEntryMap.values()) {
      if (playerEntry.getId() == id) {
        return playerEntry;
      }
    }
    return null;
  }

  public PlayerEntry getPlayerEntry(Player player) {
    return playerEntryMap.get(player.getUniqueId());
  }

  public Collection<BagEntry> getPlayerBagList(int playerFK) {
    return playerBagEntryMap.get(playerFK);
  }

  public void putPlayerBagEntry(int playerFK, @NotNull BagEntry bagEntry) {
    playerBagEntryMap.put(playerFK, bagEntry);
  }

  public Multimap<Integer, BagEntry> getPlayerBagMap() {
    return playerBagEntryMap;
  }

  public void clearPlayerEntries() {
    playerEntryMap.clear();
  }
}