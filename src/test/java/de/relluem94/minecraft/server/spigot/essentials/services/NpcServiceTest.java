package de.relluem94.minecraft.server.spigot.essentials.services;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.relluem94.minecraft.server.spigot.essentials.helpers.InventoryHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.NpcEquipmentInventoryHelper;
import de.relluem94.minecraft.server.spigot.essentials.models.Npc;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.NpcDialogueEntry;
import de.relluem94.minecraft.server.spigot.essentials.npcs.NpcOperationResult;
import de.relluem94.minecraft.server.spigot.essentials.npcs.NpcSpawner;
import de.relluem94.minecraft.server.spigot.essentials.npcs.NpcValidator;
import de.relluem94.minecraft.server.spigot.essentials.repositories.NpcRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.inventory.Inventory;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class NpcServiceTest {

  @Mock
  private NpcRepository npcRepository;

  @Mock
  private NpcSpawner npcSpawner;

  @Mock
  private NpcValidator npcValidator;

  @Mock
  private Inventory inventory;

  @Mock
  private World world;

  private NpcService npcService;

  @BeforeEach
  void setUp() {
    npcService = new NpcService(npcRepository, npcSpawner, npcValidator);
  }

  private Npc buildNpc(UUID id, UUID entityUUID) {
    Npc npc = new Npc(-1, id, "TestProfile", 1.0, 64.0, 1.0, 0f, 0f, "world");
    npc.setEntityUUID(entityUUID);
    return npc;
  }

  @Test
  void isTrackedNpcEntityReturnsTrueWhenEntityIsTracked() {
    UUID npcId = UUID.randomUUID();
    UUID entityUUID = UUID.randomUUID();
    Npc npc = buildNpc(npcId, entityUUID);

    when(npcValidator.validateProfileName(anyString()))
        .thenReturn(new NpcValidator.ValidationResult(true, null));
    when(npcValidator.validateCoordinates(anyDouble(), anyDouble(), anyDouble()))
        .thenReturn(new NpcValidator.ValidationResult(true, null));
    when(npcSpawner.spawnMannequin(any())).thenReturn(Optional.of(entityUUID));

    npcService.createNPC("TestProfile", 1.0, 64.0, 1.0, 0f, 0f, "world", 1);

    assertTrue(npcService.isTrackedNpcEntity(entityUUID));
  }

  @Test
  void isTrackedNpcEntityReturnsFalseWhenEntityIsNotTracked() {
    assertFalse(npcService.isTrackedNpcEntity(UUID.randomUUID()));
  }

  @Test
  void createNPCReturnsSuccessWhenValidInput() {
    UUID entityUUID = UUID.randomUUID();

    when(npcValidator.validateProfileName("TestProfile"))
        .thenReturn(new NpcValidator.ValidationResult(true, null));
    when(npcValidator.validateCoordinates(1.0, 64.0, 1.0))
        .thenReturn(new NpcValidator.ValidationResult(true, null));
    when(npcSpawner.spawnMannequin(any())).thenReturn(Optional.of(entityUUID));

    NpcOperationResult result = npcService.createNPC("TestProfile", 1.0, 64.0, 1.0, 0f, 0f, "world", 1);

    assertAll(
        () -> assertTrue(result.isSuccessful()),
        () -> assertNotNull(result.getNpc()),
        () -> assertEquals("TestProfile", result.getNpc().getProfileName()),
        () -> assertEquals(1.0, result.getNpc().getX()),
        () -> assertEquals(64.0, result.getNpc().getY()),
        () -> assertEquals(1.0, result.getNpc().getZ()),
        () -> assertEquals("world", result.getNpc().getWorldName())
    );

    verify(npcRepository, atLeastOnce()).save(any(Npc.class), eq(1));
  }

  @Test
  void createNPCReturnsFailureWhenProfileNameInvalid() {
    when(npcValidator.validateProfileName("bad"))
        .thenReturn(new NpcValidator.ValidationResult(false, "Invalid profile name."));

    NpcOperationResult result = npcService.createNPC("bad", 1.0, 64.0, 1.0, 0f, 0f, "world", 1);

    assertAll(
        () -> assertFalse(result.isSuccessful()),
        () -> assertEquals("Invalid profile name.", result.getErrorMessage())
    );
  }

  @Test
  void createNPCReturnsFailureWhenCoordinatesInvalid() {
    when(npcValidator.validateProfileName("TestProfile"))
        .thenReturn(new NpcValidator.ValidationResult(true, null));
    when(npcValidator.validateCoordinates(Double.NaN, 0, 0))
        .thenReturn(new NpcValidator.ValidationResult(false, "Invalid coordinates."));

    NpcOperationResult result = npcService.createNPC("TestProfile", Double.NaN, 0, 0, 0f, 0f, "world", 1);

    assertAll(
        () -> assertFalse(result.isSuccessful()),
        () -> assertEquals("Invalid coordinates.", result.getErrorMessage())
    );
  }

  @Test
  void updateNPCProfileReturnsSuccessAndRespawns() {
    UUID npcId = UUID.randomUUID();
    UUID oldEntityUUID = UUID.randomUUID();
    UUID newEntityUUID = UUID.randomUUID();

    Npc npc = buildNpc(npcId, oldEntityUUID);
    npc.setInventory(new JSONObject("{\"items\":[]}"));

    loadNpcIntoService(npcId, npc);

    when(npcValidator.validateProfileName("NewProfile"))
        .thenReturn(new NpcValidator.ValidationResult(true, null));
    when(npcSpawner.spawnMannequin(any())).thenReturn(Optional.of(newEntityUUID));

    try (MockedStatic<Bukkit> bukkit = mockStatic(Bukkit.class);
        MockedStatic<InventoryHelper> inventoryHelper = mockStatic(InventoryHelper.class);
        MockedStatic<NpcEquipmentInventoryHelper> equipmentHelper = mockStatic(NpcEquipmentInventoryHelper.class)) {

      bukkit.when(() -> Bukkit.createInventory(isNull(), eq(54))).thenReturn(inventory);
      inventoryHelper.when(() -> InventoryHelper.loadInventoryFromJSON(any(), any())).thenAnswer(inv -> null);
      equipmentHelper.when(() -> NpcEquipmentInventoryHelper.applyInventoryEquipmentToEntity(any(), any())).thenAnswer(inv -> null);

      NpcOperationResult result = npcService.updateNPCProfile(npcId, "NewProfile", 1);

      assertAll(
          () -> assertTrue(result.isSuccessful()),
          () -> assertEquals("NewProfile", result.getNpc().getProfileName()),
          () -> assertEquals(newEntityUUID, result.getNpc().getEntityUUID())
      );
    }

    verify(npcSpawner).despawnMannequin(oldEntityUUID);
    verify(npcRepository, atLeastOnce()).save(any(Npc.class), eq(1));
  }

  @Test
  void updateNPCProfileReturnsFailureWhenNpcNotFound() {
    NpcOperationResult result = npcService.updateNPCProfile(UUID.randomUUID(), "NewProfile", 1);

    assertFalse(result.isSuccessful());
  }

  @Test
  void updateNPCProfileReturnsFailureWhenProfileNameInvalid() {
    UUID npcId = UUID.randomUUID();
    Npc npc = buildNpc(npcId, null);
    loadNpcIntoService(npcId, npc);

    when(npcValidator.validateProfileName("bad"))
        .thenReturn(new NpcValidator.ValidationResult(false, "Invalid profile name."));

    NpcOperationResult result = npcService.updateNPCProfile(npcId, "bad", 1);

    assertFalse(result.isSuccessful());
  }

  @Test
  void updateNPCPositionReturnsSuccessAndRespawns() {
    UUID npcId = UUID.randomUUID();
    UUID oldEntityUUID = UUID.randomUUID();
    UUID newEntityUUID = UUID.randomUUID();

    Npc npc = buildNpc(npcId, oldEntityUUID);
    loadNpcIntoService(npcId, npc);

    when(npcValidator.validateCoordinates(10.0, 65.0, 10.0))
        .thenReturn(new NpcValidator.ValidationResult(true, null));
    when(npcSpawner.spawnMannequin(any())).thenReturn(Optional.of(newEntityUUID));

    NpcOperationResult result = npcService.updateNPCPosition(npcId, 10.0, 65.0, 10.0, 90f, 10f, 1);

    assertAll(
        () -> assertTrue(result.isSuccessful()),
        () -> assertEquals(10.0, result.getNpc().getX()),
        () -> assertEquals(65.0, result.getNpc().getY()),
        () -> assertEquals(10.0, result.getNpc().getZ()),
        () -> assertEquals(90f, result.getNpc().getYaw()),
        () -> assertEquals(10f, result.getNpc().getPitch()),
        () -> assertEquals(newEntityUUID, result.getNpc().getEntityUUID())
    );

    verify(npcSpawner).despawnMannequin(oldEntityUUID);
  }

  @Test
  void updateNPCPositionReturnsFailureWhenNpcNotFound() {
    NpcOperationResult result = npcService.updateNPCPosition(UUID.randomUUID(), 0, 0, 0, 0, 0, 1);

    assertFalse(result.isSuccessful());
  }

  @Test
  void updateNPCPositionReturnsFailureWhenCoordinatesInvalid() {
    UUID npcId = UUID.randomUUID();
    Npc npc = buildNpc(npcId, null);
    loadNpcIntoService(npcId, npc);

    when(npcValidator.validateCoordinates(Double.NaN, 0, 0))
        .thenReturn(new NpcValidator.ValidationResult(false, "Invalid coordinates."));

    NpcOperationResult result = npcService.updateNPCPosition(npcId, Double.NaN, 0, 0, 0, 0, 1);

    assertFalse(result.isSuccessful());
  }

  @Test
  void saveNPCInventorySavesSerializedInventory() {
    UUID npcId = UUID.randomUUID();
    Npc npc = buildNpc(npcId, null);

    try (MockedStatic<InventoryHelper> inventoryHelper = mockStatic(InventoryHelper.class)) {
      inventoryHelper.when(() -> InventoryHelper.saveInventoryToJSON(inventory)).thenReturn(new JSONObject("{\"items\":[]}"));

      npcService.saveNPCInventory(npc, inventory);

      assertEquals("{\"items\":[]}", npc.getInventory().toString());
    }

    verify(npcRepository).save(npc, -1);
  }

  @Test
  void deleteNPCReturnsSuccessAndDespawns() {
    UUID npcId = UUID.randomUUID();
    UUID entityUUID = UUID.randomUUID();
    Npc npc = buildNpc(npcId, entityUUID);
    loadNpcIntoService(npcId, npc);

    NpcOperationResult result = npcService.deleteNPC(npcId, 1);

    assertAll(
        () -> assertTrue(result.isSuccessful()),
        () -> assertEquals(npc, result.getNpc())
    );

    verify(npcSpawner).despawnMannequin(entityUUID);
    verify(npcRepository).delete(npcId, 1);
    assertFalse(npcService.getNPCById(npcId).isPresent());
  }

  @Test
  void deleteNPCReturnsFailureWhenNpcNotFound() {
    NpcOperationResult result = npcService.deleteNPC(UUID.randomUUID(), 1);

    assertFalse(result.isSuccessful());
  }

  @Test
  void despawnAllNPCsDespawnsAllTrackedNpcs() {
    UUID npcId1 = UUID.randomUUID();
    UUID entityUUID1 = UUID.randomUUID();
    UUID npcId2 = UUID.randomUUID();
    UUID entityUUID2 = UUID.randomUUID();

    loadNpcIntoService(npcId1, buildNpc(npcId1, entityUUID1));
    loadNpcIntoService(npcId2, buildNpc(npcId2, entityUUID2));

    npcService.despawnAllNPCs();

    verify(npcSpawner).despawnMannequin(entityUUID1);
    verify(npcSpawner).despawnMannequin(entityUUID2);
    assertTrue(npcService.getNPCs().isEmpty());
  }

  @Test
  void reloadNPCDialogueUpdatesDialogueLines() {
    UUID npcId = UUID.randomUUID();
    Npc npc = buildNpc(npcId, null);
    loadNpcIntoService(npcId, npc);

    List<NpcDialogueEntry> refreshedDialogue = List.of(new NpcDialogueEntry());
    Npc refreshedNpc = buildNpc(npcId, null);
    refreshedNpc.setDialogueLines(refreshedDialogue);

    when(npcRepository.loadById(npcId)).thenReturn(Optional.of(refreshedNpc));

    npcService.reloadNPCDialogue(npcId);

    assertEquals(refreshedDialogue, npc.getDialogueLines());
  }

  @Test
  void reloadNPCDialogueDoesNothingWhenNpcNotFound() {
    npcService.reloadNPCDialogue(UUID.randomUUID());

    verify(npcRepository, never()).loadById(any());
  }

  @Test
  void getNPCsReturnsAllLoadedNpcs() {
    UUID npcId1 = UUID.randomUUID();
    UUID npcId2 = UUID.randomUUID();
    loadNpcIntoService(npcId1, buildNpc(npcId1, null));
    loadNpcIntoService(npcId2, buildNpc(npcId2, null));

    List<Npc> result = npcService.getNPCs();

    assertEquals(2, result.size());
  }

  @Test
  void getNPCByIdReturnsPresentWhenNpcLoaded() {
    UUID npcId = UUID.randomUUID();
    Npc npc = buildNpc(npcId, null);
    loadNpcIntoService(npcId, npc);

    Optional<Npc> result = npcService.getNPCById(npcId);

    assertAll(
        () -> assertTrue(result.isPresent()),
        () -> assertEquals(npc, result.get())
    );
  }

  @Test
  void getNPCByIdReturnsEmptyWhenNpcNotLoaded() {
    assertTrue(npcService.getNPCById(UUID.randomUUID()).isEmpty());
  }

  @Test
  void getNearestNPCReturnsClosestNpcInSameWorld() {
    UUID npcId1 = UUID.randomUUID();
    UUID npcId2 = UUID.randomUUID();

    Npc nearNpc = new Npc(-1, npcId1, "Near", 5.0, 64.0, 5.0, 0f, 0f, "world");
    Npc farNpc = new Npc(-1, npcId2, "Far", 100.0, 64.0, 100.0, 0f, 0f, "world");

    loadNpcIntoService(npcId1, nearNpc);
    loadNpcIntoService(npcId2, farNpc);

    Optional<Npc> result = npcService.getNearestNPC(0.0, 64.0, 0.0, "world");

    assertAll(
        () -> assertTrue(result.isPresent()),
        () -> assertEquals(nearNpc, result.get())
    );
  }

  @Test
  void getNearestNPCReturnsEmptyWhenNoNpcsInWorld() {
    UUID npcId = UUID.randomUUID();
    loadNpcIntoService(npcId, new Npc(-1, npcId, "Test", 5.0, 64.0, 5.0, 0f, 0f, "other_world"));

    Optional<Npc> result = npcService.getNearestNPC(0.0, 64.0, 0.0, "world");

    assertTrue(result.isEmpty());
  }

  @Test
  void spawnNpcAddsToLoadedNpcsAndSetsEntityUUID() {
    UUID npcId = UUID.randomUUID();
    UUID entityUUID = UUID.randomUUID();
    Npc npc = buildNpc(npcId, null);

    when(npcSpawner.spawnMannequin(npc)).thenReturn(Optional.of(entityUUID));

    npcService.spawnNpc(npc);

    assertAll(
        () -> assertEquals(entityUUID, npc.getEntityUUID()),
        () -> assertTrue(npcService.getNPCById(npcId).isPresent())
    );
  }

  @Test
  void spawnNpcDoesNotOverwriteExistingLoadedNpc() {
    UUID npcId = UUID.randomUUID();
    UUID entityUUID = UUID.randomUUID();
    Npc existingNpc = buildNpc(npcId, null);
    Npc newNpc = buildNpc(npcId, null);

    loadNpcIntoService(npcId, existingNpc);
    when(npcSpawner.spawnMannequin(newNpc)).thenReturn(Optional.of(entityUUID));

    npcService.spawnNpc(newNpc);

    assertSame(existingNpc, npcService.getNPCById(npcId).get());
  }

  @Test
  void despawnNpcClearsEntityUUID() {
    UUID npcId = UUID.randomUUID();
    UUID entityUUID = UUID.randomUUID();
    Npc npc = buildNpc(npcId, entityUUID);
    loadNpcIntoService(npcId, npc);

    npcService.despawnNpc(npcId);

    verify(npcSpawner).despawnMannequin(entityUUID);
    assertNull(npc.getEntityUUID());
  }

  @Test
  void despawnNpcDoesNothingWhenNpcNotFound() {
    npcService.despawnNpc(UUID.randomUUID());

    verify(npcSpawner, never()).despawnMannequin(any());
  }

  @Test
  void getAllNpcsReturnsAllLoadedNpcs() {
    UUID npcId1 = UUID.randomUUID();
    UUID npcId2 = UUID.randomUUID();
    loadNpcIntoService(npcId1, buildNpc(npcId1, null));
    loadNpcIntoService(npcId2, buildNpc(npcId2, null));

    assertEquals(2, npcService.getAllNpcs().size());
  }

  @Test
  void loadAndSpawnNpcsInLoadedChunksSpawnsNpcsInLoadedChunks() {
    UUID npcId = UUID.randomUUID();
    UUID entityUUID = UUID.randomUUID();
    Npc npc = new Npc(-1, npcId, "Test", 16.0, 64.0, 16.0, 0f, 0f, "world");

    when(npcRepository.loadAll()).thenReturn(List.of(npc));
    when(npcSpawner.spawnMannequin(npc)).thenReturn(Optional.of(entityUUID));

    try (MockedStatic<Bukkit> bukkit = mockStatic(Bukkit.class)) {
      bukkit.when(() -> Bukkit.getWorld("world")).thenReturn(world);
      when(world.isChunkLoaded(1, 1)).thenReturn(true);

      npcService.loadAndSpawnNpcsInLoadedChunks();
    }

    assertAll(
        () -> assertTrue(npcService.getNPCById(npcId).isPresent()),
        () -> assertEquals(entityUUID, npc.getEntityUUID())
    );
  }

  @Test
  void loadAndSpawnNpcsInLoadedChunksSkipsNpcsInUnloadedChunks() {
    UUID npcId = UUID.randomUUID();
    Npc npc = new Npc(-1, npcId, "Test", 16.0, 64.0, 16.0, 0f, 0f, "world");

    when(npcRepository.loadAll()).thenReturn(List.of(npc));

    try (MockedStatic<Bukkit> bukkit = mockStatic(Bukkit.class)) {
      bukkit.when(() -> Bukkit.getWorld("world")).thenReturn(world);
      when(world.isChunkLoaded(1, 1)).thenReturn(false);

      npcService.loadAndSpawnNpcsInLoadedChunks();
    }

    verify(npcSpawner, never()).spawnMannequin(any());
  }

  @Test
  void loadAndSpawnNpcsInLoadedChunksSkipsNpcsWithMissingWorld() {
    UUID npcId = UUID.randomUUID();
    Npc npc = new Npc(-1, npcId, "Test", 16.0, 64.0, 16.0, 0f, 0f, "missing_world");

    when(npcRepository.loadAll()).thenReturn(List.of(npc));

    try (MockedStatic<Bukkit> bukkit = mockStatic(Bukkit.class)) {
      bukkit.when(() -> Bukkit.getWorld("missing_world")).thenReturn(null);

      npcService.loadAndSpawnNpcsInLoadedChunks();
    }

    verify(npcSpawner, never()).spawnMannequin(any());
  }

  @Test
  void getNPCDialoguesReturnsDialoguesFromRepository() {
    List<NpcDialogueEntry> entries = List.of(new NpcDialogueEntry());
    when(npcRepository.loadDialoguesByNpcDbId(42)).thenReturn(entries);

    List<NpcDialogueEntry> result = npcService.getNPCDialogues(42);

    assertEquals(entries, result);
  }

  @Test
  void addNPCDialogueDelegatesToRepository() {
    NpcDialogueEntry entry = new NpcDialogueEntry();

    npcService.addNPCDialogue(entry);

    verify(npcRepository).addDialogue(entry);
  }

  @Test
  void updateNPCDialogueDelegatesToRepository() {
    NpcDialogueEntry entry = new NpcDialogueEntry();
    UUID dialogueUuid = UUID.randomUUID();
    when(npcRepository.updateDialogue(entry, dialogueUuid)).thenReturn(true);

    boolean result = npcService.updateNPCDialogue(entry, dialogueUuid);

    assertTrue(result);
    verify(npcRepository).updateDialogue(entry, dialogueUuid);
  }

  @Test
  void deleteNPCDialogueByPositionDelegatesToRepository() {
    UUID npcId = UUID.randomUUID();
    Npc npc = buildNpc(npcId, null);
    loadNpcIntoService(npcId, npc);

    npcService.deleteNPCDialogueByPosition(npcId, 2, 1);

    verify(npcRepository).deleteDialogueByPosition(npcId, 2, 1);
  }

  @Test
  void deleteNPCDialogueByPositionDoesNothingWhenNpcNotFound() {
    npcService.deleteNPCDialogueByPosition(UUID.randomUUID(), 0, 1);

    verify(npcRepository, never()).deleteDialogueByPosition(any(), anyInt(), anyInt());
  }

  @Test
  void deleteNPCDoesNotDespawnWhenEntityUUIDIsNull() {
    UUID npcId = UUID.randomUUID();
    Npc npc = buildNpc(npcId, null);
    loadNpcIntoService(npcId, npc);

    NpcOperationResult result = npcService.deleteNPC(npcId, 1);

    assertTrue(result.isSuccessful());
    verify(npcSpawner, never()).despawnMannequin(any());
  }

  @Test
  void restoreNPCEquipmentDoesNothingWhenInventoryIsNull() {
    UUID npcId = UUID.randomUUID();
    UUID oldEntityUUID = UUID.randomUUID();
    UUID newEntityUUID = UUID.randomUUID();

    Npc npc = buildNpc(npcId, oldEntityUUID);
    loadNpcIntoService(npcId, npc);

    when(npcValidator.validateProfileName("NewProfile"))
        .thenReturn(new NpcValidator.ValidationResult(true, null));
    when(npcSpawner.spawnMannequin(any())).thenReturn(Optional.of(newEntityUUID));

    NpcOperationResult result = npcService.updateNPCProfile(npcId, "NewProfile", 1);

    assertTrue(result.isSuccessful());
    verify(npcSpawner, never()).despawnMannequin(newEntityUUID);
  }

  @Test
  void despawnAllNPCsSkipsNpcsWithNullEntityUUID() {
    UUID npcId1 = UUID.randomUUID();
    UUID entityUUID1 = UUID.randomUUID();
    UUID npcId2 = UUID.randomUUID();

    loadNpcIntoService(npcId1, buildNpc(npcId1, entityUUID1));
    loadNpcIntoService(npcId2, buildNpc(npcId2, null));

    npcService.despawnAllNPCs();

    verify(npcSpawner).despawnMannequin(entityUUID1);
    verify(npcSpawner, never()).despawnMannequin(null);
    assertTrue(npcService.getNPCs().isEmpty());
  }

  @Test
  void despawnNpcDoesNothingWhenEntityUUIDIsNull() {
    UUID npcId = UUID.randomUUID();
    Npc npc = buildNpc(npcId, null);
    loadNpcIntoService(npcId, npc);

    npcService.despawnNpc(npcId);

    verify(npcSpawner, never()).despawnMannequin(any());
  }

  @Test
  void updateNPCPositionDoesNotDespawnWhenEntityUUIDIsNull() {
    UUID npcId = UUID.randomUUID();
    Npc npc = buildNpc(npcId, null);
    loadNpcIntoService(npcId, npc);

    UUID newEntityUUID = UUID.randomUUID();

    when(npcValidator.validateCoordinates(10.0, 65.0, 10.0))
        .thenReturn(new NpcValidator.ValidationResult(true, null));
    when(npcSpawner.spawnMannequin(any())).thenReturn(Optional.of(newEntityUUID));

    NpcOperationResult result = npcService.updateNPCPosition(npcId, 10.0, 65.0, 10.0, 90f, 10f, 1);

    assertTrue(result.isSuccessful());
    verify(npcSpawner, never()).despawnMannequin(any());
  }

  @Test
  void restoreNPCEquipmentDoesNothingWhenEntityUUIDIsNullAfterRespawn() {
    UUID npcId = UUID.randomUUID();
    UUID oldEntityUUID = UUID.randomUUID();

    Npc npc = buildNpc(npcId, oldEntityUUID);
    npc.setInventory(new JSONObject("{\"items\":[]}"));
    loadNpcIntoService(npcId, npc);

    when(npcValidator.validateProfileName("NewProfile"))
        .thenReturn(new NpcValidator.ValidationResult(true, null));
    when(npcSpawner.spawnMannequin(any())).thenReturn(Optional.empty());

    NpcOperationResult result = npcService.updateNPCProfile(npcId, "NewProfile", 1);

    assertTrue(result.isSuccessful());
    verify(npcSpawner).despawnMannequin(oldEntityUUID);
  }

  @Test
  void restoreNPCEquipmentDoesNothingWhenInventoryIsNullOnPositionUpdate() {
    UUID npcId = UUID.randomUUID();
    UUID oldEntityUUID = UUID.randomUUID();
    UUID newEntityUUID = UUID.randomUUID();

    Npc npc = buildNpc(npcId, oldEntityUUID);
    loadNpcIntoService(npcId, npc);

    when(npcValidator.validateCoordinates(10.0, 65.0, 10.0))
        .thenReturn(new NpcValidator.ValidationResult(true, null));
    when(npcSpawner.spawnMannequin(any())).thenReturn(Optional.of(newEntityUUID));

    NpcOperationResult result = npcService.updateNPCPosition(npcId, 10.0, 65.0, 10.0, 90f, 10f, 1);

    assertTrue(result.isSuccessful());
    verify(npcSpawner).despawnMannequin(oldEntityUUID);
    assertEquals(newEntityUUID, result.getNpc().getEntityUUID());
  }

  @Test
  void updateNPCProfileDoesNotDespawnWhenEntityUUIDIsNull() {
    UUID npcId = UUID.randomUUID();
    UUID newEntityUUID = UUID.randomUUID();

    Npc npc = buildNpc(npcId, null);
    loadNpcIntoService(npcId, npc);

    when(npcValidator.validateProfileName("NewProfile"))
        .thenReturn(new NpcValidator.ValidationResult(true, null));
    when(npcSpawner.spawnMannequin(any())).thenReturn(Optional.of(newEntityUUID));

    NpcOperationResult result = npcService.updateNPCProfile(npcId, "NewProfile", 1);

    assertTrue(result.isSuccessful());
    verify(npcSpawner, never()).despawnMannequin(any());
  }

  @Test
  void restoreNPCEquipmentDoesNothingWhenInventorySetButEntityUUIDNullAfterRespawn() {
    UUID npcId = UUID.randomUUID();
    UUID oldEntityUUID = UUID.randomUUID();

    Npc npc = buildNpc(npcId, null);
    npc.setInventory(null);

    loadNpcIntoService(npcId, npc);
    npc.setEntityUUID(oldEntityUUID);

    when(npcValidator.validateProfileName("NewProfile"))
        .thenReturn(new NpcValidator.ValidationResult(true, null));
    when(npcSpawner.spawnMannequin(any())).thenReturn(Optional.empty());

    NpcOperationResult result = npcService.updateNPCProfile(npcId, "NewProfile", 1);

    assertTrue(result.isSuccessful());
    verify(npcSpawner).despawnMannequin(oldEntityUUID);
    assertEquals(oldEntityUUID, npc.getEntityUUID());
    assertNull(npc.getInventory());
  }

  @Test
  void restoreNPCEquipmentDoesNothingWhenInventorySetButInventoryIsNullAfterRespawn() {
    UUID npcId = UUID.randomUUID();

    Npc npc = buildNpc(npcId, null);
    npc.setInventory(null);
    loadNpcIntoService(npcId, npc);
    npc.setEntityUUID(null);

    when(npcValidator.validateProfileName("NewProfile"))
        .thenReturn(new NpcValidator.ValidationResult(true, null));
    when(npcSpawner.spawnMannequin(any())).thenReturn(Optional.empty());

    NpcOperationResult result = npcService.updateNPCProfile(npcId, "NewProfile", 1);

    assertTrue(result.isSuccessful());
    verify(npcSpawner, never()).despawnMannequin(any());
    assertNull(npc.getEntityUUID());
  }


  @Test
  void restoreNPCEquipmentDoesNothingWhenInventoryNullButEntityUUIDSet() {
    UUID npcId = UUID.randomUUID();
    UUID oldEntityUUID = UUID.randomUUID();
    UUID newEntityUUID = UUID.randomUUID();

    Npc npc = buildNpc(npcId, oldEntityUUID);
    loadNpcIntoService(npcId, npc);

    when(npcValidator.validateProfileName("NewProfile"))
        .thenReturn(new NpcValidator.ValidationResult(true, null));
    when(npcSpawner.spawnMannequin(any())).thenReturn(Optional.of(newEntityUUID));

    NpcOperationResult result = npcService.updateNPCProfile(npcId, "NewProfile", 1);

    assertTrue(result.isSuccessful());
    assertEquals(newEntityUUID, result.getNpc().getEntityUUID());
    verify(npcSpawner).despawnMannequin(oldEntityUUID);
  }

  private void loadNpcIntoService(UUID npcId, Npc npc) {
    when(npcRepository.loadAll()).thenReturn(List.of(npc));

    try (MockedStatic<Bukkit> bukkit = mockStatic(Bukkit.class)) {
      bukkit.when(() -> Bukkit.getWorld(anyString())).thenReturn(world);
      when(world.isChunkLoaded(anyInt(), anyInt())).thenReturn(false);

      npcService.loadAndSpawnNpcsInLoadedChunks();
    }
  }
}