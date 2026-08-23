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
import static de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants.PLUGIN_ITEM_NAMESPACE_BANK_DEPOSIT_50_PERCENT;
import static de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants.PLUGIN_ITEM_NAMESPACE_BANK_DEPOSIT_5_PERCENT;
import static de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants.PLUGIN_ITEM_NAMESPACE_BANK_DEPOSIT_ALL;
import static de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants.PLUGIN_ITEM_NAMESPACE_BANK_UPGRADE;
import static de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants.PLUGIN_ITEM_NAMESPACE_BANK_WITHDRAW;
import static de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants.PLUGIN_ITEM_NAMESPACE_BANK_WITHDRAW_20_PERCENT;
import static de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants.PLUGIN_ITEM_NAMESPACE_BANK_WITHDRAW_50_PERCENT;
import static de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants.PLUGIN_ITEM_NAMESPACE_BANK_WITHDRAW_5_PERCENT;
import static de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants.PLUGIN_ITEM_NAMESPACE_BANK_WITHDRAW_ALL;
import static de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants.PLUGIN_ITEM_NAMESPACE_NPC_GUI_CLOSE;
import static de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants.PLUGIN_ITEM_NAMESPACE_NPC_GUI_DISABLED;

import de.relluem94.minecraft.server.spigot.essentials.annotations.ListenerName;
import de.relluem94.minecraft.server.spigot.essentials.constants.Constants;
import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.helpers.InventoryHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.StringHelper;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.ListenerConstruct;
import de.relluem94.minecraft.server.spigot.essentials.models.RelluEssentialsNamespacedKey;
import de.relluem94.minecraft.server.spigot.essentials.models.items.CustomItem;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.BankAccountEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.BankTransactionEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.PlayerEntry;
import de.relluem94.minecraft.server.spigot.essentials.services.BankService;
import de.relluem94.minecraft.server.spigot.essentials.services.PlayerService;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;
import lombok.NonNull;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Listener that handles inventory click events specifically for NPC-related GUIs.
 */
@ListenerName("InventoryClickNpc")
public class InventoryClickNpc implements ListenerConstruct {

  private NpcTradeHandler tradeHandler;
  private ServiceContext serviceContext;
  private Map<
      RelluEssentialsNamespacedKey,
      BiConsumer<Player, BankAccountEntry>> bankerDepositActions;

  private Map<
      RelluEssentialsNamespacedKey,
      BiConsumer<Player, BankAccountEntry>> initBankerDepositActions() {
    String ns = serviceContext.getPluginMetadataService().getName();
    BankService bs = serviceContext.getBankService();
    PlayerService ps = serviceContext.getPlayerService();

    return Map.of(
        new RelluEssentialsNamespacedKey(ns, PLUGIN_ITEM_NAMESPACE_BANK_DEPOSIT_5_PERCENT),
        (p, bae) -> bs.deposit(ps.getPlayerEntry(p), p, bae, 5f),
        new RelluEssentialsNamespacedKey(ns, PLUGIN_ITEM_NAMESPACE_BANK_DEPOSIT_20_PERCENT),
        (p, bae) -> bs.deposit(ps.getPlayerEntry(p), p, bae, 20f),
        new RelluEssentialsNamespacedKey(ns, PLUGIN_ITEM_NAMESPACE_BANK_DEPOSIT_50_PERCENT),
        (p, bae) -> bs.deposit(ps.getPlayerEntry(p), p, bae, 50f),
        new RelluEssentialsNamespacedKey(ns, PLUGIN_ITEM_NAMESPACE_BANK_DEPOSIT_ALL),
        (p, bae) -> bs.deposit(ps.getPlayerEntry(p), p, bae, 100f),
        new RelluEssentialsNamespacedKey(ns, PLUGIN_ITEM_NAMESPACE_BANK_WITHDRAW_5_PERCENT),
        (p, bae) -> bs.withdraw(ps.getPlayerEntry(p), p, bae, 5f),
        new RelluEssentialsNamespacedKey(ns, PLUGIN_ITEM_NAMESPACE_BANK_WITHDRAW_20_PERCENT),
        (p, bae) -> bs.withdraw(ps.getPlayerEntry(p), p, bae, 20f),
        new RelluEssentialsNamespacedKey(ns, PLUGIN_ITEM_NAMESPACE_BANK_WITHDRAW_50_PERCENT),
        (p, bae) -> bs.withdraw(ps.getPlayerEntry(p), p, bae, 50f),
        new RelluEssentialsNamespacedKey(ns, PLUGIN_ITEM_NAMESPACE_BANK_WITHDRAW_ALL),
        (p, bae) -> bs.withdraw(ps.getPlayerEntry(p), p, bae, 100f));
  }

