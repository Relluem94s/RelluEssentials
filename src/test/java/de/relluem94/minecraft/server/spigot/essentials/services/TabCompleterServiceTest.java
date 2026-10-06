package de.relluem94.minecraft.server.spigot.essentials.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.enums.ProtectionFlags;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandsEnum;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.GroupEntry;
import de.relluem94.minecraft.server.spigot.essentials.persistence.bukkit.BukkitRegistryAdapter;
import de.relluem94.minecraft.server.spigot.essentials.persistence.dao.GroupDao;
import de.relluem94.minecraft.server.spigot.essentials.persistence.jdbc.QueryExecutor;
import de.relluem94.minecraft.server.spigot.essentials.registries.GroupRegistry;
import de.relluem94.minecraft.server.spigot.essentials.registries.PlayerRegistry;
import de.relluem94.minecraft.server.spigot.essentials.repositories.GroupRepository;
import java.lang.reflect.Field;
import java.util.List;
import java.util.function.Predicate;
import org.bukkit.Material;
import org.bukkit.WeatherType;
import org.bukkit.World;
import org.bukkit.WorldType;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TabCompleterServiceTest {

  @Mock
  private static QueryExecutor queryExecutor;
  private static TabCompleterService tabCompleterService;
  private static BukkitRegistryAdapter bukkitRegistryAdapter;
  private static ServerService serverService;

  @BeforeAll
  static void setUp() throws NoSuchFieldException, IllegalAccessException {
    RelluEssentials relluEssentials = mock(RelluEssentials.class);
    PlayerService playerService = mock(PlayerService.class);
    WarpService warpService = mock(WarpService.class);
    serverService = mock(ServerService.class);
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
    when(serviceContext.getServerService()).thenReturn(serverService);
    when(relluEssentials.getServiceContext()).thenReturn(serviceContext);

    bukkitRegistryAdapter = mock(BukkitRegistryAdapter.class);
    tabCompleterService = new TabCompleterService(bukkitRegistryAdapter, serviceContext);
  }

  @Test
  void constructorCreatesInstanceSuccessfully() {
    BukkitRegistryAdapter localBukkitRegistryAdapter = mock(BukkitRegistryAdapter.class);
    ServiceContext localServiceContext = mock(ServiceContext.class);

    TabCompleterService instance = new TabCompleterService(localBukkitRegistryAdapter, localServiceContext);

    assertInstanceOf(TabCompleterService.class, instance);
  }

  @Test
  void getProtectionFlagsReturnsAllFlags() {
    List<String> result = tabCompleterService.getProtectionFlags();

    assertEquals(ProtectionFlags.values().length, result.size());
    for (ProtectionFlags flag : ProtectionFlags.values()) {
      assertTrue(result.contains(flag.toString()));
    }
  }

  @Test
  void getCommandsReturnsEmptyListWhenNoCommandsGiven() {
    List<String> result = tabCompleterService.getCommands(new CommandsEnum[]{});

    assertTrue(result.isEmpty());
  }

  @Test
  void getCommandsReturnsCommandNames() {
    CommandsEnum firstCommand = mock(CommandsEnum.class);
    CommandsEnum secondCommand = mock(CommandsEnum.class);
    when(firstCommand.getName()).thenReturn("fly");
    when(secondCommand.getName()).thenReturn("home");

    List<String> result = tabCompleterService.getCommands(
        new CommandsEnum[]{firstCommand, secondCommand});

    assertEquals(2, result.size());
    assertTrue(result.contains("fly"));
    assertTrue(result.contains("home"));
  }

  @Test
  void getGroupsReturnsEmptyListWhenNoGroupsExist() {
    List<String> result = tabCompleterService.getGroups(List.of());

    assertTrue(result.isEmpty());
  }

  @Test
  void getGroupsReturnsGroupNames() {
    GroupEntry adminGroup = new GroupEntry();
    adminGroup.setName("admin");

    GroupEntry userGroup = new GroupEntry();
    userGroup.setName("user");

    List<String> result = tabCompleterService.getGroups(List.of(adminGroup, userGroup));

    assertEquals(2, result.size());
    assertTrue(result.contains("admin"));
    assertTrue(result.contains("user"));
  }

  @Test
  void getWorldTypesReturnsAllWorldTypes() {
    List<String> result = tabCompleterService.getWorldTypes();

    assertEquals(WorldType.values().length, result.size());
    for (WorldType worldType : WorldType.values()) {
      assertTrue(result.contains(worldType.getName()));
    }
  }

  @Test
  void getWorldEnvironmentTypesReturnsAllEnvironments() {
    List<String> result = tabCompleterService.getWorldEnvironmentTypes();

    assertEquals(World.Environment.values().length, result.size());
    for (World.Environment environment : World.Environment.values()) {
      assertTrue(result.contains(environment.name()));
    }
  }

  @Test
  void getWeatherTypesReturnsAllWeatherTypes() {
    List<String> result = tabCompleterService.getWeatherTypes();

    assertEquals(WeatherType.values().length, result.size());
    for (WeatherType weatherType : WeatherType.values()) {
      assertTrue(result.contains(weatherType.name()));
    }
  }

  @Test
  void getMaterialsWithNullFilterReturnsSolidNonLegacyBlocks() {
    Material solidBlock = mock(Material.class);
    when(solidBlock.name()).thenReturn("STONE");
    when(solidBlock.isBlock()).thenReturn(true);
    when(solidBlock.isSolid()).thenReturn(true);

    when(bukkitRegistryAdapter.getAllMaterials()).thenReturn(List.of(solidBlock));

    List<String> result = tabCompleterService.getMaterials(null);

    assertEquals(1, result.size());
    assertTrue(result.contains("STONE"));
  }

  @Test
  void getMaterialsExcludesLegacyMaterials() {
    Material legacyMaterial = mock(Material.class);
    when(legacyMaterial.name()).thenReturn("LEGACY_STONE");

    when(bukkitRegistryAdapter.getAllMaterials()).thenReturn(List.of(legacyMaterial));

    List<String> result = tabCompleterService.getMaterials(null);

    assertTrue(result.isEmpty());
  }

  @Test
  void getMaterialsExcludesNonBlockMaterials() {
    Material nonBlock = mock(Material.class);
    when(nonBlock.name()).thenReturn("STICK");
    when(nonBlock.isBlock()).thenReturn(false);

    when(bukkitRegistryAdapter.getAllMaterials()).thenReturn(List.of(nonBlock));

    List<String> result = tabCompleterService.getMaterials(null);

    assertTrue(result.isEmpty());
  }

  @Test
  void getMaterialsExcludesNonSolidBlocks() {
    Material nonSolidBlock = mock(Material.class);
    when(nonSolidBlock.name()).thenReturn("WATER");
    when(nonSolidBlock.isBlock()).thenReturn(true);
    when(nonSolidBlock.isSolid()).thenReturn(false);

    when(bukkitRegistryAdapter.getAllMaterials()).thenReturn(List.of(nonSolidBlock));

    List<String> result = tabCompleterService.getMaterials(null);

    assertTrue(result.isEmpty());
  }

  @Test
  void getMaterialsWithFilterMatchingMaterialNameReturnsMatchingMaterial() {
    Material solidBlock = mock(Material.class);
    when(solidBlock.name()).thenReturn("STONE");
    when(solidBlock.isBlock()).thenReturn(true);
    when(solidBlock.isSolid()).thenReturn(true);

    when(bukkitRegistryAdapter.getAllMaterials()).thenReturn(List.of(solidBlock));

    List<String> result = tabCompleterService.getMaterials("sto");

    assertEquals(1, result.size());
    assertTrue(result.contains("STONE"));
  }

  @Test
  void getMaterialsWithFilterNotMatchingMaterialNameReturnsEmptyList() {
    Material solidBlock = mock(Material.class);
    when(solidBlock.name()).thenReturn("STONE");
    when(solidBlock.isBlock()).thenReturn(true);
    when(solidBlock.isSolid()).thenReturn(true);

    when(bukkitRegistryAdapter.getAllMaterials()).thenReturn(List.of(solidBlock));

    List<String> result = tabCompleterService.getMaterials("dirt");

    assertTrue(result.isEmpty());
  }

  @Test
  void getMaterialsWithFilterIsCaseInsensitive() {
    Material solidBlock = mock(Material.class);
    when(solidBlock.name()).thenReturn("STONE");
    when(solidBlock.isBlock()).thenReturn(true);
    when(solidBlock.isSolid()).thenReturn(true);

    when(bukkitRegistryAdapter.getAllMaterials()).thenReturn(List.of(solidBlock));

    List<String> result = tabCompleterService.getMaterials("STO");

    assertEquals(1, result.size());
    assertTrue(result.contains("STONE"));
  }

  @Test
  void getMaterialsReturnsEmptyListWhenNoMaterialsAvailable() {
    when(bukkitRegistryAdapter.getAllMaterials()).thenReturn(List.of());

    List<String> result = tabCompleterService.getMaterials(null);

    assertTrue(result.isEmpty());
  }

  @Test
  void getMaterialsByPredicateWithNullFilterReturnsAllMatchingMaterials() {
    Material matchingMaterial = mock(Material.class);
    when(matchingMaterial.name()).thenReturn("STONE");

    when(bukkitRegistryAdapter.getAllMaterials()).thenReturn(List.of(matchingMaterial));

    Predicate<Material> alwaysTrue = _ -> true;
    List<String> result = tabCompleterService.getMaterialsByPredicate(alwaysTrue, null);

    assertEquals(1, result.size());
    assertTrue(result.contains("STONE"));
  }

  @Test
  void getMaterialsByPredicateWithFilterMatchingPrefixReturnsMaterial() {
    Material matchingMaterial = mock(Material.class);
    when(matchingMaterial.name()).thenReturn("STONE");

    when(bukkitRegistryAdapter.getAllMaterials()).thenReturn(List.of(matchingMaterial));

    Predicate<Material> alwaysTrue = _ -> true;
    List<String> result = tabCompleterService.getMaterialsByPredicate(alwaysTrue, "STO");

    assertEquals(1, result.size());
    assertTrue(result.contains("STONE"));
  }

  @Test
  void getMaterialsByPredicateWithFilterNotMatchingPrefixReturnsEmptyList() {
    Material matchingMaterial = mock(Material.class);
    when(matchingMaterial.name()).thenReturn("STONE");

    when(bukkitRegistryAdapter.getAllMaterials()).thenReturn(List.of(matchingMaterial));

    Predicate<Material> alwaysTrue = _ -> true;
    List<String> result = tabCompleterService.getMaterialsByPredicate(alwaysTrue, "DIRT");

    assertTrue(result.isEmpty());
  }

  @Test
  void getMaterialsByPredicateExcludesMaterialsNotMatchingPredicate() {
    Material matchingMaterial = mock(Material.class);

    when(bukkitRegistryAdapter.getAllMaterials()).thenReturn(List.of(matchingMaterial));

    Predicate<Material> alwaysFalse = _ -> false;
    List<String> result = tabCompleterService.getMaterialsByPredicate(alwaysFalse, null);

    assertTrue(result.isEmpty());
  }

  @Test
  void getMaterialsByPredicateReturnsSortedList() {
    Material materialC = mock(Material.class);
    when(materialC.name()).thenReturn("COBBLESTONE");

    Material materialA = mock(Material.class);
    when(materialA.name()).thenReturn("ANDESITE");

    Material materialS = mock(Material.class);
    when(materialS.name()).thenReturn("STONE");

    when(bukkitRegistryAdapter.getAllMaterials()).thenReturn(List.of(materialC, materialS, materialA));

    Predicate<Material> alwaysTrue = _ -> true;
    List<String> result = tabCompleterService.getMaterialsByPredicate(alwaysTrue, null);

    assertEquals(List.of("ANDESITE", "COBBLESTONE", "STONE"), result);
  }

  @Test
  void getMaterialsByPredicateWithFilterConvertsToUpperCase() {
    Material matchingMaterial = mock(Material.class);
    when(matchingMaterial.name()).thenReturn("STONE");

    when(bukkitRegistryAdapter.getAllMaterials()).thenReturn(List.of(matchingMaterial));

    Predicate<Material> alwaysTrue = _ -> true;
    List<String> result = tabCompleterService.getMaterialsByPredicate(alwaysTrue, "sto");

    assertEquals(1, result.size());
    assertTrue(result.contains("STONE"));
  }

  @Test
  void getMaterialsByPredicateReturnsEmptyListWhenNoMaterialsAvailable() {
    when(bukkitRegistryAdapter.getAllMaterials()).thenReturn(List.of());

    Predicate<Material> alwaysTrue = _ -> true;
    List<String> result = tabCompleterService.getMaterialsByPredicate(alwaysTrue, null);

    assertTrue(result.isEmpty());
  }

  @Test
  void getOnlinePlayerNamesReturnsAllPlayersWhenSenderIsNull() {
    Player firstPlayer = mock(Player.class);
    Player secondPlayer = mock(Player.class);
    when(firstPlayer.getName()).thenReturn("Alice");
    when(secondPlayer.getName()).thenReturn("Bob");

    doReturn(List.of(firstPlayer, secondPlayer)).when(serverService).getOnlinePlayers();

    List<String> result = tabCompleterService.getOnlinePlayerNames(null);

    assertEquals(2, result.size());
    assertTrue(result.contains("Alice"));
    assertTrue(result.contains("Bob"));
  }

  @Test
  void getOnlinePlayerNamesExcludesSenderFromList() {
    Player senderPlayer = mock(Player.class);
    Player otherPlayer = mock(Player.class);
    when(otherPlayer.getName()).thenReturn("Bob");

    doReturn(List.of(senderPlayer, otherPlayer)).when(serverService).getOnlinePlayers();

    List<String> result = tabCompleterService.getOnlinePlayerNames(senderPlayer);

    assertEquals(1, result.size());
    assertTrue(result.contains("Bob"));
  }

  @Test
  void getOnlinePlayerNamesDoesNotExcludeNonPlayerSender() {
    Player firstPlayer = mock(Player.class);
    Player secondPlayer = mock(Player.class);
    when(firstPlayer.getName()).thenReturn("Alice");
    when(secondPlayer.getName()).thenReturn("Bob");

    CommandSender consoleSender = mock(CommandSender.class);

    doReturn(List.of(firstPlayer, secondPlayer)).when(serverService).getOnlinePlayers();

    List<String> result = tabCompleterService.getOnlinePlayerNames(consoleSender);

    assertEquals(2, result.size());
    assertTrue(result.contains("Alice"));
    assertTrue(result.contains("Bob"));
  }

  @Test
  void getOnlinePlayerNamesReturnsEmptyListWhenNoPlayersOnline() {
    when(serverService.getOnlinePlayers()).thenReturn(List.of());

    List<String> result = tabCompleterService.getOnlinePlayerNames(null);

    assertTrue(result.isEmpty());
  }
}