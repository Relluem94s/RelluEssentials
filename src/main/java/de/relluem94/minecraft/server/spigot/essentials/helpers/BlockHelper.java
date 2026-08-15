package de.relluem94.minecraft.server.spigot.essentials.helpers;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import java.util.HashMap;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;


/**
 *
 * @author Relluem94
 */
@Deprecated
public class BlockHelper {

  private final HashMap<Location, Long> locations = new HashMap<>();
  @Setter
  private Material type;

  public BlockHelper(Material type) {
    this.type = type;
  }

  public static boolean checkBlockAt(@NotNull Location location, Material mat) {
    return location.getBlock().getType() == mat;
  }

  public void addLocation(Location location, Long delay) {
    locations.put(location, delay);
  }

  public void putAll(@NotNull BlockHelper setBlockHelper) {
    locations.putAll(setBlockHelper.locations);
  }

  public void setBlocks(long addDelay) {
    locations.forEach((location, delay) -> Bukkit.getServer().getScheduler()
        .scheduleSyncDelayedTask(RelluEssentials.getInstance(),
            () -> location.getBlock().setType(type), Math.abs(delay + addDelay)));
  }

  public void setBlocks() {
    setBlocks(0);
  }
}