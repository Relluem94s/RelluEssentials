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

@AllArgsConstructor
public class LocationDao {

  private final QueryExecutor queryExecutor;
  private final ServiceContext serviceContext;

  public int deleteOutdatedLocations() {
    return queryExecutor.executeUpdateWithCount("cleanupLocations.sql", _ -> {});
  }

  public LocationEntry getLocation(@NotNull Location l, int type) {
    return queryExecutor.querySingle("getLocationByLocation.sql", ps -> {
      ps.setFloat(1, (float) l.getX());
      ps.setFloat(2, (float) l.getY());
      ps.setFloat(3, (float) l.getZ());
      ps.setInt(4, type);
    }, rs -> LocationMapper.mapLocation(rs, serviceContext.getLocationTypeService()));
  }

  public LocationEntry findById(int id) {
    return queryExecutor.querySingle("getLocationById.sql", ps -> ps.setInt(1, id),
        rs -> LocationMapper.mapLocation(rs, serviceContext.getLocationTypeService()));
  }

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

  public void deleteLocation(@NotNull LocationEntry le) {
   deleteById(le.getId(), le.getPlayerId());
  }

  public void deleteById(int id, int playerId) {
    queryExecutor.executeUpdate("deleteLocation.sql", ps -> {
      ps.setInt(1, playerId);
      ps.setInt(2, id);
    });
  }

  public List<LocationEntry> getLocations(int id, int type) {
    return queryExecutor.queryList("getLocationsByPlayer.sql", ps -> ps.setInt(1, id), rs -> {
      if (type != rs.getInt(DatabaseMappings.FIELD_LOCATION_TYPE_FK)) {
        return null;
      }
      return LocationMapper.mapLocation(rs, serviceContext.getLocationTypeService());
    }).stream().filter(Objects::nonNull)
        .collect(java.util.stream.Collectors.toCollection(ArrayList::new));
  }

  public List<LocationEntry> getLocationsByType(int type) {
    return queryExecutor.queryList("getLocationsByType.sql", _ -> {}, rs -> {
          if (type != rs.getInt(DatabaseMappings.FIELD_LOCATION_TYPE_FK)) {
            return null;
          }
          return LocationMapper.mapLocation(rs, serviceContext.getLocationTypeService());
        }).stream().filter(Objects::nonNull)
        .collect(java.util.stream.Collectors.toCollection(ArrayList::new));
  }



}