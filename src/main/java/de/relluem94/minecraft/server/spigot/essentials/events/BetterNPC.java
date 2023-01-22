package de.relluem94.minecraft.server.spigot.essentials.events;


import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Villager.Profession;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;

import de.relluem94.minecraft.server.spigot.essentials.CustomItems;
import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.Strings;
import de.relluem94.minecraft.server.spigot.essentials.NPC.Banker;
import de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants;
import de.relluem94.minecraft.server.spigot.essentials.helpers.DatabaseHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.InventoryHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.NPCHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.BankAccountEntry;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.BankTierEntry;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.PlayerEntry;


public class BetterNPC implements Listener {

    @EventHandler
    public void onNPCPlacement(PlayerInteractEvent e) {
        if (e.getHand() != null && e.getHand().equals(EquipmentSlot.HAND)) {
            if (e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.LEFT_CLICK_BLOCK) {
                if(e.getItem() != null && (
                    e.getItem().equals(Banker.npc.getCustomItem()) || 
                    e.getItem().equals(CustomItems.npcFisher.getCustomItem()) || 
                    e.getItem().equals(CustomItems.npcFarmer.getCustomItem()) || 
                    e.getItem().equals(CustomItems.npcSmith.getCustomItem()) || 
                    e.getItem().equals(CustomItems.npcAdventurer.getCustomItem()) || 
                    e.getItem().equals(CustomItems.npcMiner.getCustomItem())
                    )){
                    e.setCancelled(true);

                    Location location = e.getClickedBlock().getLocation().add(0, 1, 0);
                    location.setYaw(e.getPlayer().getLocation().getYaw());

                    NPCHelper nh;
                    if(e.getItem().equals(Banker.npc.getCustomItem())){
                        nh = new NPCHelper(location, Banker.npc.getDisplayName(), Profession.NONE, true);
                        nh.spawn();
                    }
                    else if(e.getItem().equals(CustomItems.npcFisher.getCustomItem())){
                        nh = new NPCHelper(location, CustomItems.npcFisher.getDisplayName(), Profession.FISHERMAN, true);
                        nh.spawn();
                    }
                    else if(e.getItem().equals(CustomItems.npcFarmer.getCustomItem())){
                        nh = new NPCHelper(location, CustomItems.npcFarmer.getDisplayName(), Profession.FARMER, true);
                        nh.spawn();
                    }
                    else if(e.getItem().equals(CustomItems.npcSmith.getCustomItem())){
                        nh = new NPCHelper(location, CustomItems.npcSmith.getDisplayName(), Profession.WEAPONSMITH, true);
                        nh.spawn();
                    }
                    else if(e.getItem().equals(CustomItems.npcAdventurer.getCustomItem())){
                        nh = new NPCHelper(location, CustomItems.npcAdventurer.getDisplayName(), Profession.NONE, true);
                        nh.spawn();
                    }
                    else if(e.getItem().equals(CustomItems.npcMiner.getCustomItem())){
                        nh = new NPCHelper(location, CustomItems.npcMiner.getDisplayName(), Profession.NONE, true);
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
                e.setCancelled(true);
            }
            else if(e.getRightClicked().getCustomName() != null && e.getRightClicked().getCustomName().equals(ItemConstants.PLUGIN_ITEM_NPC_FARMER)){
                e.setCancelled(true);
            }
            else if(e.getRightClicked().getCustomName() != null && e.getRightClicked().getCustomName().equals(ItemConstants.PLUGIN_ITEM_NPC_SMITH)){
                e.setCancelled(true);
            }
            else if(e.getRightClicked().getCustomName() != null && e.getRightClicked().getCustomName().equals(ItemConstants.PLUGIN_ITEM_NPC_ADVENTURER)){
                e.setCancelled(true);
            }
            else if(e.getRightClicked().getCustomName() != null && e.getRightClicked().getCustomName().equals(ItemConstants.PLUGIN_ITEM_NPC_MINER)){
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onInventoryClickItem(InventoryClickEvent e) {
        if (e.getView().getTitle().equals(Strings.PLUGIN_PREFIX + Strings.PLUGIN_SPACER + ItemConstants.PLUGIN_ITEM_NPC_BANKER) && e.getCurrentItem() != null && e.getWhoClicked() instanceof Player) {
            Player p = (Player) e.getWhoClicked();
            PlayerEntry pe = RelluEssentials.playerEntryList.get(p.getUniqueId());
            BankAccountEntry bae = RelluEssentials.dBH.getPlayerBankAccount(pe.getID());
            if(e.getCurrentItem().equals(Banker.npc_gui_deposit.getCustomItem())){
                InventoryHelper.closeInventory(p);
                InventoryHelper.openInventory(p, Banker.getDepositGUI());
            }
            else if(e.getCurrentItem().equals(Banker.npc_gui_deposit_5_percent.getCustomItem())){
                float purse = pe.getPurse();
                if(purse >= 1){
                    float transaction_value = (purse / 100)  * 5f;
                    RelluEssentials.dBH.addTransactionToBank(pe.getID(), bae.getId(), transaction_value, bae.getValue(), bae.getTier().getId());
                    pe.setPurse(purse - transaction_value);
                    RelluEssentials.dBH.updatePlayer(pe);
                    InventoryHelper.closeInventory(p);
                }
                else{
                    p.sendMessage("to less money to do a transaction");
                }
            }
            else if(e.getCurrentItem().equals(Banker.npc_gui_deposit_20_percent.getCustomItem())){
                float purse = pe.getPurse();
                if(purse >= 1){
                    float transaction_value = (purse / 100) * 20f;
                    RelluEssentials.dBH.addTransactionToBank(pe.getID(), bae.getId(), transaction_value, bae.getValue(), bae.getTier().getId());
                    pe.setPurse(purse - transaction_value);
                    RelluEssentials.dBH.updatePlayer(pe);
                    InventoryHelper.closeInventory(p);
                }
                else{
                    p.sendMessage("to less money to do a transaction");
                }
            }
            else if(e.getCurrentItem().equals(Banker.npc_gui_deposit_50_percent.getCustomItem())){
                float purse = pe.getPurse();
                if(purse >= 1){
                    float transaction_value = purse / 2;
                    RelluEssentials.dBH.addTransactionToBank(pe.getID(), bae.getId(), transaction_value, bae.getValue(), bae.getTier().getId());
                    pe.setPurse(purse - transaction_value);
                    RelluEssentials.dBH.updatePlayer(pe);
                    InventoryHelper.closeInventory(p);
                }
                else{
                    p.sendMessage("to less money to do a transaction");
                }
            }
            else if(e.getCurrentItem().equals(Banker.npc_gui_deposit_all.getCustomItem())){
                float purse = pe.getPurse();
                if(purse >= 1){
                    float transaction_value = purse;
                    RelluEssentials.dBH.addTransactionToBank(pe.getID(), bae.getId(), transaction_value, bae.getValue(), bae.getTier().getId());
                    pe.setPurse(purse - transaction_value);
                    RelluEssentials.dBH.updatePlayer(pe);
                    InventoryHelper.closeInventory(p);
                }
                else{
                    p.sendMessage("to less money to do a transaction");
                }
            }
            else if(e.getCurrentItem().equals(Banker.npc_gui_withdraw.getCustomItem())){
                InventoryHelper.closeInventory(p);
                InventoryHelper.openInventory(p, Banker.getWithdrawGUI());
            }
            else if(e.getCurrentItem().equals(Banker.npc_gui_balance.getCustomItem())){
                InventoryHelper.closeInventory(p);
                InventoryHelper.openInventory(p, Banker.getBalanceGUI());
            }
            else if(e.getCurrentItem().equals(Banker.npc_gui_balance_total.getCustomItem())){
                InventoryHelper.closeInventory(p);
                p.sendMessage("Your total is: " +  bae.getValue());
            }
            else if(e.getCurrentItem().equals(Banker.npc_gui_balance_transactions.getCustomItem())){
                InventoryHelper.closeInventory(p);
                p.sendMessage("Your transactions are:");

            }
            else if(e.getCurrentItem().equals(Banker.npc_gui_upgrade.getCustomItem())){
                InventoryHelper.closeInventory(p);
                InventoryHelper.openInventory(p, Banker.getUpgradeGUI());
            }
            else if(e.getCurrentItem().equals(CustomItems.npc_gui_close.getCustomItem())){
                InventoryHelper.closeInventory(p);
            }
            e.setCancelled(true);
        }
    }
}
