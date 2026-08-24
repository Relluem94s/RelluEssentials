package de.relluem94.minecraft.server.spigot.essentials.services;

import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.enums.LocationType;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.LocationEntry;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.bukkit.World;
import org.bukkit.entity.Player;

/**
 * Service responsible for managing warp locations.
 */
public class WarpService {

  private final ServiceContext serviceContext;

  /**
   * Constructs a new WarpService.
   *
   * @param serviceContext the service context used to access other services
   */
  public WarpService(ServiceContext serviceContext) {
    this.serviceContext = serviceContext;
  }

  /**
   * Finds a warp by its name.
   *
   * @param name the name of the warp to find
   * @return an {@link Optional} containing the found {@link LocationEntry}, or empty if not found
   */
  public Optional<LocationEntry> findWarpByName(String name) {
    return serviceContext.getLocationService().findByType(LocationType.WARP).stream()
        .filter(le -> le.getLocationName().equals(name))
        .findFirst();
  }

  /**
   * Finds a warp by its name and the specific world it resides in.
   *
   * @param name  the name of the warp to find
   * @param world the world where the warp should be located
   * @return an {@link Optional} containing the found {@link LocationEntry}, or empty if not found
   */
  public Optional<LocationEntry> findWarpByNameAndWorld(String name, World world) {
    return serviceContext.getLocationService().findByType(LocationType.WARP).stream()
        .filter(le -> le.getLocation() != null
            && le.getLocation().getWorld() != null
            && le.getLocationName().equals(name)
            && le.getLocation().getWorld().equals(world))
        .findFirst();
  }

  /**
   * Finds all warps located in a specific world.
   *
   * @param world the world to filter warps by
   * @return a list of {@link LocationEntry} objects found in the specified world
   */
  public List<LocationEntry> findWarpsByWorld(World world) {
    return serviceContext.getLocationService().findByType(LocationType.WARP).stream()
        .filter(le -> le.getLocation() != null
            && le.getLocation().getWorld() != null
            && le.getLocation().getWorld().equals(world))
        .collect(Collectors.toList());
  }

  /**
   * Retrieves the names of all warps located in a specific world.
   *
   * @param world the world to filter warp names by
   * @return a list of warp names as strings
   */
  public List<String> getWarpNamesByWorld(World world) {
    return findWarpsByWorld(world).stream()
        .map(LocationEntry::getLocationName)
        .collect(Collectors.toList());
  }

  /**
   * Checks if a warp with the given name exists.
   *
   * @param name the name of the warp to check
   * @return true if the warp exists, false otherwise
   */
  public boolean warpExists(String name) {
    return findWarpByName(name).isPresent();
  }

  /**
   * Adds a new warp at the player's current location.
   *
   * @param name     the name of the new warp
   * @param player   the player creating the warp
   * @param playerId the unique identifier of the player
   * @return true if the warp was successfully added, false if a warp with that name already exists
   */
  public boolean addWarp(String name, Player player, int playerId) {
    if (warpExists(name)) {
      return false;
    }
    serviceContext.getLocationService().saveAndFetch(
        serviceContext.getLocationService()
            .buildLocationEntry(player, name, LocationType.WARP, playerId));
    return true;
  }

  /**
   * Removes a warp by its name.
   *
   * @param name the name of the warp to remove
   * @return true if the warp was found and removed, false otherwise
   */
  public boolean removeWarp(String name) {
    return findWarpByName(name)
        .map(locationEntry -> {
          serviceContext.getLocationService().delete(locationEntry);
          return true;
        })
        .orElse(false);
  }
}