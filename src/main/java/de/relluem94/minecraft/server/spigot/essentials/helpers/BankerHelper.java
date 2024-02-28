package de.relluem94.minecraft.server.spigot.essentials.helpers;

import static de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants.PLUGIN_ITEM_NPC_BANKER_GUI_BALANCE;
import static de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants.PLUGIN_ITEM_NPC_BANKER_GUI_BALANCE_LORE1;
import static de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants.PLUGIN_ITEM_NPC_BANKER_GUI_BALANCE_TOTAL;
import static de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants.PLUGIN_ITEM_NPC_BANKER_GUI_BALANCE_TRANSACTIONS;
import static de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants.PLUGIN_ITEM_NPC_BANKER_GUI_DEPOSIT;
import static de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants.PLUGIN_ITEM_NPC_BANKER_GUI_DEPOSIT_ALL;
import static de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants.PLUGIN_ITEM_NPC_BANKER_GUI_DEPOSIT_AMOUNT_LORE1;
import static de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants.PLUGIN_ITEM_NPC_BANKER_GUI_DEPOSIT_LORE1;
import static de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants.PLUGIN_ITEM_NPC_BANKER_GUI_DEPOSIT_X_PERCENT;
import static de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants.PLUGIN_ITEM_NPC_BANKER_GUI_UPGRADE;
import static de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants.PLUGIN_ITEM_NPC_BANKER_GUI_UPGRADE_LORE1;
import static de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants.PLUGIN_ITEM_NPC_BANKER_GUI_WITHDRAW;
import static de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants.PLUGIN_ITEM_NPC_BANKER_GUI_WITHDRAW_ALL;
import static de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants.PLUGIN_ITEM_NPC_BANKER_GUI_WITHDRAW_AMOUNT_LORE1;
import static de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants.PLUGIN_ITEM_NPC_BANKER_GUI_WITHDRAW_LORE1;
import static de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants.PLUGIN_ITEM_NPC_BANKER_GUI_WITHDRAW_X_PERCENT;
import static de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants.PLUGIN_ITEM_NPC_BANKER_PORTABLE_BANK;
import static de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants.PLUGIN_ITEM_NPC_BANKER_PORTABLE_BANK_LORE1;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.Strings;
import de.relluem94.minecraft.server.spigot.essentials.constants.EventConstants;
import de.relluem94.minecraft.server.spigot.essentials.helpers.ItemHelper.Rarity;
import de.relluem94.minecraft.server.spigot.essentials.helpers.ItemHelper.Type;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.BankAccountEntry;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.BankTierEntry;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.PlayerEntry;

public class BankerHelper {

    private BankerHelper() {
        throw new IllegalStateException(Strings.PLUGIN_INTERNAL_UTILITY_CLASS);
    }

 
    public static final ItemHelper npc_portable_bank = new ItemHelper(Material.YELLOW_SHULKER_BOX, 1, PLUGIN_ITEM_NPC_BANKER_PORTABLE_BANK, Type.TOOL, Rarity.LEGENDARY, List.of(PLUGIN_ITEM_NPC_BANKER_PORTABLE_BANK_LORE1));

    public static final ItemHelper npc_gui_deposit = new ItemHelper(Material.GREEN_SHULKER_BOX, 1, PLUGIN_ITEM_NPC_BANKER_GUI_DEPOSIT, Type.NPC_GUI, Rarity.NONE, List.of(PLUGIN_ITEM_NPC_BANKER_GUI_DEPOSIT_LORE1));
    public static final ItemHelper npc_gui_withdraw = new ItemHelper(Material.RED_SHULKER_BOX, 1, PLUGIN_ITEM_NPC_BANKER_GUI_WITHDRAW, Type.NPC_GUI, Rarity.NONE, List.of(PLUGIN_ITEM_NPC_BANKER_GUI_WITHDRAW_LORE1));
    public static final ItemHelper npc_gui_balance = new ItemHelper(Material.YELLOW_SHULKER_BOX, 1, PLUGIN_ITEM_NPC_BANKER_GUI_BALANCE, Type.NPC_GUI, Rarity.NONE, List.of(PLUGIN_ITEM_NPC_BANKER_GUI_BALANCE_LORE1));
    public static final ItemHelper npc_gui_upgrade = new ItemHelper(Material.DIAMOND_BLOCK, 1, PLUGIN_ITEM_NPC_BANKER_GUI_UPGRADE, Type.NPC_GUI, Rarity.NONE, List.of(PLUGIN_ITEM_NPC_BANKER_GUI_UPGRADE_LORE1));

