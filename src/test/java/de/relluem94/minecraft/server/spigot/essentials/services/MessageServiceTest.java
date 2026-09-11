package de.relluem94.minecraft.server.spigot.essentials.services;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import org.bukkit.Location;
import org.bukkit.World;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MessageServiceTest {

  @Mock
  private TranslationService translationService;

  @Mock
  private Location location;

  @Mock
  private World world;

  private MessageService messageService;

  @BeforeEach
  void setUp() {
    messageService = new MessageService(translationService);
  }

  @Test
  void locationToStringWithoutRoundReturnsFormattedStringWithWorldName() {
    when(location.getWorld()).thenReturn(world);
    when(location.getX()).thenReturn(1.5);
    when(location.getY()).thenReturn(2.5);
    when(location.getZ()).thenReturn(3.5);
    when(world.getName()).thenReturn("world");
    when(translationService.get(MessageKey.COMMAND_WHERE_STRING, 1.5, 2.5, 3.5, "world")).thenReturn("1.5 2.5 3.5 world");

    String result = messageService.locationToString(location);

    assertAll(
        () -> assertNotNull(result),
        () -> assertEquals("1.5 2.5 3.5 world", result)
    );
  }

  @Test
  void locationToStringWithoutRoundReturnsFormattedStringWithNullWorld() {
    when(location.getWorld()).thenReturn(null);
    when(location.getX()).thenReturn(1.5);
    when(location.getY()).thenReturn(2.5);
    when(location.getZ()).thenReturn(3.5);
    when(translationService.get(MessageKey.COMMAND_WHERE_STRING, 1.5, 2.5, 3.5, "null")).thenReturn("1.5 2.5 3.5 null");

    String result = messageService.locationToString(location);

    assertAll(
        () -> assertNotNull(result),
        () -> assertEquals("1.5 2.5 3.5 null", result)
    );
  }

  @Test
  void locationToStringWithRoundFalseReturnsFormattedStringWithWorldName() {
    when(location.getWorld()).thenReturn(world);
    when(location.getX()).thenReturn(4.0);
    when(location.getY()).thenReturn(5.0);
    when(location.getZ()).thenReturn(6.0);
    when(world.getName()).thenReturn("nether");
    when(translationService.get(MessageKey.COMMAND_WHERE_STRING, 4.0, 5.0, 6.0, "nether")).thenReturn("4.0 5.0 6.0 nether");

    String result = messageService.locationToString(location, false);

    assertAll(
        () -> assertNotNull(result),
        () -> assertEquals("4.0 5.0 6.0 nether", result)
    );
  }

  @Test
  void locationToStringWithRoundFalseReturnsFormattedStringWithNullWorld() {
    when(location.getWorld()).thenReturn(null);
    when(location.getX()).thenReturn(4.0);
    when(location.getY()).thenReturn(5.0);
    when(location.getZ()).thenReturn(6.0);
    when(translationService.get(MessageKey.COMMAND_WHERE_STRING, 4.0, 5.0, 6.0, "null")).thenReturn("4.0 5.0 6.0 null");

    String result = messageService.locationToString(location, false);

    assertAll(
        () -> assertNotNull(result),
        () -> assertEquals("4.0 5.0 6.0 null", result)
    );
  }

  @Test
  void locationToStringWithRoundTrueDelegatesToLocationToStringWithoutRound() {
    when(location.getWorld()).thenReturn(world);
    when(location.getX()).thenReturn(7.0);
    when(location.getY()).thenReturn(8.0);
    when(location.getZ()).thenReturn(9.0);
    when(world.getName()).thenReturn("end");
    when(translationService.get(MessageKey.COMMAND_WHERE_STRING, 7.0, 8.0, 9.0, "end")).thenReturn("7.0 8.0 9.0 end");

    String result = messageService.locationToString(location, true);

    assertAll(
        () -> assertNotNull(result),
        () -> assertEquals("7.0 8.0 9.0 end", result)
    );
  }

  @Test
  void locationToStringWithoutRoundPropagatesTranslationServiceException() {
    when(location.getWorld()).thenReturn(world);
    when(location.getX()).thenReturn(1.0);
    when(location.getY()).thenReturn(2.0);
    when(location.getZ()).thenReturn(3.0);
    when(world.getName()).thenReturn("world");
    when(translationService.get(any(), any(), any(), any(), any())).thenThrow(new RuntimeException("translation error"));

    assertThrows(RuntimeException.class, () -> messageService.locationToString(location));
  }

  @Test
  void locationToStringWithRoundFalsePropagatesTranslationServiceException() {
    when(location.getWorld()).thenReturn(world);
    when(location.getX()).thenReturn(1.0);
    when(location.getY()).thenReturn(2.0);
    when(location.getZ()).thenReturn(3.0);
    when(world.getName()).thenReturn("world");
    when(translationService.get(any(), any(), any(), any(), any())).thenThrow(new RuntimeException("translation error"));

    assertThrows(RuntimeException.class, () -> messageService.locationToString(location, false));
  }

  @Test
  void locationToStringWithRoundTruePropagatesTranslationServiceException() {
    when(location.getWorld()).thenReturn(world);
    when(location.getX()).thenReturn(1.0);
    when(location.getY()).thenReturn(2.0);
    when(location.getZ()).thenReturn(3.0);
    when(world.getName()).thenReturn("world");
    when(translationService.get(any(), any(), any(), any(), any())).thenThrow(new RuntimeException("translation error"));

    assertThrows(RuntimeException.class, () -> messageService.locationToString(location, true));
  }
}