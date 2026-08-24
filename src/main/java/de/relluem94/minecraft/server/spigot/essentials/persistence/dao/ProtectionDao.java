package de.relluem94.minecraft.server.spigot.essentials.persistence.dao;

import de.relluem94.minecraft.server.spigot.essentials.models.pojo.ProtectionEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.ProtectionLockEntry;
import de.relluem94.minecraft.server.spigot.essentials.persistence.dao.mapper.ProtectionMapper;
import de.relluem94.minecraft.server.spigot.essentials.persistence.jdbc.QueryExecutor;
import java.util.List;
import java.util.Objects;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

public class ProtectionDao {

  private final QueryExecutor queryExecutor;

  public ProtectionDao(QueryExecutor queryExecutor) {
    this.queryExecutor = queryExecutor;
  }

  public int deleteOutdatedProtections() {
    return queryExecutor.executeUpdateWithCount("cleanupProtections.sql", _ -> {
    });
  }

  public List<Long> findOutdatedProtectionIds() {
    return queryExecutor.queryList("findOutdatedProtectionIds.sql", _ -> {
    }, rs -> rs.getLong("id"));
  }

  public List<ProtectionLockEntry> findAllLocks() {
    return queryExecutor.queryList("getProtectionLocks.sql", _ -> {
    }, ProtectionMapper::mapProtectionLock);
  }

  public void deleteById(int id, int playerId) {
    queryExecutor.executeUpdate("deleteProtection.sql", ps -> {
      ps.setInt(1, playerId);
      ps.setInt(2, id);
    });
  }

  public List<ProtectionEntry> findAll() {
    return queryExecutor.queryList("getProtections.sql", _ -> {
    }, ProtectionMapper::mapProtection);
  }

  public void insertProtection(@NotNull ProtectionEntry pe) {
    queryExecutor.executeUpdate("insertProtection.sql", ps -> {
      ps.setInt(1, pe.getCreatedBy());
      ps.setInt(2, pe.getLocationEntry().getId());
      ps.setString(3, pe.getMaterialName());
      ps.setString(4, pe.getFlags().toString());
      ps.setString(5, pe.getRights().toString());
    });
  }

  public void updateProtectionFlag(@NotNull ProtectionEntry pe) {
    queryExecutor.executeUpdate("updateProtectionFlags.sql", ps -> {
      ps.setInt(1, pe.getLocationEntry().getPlayerId());
      ps.setString(2, pe.getFlags().toString());
      ps.setInt(3, pe.getId());
    });
  }

  public void updateProtectionRight(@NotNull ProtectionEntry pe) {
    queryExecutor.executeUpdate("updateProtectionRights.sql", ps -> {
      ps.setInt(1, pe.getLocationEntry().getPlayerId());
      ps.setString(2, pe.getRights().toString());
      ps.setInt(3, pe.getId());
    });
  }

  public ProtectionEntry getProtectionByLocation(@NotNull Location l) {
    return queryExecutor.querySingle("getProtectionByLocation.sql", ps -> {
      ps.setFloat(1, (float) l.getX());
      ps.setFloat(2, (float) l.getY());
      ps.setFloat(3, (float) l.getZ());
      ps.setString(4, Objects.requireNonNull(l.getWorld()).getName());
    }, ProtectionMapper::mapProtection);
  }
}