package de.relluem94.minecraft.server.spigot.essentials.repositories;

import de.relluem94.minecraft.server.spigot.essentials.models.pojo.LocationEntry;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.bukkit.World;

/**
 * Repository for managing warp {@link LocationEntry} objects in memory.
 */
public class WarpRepository {

  private final List<LocationEntry> warps;

  /**
   * @param warps mutable list used as the underlying warp storage
   */
  public WarpRepository(List<LocationEntry> warps) {
    this.warps = warps;
  }

  /**
   * Finds a warp by its name.
   *
   * @param name the warp name to search for
   * @return an {@link Optional} containing the matching entry, or empty if not found
   */
  public Optional<LocationEntry> findByName(String name) {
    return warps.stream()
        .filter(le -> le != null && le.getLocationName().equals(name))
        .findFirst();
  }

  /**
   * Finds a warp by its name and world.
   *
   * @param name  the warp name to search for
   * @param world the world the warp must be located in
   * @return an {@link Optional} containing the matching entry, or empty if not found
   */
  public Optional<LocationEntry> findByNameAndWorld(String name, World world) {
    return warps.stream()
        .filter(le -> le != null
            && le.getLocation() != null
            && le.getLocation().getWorld() != null
            && le.getLocationName().equals(name)
            && le.getLocation().getWorld().equals(world))
        .findFirst();
  }

  /**
   * Returns all warps located in the given world.
   *
   * @param world the world to filter by
   * @return a list of matching {@link LocationEntry} objects
   */
  public List<LocationEntry> findByWorld(World world) {
    return warps.stream()
        .filter(le -> le != null
            && le.getLocation() != null
            && le.getLocation().getWorld() != null
            && le.getLocation().getWorld().equals(world))
        .collect(Collectors.toList());
  }

  /**
   * Adds a warp entry to the repository.
   *
   * @param le the {@link LocationEntry} to add
   */
  public void save(LocationEntry le) {
    warps.add(le);
  }

  /**
   * Removes a warp entry from the repository.
   *
   * @param le the {@link LocationEntry} to remove
   */
  public void delete(LocationEntry le) {
    warps.remove(le);
  }
}