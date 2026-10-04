package de.relluem94.minecraft.server.spigot.essentials.repositories;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * Repository for managing back locations of players.
 * Stores the last known location for each player to allow them to teleport back.
 */
public class BackLocationRepository {

  private final Map<Player, Location> backLocations = new HashMap<>();

  /**
   * Saves or overwrites the back location for the given player.
   *
   * @param player the player whose back location is being saved
   * @param location the location to store as the player's back location
   */
  public void save(Player player, Location location) {
    backLocations.put(player, location);
  }

  /**
   * Retrieves the back location of the given player, if present.
   *
   * @param player the player whose back location is being looked up
   * @return an {@link Optional} containing the back location, or empty if none exists
   */
  public Optional<Location> find(Player player) {
    return Optional.ofNullable(backLocations.get(player));
  }

  /**
   * Removes the back location of the given player.
   *
   * @param player the player whose back location should be deleted
   */
  public void delete(Player player) {
    backLocations.remove(player);
  }

  /**
   * Checks whether a back location exists for the given player.
   *
   * @param player the player to check
   * @return {@code true} if a back location exists for the player, {@code false} otherwise
   */
  public boolean exists(Player player) {
    return backLocations.containsKey(player);
  }
}