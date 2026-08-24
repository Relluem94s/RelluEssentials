package de.relluem94.minecraft.server.spigot.essentials.repositories;

import de.relluem94.minecraft.server.spigot.essentials.models.pojo.BagTypeEntry;
import de.relluem94.minecraft.server.spigot.essentials.persistence.dao.BagDao;
import java.util.List;
import java.util.Optional;

/**
 * Repository for persisting and retrieving {@link BagTypeEntry} data via {@link BagDao}.
 */
public class BagTypeRepository {

  private final BagDao bagDao;

  /**
   * Creates a new {@link BagTypeRepository} backed by the given {@link BagDao}.
   *
   * @param bagDao the DAO used to access bag type data
   */
  public BagTypeRepository(BagDao bagDao) {
    this.bagDao = bagDao;
  }

  /**
   * Retrieves all {@link BagTypeEntry} instances from the database.
   *
   * @return a {@link List} of all {@link BagTypeEntry} instances
   */
  public List<BagTypeEntry> findAll() {
    return bagDao.findAllBagTypes();
  }

  /**
   * Finds a {@link BagTypeEntry} by its unique identifier.
   *
   * @param id the unique identifier to search for
   * @return an {@link Optional} containing the found {@link BagTypeEntry}, or empty if not found
   */
  public Optional<BagTypeEntry> findById(int id) {
    return bagDao.findBagTypeById(id);
  }
}