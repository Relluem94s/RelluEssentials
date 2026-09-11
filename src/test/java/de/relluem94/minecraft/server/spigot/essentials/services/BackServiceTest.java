package de.relluem94.minecraft.server.spigot.essentials.services;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.relluem94.minecraft.server.spigot.essentials.repositories.BackLocationRepository;
import java.util.Optional;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BackServiceTest {

  @Mock
  private BackLocationRepository backLocationRepository;

  @Mock
  private Player player;

  @Mock
  private Location location;

  @InjectMocks
  private BackService backService;

  @Test
  void saveBackPointDeletesExistingAndSavesCurrentLocation() {
    when(player.getLocation()).thenReturn(location);

    backService.saveBackPoint(player);

    assertAll(
        () -> verify(backLocationRepository).delete(player),
        () -> verify(backLocationRepository).save(player, location)
    );
  }

  @Test
  void saveBackPointPropagatesExceptionFromDelete() {
    doThrow(new RuntimeException("delete failed")).when(backLocationRepository).delete(player);

    assertThrows(RuntimeException.class, () -> backService.saveBackPoint(player));
  }

  @Test
  void saveBackPointPropagatesExceptionFromSave() {
    when(player.getLocation()).thenReturn(location);
    doThrow(new RuntimeException("save failed")).when(backLocationRepository).save(player, location);

    assertThrows(RuntimeException.class, () -> backService.saveBackPoint(player));
  }

  @Test
  void findBackPointReturnsLocationWhenExists() {
    when(backLocationRepository.find(player)).thenReturn(Optional.of(location));

    Optional<Location> result = backService.findBackPoint(player);

    assertTrue(result.isPresent());
    assertEquals(location, result.get());
  }

  @Test
  void findBackPointReturnsEmptyWhenNoLocationExists() {
    when(backLocationRepository.find(player)).thenReturn(Optional.empty());

    Optional<Location> result = backService.findBackPoint(player);

    assertFalse(result.isPresent());
  }

  @Test
  void findBackPointPropagatesExceptionFromRepository() {
    when(backLocationRepository.find(player)).thenThrow(new RuntimeException("find failed"));

    assertThrows(RuntimeException.class, () -> backService.findBackPoint(player));
  }

  @Test
  void removeBackPointDelegatesDeletionToRepository() {
    backService.removeBackPoint(player);

    verify(backLocationRepository).delete(player);
  }

  @Test
  void removeBackPointPropagatesExceptionFromRepository() {
    doThrow(new RuntimeException("delete failed")).when(backLocationRepository).delete(player);

    assertThrows(RuntimeException.class, () -> backService.removeBackPoint(player));
  }

  @Test
  void hasBackPointReturnsTrueWhenBackPointExists() {
    when(backLocationRepository.exists(player)).thenReturn(true);

    boolean result = backService.hasBackPoint(player);

    assertTrue(result);
  }

  @Test
  void hasBackPointReturnsFalseWhenNoBackPointExists() {
    when(backLocationRepository.exists(player)).thenReturn(false);

    boolean result = backService.hasBackPoint(player);

    assertFalse(result);
  }

  @Test
  void hasBackPointPropagatesExceptionFromRepository() {
    when(backLocationRepository.exists(player)).thenThrow(new RuntimeException("exists failed"));

    assertThrows(RuntimeException.class, () -> backService.hasBackPoint(player));
  }
}