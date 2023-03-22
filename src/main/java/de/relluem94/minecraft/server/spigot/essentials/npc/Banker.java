package de.relluem94.minecraft.server.spigot.essentials.npc;

import java.util.List;

import org.bukkit.entity.Villager.Profession;
import org.bukkit.inventory.Inventory;

import de.relluem94.minecraft.server.spigot.essentials.CustomItems;
import de.relluem94.minecraft.server.spigot.essentials.helpers.BankerHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.InventoryHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.ItemHelper;
import de.relluem94.minecraft.server.spigot.essentials.npc.interfaces.IBanker;

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
    public Inventory getDepositGUI(){
        Inventory inv = InventoryHelper.fillInventory(InventoryHelper.createInventory(27, getTitle()), CustomItems.npc_gui_disabled.getCustomItem());

        inv.setItem(10, BankerHelper.npc_gui_deposit_5_percent.getCustomItem());
        inv.setItem(12, BankerHelper.npc_gui_deposit_20_percent.getCustomItem());
        inv.setItem(14, BankerHelper.npc_gui_deposit_50_percent.getCustomItem());
        inv.setItem(16, BankerHelper.npc_gui_deposit_all.getCustomItem());
        inv.setItem(26, CustomItems.npc_gui_close.getCustomItem());

        return inv;
    }

    @Override
    public Inventory getWithdrawGUI(){
        Inventory inv = InventoryHelper.fillInventory(InventoryHelper.createInventory(27, getTitle()), CustomItems.npc_gui_disabled.getCustomItem());
    
        inv.setItem(10, BankerHelper.npc_gui_withdraw_5_percent.getCustomItem());
        inv.setItem(12, BankerHelper.npc_gui_withdraw_20_percent.getCustomItem());
        inv.setItem(14, BankerHelper.npc_gui_withdraw_50_percent.getCustomItem());
        inv.setItem(16, BankerHelper.npc_gui_withdraw_all.getCustomItem());
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