package de.relluem94.minecraft.server.spigot.essentials.helpers;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.constants.Constants;
import de.relluem94.minecraft.server.spigot.essentials.constants.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.helpers.ItemHelper.Rarity;
import de.relluem94.minecraft.server.spigot.essentials.helpers.ItemHelper.Type;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.BankAccountEntry;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.BankTierEntry;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.PlayerEntry;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;

import static de.relluem94.minecraft.server.spigot.essentials.RelluEssentials.languageHelper;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_NAME_MONEY;
import static de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants.*;

public class BankerHelper {

    private BankerHelper() {
        throw new IllegalStateException(Constants.PLUGIN_INTERNAL_UTILITY_CLASS);
    }

    @SuppressWarnings("unused")
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

    public static @NotNull ItemStack addLoreLine(@NotNull ItemStack is, String line) {
        ItemMeta im = is.getItemMeta();
        if (im == null) return is;

        List<String> lore = im.getLore() != null ? new ArrayList<>(im.getLore()) : new ArrayList<>();
        if(lore.size() == 1){
            lore.add(line);
        }
        else{
            lore.set(1, line);
        }

        im.setLore(lore);
        is.setItemMeta(im);
        return is;
    }

    public static final Material UPGRADE_MATERIAL = Material.AMETHYST_SHARD;

    public static @NonNull List<ItemHelper> getBankTiers(){
        List<ItemHelper> lih = new ArrayList<>();
        for(int i = 0; i < RelluEssentials.getInstance().getBankAPI().getBankTiers().size(); i++){
            BankTierEntry bte = RelluEssentials.getInstance().getBankAPI().getBankTiers().get(i);
            String lore1 = languageHelper.get(MessageKey.PLUGIN_EVENT_NPC_BANKER_COST_LORE,     bte.getCost());
            String lore2 = languageHelper.get(MessageKey.PLUGIN_EVENT_NPC_BANKER_INTEREST_LORE, bte.getInterest());
            String lore3 = languageHelper.get(MessageKey.PLUGIN_EVENT_NPC_BANKER_LIMIT_LORE,    bte.getLimit());


            ItemHelper itemHelper = new ItemHelper(new ItemStack(UPGRADE_MATERIAL, 1), bte.getName(), Type.NPC_GUI, Rarity.NONE, Arrays.asList(lore1, lore2, lore3));
            itemHelper.setData(new NamespacedKey(RelluEssentials.getInstance(), "cost"), ""+bte.getCost());
            lih.add(itemHelper);
        }
        return lih;
    }



