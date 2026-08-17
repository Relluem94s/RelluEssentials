package de.relluem94.minecraft.server.spigot.essentials.registries;

import de.relluem94.rellulib.stores.DoubleStore;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * Registry responsible for managing and storing location-based data for players.
 */
public class PositionRegistry {

  /**
   * A mapping of players to their respective location stores.
   */
  private final Map<Player, DoubleStore<Location, Location>> positions = new HashMap<>();

  /**
   * Associates a specific location store with a player.
   *
   * @param player the player to associate the store with
   * @param locationStore the store containing location data
   */
  public void put(Player player, DoubleStore<Location, Location> locationStore) {
    positions.put(player, locationStore);
  }

  /**
   * Removes the location store associated with the specified player.
   *
   * @param player the player whose data should be removed
   */
  public void remove(Player player) {
    positions.remove(player);
  }

  /**
   * Retrieves all currently registered player position stores.
   *
   * @return a map containing all players and their corresponding location stores
   */
  public Map<Player, DoubleStore<Location, Location>> getAll() {
    return positions;
  }

  /**
   * Checks if a player has an associated location store in the registry.
   *
   * @param player the player to check
   * @return true if the player is registered, false otherwise
   */
  public boolean contains(Player player) {
    return positions.containsKey(player);
  }
}