package de.relluem94.minecraft.server.spigot.essentials.listeners.npc;

import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_COLOR_MONEY;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_EVENT_NPC_BANKER_TRANSACTION_NEGATIVE;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_EVENT_NPC_BANKER_TRANSACTION_POSITIVE;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_NAME_MONEY;
import static de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants.PLUGIN_ITEM_NAMESPACE_NPC_GUI_DISABLED;

import de.relluem94.minecraft.server.spigot.essentials.annotations.ListenerName;
import de.relluem94.minecraft.server.spigot.essentials.constants.Constants;
import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.helpers.InventoryHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.ItemHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.StringHelper;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.ListenerConstruct;
import de.relluem94.minecraft.server.spigot.essentials.models.RelluEssentialsNamespacedKey;
import de.relluem94.minecraft.server.spigot.essentials.models.items.CustomItem;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.BankAccountEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.BankTransactionEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.PlayerEntry;
import de.relluem94.minecraft.server.spigot.essentials.services.BankService;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;
import lombok.NonNull;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

@ListenerName("InventoryClickNpc")
public class InventoryClickNpc implements ListenerConstruct {

  private NpcTradeHandler tradeHandler;
  private ServiceContext serviceContext;
  private final Map<ItemHelper, BiConsumer<Player, BankAccountEntry>> bankerDepositActions = Map.of(
      BankService.npc_gui_deposit_5_percent, (p, bae) -> serviceContext.getBankService()
          .deposit(serviceContext.getPlayerService().getPlayerEntry(p), p, bae, 5f),
      BankService.npc_gui_deposit_20_percent, (p, bae) -> serviceContext.getBankService()
          .deposit(serviceContext.getPlayerService().getPlayerEntry(p), p, bae, 20f),
      BankService.npc_gui_deposit_50_percent, (p, bae) -> serviceContext.getBankService()
          .deposit(serviceContext.getPlayerService().getPlayerEntry(p), p, bae, 50f),
      BankService.npc_gui_deposit_all, (p, bae) -> serviceContext.getBankService()
          .deposit(serviceContext.getPlayerService().getPlayerEntry(p), p, bae, 100f),
      BankService.npc_gui_withdraw_5_percent, (p, bae) -> serviceContext.getBankService()
          .withdraw(serviceContext.getPlayerService().getPlayerEntry(p), p, bae, 5f),
      BankService.npc_gui_withdraw_20_percent, (p, bae) -> serviceContext.getBankService()
          .withdraw(serviceContext.getPlayerService().getPlayerEntry(p), p, bae, 20f),
      BankService.npc_gui_withdraw_50_percent, (p, bae) -> serviceContext.getBankService()
          .withdraw(serviceContext.getPlayerService().getPlayerEntry(p), p, bae, 50f),
      BankService.npc_gui_withdraw_all, (p, bae) -> serviceContext.getBankService()
          .withdraw(serviceContext.getPlayerService().getPlayerEntry(p), p, bae, 100f));

  @Override
  public void injectContext(ServiceContext context) {
    this.serviceContext = context;
    tradeHandler = new NpcTradeHandler(context);
  }

  @EventHandler
  public void onInventoryClickItem(@NonNull InventoryClickEvent e) {
    if (!(e.getWhoClicked() instanceof Player player) || e.getCurrentItem() == null) {
      return;
    }

    PlayerEntry playerEntry = serviceContext.getPlayerService().getPlayerEntry(player);
    String title = e.getView().getTitle();

    if (title.equals(serviceContext.getTraderNpcService().getBankerNpc().getTitle())) {
      handleBankerInventory(e, player, playerEntry);
    } else if (serviceContext.getTraderNpcService().getTraderNpcTitles().contains(title)) {
      tradeHandler.handle(e.getCurrentItem(), e.getClickedInventory(), player, playerEntry,
          e.getSlot(), e.isRightClick());
      e.setCancelled(true);
    } else if (isNpcOrCustomHeadsInventory(title)) {
      handleNpcOrCustomHeadsInventory(e, player);
    } else if (title.equals(
        Constants.PLUGIN_NAME_PREFIX + Constants.PLUGIN_FORMS_SPACER_MESSAGE + "§dWorlds")) {
      handleWorldsInventory(e, player);
    }
  }

