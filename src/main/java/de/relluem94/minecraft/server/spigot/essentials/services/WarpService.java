package de.relluem94.minecraft.server.spigot.essentials.services;

import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.enums.LocationType;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.LocationEntry;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class WarpService {

  private final ServiceContext serviceContext;

  public WarpService(ServiceContext serviceContext) {
    this.serviceContext = serviceContext;
  }

  public Optional<LocationEntry> findWarpByName(String name) {
    return serviceContext.getLocationService().findByType(LocationType.WARP).stream()
        .filter(le -> le.getLocationName().equals(name))
        .findFirst();
  }

  public Optional<LocationEntry> findWarpByNameAndWorld(String name, World world) {
    return serviceContext.getLocationService().findByType(LocationType.WARP).stream()
        .filter(le -> le.getLocation() != null
            && le.getLocation().getWorld() != null
            && le.getLocationName().equals(name)
            && le.getLocation().getWorld().equals(world))
        .findFirst();
  }

  public List<LocationEntry> findWarpsByWorld(World world) {
    return serviceContext.getLocationService().findByType(LocationType.WARP).stream()
        .filter(le -> le.getLocation() != null
            && le.getLocation().getWorld() != null
            && le.getLocation().getWorld().equals(world))
        .collect(Collectors.toList());
  }

  public List<String> getWarpNamesByWorld(World world) {
    return findWarpsByWorld(world).stream()
        .map(LocationEntry::getLocationName)
        .collect(Collectors.toList());
  }

  public boolean warpExists(String name) {
    return findWarpByName(name).isPresent();
  }

  public boolean addWarp(String name, Player player, int playerId) {
    if (warpExists(name)) {
      return false;
    }
    serviceContext.getLocationService().saveAndFetch(
        serviceContext.getLocationService().buildLocationEntry(player, name, LocationType.WARP, playerId));
    return true;
  }

  public boolean removeWarp(String name) {
    return findWarpByName(name)
        .map(locationEntry -> {
          serviceContext.getLocationService().delete(locationEntry);
          return true;
        })
        .orElse(false);
  }
}