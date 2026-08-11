package de.relluem94.minecraft.server.spigot.essentials.helpers.db.mapper;

import static de.relluem94.minecraft.server.spigot.essentials.constants.db.DatabaseMappings.FIELD_ID;
import static de.relluem94.minecraft.server.spigot.essentials.constants.db.DatabaseMappings.FIELD_LOCATION_NAME;
import static de.relluem94.minecraft.server.spigot.essentials.constants.db.DatabaseMappings.FIELD_LOCATION_TYPE;
import static de.relluem94.minecraft.server.spigot.essentials.constants.db.DatabaseMappings.FIELD_LOCATION_TYPE_FK;
import static de.relluem94.minecraft.server.spigot.essentials.constants.db.DatabaseMappings.FIELD_PITCH;
import static de.relluem94.minecraft.server.spigot.essentials.constants.db.DatabaseMappings.FIELD_PLAYER_FK;
import static de.relluem94.minecraft.server.spigot.essentials.constants.db.DatabaseMappings.FIELD_POS_X;
import static de.relluem94.minecraft.server.spigot.essentials.constants.db.DatabaseMappings.FIELD_POS_Y;
import static de.relluem94.minecraft.server.spigot.essentials.constants.db.DatabaseMappings.FIELD_POS_Z;
import static de.relluem94.minecraft.server.spigot.essentials.constants.db.DatabaseMappings.FIELD_WORLD;
import static de.relluem94.minecraft.server.spigot.essentials.constants.db.DatabaseMappings.FIELD_YAW;

import de.relluem94.minecraft.server.spigot.essentials.constants.Constants;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.LocationEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.LocationTypeEntry;
import de.relluem94.minecraft.server.spigot.essentials.services.LocationTypeService;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.jspecify.annotations.NonNull;

public class LocationMapper {

  private LocationMapper() {
    throw new IllegalStateException(Constants.PLUGIN_INTERNAL_UTILITY_CLASS);
  }

  public static @NonNull LocationEntry mapLocation(
      @NonNull ResultSet rs,
      @NonNull LocationTypeService locationTypeService
  ) throws SQLException {
    LocationEntry locationEntry = new LocationEntry();
    locationEntry.setId(rs.getInt(FIELD_ID));
    locationEntry.setPlayerId(rs.getInt(FIELD_PLAYER_FK));
    locationEntry.setLocationName(rs.getString(FIELD_LOCATION_NAME));

    locationEntry.setWorld(rs.getString(FIELD_WORLD));
    locationEntry.setX(rs.getFloat(FIELD_POS_X));
    locationEntry.setY(rs.getFloat(FIELD_POS_Y));
    locationEntry.setZ(rs.getFloat(FIELD_POS_Z));
    locationEntry.setPitch(rs.getFloat(FIELD_PITCH));
    locationEntry.setYaw(rs.getFloat(FIELD_YAW));

    locationTypeService.findById(rs.getInt(FIELD_LOCATION_TYPE_FK))
        .ifPresent(locationEntry::setLocationType);
    return locationEntry;
  }

  public static @NonNull LocationTypeEntry mapLocationType(@NonNull ResultSet rs)
      throws SQLException {
    LocationTypeEntry locationTypeEntry = new LocationTypeEntry();
    locationTypeEntry.setId(rs.getInt(FIELD_ID));
    locationTypeEntry.setType(rs.getString(FIELD_LOCATION_TYPE));
    return locationTypeEntry;
  }


}
