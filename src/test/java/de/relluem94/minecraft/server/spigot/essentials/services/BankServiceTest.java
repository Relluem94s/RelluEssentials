package de.relluem94.minecraft.server.spigot.essentials.services;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyDouble;
import static org.mockito.Mockito.anyFloat;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.argThat;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.helpers.ItemHelper;
import de.relluem94.minecraft.server.spigot.essentials.models.RelluEssentialsNamespacedKey;
import de.relluem94.minecraft.server.spigot.essentials.models.items.CustomItem;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.BankAccountEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.BankTierEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.BankTransactionEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.PlayerEntry;
import de.relluem94.minecraft.server.spigot.essentials.registries.BankTierRegistry;
import de.relluem94.minecraft.server.spigot.essentials.repositories.BankRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BankServiceTest {

  @Mock
  private ServiceContext serviceContext;
  @Mock
  private BankTierRegistry bankTierRegistry;
  @Mock
  private BankRepository bankRepository;
  @Mock
  private JavaPlugin plugin;
  @Mock
  private TranslationService translationService;
  @Mock
  private ItemService itemService;
  @Mock
  private PluginMetadataService pluginMetadataService;
  @Mock
  private PlayerService playerService;
  @Mock
  private Player player;

  private BankService bankService;

  @BeforeEach
  void setUp() {
    bankService = new BankService(serviceContext, bankTierRegistry, bankRepository, plugin);
  }

  private BankTierEntry createBankTierEntry(int id, long cost, double interest, long limit) {
    BankTierEntry tier = new BankTierEntry();
    tier.setId(id);
    tier.setCost(cost);
    tier.setInterest(interest);
    tier.setLimit(limit);
    tier.setName("Tier " + id);
    return tier;
  }

  private BankAccountEntry createBankAccountEntry(int playerId, double value, BankTierEntry tier) {
    BankAccountEntry bae = new BankAccountEntry();
    bae.setId(1);
    bae.setPlayerId(playerId);
    bae.setValue(value);
    bae.setTier(tier);
    return bae;
  }

  private PlayerEntry createPlayerEntry(double purse) {
    PlayerEntry pe = new PlayerEntry();
    pe.setId(1);
    pe.setPurse(purse);
    return pe;
  }

  @Test
  void getBankItemReturnsCustomItemWhenFound() {
    CustomItem expectedItem = mock(CustomItem.class);
    when(serviceContext.getPluginMetadataService()).thenReturn(pluginMetadataService);
    when(pluginMetadataService.getName()).thenReturn("essentials");
    when(serviceContext.getItemService()).thenReturn(itemService);
    when(itemService.find(any(RelluEssentialsNamespacedKey.class))).thenReturn(Optional.of(expectedItem));

    CustomItem result = bankService.getBankItem("some_item");

    assertEquals(expectedItem, result);
  }

  @Test
  void getBankItemReturnsNullWhenNotFound() {
    when(serviceContext.getPluginMetadataService()).thenReturn(pluginMetadataService);
    when(pluginMetadataService.getName()).thenReturn("essentials");
    when(serviceContext.getItemService()).thenReturn(itemService);
    when(itemService.find(any(RelluEssentialsNamespacedKey.class))).thenReturn(Optional.empty());

    CustomItem result = bankService.getBankItem("missing_item");

    assertNull(result);
  }

  @Test
  void addLoreLineAddsSecondLineWhenLoreHasOneEntry() {
    ItemStack itemStack = mock(ItemStack.class);
    ItemMeta itemMeta = mock(ItemMeta.class);
    List<String> existingLore = new ArrayList<>();
    existingLore.add("First line");

    when(itemStack.getItemMeta()).thenReturn(itemMeta);
    when(itemMeta.getLore()).thenReturn(existingLore);

    bankService.addLoreLine(itemStack, "Second line");

    verify(itemMeta).setLore(argThat(lore -> lore.size() == 2 && lore.get(1).equals("Second line")));
    verify(itemStack).setItemMeta(itemMeta);
  }

  @Test
  void addLoreLineReplacesSecondLineWhenLoreHasMoreThanOneEntry() {
    ItemStack itemStack = mock(ItemStack.class);
    ItemMeta itemMeta = mock(ItemMeta.class);
    List<String> existingLore = new ArrayList<>();
    existingLore.add("First line");
    existingLore.add("Old second line");

    when(itemStack.getItemMeta()).thenReturn(itemMeta);
    when(itemMeta.getLore()).thenReturn(existingLore);

    bankService.addLoreLine(itemStack, "New second line");

    verify(itemMeta).setLore(argThat(lore -> lore.get(1).equals("New second line")));
    verify(itemStack).setItemMeta(itemMeta);
  }

  @Test
  void addLoreLineReturnsUnchangedItemStackWhenItemMetaIsNull() {
    ItemStack itemStack = mock(ItemStack.class);
    when(itemStack.getItemMeta()).thenReturn(null);

    ItemStack result = bankService.addLoreLine(itemStack, "Some line");

    assertEquals(itemStack, result);
    verify(itemStack, never()).setItemMeta(any());
  }

  @Test
  void addLoreLineInitializesLoreWhenLoreIsNull() {
    ItemStack itemStack = mock(ItemStack.class);
    ItemMeta itemMeta = mock(ItemMeta.class);

    when(itemStack.getItemMeta()).thenReturn(itemMeta);
    when(itemMeta.getLore()).thenReturn(null);

    bankService.addLoreLine(itemStack, "First added line");

    verify(itemMeta).setLore(argThat(lore -> lore.size() == 1 && lore.getFirst().equals("First added line")));
  }

  @Test
  void getBankTiersReturnsTierItemsForEachRegisteredTier() {
    BankTierEntry tier1 = createBankTierEntry(1, 100L, 2.5, 10000);
    BankTierEntry tier2 = createBankTierEntry(2, 500L, 5.0, 50000);
    List<BankTierEntry> tiers = List.of(tier1, tier2);

    when(bankTierRegistry.getBankTiers()).thenReturn(tiers);
    when(serviceContext.getTranslationService()).thenReturn(translationService);
    when(translationService.get(eq(MessageKey.PLUGIN_EVENT_NPC_BANKER_COST_LORE), any())).thenReturn("Cost lore");
    when(translationService.get(eq(MessageKey.PLUGIN_EVENT_NPC_BANKER_INTEREST_LORE), any())).thenReturn("Interest lore");
    when(translationService.get(eq(MessageKey.PLUGIN_EVENT_NPC_BANKER_LIMIT_LORE), any())).thenReturn("Limit lore");
    when(plugin.getName()).thenReturn("essentials");

    List<ItemHelper> result = bankService.getBankTiers();

    assertAll(
        () -> assertEquals(2, result.size()),
        () -> assertNotNull(result.getFirst()),
        () -> assertNotNull(result.get(1))
    );
  }

  @Test
  void depositDepositsFullPurseWhenPercentageIs100AndWithinLimit() {
    BankTierEntry tier = createBankTierEntry(1, 0L, 2.0, 100000);
    BankAccountEntry bae = createBankAccountEntry(1, 500.0, tier);
    PlayerEntry pe = createPlayerEntry(1000.0);

    when(serviceContext.getTranslationService()).thenReturn(translationService);
    when(translationService.getWithPrefix(eq(MessageKey.PLUGIN_EVENT_NPC_BANKER_DEPOSIT_MESSAGE), any(), any())).thenReturn("deposited");
    when(translationService.getWithPrefix(eq(MessageKey.PLUGIN_EVENT_NPC_BANKER_TOTAL), any(), any())).thenReturn("total");

    bankService.deposit(pe, player, bae, 100f);

    assertAll(
        () -> assertEquals(0.0, pe.getPurse()),
        () -> assertTrue(pe.isHasToBeUpdated()),
        () -> verify(bankRepository).addTransactionToBank(eq(1), eq(1), eq(1000.0), eq(500.0), eq(1))
    );
  }

  @Test
  void depositDepositsPartialPurseWhenPercentageIsNot100AndWithinLimit() {
    BankTierEntry tier = createBankTierEntry(1, 0L, 2.0, 100000);
    BankAccountEntry bae = createBankAccountEntry(1, 500.0, tier);
    PlayerEntry pe = createPlayerEntry(1000.0);

    when(serviceContext.getTranslationService()).thenReturn(translationService);
    when(translationService.getWithPrefix(eq(MessageKey.PLUGIN_EVENT_NPC_BANKER_DEPOSIT_MESSAGE), any(), any())).thenReturn("deposited");
    when(translationService.getWithPrefix(eq(MessageKey.PLUGIN_EVENT_NPC_BANKER_TOTAL), any(), any())).thenReturn("total");

    bankService.deposit(pe, player, bae, 50f);

    assertAll(
        () -> assertEquals(500.0, pe.getPurse()),
        () -> assertTrue(pe.isHasToBeUpdated()),
        () -> verify(bankRepository).addTransactionToBank(eq(1), eq(1), eq(500.0), eq(500.0), eq(1))
    );
  }

  @Test
  void depositDepositsUpToLimitAndSendsLimitMessageWhenLimitExceeded() {
    BankTierEntry tier = createBankTierEntry(1, 0L, 2.0, 600);
    BankAccountEntry bae = createBankAccountEntry(1, 500.0, tier);
    PlayerEntry pe = createPlayerEntry(1000.0);

    when(serviceContext.getTranslationService()).thenReturn(translationService);
    when(translationService.getWithPrefix(eq(MessageKey.PLUGIN_EVENT_NPC_BANKER_DEPOSIT_MESSAGE), any(), any())).thenReturn("deposited");
    when(translationService.getWithPrefix(eq(MessageKey.PLUGIN_EVENT_NPC_BANKER_TOTAL), any(), any())).thenReturn("total");
    when(translationService.getWithPrefix(MessageKey.PLUGIN_EVENT_NPC_BANKER_DEPOSIT_LIMIT_MESSAGE)).thenReturn("limit reached");

    bankService.deposit(pe, player, bae, 100f);

    assertAll(
        () -> assertEquals(900.0, pe.getPurse()),
        () -> verify(bankRepository).addTransactionToBank(eq(1), eq(1), eq(100.0), eq(500.0), eq(1)),
        () -> verify(player).sendMessage("limit reached")
    );
  }

  @Test
  void depositSendsLimitMessageWithoutTransactionWhenAlreadyAtLimit() {
    BankTierEntry tier = createBankTierEntry(1, 0L, 2.0, 500);
    BankAccountEntry bae = createBankAccountEntry(1, 500.0, tier);
    PlayerEntry pe = createPlayerEntry(1000.0);

    when(serviceContext.getTranslationService()).thenReturn(translationService);
    when(translationService.getWithPrefix(MessageKey.PLUGIN_EVENT_NPC_BANKER_DEPOSIT_LIMIT_MESSAGE)).thenReturn("limit reached");

    bankService.deposit(pe, player, bae, 100f);

    assertAll(
        () -> assertEquals(1000.0, pe.getPurse()),
        () -> verify(bankRepository, never()).addTransactionToBank(anyInt(), anyInt(), anyDouble(), anyDouble(), anyInt()),
        () -> verify(player).sendMessage("limit reached")
    );
  }

  @Test
  void depositSendsNoCoinsMessageWhenPurseIsLessThanOne() {
    BankTierEntry tier = createBankTierEntry(1, 0L, 2.0, 100000);
    BankAccountEntry bae = createBankAccountEntry(1, 500.0, tier);
    PlayerEntry pe = createPlayerEntry(0.5);

    when(serviceContext.getTranslationService()).thenReturn(translationService);
    when(translationService.getWithPrefix(eq(MessageKey.PLUGIN_EVENT_NPC_BANKER_DEPOSIT_NO_COINS_MESSAGE), any())).thenReturn("no coins");

    bankService.deposit(pe, player, bae, 100f);

    assertAll(
        () -> verify(bankRepository, never()).addTransactionToBank(anyInt(), anyInt(), anyDouble(), anyDouble(), anyInt()),
        () -> verify(player).sendMessage("no coins")
    );
  }

  @Test
  void withdrawWithdrawsFullBankBalanceWhenPercentageIs100() {
    BankTierEntry tier = createBankTierEntry(1, 0L, 2.0, 100000);
    BankAccountEntry bae = createBankAccountEntry(1, 1000.0, tier);
    PlayerEntry pe = createPlayerEntry(200.0);

    when(serviceContext.getTranslationService()).thenReturn(translationService);
    when(translationService.getWithPrefix(eq(MessageKey.PLUGIN_EVENT_NPC_BANKER_WITHDRAW_MESSAGE), any(), any())).thenReturn("withdrawn");
    when(translationService.getWithPrefix(eq(MessageKey.PLUGIN_EVENT_NPC_BANKER_TOTAL), any(), any())).thenReturn("total");

    bankService.withdraw(pe, player, bae, 100f);

    assertAll(
        () -> assertEquals(1200.0, pe.getPurse()),
        () -> assertTrue(pe.isHasToBeUpdated()),
        () -> verify(bankRepository).addTransactionToBank(eq(1), eq(1), eq(-1000.0), eq(1000.0), eq(1))
    );
  }

  @Test
  void withdrawWithdrawsPartialBankBalanceWhenPercentageIsNot100() {
    BankTierEntry tier = createBankTierEntry(1, 0L, 2.0, 100000);
    BankAccountEntry bae = createBankAccountEntry(1, 1000.0, tier);
    PlayerEntry pe = createPlayerEntry(200.0);

    when(serviceContext.getTranslationService()).thenReturn(translationService);
    when(translationService.getWithPrefix(eq(MessageKey.PLUGIN_EVENT_NPC_BANKER_WITHDRAW_MESSAGE), any(), any())).thenReturn("withdrawn");
    when(translationService.getWithPrefix(eq(MessageKey.PLUGIN_EVENT_NPC_BANKER_TOTAL), any(), any())).thenReturn("total");

    bankService.withdraw(pe, player, bae, 50f);

    assertAll(
        () -> assertEquals(700.0, pe.getPurse()),
        () -> verify(bankRepository).addTransactionToBank(eq(1), eq(1), eq(-500.0), eq(1000.0), eq(1))
    );
  }

  @Test
  void withdrawSendsNotEnoughCoinsMessageWhenBankBalanceLessThanOne() {
    BankTierEntry tier = createBankTierEntry(1, 0L, 2.0, 100000);
    BankAccountEntry bae = createBankAccountEntry(1, 0.5, tier);
    PlayerEntry pe = createPlayerEntry(200.0);

    when(serviceContext.getTranslationService()).thenReturn(translationService);
    when(translationService.getWithPrefix(eq(MessageKey.PLUGIN_EVENT_NPC_BANKER_NOT_ENOUGH_COINS), any())).thenReturn("not enough");

    bankService.withdraw(pe, player, bae, 100f);

    assertAll(
        () -> assertEquals(200.0, pe.getPurse()),
        () -> verify(bankRepository, never()).addTransactionToBank(anyInt(), anyInt(), anyDouble(), anyDouble(), anyInt()),
        () -> verify(player).sendMessage("not enough")
    );
  }

  @Test
  void upgradeAccountUpgradesUsingPurseWhenPurseIsSufficient() {
    BankTierEntry currentTier = createBankTierEntry(1, 100L, 2.0, 10000);
    BankTierEntry targetTier = createBankTierEntry(2, 500L, 5.0, 50000);
    BankAccountEntry bae = createBankAccountEntry(1, 1000.0, currentTier);
    PlayerEntry pe = createPlayerEntry(1000.0);

    when(bankTierRegistry.getBankTiers()).thenReturn(List.of(targetTier));
    when(serviceContext.getTranslationService()).thenReturn(translationService);
    when(translationService.get(any(MessageKey.class), any())).thenReturn("lore");
    when(plugin.getName()).thenReturn("essentials");
    when(translationService.getWithPrefix(MessageKey.PLUGIN_EVENT_NPC_BANKER_BUY_USING_PURSE)).thenReturn("bought with purse");

    List<ItemHelper> tiers = bankService.getBankTiers();
    ItemStack upgradeItem = tiers.getFirst().getCustomItem();

    bankService.upgradeAccount(upgradeItem, player, pe, bae);

    assertAll(
        () -> assertEquals(500.0, pe.getPurse()),
        () -> assertTrue(pe.isHasToBeUpdated()),
        () -> verify(bankRepository).updateBankAccount(eq(1), eq(0f), eq(1000.0), eq(2)),
        () -> verify(player).sendMessage("bought with purse")
    );
  }

  @Test
  void upgradeAccountUpgradesUsingBankWhenBankIsSufficientAndPurseIsNot() {
    BankTierEntry currentTier = createBankTierEntry(1, 100L, 2.0, 10000);
    BankTierEntry targetTier = createBankTierEntry(2, 500L, 5.0, 50000);
    BankAccountEntry bae = createBankAccountEntry(1, 1000.0, currentTier);
    PlayerEntry pe = createPlayerEntry(200.0);

    when(bankTierRegistry.getBankTiers()).thenReturn(List.of(targetTier));
    when(serviceContext.getTranslationService()).thenReturn(translationService);
    when(translationService.get(any(MessageKey.class), any())).thenReturn("lore");
    when(plugin.getName()).thenReturn("essentials");
    when(translationService.getWithPrefix(MessageKey.PLUGIN_EVENT_NPC_BANKER_BUY_USING_BANK)).thenReturn("bought with bank");

    List<ItemHelper> tiers = bankService.getBankTiers();
    ItemStack upgradeItem = tiers.getFirst().getCustomItem();

    bankService.upgradeAccount(upgradeItem, player, pe, bae);

    assertAll(
        () -> verify(bankRepository).addTransactionToBank(eq(1), eq(1), eq(-500L), eq(1000.0), eq(2)),
        () -> verify(player).sendMessage("bought with bank")
    );
  }

  @Test
  void upgradeAccountUpgradesUsingBothWhenNeitherAloneIsSufficient() {
    BankTierEntry currentTier = createBankTierEntry(1, 100L, 2.0, 10000);
    BankTierEntry targetTier = createBankTierEntry(2, 500L, 5.0, 50000);
    BankAccountEntry bae = createBankAccountEntry(1, 300.0, currentTier);
    PlayerEntry pe = createPlayerEntry(300.0);

    when(bankTierRegistry.getBankTiers()).thenReturn(List.of(targetTier));
    when(serviceContext.getTranslationService()).thenReturn(translationService);
    when(translationService.get(any(MessageKey.class), any())).thenReturn("lore");
    when(plugin.getName()).thenReturn("essentials");
    when(translationService.getWithPrefix(MessageKey.PLUGIN_EVENT_NPC_BANKER_BUY_USING_BOTH)).thenReturn("bought with both");

    List<ItemHelper> tiers = bankService.getBankTiers();
    ItemStack upgradeItem = tiers.getFirst().getCustomItem();

    bankService.upgradeAccount(upgradeItem, player, pe, bae);

    assertAll(
        () -> assertEquals(0.0, pe.getPurse()),
        () -> assertTrue(pe.isHasToBeUpdated()),
        () -> verify(bankRepository).addTransactionToBank(eq(1), eq(1), eq(-200.0), eq(300.0), eq(2)),
        () -> verify(player).sendMessage("bought with both")
    );
  }

  @Test
  void upgradeAccountSendsNotEnoughCoinsMessageWhenTotalIsInsufficient() {
    BankTierEntry currentTier = createBankTierEntry(1, 100L, 2.0, 10000);
    BankTierEntry targetTier = createBankTierEntry(2, 500L, 5.0, 50000);
    BankAccountEntry bae = createBankAccountEntry(1, 100.0, currentTier);
    PlayerEntry pe = createPlayerEntry(100.0);

    when(bankTierRegistry.getBankTiers()).thenReturn(List.of(targetTier));
    when(serviceContext.getTranslationService()).thenReturn(translationService);
    when(translationService.get(any(MessageKey.class), any())).thenReturn("lore");
    when(plugin.getName()).thenReturn("essentials");
    when(translationService.getWithPrefix(eq(MessageKey.PLUGIN_EVENT_NPC_BANKER_NOT_ENOUGH_COINS), any())).thenReturn("not enough");

    List<ItemHelper> tiers = bankService.getBankTiers();
    ItemStack upgradeItem = tiers.getFirst().getCustomItem();

    bankService.upgradeAccount(upgradeItem, player, pe, bae);

    verify(player).sendMessage("not enough");
  }

  @Test
  void upgradeAccountSendsAlreadyBoughtMessageWhenSameCost() {
    BankTierEntry currentTier = createBankTierEntry(1, 500L, 2.0, 10000);
    BankTierEntry targetTier = createBankTierEntry(2, 500L, 5.0, 50000);
    BankAccountEntry bae = createBankAccountEntry(1, 1000.0, currentTier);
    PlayerEntry pe = createPlayerEntry(1000.0);

    when(bankTierRegistry.getBankTiers()).thenReturn(List.of(targetTier));
    when(serviceContext.getTranslationService()).thenReturn(translationService);
    when(translationService.get(any(MessageKey.class), any())).thenReturn("lore");
    when(plugin.getName()).thenReturn("essentials");
    when(translationService.getWithPrefix(MessageKey.PLUGIN_EVENT_NPC_BANKER_BUY_ALREADY_BOUGHT)).thenReturn("already bought");

    List<ItemHelper> tiers = bankService.getBankTiers();
    ItemStack upgradeItem = tiers.getFirst().getCustomItem();

    bankService.upgradeAccount(upgradeItem, player, pe, bae);

    verify(player).sendMessage("already bought");
  }

  @Test
  void upgradeAccountSendsAlreadyBoughtMessageWhenSameTierId() {
    BankTierEntry currentTier = createBankTierEntry(2, 100L, 2.0, 10000);
    BankTierEntry targetTier = createBankTierEntry(2, 500L, 5.0, 50000);
    BankAccountEntry bae = createBankAccountEntry(1, 1000.0, currentTier);
    PlayerEntry pe = createPlayerEntry(1000.0);

    when(bankTierRegistry.getBankTiers()).thenReturn(List.of(targetTier));
    when(serviceContext.getTranslationService()).thenReturn(translationService);
    when(translationService.get(any(MessageKey.class), any())).thenReturn("lore");
    when(plugin.getName()).thenReturn("essentials");
    when(translationService.getWithPrefix(MessageKey.PLUGIN_EVENT_NPC_BANKER_BUY_ALREADY_BOUGHT)).thenReturn("already bought");

    List<ItemHelper> tiers = bankService.getBankTiers();
    ItemStack upgradeItem = tiers.getFirst().getCustomItem();

    bankService.upgradeAccount(upgradeItem, player, pe, bae);

    verify(player).sendMessage("already bought");
  }

  @Test
  void upgradeAccountSendsLowerAccountMessageWhenCurrentTierCostIsHigher() {
    BankTierEntry currentTier = createBankTierEntry(3, 1000L, 2.0, 10000);
    BankTierEntry targetTier = createBankTierEntry(2, 500L, 5.0, 50000);
    BankAccountEntry bae = createBankAccountEntry(1, 1000.0, currentTier);
    PlayerEntry pe = createPlayerEntry(2000.0);

    when(bankTierRegistry.getBankTiers()).thenReturn(List.of(targetTier));
    when(serviceContext.getTranslationService()).thenReturn(translationService);
    when(translationService.get(any(MessageKey.class), any())).thenReturn("lore");
    when(plugin.getName()).thenReturn("essentials");
    when(translationService.getWithPrefix(MessageKey.PLUGIN_EVENT_NPC_BANKER_BUY_LOWER_ACCOUNT)).thenReturn("lower account");

    List<ItemHelper> tiers = bankService.getBankTiers();
    ItemStack upgradeItem = tiers.getFirst().getCustomItem();

    bankService.upgradeAccount(upgradeItem, player, pe, bae);

    verify(player).sendMessage("lower account");
  }

  @Test
  void upgradeAccountDoesNothingWhenTierEntryNotFound() {
    BankTierEntry currentTier = createBankTierEntry(1, 100L, 2.0, 10000);
    BankTierEntry registryTier = createBankTierEntry(2, 500L, 5.0, 50000);
    BankAccountEntry bae = createBankAccountEntry(1, 1000.0, currentTier);
    PlayerEntry pe = createPlayerEntry(1000.0);

    when(bankTierRegistry.getBankTiers()).thenReturn(List.of(registryTier));
    when(serviceContext.getTranslationService()).thenReturn(translationService);
    when(translationService.get(any(MessageKey.class), any())).thenReturn("lore");
    when(plugin.getName()).thenReturn("essentials");

    List<ItemHelper> tiers = bankService.getBankTiers();
    ItemStack upgradeItem = tiers.getFirst().getCustomItem();

    when(bankTierRegistry.getBankTiers()).thenReturn(List.of());

    bankService.upgradeAccount(upgradeItem, player, pe, bae);

    verify(bankRepository, never()).updateBankAccount(anyInt(), anyFloat(), anyDouble(), anyInt());
    verify(bankRepository, never()).addTransactionToBank(anyInt(), anyInt(), anyDouble(), anyDouble(), anyInt());
  }

  @Test
  void findBankAccountByPlayerIdReturnsBankAccountEntry() {
    BankTierEntry tier = createBankTierEntry(1, 0L, 2.0, 10000);
    BankAccountEntry expected = createBankAccountEntry(42, 500.0, tier);
    when(bankRepository.findBankAccountByPlayerId(42)).thenReturn(expected);

    BankAccountEntry result = bankService.findBankAccountByPlayerId(42);

    assertEquals(expected, result);
  }

  @Test
  void findBankAccountByPlayerIdReturnsNullWhenNotFound() {
    when(bankRepository.findBankAccountByPlayerId(99)).thenReturn(null);

    BankAccountEntry result = bankService.findBankAccountByPlayerId(99);

    assertNull(result);
  }

  @Test
  void insertBankAccountDelegatesToRepository() {
    BankTierEntry tier = createBankTierEntry(1, 0L, 2.0, 10000);
    BankAccountEntry bae = createBankAccountEntry(1, 0.0, tier);

    bankService.insertBankAccount(bae);

    verify(bankRepository).insertBankAccount(bae);
  }

  @Test
  void findTransactionsByBankAccountIdDelegatesToRepository() {
    List<BankTransactionEntry> expectedTransactions = new ArrayList<>();
    when(bankRepository.findTransactionsByBankAccountId(1)).thenReturn(expectedTransactions);

    List<BankTransactionEntry> result = bankService.findTransactionsByBankAccountId(1);

    assertEquals(expectedTransactions, result);
  }

  @Test
  void getBankTierEntryByCostReturnsTierWhenMatchFound() {
    BankTierEntry tier = createBankTierEntry(1, 500L, 2.0, 10000);
    when(bankTierRegistry.getBankTiers()).thenReturn(List.of(tier));

    BankTierEntry result = bankService.getBankTierEntryByCost(500L);

    assertEquals(tier, result);
  }

  @Test
  void getBankTierEntryByCostReturnsNullWhenNoMatchFound() {
    BankTierEntry tier = createBankTierEntry(1, 500L, 2.0, 10000);
    when(bankTierRegistry.getBankTiers()).thenReturn(List.of(tier));

    BankTierEntry result = bankService.getBankTierEntryByCost(999L);

    assertNull(result);
  }

  @Test
  void getBankTierEntryByIdDelegatesToRegistry() {
    BankTierEntry tier = createBankTierEntry(3, 500L, 2.0, 10000);
    when(bankTierRegistry.getBankTierById(3)).thenReturn(tier);

    BankTierEntry result = bankService.getBankTierEntryById(3);

    assertEquals(tier, result);
  }

  @Test
  void getBankTierEntryByIdReturnsNullWhenNotFound() {
    when(bankTierRegistry.getBankTierById(99)).thenReturn(null);

    BankTierEntry result = bankService.getBankTierEntryById(99);

    assertNull(result);
  }

  @Test
  void payInterestToPlayerDoesNothingWhenPlayerNotInInterestMap() {
    UUID uuid = UUID.randomUUID();
    when(player.getUniqueId()).thenReturn(uuid);

    bankService.payInterestToPlayer(player);

    verify(bankRepository, never()).addTransactionToBank(anyInt(), anyInt(), anyDouble(), anyDouble(), anyInt());
  }

  @Test
  void payInterestToPlayerPaysInterestAndRemovesFromMapWhenPresent() {
    UUID uuid = UUID.randomUUID();
    BankTierEntry tier = createBankTierEntry(1, 0L, 10.0, 100000);
    BankAccountEntry bae = createBankAccountEntry(1, 1000.0, tier);

    when(player.getUniqueId()).thenReturn(uuid);
    when(serviceContext.getTranslationService()).thenReturn(translationService);
    when(translationService.getWithPrefix(eq(MessageKey.PLUGIN_EVENT_NPC_BANKER_INTEREST), any(), any())).thenReturn("interest paid");

    bankService.checkInterest(uuid, true);

    when(bankRepository.findBankAccountByPlayerId(anyInt())).thenReturn(null);
    when(serviceContext.getPlayerService()).thenReturn(playerService);
    PlayerEntry pe = createPlayerEntry(0.0);
    when(playerService.getPlayerEntry(uuid)).thenReturn(pe);
    when(bankRepository.findBankAccountByPlayerId(1)).thenReturn(bae);

    BankService freshService = new BankService(serviceContext, bankTierRegistry, bankRepository, plugin);
    freshService.checkInterest(uuid, true);
    freshService.payInterestToPlayer(player);

    verify(bankRepository).addTransactionToBank(eq(1), eq(1), eq(100.0), eq(1000.0), eq(1));
    verify(player).sendMessage("interest paid");
  }
}