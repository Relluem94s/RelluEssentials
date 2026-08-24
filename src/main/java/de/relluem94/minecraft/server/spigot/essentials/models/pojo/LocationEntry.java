package de.relluem94.minecraft.server.spigot.essentials.models.pojo;

import java.util.Objects;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;

/**
 * Represents a serializable entry of a Minecraft location.
 *
 * @author rellu
 */
@Setter
@Getter
@EqualsAndHashCode
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

  /**
   * Creates a new Bukkit Location instance based on the stored coordinates and world name.
   *
   * @return the reconstructed {@link Location}
   */
  public Location getLocation() {
    return new Location(Bukkit.getWorld(world), x, y, z, yaw, pitch);
  }

  /**
   * Updates the entry fields with values from the provided Bukkit Location.
   *
   * @param location the location to extract data from
   */
  public void setLocation(Location location) {
    this.world = Objects.requireNonNull(location.getWorld()).getName();
    this.x = location.getX();
    this.y = location.getY();
    this.z = location.getZ();
    this.yaw = location.getYaw();
    this.pitch = location.getPitch();
  }
}