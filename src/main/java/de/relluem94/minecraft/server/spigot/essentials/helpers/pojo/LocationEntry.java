package de.relluem94.minecraft.server.spigot.essentials.helpers.pojo;

import org.bukkit.Bukkit;
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
    private String world;

    private double x;
    private double y;
    private double z;
    private float yaw;
    private float pitch;
    private int playerFK;
    private String locationName;
    private LocationTypeEntry locationType;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
    
    public String getWorld() {
        return world;
    }

    public void setWorld(String world) {
        this.world = world;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public float getYaw() {
        return yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public float getPitch() {
        return pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public Location getLocation() {
        return new Location(Bukkit.getWorld(world), x, y, z, yaw, pitch);
    }

    public void setLocation(Location location) {
        setWorld(location.getWorld().getName());
        setX(location.getX());
        setY(location.getY());
        setZ(location.getZ());
        setYaw(location.getYaw());
        setPitch(location.getPitch());
    }
}
