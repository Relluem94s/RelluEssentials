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

import de.relluem94.minecraft.server.spigot.essentials.CustomItems;
import de.relluem94.minecraft.server.spigot.essentials.Strings;
import de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants;
import de.relluem94.minecraft.server.spigot.essentials.helpers.InventoryHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.NPCHelper;


public class BetterNPC implements Listener {

    @EventHandler
    public void onNPCPlacement(PlayerInteractEvent e) {
        if (e.getHand() != null && e.getHand().equals(EquipmentSlot.HAND)) {
            if (e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.LEFT_CLICK_BLOCK) {
                if(e.getItem() != null && (
                    e.getItem().equals(CustomItems.npcBanker.getCustomItem()) || 
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
                    if(e.getItem().equals(CustomItems.npcBanker.getCustomItem())){
                        nh = new NPCHelper(location, CustomItems.npcBanker.getDisplayName(), Profession.NONE, true);
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
                org.bukkit.inventory.Inventory inv = InventoryHelper.fillInventory(InventoryHelper.createInventory(27, Strings.PLUGIN_PREFIX + Strings.PLUGIN_SPACER + ItemConstants.PLUGIN_ITEM_NPC_BANKER), CustomItems.npc_gui_disabled.getCustomItem());
    
                inv.setItem(0, CustomItems.npcBanker_gui_deposit.getCustomItem());
                inv.setItem(4, CustomItems.npcBanker_gui_withdraw.getCustomItem());
                inv.setItem(8, CustomItems.npcBanker_gui_balance.getCustomItem());
                inv.setItem(26, CustomItems.npcBanker_gui_upgrade.getCustomItem());
    
                InventoryHelper.openInventory(p, inv);
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
        if (e.getView().getTitle().equals(Strings.PLUGIN_PREFIX + Strings.PLUGIN_SPACER + ItemConstants.PLUGIN_ITEM_NPC_BANKER)) {
            if(e.getCurrentItem().equals(CustomItems.npcBanker_gui_deposit.getCustomItem())){
                e.getWhoClicked().closeInventory();
            }
            else if(e.getCurrentItem().equals(CustomItems.npcBanker_gui_withdraw.getCustomItem())){
                e.getWhoClicked().closeInventory();
            }
            else if(e.getCurrentItem().equals(CustomItems.npcBanker_gui_balance.getCustomItem())){
                e.getWhoClicked().closeInventory();
            }
            else if(e.getCurrentItem().equals(CustomItems.npcBanker_gui_upgrade.getCustomItem())){
                e.getWhoClicked().closeInventory();
            }
            e.setCancelled(true);
        }
    }
}
