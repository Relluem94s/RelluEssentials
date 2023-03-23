package de.relluem94.minecraft.server.spigot.essentials.helpers.pojo;

import org.bukkit.Location;

/**
 *
 * @author rellu
 */
public class LocationEntry {

    public static final String FIELD_ID = "id";
    public static final String FIELD_PLAYER_FK = "player_fk";
    public static final String FIELD_LOCATION_NAME = "location_name";
    public static final String FIELD_WORLD = "world";
    public static final String FIELD_POS_X = "x";
    public static final String FIELD_POS_Y = "y";
    public static final String FIELD_POS_Z = "z";
    public static final String FIELD_YAW = "yaw";
    public static final String FIELD_PITCH = "pitch";
    public static final String FIELD_LOCATION_TYPE_FK = "location_type_fk";

    private int id;
    private Location location;
    private int playerFK;
    private String locationName;
    private LocationTypeEntry locationType;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public int getPlayerId() {
        return playerFK;
    }

    public void setPlayerId(int playerId) {
        this.playerFK = playerId;
    }

    public LocationTypeEntry getLocationType() {
        return locationType;
    }

    public void setLocationType(LocationTypeEntry locationType) {
        this.locationType = locationType;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }
}
