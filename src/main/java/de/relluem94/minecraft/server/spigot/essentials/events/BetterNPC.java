package de.relluem94.minecraft.server.spigot.essentials.events;


import java.util.List;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Villager.Profession;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;

import de.relluem94.minecraft.server.spigot.essentials.CustomItems;
import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.Strings;
import de.relluem94.minecraft.server.spigot.essentials.NPC.Adventurer;
import de.relluem94.minecraft.server.spigot.essentials.NPC.Baker;
import de.relluem94.minecraft.server.spigot.essentials.NPC.Banker;
import de.relluem94.minecraft.server.spigot.essentials.NPC.Butcher;
import de.relluem94.minecraft.server.spigot.essentials.NPC.Farmer;
import de.relluem94.minecraft.server.spigot.essentials.NPC.Fisher;
import de.relluem94.minecraft.server.spigot.essentials.NPC.Miner;
import de.relluem94.minecraft.server.spigot.essentials.NPC.Smith;
import de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants;
import de.relluem94.minecraft.server.spigot.essentials.constants.ItemPrice;
import de.relluem94.minecraft.server.spigot.essentials.helpers.InventoryHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.NPCHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.BankAccountEntry;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.BankTierEntry;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.BankTransactionEntry;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.PlayerEntry;


public class BetterNPC implements Listener {

    @EventHandler
    public void onNPCPlacement(PlayerInteractEvent e) {
        if (e.getHand() != null && e.getHand().equals(EquipmentSlot.HAND)) {
            if (e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.LEFT_CLICK_BLOCK) {
                if(e.getItem() != null && (
                    e.getItem().equals(Banker.npc.getCustomItem()) || 
                    e.getItem().equals(Fisher.npc.getCustomItem()) || 
                    e.getItem().equals(Farmer.npc.getCustomItem()) ||
                    e.getItem().equals(Baker.npc.getCustomItem()) || 
                    e.getItem().equals(Butcher.npc.getCustomItem()) || 
                    e.getItem().equals(Smith.npc.getCustomItem()) || 
                    e.getItem().equals(Adventurer.npc.getCustomItem()) || 
                    e.getItem().equals(Miner.npc.getCustomItem())
                    )){
                    e.setCancelled(true);

                    Location location = e.getClickedBlock().getLocation().add(0, 1, 0);
                    location.setYaw(e.getPlayer().getLocation().getYaw());

                    NPCHelper nh;
                    if(e.getItem().equals(Banker.npc.getCustomItem())){
                        nh = new NPCHelper(location, Banker.npc.getDisplayName(), Profession.NONE, true);
                        nh.spawn();
                    }
                    else if(e.getItem().equals(Fisher.npc.getCustomItem())){
                        nh = new NPCHelper(location, Fisher.npc.getDisplayName(), Profession.FISHERMAN, true);
                        nh.spawn();
                    }
                    else if(e.getItem().equals(Farmer.npc.getCustomItem())){
                        nh = new NPCHelper(location, Farmer.npc.getDisplayName(), Profession.FARMER, true);
                        nh.spawn();
                    }
                    else if(e.getItem().equals(Smith.npc.getCustomItem())){
                        nh = new NPCHelper(location, Smith.npc.getDisplayName(), Profession.WEAPONSMITH, true);
                        nh.spawn();
                    }
                    else if(e.getItem().equals(Adventurer.npc.getCustomItem())){
                        nh = new NPCHelper(location, Adventurer.npc.getDisplayName(), Profession.NONE, true);
                        nh.spawn();
                    }
                    else if(e.getItem().equals(Miner.npc.getCustomItem())){
                        nh = new NPCHelper(location, Miner.npc.getDisplayName(), Profession.NONE, true);
                        nh.spawn();
                    }
                    else if(e.getItem().equals(Baker.npc.getCustomItem())){
                        nh = new NPCHelper(location, Baker.npc.getDisplayName(), Profession.NONE, true);
                        nh.spawn();
                    }
                    else if(e.getItem().equals(Butcher.npc.getCustomItem())){
                        nh = new NPCHelper(location, Butcher.npc.getDisplayName(), Profession.BUTCHER, true);
                        nh.spawn();
                    }

                    e.getPlayer().sendMessage("NPC is Placed");
                }
            }
        }
    }


