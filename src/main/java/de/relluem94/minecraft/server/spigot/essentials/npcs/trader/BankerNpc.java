package de.relluem94.minecraft.server.spigot.essentials.npcs.trader;

import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_NAME_MONEY;
import static de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants.PLUGIN_ITEM_NAMESPACE_BANK_BALANCE;
import static de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants.PLUGIN_ITEM_NAMESPACE_BANK_BALANCE_TOTAL;
import static de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants.PLUGIN_ITEM_NAMESPACE_BANK_BALANCE_TRANSACTIONS;
import static de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants.PLUGIN_ITEM_NAMESPACE_BANK_DEPOSIT;
import static de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants.PLUGIN_ITEM_NAMESPACE_BANK_UPGRADE;
import static de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants.PLUGIN_ITEM_NAMESPACE_BANK_WITHDRAW;
import static de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants.PLUGIN_ITEM_NAMESPACE_NPC_GUI_CLOSE;
import static de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants.PLUGIN_ITEM_NAMESPACE_NPC_GUI_DISABLED;

import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.helpers.InventoryHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.ItemHelper;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.npc.BankerGui;
import de.relluem94.minecraft.server.spigot.essentials.models.RelluEssentialsNamespacedKey;
import de.relluem94.minecraft.server.spigot.essentials.models.items.CustomItem;
import de.relluem94.minecraft.server.spigot.essentials.services.BankService;
import java.util.List;
import org.bukkit.entity.Villager.Profession;
import org.bukkit.inventory.Inventory;

/**
 * Represents a Banker NPC that provides access to banking-related GUIs.
 */
public class BankerNpc extends TraderNpc implements BankerGui {

  private final ServiceContext serviceContext;

  /**
   * Creates a new Banker NPC.
   *
   * @param serviceContext the service context used to access various plugin services
   */
  public BankerNpc(ServiceContext serviceContext) {
    super("§dBanker", Profession.NONE, Type.BANKER);
    this.serviceContext = serviceContext;
  }

  private CustomItem resolveDisabledItem() {
    return serviceContext.getItemService().find(
        new RelluEssentialsNamespacedKey(serviceContext.getPluginMetadataService().getName(),
            PLUGIN_ITEM_NAMESPACE_NPC_GUI_DISABLED)).orElseThrow();
  }

  private CustomItem resolveCloseItem() {
    return serviceContext.getItemService().find(
        new RelluEssentialsNamespacedKey(serviceContext.getPluginMetadataService().getName(),
            PLUGIN_ITEM_NAMESPACE_NPC_GUI_CLOSE)).orElseThrow();
  }

  @Override
  public Inventory getMainGUI() {
    Inventory inv = InventoryHelper.fillInventory(InventoryHelper.createInventory(27, getTitle()),
        resolveDisabledItem().toItemStack());

    CustomItem depositItem = serviceContext.getBankService()
        .getBankItem(PLUGIN_ITEM_NAMESPACE_BANK_DEPOSIT);
    if (depositItem != null) {
      inv.setItem(10, depositItem.toItemStack());
    }

    CustomItem withdrawItem = serviceContext.getBankService()
        .getBankItem(PLUGIN_ITEM_NAMESPACE_BANK_WITHDRAW);
    if (withdrawItem != null) {
      inv.setItem(12, withdrawItem.toItemStack());
    }

    CustomItem balanceItem = serviceContext.getBankService()
        .getBankItem(PLUGIN_ITEM_NAMESPACE_BANK_BALANCE);
    if (balanceItem != null) {
      inv.setItem(14, balanceItem.toItemStack());
    }

    CustomItem upgradeItem = serviceContext.getBankService()
        .getBankItem(PLUGIN_ITEM_NAMESPACE_BANK_UPGRADE);
    if (upgradeItem != null) {
      inv.setItem(16, upgradeItem.toItemStack());
    }

    inv.setItem(26, resolveCloseItem().toItemStack());

    return inv;
  }

  @Override
  public Inventory getDepositGUI(double total) {
    Inventory inv = InventoryHelper.fillInventory(InventoryHelper.createInventory(27, getTitle()),
        resolveDisabledItem().toItemStack());

    long amount5 = Math.round(total * 0.05);
    long amount20 = Math.round(total * 0.20);
    long amount50 = Math.round(total * 0.50);
    long amountAll = Math.round(total);

    inv.setItem(10, serviceContext.getBankService()
        .addLoreLine(BankService.npc_gui_deposit_5_percent.getCustomItem(),
            serviceContext.getTranslationService()
                .get(MessageKey.PLUGIN_EVENT_NPC_BANKER_DEPOSIT_LORE, amount5, PLUGIN_NAME_MONEY)));
    inv.setItem(12, serviceContext.getBankService()
        .addLoreLine(BankService.npc_gui_deposit_20_percent.getCustomItem(),
            serviceContext.getTranslationService()
                .get(MessageKey.PLUGIN_EVENT_NPC_BANKER_DEPOSIT_LORE, amount20,
                    PLUGIN_NAME_MONEY)));
    inv.setItem(14, serviceContext.getBankService()
        .addLoreLine(BankService.npc_gui_deposit_50_percent.getCustomItem(),
            serviceContext.getTranslationService()
                .get(MessageKey.PLUGIN_EVENT_NPC_BANKER_DEPOSIT_LORE, amount50,
                    PLUGIN_NAME_MONEY)));
    inv.setItem(16, serviceContext.getBankService()
        .addLoreLine(BankService.npc_gui_deposit_all.getCustomItem(),
            serviceContext.getTranslationService()
                .get(MessageKey.PLUGIN_EVENT_NPC_BANKER_DEPOSIT_LORE, amountAll,
                    PLUGIN_NAME_MONEY)));
    inv.setItem(26, resolveCloseItem().toItemStack());

    return inv;
  }

