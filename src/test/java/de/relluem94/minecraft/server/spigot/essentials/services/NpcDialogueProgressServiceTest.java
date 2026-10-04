package de.relluem94.minecraft.server.spigot.essentials.services;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.relluem94.minecraft.server.spigot.essentials.registries.NpcDialogueRegistry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class NpcDialogueProgressServiceTest {

  @Mock
  private NpcDialogueRegistry npcDialogueRegistry;

  @InjectMocks
  private NpcDialogueProgressService npcDialogueProgressService;

  @Test
  void getNextLineIndexAndAdvanceReturnsZeroForNewPlayer() {
    UUID npcId = UUID.randomUUID();
    UUID playerId = UUID.randomUUID();
    Map<UUID, Integer> progressMap = new HashMap<>();

    when(npcDialogueRegistry.getOrCreateProgressForNpc(npcId)).thenReturn(progressMap);

    int result = npcDialogueProgressService.getNextLineIndexAndAdvance(npcId, playerId, 5);

    assertAll(
        () -> assertEquals(0, result),
        () -> assertEquals(1, progressMap.get(playerId))
    );
  }

  @Test
  void getNextLineIndexAndAdvanceReturnsCurrentIndexAndAdvances() {
    UUID npcId = UUID.randomUUID();
    UUID playerId = UUID.randomUUID();
    Map<UUID, Integer> progressMap = new HashMap<>();
    progressMap.put(playerId, 2);

    when(npcDialogueRegistry.getOrCreateProgressForNpc(npcId)).thenReturn(progressMap);

    int result = npcDialogueProgressService.getNextLineIndexAndAdvance(npcId, playerId, 5);

    assertAll(
        () -> assertEquals(2, result),
        () -> assertEquals(3, progressMap.get(playerId))
    );
  }

  @Test
  void getNextLineIndexAndAdvanceWrapsAroundAtLastLine() {
    UUID npcId = UUID.randomUUID();
    UUID playerId = UUID.randomUUID();
    Map<UUID, Integer> progressMap = new HashMap<>();
    progressMap.put(playerId, 4);

    when(npcDialogueRegistry.getOrCreateProgressForNpc(npcId)).thenReturn(progressMap);

    int result = npcDialogueProgressService.getNextLineIndexAndAdvance(npcId, playerId, 5);

    assertAll(
        () -> assertEquals(4, result),
        () -> assertEquals(0, progressMap.get(playerId))
    );
  }

  @Test
  void getNextLineIndexAndAdvancePropagatesRegistryException() {
    UUID npcId = UUID.randomUUID();
    UUID playerId = UUID.randomUUID();

    when(npcDialogueRegistry.getOrCreateProgressForNpc(npcId)).thenThrow(new RuntimeException("registry failure"));

    assertThrows(RuntimeException.class,
        () -> npcDialogueProgressService.getNextLineIndexAndAdvance(npcId, playerId, 5));
  }

  @Test
  void resetPlayerProgressRemovesPlayerFromAllNpcMaps() {
    UUID playerId = UUID.randomUUID();
    Map<UUID, Integer> firstProgressMap = new HashMap<>();
    firstProgressMap.put(playerId, 1);
    Map<UUID, Integer> secondProgressMap = new HashMap<>();
    secondProgressMap.put(playerId, 3);

    List<Map<UUID, Integer>> allProgressMaps = new ArrayList<>();
    allProgressMaps.add(firstProgressMap);
    allProgressMaps.add(secondProgressMap);

    when(npcDialogueRegistry.getAllNpcProgressMaps()).thenReturn(allProgressMaps);

    npcDialogueProgressService.resetPlayerProgress(playerId);

    assertAll(
        () -> assertFalse(firstProgressMap.containsKey(playerId)),
        () -> assertFalse(secondProgressMap.containsKey(playerId))
    );
  }

  @Test
  void resetPlayerProgressDoesNotAffectOtherPlayers() {
    UUID playerId = UUID.randomUUID();
    UUID otherPlayerId = UUID.randomUUID();
    Map<UUID, Integer> progressMap = new HashMap<>();
    progressMap.put(playerId, 2);
    progressMap.put(otherPlayerId, 1);

    when(npcDialogueRegistry.getAllNpcProgressMaps()).thenReturn(List.of(progressMap));

    npcDialogueProgressService.resetPlayerProgress(playerId);

    assertAll(
        () -> assertFalse(progressMap.containsKey(playerId)),
        () -> assertTrue(progressMap.containsKey(otherPlayerId))
    );
  }

  @Test
  void resetPlayerProgressPropagatesRegistryException() {
    UUID playerId = UUID.randomUUID();

    when(npcDialogueRegistry.getAllNpcProgressMaps()).thenThrow(new RuntimeException("registry failure"));

    assertThrows(RuntimeException.class,
        () -> npcDialogueProgressService.resetPlayerProgress(playerId));
  }

  @Test
  void removeNpcDelegatesRemovalToRegistry() {
    UUID npcId = UUID.randomUUID();

    npcDialogueProgressService.removeNpc(npcId);

    verify(npcDialogueRegistry, times(1)).removeNpc(npcId);
  }

  @Test
  void removeNpcPropagatesRegistryException() {
    UUID npcId = UUID.randomUUID();

    doThrow(new RuntimeException("registry failure")).when(npcDialogueRegistry).removeNpc(npcId);

    assertThrows(RuntimeException.class,
        () -> npcDialogueProgressService.removeNpc(npcId));
  }
}