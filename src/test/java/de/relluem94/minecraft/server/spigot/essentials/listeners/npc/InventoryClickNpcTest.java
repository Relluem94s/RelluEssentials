package de.relluem94.minecraft.server.spigot.essentials.listeners.npc;

import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_COLOR_MONEY;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_EVENT_NPC_BANKER_TRANSACTION_NEGATIVE;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_EVENT_NPC_BANKER_TRANSACTION_POSITIVE;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_NAME_MONEY;
import static de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants.PLUGIN_ITEM_NAMESPACE_BANK_BALANCE;
import static de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants.PLUGIN_ITEM_NAMESPACE_BANK_BALANCE_TOTAL;
import static de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants.PLUGIN_ITEM_NAMESPACE_BANK_BALANCE_TRANSACTIONS;
import static de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants.PLUGIN_ITEM_NAMESPACE_BANK_DEPOSIT;
import static de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants.PLUGIN_ITEM_NAMESPACE_BANK_DEPOSIT_20_PERCENT;
import static de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants.PLUGIN_ITEM_NAMESPACE_BANK_DEPOSIT_5_PERCENT;
import static de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants.PLUGIN_ITEM_NAMESPACE_BANK_UPGRADE;
import static de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants.PLUGIN_ITEM_NAMESPACE_BANK_WITHDRAW;
import static de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants.PLUGIN_ITEM_NAMESPACE_BANK_WITHDRAW_5_PERCENT;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.relluem94.minecraft.server.spigot.essentials.constants.Constants;
import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.helpers.InventoryHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.StringHelper;
import de.relluem94.minecraft.server.spigot.essentials.models.RelluEssentialsNamespacedKey;
import de.relluem94.minecraft.server.spigot.essentials.models.items.CustomItem;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.BankAccountEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.BankTransactionEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.PlayerEntry;
import de.relluem94.minecraft.server.spigot.essentials.npcs.trader.BankerNpc;
import de.relluem94.minecraft.server.spigot.essentials.services.BankService;
import de.relluem94.minecraft.server.spigot.essentials.services.ItemService;
import de.relluem94.minecraft.server.spigot.essentials.services.PlayerService;
import de.relluem94.minecraft.server.spigot.essentials.services.PluginMetadataService;
import de.relluem94.minecraft.server.spigot.essentials.services.TeleportService;
import de.relluem94.minecraft.server.spigot.essentials.services.TraderNpcService;
import de.relluem94.minecraft.server.spigot.essentials.services.TranslationService;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class InventoryClickNpcTest {

  @Mock
  private ServiceContext serviceContext;
  @Mock
  private TraderNpcService traderNpcService;
  @Mock
  private BankService bankService;
  @Mock
  private PlayerService playerService;
  @Mock
  private ItemService itemService;
  @Mock
  private TranslationService translationService;
  @Mock
  private PluginMetadataService pluginMetadataService;
  @Mock
  private TeleportService teleportService;
  @Mock
  private BankerNpc bankerNpc;
  @Mock
  private NpcTradeHandler npcTradeHandler;

  private InventoryClickNpc listener;

  @BeforeEach
  void setUp() {
    when(serviceContext.getTraderNpcService()).thenReturn(traderNpcService);
    when(serviceContext.getBankService()).thenReturn(bankService);
    when(serviceContext.getPlayerService()).thenReturn(playerService);
    lenient().when(serviceContext.getItemService()).thenReturn(itemService);
    lenient().when(serviceContext.getTranslationService()).thenReturn(translationService);
    lenient().when(serviceContext.getPluginMetadataService()).thenReturn(pluginMetadataService);
    lenient().when(serviceContext.getTeleportService()).thenReturn(teleportService);
    lenient().when(pluginMetadataService.getName()).thenReturn("relluessentials");
    lenient().when(traderNpcService.getBankerNpc()).thenReturn(bankerNpc);
    lenient().when(bankerNpc.getTitle()).thenReturn("BankerTitle");
    lenient().when(traderNpcService.getTraderNpcTitles()).thenReturn(List.of("TraderTitle"));

    CustomItem anyItem = mock(CustomItem.class);
    ItemStack anyItemStack = mock(ItemStack.class);
    when(anyItem.toItemStack()).thenReturn(anyItemStack);
    when(itemService.find(any())).thenReturn(Optional.of(anyItem));

    listener = new InventoryClickNpc();
    listener.injectContext(serviceContext);
  }

  private InventoryClickEvent buildClickEvent(Player player, String title, ItemStack clickedItem) {
    InventoryClickEvent event = mock(InventoryClickEvent.class);
    InventoryView view = mock(InventoryView.class);
    when(event.getWhoClicked()).thenReturn(player);
    when(event.getCurrentItem()).thenReturn(clickedItem);
    when(event.getView()).thenReturn(view);
    when(view.getTitle()).thenReturn(title);
    return event;
  }

  private Player buildPlayer() {
    Player player = mock(Player.class);
    PlayerInventory playerInventory = mock(PlayerInventory.class);
    lenient().when(player.getInventory()).thenReturn(playerInventory);
    return player;
  }

  private PlayerEntry buildPlayerEntry(int id) {
    PlayerEntry playerEntry = mock(PlayerEntry.class);
    lenient().when(playerEntry.getId()).thenReturn(id);
    lenient().when(playerEntry.getPurse()).thenReturn(500.0);
    return playerEntry;
  }

  private BankAccountEntry buildBankAccount(int id, double value) {
    BankAccountEntry bankAccount = mock(BankAccountEntry.class);
    lenient().when(bankAccount.getId()).thenReturn(id);
    lenient().when(bankAccount.getValue()).thenReturn(value);
    return bankAccount;
  }

  private CustomItem buildCustomItem(Material material) {
    CustomItem customItem = mock(CustomItem.class);
    ItemStack itemStack = mock(ItemStack.class);
    when(customItem.toItemStack()).thenReturn(itemStack);
    when(itemStack.getType()).thenReturn(material);
    return customItem;
  }

  private CustomItem buildCustomItemWithSimilar(boolean similar, ItemStack clickedItem) {
    CustomItem customItem = mock(CustomItem.class);
    ItemStack itemStack = mock(ItemStack.class);
    when(customItem.toItemStack()).thenReturn(itemStack);
    when(itemStack.isSimilar(clickedItem)).thenReturn(similar);
    return customItem;
  }

  @Test
  void onInventoryClickItemWhenClickerIsNotPlayerDoesNothing() {
    InventoryClickEvent event = mock(InventoryClickEvent.class);
    HumanEntity notAPlayer = mock(HumanEntity.class);
    when(event.getWhoClicked()).thenReturn(notAPlayer);

    listener.onInventoryClickItem(event);

    verify(event, never()).setCancelled(true);
  }

  @Test
  void onInventoryClickItemWhenCurrentItemIsNullDoesNothing() {
    Player player = buildPlayer();
    InventoryClickEvent event = mock(InventoryClickEvent.class);
    InventoryView view = mock(InventoryView.class);
    when(event.getWhoClicked()).thenReturn(player);
    when(event.getCurrentItem()).thenReturn(null);
    when(event.getView()).thenReturn(view);
    when(view.getTitle()).thenReturn("SomeUnknownTitle");
    when(playerService.getPlayerEntry(player)).thenReturn(mock(PlayerEntry.class));

    listener.onInventoryClickItem(event);

    verify(event, never()).setCancelled(true);
  }

  @Test
  void onInventoryClickItemWhenTitleMatchesBankerNpcCallsHandleBankerInventory() {
    Player player = buildPlayer();
    PlayerEntry playerEntry = buildPlayerEntry(1);
    BankAccountEntry bankAccount = buildBankAccount(10, 1000.0);
    ItemStack clickedItem = mock(ItemStack.class);

    when(playerService.getPlayerEntry(player)).thenReturn(playerEntry);
    when(bankService.findBankAccountByPlayerId(1)).thenReturn(bankAccount);

    CustomItem closeItem = mock(CustomItem.class);
    ItemStack closeItemStack = mock(ItemStack.class);
    when(closeItem.toItemStack()).thenReturn(closeItemStack);
    when(closeItemStack.isSimilar(clickedItem)).thenReturn(false);
    when(itemService.find(any())).thenReturn(Optional.of(closeItem));

    when(bankService.getBankItem(PLUGIN_ITEM_NAMESPACE_BANK_DEPOSIT)).thenReturn(null);
    when(bankService.getBankItem(PLUGIN_ITEM_NAMESPACE_BANK_BALANCE_TOTAL)).thenReturn(null);
    when(bankService.getBankItem(PLUGIN_ITEM_NAMESPACE_BANK_BALANCE)).thenReturn(null);
    when(bankService.getBankItem(PLUGIN_ITEM_NAMESPACE_BANK_WITHDRAW)).thenReturn(null);
    when(bankService.getBankItem(PLUGIN_ITEM_NAMESPACE_BANK_BALANCE_TRANSACTIONS)).thenReturn(null);
    when(bankService.getBankItem(PLUGIN_ITEM_NAMESPACE_BANK_UPGRADE)).thenReturn(null);
    when(clickedItem.getType()).thenReturn(Material.DIRT);
    when(itemService.getAll()).thenReturn(Map.of());

    InventoryClickEvent event = buildClickEvent(player, "BankerTitle", clickedItem);

    listener.onInventoryClickItem(event);

    verify(event).setCancelled(true);
  }

  @Test
  void onInventoryClickItemWhenTitleMatchesTraderNpcDelegatesToTradeHandler() {
    Player player = buildPlayer();
    PlayerEntry playerEntry = buildPlayerEntry(1);
    ItemStack clickedItem = mock(ItemStack.class);
    Inventory clickedInventory = mock(Inventory.class);

    when(playerService.getPlayerEntry(player)).thenReturn(playerEntry);

    InventoryClickEvent event = buildClickEvent(player, "TraderTitle", clickedItem);
    when(event.getClickedInventory()).thenReturn(clickedInventory);
    when(event.getSlot()).thenReturn(0);
    when(event.isRightClick()).thenReturn(false);

    listener.onInventoryClickItem(event);

    verify(event).setCancelled(true);
  }

  @Test
  void onInventoryClickItemWhenTitleMatchesNpcInventoryHandlesNpcInventory() {
    Player player = buildPlayer();
    PlayerEntry playerEntry = buildPlayerEntry(1);
    ItemStack clickedItem = mock(ItemStack.class);

    when(playerService.getPlayerEntry(player)).thenReturn(playerEntry);

    String npcTitle = Constants.PLUGIN_NAME_PREFIX + Constants.PLUGIN_FORMS_SPACER_MESSAGE + "§dNPCs";
    InventoryClickEvent event = buildClickEvent(player, npcTitle, clickedItem);

    CustomItem disabledItem = mock(CustomItem.class);
    ItemStack disabledItemStack = mock(ItemStack.class);
    when(disabledItem.toItemStack()).thenReturn(disabledItemStack);
    when(disabledItemStack.equals(clickedItem)).thenReturn(false);
    when(itemService.find(any())).thenReturn(Optional.of(disabledItem));

    ItemStack clonedItem = mock(ItemStack.class);
    when(clickedItem.clone()).thenReturn(clonedItem);

    listener.onInventoryClickItem(event);

    verify(event).setCancelled(true);
    verify(player.getInventory()).addItem(clonedItem);
  }

  @Test
  void onInventoryClickItemWhenTitleMatchesWorldsInventoryHandlesWorldsInventory() {
    Player player = buildPlayer();
    PlayerEntry playerEntry = buildPlayerEntry(1);
    ItemStack clickedItem = mock(ItemStack.class);
    org.bukkit.inventory.meta.ItemMeta meta = mock(org.bukkit.inventory.meta.ItemMeta.class);

    when(playerService.getPlayerEntry(player)).thenReturn(playerEntry);
    when(itemService.isItemStack(any(), eq(clickedItem))).thenReturn(false);
    when(clickedItem.getItemMeta()).thenReturn(meta);
    when(meta.getDisplayName()).thenReturn("world_name");

    String worldsTitle = Constants.PLUGIN_NAME_PREFIX + Constants.PLUGIN_FORMS_SPACER_MESSAGE + "§dWorlds";
    InventoryClickEvent event = buildClickEvent(player, worldsTitle, clickedItem);

    listener.onInventoryClickItem(event);

    verify(event).setCancelled(true);
    verify(teleportService).teleportWorld(player, "world_name");
  }

  @Test
  void onInventoryClickItemWhenTitleMatchesWorldsAndItemIsDisabledDoesNotTeleport() {
    Player player = buildPlayer();
    PlayerEntry playerEntry = buildPlayerEntry(1);
    ItemStack clickedItem = mock(ItemStack.class);

    when(playerService.getPlayerEntry(player)).thenReturn(playerEntry);
    when(itemService.isItemStack(any(), eq(clickedItem))).thenReturn(true);

    String worldsTitle = Constants.PLUGIN_NAME_PREFIX + Constants.PLUGIN_FORMS_SPACER_MESSAGE + "§dWorlds";
    InventoryClickEvent event = buildClickEvent(player, worldsTitle, clickedItem);

    listener.onInventoryClickItem(event);

    verify(teleportService, never()).teleportWorld(any(), anyString());
  }

  @Test
  void onInventoryClickItemWhenTitleMatchesWorldsAndItemMetaIsNullDoesNotTeleport() {
    Player player = buildPlayer();
    PlayerEntry playerEntry = buildPlayerEntry(1);
    ItemStack clickedItem = mock(ItemStack.class);

    when(playerService.getPlayerEntry(player)).thenReturn(playerEntry);
    when(itemService.isItemStack(any(), eq(clickedItem))).thenReturn(false);
    when(clickedItem.getItemMeta()).thenReturn(null);

    String worldsTitle = Constants.PLUGIN_NAME_PREFIX + Constants.PLUGIN_FORMS_SPACER_MESSAGE + "§dWorlds";
    InventoryClickEvent event = buildClickEvent(player, worldsTitle, clickedItem);

    listener.onInventoryClickItem(event);

    verify(teleportService, never()).teleportWorld(any(), anyString());
  }

  @Test
  void handleBankerInventoryWhenClickedItemIsNullDoesNothing() {
    Player player = buildPlayer();
    PlayerEntry playerEntry = buildPlayerEntry(1);

    when(playerService.getPlayerEntry(player)).thenReturn(playerEntry);

    InventoryClickEvent event = mock(InventoryClickEvent.class);
    InventoryView view = mock(InventoryView.class);
    when(event.getWhoClicked()).thenReturn(player);
    when(event.getCurrentItem()).thenReturn(null);
    when(event.getView()).thenReturn(view);
    when(view.getTitle()).thenReturn("BankerTitle");

    listener.onInventoryClickItem(event);

    verify(event).setCancelled(true);
    verify(bankService, never()).findBankAccountByPlayerId(any());
  }

  @Test
  void handleBankerInventoryWhenBankAccountIsNullDoesNothing() {
    Player player = buildPlayer();
    PlayerEntry playerEntry = buildPlayerEntry(1);
    ItemStack clickedItem = mock(ItemStack.class);

    when(playerService.getPlayerEntry(player)).thenReturn(playerEntry);
    when(bankService.findBankAccountByPlayerId(1)).thenReturn(null);

    InventoryClickEvent event = buildClickEvent(player, "BankerTitle", clickedItem);

    listener.onInventoryClickItem(event);

    verify(event).setCancelled(true);
    verify(itemService, never()).find(any());
  }

  @Test
  void handleBankerInventoryWhenCloseItemNotFoundDoesNothing() {
    Player player = buildPlayer();
    PlayerEntry playerEntry = buildPlayerEntry(1);
    ItemStack clickedItem = mock(ItemStack.class);
    BankAccountEntry bankAccount = buildBankAccount(10, 1000.0);

    when(playerService.getPlayerEntry(player)).thenReturn(playerEntry);
    when(bankService.findBankAccountByPlayerId(1)).thenReturn(bankAccount);
    when(itemService.find(any())).thenReturn(Optional.empty());

    InventoryClickEvent event = buildClickEvent(player, "BankerTitle", clickedItem);

    listener.onInventoryClickItem(event);

    verify(bankService, never()).getBankItem(anyString());
  }

  @Test
  void handleBankerInventoryWhenClickedItemMatchesDepositOpensDepositGUI() {
    Player player = buildPlayer();
    PlayerEntry playerEntry = buildPlayerEntry(1);
    BankAccountEntry bankAccount = buildBankAccount(10, 1000.0);

    ItemStack clickedItem = mock(ItemStack.class);
    when(clickedItem.getType()).thenReturn(Material.EMERALD);

    CustomItem closeItem = mock(CustomItem.class);
    ItemStack closeItemStack = mock(ItemStack.class);
    when(closeItem.toItemStack()).thenReturn(closeItemStack);
    when(itemService.find(any())).thenReturn(Optional.of(closeItem));

    CustomItem depositItem = buildCustomItem(Material.EMERALD);
    Inventory depositGui = mock(Inventory.class);

    when(playerService.getPlayerEntry(player)).thenReturn(playerEntry);
    when(bankService.findBankAccountByPlayerId(1)).thenReturn(bankAccount);
    when(bankService.getBankItem(PLUGIN_ITEM_NAMESPACE_BANK_DEPOSIT)).thenReturn(depositItem);
    when(bankerNpc.getDepositGUI(500.0)).thenReturn(depositGui);

    InventoryClickEvent event = buildClickEvent(player, "BankerTitle", clickedItem);

    try (MockedStatic<InventoryHelper> inventoryHelper = Mockito.mockStatic(InventoryHelper.class)) {
      listener.onInventoryClickItem(event);
      inventoryHelper.verify(() -> InventoryHelper.closeInventory(player));
      inventoryHelper.verify(() -> InventoryHelper.openInventory(player, depositGui));
    }
  }

  @Test
  void handleBankerInventoryWhenClickedItemMatchesTotalBalanceSendsMessage() {
    Player player = buildPlayer();
    PlayerEntry playerEntry = buildPlayerEntry(1);
    BankAccountEntry bankAccount = buildBankAccount(10, 1000.0);

    ItemStack clickedItem = mock(ItemStack.class);
    when(clickedItem.getType()).thenReturn(Material.GOLD_INGOT);

    CustomItem closeItem = mock(CustomItem.class);
    ItemStack closeItemStack = mock(ItemStack.class);
    when(closeItem.toItemStack()).thenReturn(closeItemStack);
    when(itemService.find(any())).thenReturn(Optional.of(closeItem));

    CustomItem depositItem = buildCustomItem(Material.EMERALD);
    CustomItem totalBalanceItem = buildCustomItem(Material.GOLD_INGOT);

    when(playerService.getPlayerEntry(player)).thenReturn(playerEntry);
    when(bankService.findBankAccountByPlayerId(1)).thenReturn(bankAccount);
    when(bankService.getBankItem(PLUGIN_ITEM_NAMESPACE_BANK_DEPOSIT)).thenReturn(depositItem);
    when(bankService.getBankItem(PLUGIN_ITEM_NAMESPACE_BANK_BALANCE_TOTAL)).thenReturn(totalBalanceItem);
    when(translationService.getWithPrefix(eq(MessageKey.PLUGIN_EVENT_NPC_BANKER_TOTAL), anyString(), anyString()))
        .thenReturn("balance message");

    InventoryClickEvent event = buildClickEvent(player, "BankerTitle", clickedItem);

    try (MockedStatic<InventoryHelper> inventoryHelper = Mockito.mockStatic(InventoryHelper.class)) {
      try (MockedStatic<StringHelper> stringHelper = Mockito.mockStatic(StringHelper.class)) {
        stringHelper.when(() -> StringHelper.formatDouble(1000.0)).thenReturn("1000.00");
        listener.onInventoryClickItem(event);
        inventoryHelper.verify(() -> InventoryHelper.closeInventory(player));
        verify(player).sendMessage("balance message");
      }
    }
  }

  @Test
  void handleBankerInventoryWhenClickedItemMatchesBalanceOpensBalanceGUI() {
    Player player = buildPlayer();
    PlayerEntry playerEntry = buildPlayerEntry(1);
    BankAccountEntry bankAccount = buildBankAccount(10, 1000.0);

    ItemStack clickedItem = mock(ItemStack.class);
    when(clickedItem.getType()).thenReturn(Material.DIAMOND);

    CustomItem closeItem = mock(CustomItem.class);
    ItemStack closeItemStack = mock(ItemStack.class);
    when(closeItem.toItemStack()).thenReturn(closeItemStack);
    when(itemService.find(any())).thenReturn(Optional.of(closeItem));

    CustomItem depositItem = buildCustomItem(Material.EMERALD);
    CustomItem totalBalanceItem = buildCustomItem(Material.GOLD_INGOT);
    CustomItem balanceItem = buildCustomItem(Material.DIAMOND);
    Inventory balanceGui = mock(Inventory.class);

    when(playerService.getPlayerEntry(player)).thenReturn(playerEntry);
    when(bankService.findBankAccountByPlayerId(1)).thenReturn(bankAccount);
    when(bankService.getBankItem(PLUGIN_ITEM_NAMESPACE_BANK_DEPOSIT)).thenReturn(depositItem);
    when(bankService.getBankItem(PLUGIN_ITEM_NAMESPACE_BANK_BALANCE_TOTAL)).thenReturn(totalBalanceItem);
    when(bankService.getBankItem(PLUGIN_ITEM_NAMESPACE_BANK_BALANCE)).thenReturn(balanceItem);
    when(bankerNpc.getBalanceGUI()).thenReturn(balanceGui);

    InventoryClickEvent event = buildClickEvent(player, "BankerTitle", clickedItem);

    try (MockedStatic<InventoryHelper> inventoryHelper = Mockito.mockStatic(InventoryHelper.class)) {
      listener.onInventoryClickItem(event);
      inventoryHelper.verify(() -> InventoryHelper.closeInventory(player));
      inventoryHelper.verify(() -> InventoryHelper.openInventory(player, balanceGui));
    }
  }

  @Test
  void handleBankerInventoryWhenClickedItemMatchesWithdrawOpensWithdrawGUI() {
    Player player = buildPlayer();
    PlayerEntry playerEntry = buildPlayerEntry(1);
    BankAccountEntry bankAccount = buildBankAccount(10, 1000.0);

    ItemStack clickedItem = mock(ItemStack.class);
    when(clickedItem.getType()).thenReturn(Material.IRON_INGOT);

    CustomItem closeItem = mock(CustomItem.class);
    ItemStack closeItemStack = mock(ItemStack.class);
    when(closeItem.toItemStack()).thenReturn(closeItemStack);
    when(itemService.find(any())).thenReturn(Optional.of(closeItem));

    CustomItem depositItem = buildCustomItem(Material.EMERALD);
    CustomItem totalBalanceItem = buildCustomItem(Material.GOLD_INGOT);
    CustomItem balanceItem = buildCustomItem(Material.DIAMOND);
    CustomItem withdrawItem = buildCustomItem(Material.IRON_INGOT);
    Inventory withdrawGui = mock(Inventory.class);

    when(playerService.getPlayerEntry(player)).thenReturn(playerEntry);
    when(bankService.findBankAccountByPlayerId(1)).thenReturn(bankAccount);
    when(bankService.getBankItem(PLUGIN_ITEM_NAMESPACE_BANK_DEPOSIT)).thenReturn(depositItem);
    when(bankService.getBankItem(PLUGIN_ITEM_NAMESPACE_BANK_BALANCE_TOTAL)).thenReturn(totalBalanceItem);
    when(bankService.getBankItem(PLUGIN_ITEM_NAMESPACE_BANK_BALANCE)).thenReturn(balanceItem);
    when(bankService.getBankItem(PLUGIN_ITEM_NAMESPACE_BANK_WITHDRAW)).thenReturn(withdrawItem);
    when(bankerNpc.getWithdrawGUI(1000.0)).thenReturn(withdrawGui);

    InventoryClickEvent event = buildClickEvent(player, "BankerTitle", clickedItem);

    try (MockedStatic<InventoryHelper> inventoryHelper = Mockito.mockStatic(InventoryHelper.class)) {
      listener.onInventoryClickItem(event);
      inventoryHelper.verify(() -> InventoryHelper.closeInventory(player));
      inventoryHelper.verify(() -> InventoryHelper.openInventory(player, withdrawGui));
    }
  }

  @Test
  void handleBankerInventoryWhenClickedItemMatchesUpgradeMaterialCallsUpgradeAccount() {
    Player player = buildPlayer();
    PlayerEntry playerEntry = buildPlayerEntry(1);
    BankAccountEntry bankAccount = buildBankAccount(10, 1000.0);

    ItemStack clickedItem = mock(ItemStack.class);
    when(clickedItem.getType()).thenReturn(BankService.UPGRADE_MATERIAL);

    CustomItem closeItem = mock(CustomItem.class);
    ItemStack closeItemStack = mock(ItemStack.class);
    when(closeItem.toItemStack()).thenReturn(closeItemStack);
    when(itemService.find(any())).thenReturn(Optional.of(closeItem));

    CustomItem depositItem = buildCustomItem(Material.EMERALD);
    CustomItem totalBalanceItem = buildCustomItem(Material.GOLD_INGOT);
    CustomItem balanceItem = buildCustomItem(Material.DIAMOND);
    CustomItem withdrawItem = buildCustomItem(Material.IRON_INGOT);

    when(playerService.getPlayerEntry(player)).thenReturn(playerEntry);
    when(bankService.findBankAccountByPlayerId(1)).thenReturn(bankAccount);
    when(bankService.getBankItem(PLUGIN_ITEM_NAMESPACE_BANK_DEPOSIT)).thenReturn(depositItem);
    when(bankService.getBankItem(PLUGIN_ITEM_NAMESPACE_BANK_BALANCE_TOTAL)).thenReturn(totalBalanceItem);
    when(bankService.getBankItem(PLUGIN_ITEM_NAMESPACE_BANK_BALANCE)).thenReturn(balanceItem);
    when(bankService.getBankItem(PLUGIN_ITEM_NAMESPACE_BANK_WITHDRAW)).thenReturn(withdrawItem);

    InventoryClickEvent event = buildClickEvent(player, "BankerTitle", clickedItem);

    listener.onInventoryClickItem(event);

    verify(bankService).upgradeAccount(clickedItem, player, playerEntry, bankAccount);
  }

  @Test
  void handleBankerInventoryWhenClickedItemMatchesTransactionsSendsTransactionHistory() {
    Player player = buildPlayer();
    PlayerEntry playerEntry = buildPlayerEntry(1);
    BankAccountEntry bankAccount = buildBankAccount(10, 1000.0);

    ItemStack clickedItem = mock(ItemStack.class);
    when(clickedItem.getType()).thenReturn(Material.PAPER);

    CustomItem closeItem = mock(CustomItem.class);
    ItemStack closeItemStack = mock(ItemStack.class);
    when(closeItem.toItemStack()).thenReturn(closeItemStack);
    when(itemService.find(any())).thenReturn(Optional.of(closeItem));

    CustomItem depositItem = buildCustomItem(Material.EMERALD);
    CustomItem totalBalanceItem = buildCustomItem(Material.GOLD_INGOT);
    CustomItem balanceItem = buildCustomItem(Material.DIAMOND);
    CustomItem withdrawItem = buildCustomItem(Material.IRON_INGOT);
    CustomItem transactionsItem = buildCustomItem(Material.PAPER);

    BankTransactionEntry positiveTransaction = mock(BankTransactionEntry.class);
    when(positiveTransaction.getValue()).thenReturn(100.0);
    when(positiveTransaction.getCreated()).thenReturn("2024-01-01");

    BankTransactionEntry negativeTransaction = mock(BankTransactionEntry.class);
    when(negativeTransaction.getValue()).thenReturn(0.5);
    when(negativeTransaction.getCreated()).thenReturn("2024-01-02");

    when(playerService.getPlayerEntry(player)).thenReturn(playerEntry);
    when(bankService.findBankAccountByPlayerId(1)).thenReturn(bankAccount);
    when(bankService.getBankItem(PLUGIN_ITEM_NAMESPACE_BANK_DEPOSIT)).thenReturn(depositItem);
    when(bankService.getBankItem(PLUGIN_ITEM_NAMESPACE_BANK_BALANCE_TOTAL)).thenReturn(totalBalanceItem);
    when(bankService.getBankItem(PLUGIN_ITEM_NAMESPACE_BANK_BALANCE)).thenReturn(balanceItem);
    when(bankService.getBankItem(PLUGIN_ITEM_NAMESPACE_BANK_WITHDRAW)).thenReturn(withdrawItem);
    when(bankService.getBankItem(PLUGIN_ITEM_NAMESPACE_BANK_BALANCE_TRANSACTIONS)).thenReturn(transactionsItem);
    when(bankService.findTransactionsByBankAccountId(10)).thenReturn(List.of(positiveTransaction, negativeTransaction));
    when(translationService.getWithPrefix(MessageKey.PLUGIN_EVENT_NPC_BANKER_TRANSACTION)).thenReturn("header");
    when(translationService.getWithPrefix(eq(MessageKey.PLUGIN_EVENT_NPC_BANKER_TRANSACTION_LIST),
        eq(PLUGIN_EVENT_NPC_BANKER_TRANSACTION_POSITIVE), eq(PLUGIN_COLOR_MONEY), anyString(),
        eq(PLUGIN_NAME_MONEY), eq("2024-01-01"))).thenReturn("positive entry");
    when(translationService.getWithPrefix(eq(MessageKey.PLUGIN_EVENT_NPC_BANKER_TRANSACTION_LIST),
        eq(PLUGIN_EVENT_NPC_BANKER_TRANSACTION_NEGATIVE), eq(PLUGIN_COLOR_MONEY), anyString(),
        eq(PLUGIN_NAME_MONEY), eq("2024-01-02"))).thenReturn("negative entry");

    InventoryClickEvent event = buildClickEvent(player, "BankerTitle", clickedItem);

    try (MockedStatic<InventoryHelper> inventoryHelper = Mockito.mockStatic(InventoryHelper.class)) {
      try (MockedStatic<StringHelper> stringHelper = Mockito.mockStatic(StringHelper.class)) {
        stringHelper.when(() -> StringHelper.formatDouble(100.0)).thenReturn("100.00");
        stringHelper.when(() -> StringHelper.formatDouble(0.5)).thenReturn("0.50");
        listener.onInventoryClickItem(event);
        inventoryHelper.verify(() -> InventoryHelper.closeInventory(player));
        verify(player).sendMessage("header");
        verify(player).sendMessage("positive entry");
        verify(player).sendMessage("negative entry");
      }
    }
  }

  @Test
  void handleBankerInventoryWhenClickedItemMatchesUpgradeItemOpensUpgradeGUI() {
    Player player = buildPlayer();
    PlayerEntry playerEntry = buildPlayerEntry(1);
    BankAccountEntry bankAccount = buildBankAccount(10, 1000.0);

    ItemStack clickedItem = mock(ItemStack.class);
    when(clickedItem.getType()).thenReturn(Material.NETHER_STAR);

    CustomItem closeItem = mock(CustomItem.class);
    ItemStack closeItemStack = mock(ItemStack.class);
    when(closeItem.toItemStack()).thenReturn(closeItemStack);
    when(itemService.find(any())).thenReturn(Optional.of(closeItem));

    CustomItem depositItem = buildCustomItem(Material.EMERALD);
    CustomItem totalBalanceItem = buildCustomItem(Material.GOLD_INGOT);
    CustomItem balanceItem = buildCustomItem(Material.DIAMOND);
    CustomItem withdrawItem = buildCustomItem(Material.IRON_INGOT);
    CustomItem transactionsItem = buildCustomItem(Material.PAPER);
    CustomItem upgradeItem = buildCustomItem(Material.NETHER_STAR);
    Inventory upgradeGui = mock(Inventory.class);

    when(playerService.getPlayerEntry(player)).thenReturn(playerEntry);
    when(bankService.findBankAccountByPlayerId(1)).thenReturn(bankAccount);
    when(bankService.getBankItem(PLUGIN_ITEM_NAMESPACE_BANK_DEPOSIT)).thenReturn(depositItem);
    when(bankService.getBankItem(PLUGIN_ITEM_NAMESPACE_BANK_BALANCE_TOTAL)).thenReturn(totalBalanceItem);
    when(bankService.getBankItem(PLUGIN_ITEM_NAMESPACE_BANK_BALANCE)).thenReturn(balanceItem);
    when(bankService.getBankItem(PLUGIN_ITEM_NAMESPACE_BANK_WITHDRAW)).thenReturn(withdrawItem);
    when(bankService.getBankItem(PLUGIN_ITEM_NAMESPACE_BANK_BALANCE_TRANSACTIONS)).thenReturn(transactionsItem);
    when(bankService.getBankItem(PLUGIN_ITEM_NAMESPACE_BANK_UPGRADE)).thenReturn(upgradeItem);
    when(bankerNpc.getUpgradeGUI()).thenReturn(upgradeGui);

    InventoryClickEvent event = buildClickEvent(player, "BankerTitle", clickedItem);

    try (MockedStatic<InventoryHelper> inventoryHelper = Mockito.mockStatic(InventoryHelper.class)) {
      listener.onInventoryClickItem(event);
      inventoryHelper.verify(() -> InventoryHelper.closeInventory(player));
      inventoryHelper.verify(() -> InventoryHelper.openInventory(player, upgradeGui));
    }
  }

  @Test
  void handleBankerInventoryWhenClickedItemMatchesCloseItemClosesInventory() {
    Player player = buildPlayer();
    PlayerEntry playerEntry = buildPlayerEntry(1);
    BankAccountEntry bankAccount = buildBankAccount(10, 1000.0);

    ItemStack clickedItem = mock(ItemStack.class);
    when(clickedItem.getType()).thenReturn(Material.BARRIER);

    CustomItem closeItem = mock(CustomItem.class);
    ItemStack closeItemStack = mock(ItemStack.class);
    when(closeItem.toItemStack()).thenReturn(closeItemStack);
    when(closeItemStack.isSimilar(clickedItem)).thenReturn(true);
    when(itemService.find(any())).thenReturn(Optional.of(closeItem));

    CustomItem depositItem = buildCustomItem(Material.EMERALD);
    CustomItem totalBalanceItem = buildCustomItem(Material.GOLD_INGOT);
    CustomItem balanceItem = buildCustomItem(Material.DIAMOND);
    CustomItem withdrawItem = buildCustomItem(Material.IRON_INGOT);
    CustomItem transactionsItem = buildCustomItem(Material.PAPER);
    CustomItem upgradeItem = buildCustomItem(Material.NETHER_STAR);

    when(playerService.getPlayerEntry(player)).thenReturn(playerEntry);
    when(bankService.findBankAccountByPlayerId(1)).thenReturn(bankAccount);
    when(bankService.getBankItem(PLUGIN_ITEM_NAMESPACE_BANK_DEPOSIT)).thenReturn(depositItem);
    when(bankService.getBankItem(PLUGIN_ITEM_NAMESPACE_BANK_BALANCE_TOTAL)).thenReturn(totalBalanceItem);
    when(bankService.getBankItem(PLUGIN_ITEM_NAMESPACE_BANK_BALANCE)).thenReturn(balanceItem);
    when(bankService.getBankItem(PLUGIN_ITEM_NAMESPACE_BANK_WITHDRAW)).thenReturn(withdrawItem);
    when(bankService.getBankItem(PLUGIN_ITEM_NAMESPACE_BANK_BALANCE_TRANSACTIONS)).thenReturn(transactionsItem);
    when(bankService.getBankItem(PLUGIN_ITEM_NAMESPACE_BANK_UPGRADE)).thenReturn(upgradeItem);

    InventoryClickEvent event = buildClickEvent(player, "BankerTitle", clickedItem);

    try (MockedStatic<InventoryHelper> inventoryHelper = Mockito.mockStatic(InventoryHelper.class)) {
      listener.onInventoryClickItem(event);
      inventoryHelper.verify(() -> InventoryHelper.closeInventory(player));
    }
  }

  @Test
  void handleBankerInventoryWhenClickedItemMatchesDepositPercentActionExecutesDeposit() {
    Player player = buildPlayer();
    PlayerEntry playerEntry = buildPlayerEntry(1);
    BankAccountEntry bankAccount = buildBankAccount(10, 1000.0);

    ItemStack clickedItem = mock(ItemStack.class);
    org.bukkit.inventory.meta.ItemMeta clickedItemMeta = mock(org.bukkit.inventory.meta.ItemMeta.class);
    when(clickedItem.getType()).thenReturn(Material.STONE);
    when(clickedItem.getItemMeta()).thenReturn(clickedItemMeta);
    when(clickedItemMeta.getDisplayName()).thenReturn("Deposit5");

    CustomItem closeItem = mock(CustomItem.class);
    ItemStack closeItemStack = mock(ItemStack.class);
    when(closeItem.toItemStack()).thenReturn(closeItemStack);
    when(closeItemStack.isSimilar(clickedItem)).thenReturn(false);
    when(itemService.find(any())).thenReturn(Optional.of(closeItem));

    CustomItem depositPercentItem = mock(CustomItem.class);
    ItemStack depositPercentItemStack = mock(ItemStack.class);
    when(depositPercentItem.toItemStack()).thenReturn(depositPercentItemStack);
    when(depositPercentItemStack.getType()).thenReturn(Material.COBBLESTONE);
    when(depositPercentItem.displayName()).thenReturn("Deposit5");
    RelluEssentialsNamespacedKey deposit5Key = new RelluEssentialsNamespacedKey("relluessentials", PLUGIN_ITEM_NAMESPACE_BANK_DEPOSIT_5_PERCENT);
    when(depositPercentItem.relluEssentialsNamespacedKey()).thenReturn(deposit5Key);

    when(playerService.getPlayerEntry(player)).thenReturn(playerEntry);
    when(bankService.findBankAccountByPlayerId(1)).thenReturn(bankAccount);
    when(bankService.getBankItem(PLUGIN_ITEM_NAMESPACE_BANK_DEPOSIT)).thenReturn(null);
    when(bankService.getBankItem(PLUGIN_ITEM_NAMESPACE_BANK_BALANCE_TOTAL)).thenReturn(null);
    when(bankService.getBankItem(PLUGIN_ITEM_NAMESPACE_BANK_BALANCE)).thenReturn(null);
    when(bankService.getBankItem(PLUGIN_ITEM_NAMESPACE_BANK_WITHDRAW)).thenReturn(null);
    when(bankService.getBankItem(PLUGIN_ITEM_NAMESPACE_BANK_BALANCE_TRANSACTIONS)).thenReturn(null);
    when(bankService.getBankItem(PLUGIN_ITEM_NAMESPACE_BANK_UPGRADE)).thenReturn(null);
    when(itemService.getAll()).thenReturn(Map.of(deposit5Key.toString(), depositPercentItem));

    InventoryClickEvent event = buildClickEvent(player, "BankerTitle", clickedItem);

    listener.onInventoryClickItem(event);

    verify(bankService).deposit(playerEntry, player, bankAccount, 5f);
  }

  @Test
  void handleBankerInventoryWhenClickedItemMatchesWithdrawPercentActionExecutesWithdraw() {
    Player player = buildPlayer();
    PlayerEntry playerEntry = buildPlayerEntry(1);
    BankAccountEntry bankAccount = buildBankAccount(10, 1000.0);

    ItemStack clickedItem = mock(ItemStack.class);
    org.bukkit.inventory.meta.ItemMeta clickedItemMeta = mock(org.bukkit.inventory.meta.ItemMeta.class);
    when(clickedItem.getType()).thenReturn(Material.STONE);
    when(clickedItem.getItemMeta()).thenReturn(clickedItemMeta);
    when(clickedItemMeta.getDisplayName()).thenReturn("Withdraw5");

    CustomItem closeItem = mock(CustomItem.class);
    ItemStack closeItemStack = mock(ItemStack.class);
    when(closeItem.toItemStack()).thenReturn(closeItemStack);
    when(closeItemStack.isSimilar(clickedItem)).thenReturn(false);
    when(itemService.find(any())).thenReturn(Optional.of(closeItem));

    CustomItem withdrawPercentItem = mock(CustomItem.class);
    ItemStack withdrawPercentItemStack = mock(ItemStack.class);
    when(withdrawPercentItem.toItemStack()).thenReturn(withdrawPercentItemStack);
    when(withdrawPercentItemStack.getType()).thenReturn(Material.COBBLESTONE);
    when(withdrawPercentItem.displayName()).thenReturn("Withdraw5");
    RelluEssentialsNamespacedKey withdraw5Key = new RelluEssentialsNamespacedKey("relluessentials", PLUGIN_ITEM_NAMESPACE_BANK_WITHDRAW_5_PERCENT);
    when(withdrawPercentItem.relluEssentialsNamespacedKey()).thenReturn(withdraw5Key);

    when(playerService.getPlayerEntry(player)).thenReturn(playerEntry);
    when(bankService.findBankAccountByPlayerId(1)).thenReturn(bankAccount);
    when(bankService.getBankItem(PLUGIN_ITEM_NAMESPACE_BANK_DEPOSIT)).thenReturn(null);
    when(bankService.getBankItem(PLUGIN_ITEM_NAMESPACE_BANK_BALANCE_TOTAL)).thenReturn(null);
    when(bankService.getBankItem(PLUGIN_ITEM_NAMESPACE_BANK_BALANCE)).thenReturn(null);
    when(bankService.getBankItem(PLUGIN_ITEM_NAMESPACE_BANK_WITHDRAW)).thenReturn(null);
    when(bankService.getBankItem(PLUGIN_ITEM_NAMESPACE_BANK_BALANCE_TRANSACTIONS)).thenReturn(null);
    when(bankService.getBankItem(PLUGIN_ITEM_NAMESPACE_BANK_UPGRADE)).thenReturn(null);
    when(itemService.getAll()).thenReturn(Map.of(withdraw5Key.toString(), withdrawPercentItem));

    InventoryClickEvent event = buildClickEvent(player, "BankerTitle", clickedItem);

    listener.onInventoryClickItem(event);

    verify(bankService).withdraw(playerEntry, player, bankAccount, 5f);
  }

  @Test
  void handleBankerInventoryWhenClickedItemDisplayNameMatchesNoActionDoesNothing() {
    Player player = buildPlayer();
    PlayerEntry playerEntry = buildPlayerEntry(1);
    BankAccountEntry bankAccount = buildBankAccount(10, 1000.0);

    ItemStack clickedItem = mock(ItemStack.class);
    org.bukkit.inventory.meta.ItemMeta clickedItemMeta = mock(org.bukkit.inventory.meta.ItemMeta.class);
    when(clickedItem.getType()).thenReturn(Material.STONE);
    when(clickedItem.getItemMeta()).thenReturn(clickedItemMeta);
    when(clickedItemMeta.getDisplayName()).thenReturn("UnknownItem");

    CustomItem closeItem = mock(CustomItem.class);
    ItemStack closeItemStack = mock(ItemStack.class);
    when(closeItem.toItemStack()).thenReturn(closeItemStack);
    when(closeItemStack.isSimilar(clickedItem)).thenReturn(false);
    when(itemService.find(any())).thenReturn(Optional.of(closeItem));

    CustomItem someOtherItem = mock(CustomItem.class);
    ItemStack someOtherItemStack = mock(ItemStack.class);
    when(someOtherItem.toItemStack()).thenReturn(someOtherItemStack);
    when(someOtherItemStack.getType()).thenReturn(Material.COBBLESTONE);
    when(someOtherItem.displayName()).thenReturn("SomethingElse");
    RelluEssentialsNamespacedKey someKey = new RelluEssentialsNamespacedKey("relluessentials", PLUGIN_ITEM_NAMESPACE_BANK_DEPOSIT_20_PERCENT);

    when(playerService.getPlayerEntry(player)).thenReturn(playerEntry);
    when(bankService.findBankAccountByPlayerId(1)).thenReturn(bankAccount);
    when(bankService.getBankItem(PLUGIN_ITEM_NAMESPACE_BANK_DEPOSIT)).thenReturn(null);
    when(bankService.getBankItem(PLUGIN_ITEM_NAMESPACE_BANK_BALANCE_TOTAL)).thenReturn(null);
    when(bankService.getBankItem(PLUGIN_ITEM_NAMESPACE_BANK_BALANCE)).thenReturn(null);
    when(bankService.getBankItem(PLUGIN_ITEM_NAMESPACE_BANK_WITHDRAW)).thenReturn(null);
    when(bankService.getBankItem(PLUGIN_ITEM_NAMESPACE_BANK_BALANCE_TRANSACTIONS)).thenReturn(null);
    when(bankService.getBankItem(PLUGIN_ITEM_NAMESPACE_BANK_UPGRADE)).thenReturn(null);
    when(itemService.getAll()).thenReturn(Map.of(someKey.toString(), someOtherItem));

    InventoryClickEvent event = buildClickEvent(player, "BankerTitle", clickedItem);

    listener.onInventoryClickItem(event);

    verify(bankService, never()).deposit(any(), any(), any(), any());
    verify(bankService, never()).withdraw(any(), any(), any(), any());
  }

  @Test
  void handleNpcInventoryWhenDisabledItemMatchesClickedItemDoesNotAddToPlayerInventory() {
    Player player = buildPlayer();
    PlayerEntry playerEntry = buildPlayerEntry(1);
    ItemStack clickedItem = mock(ItemStack.class);

    when(playerService.getPlayerEntry(player)).thenReturn(playerEntry);

    String npcTitle = Constants.PLUGIN_NAME_PREFIX + Constants.PLUGIN_FORMS_SPACER_MESSAGE + "§dNPCs";
    InventoryClickEvent event = buildClickEvent(player, npcTitle, clickedItem);

    CustomItem disabledItem = mock(CustomItem.class);
    ItemStack disabledItemStack = mock(ItemStack.class);
    when(disabledItem.toItemStack()).thenReturn(disabledItemStack);
    when(disabledItemStack.equals(clickedItem)).thenReturn(true);
    when(itemService.find(any())).thenReturn(Optional.of(disabledItem));

    listener.onInventoryClickItem(event);

    verify(player.getInventory(), never()).addItem(any());
  }

  @Test
  void handleNpcInventoryWhenDisabledItemNotFoundDoesNothing() {
    Player player = buildPlayer();
    PlayerEntry playerEntry = buildPlayerEntry(1);
    ItemStack clickedItem = mock(ItemStack.class);

    when(playerService.getPlayerEntry(player)).thenReturn(playerEntry);

    String npcTitle = Constants.PLUGIN_NAME_PREFIX + Constants.PLUGIN_FORMS_SPACER_MESSAGE + "§dNPCs";
    InventoryClickEvent event = buildClickEvent(player, npcTitle, clickedItem);

    when(itemService.find(any())).thenReturn(Optional.empty());

    listener.onInventoryClickItem(event);

    verify(player.getInventory(), never()).addItem(any());
  }

  @Test
  void handleNpcInventoryWhenCurrentItemIsNullDoesNothing() {
    Player player = buildPlayer();
    PlayerEntry playerEntry = buildPlayerEntry(1);

    when(playerService.getPlayerEntry(player)).thenReturn(playerEntry);

    String npcTitle = Constants.PLUGIN_NAME_PREFIX + Constants.PLUGIN_FORMS_SPACER_MESSAGE + "§dNPCs";
    InventoryClickEvent event = mock(InventoryClickEvent.class);
    InventoryView view = mock(InventoryView.class);
    when(event.getWhoClicked()).thenReturn(player);
    when(event.getCurrentItem()).thenReturn(null);
    when(event.getView()).thenReturn(view);
    when(view.getTitle()).thenReturn(npcTitle);

    listener.onInventoryClickItem(event);

    verify(itemService, never()).find(any());
  }

  @Test
  void handleCustomHeadsInventoryWhenDisabledItemMatchesClickedItemDoesNotAddToPlayerInventory() {
    Player player = buildPlayer();
    PlayerEntry playerEntry = buildPlayerEntry(1);
    ItemStack clickedItem = mock(ItemStack.class);

    when(playerService.getPlayerEntry(player)).thenReturn(playerEntry);
    when(translationService.getWithPrefix(MessageKey.COMMAND_CUSTOMHEADS_TITLE)).thenReturn("CustomHeadsTitle");

    InventoryClickEvent event = buildClickEvent(player, "CustomHeadsTitle", clickedItem);

    CustomItem disabledItem = mock(CustomItem.class);
    ItemStack disabledItemStack = mock(ItemStack.class);
    when(disabledItem.toItemStack()).thenReturn(disabledItemStack);
    when(disabledItemStack.equals(clickedItem)).thenReturn(true);
    when(itemService.find(any())).thenReturn(Optional.of(disabledItem));

    listener.onInventoryClickItem(event);

    verify(player.getInventory(), never()).addItem(any());
  }

  @Test
  void onInventoryClickItemWhenTitleDoesNotMatchAnyHandlerDoesNothing() {
    Player player = buildPlayer();
    ItemStack clickedItem = mock(ItemStack.class);

    when(playerService.getPlayerEntry(player)).thenReturn(mock(PlayerEntry.class));
    when(translationService.getWithPrefix(MessageKey.COMMAND_CUSTOMHEADS_TITLE)).thenReturn("CustomHeadsTitle");

    InventoryClickEvent event = buildClickEvent(player, "CompletelyUnknownTitle", clickedItem);

    listener.onInventoryClickItem(event);

    verify(event, never()).setCancelled(true);
    verify(bankService, never()).findBankAccountByPlayerId(anyInt());
    verify(teleportService, never()).teleportWorld(any(), anyString());
  }
}