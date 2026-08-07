package de.relluem94.minecraft.server.spigot.essentials.services;

import de.relluem94.minecraft.server.spigot.essentials.repositories.BackLocationRepository;
import java.util.Optional;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class BackService {

  private final BackLocationRepository backLocationRepository;

  public BackService(BackLocationRepository backLocationRepository) {
    this.backLocationRepository = backLocationRepository;
  }

  public void saveBackPoint(Player player) {
    backLocationRepository.delete(player);
    backLocationRepository.save(player, player.getLocation());
  }

  public Optional<Location> findBackPoint(Player player) {
    return backLocationRepository.find(player);
  }

  public void removeBackPoint(Player player) {
    backLocationRepository.delete(player);
  }

  public boolean hasBackPoint(Player player) {
    return backLocationRepository.exists(player);
  }
}