package de.relluem94.minecraft.server.spigot.essentials.services;

import static de.relluem94.minecraft.server.spigot.essentials.constants.ExceptionConstants.PLUGIN_EXCEPTION_LOCATION_TYPE_NOT_FOUND;

import de.relluem94.minecraft.server.spigot.essentials.enums.LocationType;
import de.relluem94.minecraft.server.spigot.essentials.helpers.DatabaseHelper;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.LocationEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.LocationTypeEntry;
import de.relluem94.minecraft.server.spigot.essentials.repositories.WarpRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class WarpService {

  private final WarpRepository warpRepository;
  private final DatabaseHelper databaseHelper;
  private final LocationTypeService locationTypeService;

  public WarpService(WarpRepository warpRepository, DatabaseHelper databaseHelper,
      LocationTypeService locationTypeService) {
    this.warpRepository = warpRepository;
    this.databaseHelper = databaseHelper;
    this.locationTypeService = locationTypeService;
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

    LocationTypeEntry warpType = locationTypeService.findByName(LocationType.WARP)
        .orElseThrow(() -> new IllegalStateException(PLUGIN_EXCEPTION_LOCATION_TYPE_NOT_FOUND));

    LocationEntry le = new LocationEntry();
    le.setLocation(player.getLocation());
    le.setLocationName(name);
    le.setLocationType(warpType);
    le.setPlayerId(playerId);

    databaseHelper.insertLocation(le);

    LocationEntry persisted = databaseHelper.getLocation(player.getLocation(), warpType.getId());
    warpRepository.save(persisted != null ? persisted : le);

     return true;
  }

  public boolean removeWarp(String name) {
    return warpRepository.findByName(name)
        .map(le -> {
          databaseHelper.deleteLocation(le);
          warpRepository.delete(le);
          return true;
        })
        .orElse(false);
  }
}