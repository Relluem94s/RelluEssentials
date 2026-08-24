package de.relluem94.minecraft.server.spigot.essentials.repositories;

import de.relluem94.minecraft.server.spigot.essentials.models.pojo.BagEntry;
import de.relluem94.minecraft.server.spigot.essentials.persistence.dao.BagDao;
import java.util.List;
import java.util.Optional;

/**
 * Repository for persisting and retrieving {@link BagEntry} data via {@link BagDao}.
 */
public class BagRepository {

  private final BagDao bagDao;

  /**
   * Creates a new {@link BagRepository} backed by the given {@link BagDao}.
   *
   * @param bagDao the DAO used to access bag data
   */
  public BagRepository(BagDao bagDao) {
    this.bagDao = bagDao;
  }

  /**
   * Retrieves all {@link BagEntry} instances from the database.
   *
   * @return a {@link List} of all {@link BagEntry} instances
   */
  public List<BagEntry> findAll() {
    return bagDao.findAllBags();
  }

  /**
   * Finds a {@link BagEntry} by player id and bag type id.
   *
   * @param playerId  the player id to search for
   * @param bagTypeId the bag type id to search for
   * @return an {@link Optional} containing the matching entry, or empty if not found
   */
  public Optional<BagEntry> findByPlayerIdAndBagTypeId(int playerId, int bagTypeId) {
    return bagDao.findBagByPlayerIdAndBagTypeId(playerId, bagTypeId);
  }

  /**
   * Inserts a new {@link BagEntry} for the given player and bag type into the database.
   *
   * @param playerId  the player id to insert for
   * @param bagTypeId the bag type id to insert
   * @return the newly created {@link BagEntry}
   */
  public BagEntry insert(int playerId, int bagTypeId) {
    bagDao.insertBag(playerId, bagTypeId);
    return bagDao.findBagByPlayerIdAndBagTypeId(playerId, bagTypeId).orElseThrow();
  }

  /**
   * Persists the current state of the given {@link BagEntry} to the database.
   *
   * @param bagEntry the {@link BagEntry} to update
   */
  public void update(BagEntry bagEntry) {
    bagDao.updateBag(bagEntry);
  }
}