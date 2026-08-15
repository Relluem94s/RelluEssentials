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

public class WorldGroupDao {

  private final QueryExecutor queryExecutor;
  private final ServiceContext serviceContext;

  public WorldGroupDao(QueryExecutor queryExecutor, ServiceContext serviceContext) {
    this.queryExecutor = queryExecutor;
    this.serviceContext = serviceContext;
  }

  public List<WorldGroupSettingEntry> findAllWorldGroupSettings() {
    return queryExecutor.queryList(
        "getAllWorldGroupSettings.sql",
        _ -> {},
        rs -> WorldGroupSettingMapper.mapWorldGroupSetting(rs, serviceContext.getSettingService())
    );
  }

  public List<WorldGroupEntry> findAllWorldGroups() {
    List<WorldGroupSettingEntry> allSettings = findAllWorldGroupSettings();
    return queryExecutor.queryList(
        "getWorldGroups.sql",
        _ -> {},
        rs -> WorldMapper.mapWorldGroup(rs, allSettings)
    );
  }

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

  public void insertWorldGroup(@NotNull WorldGroupEntry worldGroupEntry) {
    queryExecutor.executeUpdate(
        "insertWorldGroup.sql",
        ps -> {
          ps.setInt(1, worldGroupEntry.getCreatedBy());
          ps.setString(2, worldGroupEntry.getName());
        }
    );
  }

  public WorldGroupEntry findWorldGroupByName(String name) {
    List<WorldGroupSettingEntry> allSettings = findAllWorldGroupSettings();
    return queryExecutor.querySingle(
        "getWorldGroupByName.sql",
        ps -> ps.setString(1, name),
        rs -> WorldMapper.mapWorldGroup(rs, allSettings)
    );
  }

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