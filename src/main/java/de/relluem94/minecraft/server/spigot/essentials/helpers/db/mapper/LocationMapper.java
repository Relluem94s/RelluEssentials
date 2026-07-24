package de.relluem94.minecraft.server.spigot.essentials.helpers.db.mapper;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.constants.Constants;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.LocationEntry;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.LocationTypeEntry;
import org.jspecify.annotations.NonNull;

import java.sql.ResultSet;
import java.sql.SQLException;

import static de.relluem94.minecraft.server.spigot.essentials.constants.DatabaseMappings.*;

public class LocationMapper {
    private LocationMapper() {
        throw new IllegalStateException(Constants.PLUGIN_INTERNAL_UTILITY_CLASS);
    }

    public static @NonNull LocationEntry mapLocation(@NonNull ResultSet rs) throws SQLException {
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

        for (LocationTypeEntry lte : RelluEssentials.getInstance().locationTypeEntryList) {
            if (lte.getId() == rs.getInt(FIELD_LOCATION_TYPE_FK)) {
                locationEntry.setLocationType(lte);
            }
        }
        return locationEntry;
    }

    public static @NonNull LocationTypeEntry mapLocationType(@NonNull ResultSet rs) throws SQLException {
        LocationTypeEntry locationTypeEntry = new LocationTypeEntry();
        locationTypeEntry.setId(rs.getInt(FIELD_ID));
        locationTypeEntry.setType(rs.getString(FIELD_LOCATION_TYPE));
        return locationTypeEntry;
    }


}
