package de.relluem94.minecraft.server.spigot.essentials.NPC;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import de.relluem94.minecraft.server.spigot.essentials.CustomItems;
import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.Strings;
import de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants;
import de.relluem94.minecraft.server.spigot.essentials.helpers.InventoryHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.ItemHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.ItemHelper.Rarity;
import de.relluem94.minecraft.server.spigot.essentials.helpers.ItemHelper.Type;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.BankAccountEntry;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.PlayerEntry;

import static de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants.*;

import java.util.Arrays;

public class Banker {
    
    public static final String MAIN_GUI = Strings.PLUGIN_PREFIX + Strings.PLUGIN_SPACER + ItemConstants.PLUGIN_ITEM_NPC_BANKER;

    public static ItemHelper npc = new ItemHelper(Material.VILLAGER_SPAWN_EGG, 1, PLUGIN_ITEM_NPC_BANKER, Type.NPC, Rarity.LEGENDARY, Arrays.asList(new String[]{PLUGIN_ITEM_NPC_LORE1}));
    public static ItemHelper npc_portable_bank = new ItemHelper(Material.YELLOW_SHULKER_BOX, 1, PLUGIN_ITEM_NPC_BANKER_PORTABLE_BANK, Type.TOOL, Rarity.LEGENDARY, Arrays.asList(new String[]{PLUGIN_ITEM_NPC_BANKER_PORTABLE_BANK_LORE1}));


    public static ItemHelper npc_gui_deposit = new ItemHelper(Material.GREEN_SHULKER_BOX, 1, PLUGIN_ITEM_NPC_BANKER_GUI_DEPOSIT, Type.NPC_GUI, Rarity.NONE, Arrays.asList(new String[]{PLUGIN_ITEM_NPC_BANKER_GUI_DEPOSIT_LORE1}));
    public static ItemHelper npc_gui_withdraw = new ItemHelper(Material.RED_SHULKER_BOX, 1, PLUGIN_ITEM_NPC_BANKER_GUI_WITHDRAW, Type.NPC_GUI, Rarity.NONE, Arrays.asList(new String[]{PLUGIN_ITEM_NPC_BANKER_GUI_WITHDRAW_LORE1}));
    public static ItemHelper npc_gui_balance = new ItemHelper(Material.YELLOW_SHULKER_BOX, 1, PLUGIN_ITEM_NPC_BANKER_GUI_BALANCE, Type.NPC_GUI, Rarity.NONE, Arrays.asList(new String[]{PLUGIN_ITEM_NPC_BANKER_GUI_BALANCE_LORE1}));
    public static ItemHelper npc_gui_upgrade = new ItemHelper(Material.DIAMOND_BLOCK, 1, PLUGIN_ITEM_NPC_BANKER_GUI_UPGRADE, Type.NPC_GUI, Rarity.NONE, Arrays.asList(new String[]{PLUGIN_ITEM_NPC_BANKER_GUI_UPGRADE_LORE1}));

    public static ItemHelper npc_gui_deposit_all = new ItemHelper(Material.GOLD_BLOCK, 1, PLUGIN_ITEM_NPC_BANKER_GUI_DEPOSIT_ALL, Type.NPC_GUI, Rarity.NONE, Arrays.asList(new String[]{PLUGIN_ITEM_NPC_BANKER_GUI_DEPOSIT_AMOUNT_LORE1}));
    public static ItemHelper npc_gui_deposit_5_percent = new ItemHelper(Material.GOLD_NUGGET, 1, String.format(PLUGIN_ITEM_NPC_BANKER_GUI_DEPOSIT_X_PERCENT, 5), Type.NPC_GUI, Rarity.NONE, Arrays.asList(new String[]{PLUGIN_ITEM_NPC_BANKER_GUI_DEPOSIT_AMOUNT_LORE1}));
    public static ItemHelper npc_gui_deposit_20_percent = new ItemHelper(Material.GOLD_INGOT, 1, String.format(PLUGIN_ITEM_NPC_BANKER_GUI_DEPOSIT_X_PERCENT, 20), Type.NPC_GUI, Rarity.NONE, Arrays.asList(new String[]{PLUGIN_ITEM_NPC_BANKER_GUI_DEPOSIT_AMOUNT_LORE1}));
    public static ItemHelper npc_gui_deposit_50_percent = new ItemHelper(Material.GOLD_INGOT, 1, String.format(PLUGIN_ITEM_NPC_BANKER_GUI_DEPOSIT_X_PERCENT, 50), Type.NPC_GUI, Rarity.NONE, Arrays.asList(new String[]{PLUGIN_ITEM_NPC_BANKER_GUI_DEPOSIT_AMOUNT_LORE1}));

