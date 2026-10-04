package de.relluem94.minecraft.server.spigot.essentials.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import java.util.Optional;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BackLocationRepositoryTest {

  private BackLocationRepository backLocationRepository;
  private Player player;
  private Location location;

  @BeforeEach
  void setUp() {
    backLocationRepository = new BackLocationRepository();
    player = mock(Player.class);
    location = mock(Location.class);
  }

  @Test
  void savePersistsLocationForPlayer() {
    backLocationRepository.save(player, location);

    Optional<Location> result = backLocationRepository.find(player);
    assertTrue(result.isPresent());
    assertEquals(location, result.get());
  }

  @Test
  void saveOverwritesExistingLocationForPlayer() {
    Location firstLocation = mock(Location.class);
    Location secondLocation = mock(Location.class);

    backLocationRepository.save(player, firstLocation);
    backLocationRepository.save(player, secondLocation);

    Optional<Location> result = backLocationRepository.find(player);
    assertTrue(result.isPresent());
    assertEquals(secondLocation, result.get());
  }

  @Test
  void findReturnsEmptyWhenNoLocationSavedForPlayer() {
    Optional<Location> result = backLocationRepository.find(player);

    assertFalse(result.isPresent());
  }

  @Test
  void findReturnsLocationOnlyForCorrectPlayer() {
    Player otherPlayer = mock(Player.class);
    backLocationRepository.save(player, location);

    Optional<Location> result = backLocationRepository.find(otherPlayer);

    assertFalse(result.isPresent());
  }

  @Test
  void deleteRemovesLocationForPlayer() {
    backLocationRepository.save(player, location);
    backLocationRepository.delete(player);

    Optional<Location> result = backLocationRepository.find(player);
    assertFalse(result.isPresent());
  }

  @Test
  void deleteDoesNotThrowWhenPlayerHasNoLocation() {
    backLocationRepository.delete(player);

    Optional<Location> result = backLocationRepository.find(player);
    assertFalse(result.isPresent());
  }

  @Test
  void deleteDoesNotAffectOtherPlayers() {
    Player otherPlayer = mock(Player.class);
    Location otherLocation = mock(Location.class);

    backLocationRepository.save(player, location);
    backLocationRepository.save(otherPlayer, otherLocation);
    backLocationRepository.delete(player);

    Optional<Location> result = backLocationRepository.find(otherPlayer);
    assertTrue(result.isPresent());
    assertEquals(otherLocation, result.get());
  }

  @Test
  void existsReturnsTrueWhenLocationSavedForPlayer() {
    backLocationRepository.save(player, location);

    assertTrue(backLocationRepository.exists(player));
  }

  @Test
  void existsReturnsFalseWhenNoLocationSavedForPlayer() {
    assertFalse(backLocationRepository.exists(player));
  }

  @Test
  void existsReturnsFalseAfterDeletion() {
    backLocationRepository.save(player, location);
    backLocationRepository.delete(player);

    assertFalse(backLocationRepository.exists(player));
  }

  @Test
  void existsReturnsFalseForDifferentPlayer() {
    Player otherPlayer = mock(Player.class);
    backLocationRepository.save(player, location);

    assertFalse(backLocationRepository.exists(otherPlayer));
  }
}