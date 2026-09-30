package de.relluem94.minecraft.server.spigot.essentials.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import de.relluem94.minecraft.server.spigot.essentials.models.pojo.ModifyHistoryEntry;
import java.util.List;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UndoHistoryRepositoryTest {

  @Mock
  private Player player;

  @Mock
  private Player otherPlayer;

  @Mock
  private ModifyHistoryEntry modifyHistoryEntry;

  private UndoHistoryRepository undoHistoryRepository;

  @BeforeEach
  void setUp() {
    undoHistoryRepository = new UndoHistoryRepository();
  }

  @Test
  void addStoresHistoryEntryForPlayer() {
    List<ModifyHistoryEntry> history = List.of(modifyHistoryEntry);

    undoHistoryRepository.add(player, history);

    List<List<ModifyHistoryEntry>> result = undoHistoryRepository.findByPlayer(player);
    assertEquals(1, result.size());
    assertEquals(history, result.getFirst());
  }

  @Test
  void addStoresMultipleHistoryEntriesForSamePlayer() {
    List<ModifyHistoryEntry> firstHistory = List.of(modifyHistoryEntry);
    List<ModifyHistoryEntry> secondHistory = List.of(modifyHistoryEntry);

    undoHistoryRepository.add(player, firstHistory);
    undoHistoryRepository.add(player, secondHistory);

    List<List<ModifyHistoryEntry>> result = undoHistoryRepository.findByPlayer(player);
    assertEquals(2, result.size());
  }

  @Test
  void addStoresHistoryEntriesSeparatelyPerPlayer() {
    List<ModifyHistoryEntry> playerHistory = List.of(modifyHistoryEntry);
    List<ModifyHistoryEntry> otherPlayerHistory = List.of(modifyHistoryEntry);

    undoHistoryRepository.add(player, playerHistory);
    undoHistoryRepository.add(otherPlayer, otherPlayerHistory);

    assertEquals(1, undoHistoryRepository.findByPlayer(player).size());
    assertEquals(1, undoHistoryRepository.findByPlayer(otherPlayer).size());
  }

  @Test
  void findByPlayerReturnsEmptyListWhenPlayerHasNoHistory() {
    List<List<ModifyHistoryEntry>> result = undoHistoryRepository.findByPlayer(player);

    assertTrue(result.isEmpty());
  }

  @Test
  void findByPlayerReturnsAllEntriesForPlayer() {
    List<ModifyHistoryEntry> firstHistory = List.of(modifyHistoryEntry);
    List<ModifyHistoryEntry> secondHistory = List.of(modifyHistoryEntry);

    undoHistoryRepository.add(player, firstHistory);
    undoHistoryRepository.add(player, secondHistory);

    List<List<ModifyHistoryEntry>> result = undoHistoryRepository.findByPlayer(player);
    assertEquals(2, result.size());
    assertEquals(firstHistory, result.get(0));
    assertEquals(secondHistory, result.get(1));
  }

  @Test
  void findByPlayerDoesNotReturnEntriesOfOtherPlayer() {
    undoHistoryRepository.add(otherPlayer, List.of(modifyHistoryEntry));

    List<List<ModifyHistoryEntry>> result = undoHistoryRepository.findByPlayer(player);

    assertTrue(result.isEmpty());
  }

  @Test
  void removeLastRemovesMostRecentHistoryEntryForPlayer() {
    List<ModifyHistoryEntry> firstHistory = List.of(modifyHistoryEntry);
    List<ModifyHistoryEntry> secondHistory = List.of(modifyHistoryEntry);

    undoHistoryRepository.add(player, firstHistory);
    undoHistoryRepository.add(player, secondHistory);

    undoHistoryRepository.removeLast(player);

    List<List<ModifyHistoryEntry>> result = undoHistoryRepository.findByPlayer(player);
    assertEquals(1, result.size());
    assertEquals(firstHistory, result.getFirst());
  }

  @Test
  void removeLastDoesNothingWhenPlayerHasNoHistory() {
    undoHistoryRepository.removeLast(player);

    assertTrue(undoHistoryRepository.findByPlayer(player).isEmpty());
  }

  @Test
  void removeLastDoesNothingWhenPlayerIsUnknown() {
    undoHistoryRepository.add(otherPlayer, List.of(modifyHistoryEntry));

    undoHistoryRepository.removeLast(player);

    assertEquals(1, undoHistoryRepository.findByPlayer(otherPlayer).size());
  }

  @Test
  void removeLastLeavesOtherPlayersHistoryIntact() {
    undoHistoryRepository.add(player, List.of(modifyHistoryEntry));
    undoHistoryRepository.add(otherPlayer, List.of(modifyHistoryEntry));

    undoHistoryRepository.removeLast(player);

    assertEquals(1, undoHistoryRepository.findByPlayer(otherPlayer).size());
  }

  @Test
  void hasHistoryReturnsTrueWhenPlayerHasHistory() {
    undoHistoryRepository.add(player, List.of(modifyHistoryEntry));

    assertTrue(undoHistoryRepository.hasHistory(player));
  }

  @Test
  void hasHistoryReturnsFalseWhenPlayerHasNoHistory() {
    assertFalse(undoHistoryRepository.hasHistory(player));
  }

  @Test
  void hasHistoryReturnsFalseWhenPlayerIsUnknown() {
    undoHistoryRepository.add(otherPlayer, List.of(modifyHistoryEntry));

    assertFalse(undoHistoryRepository.hasHistory(player));
  }

  @Test
  void hasHistoryReturnsFalseAfterLastEntryRemoved() {
    undoHistoryRepository.add(player, List.of(modifyHistoryEntry));

    undoHistoryRepository.removeLast(player);

    assertFalse(undoHistoryRepository.hasHistory(player));
  }

  @Test
  void hasHistoryReturnsTrueWhenMultipleEntriesExistAndOneIsRemoved() {
    undoHistoryRepository.add(player, List.of(modifyHistoryEntry));
    undoHistoryRepository.add(player, List.of(modifyHistoryEntry));

    undoHistoryRepository.removeLast(player);

    assertTrue(undoHistoryRepository.hasHistory(player));
  }

  @Test
  void removeLastDoesNothingWhenPlayerHistoryIsEmpty() {
    undoHistoryRepository.add(player, List.of(modifyHistoryEntry));
    undoHistoryRepository.removeLast(player);

    undoHistoryRepository.removeLast(player);

    assertFalse(undoHistoryRepository.hasHistory(player));
  }
}