package de.relluem94.minecraft.server.spigot.essentials.repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class BackLocationRepository {

  private final Map<Player, Location> backLocations = new HashMap<>();

  public void save(Player player, Location location) {
    backLocations.put(player, location);
  }

  public Optional<Location> find(Player player) {
    return Optional.ofNullable(backLocations.get(player));
  }

  public void delete(Player player) {
    backLocations.remove(player);
  }

  public boolean exists(Player player) {
    return backLocations.containsKey(player);
  }
}