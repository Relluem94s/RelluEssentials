package de.relluem94.minecraft.server.spigot.essentials.persistence.dao;

import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.enums.LocationType;
import de.relluem94.minecraft.server.spigot.essentials.enums.PlayerState;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.PlayerEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.PlayerPartnerEntry;
import de.relluem94.minecraft.server.spigot.essentials.persistence.dao.mapper.PlayerMapper;
import de.relluem94.minecraft.server.spigot.essentials.persistence.jdbc.QueryExecutor;
import java.util.List;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;

/**
 * Data Access Object for managing player-related database operations. Handles CRUD operations for
 * players and their partner relationships.
 */
@AllArgsConstructor
public class PlayerDao {

  private final QueryExecutor queryExecutor;
  private final ServiceContext serviceContext;

  /**
   * Retrieves all players from the database, including their homes, deaths, partner and default
   * state.
   *
   * @return a list of all {@link PlayerEntry} objects, or an empty list if none exist
   */
  public List<PlayerEntry> findAll() {
    return queryExecutor.queryList("getPlayers.sql", _ -> {
    }, rs -> {
      PlayerEntry playerEntry = PlayerMapper.mapPlayer(rs, serviceContext.getGroupService());
      playerEntry.setHomes(serviceContext.getLocationService()
          .findByPlayerAndType(playerEntry.getId(), LocationType.HOME));
      playerEntry.setDeaths(serviceContext.getLocationService()
          .findByPlayerAndType(playerEntry.getId(), LocationType.DEATH));
      playerEntry.setPartner(findPartnerByPlayerId(playerEntry.getId()));
      playerEntry.setPlayerState(PlayerState.DEFAULT);
      return playerEntry;
    });
  }

  /**
   * Retrieves a single player by their UUID, including their homes, deaths, partner and default
   * state.
   *
   * @param uuid the UUID of the player to retrieve
   * @return the matching {@link PlayerEntry}, or {@code null} if no player with the given UUID
   *     exists
   */
  public PlayerEntry findByUuid(@NotNull String uuid) {
    return queryExecutor.querySingle("getPlayer.sql", ps -> ps.setString(1, uuid), rs -> {
      PlayerEntry playerEntry = PlayerMapper.mapPlayer(rs, serviceContext.getGroupService());
      playerEntry.setHomes(serviceContext.getLocationService()
          .findByPlayerAndType(playerEntry.getId(), LocationType.HOME));
      playerEntry.setDeaths(serviceContext.getLocationService()
          .findByPlayerAndType(playerEntry.getId(), LocationType.DEATH));
      playerEntry.setPartner(findPartnerByPlayerId(playerEntry.getId()));
      playerEntry.setPlayerState(PlayerState.DEFAULT);
      return playerEntry;
    });
  }

  /**
   * Inserts a new player record into the database.
   *
   * @param playerEntry the {@link PlayerEntry} containing the data to persist
   */
  public void insert(@NotNull PlayerEntry playerEntry) {
    queryExecutor.executeUpdate("insertPlayer.sql", ps -> {
      ps.setInt(1, playerEntry.getCreatedBy());
      ps.setString(2, playerEntry.getUuid());
      ps.setString(3, playerEntry.getName());
      ps.setString(4, playerEntry.getCustomName());
      ps.setInt(5, playerEntry.getGroup().getId());
    });
  }

  /**
   * Updates an existing player record in the database.
   *
   * @param playerEntry the {@link PlayerEntry} containing the updated data
   */
  public void update(@NotNull PlayerEntry playerEntry) {
    queryExecutor.executeUpdate("updatePlayer.sql", ps -> {
      ps.setInt(1, playerEntry.getId());
      ps.setInt(2, playerEntry.getGroup().getId());
      ps.setBoolean(3, playerEntry.isAfk());
      ps.setBoolean(4, playerEntry.isFlying());
      ps.setString(5, playerEntry.getName());
      ps.setString(6, playerEntry.getCustomName());
      ps.setDouble(7, playerEntry.getPurse());
      ps.setString(8, playerEntry.getUuid());
    });
  }

  /**
   * Retrieves the partner relationship entry for a given player ID.
   *
   * @param playerId the ID of the player whose partner relationship should be retrieved
   * @return the matching {@link PlayerPartnerEntry}, or {@code null} if no partner relationship
   *     exists
   */
  public PlayerPartnerEntry findPartnerByPlayerId(int playerId) {
    return queryExecutor.querySingle("getPlayerPartner.sql", ps -> {
      ps.setInt(1, playerId);
      ps.setInt(2, playerId);
    }, PlayerMapper::mapPlayerPartner);
  }

  /**
   * Inserts a new partner relationship record into the database.
   *
   * @param partnerEntry the {@link PlayerPartnerEntry} containing the partner relationship data to
   *                     persist
   */
  public void insertPartner(@NotNull PlayerPartnerEntry partnerEntry) {
    queryExecutor.executeUpdate("insertPlayerPartner.sql", ps -> {
      ps.setInt(1, partnerEntry.getCreatedBy());
      ps.setInt(2, partnerEntry.getFirstPartnerId());
      ps.setInt(3, partnerEntry.getSecondPartnerId());
      ps.setBoolean(4, partnerEntry.isShareProtections());
    });
  }

  /**
   * Soft-deletes an existing partner relationship record in the database.
   *
   * @param partnerEntry the {@link PlayerPartnerEntry} identifying the relationship to delete
   */
  public void deletePartner(@NotNull PlayerPartnerEntry partnerEntry) {
    queryExecutor.executeUpdate("deletePlayerPartner.sql", ps -> {
      ps.setInt(1, partnerEntry.getDeletedBy());
      ps.setInt(2, partnerEntry.getId());
    });
  }

  /**
   * Updates an existing partner relationship record in the database.
   *
   * @param partnerEntry the {@link PlayerPartnerEntry} containing the updated partner relationship
   *                     data
   */
  public void updatePartner(@NotNull PlayerPartnerEntry partnerEntry) {
    queryExecutor.executeUpdate("updatePlayerPartner.sql", ps -> {
      ps.setInt(1, partnerEntry.getUpdatedBy());
      ps.setBoolean(2, partnerEntry.isShareProtections());
      ps.setInt(3, partnerEntry.getId());
    });
  }
}