    public static ItemHelper npc_gui_withdraw_all = new ItemHelper(Material.GOLD_BLOCK, 1, PLUGIN_ITEM_NPC_BANKER_GUI_WITHDRAW_ALL, Type.NPC_GUI, Rarity.NONE, Arrays.asList(new String[]{PLUGIN_ITEM_NPC_BANKER_GUI_WITHDRAW_AMOUNT_LORE1}));
    public static ItemHelper npc_gui_withdraw_5_percent = new ItemHelper(Material.GOLD_NUGGET, 1, String.format(PLUGIN_ITEM_NPC_BANKER_GUI_WITHDRAW_X_PERCENT, 5), Type.NPC_GUI, Rarity.NONE, Arrays.asList(new String[]{PLUGIN_ITEM_NPC_BANKER_GUI_WITHDRAW_AMOUNT_LORE1}));
    public static ItemHelper npc_gui_withdraw_20_percent = new ItemHelper(Material.GOLD_INGOT, 1, String.format(PLUGIN_ITEM_NPC_BANKER_GUI_WITHDRAW_X_PERCENT, 20), Type.NPC_GUI, Rarity.NONE, Arrays.asList(new String[]{PLUGIN_ITEM_NPC_BANKER_GUI_WITHDRAW_AMOUNT_LORE1}));
    public static ItemHelper npc_gui_withdraw_50_percent = new ItemHelper(Material.GOLD_INGOT, 1, String.format(PLUGIN_ITEM_NPC_BANKER_GUI_WITHDRAW_X_PERCENT, 50), Type.NPC_GUI, Rarity.NONE, Arrays.asList(new String[]{PLUGIN_ITEM_NPC_BANKER_GUI_WITHDRAW_AMOUNT_LORE1}));

    public static ItemHelper npc_gui_balance_total = new ItemHelper(Material.YELLOW_SHULKER_BOX, 1, PLUGIN_ITEM_NPC_BANKER_GUI_BALANCE_TOTAL, Type.NPC_GUI, Rarity.NONE, Arrays.asList(new String[]{}));
    public static ItemHelper npc_gui_balance_transactions = new ItemHelper(Material.MAP, 1, PLUGIN_ITEM_NPC_BANKER_GUI_BALANCE_TRANSACTIONS, Type.NPC_GUI, Rarity.NONE, Arrays.asList(new String[]{}));


    public static Inventory getMainGUI(){
        Inventory inv = InventoryHelper.fillInventory(InventoryHelper.createInventory(27, MAIN_GUI), CustomItems.npc_gui_disabled.getCustomItem());
    
        inv.setItem(10, npc_gui_deposit.getCustomItem());
        inv.setItem(12, npc_gui_withdraw.getCustomItem());
        inv.setItem(14, npc_gui_balance.getCustomItem());
        inv.setItem(16, npc_gui_upgrade.getCustomItem());
        inv.setItem(26, CustomItems.npc_gui_close.getCustomItem());

        return inv;
    }

    public static Inventory getDepositGUI(){
        Inventory inv = InventoryHelper.fillInventory(InventoryHelper.createInventory(27, MAIN_GUI), CustomItems.npc_gui_disabled.getCustomItem());

        inv.setItem(10, npc_gui_deposit_5_percent.getCustomItem());
        inv.setItem(12, npc_gui_deposit_20_percent.getCustomItem());
        inv.setItem(14, npc_gui_deposit_50_percent.getCustomItem());
        inv.setItem(16, npc_gui_deposit_all.getCustomItem());
        inv.setItem(26, CustomItems.npc_gui_close.getCustomItem());

        return inv;
    }

