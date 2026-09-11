package de.relluem94.minecraft.server.spigot.essentials.persistence.dao;

import static de.relluem94.minecraft.server.spigot.essentials.constants.InventoryConstants.BAG_SIZE;

import de.relluem94.minecraft.server.spigot.essentials.constants.db.DatabaseMappings;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.BagEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.BagTypeEntry;
import de.relluem94.minecraft.server.spigot.essentials.persistence.dao.mapper.BagMapper;
import de.relluem94.minecraft.server.spigot.essentials.persistence.jdbc.QueryExecutor;
import java.util.List;
import java.util.Optional;

/**
 * Data Access Object for managing bag and bag type persistence operations.
 *
 * @author rellu
 */
public class BagDao {

  private final QueryExecutor queryExecutor;

  /**
   * Creates a new BagDao with the given query executor.
   *
   * @param queryExecutor the executor used to run SQL queries and updates
   */
  public BagDao(QueryExecutor queryExecutor) {
    this.queryExecutor = queryExecutor;
  }

  /**
   * Retrieves all available bag types from the database.
   *
   * @return a list of all {@link BagTypeEntry} records
   */
  public List<BagTypeEntry> findAllBagTypes() {
    return queryExecutor.queryList("getBagTypes.sql", _ -> {
    }, BagMapper::mapBagType);
  }

  /**
   * Retrieves all available bag types from the database.
   *
   * @return a list of all {@link BagTypeEntry} records
   */
  public Optional<BagTypeEntry> findBagTypeById(int bagTypeId) {
    return Optional.of(
        queryExecutor.querySingle("getBagTypeById.sql",
            ps -> ps.setInt(1, bagTypeId),
            BagMapper::mapBagType
        )
    );
  }

  /**
   * Retrieves all bags from the database, each populated with its associated bag type.
   *
   * @return a list of all {@link BagEntry} records
   */
  public List<BagEntry> findAllBags() {
    return queryExecutor.queryList("getBags.sql", _ -> {
    }, rs -> {
      BagEntry bagEntry = BagMapper.mapBag(rs);
      bagEntry.setBagType(findBagTypeById(rs.getInt(DatabaseMappings.FIELD_BAG_TYPE_FK))
          .orElseThrow());
      return bagEntry;
    });
  }

  /**
   * Retrieves a specific bag belonging to a player, identified by player ID and bag type ID.
   *
   * @param playerId  the unique identifier of the player
   * @param bagTypeId the unique identifier of the bag type
   * @return an {@link Optional} containing the matching {@link BagEntry}, or empty if none exists
   */
  public Optional<BagEntry> findBagByPlayerIdAndBagTypeId(int playerId, int bagTypeId) {
    return Optional.ofNullable(
        queryExecutor.querySingle("getBagByPlayerAndType.sql", ps -> {
          ps.setInt(1, bagTypeId);
          ps.setInt(2, playerId);
        }, rs -> {
          BagEntry bagEntry = BagMapper.mapBag(rs);
          bagEntry.setBagType(findBagTypeById(rs.getInt(DatabaseMappings.FIELD_BAG_TYPE_FK))
              .orElseThrow());
          return bagEntry;
        })
    );
  }

  /**
   * Inserts a new bag record for the given player and bag type.
   *
   * @param playerId  the unique identifier of the player
   * @param bagTypeId the unique identifier of the bag type
   */
  public void insertBag(int playerId, int bagTypeId) {
    queryExecutor.executeUpdate("insertBag.sql", ps -> {
      ps.setInt(1, playerId);
      ps.setInt(2, playerId);
      ps.setInt(3, bagTypeId);
    });
  }

  /**
   * Updates an existing bag record with the current state of the given bag entry.
   *
   * @param bagEntry the {@link BagEntry} containing the updated data to persist
   */
  public void updateBag(BagEntry bagEntry) {
    queryExecutor.executeUpdate("updateBag.sql", ps -> {
      ps.setInt(1, bagEntry.getPlayerId());
      for (int slotIndex = 0; slotIndex < BAG_SIZE; slotIndex++) {
        ps.setInt(slotIndex + 2, bagEntry.getSlotValue(slotIndex));
      }
      ps.setInt(BAG_SIZE + 2, bagEntry.getId());
    });
  }
}