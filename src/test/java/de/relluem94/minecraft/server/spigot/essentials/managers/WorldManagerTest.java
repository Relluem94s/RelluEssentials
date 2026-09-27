package de.relluem94.minecraft.server.spigot.essentials.managers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.exceptions.WorldNotLoadedException;
import de.relluem94.minecraft.server.spigot.essentials.helpers.WorldHelper;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.WorldEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.WorldGroupEntry;
import de.relluem94.minecraft.server.spigot.essentials.persistence.bukkit.BukkitRegistryAdapter;
import de.relluem94.minecraft.server.spigot.essentials.services.PluginMetadataService;
import de.relluem94.minecraft.server.spigot.essentials.services.WorldGroupService;
import java.lang.reflect.Field;
import java.util.Optional;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.WorldType;
import org.bukkit.command.ConsoleCommandSender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class WorldManagerTest {

  @Mock
  private RelluEssentials plugin;

  @Mock
  private ServiceContext serviceContext;

  @Mock
  private WorldGroupService worldGroupService;

  @Mock
  private PluginMetadataService pluginMetadataService;

  @Mock
  private Server server;

  @Mock
  private ConsoleCommandSender consoleCommandSender;

  @Mock
  private World world;

  @Mock
  private WorldGroupEntry worldGroupEntry;

  @Mock
  private WorldEntry worldEntry;

  @Mock
  private BukkitRegistryAdapter bukkitRegistryAdapter;

  private WorldManager worldManager;

  @BeforeEach
  void setUp() {
    worldManager = new WorldManager(bukkitRegistryAdapter);

    lenient().when(plugin.getServiceContext()).thenReturn(serviceContext);
    lenient().when(serviceContext.getWorldGroupService()).thenReturn(worldGroupService);
    lenient().when(serviceContext.getPluginMetadataService()).thenReturn(pluginMetadataService);
    lenient().when(pluginMetadataService.getPlugin()).thenReturn(plugin);
    lenient().when(plugin.getServer()).thenReturn(server);
    lenient().when(server.getConsoleSender()).thenReturn(consoleCommandSender);
  }

  @Test
  void initialServiceContextFieldIsNull() throws Exception {
    Field contextField = WorldManager.class.getDeclaredField("serviceContext");
    contextField.setAccessible(true);

    assertFalse(Optional.ofNullable(contextField.get(worldManager)).isPresent());
  }

  @Test
  void enableSetsServiceContextFromPlugin() throws Exception {
    Multimap<WorldGroupEntry, WorldEntry> worldsMap = ArrayListMultimap.create();
    when(worldGroupService.getWorldsMap()).thenReturn(worldsMap);

    worldManager.enable(plugin);

    Field contextField = WorldManager.class.getDeclaredField("serviceContext");
    contextField.setAccessible(true);
    assertEquals(serviceContext, contextField.get(worldManager));
  }

  @Test
  void enableSendsWorldsSizeMessageToConsole() {
    Multimap<WorldGroupEntry, WorldEntry> worldsMap = ArrayListMultimap.create();
    when(worldGroupService.getWorldsMap()).thenReturn(worldsMap);

    worldManager.enable(plugin);

    verify(consoleCommandSender).sendMessage(anyString(), anyString());
  }

  @Test
  void enableSkipsNullWorldGroupEntry() {
    Multimap<WorldGroupEntry, WorldEntry> worldsMap = ArrayListMultimap.create();
    worldsMap.put(null, worldEntry);
    when(worldGroupService.getWorldsMap()).thenReturn(worldsMap);

    try (MockedStatic<WorldHelper> worldHelperMock = mockStatic(WorldHelper.class)) {
      worldManager.enable(plugin);

      worldHelperMock.verify(() -> WorldHelper.worldExists(anyString()), never());
    }
  }

  @Test
  void enableSkipsNullWorldEntry() {
    Multimap<WorldGroupEntry, WorldEntry> worldsMap = ArrayListMultimap.create();
    worldsMap.put(worldGroupEntry, null);
    when(worldGroupService.getWorldsMap()).thenReturn(worldsMap);

    try (MockedStatic<WorldHelper> worldHelperMock = mockStatic(WorldHelper.class)) {
      worldManager.enable(plugin);

      worldHelperMock.verify(() -> WorldHelper.worldExists(anyString()), never());
    }
  }

  @Test
  void enableCreatesWorldWhenWorldDoesNotExist() {
    when(worldEntry.getName()).thenReturn("testworld");

    Multimap<WorldGroupEntry, WorldEntry> worldsMap = ArrayListMultimap.create();
    worldsMap.put(worldGroupEntry, worldEntry);
    when(worldGroupService.getWorldsMap()).thenReturn(worldsMap);

    try (MockedStatic<WorldHelper> worldHelperMock = mockStatic(WorldHelper.class)) {
      worldHelperMock.when(() -> WorldHelper.worldExists("testworld")).thenReturn(false);

      worldManager.enable(plugin);

      worldHelperMock.verify(
          () -> WorldHelper.createWorld(eq("testworld"), any(WorldType.class),
              any(World.Environment.class), eq(false)));
    }
  }

  @Test
  void enableDoesNotLoadWorldWhenAlreadyLoaded() {
    when(worldEntry.getName()).thenReturn("testworld");
    when(server.getWorld("testworld")).thenReturn(world);

    Multimap<WorldGroupEntry, WorldEntry> worldsMap = ArrayListMultimap.create();
    worldsMap.put(worldGroupEntry, worldEntry);
    when(worldGroupService.getWorldsMap()).thenReturn(worldsMap);

    try (MockedStatic<WorldHelper> worldHelperMock = mockStatic(WorldHelper.class)) {
      worldHelperMock.when(() -> WorldHelper.worldExists("testworld")).thenReturn(true);

      worldManager.enable(plugin);

      worldHelperMock.verify(() -> WorldHelper.loadWorld(anyString()), never());
    }
  }

  @Test
  void enableDetectsNetherEnvironmentBySuffix() {
    when(worldEntry.getName()).thenReturn("testworld_nether");

    Multimap<WorldGroupEntry, WorldEntry> worldsMap = ArrayListMultimap.create();
    worldsMap.put(worldGroupEntry, worldEntry);
    when(worldGroupService.getWorldsMap()).thenReturn(worldsMap);

    try (MockedStatic<WorldHelper> worldHelperMock = mockStatic(WorldHelper.class)) {
      worldHelperMock.when(() -> WorldHelper.worldExists("testworld_nether")).thenReturn(false);

      worldManager.enable(plugin);

      worldHelperMock.verify(
          () -> WorldHelper.createWorld(eq("testworld_nether"), any(WorldType.class),
              eq(World.Environment.NETHER), eq(false)));
    }
  }

  @Test
  void enableDetectsTheEndEnvironmentBySuffix() {
    when(worldEntry.getName()).thenReturn("testworld_the_end");

    Multimap<WorldGroupEntry, WorldEntry> worldsMap = ArrayListMultimap.create();
    worldsMap.put(worldGroupEntry, worldEntry);
    when(worldGroupService.getWorldsMap()).thenReturn(worldsMap);

    try (MockedStatic<WorldHelper> worldHelperMock = mockStatic(WorldHelper.class)) {
      worldHelperMock.when(() -> WorldHelper.worldExists("testworld_the_end")).thenReturn(false);

      worldManager.enable(plugin);

      worldHelperMock.verify(
          () -> WorldHelper.createWorld(eq("testworld_the_end"), any(WorldType.class),
              eq(World.Environment.THE_END), eq(false)));
    }
  }

  @Test
  void enableDetectsCustomEnvironmentBySuffix() {
    when(worldEntry.getName()).thenReturn("testworld_custom");

    Multimap<WorldGroupEntry, WorldEntry> worldsMap = ArrayListMultimap.create();
    worldsMap.put(worldGroupEntry, worldEntry);
    when(worldGroupService.getWorldsMap()).thenReturn(worldsMap);

    try (MockedStatic<WorldHelper> worldHelperMock = mockStatic(WorldHelper.class)) {
      worldHelperMock.when(() -> WorldHelper.worldExists("testworld_custom")).thenReturn(false);

      worldManager.enable(plugin);

      worldHelperMock.verify(
          () -> WorldHelper.createWorld(eq("testworld_custom"), any(WorldType.class),
              eq(World.Environment.CUSTOM), eq(false)));
    }
  }

  @Test
  void enableUsesNormalEnvironmentWhenNoSuffixMatches() {
    when(worldEntry.getName()).thenReturn("testworld");

    Multimap<WorldGroupEntry, WorldEntry> worldsMap = ArrayListMultimap.create();
    worldsMap.put(worldGroupEntry, worldEntry);
    when(worldGroupService.getWorldsMap()).thenReturn(worldsMap);

    try (MockedStatic<WorldHelper> worldHelperMock = mockStatic(WorldHelper.class)) {
      worldHelperMock.when(() -> WorldHelper.worldExists("testworld")).thenReturn(false);

      worldManager.enable(plugin);

      worldHelperMock.verify(
          () -> WorldHelper.createWorld(eq("testworld"), any(WorldType.class),
              eq(World.Environment.NORMAL), eq(false)));
    }
  }

  @Test
  void enableDoesNotSetGameRulesWhenWorldIsNullAfterLoad() {
    when(worldEntry.getName()).thenReturn("testworld");
    when(server.getWorld("testworld")).thenReturn(null);

    Multimap<WorldGroupEntry, WorldEntry> worldsMap = ArrayListMultimap.create();
    worldsMap.put(worldGroupEntry, worldEntry);
    when(worldGroupService.getWorldsMap()).thenReturn(worldsMap);

    try (MockedStatic<WorldHelper> worldHelperMock = mockStatic(WorldHelper.class)) {
      worldHelperMock.when(() -> WorldHelper.worldExists("testworld")).thenReturn(true);

      worldManager.enable(plugin);

      verify(world, never()).setGameRule(any(), any());
    }
  }

  @Test
  void disableUnloadsAllWorldsWithSave() throws Exception {
    when(worldEntry.getName()).thenReturn("testworld");

    Multimap<WorldGroupEntry, WorldEntry> worldsMap = ArrayListMultimap.create();
    worldsMap.put(worldGroupEntry, worldEntry);
    when(worldGroupService.getWorldsMap()).thenReturn(worldsMap);

    Field contextField = WorldManager.class.getDeclaredField("serviceContext");
    contextField.setAccessible(true);
    contextField.set(worldManager, serviceContext);

    try (MockedStatic<WorldHelper> worldHelperMock = mockStatic(WorldHelper.class)) {
      worldManager.disable(plugin);

      worldHelperMock.verify(() -> WorldHelper.unloadWorld("testworld", true));
    }
  }

  @Test
  void disableSkipsNullWorldGroupEntry() throws Exception {
    Multimap<WorldGroupEntry, WorldEntry> worldsMap = ArrayListMultimap.create();
    worldsMap.put(null, worldEntry);
    when(worldGroupService.getWorldsMap()).thenReturn(worldsMap);

    Field contextField = WorldManager.class.getDeclaredField("serviceContext");
    contextField.setAccessible(true);
    contextField.set(worldManager, serviceContext);

    try (MockedStatic<WorldHelper> worldHelperMock = mockStatic(WorldHelper.class)) {
      worldManager.disable(plugin);

      worldHelperMock.verify(() -> WorldHelper.unloadWorld(anyString(), anyBoolean()), never());
    }
  }

  @Test
  void disableSkipsNullWorldEntry() throws Exception {
    Multimap<WorldGroupEntry, WorldEntry> worldsMap = ArrayListMultimap.create();
    worldsMap.put(worldGroupEntry, null);
    when(worldGroupService.getWorldsMap()).thenReturn(worldsMap);

    Field contextField = WorldManager.class.getDeclaredField("serviceContext");
    contextField.setAccessible(true);
    contextField.set(worldManager, serviceContext);

    try (MockedStatic<WorldHelper> worldHelperMock = mockStatic(WorldHelper.class)) {
      worldManager.disable(plugin);

      worldHelperMock.verify(() -> WorldHelper.unloadWorld(anyString(), anyBoolean()), never());
    }
  }

  @Test
  void disableLogsWarningWhenWorldNotLoadedExceptionIsThrown() throws Exception {
    Logger logger = Logger.getLogger("de.relluem94.minecraft.server.spigot.essentials.managers.WorldManager");
    logger.setUseParentHandlers(false);
    logger.setLevel(Level.OFF);

    when(worldEntry.getName()).thenReturn("testworld");

    Multimap<WorldGroupEntry, WorldEntry> worldsMap = ArrayListMultimap.create();
    worldsMap.put(worldGroupEntry, worldEntry);
    when(worldGroupService.getWorldsMap()).thenReturn(worldsMap);

    Field contextField = WorldManager.class.getDeclaredField("serviceContext");
    contextField.setAccessible(true);
    contextField.set(worldManager, serviceContext);

    try (MockedStatic<WorldHelper> worldHelperMock = mockStatic(WorldHelper.class)) {
      worldHelperMock.when(() -> WorldHelper.unloadWorld("testworld", true))
          .thenThrow(new WorldNotLoadedException("testworld"));

      worldManager.disable(plugin);

      worldHelperMock.verify(() -> WorldHelper.unloadWorld("testworld", true));
    }
  }

  @Test
  void disableHandlesEmptyWorldsMap() throws Exception {
    Multimap<WorldGroupEntry, WorldEntry> worldsMap = ArrayListMultimap.create();
    when(worldGroupService.getWorldsMap()).thenReturn(worldsMap);

    Field contextField = WorldManager.class.getDeclaredField("serviceContext");
    contextField.setAccessible(true);
    contextField.set(worldManager, serviceContext);

    try (MockedStatic<WorldHelper> worldHelperMock = mockStatic(WorldHelper.class)) {
      worldManager.disable(plugin);

      worldHelperMock.verify(() -> WorldHelper.unloadWorld(anyString(), anyBoolean()), never());
    }
  }

  @Test
  void enableSetsGameRulesAfterLoadingWorld() {
    when(worldEntry.getName()).thenReturn("testworld");
    when(server.getWorld("testworld")).thenReturn(null).thenReturn(world);

    Multimap<WorldGroupEntry, WorldEntry> worldsMap = ArrayListMultimap.create();
    worldsMap.put(worldGroupEntry, worldEntry);
    when(worldGroupService.getWorldsMap()).thenReturn(worldsMap);

    try (MockedStatic<WorldHelper> worldHelperMock = mockStatic(WorldHelper.class)) {
      worldHelperMock.when(() -> WorldHelper.worldExists("testworld")).thenReturn(true);

      worldManager.enable(plugin);

      verify(bukkitRegistryAdapter).applyGameRule(world, "fireDamage", false);
      verify(bukkitRegistryAdapter).applyGameRule(world, "doMobSpawning", false);
      verify(bukkitRegistryAdapter).applyGameRule(world, "mobGriefing", false);
      verify(bukkitRegistryAdapter).applyGameRule(world, "doWeatherCycle", false);
    }
  }

  @Test
  void enableCreatesLobbyWorldWithFixedSeedAndSetsSpawnAndGameRules() {
    when(worldEntry.getName()).thenReturn("lobby");
    when(server.getWorld("lobby")).thenReturn(world);

    Multimap<WorldGroupEntry, WorldEntry> worldsMap = ArrayListMultimap.create();
    worldsMap.put(worldGroupEntry, worldEntry);
    when(worldGroupService.getWorldsMap()).thenReturn(worldsMap);

    try (MockedStatic<WorldHelper> worldHelperMock = mockStatic(WorldHelper.class)) {
      worldHelperMock.when(() -> WorldHelper.worldExists("lobby")).thenReturn(false);

      worldManager.enable(plugin);

      worldHelperMock.verify(
          () -> WorldHelper.createWorld(eq("lobby"), eq(WorldType.NORMAL),
              eq(World.Environment.NORMAL), eq(false), eq(6203818585396731238L)));
      verify(bukkitRegistryAdapter).applyGameRule(world, "fireDamage", false);
      verify(bukkitRegistryAdapter).applyGameRule(world, "doMobSpawning", false);
      verify(bukkitRegistryAdapter).applyGameRule(world, "mobGriefing", false);
      verify(bukkitRegistryAdapter).applyGameRule(world, "doWeatherCycle", false);
    }
  }

  @Test
  void enableLoadsWorldWhenWorldExistsButNotLoaded() {
    when(worldEntry.getName()).thenReturn("testworld");
    when(server.getWorld("testworld")).thenReturn(null).thenReturn(world);

    Multimap<WorldGroupEntry, WorldEntry> worldsMap = ArrayListMultimap.create();
    worldsMap.put(worldGroupEntry, worldEntry);
    when(worldGroupService.getWorldsMap()).thenReturn(worldsMap);

    try (MockedStatic<WorldHelper> worldHelperMock = mockStatic(WorldHelper.class)) {
      worldHelperMock.when(() -> WorldHelper.worldExists("testworld")).thenReturn(true);

      worldManager.enable(plugin);

      worldHelperMock.verify(() -> WorldHelper.loadWorld("testworld"));
    }
  }


  @Test
  void enableDoesNotSetSpawnLocationWhenLobbyWorldIsNull() {
    when(worldEntry.getName()).thenReturn("lobby");
    when(server.getWorld("lobby")).thenReturn(null);

    Multimap<WorldGroupEntry, WorldEntry> worldsMap = ArrayListMultimap.create();
    worldsMap.put(worldGroupEntry, worldEntry);
    when(worldGroupService.getWorldsMap()).thenReturn(worldsMap);

    try (MockedStatic<WorldHelper> worldHelperMock = mockStatic(WorldHelper.class)) {
      worldHelperMock.when(() -> WorldHelper.worldExists("lobby")).thenReturn(false);

      worldManager.enable(plugin);

      verify(world, never()).setSpawnLocation(anyInt(), anyInt(), anyInt());
    }
  }

  @ParameterizedTest
  @MethodSource("lobbySpawnLocationProvider")
  void enableSetsCorrectLobbySpawnLocationForRandomValue(int randomValue, int expectedX, int expectedY, int expectedZ) throws Exception {
    when(worldEntry.getName()).thenReturn("lobby");
    when(server.getWorld("lobby")).thenReturn(world);

    Multimap<WorldGroupEntry, WorldEntry> worldsMap = ArrayListMultimap.create();
    worldsMap.put(worldGroupEntry, worldEntry);
    when(worldGroupService.getWorldsMap()).thenReturn(worldsMap);

    Random fixedRandom = new Random() {
      @Override
      public int nextInt(int bound) {
        return randomValue - 1;
      }
    };

    Field randomField = WorldManager.class.getDeclaredField("random");
    randomField.setAccessible(true);
    randomField.set(worldManager, fixedRandom);

    try (MockedStatic<WorldHelper> worldHelperMock = mockStatic(WorldHelper.class)) {
      worldHelperMock.when(() -> WorldHelper.worldExists("lobby")).thenReturn(false);

      worldManager.enable(plugin);

      verify(world).setSpawnLocation(expectedX, expectedY, expectedZ);
    }
  }

  private static Stream<Arguments> lobbySpawnLocationProvider() {
    return Stream.of(
        Arguments.of(1, 140, 143, 188),
        Arguments.of(2, -226, 115, -5777),
        Arguments.of(3, 718, 136, -4215),
        Arguments.of(4, 497, 68, -2800),
        Arguments.of(5, 141, 143, 188)
    );
  }
}