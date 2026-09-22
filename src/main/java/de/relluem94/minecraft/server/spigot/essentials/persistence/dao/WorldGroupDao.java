package de.relluem94.minecraft.server.spigot.essentials.persistence.dao;

import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.PlayerEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.WorldEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.WorldGroupEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.WorldGroupInventoryEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.WorldGroupSettingEntry;
import de.relluem94.minecraft.server.spigot.essentials.persistence.dao.mapper.WorldGroupSettingMapper;
import de.relluem94.minecraft.server.spigot.essentials.persistence.dao.mapper.WorldMapper;
import de.relluem94.minecraft.server.spigot.essentials.persistence.jdbc.QueryExecutor;
import java.util.List;
import org.jetbrains.annotations.NotNull;

/**
 * Data Access Object for managing world groups, their associated worlds,
 * settings, and player inventories within the persistence layer.
 */
public class WorldGroupDao {

  private final QueryExecutor queryExecutor;
  private final ServiceContext serviceContext;

  /**
   * Creates a new {@code WorldGroupDao} with the required dependencies for
   * query execution and service access.
   *
   * @param queryExecutor  the executor used to run SQL queries against the database
   * @param serviceContext the context providing access to application-level services
   */
  public WorldGroupDao(QueryExecutor queryExecutor, ServiceContext serviceContext) {
    this.queryExecutor = queryExecutor;
    this.serviceContext = serviceContext;
  }

  /**
   * Retrieves all world group settings from the database.
   *
   * @return a list of {@link WorldGroupSettingEntry} representing all stored world group settings
   */
  public List<WorldGroupSettingEntry> findAllWorldGroupSettings() {
    return queryExecutor.queryList(
        "getAllWorldGroupSettings.sql",
        _ -> {},
        rs -> WorldGroupSettingMapper.mapWorldGroupSetting(rs, serviceContext.getSettingService())
    );
  }

  /**
   * Retrieves all world groups from the database, each enriched with their associated settings.
   *
   * @return a list of {@link WorldGroupEntry} representing all stored world groups
   */
  public List<WorldGroupEntry> findAllWorldGroups() {
    List<WorldGroupSettingEntry> allSettings = findAllWorldGroupSettings();
    return queryExecutor.queryList(
        "getWorldGroups.sql",
        _ -> {},
        rs -> WorldMapper.mapWorldGroup(rs, allSettings)
    );
  }

  /**
   * Retrieves all worlds belonging to the given world group.
   *
   * @param worldGroupEntry the world group whose worlds are to be retrieved
   * @return a list of {@link WorldEntry} associated with the given world group
   */
  public List<WorldEntry> findWorldsByGroup(@NotNull WorldGroupEntry worldGroupEntry) {
    return queryExecutor.queryList(
        "getWorldByGroup.sql",
        ps -> ps.setInt(1, worldGroupEntry.getId()),
        rs -> {
          WorldEntry worldEntry = WorldMapper.mapWorld(rs);
          worldEntry.setWorldGroupEntry(worldGroupEntry);
          return worldEntry;
        }
    );
  }

  /**
   * Retrieves the inventory entry for a specific player within a specific world group.
   *
   * @param playerEntry     the player whose inventory is to be retrieved
   * @param worldGroupEntry the world group in which the inventory is stored
   * @return the {@link WorldGroupInventoryEntry} for the given player and world group,
   *         or {@code null} if no entry exists
   */
  public WorldGroupInventoryEntry findInventoryByGroupAndPlayer(
      @NotNull PlayerEntry playerEntry, @NotNull WorldGroupEntry worldGroupEntry) {
    return queryExecutor.querySingle(
        "getWorldInventoryByGroupAndPlayer.sql",
        ps -> {
          ps.setInt(1, worldGroupEntry.getId());
          ps.setInt(2, playerEntry.getId());
        },
        rs -> {
          WorldGroupInventoryEntry inventoryEntry = WorldMapper.mapWorldGroupInventory(rs);
          inventoryEntry.setWorldGroupEntry(worldGroupEntry);
          return inventoryEntry;
        }
    );
  }

  /**
   * Persists a new inventory entry for a player within a world group.
   *
   * @param inventoryEntry the inventory entry to insert, containing player, world group,
   *                       inventory contents, health, food level, and experience data
   */
  public void insertInventory(@NotNull WorldGroupInventoryEntry inventoryEntry) {
    queryExecutor.executeUpdate(
        "insertWorldInventoryByGroupAndPlayer.sql",
        ps -> {
          ps.setInt(1, inventoryEntry.getPlayerId());
          ps.setInt(2, inventoryEntry.getPlayerId());
          ps.setInt(3, inventoryEntry.getWorldGroupEntry().getId());
          ps.setString(4, inventoryEntry.getInventory().toString());
          ps.setDouble(5, inventoryEntry.getHealth());
          ps.setInt(6, inventoryEntry.getFoodLevel());
          ps.setInt(7, inventoryEntry.getTotalExperience());
        }
    );
  }

  /**
   * Updates an existing inventory entry for a player within a world group.
   *
   * @param inventoryEntry the inventory entry containing the updated inventory contents,
   *                       health, food level, experience, and the identifier of the updating player
   */
  public void updateInventory(@NotNull WorldGroupInventoryEntry inventoryEntry) {
    queryExecutor.executeUpdate(
        "updateWorldInventoryByGroupAndPlayer.sql",
        ps -> {
          ps.setInt(1, inventoryEntry.getUpdatedBy());
          ps.setString(2, inventoryEntry.getInventory().toString());
          ps.setDouble(3, inventoryEntry.getHealth());
          ps.setInt(4, inventoryEntry.getFoodLevel());
          ps.setInt(5, inventoryEntry.getTotalExperience());
          ps.setInt(6, inventoryEntry.getPlayerId());
          ps.setInt(7, inventoryEntry.getWorldGroupEntry().getId());
        }
    );
  }

  /**
   * Persists a new world group to the database.
   *
   * @param worldGroupEntry the world group entry to insert, containing the creator and group name
   */
  public void insertWorldGroup(@NotNull WorldGroupEntry worldGroupEntry) {
    queryExecutor.executeUpdate(
        "insertWorldGroup.sql",
        ps -> {
          ps.setInt(1, worldGroupEntry.getCreatedBy());
          ps.setString(2, worldGroupEntry.getName());
        }
    );
  }

  /**
   * Retrieves a world group by its name, enriched with all associated settings.
   *
   * @param name the name of the world group to look up
   * @return the matching {@link WorldGroupEntry}, or {@code null} if no world group
   *     with the given name exists
   */
  public WorldGroupEntry findWorldGroupByName(String name) {
    List<WorldGroupSettingEntry> allSettings = findAllWorldGroupSettings();
    return queryExecutor.querySingle(
        "getWorldGroupByName.sql",
        ps -> ps.setString(1, name),
        rs -> WorldMapper.mapWorldGroup(rs, allSettings)
    );
  }

  /**
   * Persists a new world entry to the database, associating it with a world
   * group and a permission group.
   *
   * @param worldEntry the world entry to insert, containing the creator, world name,
   *                   associated world group, and permission group
   */
  public void insertWorld(@NotNull WorldEntry worldEntry) {
    queryExecutor.executeUpdate(
        "insertWorld.sql",
        ps -> {
          ps.setInt(1, worldEntry.getCreatedBy());
          ps.setString(2, worldEntry.getName());
          ps.setInt(3, worldEntry.getWorldGroupEntry().getId());
          ps.setInt(4, worldEntry.getGroupEntry().getId());
        }
    );
  }
}