  private void handleBankerInventory(InventoryClickEvent e, Player player,
      PlayerEntry playerEntry) {
    e.setCancelled(true);
    ItemStack clickedItem = e.getCurrentItem();
    BankAccountEntry bankAccount = serviceContext.getBankService()
        .findBankAccountByPlayerId(playerEntry.getId());

    Optional<CustomItem> optionalDisabledItemHelper = serviceContext.getItemService().find(
        new RelluEssentialsNamespacedKey(serviceContext.getPluginMetadataService().getName(),
            PLUGIN_ITEM_NAMESPACE_NPC_GUI_DISABLED));

    if (clickedItem == null) {
      return;
    }

    if (bankAccount == null) {
      return;
    }

    if (optionalDisabledItemHelper.isEmpty()) {
      return;
    }

    CustomItem disabledItemHelper = optionalDisabledItemHelper.get();

    if (BankService.npc_gui_deposit.equalsName(clickedItem)) {
      InventoryHelper.closeInventory(player);
      InventoryHelper.openInventory(player, serviceContext.getTraderNpcService().getBankerNpc()
          .getDepositGUI(playerEntry.getPurse()));
    } else if (BankService.npc_gui_balance_total.equalsName(clickedItem)) {
      InventoryHelper.closeInventory(player);
      player.sendMessage(serviceContext.getTranslationService()
          .getWithPrefix(MessageKey.PLUGIN_EVENT_NPC_BANKER_TOTAL,
              StringHelper.formatDouble(bankAccount.getValue()), PLUGIN_NAME_MONEY));
    } else if (BankService.npc_gui_balance.equalsName(clickedItem)) {
      InventoryHelper.closeInventory(player);
      InventoryHelper.openInventory(player,
          serviceContext.getTraderNpcService().getBankerNpc().getBalanceGUI());
    } else if (BankService.npc_gui_withdraw.getCustomItem().getType()
        .equals(clickedItem.getType())) {
      InventoryHelper.closeInventory(player);
      InventoryHelper.openInventory(player, serviceContext.getTraderNpcService().getBankerNpc()
          .getWithdrawGUI(bankAccount.getValue()));
    } else if (BankService.UPGRADE_MATERIAL.equals(clickedItem.getType())) {
      serviceContext.getBankService().upgradeAccount(clickedItem, player, playerEntry, bankAccount);
    } else if (BankService.npc_gui_balance_transactions.equalsExact(clickedItem)) {
      handleTransactionHistory(player, bankAccount);
    } else if (BankService.npc_gui_upgrade.equalsExact(clickedItem)) {
      InventoryHelper.closeInventory(player);
      InventoryHelper.openInventory(player,
          serviceContext.getTraderNpcService().getBankerNpc().getUpgradeGUI());
    } else if (disabledItemHelper.toItemStack().isSimilar(clickedItem)) {
      InventoryHelper.closeInventory(player);
    } else {
      bankerDepositActions.entrySet().stream()
          .filter(entry -> entry.getKey().equalsExact(clickedItem)).findFirst()
          .ifPresent(entry -> entry.getValue().accept(player, bankAccount));
    }
  }

  private void handleTransactionHistory(Player player, BankAccountEntry bankAccount) {
    InventoryHelper.closeInventory(player);
    player.sendMessage(serviceContext.getTranslationService()
        .getWithPrefix(MessageKey.PLUGIN_EVENT_NPC_BANKER_TRANSACTION));
    List<BankTransactionEntry> transactions = serviceContext.getBankService()
        .findTransactionsByBankAccountId(bankAccount.getId());
    transactions.forEach(transaction -> player.sendMessage(serviceContext.getTranslationService()
        .getWithPrefix(MessageKey.PLUGIN_EVENT_NPC_BANKER_TRANSACTION_LIST,
            transaction.getValue() > 1 ? PLUGIN_EVENT_NPC_BANKER_TRANSACTION_POSITIVE
                : PLUGIN_EVENT_NPC_BANKER_TRANSACTION_NEGATIVE, PLUGIN_COLOR_MONEY,
            StringHelper.formatDouble(transaction.getValue()), PLUGIN_NAME_MONEY,
            transaction.getCreated())));
  }

  private boolean isNpcOrCustomHeadsInventory(@NonNull String title) {
    return title.equals(
        Constants.PLUGIN_NAME_PREFIX + Constants.PLUGIN_FORMS_SPACER_MESSAGE + "§dNPCs")
        || title.equals(
        serviceContext.getTranslationService().getWithPrefix(MessageKey.COMMAND_CUSTOMHEADS_TITLE));
  }

  private void handleNpcOrCustomHeadsInventory(InventoryClickEvent e, Player player) {
    e.setCancelled(true);
    if (e.getCurrentItem() == null) {
      return;
    }

    Optional<CustomItem> optionalItemHelper = serviceContext.getItemService().find(
        new RelluEssentialsNamespacedKey(serviceContext.getPluginMetadataService().getName(),
            PLUGIN_ITEM_NAMESPACE_NPC_GUI_DISABLED));
    if (optionalItemHelper.isEmpty()) {
      return;
    }

    CustomItem disabledItemHelper = optionalItemHelper.get();

    if (!disabledItemHelper.toItemStack().equals(e.getCurrentItem())) {
      player.getInventory().addItem(e.getCurrentItem().clone());
    }
  }

  private void handleWorldsInventory(InventoryClickEvent e, Player player) {
    e.setCancelled(true);
    if (e.getCurrentItem() == null) {
      return;
    }
    if (serviceContext.getItemService().isItemStack(
        new RelluEssentialsNamespacedKey(serviceContext.getPluginMetadataService().getName(),
            PLUGIN_ITEM_NAMESPACE_NPC_GUI_DISABLED), e.getCurrentItem())) {
      return;
    }
    if (e.getCurrentItem().getItemMeta() == null) {
      return;
    }
    serviceContext.getTeleportService()
        .teleportWorld(player, e.getCurrentItem().getItemMeta().getDisplayName());
  }
}