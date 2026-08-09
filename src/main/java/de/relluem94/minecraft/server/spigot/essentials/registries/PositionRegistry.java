package de.relluem94.minecraft.server.spigot.essentials.registries;

import de.relluem94.rellulib.stores.DoubleStore;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class PositionRegistry {

  private final Map<Player, DoubleStore<Location, Location>> positions = new HashMap<>();

  public void put(Player player, DoubleStore<Location, Location> locationStore) {
    positions.put(player, locationStore);
  }

  public void remove(Player player) {
    positions.remove(player);
  }

  public Map<Player, DoubleStore<Location, Location>> getAll() {
    return positions;
  }

  public boolean contains(Player player) {
    return positions.containsKey(player);
  }
}