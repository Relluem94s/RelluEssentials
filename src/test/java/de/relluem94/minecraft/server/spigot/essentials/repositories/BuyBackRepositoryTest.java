package de.relluem94.minecraft.server.spigot.essentials.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import java.util.List;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BuyBackRepositoryTest {

  private BuyBackRepository buyBackRepository;
  private Player player;
  private ItemStack firstItem;
  private ItemStack secondItem;

  @BeforeEach
  void setUp() {
    buyBackRepository = new BuyBackRepository();
    player = mock(Player.class);
    firstItem = mock(ItemStack.class);
    secondItem = mock(ItemStack.class);
  }

  @Test
  void addItemsStoresItemsForPlayer() {
    buyBackRepository.addItems(player, List.of(firstItem, secondItem));

    List<ItemStack> result = buyBackRepository.findByPlayer(player);

    assertEquals(2, result.size());
    assertTrue(result.contains(firstItem));
    assertTrue(result.contains(secondItem));
  }

  @Test
  void addItemsAppendsToExistingHistory() {
    buyBackRepository.addItems(player, List.of(firstItem));
    buyBackRepository.addItems(player, List.of(secondItem));

    List<ItemStack> result = buyBackRepository.findByPlayer(player);

    assertEquals(2, result.size());
    assertTrue(result.contains(firstItem));
    assertTrue(result.contains(secondItem));
  }

  @Test
  void addItemsDoesNotOverwriteExistingHistory() {
    buyBackRepository.addItems(player, List.of(firstItem));
    buyBackRepository.addItems(player, List.of(secondItem));

    List<ItemStack> result = buyBackRepository.findByPlayer(player);

    assertEquals(2, result.size());
  }

  @Test
  void addItemsWithMultiplePlayersMaintainsSeparateHistories() {
    Player secondPlayer = mock(Player.class);

    buyBackRepository.addItems(player, List.of(firstItem));
    buyBackRepository.addItems(secondPlayer, List.of(secondItem));

    List<ItemStack> firstPlayerHistory = buyBackRepository.findByPlayer(player);
    List<ItemStack> secondPlayerHistory = buyBackRepository.findByPlayer(secondPlayer);

    assertEquals(1, firstPlayerHistory.size());
    assertTrue(firstPlayerHistory.contains(firstItem));
    assertEquals(1, secondPlayerHistory.size());
    assertTrue(secondPlayerHistory.contains(secondItem));
  }

  @Test
  void findByPlayerReturnsEmptyListForUnknownPlayer() {
    List<ItemStack> result = buyBackRepository.findByPlayer(player);

    assertTrue(result.isEmpty());
  }

  @Test
  void findByPlayerReturnsEmptyListForPlayerWithNoHistory() {
    Player unknownPlayer = mock(Player.class);

    List<ItemStack> result = buyBackRepository.findByPlayer(unknownPlayer);

    assertTrue(result.isEmpty());
  }

  @Test
  void findByPlayerReturnsNewEmptyListInstanceEachTimeForUnknownPlayer() {
    List<ItemStack> firstCall = buyBackRepository.findByPlayer(player);
    List<ItemStack> secondCall = buyBackRepository.findByPlayer(player);

    assertNotSame(firstCall, secondCall);
  }

  @Test
  void removeLastEntryRemovesMostRecentItem() {
    buyBackRepository.addItems(player, List.of(firstItem, secondItem));

    buyBackRepository.removeLastEntry(player);

    List<ItemStack> result = buyBackRepository.findByPlayer(player);
    assertEquals(1, result.size());
    assertTrue(result.contains(firstItem));
  }

  @Test
  void removeLastEntryDoesNothingForUnknownPlayer() {
    Player unknownPlayer = mock(Player.class);

    buyBackRepository.removeLastEntry(unknownPlayer);

    assertTrue(buyBackRepository.findByPlayer(unknownPlayer).isEmpty());
  }

  @Test
  void removeLastEntryDoesNothingWhenHistoryIsEmpty() {
    buyBackRepository.addItems(player, List.of(firstItem));
    buyBackRepository.removeLastEntry(player);

    buyBackRepository.removeLastEntry(player);

    assertTrue(buyBackRepository.findByPlayer(player).isEmpty());
  }

  @Test
  void removeLastEntryRemovesOnlyLastItemWhenMultipleItemsPresent() {
    buyBackRepository.addItems(player, List.of(firstItem, secondItem));

    buyBackRepository.removeLastEntry(player);

    List<ItemStack> result = buyBackRepository.findByPlayer(player);
    assertEquals(1, result.size());
    assertTrue(result.contains(firstItem));
  }

  @Test
  void deleteByPlayerRemovesEntireHistory() {
    buyBackRepository.addItems(player, List.of(firstItem, secondItem));

    buyBackRepository.deleteByPlayer(player);

    assertTrue(buyBackRepository.findByPlayer(player).isEmpty());
  }

  @Test
  void deleteByPlayerDoesNothingForUnknownPlayer() {
    Player unknownPlayer = mock(Player.class);

    buyBackRepository.deleteByPlayer(unknownPlayer);

    assertTrue(buyBackRepository.findByPlayer(unknownPlayer).isEmpty());
  }

  @Test
  void deleteByPlayerDoesNotAffectOtherPlayers() {
    Player secondPlayer = mock(Player.class);
    buyBackRepository.addItems(player, List.of(firstItem));
    buyBackRepository.addItems(secondPlayer, List.of(secondItem));

    buyBackRepository.deleteByPlayer(player);

    List<ItemStack> secondPlayerHistory = buyBackRepository.findByPlayer(secondPlayer);
    assertEquals(1, secondPlayerHistory.size());
    assertTrue(secondPlayerHistory.contains(secondItem));
  }
}