package de.relluem94.minecraft.server.spigot.essentials.events;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.SkullMeta;

import de.relluem94.minecraft.server.spigot.essentials.CustomItems;
import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.Strings;
import de.relluem94.minecraft.server.spigot.essentials.api.BagAPI;
import de.relluem94.minecraft.server.spigot.essentials.api.NPCAPI;
import de.relluem94.minecraft.server.spigot.essentials.api.PlayerAPI;
import de.relluem94.minecraft.server.spigot.essentials.constants.CustomHeads;
import de.relluem94.minecraft.server.spigot.essentials.constants.EventConstants;
import de.relluem94.minecraft.server.spigot.essentials.constants.ItemPrice;
import de.relluem94.minecraft.server.spigot.essentials.helpers.BagHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.BankerHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.InventoryHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.NPCHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.StringHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.BagTypeEntry;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.BankAccountEntry;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.BankTierEntry;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.BankTransactionEntry;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.PlayerEntry;
import de.relluem94.minecraft.server.spigot.essentials.permissions.Groups;
import de.relluem94.minecraft.server.spigot.essentials.permissions.Permission;


public class BetterNPC implements Listener {

    @EventHandler
    public void onNPCPlacement(PlayerInteractEvent e) {
        if (e.getHand() != null && e.getHand().equals(EquipmentSlot.HAND)) {

            if (e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.LEFT_CLICK_BLOCK) {
                if(e.getItem() != null && NPCAPI.getNPCItemStackList().contains(e.getItem())){
                    e.setCancelled(true);

                    Location location = e.getClickedBlock().getLocation().add(0, 1, 0);
                    location.setYaw(e.getPlayer().getLocation().getYaw());

                    for(int i = 0; i < NPCAPI.getNPCItemStackList().size(); i++){
                        if(NPCAPI.getNPCItemStackList().get(i).equals(e.getItem())){
                            NPCHelper nh = new NPCHelper(location, NPCAPI.getNPC(i));
                            nh.spawn();
                            e.getPlayer().sendMessage(String.format(EventConstants.PLUGIN_EVENT_NPC_SPAWN, nh.getCustomName()));
                        }
                    }
                }
            }
        }
    }


    @EventHandler
    public void onPlayerInteractEntity (PlayerInteractEntityEvent e) {
        Player p = e.getPlayer();
        if(e.getRightClicked() instanceof Villager){
            if(e.getRightClicked().getCustomName() != null) {
                String customName = e.getRightClicked().getCustomName();
                for(int i = 0; i < NPCAPI.getNPCNameList().size(); i++){
                    if(NPCAPI.getNPCNameList().get(i).equals(customName)){
                        if(customName.equals(RelluEssentials.banker.getName())){
                            PlayerEntry pe = PlayerAPI.getPlayerEntry(p);
                            BankAccountEntry bae = RelluEssentials.dBH.getPlayerBankAccount(pe.getID());
                            if(bae != null){
                                InventoryHelper.openInventory(p, RelluEssentials.banker.getMainGUI());
                            }
                            else{
                                BankTierEntry bte = RelluEssentials.dBH.getBankTier(1);
                                if(pe.getPurse() > bte.getCost()){
                                    pe.setPurse(pe.getPurse() - bte.getCost());
                                    pe.setUpdatedBy(pe.getID());
                                    pe.setToBeUpdated(true);
            
                                    bae = new BankAccountEntry();
                                    bae.setValue(0);
                                    bae.setTier(bte);
                                    bae.setPlayerId(pe.getID());
                
                                    RelluEssentials.dBH.insertBankAccount(bae);
                                    p.sendMessage(EventConstants.PLUGIN_EVENT_NPC_BANKER_OPEN_ACCOUNT);
                                }
                                else{
                                    p.sendMessage(String.format(EventConstants.PLUGIN_EVENT_NPC_BANKER_OPEN_ACCOUNT_TO_LESS_COINS, bte.getCost()));
                                }
                            }
                            e.setCancelled(true);
                        }
                        else{
                            InventoryHelper.openInventory(p, NPCAPI.getNPC(i).getMainGUI());
                            e.setCancelled(true);
                        }
                    }
                }
            }
        }
    }