  /**
   * Injects the service context into the listener and initializes required handlers.
   *
   * @param context the service context containing necessary services
   */
  @Override
  public void injectContext(ServiceContext context) {
    this.serviceContext = context;
    tradeHandler = new NpcTradeHandler(context);
    this.bankerDepositActions = initBankerDepositActions();
  }

  /**
   * Handles the {@link InventoryClickEvent} to route clicks to specific NPC GUI handlers.
   *
   * @param e the inventory click event
   */
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

    Optional<CustomItem> optionalCustomItemClose = serviceContext.getItemService().find(
        new RelluEssentialsNamespacedKey(serviceContext.getPluginMetadataService().getName(),
            PLUGIN_ITEM_NAMESPACE_NPC_GUI_CLOSE));

    if (clickedItem == null) {
      return;
    }

    if (bankAccount == null) {
      return;
    }

    if (optionalCustomItemClose.isEmpty()) {
      return;
    }

    CustomItem customItemClose = optionalCustomItemClose.get();

    CustomItem depositItem = serviceContext.getBankService()
        .getBankItem(PLUGIN_ITEM_NAMESPACE_BANK_DEPOSIT);
    CustomItem totalBalanceItem = serviceContext.getBankService()
        .getBankItem(PLUGIN_ITEM_NAMESPACE_BANK_BALANCE_TOTAL);
    CustomItem balanceItem = serviceContext.getBankService()
        .getBankItem(PLUGIN_ITEM_NAMESPACE_BANK_BALANCE);
    CustomItem withdrawItem = serviceContext.getBankService()
        .getBankItem(PLUGIN_ITEM_NAMESPACE_BANK_WITHDRAW);
    CustomItem transactionsItem = serviceContext.getBankService()
        .getBankItem(PLUGIN_ITEM_NAMESPACE_BANK_BALANCE_TRANSACTIONS);
    CustomItem upgradeItem = serviceContext.getBankService()
        .getBankItem(PLUGIN_ITEM_NAMESPACE_BANK_UPGRADE);

    if (depositItem != null && depositItem.toItemStack().getType().equals(clickedItem.getType())) {
      InventoryHelper.closeInventory(player);
      InventoryHelper.openInventory(player, serviceContext.getTraderNpcService().getBankerNpc()
          .getDepositGUI(playerEntry.getPurse()));
    } else if (totalBalanceItem != null && totalBalanceItem.toItemStack().getType()
        .equals(clickedItem.getType())) {
      InventoryHelper.closeInventory(player);
      player.sendMessage(serviceContext.getTranslationService()
          .getWithPrefix(MessageKey.PLUGIN_EVENT_NPC_BANKER_TOTAL,
              StringHelper.formatDouble(bankAccount.getValue()), PLUGIN_NAME_MONEY));
    } else if (balanceItem != null && balanceItem.toItemStack().getType()
        .equals(clickedItem.getType())) {
      InventoryHelper.closeInventory(player);
      InventoryHelper.openInventory(player,
          serviceContext.getTraderNpcService().getBankerNpc().getBalanceGUI());
    } else if (withdrawItem != null && withdrawItem.toItemStack().getType()
        .equals(clickedItem.getType())) {
      InventoryHelper.closeInventory(player);
      InventoryHelper.openInventory(player, serviceContext.getTraderNpcService().getBankerNpc()
          .getWithdrawGUI(bankAccount.getValue()));
    } else if (BankService.UPGRADE_MATERIAL.equals(clickedItem.getType())) {
      serviceContext.getBankService().upgradeAccount(clickedItem, player, playerEntry, bankAccount);
    } else if (transactionsItem != null && transactionsItem.toItemStack().getType()
        .equals(clickedItem.getType())) {
      handleTransactionHistory(player, bankAccount);
    } else if (upgradeItem != null && upgradeItem.toItemStack().getType()
        .equals(clickedItem.getType())) {
      InventoryHelper.closeInventory(player);
      InventoryHelper.openInventory(player,
          serviceContext.getTraderNpcService().getBankerNpc().getUpgradeGUI());
    } else if (customItemClose.toItemStack().isSimilar(clickedItem)) {
      InventoryHelper.closeInventory(player);
    } else {
      serviceContext.getItemService().getAll().values().stream().filter(ci -> ci.displayName()
              .equals(clickedItem.getItemMeta() != null
                  ? clickedItem.getItemMeta().getDisplayName() : ""))
          .findFirst().ifPresent(ci -> {
            BiConsumer<Player, BankAccountEntry> action = bankerDepositActions.get(
                ci.relluEssentialsNamespacedKey());
            if (action != null) {
              action.accept(player, bankAccount);
            }
          });
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