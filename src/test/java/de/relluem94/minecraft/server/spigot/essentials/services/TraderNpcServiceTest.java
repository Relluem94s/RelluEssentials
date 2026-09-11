package de.relluem94.minecraft.server.spigot.essentials.services;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.relluem94.minecraft.server.spigot.essentials.models.pojo.TraderNpcEntry;
import de.relluem94.minecraft.server.spigot.essentials.npcs.trader.BankerNpc;
import de.relluem94.minecraft.server.spigot.essentials.npcs.trader.TraderNpc;
import de.relluem94.minecraft.server.spigot.essentials.registries.TraderNpcRegistry;
import de.relluem94.minecraft.server.spigot.essentials.repositories.TraderNpcRepository;
import java.util.List;
import org.bukkit.inventory.ItemStack;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TraderNpcServiceTest {

  @Mock
  private TraderNpcRegistry traderNpcRegistry;

  @Mock
  private TraderNpcRepository traderNpcRepository;

  @Mock
  private BankerNpc bankerNpc;

  @Mock
  private TraderNpc traderNpc;

  @Mock
  private TraderNpcEntry traderNpcEntry;

  @Mock
  private ItemStack itemStack;

  private TraderNpcService traderNpcService;

  @BeforeEach
  void setUp() {
    traderNpcService = new TraderNpcService(traderNpcRegistry, traderNpcRepository, bankerNpc);
  }

  @Test
  void getBankerNpcReturnsBankerNpc() {
    assertSame(bankerNpc, traderNpcService.getBankerNpc());
  }

  @Test
  void loadAndInitialiseNpcsLoadsFromRepositoryAndInitialisesRegistry() {
    List<TraderNpcEntry> loadedNpcs = List.of(traderNpcEntry);
    when(traderNpcRepository.loadAll()).thenReturn(loadedNpcs);

    traderNpcService.loadAndInitialiseNpcs();

    assertAll(
        () -> verify(traderNpcRepository).loadAll(),
        () -> verify(traderNpcRegistry).init(loadedNpcs)
    );
  }

  @Test
  void getAllNpcsReturnsAllNpcsFromRegistry() {
    List<TraderNpc> expectedNpcs = List.of(traderNpc);
    when(traderNpcRegistry.getNpcs()).thenReturn(expectedNpcs);

    List<TraderNpc> result = traderNpcService.getAllNpcs();

    assertAll(
        () -> assertEquals(expectedNpcs, result),
        () -> verify(traderNpcRegistry).getNpcs()
    );
  }

  @Test
  void getNpcReturnsNpcAtGivenIndex() {
    when(traderNpcRegistry.getNpc(0)).thenReturn(traderNpc);

    TraderNpc result = traderNpcService.getNpc(0);

    assertAll(
        () -> assertSame(traderNpc, result),
        () -> verify(traderNpcRegistry).getNpc(0)
    );
  }

  @Test
  void getNpcSpawnEggsReturnsSpawnEggItemStacks() {
    List<ItemStack> expectedItemStacks = List.of(itemStack);
    when(traderNpcRegistry.getNpcItemStackList()).thenReturn(expectedItemStacks);

    List<ItemStack> result = traderNpcService.getNpcSpawnEggs();

    assertAll(
        () -> assertEquals(expectedItemStacks, result),
        () -> verify(traderNpcRegistry).getNpcItemStackList()
    );
  }

  @Test
  void getNpcNamesReturnsNpcNameList() {
    List<String> expectedNames = List.of("Trader", "Banker");
    when(traderNpcRegistry.getNpcNameList()).thenReturn(expectedNames);

    List<String> result = traderNpcService.getNpcNames();

    assertAll(
        () -> assertEquals(expectedNames, result),
        () -> verify(traderNpcRegistry).getNpcNameList()
    );
  }

  @Test
  void getTraderNpcTitlesReturnsTraderNpcTitleList() {
    List<String> expectedTitles = List.of("Trader Shop", "Enchanter Shop");
    when(traderNpcRegistry.getNpcTraderTitleList()).thenReturn(expectedTitles);

    List<String> result = traderNpcService.getTraderNpcTitles();

    assertAll(
        () -> assertEquals(expectedTitles, result),
        () -> verify(traderNpcRegistry).getNpcTraderTitleList()
    );
  }
}