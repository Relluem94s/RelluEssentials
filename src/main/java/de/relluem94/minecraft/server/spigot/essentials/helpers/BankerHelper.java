package de.relluem94.minecraft.server.spigot.essentials.helpers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.constants.EventConstants;
import de.relluem94.minecraft.server.spigot.essentials.helpers.ItemHelper.Rarity;
import de.relluem94.minecraft.server.spigot.essentials.helpers.ItemHelper.Type;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.BankAccountEntry;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.BankTierEntry;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.PlayerEntry;

import static de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants.*;

public class BankerHelper {

 
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



    public static Material UPGRADE_MATERIAL = Material.AMETHYST_SHARD;

    public static List<ItemHelper> getBankTiers(){
        List<ItemHelper> lih = new ArrayList<>();
        for(int i = 0; i < RelluEssentials.bankTiersList.size(); i++){
            BankTierEntry bte = RelluEssentials.bankTiersList.get(i);
            lih.add(new ItemHelper(new ItemStack(UPGRADE_MATERIAL, 1), bte.getName(), Type.NPC_GUI, Rarity.NONE, Arrays.asList(new String[]{"Costs: " + bte.getCost(), "Interest: " + bte.getInterest(), "Limit: " + bte.getLimit()})));
        }
        return lih;
    }



    public static void deposit(PlayerEntry pe, Player p, BankAccountEntry bae, float percentage){
        double purse = pe.getPurse();
        if(purse >= 1){
            double transaction_value = (double)((double)purse / (double)100)  * (double)percentage;

            if(percentage == 100){
                transaction_value = purse;
                pe.setPurse(0);
            }
            else{
                pe.setPurse((double)purse - (double)transaction_value);
            }

            RelluEssentials.dBH.addTransactionToBank(pe.getID(), bae.getId(), transaction_value, bae.getValue(), bae.getTier().getId());
            
            RelluEssentials.dBH.updatePlayer(pe);

            p.playSound(p, Sound.ITEM_ARMOR_EQUIP_GOLD, SoundCategory.MASTER, 1f, 1f);
            p.sendMessage(String.format(EventConstants.PLUGIN_EVENT_NPC_BANKER_DEPOIST_MESSAGE, StringHelper.formatDouble(transaction_value)));

            InventoryHelper.closeInventory(p);
        }
        else{
            p.sendMessage(EventConstants.PLUGIN_EVENT_NPC_BANKER_DEPOIST_NO_COINS_MESSAGE);

            p.playSound(p, Sound.ENTITY_VILLAGER_NO, SoundCategory.MASTER, 1f, 1f);
            InventoryHelper.closeInventory(p);
        }
    }

    public static void withdraw(PlayerEntry pe, Player p, BankAccountEntry bae, float percentage){
        double bank = bae.getValue();
        double purse = pe.getPurse();
        if(bank >= 1){
            double transaction_value = ((bank / 100)  * percentage);

            if(percentage == 100){
                transaction_value = bank;
            }

            pe.setPurse((double)purse + (double)transaction_value);
            RelluEssentials.dBH.addTransactionToBank(pe.getID(), bae.getId(), transaction_value*-1, bae.getValue(), bae.getTier().getId());
            
            RelluEssentials.dBH.updatePlayer(pe);

            p.playSound(p, Sound.ITEM_ARMOR_EQUIP_GOLD, SoundCategory.MASTER, 1f, 1f);
            p.sendMessage(String.format(EventConstants.PLUGIN_EVENT_NPC_BANKER_WITHDRAW_MESSAGE, StringHelper.formatDouble(transaction_value)));
            InventoryHelper.closeInventory(p);
        }
        else{
            p.sendMessage(EventConstants.PLUGIN_EVENT_NPC_BANKER_NOT_ENOUGH_COINS);

            p.playSound(p, Sound.ENTITY_VILLAGER_NO, SoundCategory.MASTER, 1f, 1f);
            InventoryHelper.closeInventory(p);
        }
    }

    public static void upgradeAccount(ItemStack itemStack, Player p, PlayerEntry pe, BankAccountEntry bae){
       


        for(ItemHelper ih : getBankTiers()){
            if(ih.getCustomItem().equals(itemStack)){
                Long costs = Long.parseLong(ih.getLore().get(0).replace("Costs: ", ""));
                BankTierEntry bt = bae.getTier();
                for(BankTierEntry bte: RelluEssentials.bankTiersList){
                    if(bte.getCost() == costs){
                        bt = bte;
                        break;
                    }
                }

                if(bae.getTier().getCost() > costs){
                    p.sendMessage(EventConstants.PLUGIN_EVENT_NPC_BANKER_LOWER_ACCOUNT);
                }
                else if(bae.getTier().getCost() == costs){
                    p.sendMessage(EventConstants.PLUGIN_EVENT_NPC_BANKER_ALREADY_BOUGHT);
                }
                else{
                    if(bt.getId() != bae.getTier().getId()){
                        double purse = pe.getPurse();
                        if(purse >= costs){
                            pe.setPurse(purse - costs);
                            RelluEssentials.dBH.updatePlayer(pe);
                            RelluEssentials.dBH.updateBankAccount(pe.getID(), 0f, bae.getValue(), bt.getId());
                            p.sendMessage(EventConstants.PLUGIN_EVENT_NPC_BANKER_BUY_USING_PURSE);
                            p.closeInventory();
                        }
                        else{
                            double account = bae.getValue();
                            if(account >= costs){
                                RelluEssentials.dBH.addTransactionToBank(pe.getID(), bae.getId(), (double)-costs, bae.getValue(), bt.getId());
                                p.sendMessage(EventConstants.PLUGIN_EVENT_NPC_BANKER_BUY_USING_BANK);
                                p.closeInventory();
                            }
                            else{
                                if(purse + account >= costs){
                                    pe.setPurse(0);
                                    RelluEssentials.dBH.updatePlayer(pe);
                                    RelluEssentials.dBH.addTransactionToBank(pe.getID(), bae.getId(), (double)-(costs-purse), bae.getValue(), bt.getId());
                                    p.sendMessage(EventConstants.PLUGIN_EVENT_NPC_BANKER_BUY_USING_BOTH);
                                    p.closeInventory();
                                }
                                else{
                                    p.sendMessage(EventConstants.PLUGIN_EVENT_NPC_BANKER_NOT_ENOUGH_COINS);
                                }
                            }
                        }
                    }
                }
            }
        }


        
    }
}