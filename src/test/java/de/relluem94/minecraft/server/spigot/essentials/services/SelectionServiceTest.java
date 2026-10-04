package de.relluem94.minecraft.server.spigot.essentials.services;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.models.Selection;
import de.relluem94.rellulib.stores.DoubleStore;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SelectionServiceTest {

  @Mock
  private ServiceContext serviceContext;

  @Mock
  private PositionService positionService;

  @Mock
  private TranslationService translationService;

  @Mock
  private Player player;

  @Mock
  private World world;

  @Mock
  private DoubleStore<Location, Location> positions;

  private SelectionService selectionService;

  @BeforeEach
  void setUp() {
    when(serviceContext.getPositionService()).thenReturn(positionService);
    lenient().when(serviceContext.getTranslationService()).thenReturn(translationService);
    selectionService = new SelectionService(serviceContext);
  }

  @Test
  void resolveReturnsNullAndSendsMessageWhenPlayerHasNoPositions() {
    when(positionService.hasPositions(player)).thenReturn(false);
    String expectedMessage = "no positions message";
    when(translationService.getWithPrefix(MessageKey.COMMAND_MODIFY_NO_POSITIONS)).thenReturn(expectedMessage);

    Selection result = selectionService.resolve(player);

    assertAll(
        () -> assertNull(result),
        () -> verify(player).sendMessage(expectedMessage)
    );
  }

  @Test
  void resolveReturnsNullAndSendsMessageWhenPos1IsNull() {
    when(positionService.hasPositions(player)).thenReturn(true);
    when(positionService.getPositions(player)).thenReturn(positions);
    when(positions.getValue()).thenReturn(null);
    String expectedMessage = "pos1 empty message";
    when(translationService.getWithPrefix(MessageKey.COMMAND_MODIFY_POS_1_EMPTY)).thenReturn(expectedMessage);

    Selection result = selectionService.resolve(player);

    assertAll(
        () -> assertNull(result),
        () -> verify(player).sendMessage(expectedMessage)
    );
  }

  @Test
  void resolveReturnsNullAndSendsMessageWhenPos2IsNull() {
    Location pos1 = new Location(world, 0, 0, 0);
    when(positionService.hasPositions(player)).thenReturn(true);
    when(positionService.getPositions(player)).thenReturn(positions);
    when(positions.getValue()).thenReturn(pos1);
    when(positions.getSecondValue()).thenReturn(null);
    String expectedMessage = "pos2 empty message";
    when(translationService.getWithPrefix(MessageKey.COMMAND_MODIFY_POS_2_EMPTY)).thenReturn(expectedMessage);

    Selection result = selectionService.resolve(player);

    assertAll(
        () -> assertNull(result),
        () -> verify(player).sendMessage(expectedMessage)
    );
  }

  @Test
  void resolveReturnsNullAndSendsMessageWhenPositionsAreInDifferentWorlds() {
    World secondWorld = mock(World.class);
    Location pos1 = new Location(world, 0, 0, 0);
    Location pos2 = new Location(secondWorld, 10, 10, 10);
    when(positionService.hasPositions(player)).thenReturn(true);
    when(positionService.getPositions(player)).thenReturn(positions);
    when(positions.getValue()).thenReturn(pos1);
    when(positions.getSecondValue()).thenReturn(pos2);
    String expectedMessage = "different worlds message";
    when(translationService.getWithPrefix(MessageKey.COMMAND_MODIFY_DIFFERENT_WORLDS)).thenReturn(expectedMessage);

    Selection result = selectionService.resolve(player);

    assertAll(
        () -> assertNull(result),
        () -> verify(player).sendMessage(expectedMessage)
    );
  }

  @Test
  void resolveReturnsSelectionWhenBothPositionsAreValidAndInSameWorld() {
    Location pos1 = new Location(world, 0, 0, 0);
    Location pos2 = new Location(world, 10, 10, 10);
    when(positionService.hasPositions(player)).thenReturn(true);
    when(positionService.getPositions(player)).thenReturn(positions);
    when(positions.getValue()).thenReturn(pos1);
    when(positions.getSecondValue()).thenReturn(pos2);

    Selection result = selectionService.resolve(player);

    assertNotNull(result);
    assertAll(
        () -> assertEquals(pos1, result.getPos1()),
        () -> assertEquals(pos2, result.getPos2()),
        () -> verify(player, never()).sendMessage(anyString())
    );
  }
}