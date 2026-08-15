package de.relluem94.minecraft.server.spigot.essentials.helpers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.enums.ProtectionFlags;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandsEnum;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.GroupEntry;
import de.relluem94.minecraft.server.spigot.essentials.persistence.dao.GroupDao;
import de.relluem94.minecraft.server.spigot.essentials.persistence.jdbc.QueryExecutor;
import de.relluem94.minecraft.server.spigot.essentials.registries.GroupRegistry;
import de.relluem94.minecraft.server.spigot.essentials.registries.PlayerRegistry;
import de.relluem94.minecraft.server.spigot.essentials.repositories.GroupRepository;
import de.relluem94.minecraft.server.spigot.essentials.services.GroupService;
import de.relluem94.minecraft.server.spigot.essentials.services.PlayerService;
import de.relluem94.minecraft.server.spigot.essentials.services.WarpService;
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
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TabCompleterHelperTest {

  private static RelluEssentials relluEssentials;
  private static PlayerService playerService;
  private static WarpService warpService;

  @Mock
  private Player player;

  @Mock
  private World world;

  @Mock
  private static QueryExecutor queryExecutor;

  @BeforeAll
  static void setUp() throws NoSuchFieldException, IllegalAccessException {
    relluEssentials = mock(RelluEssentials.class);
    playerService = mock(PlayerService.class);
    warpService = mock(WarpService.class);
    queryExecutor = mock(QueryExecutor.class);

    Field instanceField = RelluEssentials.class.getDeclaredField("instance");
    instanceField.setAccessible(true);
    instanceField.set(null, relluEssentials);

    GroupDao groupDao = new GroupDao(queryExecutor);
    GroupRepository groupRepository = new GroupRepository(groupDao);
    GroupRegistry groupRegistry = new GroupRegistry(groupRepository);
    GroupService groupService = new GroupService(groupRegistry, groupRepository);
    groupService.setPlayerRegistry(new PlayerRegistry());

    ServiceContext serviceContext = mock(ServiceContext.class);
    when(serviceContext.getGroupService()).thenReturn(groupService);
    when(serviceContext.getPlayerService()).thenReturn(playerService);
    when(serviceContext.getWarpService()).thenReturn(warpService);
    when(relluEssentials.getServiceContext()).thenReturn(serviceContext);
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
  void getGroupsReturnsEmptyListWhenNoGroupsExist()
      throws NoSuchFieldException, IllegalAccessException {
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