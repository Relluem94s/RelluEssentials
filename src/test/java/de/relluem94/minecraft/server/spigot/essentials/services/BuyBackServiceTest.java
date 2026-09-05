package de.relluem94.minecraft.server.spigot.essentials.services;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.models.items.CustomItem;
import de.relluem94.minecraft.server.spigot.essentials.repositories.BuyBackRepository;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BuyBackServiceTest {

  @Mock
  private BuyBackRepository buyBackRepository;

  @Mock
  private ServiceContext serviceContext;

  @Mock
  private EnchantmentService enchantmentService;

  @Mock
  private ItemService itemService;

  @Mock
  private Player player;

  @Mock
  private CustomItem customItem;

  @Mock
  private ItemStack item;

  private BuyBackService buyBackService;

  @BeforeEach
  void setUp() {
    buyBackService = new BuyBackService(buyBackRepository, serviceContext);
  }

  private ItemStack createTrackingMockItemStack(Material material) {
    ItemStack mockedStack = mock(ItemStack.class);
    AtomicInteger amount = new AtomicInteger(0);
    when(mockedStack.getType()).thenReturn(material);
    when(mockedStack.getAmount()).thenAnswer(_ -> amount.get());
    when(mockedStack.getMaxStackSize()).thenReturn(64);
    lenient().when(mockedStack.clone()).thenAnswer(_ -> createTrackingMockItemStack(material));
    return mockedStack;
  }

  private void stubItemResolutionToReturnRawItem(ItemStack sourceItem) {
    when(serviceContext.getEnchantmentService()).thenReturn(enchantmentService);
    when(enchantmentService.findByBookItemStack(sourceItem)).thenReturn(Optional.empty());
    when(serviceContext.getItemService()).thenReturn(itemService);
    when(itemService.findByItemStack(sourceItem)).thenReturn(Optional.empty());
  }

  @Test
  void recordSoldItemsUsesRawItemWhenNoEnchantmentOrCustomItemFound() {
    ItemStack sourceItem = createTrackingMockItemStack(Material.DIRT);
    stubItemResolutionToReturnRawItem(sourceItem);

    buyBackService.recordSoldItems(player, sourceItem, 1);

    @SuppressWarnings("unchecked")
    ArgumentCaptor<List<ItemStack>> captor = ArgumentCaptor.forClass(List.class);
    verify(buyBackRepository).addItems(eq(player), captor.capture());
    List<ItemStack> capturedStacks = captor.getValue();
    assertAll(
        () -> assertFalse(capturedStacks.isEmpty()),
        () -> assertTrue(capturedStacks.stream().allMatch(s -> s.getType() == Material.DIRT))
    );
  }

  @Test
  void recordSoldItemsUsesCustomItemWhenCustomItemFound() {
    ItemStack customItemStack = createTrackingMockItemStack(Material.STONE);
    when(serviceContext.getEnchantmentService()).thenReturn(enchantmentService);
    when(enchantmentService.findByBookItemStack(item)).thenReturn(Optional.empty());
    when(serviceContext.getItemService()).thenReturn(itemService);
    when(itemService.findByItemStack(item)).thenReturn(Optional.of(customItem));
    when(customItem.toItemStack()).thenReturn(customItemStack);

    buyBackService.recordSoldItems(player, item, 1);

    @SuppressWarnings("unchecked")
    ArgumentCaptor<List<ItemStack>> captor = ArgumentCaptor.forClass(List.class);
    verify(buyBackRepository).addItems(eq(player), captor.capture());
    List<ItemStack> capturedStacks = captor.getValue();
    assertAll(
        () -> assertFalse(capturedStacks.isEmpty()),
        () -> assertTrue(capturedStacks.stream().allMatch(s -> s.getType() == Material.STONE))
    );
  }

  @Test
  void recordSoldItemsSplitsIntoMultipleStacksWhenAmountExceedsMaxStackSize() {
    ItemStack sourceItem = createTrackingMockItemStack(Material.DIRT);
    stubItemResolutionToReturnRawItem(sourceItem);

    buyBackService.recordSoldItems(player, sourceItem, 128);

    @SuppressWarnings("unchecked")
    ArgumentCaptor<List<ItemStack>> captor = ArgumentCaptor.forClass(List.class);
    verify(buyBackRepository).addItems(eq(player), captor.capture());
    List<ItemStack> capturedStacks = captor.getValue();
    assertAll(
        () -> assertTrue(capturedStacks.size() > 1),
        () -> assertEquals(128, capturedStacks.stream().mapToInt(ItemStack::getAmount).sum())
    );
  }

  @Test
  void recordSoldItemsProducesExactAmountAcrossAllStacks() {
    ItemStack sourceItem = createTrackingMockItemStack(Material.DIRT);
    stubItemResolutionToReturnRawItem(sourceItem);

    buyBackService.recordSoldItems(player, sourceItem, 50);

    @SuppressWarnings("unchecked")
    ArgumentCaptor<List<ItemStack>> captor = ArgumentCaptor.forClass(List.class);
    verify(buyBackRepository).addItems(eq(player), captor.capture());
    int totalAmount = captor.getValue().stream().mapToInt(ItemStack::getAmount).sum();
    assertEquals(50, totalAmount);
  }

  @Test
  void getBuyBackItemsReturnsBuyBackRepositoryResult() {
    ItemStack repositoryItem = mock(ItemStack.class);
    when(buyBackRepository.findByPlayer(player)).thenReturn(List.of(repositoryItem));

    List<ItemStack> result = buyBackService.getBuyBackItems(player);

    assertAll(
        () -> assertFalse(result.isEmpty()),
        () -> assertTrue(result.contains(repositoryItem))
    );
  }

  @Test
  void getBuyBackItemsReturnsEmptyListWhenNoItemsExist() {
    when(buyBackRepository.findByPlayer(player)).thenReturn(Collections.emptyList());

    List<ItemStack> result = buyBackService.getBuyBackItems(player);

    assertTrue(result.isEmpty());
  }

  @Test
  void hasBuyBackItemsReturnsTrueWhenPlayerHasItems() {
    ItemStack repositoryItem = mock(ItemStack.class);
    when(buyBackRepository.findByPlayer(player)).thenReturn(List.of(repositoryItem));

    boolean result = buyBackService.hasBuyBackItems(player);

    assertTrue(result);
  }

  @Test
  void hasBuyBackItemsReturnsFalseWhenPlayerHasNoItems() {
    when(buyBackRepository.findByPlayer(player)).thenReturn(Collections.emptyList());

    boolean result = buyBackService.hasBuyBackItems(player);

    assertFalse(result);
  }

  @Test
  void clearBuyBackHistoryDelegatesDeleteToRepository() {
    buyBackService.clearBuyBackHistory(player);

    verify(buyBackRepository).deleteByPlayer(player);
  }

  @Test
  void removeBuyBackItemDelegatesRemoveLastEntryToRepository() {
    buyBackService.removeBuyBackItem(player);

    verify(buyBackRepository).removeLastEntry(player);
  }

  @Test
  void recordSoldItemsPropagatesRepositoryException() {
    ItemStack sourceItem = createTrackingMockItemStack(Material.DIRT);
    stubItemResolutionToReturnRawItem(sourceItem);
    doThrow(new RuntimeException("repository failure"))
        .when(buyBackRepository).addItems(any(Player.class), anyList());

    assertThrows(RuntimeException.class, () -> buyBackService.recordSoldItems(player, sourceItem, 1));
  }

  @Test
  void getBuyBackItemsPropagatesRepositoryException() {
    when(buyBackRepository.findByPlayer(player)).thenThrow(new RuntimeException("repository failure"));

    assertThrows(RuntimeException.class, () -> buyBackService.getBuyBackItems(player));
  }

  @Test
  void clearBuyBackHistoryPropagatesRepositoryException() {
    doThrow(new RuntimeException("repository failure")).when(buyBackRepository).deleteByPlayer(player);

    assertThrows(RuntimeException.class, () -> buyBackService.clearBuyBackHistory(player));
  }

  @Test
  void removeBuyBackItemPropagatesRepositoryException() {
    doThrow(new RuntimeException("repository failure")).when(buyBackRepository).removeLastEntry(player);

    assertThrows(RuntimeException.class, () -> buyBackService.removeBuyBackItem(player));
  }
}