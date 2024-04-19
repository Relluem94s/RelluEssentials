package de.relluem94.minecraft.server.spigot.essentials.events;

import java.util.List;

import org.bukkit.Bukkit;
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
import de.relluem94.minecraft.server.spigot.essentials.constants.Constants;
import de.relluem94.minecraft.server.spigot.essentials.commands.Worlds;
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
import de.relluem94.minecraft.server.spigot.essentials.items.WorldSelector;
import de.relluem94.minecraft.server.spigot.essentials.permissions.Groups;
import de.relluem94.minecraft.server.spigot.essentials.permissions.Permission;


public class BetterNPC implements Listener {

    @EventHandler
    public void onNPCPlacement(PlayerInteractEvent e) {
        if (e.getHand() != null && e.getHand().equals(EquipmentSlot.HAND)) {
            if((e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.RIGHT_CLICK_AIR) && (e.getItem() != null && new WorldSelector().equalsName(e.getItem()))){
                Worlds.openWorldMenu(e.getPlayer());
                e.setCancelled(true);
            }
            else if ((e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.LEFT_CLICK_BLOCK) && (e.getItem() != null && RelluEssentials.getInstance().getNpcAPI().getNPCItemStackList().contains(e.getItem()))){
                e.setCancelled(true);

                Location location = e.getClickedBlock().getLocation().add(0, 1, 0);
                location.setYaw(e.getPlayer().getLocation().getYaw());

                for(int i = 0; i < RelluEssentials.getInstance().getNpcAPI().getNPCItemStackList().size(); i++){
                    if(RelluEssentials.getInstance().getNpcAPI().getNPCItemStackList().get(i).equals(e.getItem())){
                        NPCHelper nh = new NPCHelper(location, RelluEssentials.getInstance().getNpcAPI().getNPC(i));
                        nh.spawn();
                        e.getPlayer().sendMessage(String.format(EventConstants.PLUGIN_EVENT_NPC_SPAWN, nh.getCustomName()));
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
                for(int i = 0; i < RelluEssentials.getInstance().getNpcAPI().getNPCNameList().size(); i++){
                    if(RelluEssentials.getInstance().getNpcAPI().getNPCNameList().get(i).equals(customName)){
                        if(customName.equals(RelluEssentials.getBanker().getName())){
                            PlayerEntry pe = RelluEssentials.getInstance().getPlayerAPI().getPlayerEntry(p);
                            BankAccountEntry bae = RelluEssentials.getInstance().getDatabaseHelper().getPlayerBankAccount(pe.getId());
                            if(bae != null){
                                InventoryHelper.openInventory(p, RelluEssentials.getBanker().getMainGUI());
                            }
                            else{
                                BankTierEntry bte = RelluEssentials.getInstance().getDatabaseHelper().getBankTier(1);
                                if(pe.getPurse() > bte.getCost()){
                                    pe.setPurse(pe.getPurse() - bte.getCost());
                                    pe.setUpdatedBy(pe.getId());
                                    pe.setHasToBeUpdated(true);
            
                                    bae = new BankAccountEntry();
                                    bae.setValue(0);
                                    bae.setTier(bte);
                                    bae.setPlayerId(pe.getId());
                
                                    RelluEssentials.getInstance().getDatabaseHelper().insertBankAccount(bae);
                                    p.sendMessage(EventConstants.PLUGIN_EVENT_NPC_BANKER_OPEN_ACCOUNT);
                                }
                                else{
                                    p.sendMessage(String.format(EventConstants.PLUGIN_EVENT_NPC_BANKER_OPEN_ACCOUNT_TO_LESS_COINS, bte.getCost()));
                                }
                            }
                            e.setCancelled(true);
                        }
                        else{
                            InventoryHelper.openInventory(p, RelluEssentials.getInstance().getNpcAPI().getNPC(i).getMainGUI());
                            e.setCancelled(true);
                        }
                    }
                }
            }
        }
    }


    private void trade(ItemStack is, Inventory inv, Player p, PlayerEntry pe, int slot, boolean isRigthClicked){
        if(CustomItems.npc_gui_close.equalsExact(is)){
            InventoryHelper.closeInventory(p);
        }
        else if(CustomItems.npc_gui_disabled.equalsExact(is)){
            return;
            // DISABLED DOES NOTHING. COULD BE AN EASTER EGG!
        }
        else if(is.getType().equals(Material.PLAYER_HEAD) && is.getItemMeta() instanceof SkullMeta && ((SkullMeta) is.getItemMeta()).getOwnerProfile() != null && ((SkullMeta) is.getItemMeta()).getOwnerProfile().getName().equals(CustomHeads.BAG.getName()) ){
            BagTypeEntry bt = null;

            for(BagTypeEntry bte : RelluEssentials.getInstance().getBagAPI().getBagTypeEntryList()){
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
                        pe.setUpdatedBy(pe.getId());
                        pe.setHasToBeUpdated(true);
                        RelluEssentials.getInstance().getDatabaseHelper().insertBag(bt.getId(), pe.getId());
                        RelluEssentials.getInstance().getPlayerAPI().putPlayerBagEntry(pe.getId(), RelluEssentials.getInstance().getDatabaseHelper().getBag(bt.getId(), pe.getId()));
                        
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
            String itemDisplayname = item.toLowerCase().replace('_', ' ');
            int amountOfItem = is.getAmount();
            int buyPricePerItem = ItemPrice.valueOf(item).getBuyPrice();
            int sellPricePerItem = ItemPrice.valueOf(item).getSellPrice();


            if(inv.getType().equals(InventoryType.CHEST)){
                ItemStack itemStack = is.clone();
                if(isRigthClicked){
                    amountOfItem = 64;
                    itemStack.setAmount(64);
                }

                double coins = buyPricePerItem * (double) amountOfItem;
                if(buyPricePerItem > 0){
                    if(pe.getPurse() - buyPricePerItem * amountOfItem >= 0){
                        if(p.getInventory().firstEmpty() != -1){
                            p.getInventory().addItem(itemStack);
                            p.updateInventory();
    
                            pe.setPurse(pe.getPurse() - coins);
                            pe.setUpdatedBy(pe.getId());
                            pe.setHasToBeUpdated(true);
    
                            p.sendMessage(String.format(EventConstants.PLUGIN_EVENT_NPC_BUY, itemDisplayname, StringHelper.formatDouble(coins), StringHelper.formatDouble(pe.getPurse())));
                            p.playSound(p, Sound.ENTITY_WANDERING_TRADER_YES, SoundCategory.MASTER, 1f, 1f);
                        }
                        else{
                            p.sendMessage(String.format(EventConstants.PLUGIN_EVENT_NPC_BUY_INVENTORY_FULL, itemDisplayname, StringHelper.formatDouble(coins)));
                        }
                    }
                    else{
                        p.sendMessage(String.format(EventConstants.PLUGIN_EVENT_NPC_BUY_NOT_ENOUGH_COINS, itemDisplayname, StringHelper.formatDouble(coins), StringHelper.formatDouble(pe.getPurse())));
                    }
                }
                else{
                    p.sendMessage(EventConstants.PLUGIN_EVENT_NPC_BUY_NOT_TRADEABLE);
                }
            }
            else if(inv.getType().equals(InventoryType.PLAYER)){

                Damageable damageable = ((Damageable) is.getItemMeta());
                if(is.getItemMeta().getEnchants().isEmpty() && sellPricePerItem != 0 && !damageable.hasDamage() && (!is.getItemMeta().hasDisplayName() || is.getItemMeta() instanceof SkullMeta)){

                    double coins;
                    
                    if(isRigthClicked){
                        amountOfItem = 0;
                        ItemStack[] iss = p.getInventory().getContents();
                        for(ItemStack lis : iss){
                            if(lis != null && lis.equals(is)){
                                amountOfItem += lis.getAmount();
                                p.getInventory().remove(lis);
                            }
                        }
                        coins = sellPricePerItem * (double) amountOfItem;
                        

                    }
                    else{
                        coins = sellPricePerItem * (double) amountOfItem;
                        p.getInventory().getItem(slot).setAmount(0);
                    }
                    
                    p.updateInventory();

                    pe.setPurse(pe.getPurse() + coins);
                    pe.setUpdatedBy(pe.getId());
                    pe.setHasToBeUpdated(true);

                    p.sendMessage(String.format(EventConstants.PLUGIN_EVENT_NPC_SELL, itemDisplayname, StringHelper.formatDouble(coins), StringHelper.formatDouble(pe.getPurse())));
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
        if(e.getWhoClicked() instanceof Player p && e.getCurrentItem() != null){
            PlayerEntry pe = RelluEssentials.getInstance().getPlayerAPI().getPlayerEntry(p);
            if (e.getView().getTitle().equals(RelluEssentials.getBanker().getTitle())) {
                BankAccountEntry bae = RelluEssentials.getInstance().getDatabaseHelper().getPlayerBankAccount(pe.getId());
                if(e.getCurrentItem().equals(BankerHelper.npc_gui_deposit.getCustomItem())){
                    InventoryHelper.closeInventory(p);
                    InventoryHelper.openInventory(p, RelluEssentials.getBanker().getDepositGUI());
                }
                else if(e.getCurrentItem().getType().equals(BankerHelper.UPGRADE_MATERIAL)){
                    BankerHelper.upgradeAccount(e.getCurrentItem(), p, pe, bae);
                }
                else if(BankerHelper.npc_gui_deposit_5_percent.equalsExact(e.getCurrentItem())){
                    BankerHelper.deposit(pe, p, bae, 5f);
                }
                else if(BankerHelper.npc_gui_deposit_20_percent.equalsExact(e.getCurrentItem())){
                    BankerHelper.deposit(pe, p, bae, 20f);
                }
                else if(BankerHelper.npc_gui_deposit_50_percent.equalsExact(e.getCurrentItem())){
                    BankerHelper.deposit(pe, p, bae, 50f);
                }
                else if(BankerHelper.npc_gui_deposit_all.equalsExact(e.getCurrentItem())){
                    BankerHelper.deposit(pe, p, bae, 100f);
                }
                else if(BankerHelper.npc_gui_withdraw_5_percent.equalsExact(e.getCurrentItem())){
                    BankerHelper.withdraw(pe, p, bae, 5f);
                }
                else if(BankerHelper.npc_gui_withdraw_20_percent.equalsExact(e.getCurrentItem())){
                    BankerHelper.withdraw(pe, p, bae, 20f);
                }
                else if(BankerHelper.npc_gui_withdraw_50_percent.equalsExact(e.getCurrentItem())){
                    BankerHelper.withdraw(pe, p, bae, 50f);
                }
                else if(BankerHelper.npc_gui_withdraw_all.equalsExact(e.getCurrentItem())){
                    BankerHelper.withdraw(pe, p, bae, 100f);
                }
                else if(BankerHelper.npc_gui_withdraw.equalsExact(e.getCurrentItem())){
                    InventoryHelper.closeInventory(p);
                    InventoryHelper.openInventory(p, RelluEssentials.getBanker().getWithdrawGUI());
                }
                else if(BankerHelper.npc_gui_balance.equalsExact(e.getCurrentItem())){
                    InventoryHelper.closeInventory(p);
                    InventoryHelper.openInventory(p, RelluEssentials.getBanker().getBalanceGUI());
                }
                else if(BankerHelper.npc_gui_balance_total.equalsExact(e.getCurrentItem())){
                    InventoryHelper.closeInventory(p);
                    p.sendMessage(String.format(EventConstants.PLUGIN_EVENT_NPC_BANKER_TOTAL,StringHelper.formatDouble(bae.getValue())));
                }
                else if(BankerHelper.npc_gui_balance_transactions.equalsExact(e.getCurrentItem())){
                    InventoryHelper.closeInventory(p);
                    p.sendMessage(EventConstants.PLUGIN_EVENT_NPC_BANKER_TRANSACTION);
                    List<BankTransactionEntry> btel = RelluEssentials.getInstance().getDatabaseHelper().getTransactionsToBankFromPlayer(bae.getId());
                    for(BankTransactionEntry bte: btel){
                        p.sendMessage(String.format(EventConstants.PLUGIN_EVENT_NPC_BANKER_TRANSACTION_LIST, bte.getValue() > 1 ? EventConstants.PLUGIN_EVENT_NPC_BANKER_TRANSACTION_POSITIVE : EventConstants.PLUGIN_EVENT_NPC_BANKER_TRANSACTION_NEGATIVE, StringHelper.formatDouble(bte.getValue()), bte.getCreated()));
                    }
                }
                else if(BankerHelper.npc_gui_upgrade.equalsExact(e.getCurrentItem())){
                    InventoryHelper.closeInventory(p);
                    InventoryHelper.openInventory(p, RelluEssentials.getBanker().getUpgradeGUI());
                }
                else if(CustomItems.npc_gui_close.equalsExact(e.getCurrentItem())){
                    InventoryHelper.closeInventory(p);
                }
                e.setCancelled(true);
            }
            else if(RelluEssentials.getInstance().getNpcAPI().getNPCTraderTitleList().contains(e.getView().getTitle())){
                    trade(e.getCurrentItem(), e.getClickedInventory(), p, pe, e.getSlot(), e.isRightClick());
                    e.setCancelled(true);
            }
            else if(
                e.getView().getTitle().equals(Constants.PLUGIN_NAME_PREFIX + Constants.PLUGIN_FORMS_SPACER_MESSAGE+"§dNPCs") ||
                e.getView().getTitle().equals(Constants.PLUGIN_COMMAND_CUSTOMHEADS_TITLE)
            ){
                if(!e.getCurrentItem().equals(CustomItems.npc_gui_disabled.getCustomItem())){
                    p.getInventory().addItem(e.getCurrentItem().clone());
                    p.updateInventory();
                }
                e.setCancelled(true);
            }
            else if(
                e.getView().getTitle().equals(Constants.PLUGIN_NAME_PREFIX + Constants.PLUGIN_FORMS_SPACER_MESSAGE+"§dWorlds")
            ){
                if(!e.getCurrentItem().equals(CustomItems.npc_gui_disabled.getCustomItem())){
                    String worldName = e.getCurrentItem().getItemMeta().getDisplayName();
                    p.teleport(Bukkit.getWorld(worldName).getSpawnLocation());
                    e.setCancelled(true);
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

        if(!RelluEssentials.getInstance().getNpcAPI().getNPCNameList().contains(e.getEntity().getCustomName())){
            return;
        }

        if(!(e.getDamager() instanceof Player p)){
            e.setCancelled(true);
            return;
        }

        if (!Permission.isAuthorized(p, Groups.getGroup("admin").getId())) {
            e.setCancelled(true);
        }
    }
}