    public static void deposit(@NonNull PlayerEntry pe, Player p, BankAccountEntry bae, float percentage){
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
                p.sendMessage(languageHelper.getWithPrefix(MessageKey.PLUGIN_EVENT_NPC_BANKER_DEPOSIT_MESSAGE, StringHelper.formatDouble(transactionValue), PLUGIN_NAME_MONEY));
                p.sendMessage(languageHelper.getWithPrefix(MessageKey.PLUGIN_EVENT_NPC_BANKER_TOTAL, StringHelper.formatDouble(bae.getValue()+transactionValue), PLUGIN_NAME_MONEY));
            }
            else{
                transactionValue = bae.getTier().getLimit() - bae.getValue();
                if(transactionValue > 0){
                    pe.setPurse(purse - transactionValue);

                    RelluEssentials.getInstance().getDatabaseHelper().addTransactionToBank(pe.getId(), bae.getId(), transactionValue, bae.getValue(), bae.getTier().getId());
                    
                    pe.setUpdatedBy(pe.getId());
                    pe.setHasToBeUpdated(true);
        
                    p.playSound(p, Sound.ITEM_ARMOR_EQUIP_GOLD, SoundCategory.MASTER, 1f, 1f);
                    p.sendMessage(languageHelper.getWithPrefix(MessageKey.PLUGIN_EVENT_NPC_BANKER_DEPOSIT_MESSAGE, StringHelper.formatDouble(transactionValue), PLUGIN_NAME_MONEY));
                    p.sendMessage(languageHelper.getWithPrefix(MessageKey.PLUGIN_EVENT_NPC_BANKER_TOTAL, StringHelper.formatDouble(bae.getValue()+transactionValue), PLUGIN_NAME_MONEY));
                    p.sendMessage();
                }
                
                p.sendMessage(languageHelper.getWithPrefix(MessageKey.PLUGIN_EVENT_NPC_BANKER_DEPOSIT_LIMIT_MESSAGE));
            }

            
            InventoryHelper.closeInventory(p);
        }
        else{
            p.sendMessage(languageHelper.getWithPrefix(MessageKey.PLUGIN_EVENT_NPC_BANKER_DEPOSIT_NO_COINS_MESSAGE, PLUGIN_NAME_MONEY));

            p.playSound(p, Sound.ENTITY_VILLAGER_NO, SoundCategory.MASTER, 1f, 1f);
            InventoryHelper.closeInventory(p);
        }
    }

    public static void withdraw(@NonNull PlayerEntry pe, Player p, @NonNull BankAccountEntry bae, float percentage){
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
            p.sendMessage(String.format(languageHelper.getWithPrefix(MessageKey.PLUGIN_EVENT_NPC_BANKER_WITHDRAW_MESSAGE, StringHelper.formatDouble(transactionValue), PLUGIN_NAME_MONEY)));
            p.sendMessage(languageHelper.getWithPrefix(MessageKey.PLUGIN_EVENT_NPC_BANKER_TOTAL, StringHelper.formatDouble(bae.getValue()-transactionValue), PLUGIN_NAME_MONEY));
            InventoryHelper.closeInventory(p);
        }
        else{
            p.sendMessage(languageHelper.getWithPrefix(MessageKey.PLUGIN_EVENT_NPC_BANKER_NOT_ENOUGH_COINS, PLUGIN_NAME_MONEY));

            p.playSound(p, Sound.ENTITY_VILLAGER_NO, SoundCategory.MASTER, 1f, 1f);
            InventoryHelper.closeInventory(p);
        }
    }

    public static void upgradeAccount(ItemStack itemStack, Player p, PlayerEntry pe, BankAccountEntry bae){
        for(ItemHelper ih : getBankTiers()){
            if(!ih.getCustomItem().equals(itemStack)){
                continue;
            }
            long costs = 0;
            if(ih.hasData(new NamespacedKey(RelluEssentials.getInstance(), "cost"))){
                costs = Long.parseLong(ih.getData(new NamespacedKey(RelluEssentials.getInstance(), "cost")));
            }

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
                p.sendMessage(languageHelper.getWithPrefix(MessageKey.PLUGIN_EVENT_NPC_BANKER_BUY_USING_PURSE));
                p.closeInventory();
                return;
            }

            double account = bae.getValue();
            if(account >= costs){
                RelluEssentials.getInstance().getDatabaseHelper().addTransactionToBank(pe.getId(), bae.getId(), -costs, bae.getValue(), bt.getId());
                p.sendMessage(languageHelper.getWithPrefix(MessageKey.PLUGIN_EVENT_NPC_BANKER_BUY_USING_BANK));
                p.closeInventory();
                return;
            }

            if(purse + account >= costs){
                pe.setPurse(0);
                pe.setUpdatedBy(pe.getId());
                pe.setHasToBeUpdated(true);
                RelluEssentials.getInstance().getDatabaseHelper().addTransactionToBank(pe.getId(), bae.getId(), -(costs-purse), bae.getValue(), bt.getId());
                p.sendMessage(languageHelper.getWithPrefix(MessageKey.PLUGIN_EVENT_NPC_BANKER_BUY_USING_BOTH));
                p.closeInventory();
                return;
            }

            p.sendMessage(languageHelper.getWithPrefix(MessageKey.PLUGIN_EVENT_NPC_BANKER_NOT_ENOUGH_COINS, PLUGIN_NAME_MONEY));
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

    public static void doInterest(@NonNull Player p){
        if(!RelluEssentials.getInstance().bankInterestMap.containsKey(p.getUniqueId())){
            return;
        }
        
        BankAccountEntry bae = RelluEssentials.getInstance().bankInterestMap.get(p.getUniqueId());

        double interest = (bae.getValue() / 100) * bae.getTier().getInterest();
        p.sendMessage(languageHelper.getWithPrefix(MessageKey.PLUGIN_EVENT_NPC_BANKER_INTEREST, StringHelper.formatDouble(interest), PLUGIN_NAME_MONEY));

        RelluEssentials.getInstance().getDatabaseHelper().addTransactionToBank(bae.getPlayerId(), bae.getId(), interest, bae.getValue(), bae.getTier().getId());
        RelluEssentials.getInstance().bankInterestMap.remove(p.getUniqueId());
    }

    public static void checkInterest(UUID uuid, boolean midnight){
        OfflinePlayer op = Bukkit.getOfflinePlayer(uuid);

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

    public static @Nullable BankTierEntry getBankTierEntryByCost(long costs){
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
            p.sendMessage(languageHelper.getWithPrefix(MessageKey.PLUGIN_EVENT_NPC_BANKER_BUY_ALREADY_BOUGHT));
            return false;
        }

        if(bt.getId() == bae.getTier().getId()){
            p.sendMessage(languageHelper.getWithPrefix(MessageKey.PLUGIN_EVENT_NPC_BANKER_BUY_ALREADY_BOUGHT));
            return false;
        }

        if(bae.getTier().getCost() > costs){
            p.sendMessage(languageHelper.getWithPrefix(MessageKey.PLUGIN_EVENT_NPC_BANKER_BUY_LOWER_ACCOUNT));
            return false;
        }
        
        return true;
    }
}