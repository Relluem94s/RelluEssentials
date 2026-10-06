package de.relluem94.minecraft.server.spigot.essentials.commands.dev;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.relluem94.minecraft.server.spigot.essentials.commands.DevCommand;
import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.exceptions.WorldNotFoundException;
import de.relluem94.minecraft.server.spigot.essentials.helpers.WorldHelper;
import de.relluem94.minecraft.server.spigot.essentials.services.ServerService;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CloneWorldCommandTest {

  @Mock
  private ServiceContext serviceContext;

  @Mock
  private ServerService serverService;

  @Mock
  private Player player;

  private CloneWorldCommand cloneWorldCommand;

  @BeforeEach
  void setUp() {
    Logger.getLogger(ServerService.class.getName()).setLevel(Level.OFF);
    cloneWorldCommand = new CloneWorldCommand(serviceContext);
    lenient().when(serviceContext.getServerService()).thenReturn(serverService);
  }

  @Test
  void executeTeleportsPlayerToClonedWorldSpawn() throws WorldNotFoundException {
    World clonedWorld = mock(World.class);
    Location spawnLocation = mock(Location.class);

    when(serverService.getWorld("world2")).thenReturn(clonedWorld);
    when(clonedWorld.getSpawnLocation()).thenReturn(spawnLocation);

    cloneWorldCommand.execute(player, new String[]{"worlds"});

    verify(serverService).cloneWorld("world2", "world");
    verify(player).teleport(spawnLocation);
  }

  @Test
  void executeDoesNotTeleportPlayerWhenClonedWorldIsNull() throws WorldNotFoundException {
    when(serverService.getWorld("world2")).thenReturn(null);

    cloneWorldCommand.execute(player, new String[]{"worlds"});

    verify(serverService).cloneWorld("world2", "world");
    verify(player, never()).teleport((Location) org.mockito.ArgumentMatchers.any());
  }

  @Test
  void executeHandlesWorldNotFoundExceptionGracefully() {
    Logger.getLogger(CloneWorldCommand.class.getName()).setLevel(Level.OFF);
    World clonedWorld = mock(World.class);
    Location spawnLocation = mock(Location.class);

    try (MockedStatic<WorldHelper> worldHelperMock = mockStatic(WorldHelper.class)) {
      worldHelperMock.when(() -> serverService.cloneWorld("world2", "world"))
          .thenThrow(new WorldNotFoundException("world not found"));
      when(serverService.getWorld("world2")).thenReturn(clonedWorld);
      when(clonedWorld.getSpawnLocation()).thenReturn(spawnLocation);

      cloneWorldCommand.execute(player, new String[]{"worlds"});

      verify(player).teleport(spawnLocation);
    }
  }

  @Test
  void matchesReturnsTrueWhenArgMatchesWorldsCommand() {
    String worldsCommandName = DevCommand.Commands.WORLDS.getName();

    boolean result = cloneWorldCommand.matches(new String[]{worldsCommandName});

    assertTrue(result);
  }

  @Test
  void matchesReturnsTrueWhenArgMatchesWorldsCommandCaseInsensitive() {
    boolean result = cloneWorldCommand.matches(
        new String[]{DevCommand.Commands.WORLDS.getName().toUpperCase()});

    assertTrue(result);
  }

  @Test
  void matchesReturnsFalseWhenNoArgsProvided() {
    boolean result = cloneWorldCommand.matches(new String[]{});

    assertFalse(result);
  }

  @Test
  void matchesReturnsFalseWhenMoreThanOneArgProvided() {
    boolean result = cloneWorldCommand.matches(
        new String[]{DevCommand.Commands.WORLDS.getName(), "extra"});

    assertFalse(result);
  }

  @ParameterizedTest
  @ValueSource(strings = {"notworlds", "clone", "world", ""})
  void matchesReturnsFalseWhenArgDoesNotMatchWorldsCommand(String invalidArg) {
    boolean result = cloneWorldCommand.matches(new String[]{invalidArg});

    assertFalse(result);
  }
}