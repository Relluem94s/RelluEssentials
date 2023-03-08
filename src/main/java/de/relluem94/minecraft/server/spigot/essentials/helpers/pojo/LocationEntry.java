package de.relluem94.minecraft.server.spigot.essentials.helpers.pojo;

import org.bukkit.Location;

/**
 *
 * @author rellu
 */
public class LocationEntry {

    private int id;
    private Location location;
    private int player_fk;
    private String location_name;
    private LocationTypeEntry locationType;

    public LocationEntry() {
    }

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
        return player_fk;
    }

    public void setPlayerId(int playerId) {
        this.player_fk = playerId;
    }

    public LocationTypeEntry getLocationType() {
        return locationType;
    }

    public void setLocationType(LocationTypeEntry locationType) {
        this.locationType = locationType;
    }

    public String getLocationName() {
        return location_name;
    }

    public void setLocationName(String locationName) {
        this.location_name = locationName;
    }
}