    public static final ItemHelper npc_gui_deposit_all = new ItemHelper(Material.GOLD_BLOCK, 1, PLUGIN_ITEM_NPC_BANKER_GUI_DEPOSIT_ALL, Type.NPC_GUI, Rarity.NONE, List.of(PLUGIN_ITEM_NPC_BANKER_GUI_DEPOSIT_AMOUNT_LORE1));
    public static final ItemHelper npc_gui_deposit_5_percent = new ItemHelper(Material.GOLD_NUGGET, 1, String.format(PLUGIN_ITEM_NPC_BANKER_GUI_DEPOSIT_X_PERCENT, 5), Type.NPC_GUI, Rarity.NONE, List.of(PLUGIN_ITEM_NPC_BANKER_GUI_DEPOSIT_AMOUNT_LORE1));
    public static final ItemHelper npc_gui_deposit_20_percent = new ItemHelper(Material.GOLD_INGOT, 1, String.format(PLUGIN_ITEM_NPC_BANKER_GUI_DEPOSIT_X_PERCENT, 20), Type.NPC_GUI, Rarity.NONE, List.of(PLUGIN_ITEM_NPC_BANKER_GUI_DEPOSIT_AMOUNT_LORE1));
    public static final ItemHelper npc_gui_deposit_50_percent = new ItemHelper(Material.GOLD_INGOT, 1, String.format(PLUGIN_ITEM_NPC_BANKER_GUI_DEPOSIT_X_PERCENT, 50), Type.NPC_GUI, Rarity.NONE, List.of(PLUGIN_ITEM_NPC_BANKER_GUI_DEPOSIT_AMOUNT_LORE1));

    public static final ItemHelper npc_gui_withdraw_all = new ItemHelper(Material.GOLD_BLOCK, 1, PLUGIN_ITEM_NPC_BANKER_GUI_WITHDRAW_ALL, Type.NPC_GUI, Rarity.NONE, List.of(PLUGIN_ITEM_NPC_BANKER_GUI_WITHDRAW_AMOUNT_LORE1));
    public static final ItemHelper npc_gui_withdraw_5_percent = new ItemHelper(Material.GOLD_NUGGET, 1, String.format(PLUGIN_ITEM_NPC_BANKER_GUI_WITHDRAW_X_PERCENT, 5), Type.NPC_GUI, Rarity.NONE, List.of(PLUGIN_ITEM_NPC_BANKER_GUI_WITHDRAW_AMOUNT_LORE1));
    public static final ItemHelper npc_gui_withdraw_20_percent = new ItemHelper(Material.GOLD_INGOT, 1, String.format(PLUGIN_ITEM_NPC_BANKER_GUI_WITHDRAW_X_PERCENT, 20), Type.NPC_GUI, Rarity.NONE, List.of(PLUGIN_ITEM_NPC_BANKER_GUI_WITHDRAW_AMOUNT_LORE1));
    public static final ItemHelper npc_gui_withdraw_50_percent = new ItemHelper(Material.GOLD_INGOT, 1, String.format(PLUGIN_ITEM_NPC_BANKER_GUI_WITHDRAW_X_PERCENT, 50), Type.NPC_GUI, Rarity.NONE, List.of(PLUGIN_ITEM_NPC_BANKER_GUI_WITHDRAW_AMOUNT_LORE1));

    public static final ItemHelper npc_gui_balance_total = new ItemHelper(Material.YELLOW_SHULKER_BOX, 1, PLUGIN_ITEM_NPC_BANKER_GUI_BALANCE_TOTAL, Type.NPC_GUI, Rarity.NONE);
    public static final ItemHelper npc_gui_balance_transactions = new ItemHelper(Material.MAP, 1, PLUGIN_ITEM_NPC_BANKER_GUI_BALANCE_TRANSACTIONS, Type.NPC_GUI, Rarity.NONE);



    public static final Material UPGRADE_MATERIAL = Material.AMETHYST_SHARD;

    public static List<ItemHelper> getBankTiers(){
        List<ItemHelper> lih = new ArrayList<>();
        for(int i = 0; i < RelluEssentials.getInstance().getBankAPI().getBankTiers().size(); i++){
            BankTierEntry bte = RelluEssentials.getInstance().getBankAPI().getBankTiers().get(i);
            String lore1 = "Costs: " + bte.getCost();
            String lore2 = "Interest: " + bte.getInterest();
            String lore3 = "Limit: " + bte.getLimit();
            lih.add(new ItemHelper(new ItemStack(UPGRADE_MATERIAL, 1), bte.getName(), Type.NPC_GUI, Rarity.NONE, Arrays.asList(lore1, lore2, lore3)));
        }
        return lih;
    }