    @EventHandler
    public void onPlayerInteractEntity (PlayerInteractEntityEvent e) {
        Player p = e.getPlayer();
        if(e.getRightClicked() instanceof Villager){
            if(e.getRightClicked().getCustomName() != null && e.getRightClicked().getCustomName().equals(ItemConstants.PLUGIN_ITEM_NPC_BANKER)){
                PlayerEntry pe = RelluEssentials.playerEntryList.get(p.getUniqueId());
                BankAccountEntry bae = RelluEssentials.dBH.getPlayerBankAccount(pe.getID());
                if(bae != null){
                    InventoryHelper.openInventory(p, Banker.getMainGUI());
                }
                else{
                    BankTierEntry bte = RelluEssentials.dBH.getBankTier(1);
                    if(pe.getPurse() > bte.getCost()){
                        pe.setPurse(pe.getPurse() - bte.getCost());
                        RelluEssentials.dBH.updatePlayer(pe);

                        bae = new BankAccountEntry();
                        bae.setValue(0);
                        bae.setTier(bte);
                        bae.setPlayerId(pe.getID());
    
                        RelluEssentials.dBH.insertBankAccount(bae);
                    }
                    else{
                        // NOT ENOUGH MONEY
                    }
                }
                
                e.setCancelled(true);
            }
            else if(e.getRightClicked().getCustomName() != null && e.getRightClicked().getCustomName().equals(ItemConstants.PLUGIN_ITEM_NPC_FISHER)){
                InventoryHelper.openInventory(p, Fisher.getMainGUI());
                e.setCancelled(true);
            }
            else if(e.getRightClicked().getCustomName() != null && e.getRightClicked().getCustomName().equals(ItemConstants.PLUGIN_ITEM_NPC_FARMER)){
                InventoryHelper.openInventory(p, Farmer.getMainGUI());
                e.setCancelled(true);
            }
            else if(e.getRightClicked().getCustomName() != null && e.getRightClicked().getCustomName().equals(ItemConstants.PLUGIN_ITEM_NPC_SMITH)){
                InventoryHelper.openInventory(p, Smith.getMainGUI());
                e.setCancelled(true);
            }
            else if(e.getRightClicked().getCustomName() != null && e.getRightClicked().getCustomName().equals(ItemConstants.PLUGIN_ITEM_NPC_ADVENTURER)){
                InventoryHelper.openInventory(p, Adventurer.getMainGUI());
                e.setCancelled(true);
            }
            else if(e.getRightClicked().getCustomName() != null && e.getRightClicked().getCustomName().equals(ItemConstants.PLUGIN_ITEM_NPC_MINER)){
                InventoryHelper.openInventory(p, Miner.getMainGUI());
                e.setCancelled(true);
            }
            else if(e.getRightClicked().getCustomName() != null && e.getRightClicked().getCustomName().equals(ItemConstants.PLUGIN_ITEM_NPC_BAKER)){
                InventoryHelper.openInventory(p, Baker.getMainGUI());
                e.setCancelled(true);
            }
            else if(e.getRightClicked().getCustomName() != null && e.getRightClicked().getCustomName().equals(ItemConstants.PLUGIN_ITEM_NPC_BUTCHER)){
                InventoryHelper.openInventory(p, Butcher.getMainGUI());
                e.setCancelled(true);
            }
            else if(e.getRightClicked().getCustomName() != null && e.getRightClicked().getCustomName().equals(ItemConstants.PLUGIN_ITEM_NPC_ADVENTURER)){
                InventoryHelper.openInventory(p, Adventurer.getMainGUI());
                e.setCancelled(true);
            }
        }
    }


