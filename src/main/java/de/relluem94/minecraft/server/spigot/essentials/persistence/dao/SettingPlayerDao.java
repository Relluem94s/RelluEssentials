package de.relluem94.minecraft.server.spigot.essentials.persistence.dao;

import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.SettingPlayerEntry;
import de.relluem94.minecraft.server.spigot.essentials.persistence.dao.mapper.SettingPlayerMapper;
import de.relluem94.minecraft.server.spigot.essentials.persistence.jdbc.QueryExecutor;
import java.util.List;
import java.util.Optional;

/**
 * Data Access Object for managing {@link SettingPlayerEntry} persistence operations.
 *
 * <p>Provides methods to query, insert, update, and soft-delete player-specific settings
 * stored in the database.
 *
 * @author rellu
 */
public class SettingPlayerDao {

  private final QueryExecutor queryExecutor;
  private final ServiceContext serviceContext;

  /**
   * Constructs a new {@code SettingPlayerDao} with the required dependencies.
   *
   * @param queryExecutor  the executor used to run SQL queries and updates
   * @param serviceContext the context providing access to application services
   */
  public SettingPlayerDao(QueryExecutor queryExecutor, ServiceContext serviceContext) {
    this.queryExecutor = queryExecutor;
    this.serviceContext = serviceContext;
  }

  /**
   * Retrieves all {@link SettingPlayerEntry} records associated with the given player ID.
   *
   * @param playerId the ID of the player whose settings are to be retrieved
   * @return a list of {@link SettingPlayerEntry} records for the specified player,
   *         or an empty list if none exist
   */
  public List<SettingPlayerEntry> findAllByPlayerId(int playerId) {
    return queryExecutor.queryList("getAllSettingPlayersByPlayerId.sql",
        statement -> statement.setInt(1, playerId),
        (rs) -> SettingPlayerMapper.mapSettingPlayer(rs, serviceContext.getSettingService()));
  }

  /**
   * Retrieves a single {@link SettingPlayerEntry} by its unique ID.
   *
   * @param id the unique identifier of the player setting entry
   * @return an {@link Optional} containing the found {@link SettingPlayerEntry},
   *         or {@link Optional#empty()} if no entry exists for the given ID
   */
  public Optional<SettingPlayerEntry> findById(int id) {
    return Optional.ofNullable(queryExecutor.querySingle("getSettingPlayerById.sql",
        statement -> statement.setInt(1, id),
        (rs) -> SettingPlayerMapper.mapSettingPlayer(rs, serviceContext.getSettingService())));
  }

  /**
   * Inserts a new {@link SettingPlayerEntry} into the database.
   *
   * @param entry the player setting entry to be persisted
   */
  public void insert(SettingPlayerEntry entry) {
    queryExecutor.executeUpdate("insertSettingPlayer.sql", statement -> {
      statement.setInt(1, entry.getCreatedBy());
      statement.setInt(2, entry.getPlayerFk());
      statement.setInt(3, entry.getSettingFk());
      statement.setBoolean(4, entry.isValue());
    });
  }

  /**
   * Updates an existing {@link SettingPlayerEntry} in the database.
   *
   * @param entry the player setting entry containing updated values
   */
  public void update(SettingPlayerEntry entry) {
    queryExecutor.executeUpdate("updateSettingPlayer.sql", statement -> {
      statement.setInt(1, entry.getUpdatedBy());
      statement.setBoolean(2, entry.isValue());
      statement.setInt(3, entry.getId());
    });
  }

  /**
   * Soft-deletes a {@link SettingPlayerEntry} by marking it as deleted in the database
   * without physically removing the record.
   *
   * @param id        the unique identifier of the player setting entry to delete
   * @param deletedBy the ID of the player or actor performing the deletion
   */
  public void softDelete(int id, int deletedBy) {
    queryExecutor.executeUpdate("deleteSettingPlayer.sql", statement -> {
      statement.setInt(1, deletedBy);
      statement.setInt(2, id);
    });
  }
}