package de.relluem94.minecraft.server.spigot.essentials.registries;

import de.relluem94.minecraft.server.spigot.essentials.models.pojo.BagEntry;
import de.relluem94.minecraft.server.spigot.essentials.repositories.BagRepository;
import java.util.Collection;
import java.util.Optional;

public class BagRegistry {

  private final BagRepository bagRepository;

  public BagRegistry(BagRepository bagRepository) {
    this.bagRepository = bagRepository;
  }

  public void register(BagEntry bagEntry) {
    if (bagRepository.existsByPlayerIdAndBagTypeId(bagEntry.getPlayerId(), bagEntry.getBagType().getId())) {
      throw new IllegalArgumentException("BagEntry is already registered: " + bagEntry);
    }
    bagRepository.save(bagEntry);
  }

  public void unregister(BagEntry bagEntry) {
    if (!bagRepository.existsByPlayerIdAndBagTypeId(bagEntry.getPlayerId(), bagEntry.getBagType().getId())) {
      throw new IllegalArgumentException("BagEntry is not registered: " + bagEntry);
    }
    bagRepository.delete(bagEntry);
  }

  public Optional<BagEntry> findByPlayerIdAndBagTypeId(int playerId, int bagTypeId) {
    return bagRepository.findByPlayerIdAndBagTypeId(playerId, bagTypeId);
  }

  public Collection<BagEntry> findAllByPlayerId(int playerId) {
    return bagRepository.findAllByPlayerId(playerId);
  }

  public boolean existsByPlayerIdAndBagTypeId(int playerId, int bagTypeId) {
    return bagRepository.existsByPlayerIdAndBagTypeId(playerId, bagTypeId);
  }

  public boolean existsByPlayerId(int playerId) {
    return bagRepository.existsByPlayerId(playerId);
  }

  public Collection<BagEntry> findAll() {
    return bagRepository.findAll();
  }
}