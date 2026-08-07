package de.relluem94.minecraft.server.spigot.essentials.repositories;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.BagEntry;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class BagRepository {

  private final Multimap<Integer, BagEntry> bagsByPlayerId = ArrayListMultimap.create();

  public BagRepository(Collection<BagEntry> initialBagEntries) {
    for (BagEntry bagEntry : initialBagEntries) {
      bagsByPlayerId.put(bagEntry.getPlayerId(), bagEntry);
    }
  }

  public Collection<BagEntry> findAllByPlayerId(int playerId) {
    return List.copyOf(bagsByPlayerId.get(playerId));
  }

  public Optional<BagEntry> findByPlayerIdAndBagTypeId(int playerId, int bagTypeId) {
    return bagsByPlayerId.get(playerId).stream()
        .filter(entry -> entry.getBagType().getId() == bagTypeId)
        .findFirst();
  }

  public boolean existsByPlayerIdAndBagTypeId(int playerId, int bagTypeId) {
    return findByPlayerIdAndBagTypeId(playerId, bagTypeId).isPresent();
  }

  public boolean existsByPlayerId(int playerId) {
    return bagsByPlayerId.containsKey(playerId);
  }

  public Collection<BagEntry> findAll() {
    return List.copyOf(bagsByPlayerId.values());
  }

  public BagEntry save(BagEntry bagEntry) {
    bagsByPlayerId.put(bagEntry.getPlayerId(), bagEntry);
    return bagEntry;
  }

  public void delete(BagEntry bagEntry) {
    bagsByPlayerId.remove(bagEntry.getPlayerId(), bagEntry);
  }
}