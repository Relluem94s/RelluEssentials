package de.relluem94.minecraft.server.spigot.essentials.listeners.npc;

import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_COLOR_MONEY;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_EVENT_NPC_BANKER_TRANSACTION_NEGATIVE;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_EVENT_NPC_BANKER_TRANSACTION_POSITIVE;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_NAME_MONEY;
import static de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants.PLUGIN_ITEM_NAMESPACE_NPC_GUI_CLOSE;
import static de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants.PLUGIN_ITEM_NAMESPACE_NPC_GUI_DISABLED;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.TeleportHelper.teleportWorld;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.constants.Constants;
import de.relluem94.minecraft.server.spigot.essentials.context.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.helpers.BankerHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.InventoryHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.ItemHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.StringHelper;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.ListenerConstruct;
import de.relluem94.minecraft.server.spigot.essentials.model.RegistryKey;
import de.relluem94.minecraft.server.spigot.essentials.model.pojo.BankAccountEntry;
import de.relluem94.minecraft.server.spigot.essentials.model.pojo.BankTransactionEntry;
import de.relluem94.minecraft.server.spigot.essentials.model.pojo.PlayerEntry;
import de.relluem94.minecraft.server.spigot.essentials.npc.trader.BankerNpc;
import de.relluem94.minecraft.server.spigot.essentials.registry.ItemRegistry;
import de.relluem94.minecraft.server.spigot.essentials.services.TranslationService;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import lombok.NonNull;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class InventoryClickNpc implements ListenerConstruct {


  private final Map<ItemHelper, BiConsumer<Player, BankAccountEntry>> bankerDepositActions = Map.of(
      BankerHelper.npc_gui_deposit_5_percent, (p, bae) -> BankerHelper.deposit(
          RelluEssentials.getInstance().getPlayerRegistry().getPlayerEntry(p), p, bae, 5f),
      BankerHelper.npc_gui_deposit_20_percent, (p, bae) -> BankerHelper.deposit(
          RelluEssentials.getInstance().getPlayerRegistry().getPlayerEntry(p), p, bae, 20f),
      BankerHelper.npc_gui_deposit_50_percent, (p, bae) -> BankerHelper.deposit(
          RelluEssentials.getInstance().getPlayerRegistry().getPlayerEntry(p), p, bae, 50f),
      BankerHelper.npc_gui_deposit_all, (p, bae) -> BankerHelper.deposit(
          RelluEssentials.getInstance().getPlayerRegistry().getPlayerEntry(p), p, bae, 100f),
      BankerHelper.npc_gui_withdraw_5_percent, (p, bae) -> BankerHelper.withdraw(
          RelluEssentials.getInstance().getPlayerRegistry().getPlayerEntry(p), p, bae, 5f),
      BankerHelper.npc_gui_withdraw_20_percent, (p, bae) -> BankerHelper.withdraw(
          RelluEssentials.getInstance().getPlayerRegistry().getPlayerEntry(p), p, bae, 20f),
      BankerHelper.npc_gui_withdraw_50_percent, (p, bae) -> BankerHelper.withdraw(
          RelluEssentials.getInstance().getPlayerRegistry().getPlayerEntry(p), p, bae, 50f),
      BankerHelper.npc_gui_withdraw_all, (p, bae) -> BankerHelper.withdraw(
          RelluEssentials.getInstance().getPlayerRegistry().getPlayerEntry(p), p, bae, 100f)
  );
  TranslationService translationService;
  BankerNpc bankerNpc;
  private NpcTradeHandler tradeHandler;

  @Override
  public void injectContext(ServiceContext context) {
    translationService = context.getTranslationService();
    tradeHandler = new NpcTradeHandler(translationService);
    bankerNpc = context.getBankerNpc();
  }

  private ItemHelper resolveDisabledItem() {
    return ItemRegistry.find(
            RegistryKey.of(RelluEssentials.getInstance(), PLUGIN_ITEM_NAMESPACE_NPC_GUI_DISABLED))
        .orElseThrow();
  }

  private ItemHelper resolveCloseItem() {
    return ItemRegistry.find(
            RegistryKey.of(RelluEssentials.getInstance(), PLUGIN_ITEM_NAMESPACE_NPC_GUI_CLOSE))
        .orElseThrow();
  }

  @EventHandler
  public void onInventoryClickItem(@NonNull InventoryClickEvent e) {
    if (!(e.getWhoClicked() instanceof Player player) || e.getCurrentItem() == null) {
      return;
    }

    PlayerEntry playerEntry = RelluEssentials.getInstance().getPlayerRegistry()
        .getPlayerEntry(player);
    String title = e.getView().getTitle();

    if (title.equals(bankerNpc.getTitle())) {
      handleBankerInventory(e, player, playerEntry);
    } else if (RelluEssentials.getInstance().getTraderNpcRegistry().getNPCTraderTitleList()
        .contains(title)) {
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
    BankAccountEntry bankAccount = RelluEssentials.getInstance().getDatabaseHelper()
        .getPlayerBankAccount(playerEntry.getId());

    if (clickedItem == null) {
      return;
    }

    if (BankerHelper.npc_gui_deposit.equalsName(clickedItem)) {
      InventoryHelper.closeInventory(player);
      InventoryHelper.openInventory(player,
          bankerNpc.getDepositGUI(playerEntry.getPurse()));
    } else if (BankerHelper.npc_gui_balance_total.equalsName(clickedItem)) {
      InventoryHelper.closeInventory(player);
      player.sendMessage(translationService.getWithPrefix(MessageKey.PLUGIN_EVENT_NPC_BANKER_TOTAL,
          StringHelper.formatDouble(bankAccount.getValue()), PLUGIN_NAME_MONEY));
    } else if (BankerHelper.npc_gui_balance.equalsName(clickedItem)) {
      InventoryHelper.closeInventory(player);
      InventoryHelper.openInventory(player, bankerNpc.getBalanceGUI());
    } else if (BankerHelper.npc_gui_withdraw.getCustomItem().getType()
        .equals(clickedItem.getType())) {
      InventoryHelper.closeInventory(player);
      InventoryHelper.openInventory(player,
          bankerNpc.getWithdrawGUI(bankAccount.getValue()));
    } else if (BankerHelper.UPGRADE_MATERIAL.equals(clickedItem.getType())) {
      BankerHelper.upgradeAccount(clickedItem, player, playerEntry, bankAccount);
    } else if (BankerHelper.npc_gui_balance_transactions.equalsExact(clickedItem)) {
      handleTransactionHistory(player, bankAccount);
    } else if (BankerHelper.npc_gui_upgrade.equalsExact(clickedItem)) {
      InventoryHelper.closeInventory(player);
      InventoryHelper.openInventory(player, bankerNpc.getUpgradeGUI());
    } else if (resolveCloseItem().equalsExact(clickedItem)) {
      InventoryHelper.closeInventory(player);
    } else {
      bankerDepositActions.entrySet().stream()
          .filter(entry -> entry.getKey().equalsExact(clickedItem))
          .findFirst()
          .ifPresent(entry -> entry.getValue().accept(player, bankAccount));
    }
  }

  private void handleTransactionHistory(Player player, BankAccountEntry bankAccount) {
    InventoryHelper.closeInventory(player);
    player.sendMessage(
        translationService.getWithPrefix(MessageKey.PLUGIN_EVENT_NPC_BANKER_TRANSACTION));
    List<BankTransactionEntry> transactions = RelluEssentials.getInstance().getDatabaseHelper()
        .getTransactionsToBankFromPlayer(bankAccount.getId());
    transactions.forEach(transaction -> player.sendMessage(
        translationService.getWithPrefix(
            MessageKey.PLUGIN_EVENT_NPC_BANKER_TRANSACTION_LIST,
            transaction.getValue() > 1 ? PLUGIN_EVENT_NPC_BANKER_TRANSACTION_POSITIVE
                : PLUGIN_EVENT_NPC_BANKER_TRANSACTION_NEGATIVE,
            PLUGIN_COLOR_MONEY,
            StringHelper.formatDouble(transaction.getValue()),
            PLUGIN_NAME_MONEY,
            transaction.getCreated()
        )
    ));
  }

  private boolean isNpcOrCustomHeadsInventory(@NonNull String title) {
    return title.equals(
        Constants.PLUGIN_NAME_PREFIX + Constants.PLUGIN_FORMS_SPACER_MESSAGE + "§dNPCs")
        || title.equals(translationService.getWithPrefix(MessageKey.COMMAND_CUSTOMHEADS_TITLE));
  }

  private void handleNpcOrCustomHeadsInventory(InventoryClickEvent e, Player player) {
    e.setCancelled(true);
    if (e.getCurrentItem() == null) {
      return;
    }

    if (!resolveDisabledItem().getCustomItem().equals(e.getCurrentItem())) {
      player.getInventory().addItem(e.getCurrentItem().clone());
    }
  }

  private void handleWorldsInventory(InventoryClickEvent e, Player player) {
    e.setCancelled(true);
    if (e.getCurrentItem() == null) {
      return;
    }
    if (resolveDisabledItem().getCustomItem().equals(e.getCurrentItem())) {
      return;
    }
    if (e.getCurrentItem().getItemMeta() == null) {
      return;
    }
    teleportWorld(player, e.getCurrentItem().getItemMeta().getDisplayName());
  }
}