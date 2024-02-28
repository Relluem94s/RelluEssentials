package de.relluem94.minecraft.server.spigot.essentials.helpers.pojo;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.Objects;

/**
 *
 * @author rellu
 */
@Setter
@Getter
public class LocationEntry {
    private int id;
    private String world;

    private double x;
    private double y;
    private double z;
    private float yaw;
    private float pitch;
    private int playerId;
    private String locationName;
    private LocationTypeEntry locationType;

    public Location getLocation() {
        return new Location(Bukkit.getWorld(world), x, y, z, yaw, pitch);
    }

    public void setLocation(Location location) {
        this.world = Objects.requireNonNull(location.getWorld()).getName();
        this.x = location.getX();
        this.y = location.getY();
        this.z = location.getZ();
        this.yaw = location.getYaw();
        this.pitch = location.getPitch();
    }
}