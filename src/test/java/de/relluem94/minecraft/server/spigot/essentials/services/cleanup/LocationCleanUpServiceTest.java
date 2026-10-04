package de.relluem94.minecraft.server.spigot.essentials.services.cleanup;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.repositories.LocationRepository;
import de.relluem94.minecraft.server.spigot.essentials.services.TranslationService;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class LocationCleanUpServiceTest {

  @Mock
  private TranslationService translationService;

  @Mock
  private LocationRepository locationRepository;

  @InjectMocks
  private LocationCleanUpService locationCleanUpService;

  @Test
  void cleanUpLocationsRemovesOutdatedLocationsAndNotifiesPlayer() {
    Player player = mock(Player.class);
    String expectedMessage = "Cleaned up 3 locations.";

    when(locationRepository.removeOutdatedLocations()).thenReturn(3);
    when(translationService.getWithPrefix(MessageKey.COMMAND_ADMIN_CLEAN_OLD_LOCATIONS_END, 3))
        .thenReturn(expectedMessage);

    locationCleanUpService.cleanUpLocations(player);

    verify(locationRepository).removeOutdatedLocations();
    verify(translationService).getWithPrefix(MessageKey.COMMAND_ADMIN_CLEAN_OLD_LOCATIONS_END, 3);
    verify(player).sendMessage(expectedMessage);
  }

  @Test
  void cleanUpLocationsNotifiesPlayerWithZeroWhenNothingDeleted() {
    Player player = mock(Player.class);
    String expectedMessage = "Cleaned up 0 locations.";

    when(locationRepository.removeOutdatedLocations()).thenReturn(0);
    when(translationService.getWithPrefix(MessageKey.COMMAND_ADMIN_CLEAN_OLD_LOCATIONS_END, 0))
        .thenReturn(expectedMessage);

    locationCleanUpService.cleanUpLocations(player);

    verify(locationRepository).removeOutdatedLocations();
    verify(translationService).getWithPrefix(MessageKey.COMMAND_ADMIN_CLEAN_OLD_LOCATIONS_END, 0);
    verify(player).sendMessage(expectedMessage);
  }

  @Test
  @SuppressWarnings("ConstantConditions")
  void cleanUpLocationsThrowsNullPointerExceptionWhenPlayerIsNull() {
    assertThrows(NullPointerException.class, () -> locationCleanUpService.cleanUpLocations(null));
  }

  @Test
  void cleanUpLocationsPropagatesExceptionFromRepository() {
    Player player = mock(Player.class);

    when(locationRepository.removeOutdatedLocations()).thenThrow(new RuntimeException("DB error"));

    assertThrows(RuntimeException.class, () -> locationCleanUpService.cleanUpLocations(player));

    verify(locationRepository).removeOutdatedLocations();
    verifyNoInteractions(translationService);
    verify(player, never()).sendMessage(anyString());
  }

  @Test
  void cleanUpLocationsPropagatesExceptionFromTranslationService() {
    Player player = mock(Player.class);

    when(locationRepository.removeOutdatedLocations()).thenReturn(1);
    when(translationService.getWithPrefix(MessageKey.COMMAND_ADMIN_CLEAN_OLD_LOCATIONS_END, 1))
        .thenThrow(new RuntimeException("Translation error"));

    assertThrows(RuntimeException.class, () -> locationCleanUpService.cleanUpLocations(player));

    verify(player, never()).sendMessage(anyString());
  }
}