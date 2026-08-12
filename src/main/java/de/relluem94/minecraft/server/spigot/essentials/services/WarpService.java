package de.relluem94.minecraft.server.spigot.essentials.services;

import de.relluem94.minecraft.server.spigot.essentials.enums.LocationType;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.LocationEntry;
import de.relluem94.minecraft.server.spigot.essentials.repositories.WarpRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class WarpService {

  private final WarpRepository warpRepository;
  private final LocationService locationService;

  public WarpService(WarpRepository warpRepository, LocationService locationService) {
    this.warpRepository = warpRepository;
    this.locationService = locationService;
  }

  public Optional<LocationEntry> findWarpByName(String name) {
    return warpRepository.findByName(name);
  }

  public Optional<LocationEntry> findWarpByNameAndWorld(String name, World world) {
    return warpRepository.findByNameAndWorld(name, world);
  }

  public List<LocationEntry> findWarpsByWorld(World world) {
    return warpRepository.findByWorld(world);
  }

  public List<String> getWarpNamesByWorld(World world) {
    return findWarpsByWorld(world).stream()
        .map(LocationEntry::getLocationName)
        .collect(Collectors.toList());
  }

  public boolean warpExists(String name) {
    return warpRepository.findByName(name).isPresent();
  }

  public boolean addWarp(String name, Player player, int playerId) {
    if (warpExists(name)) {
      return false;
    }
    LocationEntry locationEntry = locationService.buildLocationEntry(player, name, LocationType.WARP,
        playerId);
    LocationEntry persisted = locationService.saveAndFetch(locationEntry);
    warpRepository.save(persisted);
    return true;
  }

  public boolean removeWarp(String name) {
    return warpRepository.findByName(name)
        .map(locationEntry -> {
          locationService.delete(locationEntry);
          warpRepository.delete(locationEntry);
          return true;
        })
        .orElse(false);
  }
}