    public static void deposit(PlayerEntry pe, Player p, BankAccountEntry bae, float percentage){
        double purse = pe.getPurse();
        if(purse >= 1){
            double transactionValue = (purse / 100)  * percentage;

            if(bae.getTier().getLimit() >= transactionValue + bae.getValue()){
                if(percentage == 100){
                    transactionValue = purse;
                    pe.setPurse(0);
                }
                else{
                    pe.setPurse(purse - transactionValue);
                }
    
                RelluEssentials.getInstance().getDatabaseHelper().addTransactionToBank(pe.getId(), bae.getId(), transactionValue, bae.getValue(), bae.getTier().getId());
                
                pe.setUpdatedBy(pe.getId());
                pe.setHasToBeUpdated(true);
    
                p.playSound(p, Sound.ITEM_ARMOR_EQUIP_GOLD, SoundCategory.MASTER, 1f, 1f);
                p.sendMessage(String.format(EventConstants.PLUGIN_EVENT_NPC_BANKER_DEPOIST_MESSAGE, StringHelper.formatDouble(transactionValue)));
                p.sendMessage(String.format(EventConstants.PLUGIN_EVENT_NPC_BANKER_TOTAL,StringHelper.formatDouble(bae.getValue()+transactionValue)));
            }
            else{
                transactionValue = bae.getTier().getLimit() - bae.getValue();
                if(transactionValue > 0){
                    pe.setPurse(purse - transactionValue);

                    RelluEssentials.getInstance().getDatabaseHelper().addTransactionToBank(pe.getId(), bae.getId(), transactionValue, bae.getValue(), bae.getTier().getId());
                    
                    pe.setUpdatedBy(pe.getId());
                    pe.setHasToBeUpdated(true);
        
                    p.playSound(p, Sound.ITEM_ARMOR_EQUIP_GOLD, SoundCategory.MASTER, 1f, 1f);
                    p.sendMessage(String.format(EventConstants.PLUGIN_EVENT_NPC_BANKER_DEPOIST_MESSAGE, StringHelper.formatDouble(transactionValue)));
                    p.sendMessage(String.format(EventConstants.PLUGIN_EVENT_NPC_BANKER_TOTAL,StringHelper.formatDouble(bae.getValue()+transactionValue)));
                    p.sendMessage();
                }
                
                p.sendMessage(EventConstants.PLUGIN_EVENT_NPC_BANKER_DEPOIST_LIMIT_MESSAGE);
            }

            
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
            double transactionValue = ((bank / 100)  * percentage);

            if(percentage == 100){
                transactionValue = bank;
            }

            pe.setPurse(purse + transactionValue);
            RelluEssentials.getInstance().getDatabaseHelper().addTransactionToBank(pe.getId(), bae.getId(), transactionValue*-1, bae.getValue(), bae.getTier().getId());
            
            pe.setUpdatedBy(pe.getId());
            pe.setHasToBeUpdated(true);

            p.playSound(p, Sound.ITEM_ARMOR_EQUIP_GOLD, SoundCategory.MASTER, 1f, 1f);
            p.sendMessage(String.format(EventConstants.PLUGIN_EVENT_NPC_BANKER_WITHDRAW_MESSAGE, StringHelper.formatDouble(transactionValue)));
            p.sendMessage(String.format(EventConstants.PLUGIN_EVENT_NPC_BANKER_TOTAL,StringHelper.formatDouble(bae.getValue()-transactionValue)));
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
            if(!ih.getCustomItem().equals(itemStack)){
                continue;
            }

            Long costs = Long.parseLong(ih.getLore().get(0).replace("Costs: ", ""));
            BankTierEntry bt = getBankTierEntryByCost(costs);

            if(!checkAccount(p, bae, bt, costs)){
                return;
            }

            double purse = pe.getPurse();
            if(purse >= costs){
                pe.setPurse(purse - costs);
                pe.setUpdatedBy(pe.getId());
                pe.setHasToBeUpdated(true);
                RelluEssentials.getInstance().getDatabaseHelper().updateBankAccount(pe.getId(), 0f, bae.getValue(), bt.getId());
                p.sendMessage(EventConstants.PLUGIN_EVENT_NPC_BANKER_BUY_USING_PURSE);
                p.closeInventory();
                return;
            }

            double account = bae.getValue();
            if(account >= costs){
                RelluEssentials.getInstance().getDatabaseHelper().addTransactionToBank(pe.getId(), bae.getId(), -costs, bae.getValue(), bt.getId());
                p.sendMessage(EventConstants.PLUGIN_EVENT_NPC_BANKER_BUY_USING_BANK);
                p.closeInventory();
                return;
            }

            if(purse + account >= costs){
                pe.setPurse(0);
                pe.setUpdatedBy(pe.getId());
                pe.setHasToBeUpdated(true);
                RelluEssentials.getInstance().getDatabaseHelper().addTransactionToBank(pe.getId(), bae.getId(), -(costs-purse), bae.getValue(), bt.getId());
                p.sendMessage(EventConstants.PLUGIN_EVENT_NPC_BANKER_BUY_USING_BOTH);
                p.closeInventory();
                return;
            }

            p.sendMessage(EventConstants.PLUGIN_EVENT_NPC_BANKER_NOT_ENOUGH_COINS);
        }        
    }



