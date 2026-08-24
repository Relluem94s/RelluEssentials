package de.relluem94.minecraft.server.spigot.essentials.services;

import de.relluem94.minecraft.server.spigot.essentials.repositories.BackLocationRepository;
import java.util.Optional;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * Service responsible for managing players' back locations.
 */
public class BackService {

  private final BackLocationRepository backLocationRepository;

  /**
   * Constructs a new BackService.
   *
   * @param backLocationRepository the repository used to persist back locations
   */
  public BackService(BackLocationRepository backLocationRepository) {
    this.backLocationRepository = backLocationRepository;
  }

  /**
   * Saves the current location of the player as their back point.
   *
   * @param player the player whose location is being saved
   */
  public void saveBackPoint(Player player) {
    backLocationRepository.delete(player);
    backLocationRepository.save(player, player.getLocation());
  }

  /**
   * Retrieves the saved back point for the given player.
   *
   * @param player the player whose back point is being searched for
   * @return an Optional containing the player's back location, or empty if none exists
   */
  public Optional<Location> findBackPoint(Player player) {
    return backLocationRepository.find(player);
  }

  /**
   * Removes the saved back point for the given player.
   *
   * @param player the player whose back point is being removed
   */
  public void removeBackPoint(Player player) {
    backLocationRepository.delete(player);
  }

  /**
   * Checks if the player has a saved back point.
   *
   * @param player the player to check
   * @return true if a back point exists, false otherwise
   */
  public boolean hasBackPoint(Player player) {
    return backLocationRepository.exists(player);
  }
}