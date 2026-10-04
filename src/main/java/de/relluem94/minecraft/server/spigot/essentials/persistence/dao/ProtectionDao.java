package de.relluem94.minecraft.server.spigot.essentials.persistence.dao;

import de.relluem94.minecraft.server.spigot.essentials.models.pojo.ProtectionEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.ProtectionLockEntry;
import de.relluem94.minecraft.server.spigot.essentials.persistence.dao.mapper.ProtectionMapper;
import de.relluem94.minecraft.server.spigot.essentials.persistence.jdbc.QueryExecutor;
import java.util.List;
import java.util.Objects;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

/**
 * Data Access Object for managing protection entries and their associated locks in the database.
 *
 * @author Rellu
 */
public class ProtectionDao {

  private final QueryExecutor queryExecutor;

  /**
   * Creates a new {@code ProtectionDao} with the given {@link QueryExecutor}.
   *
   * @param queryExecutor the executor used to run SQL queries and updates
   */
  public ProtectionDao(QueryExecutor queryExecutor) {
    this.queryExecutor = queryExecutor;
  }

  /**
   * Deletes all outdated protections from the database.
   *
   * @return the number of rows deleted
   */
  public int deleteOutdatedProtections() {
    return queryExecutor.executeUpdateWithCount("cleanupProtections.sql", _ -> {
    });
  }

  /**
   * Retrieves the IDs of all outdated protections.
   *
   * @return a list of IDs belonging to outdated protections
   */
  public List<Long> findOutdatedProtectionIds() {
    return queryExecutor.queryList("findOutdatedProtectionIds.sql", _ -> {
    }, rs -> rs.getLong("id"));
  }

  /**
   * Retrieves all protection lock entries from the database.
   *
   * @return a list of all {@link ProtectionLockEntry} objects
   */
  public List<ProtectionLockEntry> findAllLocks() {
    return queryExecutor.queryList("getProtectionLocks.sql", _ -> {
    }, ProtectionMapper::mapProtectionLock);
  }

  /**
   * Deletes a protection entry identified by its ID and the owning player's ID.
   *
   * @param id       the ID of the protection to delete
   * @param playerId the ID of the player who owns the protection
   */
  public void deleteById(int id, int playerId) {
    queryExecutor.executeUpdate("deleteProtection.sql", ps -> {
      ps.setInt(1, playerId);
      ps.setInt(2, id);
    });
  }

  /**
   * Retrieves all protection entries from the database.
   *
   * @return a list of all {@link ProtectionEntry} objects
   */
  public List<ProtectionEntry> findAll() {
    return queryExecutor.queryList("getProtections.sql", _ -> {
    }, ProtectionMapper::mapProtection);
  }

  /**
   * Inserts a new protection entry into the database.
   *
   * @param pe the {@link ProtectionEntry} to insert
   */
  public void insertProtection(@NotNull ProtectionEntry pe) {
    queryExecutor.executeUpdate("insertProtection.sql", ps -> {
      ps.setInt(1, pe.getCreatedBy());
      ps.setInt(2, pe.getLocationEntry().getId());
      ps.setString(3, pe.getMaterialName());
      ps.setString(4, pe.getFlags().toString());
      ps.setString(5, pe.getRights().toString());
    });
  }

  /**
   * Updates the flags of an existing protection entry in the database.
   *
   * @param pe the {@link ProtectionEntry} containing the updated flags
   */
  public void updateProtectionFlag(@NotNull ProtectionEntry pe) {
    queryExecutor.executeUpdate("updateProtectionFlags.sql", ps -> {
      ps.setInt(1, pe.getLocationEntry().getPlayerId());
      ps.setString(2, pe.getFlags().toString());
      ps.setInt(3, pe.getId());
    });
  }

  /**
   * Updates the rights of an existing protection entry in the database.
   *
   * @param pe the {@link ProtectionEntry} containing the updated rights
   */
  public void updateProtectionRight(@NotNull ProtectionEntry pe) {
    queryExecutor.executeUpdate("updateProtectionRights.sql", ps -> {
      ps.setInt(1, pe.getLocationEntry().getPlayerId());
      ps.setString(2, pe.getRights().toString());
      ps.setInt(3, pe.getId());
    });
  }

  /**
   * Retrieves the protection entry associated with the given {@link Location}.
   *
   * @param l the location to look up
   * @return the {@link ProtectionEntry} at the given location, or {@code null} if none exists
   */
  public ProtectionEntry getProtectionByLocation(@NotNull Location l) {
    return queryExecutor.querySingle("getProtectionByLocation.sql", ps -> {
      ps.setFloat(1, (float) l.getX());
      ps.setFloat(2, (float) l.getY());
      ps.setFloat(3, (float) l.getZ());
      ps.setString(4, Objects.requireNonNull(l.getWorld()).getName());
    }, ProtectionMapper::mapProtection);
  }
}