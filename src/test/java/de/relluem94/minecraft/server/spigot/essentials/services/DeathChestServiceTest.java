package de.relluem94.minecraft.server.spigot.essentials.services;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.enums.LocationType;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.LocationEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.LocationTypeEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.PlayerEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.ProtectionEntry;
import java.util.Optional;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.block.DoubleChest;
import org.bukkit.block.data.type.Chest.Type;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DeathChestServiceTest {

  @Mock
  private ServiceContext serviceContext;

  @Mock
  private PlayerService playerService;

  @Mock
  private LocationTypeService locationTypeService;

  @Mock
  private LocationService locationService;

  @Mock
  private ProtectionService protectionService;

  @Mock
  private Player player;

  @Mock
  private PlayerInventory playerInventory;

  @Mock
  private World world;

  @Mock
  private Block originBlock;

  @Mock
  private Block neighborBlock;

  @Mock
  private Chest chestState;

  @Mock
  private DoubleChest doubleChest;

  @Mock
  private Inventory doubleChestInventory;

  @Mock
  private org.bukkit.block.data.type.Chest originChestData;

  @Mock
  private org.bukkit.block.data.type.Chest neighborChestData;

  @Mock
  private PlayerEntry playerEntry;

  @Mock
  private LocationTypeEntry locationTypeEntry;

  @Mock
  private LocationEntry persistedLocationEntry;

  private DeathChestService deathChestService;

  @BeforeEach
  void setUp() {
    deathChestService = new DeathChestService(serviceContext);
  }

  @Test
  void spawnDeathChestForPlayerReturnsFalseWhenAllInventoriesAreEmpty() {
    when(player.getInventory()).thenReturn(playerInventory);
    when(playerInventory.getContents()).thenReturn(new ItemStack[0]);
    when(playerInventory.getArmorContents()).thenReturn(new ItemStack[0]);
    when(playerInventory.getExtraContents()).thenReturn(new ItemStack[0]);

    boolean result = deathChestService.spawnDeathChestForPlayer(player);

    assertFalse(result);
  }

  @Test
  void spawnDeathChestForPlayerReturnsFalseWhenAllItemsAreNull() {
    when(player.getInventory()).thenReturn(playerInventory);
    when(playerInventory.getContents()).thenReturn(new ItemStack[]{null, null});
    when(playerInventory.getArmorContents()).thenReturn(new ItemStack[]{null});
    when(playerInventory.getExtraContents()).thenReturn(new ItemStack[]{null});

    boolean result = deathChestService.spawnDeathChestForPlayer(player);

    assertFalse(result);
  }

  @Test
  void spawnDeathChestForPlayerReturnsFalseWhenAllItemsAreAirMaterial() {
    ItemStack airItem = mock(ItemStack.class);
    when(airItem.getType()).thenReturn(Material.AIR);

    when(player.getInventory()).thenReturn(playerInventory);
    when(playerInventory.getContents()).thenReturn(new ItemStack[]{airItem});
    when(playerInventory.getArmorContents()).thenReturn(new ItemStack[]{airItem});
    when(playerInventory.getExtraContents()).thenReturn(new ItemStack[]{airItem});

    boolean result = deathChestService.spawnDeathChestForPlayer(player);

    assertFalse(result);
  }

  @Test
  void spawnDeathChestForPlayerReturnsFalseWhenNoAdjacentAirBlocksFound() {
    ItemStack item = mock(ItemStack.class);
    when(item.getType()).thenReturn(Material.DIRT);

    when(player.getInventory()).thenReturn(playerInventory);
    when(playerInventory.getContents()).thenReturn(new ItemStack[]{item});
    when(playerInventory.getArmorContents()).thenReturn(new ItemStack[0]);
    when(playerInventory.getExtraContents()).thenReturn(new ItemStack[0]);

    Location location = new Location(world, 0, 64, 0);
    when(player.getLocation()).thenReturn(location);
    when(world.getBlockAt(any(Location.class))).thenReturn(originBlock);
    when(originBlock.getRelative(0, 0, 0)).thenReturn(originBlock);
    when(originBlock.getRelative(0, 1, 0)).thenReturn(originBlock);
    when(originBlock.getRelative(0, 2, 0)).thenReturn(originBlock);
    when(originBlock.getRelative(0, 3, 0)).thenReturn(originBlock);
    when(originBlock.getType()).thenReturn(Material.STONE);

    Block nonAirNeighbor = mock(Block.class);
    when(originBlock.getRelative(any(BlockFace.class))).thenReturn(nonAirNeighbor);

    boolean result = deathChestService.spawnDeathChestForPlayer(player);

    assertFalse(result);
  }

  @Test
  void spawnDeathChestForPlayerReturnsTrueAndProtectsBothChestsWhenSuccessful() {
    ItemStack item = mock(ItemStack.class);
    when(item.getType()).thenReturn(Material.DIRT);

    when(player.getInventory()).thenReturn(playerInventory);
    when(playerInventory.getContents()).thenReturn(new ItemStack[]{item});
    when(playerInventory.getArmorContents()).thenReturn(new ItemStack[0]);
    when(playerInventory.getExtraContents()).thenReturn(new ItemStack[0]);

    Location location = new Location(world, 0, 64, 0);
    when(player.getLocation()).thenReturn(location);
    when(world.getBlockAt(any(Location.class))).thenReturn(originBlock);

    when(originBlock.getRelative(0, 0, 0)).thenReturn(originBlock);
    when(originBlock.getType()).thenReturn(Material.AIR);

    when(originBlock.getRelative(BlockFace.NORTH)).thenReturn(neighborBlock);
    when(neighborBlock.getType()).thenReturn(Material.AIR);

    when(originBlock.getX()).thenReturn(0);
    when(neighborBlock.getX()).thenReturn(0);
    when(originBlock.getZ()).thenReturn(0);
    when(neighborBlock.getZ()).thenReturn(-1);

    when(neighborBlock.getFace(originBlock)).thenReturn(BlockFace.SOUTH);

    when(originBlock.getBlockData()).thenReturn(originChestData);
    when(neighborBlock.getBlockData()).thenReturn(neighborChestData);

    when(neighborBlock.getFace(originBlock)).thenReturn(BlockFace.SOUTH);

    when(originBlock.getState()).thenReturn(chestState);
    when(chestState.getInventory()).thenReturn(doubleChestInventory);
    when(doubleChestInventory.getHolder()).thenReturn(doubleChest);
    when(doubleChest.getInventory()).thenReturn(doubleChestInventory);

    Location originLocation = new Location(world, 0, 64, 0);
    Location neighborLocation = new Location(world, 0, 64, -1);
    when(originBlock.getLocation()).thenReturn(originLocation);
    when(neighborBlock.getLocation()).thenReturn(neighborLocation);

    when(serviceContext.getPlayerService()).thenReturn(playerService);
    when(serviceContext.getLocationTypeService()).thenReturn(locationTypeService);
    when(serviceContext.getLocationService()).thenReturn(locationService);
    when(serviceContext.getProtectionService()).thenReturn(protectionService);

    when(playerService.getPlayerEntry(player)).thenReturn(playerEntry);
    when(playerEntry.getId()).thenReturn(1);
    when(locationTypeService.findByName(LocationType.PROTECTION))
        .thenReturn(Optional.of(locationTypeEntry));
    when(locationService.saveAndFetch(any(LocationEntry.class))).thenReturn(persistedLocationEntry);

    boolean result = deathChestService.spawnDeathChestForPlayer(player);

    assertAll(
        () -> assertTrue(result),
        () -> verify(originBlock).setType(Material.CHEST),
        () -> verify(neighborBlock).setType(Material.CHEST),
        () -> verify(originChestData).setFacing(BlockFace.EAST),
        () -> verify(neighborChestData).setFacing(BlockFace.EAST),
        () -> verify(neighborChestData).setType(Type.LEFT),
        () -> verify(originChestData).setType(Type.RIGHT),
        () -> verify(doubleChestInventory).addItem(item),
        () -> verify(playerInventory).clear(),
        () -> verify(playerInventory).setArmorContents(null),
        () -> verify(playerInventory).setExtraContents(null),
        () -> verify(protectionService, times(2))
            .saveProtectionAndAddToRegistry(any(Location.class), any(ProtectionEntry.class))
    );
  }


  @Test
  void spawnDeathChestForPlayerSetsCorrectRightsJsonWithPlayerId() {
    ItemStack item = mock(ItemStack.class);
    when(item.getType()).thenReturn(Material.STONE);

    when(player.getInventory()).thenReturn(playerInventory);
    when(playerInventory.getContents()).thenReturn(new ItemStack[]{item});
    when(playerInventory.getArmorContents()).thenReturn(new ItemStack[0]);
    when(playerInventory.getExtraContents()).thenReturn(new ItemStack[0]);

    Location location = new Location(world, 0, 64, 0);
    when(player.getLocation()).thenReturn(location);
    when(world.getBlockAt(any(Location.class))).thenReturn(originBlock);

    when(originBlock.getRelative(0, 0, 0)).thenReturn(originBlock);
    when(originBlock.getType()).thenReturn(Material.AIR);
    when(originBlock.getRelative(BlockFace.NORTH)).thenReturn(neighborBlock);
    when(neighborBlock.getType()).thenReturn(Material.AIR);

    when(originBlock.getX()).thenReturn(0);
    when(neighborBlock.getX()).thenReturn(0);
    when(originBlock.getZ()).thenReturn(0);
    when(neighborBlock.getZ()).thenReturn(-1);

    when(originBlock.getBlockData()).thenReturn(originChestData);
    when(neighborBlock.getBlockData()).thenReturn(neighborChestData);

    when(originBlock.getState()).thenReturn(chestState);
    when(chestState.getInventory()).thenReturn(doubleChestInventory);
    when(doubleChestInventory.getHolder()).thenReturn(doubleChest);
    when(doubleChest.getInventory()).thenReturn(doubleChestInventory);

    when(originBlock.getLocation()).thenReturn(new Location(world, 0, 64, 0));
    when(neighborBlock.getLocation()).thenReturn(new Location(world, 0, 64, -1));

    when(serviceContext.getPlayerService()).thenReturn(playerService);
    when(serviceContext.getLocationTypeService()).thenReturn(locationTypeService);
    when(serviceContext.getLocationService()).thenReturn(locationService);
    when(serviceContext.getProtectionService()).thenReturn(protectionService);

    when(playerService.getPlayerEntry(player)).thenReturn(playerEntry);
    when(playerEntry.getId()).thenReturn(42);
    when(locationTypeService.findByName(LocationType.PROTECTION))
        .thenReturn(Optional.of(locationTypeEntry));
    when(locationService.saveAndFetch(any(LocationEntry.class))).thenReturn(persistedLocationEntry);

    ArgumentCaptor<ProtectionEntry> protectionCaptor = ArgumentCaptor.forClass(ProtectionEntry.class);

    deathChestService.spawnDeathChestForPlayer(player);

    verify(protectionService, times(2))
        .saveProtectionAndAddToRegistry(any(Location.class), protectionCaptor.capture());

    ProtectionEntry capturedProtection = protectionCaptor.getAllValues().getFirst();

    assertAll(
        () -> assertNotNull(capturedProtection.getRights()),
        () -> assertTrue(capturedProtection.getRights().has("IDs")),
        () -> assertEquals(42, capturedProtection.getRights().getJSONArray("IDs").get(0)),
        () -> assertNotNull(capturedProtection.getFlags()),
        () -> assertNotNull(capturedProtection.getLocationEntry()),
        () -> assertNotNull(capturedProtection.getMaterialName())
    );
  }

  @Test
  void spawnDeathChestForPlayerSkipsProtectionWhenLocationTypeNotFound() {
    ItemStack item = mock(ItemStack.class);
    when(item.getType()).thenReturn(Material.DIRT);

    when(player.getInventory()).thenReturn(playerInventory);
    when(playerInventory.getContents()).thenReturn(new ItemStack[]{item});
    when(playerInventory.getArmorContents()).thenReturn(new ItemStack[0]);
    when(playerInventory.getExtraContents()).thenReturn(new ItemStack[0]);

    Location location = new Location(world, 0, 64, 0);
    when(player.getLocation()).thenReturn(location);
    when(world.getBlockAt(any(Location.class))).thenReturn(originBlock);

    when(originBlock.getRelative(0, 0, 0)).thenReturn(originBlock);
    when(originBlock.getType()).thenReturn(Material.AIR);
    when(originBlock.getRelative(BlockFace.NORTH)).thenReturn(neighborBlock);
    when(neighborBlock.getType()).thenReturn(Material.AIR);

    when(originBlock.getX()).thenReturn(0);
    when(neighborBlock.getX()).thenReturn(0);
    when(originBlock.getZ()).thenReturn(0);
    when(neighborBlock.getZ()).thenReturn(-1);

    when(originBlock.getBlockData()).thenReturn(originChestData);
    when(neighborBlock.getBlockData()).thenReturn(neighborChestData);

    when(originBlock.getState()).thenReturn(chestState);
    when(chestState.getInventory()).thenReturn(doubleChestInventory);
    when(doubleChestInventory.getHolder()).thenReturn(doubleChest);
    when(doubleChest.getInventory()).thenReturn(doubleChestInventory);

    when(serviceContext.getPlayerService()).thenReturn(playerService);
    when(serviceContext.getLocationTypeService()).thenReturn(locationTypeService);

    when(playerService.getPlayerEntry(player)).thenReturn(playerEntry);
    when(locationTypeService.findByName(LocationType.PROTECTION)).thenReturn(Optional.empty());

    boolean result = deathChestService.spawnDeathChestForPlayer(player);

    assertAll(
        () -> assertTrue(result),
        () -> verify(protectionService, never())
            .saveProtectionAndAddToRegistry(any(), any())
    );
  }

  @Test
  void spawnDeathChestForPlayerFillsChestWithArmorAndOffHandItems() {
    ItemStack mainItem = mock(ItemStack.class);
    when(mainItem.getType()).thenReturn(Material.DIRT);

    ItemStack armorItem = mock(ItemStack.class);
    when(armorItem.getType()).thenReturn(Material.DIAMOND_HELMET);

    ItemStack offHandItem = mock(ItemStack.class);
    when(offHandItem.getType()).thenReturn(Material.SHIELD);

    when(player.getInventory()).thenReturn(playerInventory);
    when(playerInventory.getContents()).thenReturn(new ItemStack[]{mainItem});
    when(playerInventory.getArmorContents()).thenReturn(new ItemStack[]{armorItem});
    when(playerInventory.getExtraContents()).thenReturn(new ItemStack[]{offHandItem});

    Location location = new Location(world, 0, 64, 0);
    when(player.getLocation()).thenReturn(location);
    when(world.getBlockAt(any(Location.class))).thenReturn(originBlock);

    when(originBlock.getRelative(0, 0, 0)).thenReturn(originBlock);
    when(originBlock.getType()).thenReturn(Material.AIR);
    when(originBlock.getRelative(BlockFace.NORTH)).thenReturn(neighborBlock);
    when(neighborBlock.getType()).thenReturn(Material.AIR);

    when(originBlock.getX()).thenReturn(0);
    when(neighborBlock.getX()).thenReturn(0);
    when(originBlock.getZ()).thenReturn(0);
    when(neighborBlock.getZ()).thenReturn(-1);

    when(originBlock.getBlockData()).thenReturn(originChestData);
    when(neighborBlock.getBlockData()).thenReturn(neighborChestData);

    when(originBlock.getState()).thenReturn(chestState);
    when(chestState.getInventory()).thenReturn(doubleChestInventory);
    when(doubleChestInventory.getHolder()).thenReturn(doubleChest);
    when(doubleChest.getInventory()).thenReturn(doubleChestInventory);

    when(originBlock.getLocation()).thenReturn(new Location(world, 0, 64, 0));
    when(neighborBlock.getLocation()).thenReturn(new Location(world, 0, 64, -1));

    when(serviceContext.getPlayerService()).thenReturn(playerService);
    when(serviceContext.getLocationTypeService()).thenReturn(locationTypeService);
    when(serviceContext.getLocationService()).thenReturn(locationService);
    when(serviceContext.getProtectionService()).thenReturn(protectionService);

    when(playerService.getPlayerEntry(player)).thenReturn(playerEntry);
    when(playerEntry.getId()).thenReturn(1);
    when(locationTypeService.findByName(LocationType.PROTECTION))
        .thenReturn(Optional.of(locationTypeEntry));
    when(locationService.saveAndFetch(any(LocationEntry.class))).thenReturn(persistedLocationEntry);

    deathChestService.spawnDeathChestForPlayer(player);

    assertAll(
        () -> verify(doubleChestInventory).addItem(mainItem),
        () -> verify(doubleChestInventory).addItem(armorItem),
        () -> verify(doubleChestInventory).addItem(offHandItem)
    );
  }

  @Test
  void spawnDeathChestForPlayerReturnsTrueWhenOnlyArmorContainsItems() {
    ItemStack armorItem = mock(ItemStack.class);
    when(armorItem.getType()).thenReturn(Material.DIAMOND_CHESTPLATE);

    when(player.getInventory()).thenReturn(playerInventory);
    when(playerInventory.getContents()).thenReturn(new ItemStack[0]);
    when(playerInventory.getArmorContents()).thenReturn(new ItemStack[]{armorItem});
    when(playerInventory.getExtraContents()).thenReturn(new ItemStack[0]);

    Location location = new Location(world, 0, 64, 0);
    when(player.getLocation()).thenReturn(location);
    when(world.getBlockAt(any(Location.class))).thenReturn(originBlock);

    when(originBlock.getRelative(0, 0, 0)).thenReturn(originBlock);
    when(originBlock.getType()).thenReturn(Material.AIR);
    when(originBlock.getRelative(BlockFace.NORTH)).thenReturn(neighborBlock);
    when(neighborBlock.getType()).thenReturn(Material.AIR);

    when(originBlock.getX()).thenReturn(0);
    when(neighborBlock.getX()).thenReturn(0);
    when(originBlock.getZ()).thenReturn(0);
    when(neighborBlock.getZ()).thenReturn(-1);

    when(originBlock.getBlockData()).thenReturn(originChestData);
    when(neighborBlock.getBlockData()).thenReturn(neighborChestData);

    when(originBlock.getState()).thenReturn(chestState);
    when(chestState.getInventory()).thenReturn(doubleChestInventory);
    when(doubleChestInventory.getHolder()).thenReturn(doubleChest);
    when(doubleChest.getInventory()).thenReturn(doubleChestInventory);

    when(originBlock.getLocation()).thenReturn(new Location(world, 0, 64, 0));
    when(neighborBlock.getLocation()).thenReturn(new Location(world, 0, 64, -1));

    when(serviceContext.getPlayerService()).thenReturn(playerService);
    when(serviceContext.getLocationTypeService()).thenReturn(locationTypeService);
    when(serviceContext.getLocationService()).thenReturn(locationService);
    when(serviceContext.getProtectionService()).thenReturn(protectionService);

    when(playerService.getPlayerEntry(player)).thenReturn(playerEntry);
    when(playerEntry.getId()).thenReturn(1);
    when(locationTypeService.findByName(LocationType.PROTECTION))
        .thenReturn(Optional.of(locationTypeEntry));
    when(locationService.saveAndFetch(any(LocationEntry.class))).thenReturn(persistedLocationEntry);

    boolean result = deathChestService.spawnDeathChestForPlayer(player);

    assertTrue(result);
  }

  @Test
  void spawnDeathChestForPlayerUsesDoubleChestInventoryWhenHolderIsDoubleChest() {
    ItemStack item = mock(ItemStack.class);
    when(item.getType()).thenReturn(Material.DIRT);

    when(player.getInventory()).thenReturn(playerInventory);
    when(playerInventory.getContents()).thenReturn(new ItemStack[]{item});
    when(playerInventory.getArmorContents()).thenReturn(new ItemStack[0]);
    when(playerInventory.getExtraContents()).thenReturn(new ItemStack[0]);

    Location location = new Location(world, 0, 64, 0);
    when(player.getLocation()).thenReturn(location);
    when(world.getBlockAt(any(Location.class))).thenReturn(originBlock);

    when(originBlock.getRelative(0, 0, 0)).thenReturn(originBlock);
    when(originBlock.getType()).thenReturn(Material.AIR);
    when(originBlock.getRelative(BlockFace.NORTH)).thenReturn(neighborBlock);
    when(neighborBlock.getType()).thenReturn(Material.AIR);

    when(originBlock.getX()).thenReturn(0);
    when(neighborBlock.getX()).thenReturn(0);
    when(originBlock.getZ()).thenReturn(0);
    when(neighborBlock.getZ()).thenReturn(-1);

    when(originBlock.getBlockData()).thenReturn(originChestData);
    when(neighborBlock.getBlockData()).thenReturn(neighborChestData);

    Inventory singleChestInventory = mock(Inventory.class);
    when(originBlock.getState()).thenReturn(chestState);
    when(chestState.getInventory()).thenReturn(singleChestInventory);
    when(singleChestInventory.getHolder()).thenReturn(doubleChest);
    when(doubleChest.getInventory()).thenReturn(doubleChestInventory);

    when(originBlock.getLocation()).thenReturn(new Location(world, 0, 64, 0));
    when(neighborBlock.getLocation()).thenReturn(new Location(world, 0, 64, -1));

    when(serviceContext.getPlayerService()).thenReturn(playerService);
    when(serviceContext.getLocationTypeService()).thenReturn(locationTypeService);
    when(serviceContext.getLocationService()).thenReturn(locationService);
    when(serviceContext.getProtectionService()).thenReturn(protectionService);

    when(playerService.getPlayerEntry(player)).thenReturn(playerEntry);
    when(playerEntry.getId()).thenReturn(1);
    when(locationTypeService.findByName(LocationType.PROTECTION))
        .thenReturn(Optional.of(locationTypeEntry));
    when(locationService.saveAndFetch(any(LocationEntry.class))).thenReturn(persistedLocationEntry);

    deathChestService.spawnDeathChestForPlayer(player);

    verify(doubleChestInventory).addItem(item);
    verify(singleChestInventory, never()).addItem(any(ItemStack.class));
  }

  @Test
  void spawnDeathChestForPlayerSetsLocationEntryFieldsCorrectly() {
    ItemStack item = mock(ItemStack.class);
    when(item.getType()).thenReturn(Material.DIRT);

    when(player.getInventory()).thenReturn(playerInventory);
    when(playerInventory.getContents()).thenReturn(new ItemStack[]{item});
    when(playerInventory.getArmorContents()).thenReturn(new ItemStack[0]);
    when(playerInventory.getExtraContents()).thenReturn(new ItemStack[0]);

    Location location = new Location(world, 0, 64, 0);
    when(player.getLocation()).thenReturn(location);
    when(world.getBlockAt(any(Location.class))).thenReturn(originBlock);

    when(originBlock.getRelative(0, 0, 0)).thenReturn(originBlock);
    when(originBlock.getType()).thenReturn(Material.AIR);
    when(originBlock.getRelative(BlockFace.NORTH)).thenReturn(neighborBlock);
    when(neighborBlock.getType()).thenReturn(Material.AIR);

    when(originBlock.getX()).thenReturn(0);
    when(neighborBlock.getX()).thenReturn(0);
    when(originBlock.getZ()).thenReturn(0);
    when(neighborBlock.getZ()).thenReturn(-1);

    when(originBlock.getBlockData()).thenReturn(originChestData);
    when(neighborBlock.getBlockData()).thenReturn(neighborChestData);

    when(originBlock.getState()).thenReturn(chestState);
    when(chestState.getInventory()).thenReturn(doubleChestInventory);
    when(doubleChestInventory.getHolder()).thenReturn(doubleChest);
    when(doubleChest.getInventory()).thenReturn(doubleChestInventory);

    Location originLocation = new Location(world, 0, 64, 0);
    when(originBlock.getLocation()).thenReturn(originLocation);
    when(neighborBlock.getLocation()).thenReturn(new Location(world, 0, 64, -1));

    when(serviceContext.getPlayerService()).thenReturn(playerService);
    when(serviceContext.getLocationTypeService()).thenReturn(locationTypeService);
    when(serviceContext.getLocationService()).thenReturn(locationService);
    when(serviceContext.getProtectionService()).thenReturn(protectionService);

    when(playerService.getPlayerEntry(player)).thenReturn(playerEntry);
    when(playerEntry.getId()).thenReturn(99);
    when(locationTypeService.findByName(LocationType.PROTECTION))
        .thenReturn(Optional.of(locationTypeEntry));

    ArgumentCaptor<LocationEntry> locationEntryCaptor = ArgumentCaptor.forClass(LocationEntry.class);
    when(locationService.saveAndFetch(locationEntryCaptor.capture())).thenReturn(persistedLocationEntry);

    deathChestService.spawnDeathChestForPlayer(player);

    LocationEntry capturedEntry = locationEntryCaptor.getAllValues().getFirst();

    assertAll(
        () -> assertEquals(99, capturedEntry.getPlayerId()),
        () -> assertEquals(locationTypeEntry, capturedEntry.getLocationType()),
        () -> verify(locationService, times(2)).saveAndFetch(any(LocationEntry.class))
    );
  }


  @Test
  void spawnDeathChestForPlayerSkipsNullItemsInArmorAndOffHandSlots() {
    ItemStack mainItem = mock(ItemStack.class);
    when(mainItem.getType()).thenReturn(Material.DIRT);

    ItemStack offHandItem = mock(ItemStack.class);
    when(offHandItem.getType()).thenReturn(Material.SHIELD);

    when(player.getInventory()).thenReturn(playerInventory);
    when(playerInventory.getContents()).thenReturn(new ItemStack[]{mainItem, null});
    when(playerInventory.getArmorContents()).thenReturn(new ItemStack[]{null, null});
    when(playerInventory.getExtraContents()).thenReturn(new ItemStack[]{offHandItem, null});

    setupSuccessfulChestPlacement();

    deathChestService.spawnDeathChestForPlayer(player);

    assertAll(
        () -> verify(doubleChestInventory).addItem(mainItem),
        () -> verify(doubleChestInventory).addItem(offHandItem),
        () -> verify(doubleChestInventory, times(2)).addItem(any(ItemStack.class))
    );
  }

  @Test
  void spawnDeathChestForPlayerSkipsAirMaterialItemsInArmorAndOffHandSlots() {
    ItemStack airItem = mock(ItemStack.class);
    when(airItem.getType()).thenReturn(Material.AIR);

    ItemStack mainItem = mock(ItemStack.class);
    when(mainItem.getType()).thenReturn(Material.DIRT);

    when(player.getInventory()).thenReturn(playerInventory);
    when(playerInventory.getContents()).thenReturn(new ItemStack[]{mainItem, airItem});
    when(playerInventory.getArmorContents()).thenReturn(new ItemStack[]{airItem, airItem});
    when(playerInventory.getExtraContents()).thenReturn(new ItemStack[]{airItem});

    setupSuccessfulChestPlacement();

    deathChestService.spawnDeathChestForPlayer(player);

    assertAll(
        () -> verify(doubleChestInventory).addItem(mainItem),
        () -> verify(doubleChestInventory, times(1)).addItem(any(ItemStack.class))
    );
  }

  private void setupSuccessfulChestPlacement() {
    Location location = new Location(world, 0, 64, 0);
    when(player.getLocation()).thenReturn(location);
    when(world.getBlockAt(any(Location.class))).thenReturn(originBlock);

    when(originBlock.getRelative(0, 0, 0)).thenReturn(originBlock);
    when(originBlock.getType()).thenReturn(Material.AIR);
    when(originBlock.getRelative(BlockFace.NORTH)).thenReturn(neighborBlock);
    when(neighborBlock.getType()).thenReturn(Material.AIR);

    when(originBlock.getX()).thenReturn(0);
    when(neighborBlock.getX()).thenReturn(0);
    when(originBlock.getZ()).thenReturn(0);
    when(neighborBlock.getZ()).thenReturn(-1);

    when(originBlock.getBlockData()).thenReturn(originChestData);
    when(neighborBlock.getBlockData()).thenReturn(neighborChestData);

    when(originBlock.getState()).thenReturn(chestState);
    when(chestState.getInventory()).thenReturn(doubleChestInventory);
    when(doubleChestInventory.getHolder()).thenReturn(doubleChest);
    when(doubleChest.getInventory()).thenReturn(doubleChestInventory);

    when(originBlock.getLocation()).thenReturn(new Location(world, 0, 64, 0));
    when(neighborBlock.getLocation()).thenReturn(new Location(world, 0, 64, -1));

    when(serviceContext.getPlayerService()).thenReturn(playerService);
    when(serviceContext.getLocationTypeService()).thenReturn(locationTypeService);
    when(serviceContext.getLocationService()).thenReturn(locationService);
    when(serviceContext.getProtectionService()).thenReturn(protectionService);

    when(playerService.getPlayerEntry(player)).thenReturn(playerEntry);
    when(playerEntry.getId()).thenReturn(1);
    when(locationTypeService.findByName(LocationType.PROTECTION))
        .thenReturn(Optional.of(locationTypeEntry));
    when(locationService.saveAndFetch(any(LocationEntry.class))).thenReturn(persistedLocationEntry);
  }


  @Test
  void spawnDeathChestForPlayerPlacesDoubleChestAlongXAxisWhenFirstBlockHasSmallerX() {
    ItemStack item = mock(ItemStack.class);
    when(item.getType()).thenReturn(Material.DIRT);

    when(player.getInventory()).thenReturn(playerInventory);
    when(playerInventory.getContents()).thenReturn(new ItemStack[]{item});
    when(playerInventory.getArmorContents()).thenReturn(new ItemStack[0]);
    when(playerInventory.getExtraContents()).thenReturn(new ItemStack[0]);

    when(originBlock.getRelative(0, 0, 0)).thenReturn(originBlock);
    when(originBlock.getType()).thenReturn(Material.AIR);
    when(originBlock.getRelative(BlockFace.NORTH)).thenReturn(neighborBlock);
    when(neighborBlock.getType()).thenReturn(Material.AIR);

    when(originBlock.getX()).thenReturn(0);
    when(neighborBlock.getX()).thenReturn(1);

    when(originBlock.getBlockData()).thenReturn(originChestData);
    when(neighborBlock.getBlockData()).thenReturn(neighborChestData);

    when(originBlock.getFace(neighborBlock)).thenReturn(BlockFace.EAST);

    setupInventoryAndServicesForChestPlacement(item);

    deathChestService.spawnDeathChestForPlayer(player);

    assertAll(
        () -> verify(originChestData).setType(Type.LEFT),
        () -> verify(neighborChestData).setType(Type.RIGHT),
        () -> verify(originChestData).setFacing(BlockFace.NORTH),
        () -> verify(neighborChestData).setFacing(BlockFace.NORTH)
    );
  }

  @Test
  void spawnDeathChestForPlayerPlacesDoubleChestAlongZAxisWhenFirstBlockHasSmallerZ() {
    ItemStack item = mock(ItemStack.class);
    when(item.getType()).thenReturn(Material.DIRT);

    when(player.getInventory()).thenReturn(playerInventory);
    when(playerInventory.getContents()).thenReturn(new ItemStack[]{item});
    when(playerInventory.getArmorContents()).thenReturn(new ItemStack[0]);
    when(playerInventory.getExtraContents()).thenReturn(new ItemStack[0]);

    when(originBlock.getRelative(0, 0, 0)).thenReturn(originBlock);
    when(originBlock.getType()).thenReturn(Material.AIR);
    when(originBlock.getRelative(BlockFace.NORTH)).thenReturn(neighborBlock);
    when(neighborBlock.getType()).thenReturn(Material.AIR);

    when(originBlock.getX()).thenReturn(0);
    when(neighborBlock.getX()).thenReturn(0);
    when(originBlock.getZ()).thenReturn(0);
    when(neighborBlock.getZ()).thenReturn(1);

    when(originBlock.getBlockData()).thenReturn(originChestData);
    when(neighborBlock.getBlockData()).thenReturn(neighborChestData);

    when(originBlock.getFace(neighborBlock)).thenReturn(BlockFace.SOUTH);

    setupInventoryAndServicesForChestPlacement(item);

    deathChestService.spawnDeathChestForPlayer(player);

    assertAll(
        () -> verify(originChestData).setType(Type.LEFT),
        () -> verify(neighborChestData).setType(Type.RIGHT),
        () -> verify(originChestData).setFacing(BlockFace.EAST),
        () -> verify(neighborChestData).setFacing(BlockFace.EAST)
    );
  }

  private void setupInventoryAndServicesForChestPlacement(ItemStack item) {
    Location location = new Location(world, 0, 64, 0);
    when(player.getLocation()).thenReturn(location);
    when(world.getBlockAt(any(Location.class))).thenReturn(originBlock);

    when(originBlock.getState()).thenReturn(chestState);
    when(chestState.getInventory()).thenReturn(doubleChestInventory);
    when(doubleChestInventory.getHolder()).thenReturn(doubleChest);
    when(doubleChest.getInventory()).thenReturn(doubleChestInventory);

    when(originBlock.getLocation()).thenReturn(new Location(world, 0, 64, 0));
    when(neighborBlock.getLocation()).thenReturn(new Location(world, 0, 64, 1));

    when(serviceContext.getPlayerService()).thenReturn(playerService);
    when(serviceContext.getLocationTypeService()).thenReturn(locationTypeService);
    when(serviceContext.getLocationService()).thenReturn(locationService);
    when(serviceContext.getProtectionService()).thenReturn(protectionService);

    when(playerService.getPlayerEntry(player)).thenReturn(playerEntry);
    when(playerEntry.getId()).thenReturn(1);
    when(locationTypeService.findByName(LocationType.PROTECTION))
        .thenReturn(Optional.of(locationTypeEntry));
    when(locationService.saveAndFetch(any(LocationEntry.class))).thenReturn(persistedLocationEntry);
  }


  @Test
  void spawnDeathChestForPlayerReturnsTrueWhenOnlyOffHandContainsItems() {
    ItemStack offHandItem = mock(ItemStack.class);
    when(offHandItem.getType()).thenReturn(Material.SHIELD);

    when(player.getInventory()).thenReturn(playerInventory);
    when(playerInventory.getContents()).thenReturn(new ItemStack[0]);
    when(playerInventory.getArmorContents()).thenReturn(new ItemStack[0]);
    when(playerInventory.getExtraContents()).thenReturn(new ItemStack[]{offHandItem});

    setupSuccessfulChestPlacement();

    boolean result = deathChestService.spawnDeathChestForPlayer(player);

    assertTrue(result);
  }

  @Test
  void spawnDeathChestForPlayerFindsAirBlocksAtHigherYOffsetWhenOriginIsNotAir() {
    ItemStack item = mock(ItemStack.class);
    when(item.getType()).thenReturn(Material.DIRT);

    when(player.getInventory()).thenReturn(playerInventory);
    when(playerInventory.getContents()).thenReturn(new ItemStack[]{item});
    when(playerInventory.getArmorContents()).thenReturn(new ItemStack[0]);
    when(playerInventory.getExtraContents()).thenReturn(new ItemStack[0]);

    Location location = new Location(world, 0, 64, 0);
    when(player.getLocation()).thenReturn(location);
    when(world.getBlockAt(any(Location.class))).thenReturn(originBlock);

    Block solidBlock = mock(Block.class);
    when(solidBlock.getType()).thenReturn(Material.STONE);

    Block airBlockAtOffset2 = mock(Block.class);
    when(airBlockAtOffset2.getType()).thenReturn(Material.AIR);

    when(originBlock.getRelative(0, 0, 0)).thenReturn(solidBlock);
    when(originBlock.getRelative(0, 1, 0)).thenReturn(solidBlock);
    when(originBlock.getRelative(0, 2, 0)).thenReturn(airBlockAtOffset2);

    when(airBlockAtOffset2.getRelative(BlockFace.NORTH)).thenReturn(neighborBlock);
    when(neighborBlock.getType()).thenReturn(Material.AIR);

    when(airBlockAtOffset2.getX()).thenReturn(0);
    when(neighborBlock.getX()).thenReturn(0);
    when(airBlockAtOffset2.getZ()).thenReturn(0);
    when(neighborBlock.getZ()).thenReturn(-1);

    org.bukkit.block.data.type.Chest airBlockChestData = mock(org.bukkit.block.data.type.Chest.class);
    when(airBlockAtOffset2.getBlockData()).thenReturn(airBlockChestData);
    when(neighborBlock.getBlockData()).thenReturn(neighborChestData);

    Chest airBlockChestState = mock(Chest.class);
    when(airBlockAtOffset2.getState()).thenReturn(airBlockChestState);
    when(airBlockChestState.getInventory()).thenReturn(doubleChestInventory);
    when(doubleChestInventory.getHolder()).thenReturn(doubleChest);
    when(doubleChest.getInventory()).thenReturn(doubleChestInventory);

    when(airBlockAtOffset2.getLocation()).thenReturn(new Location(world, 0, 66, 0));
    when(neighborBlock.getLocation()).thenReturn(new Location(world, 0, 66, -1));

    when(serviceContext.getPlayerService()).thenReturn(playerService);
    when(serviceContext.getLocationTypeService()).thenReturn(locationTypeService);
    when(serviceContext.getLocationService()).thenReturn(locationService);
    when(serviceContext.getProtectionService()).thenReturn(protectionService);

    when(playerService.getPlayerEntry(player)).thenReturn(playerEntry);
    when(playerEntry.getId()).thenReturn(1);
    when(locationTypeService.findByName(LocationType.PROTECTION))
        .thenReturn(Optional.of(locationTypeEntry));
    when(locationService.saveAndFetch(any(LocationEntry.class))).thenReturn(persistedLocationEntry);

    boolean result = deathChestService.spawnDeathChestForPlayer(player);

    assertAll(
        () -> assertTrue(result),
        () -> verify(airBlockAtOffset2).setType(Material.CHEST),
        () -> verify(neighborBlock).setType(Material.CHEST)
    );
  }

  @Test
  void spawnDeathChestForPlayerSkipsNeighborWhenNotAirAndFindsNextValidFace() {
    ItemStack item = mock(ItemStack.class);
    when(item.getType()).thenReturn(Material.DIRT);

    when(player.getInventory()).thenReturn(playerInventory);
    when(playerInventory.getContents()).thenReturn(new ItemStack[]{item});
    when(playerInventory.getArmorContents()).thenReturn(new ItemStack[0]);
    when(playerInventory.getExtraContents()).thenReturn(new ItemStack[0]);

    Location location = new Location(world, 0, 64, 0);
    when(player.getLocation()).thenReturn(location);
    when(world.getBlockAt(any(Location.class))).thenReturn(originBlock);

    when(originBlock.getRelative(0, 0, 0)).thenReturn(originBlock);
    when(originBlock.getType()).thenReturn(Material.AIR);

    Block solidNeighbor = mock(Block.class);
    when(solidNeighbor.getType()).thenReturn(Material.STONE);

    when(originBlock.getRelative(BlockFace.NORTH)).thenReturn(solidNeighbor);
    when(originBlock.getRelative(BlockFace.EAST)).thenReturn(neighborBlock);
    when(neighborBlock.getType()).thenReturn(Material.AIR);

    when(originBlock.getX()).thenReturn(0);
    when(neighborBlock.getX()).thenReturn(1);

    when(originBlock.getBlockData()).thenReturn(originChestData);
    when(neighborBlock.getBlockData()).thenReturn(neighborChestData);

    when(originBlock.getFace(neighborBlock)).thenReturn(BlockFace.EAST);

    when(originBlock.getState()).thenReturn(chestState);
    when(chestState.getInventory()).thenReturn(doubleChestInventory);
    when(doubleChestInventory.getHolder()).thenReturn(doubleChest);
    when(doubleChest.getInventory()).thenReturn(doubleChestInventory);

    when(originBlock.getLocation()).thenReturn(new Location(world, 0, 64, 0));
    when(neighborBlock.getLocation()).thenReturn(new Location(world, 1, 64, 0));

    when(serviceContext.getPlayerService()).thenReturn(playerService);
    when(serviceContext.getLocationTypeService()).thenReturn(locationTypeService);
    when(serviceContext.getLocationService()).thenReturn(locationService);
    when(serviceContext.getProtectionService()).thenReturn(protectionService);

    when(playerService.getPlayerEntry(player)).thenReturn(playerEntry);
    when(playerEntry.getId()).thenReturn(1);
    when(locationTypeService.findByName(LocationType.PROTECTION))
        .thenReturn(Optional.of(locationTypeEntry));
    when(locationService.saveAndFetch(any(LocationEntry.class))).thenReturn(persistedLocationEntry);

    boolean result = deathChestService.spawnDeathChestForPlayer(player);

    assertAll(
        () -> assertTrue(result),
        () -> verify(originBlock).setType(Material.CHEST),
        () -> verify(neighborBlock).setType(Material.CHEST)
    );
  }

}