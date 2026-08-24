package de.relluem94.minecraft.server.spigot.essentials.registries;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class NpcDialogueRegistryTest {

  private NpcDialogueRegistry npcDialogueRegistry;

  @BeforeEach
  void setUp() {
    npcDialogueRegistry = new NpcDialogueRegistry();
  }

  @Test
  void getOrCreateProgressForNpcCreatesNewMapWhenNpcIsAbsent() {
    UUID npcId = UUID.randomUUID();

    Map<UUID, Integer> result = npcDialogueRegistry.getOrCreateProgressForNpc(npcId);

    assertAll(
        () -> assertNotNull(result),
        () -> assertTrue(result.isEmpty())
    );
  }

  @Test
  void getOrCreateProgressForNpcReturnsSameMapOnSubsequentCalls() {
    UUID npcId = UUID.randomUUID();

    Map<UUID, Integer> firstCall = npcDialogueRegistry.getOrCreateProgressForNpc(npcId);
    firstCall.put(UUID.randomUUID(), 1);
    Map<UUID, Integer> secondCall = npcDialogueRegistry.getOrCreateProgressForNpc(npcId);

    assertAll(
        () -> assertSame(firstCall, secondCall),
        () -> assertEquals(1, secondCall.size())
    );
  }

  @Test
  void getOrCreateProgressForNpcCreatesIndependentMapsForDifferentNpcs() {
    UUID firstNpcId = UUID.randomUUID();
    UUID secondNpcId = UUID.randomUUID();

    Map<UUID, Integer> firstNpcProgress = npcDialogueRegistry.getOrCreateProgressForNpc(firstNpcId);
    Map<UUID, Integer> secondNpcProgress = npcDialogueRegistry.getOrCreateProgressForNpc(secondNpcId);

    assertAll(
        () -> assertNotSame(firstNpcProgress, secondNpcProgress),
        () -> assertTrue(firstNpcProgress.isEmpty()),
        () -> assertTrue(secondNpcProgress.isEmpty())
    );
  }

  @Test
  void getOrCreateProgressForNpcStoresPlayerProgress() {
    UUID npcId = UUID.randomUUID();
    UUID playerId = UUID.randomUUID();

    Map<UUID, Integer> progress = npcDialogueRegistry.getOrCreateProgressForNpc(npcId);
    progress.put(playerId, 5);

    Map<UUID, Integer> retrievedProgress = npcDialogueRegistry.getOrCreateProgressForNpc(npcId);

    assertAll(
        () -> assertEquals(1, retrievedProgress.size()),
        () -> assertEquals(5, retrievedProgress.get(playerId))
    );
  }

  @Test
  void removeNpcDeletesExistingNpcProgressMap() {
    UUID npcId = UUID.randomUUID();
    npcDialogueRegistry.getOrCreateProgressForNpc(npcId);

    npcDialogueRegistry.removeNpc(npcId);

    Map<UUID, Integer> progressAfterRemoval = npcDialogueRegistry.getOrCreateProgressForNpc(npcId);
    assertTrue(progressAfterRemoval.isEmpty());
  }

  @Test
  void removeNpcCreatesNewEmptyMapOnNextAccess() {
    UUID npcId = UUID.randomUUID();
    UUID playerId = UUID.randomUUID();

    Map<UUID, Integer> originalProgress = npcDialogueRegistry.getOrCreateProgressForNpc(npcId);
    originalProgress.put(playerId, 3);

    npcDialogueRegistry.removeNpc(npcId);

    Map<UUID, Integer> newProgress = npcDialogueRegistry.getOrCreateProgressForNpc(npcId);

    assertAll(
        () -> assertNotSame(originalProgress, newProgress),
        () -> assertTrue(newProgress.isEmpty())
    );
  }

  @Test
  void removeNpcOnNonExistentNpcDoesNotThrow() {
    UUID nonExistentNpcId = UUID.randomUUID();

    assertDoesNotThrow(() -> npcDialogueRegistry.removeNpc(nonExistentNpcId));
  }

  @Test
  void getAllNpcProgressMapsReturnsEmptyWhenNoNpcsRegistered() {
    Iterable<Map<UUID, Integer>> allProgressMaps = npcDialogueRegistry.getAllNpcProgressMaps();

    assertFalse(allProgressMaps.iterator().hasNext());
  }

  @Test
  void getAllNpcProgressMapsReturnsAllRegisteredNpcMaps() {
    UUID firstNpcId = UUID.randomUUID();
    UUID secondNpcId = UUID.randomUUID();

    npcDialogueRegistry.getOrCreateProgressForNpc(firstNpcId);
    npcDialogueRegistry.getOrCreateProgressForNpc(secondNpcId);

    Iterable<Map<UUID, Integer>> allProgressMaps = npcDialogueRegistry.getAllNpcProgressMaps();

    int count = 0;
    for (Map<UUID, Integer> ignored : allProgressMaps) {
      count++;
    }

    assertEquals(2, count);
  }

  @Test
  void getAllNpcProgressMapsReflectsProgressDataForEachNpc() {
    UUID npcId = UUID.randomUUID();
    UUID playerId = UUID.randomUUID();

    Map<UUID, Integer> progress = npcDialogueRegistry.getOrCreateProgressForNpc(npcId);
    progress.put(playerId, 7);

    Iterable<Map<UUID, Integer>> allProgressMaps = npcDialogueRegistry.getAllNpcProgressMaps();

    Map<UUID, Integer> retrievedMap = allProgressMaps.iterator().next();

    assertAll(
        () -> assertEquals(1, retrievedMap.size()),
        () -> assertEquals(7, retrievedMap.get(playerId))
    );
  }

  @Test
  void getAllNpcProgressMapsDoesNotIncludeRemovedNpc() {
    UUID npcId = UUID.randomUUID();
    npcDialogueRegistry.getOrCreateProgressForNpc(npcId);

    npcDialogueRegistry.removeNpc(npcId);

    Iterable<Map<UUID, Integer>> allProgressMaps = npcDialogueRegistry.getAllNpcProgressMaps();

    assertFalse(allProgressMaps.iterator().hasNext());
  }
}