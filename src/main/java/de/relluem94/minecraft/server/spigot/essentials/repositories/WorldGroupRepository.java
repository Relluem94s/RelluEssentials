package de.relluem94.minecraft.server.spigot.essentials.repositories;

import de.relluem94.minecraft.server.spigot.essentials.models.pojo.PlayerEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.WorldEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.WorldGroupEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.WorldGroupInventoryEntry;
import de.relluem94.minecraft.server.spigot.essentials.persistence.dao.WorldGroupDao;
import java.util.List;

public class WorldGroupRepository {

  private final WorldGroupDao worldGroupDao;

  public WorldGroupRepository(WorldGroupDao worldGroupDao) {
    this.worldGroupDao = worldGroupDao;
  }

  public List<WorldGroupEntry> findAllWorldGroups() {
    return worldGroupDao.findAllWorldGroups();
  }

  public List<WorldEntry> findWorldsByGroup(WorldGroupEntry worldGroupEntry) {
    return worldGroupDao.findWorldsByGroup(worldGroupEntry);
  }

  public WorldGroupInventoryEntry findInventoryByGroupAndPlayer(PlayerEntry playerEntry,
      WorldGroupEntry worldGroupEntry) {
    return worldGroupDao.findInventoryByGroupAndPlayer(playerEntry, worldGroupEntry);
  }

  public void saveInventory(WorldGroupInventoryEntry inventoryEntry) {
    worldGroupDao.insertInventory(inventoryEntry);
  }

  public void updateInventory(WorldGroupInventoryEntry inventoryEntry) {
    worldGroupDao.updateInventory(inventoryEntry);
  }

  public void saveWorldGroup(WorldGroupEntry worldGroupEntry) {
    worldGroupDao.insertWorldGroup(worldGroupEntry);
  }

  public WorldGroupEntry findWorldGroupByName(String name) {
    return worldGroupDao.findWorldGroupByName(name);
  }

  public void saveWorld(WorldEntry worldEntry) {
    worldGroupDao.insertWorld(worldEntry);
  }
}