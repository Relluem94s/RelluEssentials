package de.relluem94.minecraft.server.spigot.essentials.services;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.common.collect.Multimap;
import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.enums.WorldSetting;
import de.relluem94.minecraft.server.spigot.essentials.helpers.ExperienceHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.InventoryHelper;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.GroupEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.PlayerEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.SettingEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.WorldEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.WorldGroupEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.WorldGroupInventoryEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.WorldGroupSettingEntry;
import de.relluem94.minecraft.server.spigot.essentials.registries.WorldGroupRegistry;
import de.relluem94.minecraft.server.spigot.essentials.repositories.WorldGroupRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class WorldGroupServiceTest {

  @Mock
  private WorldGroupRegistry worldGroupRegistry;

  @Mock
  private WorldGroupRepository worldGroupRepository;

  @Mock
  private Player player;

  @Mock
  private World world;

  @Mock
  private PlayerInventory playerInventory;

  @Mock
  private ServiceContext serviceContext;

  @Mock
  private PlayerService playerService;

  private WorldGroupService worldGroupService;

  private MockedStatic<InventoryHelper> inventoryHelperMockedStatic;
  private MockedStatic<ExperienceHelper> experienceHelperMockedStatic;

  @BeforeEach
  void setUp() {
    worldGroupService = new WorldGroupService(serviceContext, worldGroupRegistry, worldGroupRepository);
    inventoryHelperMockedStatic = Mockito.mockStatic(InventoryHelper.class);
    experienceHelperMockedStatic = Mockito.mockStatic(ExperienceHelper.class);
  }

  @AfterEach
  void tearDown() {
    inventoryHelperMockedStatic.close();
    experienceHelperMockedStatic.close();
  }

  @Test
  void getWorldsMapReturnsEmptyMultimapInitially() {
    Multimap<WorldGroupEntry, WorldEntry> worldsMap = worldGroupService.getWorldsMap();
    assertNotNull(worldsMap);
    assertTrue(worldsMap.isEmpty());
  }

  @Test
  void loadAllClearsAndRepopulatesWorldsMap() {
    WorldGroupEntry worldGroupEntry = buildWorldGroupEntryWithNoSettings("testGroup");
    WorldEntry worldEntry = buildWorldEntry(worldGroupEntry);

    when(worldGroupRepository.findAllWorldGroups()).thenReturn(List.of(worldGroupEntry));
    when(worldGroupRepository.findWorldsByGroup(worldGroupEntry)).thenReturn(List.of(worldEntry));

    worldGroupService.loadAll();

    Multimap<WorldGroupEntry, WorldEntry> worldsMap = worldGroupService.getWorldsMap();
    assertAll(
        () -> assertEquals(1, worldsMap.size()),
        () -> assertTrue(worldsMap.containsKey(worldGroupEntry)),
        () -> assertTrue(worldsMap.containsValue(worldEntry))
    );
  }

  @Test
  void loadAllRegistersActiveSettingsForWorld() {
    WorldGroupEntry worldGroupEntry = buildWorldGroupEntryWithActiveSetting();
    WorldEntry worldEntry = buildWorldEntry(worldGroupEntry);

    when(worldGroupRepository.findAllWorldGroups()).thenReturn(List.of(worldGroupEntry));
    when(worldGroupRepository.findWorldsByGroup(worldGroupEntry)).thenReturn(List.of(worldEntry));

    worldGroupService.loadAll();

    verify(worldGroupRegistry).loadWorldsForSetting(eq(WorldSetting.DEATH_CHEST_SPAWN), any());
  }

  @Test
  void loadAllWithNoGroupsResultsInEmptyWorldsMap() {
    when(worldGroupRepository.findAllWorldGroups()).thenReturn(new ArrayList<>());

    worldGroupService.loadAll();

    assertTrue(worldGroupService.getWorldsMap().isEmpty());
  }

  @Test
  void isSettingActiveForWorldDelegatesToRegistry() {
    when(worldGroupRegistry.isSettingActiveForWorld(WorldSetting.DEATH_CHEST_SPAWN, "testWorld")).thenReturn(true);

    boolean result = worldGroupService.isSettingActiveForWorld(WorldSetting.DEATH_CHEST_SPAWN, "testWorld");

    assertTrue(result);
  }

  @Test
  void isSettingActiveForWorldReturnsFalseWhenNotActive() {
    when(worldGroupRegistry.isSettingActiveForWorld(WorldSetting.DEATH_CHEST_SPAWN, "testWorld")).thenReturn(false);

    boolean result = worldGroupService.isSettingActiveForWorld(WorldSetting.DEATH_CHEST_SPAWN, "testWorld");

    assertFalse(result);
  }

  @Test
  void findWorldGroupEntryForPlayerReturnsNullWhenWorldNotInAnyGroup() {
    when(player.getWorld()).thenReturn(world);
    when(world.getName()).thenReturn("unknownWorld");

    WorldGroupEntry result = worldGroupService.findWorldGroupEntryForPlayer(player);

    assertNull(result);
  }

  @Test
  void findWorldGroupEntryForPlayerReturnsGroupWhenWorldIsRegistered() {
    WorldGroupEntry worldGroupEntry = buildWorldGroupEntryWithNoSettings("testGroup");
    WorldEntry worldEntry = buildWorldEntry(worldGroupEntry);
    populateWorldsMap(worldGroupEntry, worldEntry);

    when(player.getWorld()).thenReturn(world);
    when(world.getName()).thenReturn("testWorld");

    WorldGroupEntry result = worldGroupService.findWorldGroupEntryForPlayer(player);

    assertEquals(worldGroupEntry, result);
  }

  @Test
  void findWorldEntryForPlayerReturnsNullWhenWorldNotRegistered() {
    when(player.getWorld()).thenReturn(world);
    when(world.getName()).thenReturn("unknownWorld");

    WorldEntry result = worldGroupService.findWorldEntryForPlayer(player);

    assertNull(result);
  }

  @Test
  void findWorldEntryForPlayerReturnsEntryWhenWorldIsRegistered() {
    WorldGroupEntry worldGroupEntry = buildWorldGroupEntryWithNoSettings("testGroup");
    WorldEntry worldEntry = buildWorldEntry(worldGroupEntry);
    populateWorldsMap(worldGroupEntry, worldEntry);

    when(player.getWorld()).thenReturn(world);
    when(world.getName()).thenReturn("testWorld");

    WorldEntry result = worldGroupService.findWorldEntryForPlayer(player);

    assertEquals(worldEntry, result);
  }

  @Test
  void loadWorldGroupInventoryForPlayerDoesNothingWhenWorldNotInAnyGroup() {
    when(player.getWorld()).thenReturn(world);
    when(world.getName()).thenReturn("unknownWorld");
    when(serviceContext.getPlayerService()).thenReturn(playerService);
    worldGroupService.loadWorldGroupInventoryForPlayer(player);

    inventoryHelperMockedStatic.verify(() -> InventoryHelper.createInventory(any(), any()), never());
  }

  @Test
  void loadWorldGroupInventoryForPlayerAppliesExistingInventory() {
    PlayerEntry playerEntry = buildPlayerEntry();
    WorldGroupEntry worldGroupEntry = buildWorldGroupEntryWithNoSettings("testGroup");
    WorldEntry worldEntry = buildWorldEntry(worldGroupEntry);
    populateWorldsMap(worldGroupEntry, worldEntry);

    WorldGroupInventoryEntry inventoryEntry = buildInventoryEntryWithInventory(playerEntry, worldGroupEntry);

    when(player.getWorld()).thenReturn(world);
    when(world.getName()).thenReturn("testWorld");
    when(playerService.getPlayerEntry(player)).thenReturn(playerEntry);
    when(serviceContext.getPlayerService()).thenReturn(playerService);
    when(worldGroupRepository.findInventoryByGroupAndPlayer(playerEntry, worldGroupEntry)).thenReturn(inventoryEntry);

    worldGroupService.loadWorldGroupInventoryForPlayer(player);

    assertAll(
        () -> inventoryHelperMockedStatic.verify(() -> InventoryHelper.createInventory(any(), eq(player))),
        () -> verify(player).setFoodLevel(inventoryEntry.getFoodLevel()),
        () -> verify(player).setHealth(inventoryEntry.getHealth()),
        () -> experienceHelperMockedStatic.verify(() -> ExperienceHelper.setTotalExperience(eq(player), eq(inventoryEntry.getTotalExperience())))
    );
  }

  private WorldGroupInventoryEntry buildInventoryEntryWithInventory(PlayerEntry playerEntry, WorldGroupEntry worldGroupEntry) {
    WorldGroupInventoryEntry entry = buildInventoryEntry(playerEntry, worldGroupEntry);
    entry.setInventory(new JSONObject());
    return entry;
  }


  @Test
  void loadWorldGroupInventoryForPlayerCreatesAndSavesNewInventoryWhenNoneExists() {
    PlayerEntry playerEntry = buildPlayerEntry();
    WorldGroupEntry worldGroupEntry = buildWorldGroupEntryWithNoSettings("testGroup");
    WorldEntry worldEntry = buildWorldEntry(worldGroupEntry);
    populateWorldsMap(worldGroupEntry, worldEntry);

    when(player.getWorld()).thenReturn(world);
    when(world.getName()).thenReturn("testWorld");
    when(playerService.getPlayerEntry(player)).thenReturn(playerEntry);
    when(worldGroupRepository.findInventoryByGroupAndPlayer(playerEntry, worldGroupEntry)).thenReturn(null);
    inventoryHelperMockedStatic.when(() -> InventoryHelper.saveInventoryToJSON(player)).thenReturn(null);
    when(serviceContext.getPlayerService()).thenReturn(playerService);

    worldGroupService.loadWorldGroupInventoryForPlayer(player);

    verify(worldGroupRepository).saveInventory(any(WorldGroupInventoryEntry.class));
  }

  @Test
  void saveWorldGroupInventoryForPlayerReturnsFalseWhenWorldNotRegistered() {
    when(player.getWorld()).thenReturn(world);
    when(world.getName()).thenReturn("unknownWorld");
    when(playerService.getPlayerEntry(player)).thenReturn(buildPlayerEntry());
    when(serviceContext.getPlayerService()).thenReturn(playerService);

    boolean result = worldGroupService.saveWorldGroupInventoryForPlayer(player, false);

    assertFalse(result);
  }

  @Test
  void saveWorldGroupInventoryForPlayerReturnsFalseWhenNoExistingInventory() {
    PlayerEntry playerEntry = buildPlayerEntry();
    WorldGroupEntry worldGroupEntry = buildWorldGroupEntryWithNoSettings("testGroup");
    WorldEntry worldEntry = buildWorldEntry(worldGroupEntry);
    populateWorldsMap(worldGroupEntry, worldEntry);

    when(player.getWorld()).thenReturn(world);
    when(world.getName()).thenReturn("testWorld");
    when(playerService.getPlayerEntry(player)).thenReturn(playerEntry);
    when(serviceContext.getPlayerService()).thenReturn(playerService);
    when(worldGroupRepository.findInventoryByGroupAndPlayer(playerEntry, worldGroupEntry)).thenReturn(null);
    inventoryHelperMockedStatic.when(() -> InventoryHelper.saveInventoryToJSON(player)).thenReturn(null);

    boolean result = worldGroupService.saveWorldGroupInventoryForPlayer(player, false);

    assertAll(
        () -> assertFalse(result),
        () -> verify(worldGroupRepository).saveInventory(any(WorldGroupInventoryEntry.class))
    );
  }

  @Test
  void saveWorldGroupInventoryForPlayerReturnsTrueWhenExistingInventoryUpdated() {
    PlayerEntry playerEntry = buildPlayerEntry();
    WorldGroupEntry worldGroupEntry = buildWorldGroupEntryWithNoSettings("testGroup");
    WorldEntry worldEntry = buildWorldEntry(worldGroupEntry);
    populateWorldsMap(worldGroupEntry, worldEntry);

    WorldGroupInventoryEntry inventoryEntry = buildInventoryEntry(playerEntry, worldGroupEntry);

    when(player.getWorld()).thenReturn(world);
    when(world.getName()).thenReturn("testWorld");
    when(playerService.getPlayerEntry(player)).thenReturn(playerEntry);
    when(serviceContext.getPlayerService()).thenReturn(playerService);
    when(worldGroupRepository.findInventoryByGroupAndPlayer(playerEntry, worldGroupEntry)).thenReturn(inventoryEntry);
    inventoryHelperMockedStatic.when(() -> InventoryHelper.saveInventoryToJSON(player)).thenReturn(null);

    boolean result = worldGroupService.saveWorldGroupInventoryForPlayer(player, false);

    assertAll(
        () -> assertTrue(result),
        () -> verify(worldGroupRepository).updateInventory(inventoryEntry)
    );
  }

  @Test
  void saveWorldGroupInventoryForPlayerClearsPlayerStateWhenClearAfterSaveIsTrue() {
    PlayerEntry playerEntry = buildPlayerEntry();
    WorldGroupEntry worldGroupEntry = buildWorldGroupEntryWithNoSettings("testGroup");
    WorldEntry worldEntry = buildWorldEntry(worldGroupEntry);
    populateWorldsMap(worldGroupEntry, worldEntry);

    WorldGroupInventoryEntry inventoryEntry = buildInventoryEntry(playerEntry, worldGroupEntry);

    when(player.getWorld()).thenReturn(world);
    when(world.getName()).thenReturn("testWorld");
    when(playerService.getPlayerEntry(player)).thenReturn(playerEntry);
    when(serviceContext.getPlayerService()).thenReturn(playerService);
    when(worldGroupRepository.findInventoryByGroupAndPlayer(playerEntry, worldGroupEntry)).thenReturn(inventoryEntry);
    when(player.getInventory()).thenReturn(playerInventory);
    inventoryHelperMockedStatic.when(() -> InventoryHelper.saveInventoryToJSON(player)).thenReturn(null);

    worldGroupService.saveWorldGroupInventoryForPlayer(player, true);

    assertAll(
        () -> verify(player).setTotalExperience(0),
        () -> verify(player).setLevel(0),
        () -> verify(player).setExp(0f),
        () -> verify(playerInventory).clear()
    );
  }

  @Test
  void saveWorldGroupInventoryForPlayerDoesNotClearPlayerStateWhenClearAfterSaveIsFalse() {
    PlayerEntry playerEntry = buildPlayerEntry();
    WorldGroupEntry worldGroupEntry = buildWorldGroupEntryWithNoSettings("testGroup");
    WorldEntry worldEntry = buildWorldEntry(worldGroupEntry);
    populateWorldsMap(worldGroupEntry, worldEntry);

    WorldGroupInventoryEntry inventoryEntry = buildInventoryEntry(playerEntry, worldGroupEntry);

    when(player.getWorld()).thenReturn(world);
    when(world.getName()).thenReturn("testWorld");
    when(playerService.getPlayerEntry(player)).thenReturn(playerEntry);
    when(serviceContext.getPlayerService()).thenReturn(playerService);
    when(worldGroupRepository.findInventoryByGroupAndPlayer(playerEntry, worldGroupEntry)).thenReturn(inventoryEntry);
    inventoryHelperMockedStatic.when(() -> InventoryHelper.saveInventoryToJSON(player)).thenReturn(null);

    worldGroupService.saveWorldGroupInventoryForPlayer(player, false);

    verify(player, never()).setTotalExperience(0);
  }

  @Test
  void saveWorldGroupInventoryForPlayerInWorldReturnsFalseWhenWorldNotRegistered() {
    PlayerEntry playerEntry = buildPlayerEntry();
    when(playerService.getPlayerEntry(player)).thenReturn(playerEntry);
    when(world.getName()).thenReturn("unknownWorld");
    when(serviceContext.getPlayerService()).thenReturn(playerService);

    boolean result = worldGroupService.saveWorldGroupInventoryForPlayerInWorld(player, world, false);

    assertFalse(result);
  }

  @Test
  void hasWorldGroupInventoryReturnsFalseWhenWorldNotRegistered() {
    PlayerEntry playerEntry = buildPlayerEntry();
    when(playerService.getPlayerEntry(player)).thenReturn(playerEntry);
    when(world.getName()).thenReturn("unknownWorld");
    when(serviceContext.getPlayerService()).thenReturn(playerService);

    boolean result = worldGroupService.hasWorldGroupInventory(player, world);

    assertFalse(result);
  }

  @Test
  void hasWorldGroupInventoryReturnsTrueWhenInventoryExists() {
    PlayerEntry playerEntry = buildPlayerEntry();
    WorldGroupEntry worldGroupEntry = buildWorldGroupEntryWithNoSettings("testGroup");
    WorldEntry worldEntry = buildWorldEntry(worldGroupEntry);
    populateWorldsMap(worldGroupEntry, worldEntry);

    WorldGroupInventoryEntry inventoryEntry = buildInventoryEntry(playerEntry, worldGroupEntry);

    when(playerService.getPlayerEntry(player)).thenReturn(playerEntry);
    when(world.getName()).thenReturn("testWorld");
    when(serviceContext.getPlayerService()).thenReturn(playerService);
    when(worldGroupRepository.findInventoryByGroupAndPlayer(playerEntry, worldGroupEntry)).thenReturn(inventoryEntry);

    boolean result = worldGroupService.hasWorldGroupInventory(player, world);

    assertTrue(result);
  }

  @Test
  void hasWorldGroupInventoryReturnsFalseWhenInventoryDoesNotExist() {
    PlayerEntry playerEntry = buildPlayerEntry();
    WorldGroupEntry worldGroupEntry = buildWorldGroupEntryWithNoSettings("testGroup");
    WorldEntry worldEntry = buildWorldEntry(worldGroupEntry);
    populateWorldsMap(worldGroupEntry, worldEntry);

    when(playerService.getPlayerEntry(player)).thenReturn(playerEntry);
    when(world.getName()).thenReturn("testWorld");
    when(serviceContext.getPlayerService()).thenReturn(playerService);
    when(worldGroupRepository.findInventoryByGroupAndPlayer(playerEntry, worldGroupEntry)).thenReturn(null);

    boolean result = worldGroupService.hasWorldGroupInventory(player, world);

    assertFalse(result);
  }

  @Test
  void findWorldGroupByNameReturnsFromWorldsMapWhenPresent() {
    WorldGroupEntry worldGroupEntry = buildWorldGroupEntryWithNoSettings("testGroup");
    WorldEntry worldEntry = buildWorldEntry(worldGroupEntry);
    populateWorldsMap(worldGroupEntry, worldEntry);

    Optional<WorldGroupEntry> result = worldGroupService.findWorldGroupByName("testGroup");

    assertAll(
        () -> assertTrue(result.isPresent()),
        () -> assertEquals(worldGroupEntry, result.get())
    );
  }

  @Test
  void findWorldGroupByNameFallsBackToRepositoryWhenNotInWorldsMap() {
    WorldGroupEntry worldGroupEntry = buildWorldGroupEntryWithNoSettings("testGroup");
    when(worldGroupRepository.findWorldGroupByName("testGroup")).thenReturn(worldGroupEntry);

    Optional<WorldGroupEntry> result = worldGroupService.findWorldGroupByName("testGroup");

    assertAll(
        () -> assertTrue(result.isPresent()),
        () -> assertEquals(worldGroupEntry, result.get())
    );
  }

  @Test
  void findWorldGroupByNameReturnsEmptyWhenNotFoundAnywhere() {
    when(worldGroupRepository.findWorldGroupByName("unknownGroup")).thenReturn(null);

    Optional<WorldGroupEntry> result = worldGroupService.findWorldGroupByName("unknownGroup");

    assertTrue(result.isEmpty());
  }

  @Test
  void createWorldGroupPersistsAndReturnsNewGroup() {
    PlayerEntry playerEntry = buildPlayerEntry();
    WorldGroupEntry worldGroupEntry = buildWorldGroupEntryWithNoSettings("newGroup");

    when(playerService.getPlayerEntry(player)).thenReturn(playerEntry);
    when(serviceContext.getPlayerService()).thenReturn(playerService);
    when(worldGroupRepository.findWorldGroupByName("newGroup")).thenReturn(worldGroupEntry);

    WorldGroupEntry result = worldGroupService.createWorldGroup(player, "newGroup");

    assertAll(
        () -> verify(worldGroupRepository).saveWorldGroup(any(WorldGroupEntry.class)),
        () -> assertEquals(worldGroupEntry, result)
    );
  }

  @Test
  void addWorldToGroupPersistsWorldAndUpdatesWorldsMap() {
    PlayerEntry playerEntry = buildPlayerEntry();
    WorldGroupEntry worldGroupEntry = buildWorldGroupEntryWithNoSettings("testGroup");

    when(playerService.getPlayerEntry(player)).thenReturn(playerEntry);
    when(serviceContext.getPlayerService()).thenReturn(playerService);
    worldGroupService.addWorldToGroup(player, worldGroupEntry, "newWorld");

    assertAll(
        () -> verify(worldGroupRepository).saveWorld(any(WorldEntry.class)),
        () -> assertTrue(worldGroupService.getWorldsMap().containsKey(worldGroupEntry))
    );
  }

  @Test
  void removeWorldFromGroupRemovesFromWorldsMapAndRegistry() {
    WorldGroupEntry worldGroupEntry = buildWorldGroupEntryWithNoSettings("testGroup");
    WorldEntry worldEntry = buildWorldEntry(worldGroupEntry);
    populateWorldsMap(worldGroupEntry, worldEntry);

    worldGroupService.removeWorldFromGroup(worldGroupEntry, "testWorld");

    assertAll(
        () -> assertFalse(worldGroupService.getWorldsMap().containsValue(worldEntry)),
        () -> verify(worldGroupRegistry).removeWorldFromAllSettings("testWorld")
    );
  }

  @Test
  void createAndRegisterWorldDoesNothingWhenWorldGroupNotFound() {
    when(worldGroupRepository.findWorldGroupByName("unknownGroup")).thenReturn(null);

    worldGroupService.createAndRegisterWorld("newWorld", "unknownGroup", new GroupEntry(), 1);

    verify(worldGroupRepository, never()).saveWorld(any());
  }

  @Test
  void createAndRegisterWorldPersistsWorldWhenGroupExists() {
    WorldGroupEntry worldGroupEntry = buildWorldGroupEntryWithNoSettings("testGroup");
    GroupEntry groupEntry = new GroupEntry();

    when(worldGroupRepository.findWorldGroupByName("testGroup")).thenReturn(worldGroupEntry);

    worldGroupService.createAndRegisterWorld("newWorld", "testGroup", groupEntry, 1);

    assertAll(
        () -> verify(worldGroupRepository).saveWorld(any(WorldEntry.class)),
        () -> assertTrue(worldGroupService.getWorldsMap().containsKey(worldGroupEntry))
    );
  }

  @Test
  void createAndRegisterWorldGroupPersistsNewGroup() {
    worldGroupService.createAndRegisterWorldGroup("newGroup", 1);

    verify(worldGroupRepository).saveWorldGroup(any(WorldGroupEntry.class));
  }

  @Test
  void initializeWorldGroupWithWorldDoesNothingWhenGroupAlreadyExists() {
    WorldGroupEntry worldGroupEntry = buildWorldGroupEntryWithNoSettings("existingGroup");
    WorldEntry worldEntry = buildWorldEntry(worldGroupEntry);
    populateWorldsMap(worldGroupEntry, worldEntry);

    worldGroupService.initializeWorldGroupWithWorld("existingGroup", "newWorld", new GroupEntry(), 1);

    verify(worldGroupRepository, never()).saveWorldGroup(any());
  }

  @Test
  void initializeWorldGroupWithWorldCreatesGroupAndWorldWhenGroupDoesNotExist() {
    WorldGroupEntry persistedEntry = buildWorldGroupEntryWithNoSettings("newGroup");

    when(worldGroupRepository.findWorldGroupByName("newGroup")).thenReturn(persistedEntry);
    when(worldGroupRepository.findAllWorldGroups()).thenReturn(List.of(persistedEntry));
    when(worldGroupRepository.findWorldsByGroup(persistedEntry)).thenReturn(new ArrayList<>());

    worldGroupService.initializeWorldGroupWithWorld("newGroup", "newWorld", new GroupEntry(), 1);

    assertAll(
        () -> verify(worldGroupRepository).saveWorldGroup(any(WorldGroupEntry.class)),
        () -> verify(worldGroupRepository).saveWorld(any(WorldEntry.class))
    );
  }

  @Test
  void loadWorldGroupInventoryForPlayerAppliesExistingInventoryWithNullInventoryContent() {
    PlayerEntry playerEntry = buildPlayerEntry();
    WorldGroupEntry worldGroupEntry = buildWorldGroupEntryWithNoSettings("testGroup");
    WorldEntry worldEntry = buildWorldEntry(worldGroupEntry);
    populateWorldsMap(worldGroupEntry, worldEntry);

    WorldGroupInventoryEntry inventoryEntry = buildInventoryEntry(playerEntry, worldGroupEntry);

    when(player.getWorld()).thenReturn(world);
    when(world.getName()).thenReturn("testWorld");
    when(playerService.getPlayerEntry(player)).thenReturn(playerEntry);
    when(serviceContext.getPlayerService()).thenReturn(playerService);
    when(worldGroupRepository.findInventoryByGroupAndPlayer(playerEntry, worldGroupEntry)).thenReturn(inventoryEntry);

    worldGroupService.loadWorldGroupInventoryForPlayer(player);

    assertAll(
        () -> inventoryHelperMockedStatic.verify(() -> InventoryHelper.createInventory(any(), any()), never()),
        () -> verify(player).setFoodLevel(inventoryEntry.getFoodLevel()),
        () -> verify(player).setHealth(inventoryEntry.getHealth()),
        () -> experienceHelperMockedStatic.verify(() -> ExperienceHelper.setTotalExperience(eq(player), eq(inventoryEntry.getTotalExperience())))
    );
  }

  @Test
  void removeWorldFromGroupDoesNotRemoveWhenGroupDoesNotMatch() {
    WorldGroupEntry worldGroupEntry = buildWorldGroupEntryWithNoSettings("testGroup");
    WorldGroupEntry otherGroupEntry = buildWorldGroupEntryWithNoSettings("otherGroup");
    WorldEntry worldEntry = buildWorldEntry(worldGroupEntry);
    populateWorldsMap(worldGroupEntry, worldEntry);

    worldGroupService.removeWorldFromGroup(otherGroupEntry, "testWorld");

    assertTrue(worldGroupService.getWorldsMap().containsValue(worldEntry));
  }

  @Test
  void removeWorldFromGroupDoesNotRemoveWhenWorldNameDoesNotMatch() {
    WorldGroupEntry worldGroupEntry = buildWorldGroupEntryWithNoSettings("testGroup");
    WorldEntry worldEntry = buildWorldEntry(worldGroupEntry);
    populateWorldsMap(worldGroupEntry, worldEntry);

    worldGroupService.removeWorldFromGroup(worldGroupEntry, "otherWorld");

    assertTrue(worldGroupService.getWorldsMap().containsValue(worldEntry));
  }

  private WorldGroupEntry buildWorldGroupEntryWithNoSettings(String name) {
    WorldGroupEntry entry = new WorldGroupEntry();
    entry.setName(name);
    entry.setSettings(new ArrayList<>());
    return entry;
  }

  private WorldGroupEntry buildWorldGroupEntryWithActiveSetting() {
    WorldGroupEntry entry = new WorldGroupEntry();
    entry.setName("testGroup");

    SettingEntry settingEntry = new SettingEntry();
    settingEntry.setName(WorldSetting.DEATH_CHEST_SPAWN.name());

    WorldGroupSettingEntry settingGroupEntry = new WorldGroupSettingEntry();
    settingGroupEntry.setSettingEntry(settingEntry);
    settingGroupEntry.setValue(true);

    entry.setSettings(List.of(settingGroupEntry));
    return entry;
  }

  private WorldEntry buildWorldEntry(WorldGroupEntry worldGroupEntry) {
    WorldEntry entry = new WorldEntry();
    entry.setName("testWorld");
    entry.setWorldGroupEntry(worldGroupEntry);
    return entry;
  }

  private PlayerEntry buildPlayerEntry() {
    PlayerEntry entry = new PlayerEntry();
    entry.setId(1);
    return entry;
  }

  private WorldGroupInventoryEntry buildInventoryEntry(PlayerEntry playerEntry, WorldGroupEntry worldGroupEntry) {
    WorldGroupInventoryEntry entry = new WorldGroupInventoryEntry();
    entry.setPlayerId(playerEntry.getId());
    entry.setWorldGroupEntry(worldGroupEntry);
    entry.setFoodLevel(20);
    entry.setHealth(20.0);
    entry.setTotalExperience(100);
    entry.setInventory(null);
    return entry;
  }

  private void populateWorldsMap(WorldGroupEntry worldGroupEntry, WorldEntry worldEntry) {
    worldGroupService.getWorldsMap().put(worldGroupEntry, worldEntry);
  }
}