    private void trade(ItemStack is, Inventory inv, Player p, PlayerEntry pe, int slot){
        if(CustomItems.npc_gui_close.equals(is)){
            InventoryHelper.closeInventory(p);
        }
        else if(CustomItems.npc_gui_sell.equals(is)){
            InventoryHelper.closeInventory(p);
        }
        else if(CustomItems.npc_gui_disabled.equals(is)){
            
        }
        else{
            String item = is.getType().name();
            String item_displayname = item.toLowerCase().replace('_', ' ');
            int amountOfItem = is.getAmount();
            int buyPricePerItem = ItemPrice.valueOf(item).getBuyPrice();
            int sellPricePerItem = ItemPrice.valueOf(item).getSellPrice();


            if(inv.getType().equals(InventoryType.CHEST)){
                int coins = buyPricePerItem * amountOfItem;
                if(buyPricePerItem > 0){
                    if(pe.getPurse() - buyPricePerItem * amountOfItem >= 0){
                        if(p.getInventory().firstEmpty() != -1){
                            p.getInventory().addItem(is.clone());
                            p.updateInventory();
    
                            pe.setPurse(pe.getPurse() - coins);
                            RelluEssentials.dBH.updatePlayer(pe);
    
                            p.sendMessage(String.format(Strings.PLUGIN_COMMAND_NPC_BUY, item_displayname, coins, pe.getPurse()));
                            p.playSound(p, Sound.ENTITY_WANDERING_TRADER_YES, SoundCategory.MASTER, 1f, 1f);
                        }
                        else{
                            p.sendMessage(String.format(Strings.PLUGIN_COMMAND_NPC_BUY_INVENTORY_FULL, item_displayname, coins));
                        }
                    }
                    else{
                        p.sendMessage(String.format(Strings.PLUGIN_COMMAND_NPC_BUY_NOT_ENOUGH_MONEY, item_displayname, coins, pe.getPurse()));
                    }
                }
                else{
                    p.sendMessage(Strings.PLUGIN_COMMAND_NPC_BUY_NOT_TRADEABLE);
                }
            }
            else if(inv.getType().equals(InventoryType.PLAYER)){

                Damageable damageable = ((Damageable) is.getItemMeta());
                
                if(is.getItemMeta().getEnchants().isEmpty() && sellPricePerItem != 0 && !damageable.hasDamage()){
                    int coins = sellPricePerItem * amountOfItem;
                    
                    p.getInventory().getItem(slot).setAmount(0);
                    p.updateInventory();

                    pe.setPurse(pe.getPurse() + coins);
                    RelluEssentials.dBH.updatePlayer(pe);

                    p.sendMessage(String.format(Strings.PLUGIN_COMMAND_NPC_SELL, item_displayname, coins, pe.getPurse()));
                    p.playSound(p, Sound.ENTITY_WANDERING_TRADER_NO, SoundCategory.MASTER, 1f, 1f);
                }
                else {
                    if(!is.getItemMeta().getEnchants().isEmpty()){
                        p.sendMessage(Strings.PLUGIN_COMMAND_NPC_SELL_ENCHANTED);
                    }
                    else{
                        if(damageable.hasDamage()){
                            p.sendMessage(Strings.PLUGIN_COMMAND_NPC_SELL_USED_ITEM);
                        }
                        else{
                            if(sellPricePerItem == 0){
                                p.sendMessage(Strings.PLUGIN_COMMAND_NPC_SELL_NO_PRICE);
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
            PlayerEntry pe = RelluEssentials.playerEntryList.get(p.getUniqueId());
            if (e.getView().getTitle().equals(Strings.PLUGIN_PREFIX + Strings.PLUGIN_SPACER + ItemConstants.PLUGIN_ITEM_NPC_BANKER)) {
                BankAccountEntry bae = RelluEssentials.dBH.getPlayerBankAccount(pe.getID());
                if(e.getCurrentItem().equals(Banker.npc_gui_deposit.getCustomItem())){
                    InventoryHelper.closeInventory(p);
                    InventoryHelper.openInventory(p, Banker.getDepositGUI());
                }
                else if(Banker.npc_gui_deposit_5_percent.equals(e.getCurrentItem())){
                    Banker.deposit(pe, p, bae, 5f);
                }
                else if(Banker.npc_gui_deposit_20_percent.equals(e.getCurrentItem())){
                    Banker.deposit(pe, p, bae, 20f);
                }
                else if( Banker.npc_gui_deposit_50_percent.equals(e.getCurrentItem())){
                    Banker.deposit(pe, p, bae, 50f);
                }
                else if(Banker.npc_gui_deposit_all.equals(e.getCurrentItem())){
                    Banker.deposit(pe, p, bae, 100f);
                }
                else if(Banker.npc_gui_withdraw_5_percent.equals(e.getCurrentItem())){
                    Banker.withdraw(pe, p, bae, 5f);
                }
                else if(Banker.npc_gui_withdraw_20_percent.equals(e.getCurrentItem())){
                    Banker.withdraw(pe, p, bae, 20f);
                }
                else if(Banker.npc_gui_withdraw_50_percent.equals(e.getCurrentItem())){
                    Banker.withdraw(pe, p, bae, 50f);
                }
                else if(Banker.npc_gui_withdraw_all.equals(e.getCurrentItem())){
                    Banker.withdraw(pe, p, bae, 100f);
                }
                else if(Banker.npc_gui_withdraw.equals(e.getCurrentItem())){
                    InventoryHelper.closeInventory(p);
                    InventoryHelper.openInventory(p, Banker.getWithdrawGUI());
                }
                else if(Banker.npc_gui_balance.equals(e.getCurrentItem())){
                    InventoryHelper.closeInventory(p);
                    InventoryHelper.openInventory(p, Banker.getBalanceGUI());
                }
                else if(Banker.npc_gui_balance_total.equals(e.getCurrentItem())){
                    InventoryHelper.closeInventory(p);
                    p.sendMessage("Your total is: " +  bae.getValue());
                }
                else if(Banker.npc_gui_balance_transactions.equals(e.getCurrentItem())){
                    InventoryHelper.closeInventory(p);
                    p.sendMessage("Your transactions are:");
                    List<BankTransactionEntry> btel = RelluEssentials.dBH.getTransactionsToBankFromPlayer(bae.getId());
                    for(BankTransactionEntry bte: btel){
                        p.sendMessage(String.format("Transaction with %s Coins on %s", bte.getValue(), bte.getCreated()));
                    }
                }
                else if(Banker.npc_gui_upgrade.equals(e.getCurrentItem())){
                    InventoryHelper.closeInventory(p);
                    InventoryHelper.openInventory(p, Banker.getUpgradeGUI());
                }
                else if(CustomItems.npc_gui_close.equals(e.getCurrentItem())){
                    InventoryHelper.closeInventory(p);
                }
                e.setCancelled(true);
            }
            else if(
                e.getView().getTitle().equals(Strings.PLUGIN_PREFIX + Strings.PLUGIN_SPACER + ItemConstants.PLUGIN_ITEM_NPC_MINER) ||
                e.getView().getTitle().equals(Strings.PLUGIN_PREFIX + Strings.PLUGIN_SPACER + ItemConstants.PLUGIN_ITEM_NPC_SMITH) ||
                e.getView().getTitle().equals(Strings.PLUGIN_PREFIX + Strings.PLUGIN_SPACER + ItemConstants.PLUGIN_ITEM_NPC_FARMER) ||
                e.getView().getTitle().equals(Strings.PLUGIN_PREFIX + Strings.PLUGIN_SPACER + ItemConstants.PLUGIN_ITEM_NPC_FISHER) ||
                e.getView().getTitle().equals(Strings.PLUGIN_PREFIX + Strings.PLUGIN_SPACER + ItemConstants.PLUGIN_ITEM_NPC_BUTCHER) ||
                e.getView().getTitle().equals(Strings.PLUGIN_PREFIX + Strings.PLUGIN_SPACER + ItemConstants.PLUGIN_ITEM_NPC_ADVENTURER) ||
                e.getView().getTitle().equals(Strings.PLUGIN_PREFIX + Strings.PLUGIN_SPACER + ItemConstants.PLUGIN_ITEM_NPC_BAKER)
                ){
                trade(e.getCurrentItem(), e.getClickedInventory(), p, pe, e.getSlot());
                e.setCancelled(true);
            }
        }
    }
}
