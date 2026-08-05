package de.relluem94.minecraft.server.spigot.essentials.npc.trader;

import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_NAME_MONEY;
import static de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants.PLUGIN_ITEM_NAMESPACE_NPC_GUI_CLOSE;
import static de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants.PLUGIN_ITEM_NAMESPACE_NPC_GUI_DISABLED;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.context.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.helpers.BankerHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.InventoryHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.ItemHelper;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.npc.BankerGui;
import de.relluem94.minecraft.server.spigot.essentials.model.RegistryKey;
import de.relluem94.minecraft.server.spigot.essentials.registry.ItemRegistry;
import de.relluem94.minecraft.server.spigot.essentials.services.TranslationService;
import java.util.List;
import org.bukkit.entity.Villager.Profession;
import org.bukkit.inventory.Inventory;

public class BankerNpc extends TraderNpc implements BankerGui {

  private final TranslationService translationService;

  public BankerNpc(ServiceContext serviceContext) {
    super("§dBanker", Profession.NONE, Type.BANKER);
    translationService = serviceContext.getTranslationService();
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

  @Override
  public Inventory getMainGUI() {
    Inventory inv = InventoryHelper.fillInventory(
        InventoryHelper.createInventory(27, getTitle()), resolveDisabledItem().getCustomItem());

    inv.setItem(10, BankerHelper.npc_gui_deposit.getCustomItem());
    inv.setItem(12, BankerHelper.npc_gui_withdraw.getCustomItem());
    inv.setItem(14, BankerHelper.npc_gui_balance.getCustomItem());
    inv.setItem(16, BankerHelper.npc_gui_upgrade.getCustomItem());
    inv.setItem(26, resolveCloseItem().getCustomItem());

    return inv;
  }

  @Override
  public Inventory getDepositGUI(double total) {
    Inventory inv = InventoryHelper.fillInventory(
        InventoryHelper.createInventory(27, getTitle()), resolveDisabledItem().getCustomItem());

    long amount5 = Math.round(total * 0.05);
    long amount20 = Math.round(total * 0.20);
    long amount50 = Math.round(total * 0.50);
    long amountAll = Math.round(total);

    inv.setItem(10, BankerHelper.addLoreLine(BankerHelper.npc_gui_deposit_5_percent.getCustomItem(),
        translationService.get(MessageKey.PLUGIN_EVENT_NPC_BANKER_DEPOSIT_LORE, amount5,
            PLUGIN_NAME_MONEY)));
    inv.setItem(12,
        BankerHelper.addLoreLine(BankerHelper.npc_gui_deposit_20_percent.getCustomItem(),
            translationService.get(MessageKey.PLUGIN_EVENT_NPC_BANKER_DEPOSIT_LORE, amount20,
                PLUGIN_NAME_MONEY)));
    inv.setItem(14,
        BankerHelper.addLoreLine(BankerHelper.npc_gui_deposit_50_percent.getCustomItem(),
            translationService.get(MessageKey.PLUGIN_EVENT_NPC_BANKER_DEPOSIT_LORE, amount50,
                PLUGIN_NAME_MONEY)));
    inv.setItem(16, BankerHelper.addLoreLine(BankerHelper.npc_gui_deposit_all.getCustomItem(),
        translationService.get(MessageKey.PLUGIN_EVENT_NPC_BANKER_DEPOSIT_LORE, amountAll,
            PLUGIN_NAME_MONEY)));
    inv.setItem(26, resolveCloseItem().getCustomItem());

    return inv;
  }

  @Override
  public Inventory getWithdrawGUI(double total) {
    Inventory inv = InventoryHelper.fillInventory(
        InventoryHelper.createInventory(27, getTitle()), resolveDisabledItem().getCustomItem());

    long amount5 = Math.round(total * 0.05);
    long amount20 = Math.round(total * 0.20);
    long amount50 = Math.round(total * 0.50);
    long amountAll = Math.round(total);

    inv.setItem(10,
        BankerHelper.addLoreLine(BankerHelper.npc_gui_withdraw_5_percent.getCustomItem(),
            translationService.get(MessageKey.PLUGIN_EVENT_NPC_BANKER_WITHDRAW_LORE, amount5,
                PLUGIN_NAME_MONEY)));
    inv.setItem(12,
        BankerHelper.addLoreLine(BankerHelper.npc_gui_withdraw_20_percent.getCustomItem(),
            translationService.get(MessageKey.PLUGIN_EVENT_NPC_BANKER_WITHDRAW_LORE, amount20,
                PLUGIN_NAME_MONEY)));
    inv.setItem(14,
        BankerHelper.addLoreLine(BankerHelper.npc_gui_withdraw_50_percent.getCustomItem(),
            translationService.get(MessageKey.PLUGIN_EVENT_NPC_BANKER_WITHDRAW_LORE, amount50,
                PLUGIN_NAME_MONEY)));
    inv.setItem(16, BankerHelper.addLoreLine(BankerHelper.npc_gui_withdraw_all.getCustomItem(),
        translationService.get(MessageKey.PLUGIN_EVENT_NPC_BANKER_WITHDRAW_LORE, amountAll,
            PLUGIN_NAME_MONEY)));
    inv.setItem(26, resolveCloseItem().getCustomItem());

    return inv;
  }

  @Override
  public Inventory getBalanceGUI() {
    Inventory inv = InventoryHelper.fillInventory(
        InventoryHelper.createInventory(27, getTitle()), resolveDisabledItem().getCustomItem());

    inv.setItem(10, BankerHelper.npc_gui_balance_total.getCustomItem());
    inv.setItem(12, BankerHelper.npc_gui_balance_transactions.getCustomItem());
    inv.setItem(26, resolveCloseItem().getCustomItem());

    return inv;
  }

  @Override
  public Inventory getUpgradeGUI() {
    Inventory inv = InventoryHelper.fillInventory(
        InventoryHelper.createInventory(27, getTitle()), resolveDisabledItem().getCustomItem());

    int slot = 0;
    List<ItemHelper> bankTiersItems = BankerHelper.getBankTiers();
    for (int i = 0; i < bankTiersItems.size(); i++) {
      slot = InventoryHelper.getNextSlot(slot);
      inv.setItem(slot, bankTiersItems.get(i).getCustomItem());
      if (bankTiersItems.size() <= 3) {
        slot++; // for spacing
        slot++; // disables it self if enduser adds new banktier
      }

      slot++;
    }

    inv.setItem(26, resolveCloseItem().getCustomItem());

    return inv;
  }
}