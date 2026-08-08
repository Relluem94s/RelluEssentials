package de.relluem94.minecraft.server.spigot.essentials.services;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.helpers.DatabaseHelper;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.LocationEntry;
import de.relluem94.minecraft.server.spigot.essentials.repositories.WarpRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class WarpService {

  private final WarpRepository warpRepository;
  private final DatabaseHelper databaseHelper;

  public WarpService(WarpRepository warpRepository, DatabaseHelper databaseHelper) {
    this.warpRepository = warpRepository;
    this.databaseHelper = databaseHelper;
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

    int typeId = 3;
    LocationEntry le = new LocationEntry();
    le.setLocation(player.getLocation());
    le.setLocationName(name);
    le.setLocationType(RelluEssentials.getInstance().locationTypeEntryList.get(typeId - 1));
    le.setPlayerId(playerId);
    databaseHelper.insertLocation(le);

    LocationEntry persisted = databaseHelper.getLocation(player.getLocation(), typeId);
    if (persisted != null) {
      le = persisted;
    }

    warpRepository.save(le);
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