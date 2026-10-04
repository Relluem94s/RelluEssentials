package de.relluem94.minecraft.server.spigot.essentials.persistence.dao;

import de.relluem94.minecraft.server.spigot.essentials.constants.db.DatabaseMappings;
import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.LocationEntry;
import de.relluem94.minecraft.server.spigot.essentials.persistence.dao.mapper.LocationMapper;
import de.relluem94.minecraft.server.spigot.essentials.persistence.jdbc.QueryExecutor;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.AllArgsConstructor;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

/**
 * Data Access Object for managing {@link LocationEntry} persistence operations.
 *
 * <p>Provides methods to insert, delete, and query locations from the database,
 * supporting filtering by player, location type, and coordinates.</p>
 *
 * @author Rellu
 */
@AllArgsConstructor
public class LocationDao {

  private final QueryExecutor queryExecutor;
  private final ServiceContext serviceContext;

  /**
   * Deletes all outdated locations from the database based on the cleanup query.
   *
   * @return the number of rows deleted
   */
  public int deleteOutdatedLocations() {
    return queryExecutor.executeUpdateWithCount("cleanupLocations.sql", _ -> {});
  }

  /**
   * Retrieves a {@link LocationEntry} matching the given {@link Location} and type.
   *
   * @param l    the Bukkit {@link Location} to search for
   * @param type the location type identifier to match
   * @return the matching {@link LocationEntry}, or {@code null} if none found
   */
  public LocationEntry getLocation(@NotNull Location l, int type) {
    return queryExecutor.querySingle("getLocationByLocation.sql", ps -> {
      ps.setFloat(1, (float) l.getX());
      ps.setFloat(2, (float) l.getY());
      ps.setFloat(3, (float) l.getZ());
      ps.setInt(4, type);
    }, rs -> LocationMapper.mapLocation(rs, serviceContext.getLocationTypeService()));
  }

  /**
   * Retrieves a {@link LocationEntry} by its unique database identifier.
   *
   * @param id the unique identifier of the location
   * @return the matching {@link LocationEntry}, or {@code null} if none found
   */
  public LocationEntry findById(int id) {
    return queryExecutor.querySingle("getLocationById.sql", ps -> ps.setInt(1, id),
        rs -> LocationMapper.mapLocation(rs, serviceContext.getLocationTypeService()));
  }

  /**
   * Inserts the given {@link LocationEntry} into the database.
   *
   * @param le the {@link LocationEntry} to persist
   */
  public void insertLocation(@NotNull LocationEntry le) {
    queryExecutor.executeUpdate("insertLocation.sql", ps -> {
      Location l = le.getLocation();
      ps.setInt(1, le.getPlayerId());
      ps.setFloat(2, (float) l.getX());
      ps.setFloat(3, (float) l.getY());
      ps.setFloat(4, (float) l.getZ());
      ps.setFloat(5, l.getYaw());
      ps.setFloat(6, l.getPitch());
      ps.setString(7, Objects.requireNonNull(l.getWorld()).getName());
      ps.setString(8, le.getLocationName());
      ps.setInt(9, le.getLocationType().getId());
      ps.setInt(10, le.getPlayerId());
    });
  }

  /**
   * Deletes the given {@link LocationEntry} from the database.
   *
   * @param le the {@link LocationEntry} to delete
   */
  public void deleteLocation(@NotNull LocationEntry le) {
    deleteById(le.getId(), le.getPlayerId());
  }

  /**
   * Deletes a location by its unique identifier and the owning player's identifier.
   *
   * @param id       the unique identifier of the location to delete
   * @param playerId the identifier of the player who owns the location
   */
  public void deleteById(int id, int playerId) {
    queryExecutor.executeUpdate("deleteLocation.sql", ps -> {
      ps.setInt(1, playerId);
      ps.setInt(2, id);
    });
  }

  /**
   * Retrieves all {@link LocationEntry} instances belonging to a specific player and type.
   *
   * @param id   the unique identifier of the player
   * @param type the location type identifier to filter by
   * @return a {@link List} of matching {@link LocationEntry} instances, never {@code null}
   */
  public List<LocationEntry> getLocations(int id, int type) {
    return queryExecutor.queryList("getLocationsByPlayer.sql", ps -> ps.setInt(1, id), rs -> {
      if (type != rs.getInt(DatabaseMappings.FIELD_LOCATION_TYPE_FK)) {
        return null;
      }
      return LocationMapper.mapLocation(rs, serviceContext.getLocationTypeService());
    }).stream().filter(Objects::nonNull)
        .collect(java.util.stream.Collectors.toCollection(ArrayList::new));
  }

  /**
   * Retrieves all {@link LocationEntry} instances matching the given location type.
   *
   * @param type the location type identifier to filter by
   * @return a {@link List} of matching {@link LocationEntry} instances, never {@code null}
   */
  public List<LocationEntry> getLocationsByType(int type) {
    return queryExecutor.queryList("getLocationsByType.sql", ps -> ps.setInt(1, type), rs -> {
      if (type != rs.getInt(DatabaseMappings.FIELD_LOCATION_TYPE_FK)) {
        return null;
      }
      return LocationMapper.mapLocation(rs, serviceContext.getLocationTypeService());
    }).stream().filter(Objects::nonNull)
        .collect(java.util.stream.Collectors.toCollection(ArrayList::new));
  }
}