    public static Inventory getWithdrawGUI(){
        Inventory inv = InventoryHelper.fillInventory(InventoryHelper.createInventory(27, MAIN_GUI), CustomItems.npc_gui_disabled.getCustomItem());
    
        inv.setItem(10, npc_gui_withdraw_5_percent.getCustomItem());
        inv.setItem(12, npc_gui_withdraw_20_percent.getCustomItem());
        inv.setItem(14, npc_gui_withdraw_50_percent.getCustomItem());
        inv.setItem(16, npc_gui_withdraw_all.getCustomItem());
        inv.setItem(26, CustomItems.npc_gui_close.getCustomItem());

        return inv;
    }

    public static Inventory getBalanceGUI(){
        Inventory inv = InventoryHelper.fillInventory(InventoryHelper.createInventory(27, MAIN_GUI), CustomItems.npc_gui_disabled.getCustomItem());

        inv.setItem(10, npc_gui_balance_total.getCustomItem());
        inv.setItem(12, npc_gui_balance_transactions.getCustomItem());
        inv.setItem(26, CustomItems.npc_gui_close.getCustomItem());

        return inv;
    }

    public static Inventory getUpgradeGUI(){
        Inventory inv = InventoryHelper.fillInventory(InventoryHelper.createInventory(27, MAIN_GUI), CustomItems.npc_gui_disabled.getCustomItem());
    
        inv.setItem(10, npc_gui_deposit.getCustomItem());
        inv.setItem(12, npc_gui_withdraw.getCustomItem());
        inv.setItem(14, npc_gui_balance.getCustomItem());
        inv.setItem(16, npc_gui_upgrade.getCustomItem());
        inv.setItem(26, CustomItems.npc_gui_close.getCustomItem());

        return inv;
    }


    public static void deposit(PlayerEntry pe, Player p, BankAccountEntry bae, float percentage){
        float purse = pe.getPurse();
        if(purse >= 1){
            float transaction_value = (purse / 100)  * percentage;
            RelluEssentials.dBH.addTransactionToBank(pe.getID(), bae.getId(), transaction_value, bae.getValue(), bae.getTier().getId());
            pe.setPurse(purse - transaction_value);
            RelluEssentials.dBH.updatePlayer(pe);

            p.playSound(p, Sound.ITEM_ARMOR_EQUIP_GOLD, SoundCategory.MASTER, 1f, 1f);
            p.sendMessage(String.format(Strings.PLUGIN_COMMAND_NPC_BANKER_DEPOIST_MESSAGE, transaction_value));

            InventoryHelper.closeInventory(p);
        }
        else{
            p.sendMessage(Strings.PLUGIN_COMMAND_NPC_BANKER_DEPOIST_NO_MONEY_MESSAGE);

            p.playSound(p, Sound.ENTITY_VILLAGER_NO, SoundCategory.MASTER, 1f, 1f);
            InventoryHelper.closeInventory(p);
        }
    }

    public static void withdraw(PlayerEntry pe, Player p, BankAccountEntry bae, float percentage){
        float bank = bae.getValue();
        float purse = pe.getPurse();
        if(bank >= 1){
            float transaction_value = ((bank / 100)  * percentage);
            RelluEssentials.dBH.addTransactionToBank(pe.getID(), bae.getId(), transaction_value*-1, bae.getValue(), bae.getTier().getId());
            pe.setPurse(purse + transaction_value);
            RelluEssentials.dBH.updatePlayer(pe);

            p.playSound(p, Sound.ITEM_ARMOR_EQUIP_GOLD, SoundCategory.MASTER, 1f, 1f);
            p.sendMessage(String.format(Strings.PLUGIN_COMMAND_NPC_BANKER_WITHDRAW_MESSAGE, transaction_value));
            InventoryHelper.closeInventory(p);
        }
        else{
            p.sendMessage(Strings.PLUGIN_COMMAND_NPC_BANKER_WITHDRAW_NO_MONEY_MESSAGE);

            p.playSound(p, Sound.ENTITY_VILLAGER_NO, SoundCategory.MASTER, 1f, 1f);
            InventoryHelper.closeInventory(p);
        }
    }

}