    public static void doInterest() {
        if(!Bukkit.getOnlinePlayers().isEmpty()){
            for(Player p : Bukkit.getOnlinePlayers()){
                checkInterest(p.getUniqueId(), true);
                doInterest(p);
            }
        }
    }

    public static void doInterest(Player p){
        if(!RelluEssentials.getInstance().bankInterestMap.containsKey(p.getUniqueId())){
            return;
        }
        
        BankAccountEntry bae = RelluEssentials.getInstance().bankInterestMap.get(p.getUniqueId());

        double interest = (bae.getValue() / 100) * bae.getTier().getInterest();
        p.sendMessage(String.format(EventConstants.PLUGIN_EVENT_NPC_BANKER_INTEREST, StringHelper.formatDouble(interest)));

        RelluEssentials.getInstance().getDatabaseHelper().addTransactionToBank(bae.getPlayerId(), bae.getId(), interest, bae.getValue(), bae.getTier().getId());
        RelluEssentials.getInstance().bankInterestMap.remove(p.getUniqueId());
    }

    public static void checkInterest(UUID uuid, boolean midnight){
        OfflinePlayer op = Bukkit.getOfflinePlayer(uuid);

        if(op == null){
            return;
        } 

        if(!op.hasPlayedBefore()){
            return;
        } 
        
        PlayerEntry pe = RelluEssentials.getInstance().getPlayerAPI().getPlayerEntry(uuid);
        if(pe == null){
            return;
        }

        BankAccountEntry bae = RelluEssentials.getInstance().getDatabaseHelper().getPlayerBankAccount(pe.getId());
        if(bae == null){
            return;
        }

        if(midnight){
            RelluEssentials.getInstance().bankInterestMap.put(uuid, bae);
            return;
        }
        
        long lastPlayedTime = op.getLastPlayed()/1000L;
        
        LocalDate localDate = LocalDate.now();
        ZonedDateTime startOfDayInZone = localDate.atStartOfDay(ZoneId.systemDefault());

        Date lastPlayedDate = new Date(lastPlayedTime*1000L); 
        Date todayDate = new Date(startOfDayInZone.toInstant().toEpochMilli());

        if(lastPlayedDate.before(todayDate)){
            RelluEssentials.getInstance().bankInterestMap.put(uuid, bae);
        }
    }

    public static BankTierEntry getBankTierEntryByCost(long costs){
        for(BankTierEntry bte: RelluEssentials.getInstance().getBankAPI().getBankTiers()){
            if(bte.getCost() == costs){
                return bte;
            }
        }
        return null;
    }



    private static boolean checkAccount(Player p, BankAccountEntry bae, BankTierEntry bt, long costs){     
        if(bt == null){
            return false;
        }

        if(bae.getTier().getCost() == costs){
            p.sendMessage(EventConstants.PLUGIN_EVENT_NPC_BANKER_BUY_ALREADY_BOUGHT);
            return false;
        }

        if(bt.getId() == bae.getTier().getId()){
            p.sendMessage(EventConstants.PLUGIN_EVENT_NPC_BANKER_BUY_ALREADY_BOUGHT);
            return false;
        }

        if(bae.getTier().getCost() > costs){
            p.sendMessage(EventConstants.PLUGIN_EVENT_NPC_BANKER_BUY_LOWER_ACCOUNT);
            return false;
        }
        
        return true;
    }
}