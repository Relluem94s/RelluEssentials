package de.relluem94.minecraft.server.spigot.essentials.repositories;

import de.relluem94.minecraft.server.spigot.essentials.models.pojo.TraderNpcEntry;
import de.relluem94.minecraft.server.spigot.essentials.persistence.dao.TraderNpcDao;
import java.util.List;

/**
 * Repository for trader NPC data access.
 *
 * <p>Acts as an abstraction layer between the service layer and
 * the {@link TraderNpcDao}, providing a domain-oriented API
 * for retrieving trader NPC configurations.</p>
 */
public class TraderNpcRepository {

  private final TraderNpcDao traderNpcDao;

  public TraderNpcRepository(TraderNpcDao traderNpcDao) {
    this.traderNpcDao = traderNpcDao;
  }

  /**
   * Loads all trader NPC entries from the data store.
   *
   * @return a list of all available {@link TraderNpcEntry} records
   */
  public List<TraderNpcEntry> loadAll() {
    return traderNpcDao.findAll();
  }
}