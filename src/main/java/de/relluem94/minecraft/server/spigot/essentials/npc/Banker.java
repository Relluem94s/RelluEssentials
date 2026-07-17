package de.relluem94.minecraft.server.spigot.essentials.npc;

import de.relluem94.minecraft.server.spigot.essentials.CustomItems;
import de.relluem94.minecraft.server.spigot.essentials.constants.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.helpers.BankerHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.InventoryHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.ItemHelper;
import de.relluem94.minecraft.server.spigot.essentials.npc.interfaces.IBanker;
import org.bukkit.entity.Villager.Profession;
import org.bukkit.inventory.Inventory;

import java.util.List;

import static de.relluem94.minecraft.server.spigot.essentials.RelluEssentials.languageHelper;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_NAME_MONEY;

public class Banker extends NPC implements IBanker {
   
    public Banker(){
        super("§dBanker", Profession.NONE, Type.BANKER);
    }

    @Override
    public Inventory getMainGUI(){
        Inventory inv = InventoryHelper.fillInventory(InventoryHelper.createInventory(27, getTitle()), CustomItems.npc_gui_disabled.getCustomItem());
    
        inv.setItem(10, BankerHelper.npc_gui_deposit.getCustomItem());
        inv.setItem(12, BankerHelper.npc_gui_withdraw.getCustomItem());
        inv.setItem(14, BankerHelper.npc_gui_balance.getCustomItem());
        inv.setItem(16, BankerHelper.npc_gui_upgrade.getCustomItem());
        inv.setItem(26, CustomItems.npc_gui_close.getCustomItem());

        return inv;
    }

    @Override
    public Inventory getDepositGUI(double total){
        Inventory inv = InventoryHelper.fillInventory(InventoryHelper.createInventory(27, getTitle()), CustomItems.npc_gui_disabled.getCustomItem());
        long amount5  = Math.round(total * 0.05);
        long amount20 = Math.round(total * 0.20);
        long amount50 = Math.round(total * 0.50);
        long amountAll = Math.round(total);

        inv.setItem(10, BankerHelper.addLoreLine(BankerHelper.npc_gui_deposit_5_percent.getCustomItem(),  languageHelper.get(MessageKey.PLUGIN_EVENT_NPC_BANKER_DEPOSIT_LORE, amount5,  PLUGIN_NAME_MONEY)));
        inv.setItem(12, BankerHelper.addLoreLine(BankerHelper.npc_gui_deposit_20_percent.getCustomItem(), languageHelper.get(MessageKey.PLUGIN_EVENT_NPC_BANKER_DEPOSIT_LORE, amount20, PLUGIN_NAME_MONEY)));
        inv.setItem(14, BankerHelper.addLoreLine(BankerHelper.npc_gui_deposit_50_percent.getCustomItem(), languageHelper.get(MessageKey.PLUGIN_EVENT_NPC_BANKER_DEPOSIT_LORE, amount50, PLUGIN_NAME_MONEY)));
        inv.setItem(16, BankerHelper.addLoreLine(BankerHelper.npc_gui_deposit_all.getCustomItem(),        languageHelper.get(MessageKey.PLUGIN_EVENT_NPC_BANKER_DEPOSIT_LORE, amountAll, PLUGIN_NAME_MONEY)));
        inv.setItem(26, CustomItems.npc_gui_close.getCustomItem());

        return inv;
    }

    @Override
    public Inventory getWithdrawGUI(double total){
        Inventory inv = InventoryHelper.fillInventory(InventoryHelper.createInventory(27, getTitle()), CustomItems.npc_gui_disabled.getCustomItem());

        long amount5  = Math.round(total * 0.05);
        long amount20 = Math.round(total * 0.20);
        long amount50 = Math.round(total * 0.50);
        long amountAll = Math.round(total);

        inv.setItem(10, BankerHelper.addLoreLine(BankerHelper.npc_gui_withdraw_5_percent.getCustomItem(),  languageHelper.get(MessageKey.PLUGIN_EVENT_NPC_BANKER_WITHDRAW_LORE, amount5,  PLUGIN_NAME_MONEY)));
        inv.setItem(12, BankerHelper.addLoreLine(BankerHelper.npc_gui_withdraw_20_percent.getCustomItem(), languageHelper.get(MessageKey.PLUGIN_EVENT_NPC_BANKER_WITHDRAW_LORE, amount20, PLUGIN_NAME_MONEY)));
        inv.setItem(14, BankerHelper.addLoreLine(BankerHelper.npc_gui_withdraw_50_percent.getCustomItem(), languageHelper.get(MessageKey.PLUGIN_EVENT_NPC_BANKER_WITHDRAW_LORE, amount50, PLUGIN_NAME_MONEY)));
        inv.setItem(16, BankerHelper.addLoreLine(BankerHelper.npc_gui_withdraw_all.getCustomItem(),        languageHelper.get(MessageKey.PLUGIN_EVENT_NPC_BANKER_WITHDRAW_LORE, amountAll, PLUGIN_NAME_MONEY)));
        inv.setItem(26, CustomItems.npc_gui_close.getCustomItem());

        return inv;
    }

    @Override
    public Inventory getBalanceGUI(){
        Inventory inv = InventoryHelper.fillInventory(InventoryHelper.createInventory(27, getTitle()), CustomItems.npc_gui_disabled.getCustomItem());

        inv.setItem(10, BankerHelper.npc_gui_balance_total.getCustomItem());
        inv.setItem(12, BankerHelper.npc_gui_balance_transactions.getCustomItem());
        inv.setItem(26, CustomItems.npc_gui_close.getCustomItem());

        return inv;
    }

    @Override
    public Inventory getUpgradeGUI(){
        Inventory inv = InventoryHelper.fillInventory(InventoryHelper.createInventory(27, getTitle()), CustomItems.npc_gui_disabled.getCustomItem());

        int slot = 0;
        List<ItemHelper> bankTiersItems = BankerHelper.getBankTiers();
        for(int i = 0; i < bankTiersItems.size(); i++){
            slot = InventoryHelper.getNextSlot(slot);           
            inv.setItem(slot,bankTiersItems.get(i).getCustomItem());
            if(bankTiersItems.size() <=3){
                slot++; // for spacing
                slot++; // disables it self if enduser adds new banktier
            }
            
            slot++;
        }

        inv.setItem(26, CustomItems.npc_gui_close.getCustomItem());

        return inv;
    }
}