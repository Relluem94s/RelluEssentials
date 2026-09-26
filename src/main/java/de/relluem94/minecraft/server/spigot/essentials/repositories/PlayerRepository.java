package de.relluem94.minecraft.server.spigot.essentials.repositories;

import de.relluem94.minecraft.server.spigot.essentials.models.pojo.PlayerEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.PlayerPartnerEntry;
import de.relluem94.minecraft.server.spigot.essentials.persistence.dao.PlayerDao;
import java.util.List;
import org.jetbrains.annotations.NotNull;

/**
 * Repository for managing player and player partner data.
 * Acts as an abstraction layer over {@link PlayerDao} to provide
 * a clean API for player persistence operations.
 *
 * @author rellu
 */
public class PlayerRepository {

  private final PlayerDao playerDao;

  /**
   * Creates a new PlayerRepository with the given data access object.
   *
   * @param playerDao the DAO used to perform player persistence operations
   */
  public PlayerRepository(PlayerDao playerDao) {
    this.playerDao = playerDao;
  }

  /**
   * Retrieves all player entries from the database.
   *
   * @return a list of all {@link PlayerEntry} records
   */
  public List<PlayerEntry> findAll() {
    return playerDao.findAll();
  }

  /**
   * Retrieves a player entry by its unique identifier.
   *
   * @param uuid the UUID of the player to look up
   * @return the matching {@link PlayerEntry}, or {@code null} if not found
   */
  public PlayerEntry findByUuid(@NotNull String uuid) {
    return playerDao.findByUuid(uuid);
  }

  /**
   * Persists a new player entry to the database.
   *
   * @param playerEntry the player entry to insert
   */
  public void save(@NotNull PlayerEntry playerEntry) {
    playerDao.insert(playerEntry);
  }

  /**
   * Updates an existing player entry in the database.
   *
   * @param playerEntry the player entry containing updated values
   */
  public void update(@NotNull PlayerEntry playerEntry) {
    playerDao.update(playerEntry);
  }

  /**
   * Retrieves the partner entry associated with the given player ID.
   *
   * @param playerId the ID of the player whose partner entry is requested
   * @return the matching {@link PlayerPartnerEntry}, or {@code null} if not found
   */
  public PlayerPartnerEntry findPartnerByPlayerId(int playerId) {
    return playerDao.findPartnerByPlayerId(playerId);
  }

  /**
   * Persists a new player partner entry to the database.
   *
   * @param partnerEntry the partner entry to insert
   */
  public void savePartner(@NotNull PlayerPartnerEntry partnerEntry) {
    playerDao.insertPartner(partnerEntry);
  }

  /**
   * Removes an existing player partner entry from the database.
   *
   * @param partnerEntry the partner entry to delete
   */
  public void deletePartner(@NotNull PlayerPartnerEntry partnerEntry) {
    playerDao.deletePartner(partnerEntry);
  }

  /**
   * Updates an existing player partner entry in the database.
   *
   * @param partnerEntry the partner entry containing updated values
   */
  public void updatePartner(@NotNull PlayerPartnerEntry partnerEntry) {
    playerDao.updatePartner(partnerEntry);
  }
}