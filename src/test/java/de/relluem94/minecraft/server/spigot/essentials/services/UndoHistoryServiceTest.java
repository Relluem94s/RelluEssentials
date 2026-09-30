package de.relluem94.minecraft.server.spigot.essentials.services;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.relluem94.minecraft.server.spigot.essentials.models.pojo.ModifyHistoryEntry;
import de.relluem94.minecraft.server.spigot.essentials.repositories.UndoHistoryRepository;
import java.util.LinkedList;
import java.util.List;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UndoHistoryServiceTest {

  @Mock
  private UndoHistoryRepository undoHistoryRepository;

  @Mock
  private Player player;

  @InjectMocks
  private UndoHistoryService undoHistoryService;

  @Test
  void addHistoryDelegatesToRepository() {
    List<ModifyHistoryEntry> history = List.of(mock(ModifyHistoryEntry.class));

    undoHistoryService.addHistory(player, history);

    verify(undoHistoryRepository).add(player, history);
  }

  @Test
  void addHistoryWithEmptyListDelegatesToRepository() {
    List<ModifyHistoryEntry> emptyHistory = List.of();

    undoHistoryService.addHistory(player, emptyHistory);

    verify(undoHistoryRepository).add(player, emptyHistory);
  }

  @Test
  void popLastHistoryReturnsNullWhenNoHistoryExists() {
    when(undoHistoryRepository.hasHistory(player)).thenReturn(false);

    List<ModifyHistoryEntry> result = undoHistoryService.popLastHistory(player);

    assertAll(
        () -> assertNull(result),
        () -> verify(undoHistoryRepository, never()).findByPlayer(any()),
        () -> verify(undoHistoryRepository, never()).removeLast(any())
    );
  }

  @Test
  void popLastHistoryReturnsLastEntryAndRemovesItWhenHistoryExists() {
    List<ModifyHistoryEntry> firstHistory = List.of(mock(ModifyHistoryEntry.class));
    List<ModifyHistoryEntry> lastHistory = List.of(mock(ModifyHistoryEntry.class), mock(ModifyHistoryEntry.class));

    LinkedList<List<ModifyHistoryEntry>> historyList = new LinkedList<>();
    historyList.add(firstHistory);
    historyList.add(lastHistory);

    when(undoHistoryRepository.hasHistory(player)).thenReturn(true);
    when(undoHistoryRepository.findByPlayer(player)).thenReturn(historyList);

    List<ModifyHistoryEntry> result = undoHistoryService.popLastHistory(player);

    assertAll(
        () -> assertNotNull(result),
        () -> assertEquals(lastHistory, result),
        () -> verify(undoHistoryRepository).removeLast(player)
    );
  }

  @Test
  void popLastHistoryReturnsCorrectSingleEntryWhenOnlyOneHistoryExists() {
    ModifyHistoryEntry entry = mock(ModifyHistoryEntry.class);
    List<ModifyHistoryEntry> singleHistory = List.of(entry);

    LinkedList<List<ModifyHistoryEntry>> historyList = new LinkedList<>();
    historyList.add(singleHistory);

    when(undoHistoryRepository.hasHistory(player)).thenReturn(true);
    when(undoHistoryRepository.findByPlayer(player)).thenReturn(historyList);

    List<ModifyHistoryEntry> result = undoHistoryService.popLastHistory(player);

    assertNotNull(result);
    assertEquals(singleHistory, result);
    assertEquals(1, result.size());
    verify(undoHistoryRepository).removeLast(player);
  }

  @Test
  void popLastHistoryPropagatesExceptionFromFindByPlayer() {
    when(undoHistoryRepository.hasHistory(player)).thenReturn(true);
    when(undoHistoryRepository.findByPlayer(player)).thenThrow(new RuntimeException("repository failure"));

    assertThrows(RuntimeException.class, () -> undoHistoryService.popLastHistory(player));
  }

  @Test
  void popLastHistoryPropagatesExceptionFromRemoveLast() {
    List<ModifyHistoryEntry> history = List.of(mock(ModifyHistoryEntry.class));
    LinkedList<List<ModifyHistoryEntry>> historyList = new LinkedList<>();
    historyList.add(history);

    when(undoHistoryRepository.hasHistory(player)).thenReturn(true);
    when(undoHistoryRepository.findByPlayer(player)).thenReturn(historyList);
    doThrow(new RuntimeException("remove failure")).when(undoHistoryRepository).removeLast(player);

    assertThrows(RuntimeException.class, () -> undoHistoryService.popLastHistory(player));
  }

  @Test
  void addHistoryPropagatesExceptionFromRepository() {
    List<ModifyHistoryEntry> history = List.of(mock(ModifyHistoryEntry.class));
    doThrow(new RuntimeException("add failure")).when(undoHistoryRepository).add(player, history);

    assertThrows(RuntimeException.class, () -> undoHistoryService.addHistory(player, history));
  }
}