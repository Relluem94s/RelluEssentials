package de.relluem94.minecraft.server.spigot.essentials.repositories;

import de.relluem94.minecraft.server.spigot.essentials.models.pojo.PlayerEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.WorldEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.WorldGroupEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.WorldGroupInventoryEntry;
import de.relluem94.minecraft.server.spigot.essentials.persistence.dao.WorldGroupDao;
import java.util.List;

/**
 * Repository for managing world groups, their associated worlds, and player inventories.
 * Acts as an abstraction layer between the application logic and the underlying data access object.
 *
 * @author rellu
 */
public class WorldGroupRepository {

  private final WorldGroupDao worldGroupDao;

  /**
   * Creates a new WorldGroupRepository with the given data access object.
   *
   * @param worldGroupDao the DAO used to perform database operations for world groups
   */
  public WorldGroupRepository(WorldGroupDao worldGroupDao) {
    this.worldGroupDao = worldGroupDao;
  }

  /**
   * Retrieves all existing world groups.
   *
   * @return a list of all {@link WorldGroupEntry} instances stored in the database
   */
  public List<WorldGroupEntry> findAllWorldGroups() {
    return worldGroupDao.findAllWorldGroups();
  }

  /**
   * Retrieves all worlds that belong to the given world group.
   *
   * @param worldGroupEntry the world group whose associated worlds are to be retrieved
   * @return a list of {@link WorldEntry} instances belonging to the specified world group
   */
  public List<WorldEntry> findWorldsByGroup(WorldGroupEntry worldGroupEntry) {
    return worldGroupDao.findWorldsByGroup(worldGroupEntry);
  }

  /**
   * Retrieves the inventory of a specific player within a specific world group.
   *
   * @param playerEntry     the player whose inventory is to be retrieved
   * @param worldGroupEntry the world group in which the inventory is stored
   * @return the {@link WorldGroupInventoryEntry} for the given player and world group,
   *         or {@code null} if no inventory exists
   */
  public WorldGroupInventoryEntry findInventoryByGroupAndPlayer(PlayerEntry playerEntry,
      WorldGroupEntry worldGroupEntry) {
    return worldGroupDao.findInventoryByGroupAndPlayer(playerEntry, worldGroupEntry);
  }

  /**
   * Persists a new inventory entry for a player in a world group.
   *
   * @param inventoryEntry the inventory entry to be inserted into the database
   */
  public void saveInventory(WorldGroupInventoryEntry inventoryEntry) {
    worldGroupDao.insertInventory(inventoryEntry);
  }

  /**
   * Updates an existing inventory entry for a player in a world group.
   *
   * @param inventoryEntry the inventory entry containing the updated data to be saved
   */
  public void updateInventory(WorldGroupInventoryEntry inventoryEntry) {
    worldGroupDao.updateInventory(inventoryEntry);
  }

  /**
   * Persists a new world group entry.
   *
   * @param worldGroupEntry the world group entry to be inserted into the database
   */
  public void saveWorldGroup(WorldGroupEntry worldGroupEntry) {
    worldGroupDao.insertWorldGroup(worldGroupEntry);
  }

  /**
   * Retrieves a world group by its name.
   *
   * @param name the name of the world group to search for
   * @return the matching {@link WorldGroupEntry}, or {@code null} if no world group
   *     with the given name exists
   */
  public WorldGroupEntry findWorldGroupByName(String name) {
    return worldGroupDao.findWorldGroupByName(name);
  }

  /**
   * Persists a new world entry.
   *
   * @param worldEntry the world entry to be inserted into the database
   */
  public void saveWorld(WorldEntry worldEntry) {
    worldGroupDao.insertWorld(worldEntry);
  }
}