  @Override
  public Inventory getWithdrawGUI(double total) {
    Inventory inv = InventoryHelper.fillInventory(InventoryHelper.createInventory(27, getTitle()),
        resolveDisabledItem().toItemStack());

    long amount5 = Math.round(total * 0.05);
    long amount20 = Math.round(total * 0.20);
    long amount50 = Math.round(total * 0.50);
    long amountAll = Math.round(total);

    inv.setItem(10, serviceContext.getBankService()
        .addLoreLine(BankService.npc_gui_withdraw_5_percent.getCustomItem(),
            serviceContext.getTranslationService()
                .get(MessageKey.PLUGIN_EVENT_NPC_BANKER_WITHDRAW_LORE, amount5,
                    PLUGIN_NAME_MONEY)));
    inv.setItem(12, serviceContext.getBankService()
        .addLoreLine(BankService.npc_gui_withdraw_20_percent.getCustomItem(),
            serviceContext.getTranslationService()
                .get(MessageKey.PLUGIN_EVENT_NPC_BANKER_WITHDRAW_LORE, amount20,
                    PLUGIN_NAME_MONEY)));
    inv.setItem(14, serviceContext.getBankService()
        .addLoreLine(BankService.npc_gui_withdraw_50_percent.getCustomItem(),
            serviceContext.getTranslationService()
                .get(MessageKey.PLUGIN_EVENT_NPC_BANKER_WITHDRAW_LORE, amount50,
                    PLUGIN_NAME_MONEY)));
    inv.setItem(16, serviceContext.getBankService()
        .addLoreLine(BankService.npc_gui_withdraw_all.getCustomItem(),
            serviceContext.getTranslationService()
                .get(MessageKey.PLUGIN_EVENT_NPC_BANKER_WITHDRAW_LORE, amountAll,
                    PLUGIN_NAME_MONEY)));
    inv.setItem(26, resolveCloseItem().toItemStack());

    return inv;
  }

  @Override
  public Inventory getBalanceGUI() {
    Inventory inv = InventoryHelper.fillInventory(InventoryHelper.createInventory(27, getTitle()),
        resolveDisabledItem().toItemStack());

    CustomItem depositItem = serviceContext.getBankService()
        .getBankItem(PLUGIN_ITEM_NAMESPACE_BANK_DEPOSIT);
    if (depositItem != null) {
      inv.setItem(10, depositItem.toItemStack());
    }

    CustomItem withdrawItem = serviceContext.getBankService()
        .getBankItem(PLUGIN_ITEM_NAMESPACE_BANK_WITHDRAW);
    if (withdrawItem != null) {
      inv.setItem(12, withdrawItem.toItemStack());
    }

    CustomItem totalBalanceItem = serviceContext.getBankService()
        .getBankItem(PLUGIN_ITEM_NAMESPACE_BANK_BALANCE_TOTAL);
    if (totalBalanceItem != null) {
      inv.setItem(10, totalBalanceItem.toItemStack());
    }

    CustomItem transactionsItem = serviceContext.getBankService()
        .getBankItem(PLUGIN_ITEM_NAMESPACE_BANK_BALANCE_TRANSACTIONS);
    if (transactionsItem != null) {
      inv.setItem(12, transactionsItem.toItemStack());
    }

    inv.setItem(26, resolveCloseItem().toItemStack());

    return inv;
  }

  @Override
  public Inventory getUpgradeGUI() {
    Inventory inv = InventoryHelper.fillInventory(InventoryHelper.createInventory(27, getTitle()),
        resolveDisabledItem().toItemStack());

    int slot = 0;
    List<ItemHelper> bankTiersItems = serviceContext.getBankService().getBankTiers();
    for (int i = 0; i < bankTiersItems.size(); i++) {
      slot = InventoryHelper.getNextSlot(slot);
      inv.setItem(slot, bankTiersItems.get(i).getCustomItem());
      if (bankTiersItems.size() <= 3) {
        slot++; // for spacing
        slot++; // disables it self if enduser adds new banktier
      }

      slot++;
    }

    inv.setItem(26, resolveCloseItem().toItemStack());

    return inv;
  }
}