    private void trade(ItemStack is, Inventory inv, Player p, PlayerEntry pe, int slot, boolean isRigthClicked){
        if(CustomItems.npc_gui_close.equals(is)){
            InventoryHelper.closeInventory(p);
        }
        else if(CustomItems.npc_gui_disabled.equals(is)){
            // DISABLED DOES NOTHING. COULD BE AN EASTER EGG!
        }
        else if(is.getType().equals(Material.PLAYER_HEAD) && is.getItemMeta() instanceof SkullMeta && ((SkullMeta) is.getItemMeta()).getOwnerProfile() != null && ((SkullMeta) is.getItemMeta()).getOwnerProfile().getName().equals(CustomHeads.BAG.getName()) ){
            BagTypeEntry bt = null;

            for(BagTypeEntry bte : BagAPI.getBagTypeEntryList()){
                if(bte.getDisplayName().equals(is.getItemMeta().getDisplayName())){
                    bt = bte;
                }
            }

            if(bt != null){
                double purse = pe.getPurse();
                int price = bt.getCost();

                if(!BagHelper.hasBag(bt.getId(), pe)){
                    if(purse >= price){
                        pe.setPurse(purse - price);
                        pe.setUpdatedBy(pe.getID());
                        pe.setToBeUpdated(true);
                        RelluEssentials.dBH.insertBag(bt.getId(), pe.getID());
                        PlayerAPI.putPlayerBagEntry(pe.getID(), RelluEssentials.dBH.getBag(bt.getId(), pe.getID()));
                        
                        p.sendMessage(String.format(EventConstants.PLUGIN_EVENT_NPC_BAGS_BOUGHT, bt.getDisplayName()));
                    }
                    else{
                        p.sendMessage(EventConstants.PLUGIN_EVENT_NPC_BAGS_NO_COINS);
                    }
                }
                else{
                    p.sendMessage(String.format(EventConstants.PLUGIN_EVENT_NPC_BAGS_ALREADY_BOUGHT, bt.getDisplayName()));
                }
            }
            else{
                p.sendMessage(EventConstants.PLUGIN_EVENT_NPC_BAGS_NO_BAG_FOUND);
            }
        }
        else{
            String item = is.getType().name();
            String item_displayname = item.toLowerCase().replace('_', ' ');
            int amountOfItem = is.getAmount();
            int buyPricePerItem = ItemPrice.valueOf(item).getBuyPrice();
            int sellPricePerItem = ItemPrice.valueOf(item).getSellPrice();


            if(inv.getType().equals(InventoryType.CHEST)){
                ItemStack is_item = is.clone();
                if(isRigthClicked){
                    amountOfItem = 64;
                    is_item.setAmount(64);
                }

                double coins = buyPricePerItem * amountOfItem;
                if(buyPricePerItem > 0){
                    if(pe.getPurse() - buyPricePerItem * amountOfItem >= 0){
                        if(p.getInventory().firstEmpty() != -1){
                            p.getInventory().addItem(is_item);
                            p.updateInventory();
    
                            pe.setPurse(pe.getPurse() - coins);
                            pe.setUpdatedBy(pe.getID());
                            pe.setToBeUpdated(true);
    
                            p.sendMessage(String.format(EventConstants.PLUGIN_EVENT_NPC_BUY, item_displayname, StringHelper.formatDouble(coins), StringHelper.formatDouble(pe.getPurse())));
                            p.playSound(p, Sound.ENTITY_WANDERING_TRADER_YES, SoundCategory.MASTER, 1f, 1f);
                        }
                        else{
                            p.sendMessage(String.format(EventConstants.PLUGIN_EVENT_NPC_BUY_INVENTORY_FULL, item_displayname, StringHelper.formatDouble(coins)));
                        }
                    }
                    else{
                        p.sendMessage(String.format(EventConstants.PLUGIN_EVENT_NPC_BUY_NOT_ENOUGH_COINS, item_displayname, StringHelper.formatDouble(coins), StringHelper.formatDouble(pe.getPurse())));
                    }
                }
                else{
                    p.sendMessage(EventConstants.PLUGIN_EVENT_NPC_BUY_NOT_TRADEABLE);
                }
            }
            else if(inv.getType().equals(InventoryType.PLAYER)){

                Damageable damageable = ((Damageable) is.getItemMeta());
                if(is.getItemMeta().getEnchants().isEmpty() && sellPricePerItem != 0 && !damageable.hasDamage() && (!is.getItemMeta().hasDisplayName() || is.getItemMeta() instanceof SkullMeta)){

                    double coins = 0;
                    
                    if(isRigthClicked){
                        amountOfItem = 0;
                        ItemStack[] iss = p.getInventory().getContents();
                        for(ItemStack lis : iss){
                            if(lis != null && lis.equals(is)){
                                amountOfItem += lis.getAmount();
                                p.getInventory().remove(lis);
                            }
                        }
                        coins = sellPricePerItem * amountOfItem;
                        

                    }
                    else{
                        coins = sellPricePerItem * amountOfItem;
                        p.getInventory().getItem(slot).setAmount(0);
                    }
                    
                    p.updateInventory();

                    pe.setPurse(pe.getPurse() + coins);
                    pe.setUpdatedBy(pe.getID());
                    pe.setToBeUpdated(true);

                    p.sendMessage(String.format(EventConstants.PLUGIN_EVENT_NPC_SELL, item_displayname, StringHelper.formatDouble(coins), StringHelper.formatDouble(pe.getPurse())));
                    p.playSound(p, Sound.ENTITY_WANDERING_TRADER_NO, SoundCategory.MASTER, 1f, 1f);
                }
                else {
                    if(!is.getItemMeta().getEnchants().isEmpty()){
                        p.sendMessage(EventConstants.PLUGIN_EVENT_NPC_SELL_ENCHANTED);
                    }
                    else{
                        if(damageable.hasDamage()){
                            p.sendMessage(EventConstants.PLUGIN_EVENT_NPC_SELL_USED_ITEM);
                        }
                        else{
                            if(sellPricePerItem == 0){
                                p.sendMessage(EventConstants.PLUGIN_EVENT_NPC_SELL_NO_PRICE);
                            }
                            else{
                                if(is.getItemMeta().hasDisplayName()){
                                    p.sendMessage(EventConstants.PLUGIN_EVENT_NPC_SELL_RENAMED);
                                }
                            }
                        }
                    }
                }
            }
        }
    }


   
    @EventHandler
    public void onInventoryClickItem(InventoryClickEvent e) {
        if(e.getWhoClicked() instanceof Player && e.getCurrentItem() != null){
            Player p = (Player) e.getWhoClicked();
            PlayerEntry pe = PlayerAPI.getPlayerEntry(p);
            if (e.getView().getTitle().equals(RelluEssentials.banker.getTitle())) {
                BankAccountEntry bae = RelluEssentials.dBH.getPlayerBankAccount(pe.getID());
                if(e.getCurrentItem().equals(BankerHelper.npc_gui_deposit.getCustomItem())){
                    InventoryHelper.closeInventory(p);
                    InventoryHelper.openInventory(p, RelluEssentials.banker.getDepositGUI());
                }
                else if(e.getCurrentItem().getType().equals(BankerHelper.UPGRADE_MATERIAL)){
                    BankerHelper.upgradeAccount(e.getCurrentItem(), p, pe, bae);
                }
                else if(BankerHelper.npc_gui_deposit_5_percent.equals(e.getCurrentItem())){
                    BankerHelper.deposit(pe, p, bae, 5f);
                }
                else if(BankerHelper.npc_gui_deposit_20_percent.equals(e.getCurrentItem())){
                    BankerHelper.deposit(pe, p, bae, 20f);
                }
                else if(BankerHelper.npc_gui_deposit_50_percent.equals(e.getCurrentItem())){
                    BankerHelper.deposit(pe, p, bae, 50f);
                }
                else if(BankerHelper.npc_gui_deposit_all.equals(e.getCurrentItem())){
                    BankerHelper.deposit(pe, p, bae, 100f);
                }
                else if(BankerHelper.npc_gui_withdraw_5_percent.equals(e.getCurrentItem())){
                    BankerHelper.withdraw(pe, p, bae, 5f);
                }
                else if(BankerHelper.npc_gui_withdraw_20_percent.equals(e.getCurrentItem())){
                    BankerHelper.withdraw(pe, p, bae, 20f);
                }
                else if(BankerHelper.npc_gui_withdraw_50_percent.equals(e.getCurrentItem())){
                    BankerHelper.withdraw(pe, p, bae, 50f);
                }
                else if(BankerHelper.npc_gui_withdraw_all.equals(e.getCurrentItem())){
                    BankerHelper.withdraw(pe, p, bae, 100f);
                }
                else if(BankerHelper.npc_gui_withdraw.equals(e.getCurrentItem())){
                    InventoryHelper.closeInventory(p);
                    InventoryHelper.openInventory(p, RelluEssentials.banker.getWithdrawGUI());
                }
                else if(BankerHelper.npc_gui_balance.equals(e.getCurrentItem())){
                    InventoryHelper.closeInventory(p);
                    InventoryHelper.openInventory(p, RelluEssentials.banker.getBalanceGUI());
                }
                else if(BankerHelper.npc_gui_balance_total.equals(e.getCurrentItem())){
                    InventoryHelper.closeInventory(p);
                    p.sendMessage(String.format(EventConstants.PLUGIN_EVENT_NPC_BANKER_TOTAL,StringHelper.formatDouble(bae.getValue())));
                }
                else if(BankerHelper.npc_gui_balance_transactions.equals(e.getCurrentItem())){
                    InventoryHelper.closeInventory(p);
                    p.sendMessage(EventConstants.PLUGIN_EVENT_NPC_BANKER_TRANSACTION);
                    List<BankTransactionEntry> btel = RelluEssentials.dBH.getTransactionsToBankFromPlayer(bae.getId());
                    for(BankTransactionEntry bte: btel){
                        p.sendMessage(String.format(EventConstants.PLUGIN_EVENT_NPC_BANKER_TRANSACTION_LIST, bte.getValue() > 1 ? EventConstants.PLUGIN_EVENT_NPC_BANKER_TRANSACTION_POSITIVE : EventConstants.PLUGIN_EVENT_NPC_BANKER_TRANSACTION_NEGATIVE, StringHelper.formatDouble(bte.getValue()), bte.getCreated()));
                    }
                }
                else if(BankerHelper.npc_gui_upgrade.equals(e.getCurrentItem())){
                    InventoryHelper.closeInventory(p);
                    InventoryHelper.openInventory(p, RelluEssentials.banker.getUpgradeGUI());
                }
                else if(CustomItems.npc_gui_close.equals(e.getCurrentItem())){
                    InventoryHelper.closeInventory(p);
                }
                e.setCancelled(true);
            }
            else if(NPCAPI.getNPCTraderTitleList().contains(e.getView().getTitle())){
                    trade(e.getCurrentItem(), e.getClickedInventory(), p, pe, e.getSlot(), e.isRightClick());
                    e.setCancelled(true);
            }
            else if(e.getView().getTitle().equals(Strings.PLUGIN_PREFIX + Strings.PLUGIN_SPACER +"§dNPCs")){
                if(!e.getCurrentItem().equals(CustomItems.npc_gui_disabled.getCustomItem())){
                    p.getInventory().addItem(e.getCurrentItem().clone());
                    p.updateInventory();
                }
                e.setCancelled(true);
            }
            else if(e.getView().getTitle().equals(Strings.PLUGIN_COMMAND_CUSTOMHEADS_TITLE)){
                if(!e.getCurrentItem().equals(CustomItems.npc_gui_disabled.getCustomItem())){
                    p.getInventory().addItem(e.getCurrentItem().clone());
                    p.updateInventory();
                }
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onNPCDamage(EntityDamageByEntityEvent e){
        if(!(e.getEntity() instanceof Villager)){
            return;
        }

        if(e.getEntity().getCustomName() == null){
            return;
        }

        if(!NPCAPI.getNPCNameList().contains(e.getEntity().getCustomName())){
            return;
        }

        if(!(e.getDamager() instanceof Player)){
            e.setCancelled(true);
            return;
        }

        Player p = (Player) e.getDamager();

        if (!Permission.isAuthorized(p, Groups.getGroup("admin").getId())) {
            e.setCancelled(true);
            return;
        }
    }
}
