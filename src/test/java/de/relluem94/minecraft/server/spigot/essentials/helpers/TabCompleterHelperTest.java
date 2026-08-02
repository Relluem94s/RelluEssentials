package de.relluem94.minecraft.server.spigot.essentials.helpers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.enums.ProtectionFlags;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandsEnum;
import de.relluem94.minecraft.server.spigot.essentials.model.pojo.GroupEntry;
import de.relluem94.minecraft.server.spigot.essentials.model.pojo.LocationEntry;
import de.relluem94.minecraft.server.spigot.essentials.model.pojo.PlayerEntry;
import de.relluem94.minecraft.server.spigot.essentials.registry.PlayerRegistry;
import de.relluem94.minecraft.server.spigot.essentials.repository.WarpRepository;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.WeatherType;
import org.bukkit.World;
import org.bukkit.WorldType;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TabCompleterHelperTest {

  @Mock
  private RelluEssentials relluEssentials;

  @Mock
  private PlayerRegistry playerRegistry;

  @Mock
  private WarpRepository warpRepository;

  @Mock
  private Player player;

  @Mock
  private World world;

  @BeforeEach
  void setUp() throws NoSuchFieldException, IllegalAccessException {
    Field instanceField = RelluEssentials.class.getDeclaredField("instance");
    instanceField.setAccessible(true);
    instanceField.set(null, relluEssentials);
  }

  @Test
  void constructorThrowsIllegalStateException() throws NoSuchMethodException {
    Constructor<TabCompleterHelper> constructor = TabCompleterHelper.class.getDeclaredConstructor();
    constructor.setAccessible(true);

    InvocationTargetException thrownException = assertThrows(
        InvocationTargetException.class,
        constructor::newInstance
    );

    assertInstanceOf(IllegalStateException.class, thrownException.getCause());
  }

  @Test
  void getOnlinePlayersReturnsEmptyListWhenNoPlayersOnline() {
    try (MockedStatic<Bukkit> bukkit = mockStatic(Bukkit.class)) {
      bukkit.when(Bukkit::getOnlinePlayers).thenReturn(new ArrayList<>());

      List<String> result = TabCompleterHelper.getOnlinePlayers();

      assertTrue(result.isEmpty());
    }
  }

  @Test
  void getOnlinePlayersReturnsPlayerNames() {
    Player firstPlayer = mock(Player.class);
    Player secondPlayer = mock(Player.class);
    when(firstPlayer.getName()).thenReturn("PlayerOne");
    when(secondPlayer.getName()).thenReturn("PlayerTwo");

    try (MockedStatic<Bukkit> bukkit = mockStatic(Bukkit.class)) {
      bukkit.when(Bukkit::getOnlinePlayers).thenReturn(List.of(firstPlayer, secondPlayer));

      List<String> result = TabCompleterHelper.getOnlinePlayers();

      assertEquals(2, result.size());
      assertTrue(result.contains("PlayerOne"));
      assertTrue(result.contains("PlayerTwo"));
    }
  }

  @Test
  void getProtectionFlagsReturnsAllFlags() {
    List<String> result = TabCompleterHelper.getProtectionFlags();

    assertEquals(ProtectionFlags.values().length, result.size());
    for (ProtectionFlags flag : ProtectionFlags.values()) {
      assertTrue(result.contains(flag.toString()));
    }
  }

  @Test
  void getCommandsReturnsEmptyListWhenNoCommandsGiven() {
    List<String> result = TabCompleterHelper.getCommands(new CommandsEnum[]{});

    assertTrue(result.isEmpty());
  }

  @Test
  void getCommandsReturnsCommandNames() {
    CommandsEnum firstCommand = mock(CommandsEnum.class);
    CommandsEnum secondCommand = mock(CommandsEnum.class);
    when(firstCommand.getName()).thenReturn("fly");
    when(secondCommand.getName()).thenReturn("home");

    List<String> result = TabCompleterHelper.getCommands(
        new CommandsEnum[]{firstCommand, secondCommand});

    assertEquals(2, result.size());
    assertTrue(result.contains("fly"));
    assertTrue(result.contains("home"));
  }

  @Test
  void getWorldsReturnsEmptyListWhenNoWorldsExist() {
    try (MockedStatic<Bukkit> bukkit = mockStatic(Bukkit.class)) {
      bukkit.when(Bukkit::getWorlds).thenReturn(new ArrayList<>());

      List<String> result = TabCompleterHelper.getWorlds();

      assertTrue(result.isEmpty());
    }
  }

  @Test
  void getWorldsReturnsWorldNames() {
    World firstWorld = mock(World.class);
    World secondWorld = mock(World.class);
    when(firstWorld.getName()).thenReturn("world");
    when(secondWorld.getName()).thenReturn("world_nether");

    try (MockedStatic<Bukkit> bukkit = mockStatic(Bukkit.class)) {
      bukkit.when(Bukkit::getWorlds).thenReturn(List.of(firstWorld, secondWorld));

      List<String> result = TabCompleterHelper.getWorlds();

      assertEquals(2, result.size());
      assertTrue(result.contains("world"));
      assertTrue(result.contains("world_nether"));
    }
  }

  @Test
  void getHomesReturnsEmptyListWhenPlayerHasNoHomesOrDeaths() {
    PlayerEntry playerEntry = new PlayerEntry();
    playerEntry.setHomes(new ArrayList<>());
    playerEntry.setDeaths(new ArrayList<>());

    when(relluEssentials.getPlayerRegistry()).thenReturn(playerRegistry);
    when(playerRegistry.getPlayerEntry(player)).thenReturn(playerEntry);

    List<String> result = TabCompleterHelper.getHomes(player);

    assertTrue(result.isEmpty());
  }

  @Test
  void getHomesReturnsHomeAndDeathLocationNames() {
    LocationEntry homeEntry = new LocationEntry();
    homeEntry.setLocationName("myHome");

    LocationEntry deathEntry = new LocationEntry();
    deathEntry.setLocationName("death_1");

    PlayerEntry playerEntry = new PlayerEntry();
    playerEntry.setHomes(List.of(homeEntry));
    playerEntry.setDeaths(List.of(deathEntry));

    when(relluEssentials.getPlayerRegistry()).thenReturn(playerRegistry);
    when(playerRegistry.getPlayerEntry(player)).thenReturn(playerEntry);

    List<String> result = TabCompleterHelper.getHomes(player);

    assertEquals(2, result.size());
    assertTrue(result.contains("myHome"));
    assertTrue(result.contains("death_1"));
  }

    @Test
    void getGroupsReturnsEmptyListWhenNoGroupsExist() throws NoSuchFieldException, IllegalAccessException {
        List<String> result = TabCompleterHelper.getGroups(List.of());

        assertTrue(result.isEmpty());
    }

    @Test
    void getGroupsReturnsGroupNames() throws NoSuchFieldException, IllegalAccessException {
        GroupEntry adminGroup = new GroupEntry();
        adminGroup.setName("admin");

        GroupEntry userGroup = new GroupEntry();
        userGroup.setName("user");

        List<String> result = TabCompleterHelper.getGroups(List.of(adminGroup, userGroup));

        assertEquals(2, result.size());
        assertTrue(result.contains("admin"));
        assertTrue(result.contains("user"));
    }

    @Test
    void getWarpsReturnsEmptyListWhenNoWarpsExistForWorld() {
        when(relluEssentials.getWarpRepository()).thenReturn(warpRepository);
        when(warpRepository.findByWorld(world)).thenReturn(new ArrayList<>());

    List<String> result = TabCompleterHelper.getWarps(world);

    assertTrue(result.isEmpty());
  }

  @Test
  void getWarpsReturnsWarpNames() {
    LocationEntry warpEntry = new LocationEntry();
    warpEntry.setLocationName("spawn");

    when(relluEssentials.getWarpRepository()).thenReturn(warpRepository);
    when(warpRepository.findByWorld(world)).thenReturn(List.of(warpEntry));

    List<String> result = TabCompleterHelper.getWarps(world);

    assertEquals(1, result.size());
    assertTrue(result.contains("spawn"));
  }

  @Test
  void getWorldTypesReturnsAllWorldTypes() {
    List<String> result = TabCompleterHelper.getWorldTypes();

    assertEquals(WorldType.values().length, result.size());
    for (WorldType worldType : WorldType.values()) {
      assertTrue(result.contains(worldType.getName()));
    }
  }

  @Test
  void getWorldEnvironmentTypesReturnsAllEnvironments() {
    List<String> result = TabCompleterHelper.getWorldEnvironmentTypes();

    assertEquals(World.Environment.values().length, result.size());
    for (World.Environment environment : World.Environment.values()) {
      assertTrue(result.contains(environment.name()));
    }
  }

  @Test
  void getWeatherTypesReturnsAllWeatherTypes() {
    List<String> result = TabCompleterHelper.getWeatherTypes();

    assertEquals(WeatherType.values().length, result.size());
    for (WeatherType weatherType : WeatherType.values()) {
      assertTrue(result.contains(weatherType.name()));
    }
  }
}