package de.relluem94.minecraft.server.spigot.essentials.events;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Creeper;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

import de.relluem94.minecraft.server.spigot.essentials.CustomEnchants;
import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.Strings;
import de.relluem94.minecraft.server.spigot.essentials.constants.EntityCoins;
import de.relluem94.minecraft.server.spigot.essentials.constants.EventConstants;
import de.relluem94.minecraft.server.spigot.essentials.constants.PlayerState;
import de.relluem94.minecraft.server.spigot.essentials.helpers.BagHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.ChatHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.StringHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.PlayerEntry;

public class BetterMobs implements Listener {

    @EventHandler
    public void onSpawn(CreatureSpawnEvent e) {
        EntityType et = e.getEntity().getType();
        if (et == EntityType.PHANTOM) {
            e.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onKill(PlayerDeathEvent e) {
        if(RelluEssentials.MONEY_LOST_ON_DEATH){
            Player p = e.getEntity();
            PlayerEntry pe = RelluEssentials.getInstance().getPlayerAPI().getPlayerEntry(p);
            
            double purse = pe.getPurse();
            double losses = purse / 2;
            if(purse - losses >= 1){
                pe.setPurse(purse - losses);
                p.sendMessage(String.format(EventConstants.PLUGIN_EVENT_PLAYER_DEATH_LOST_COINS, StringHelper.formatDouble(losses)));
            }
            else{
                pe.setPurse(0);
            }

            pe.setUpdatedBy(pe.getID());
            pe.setToBeUpdated(true);
        }
    }

    @EventHandler
    public void onDeath(EntityDeathEvent e) {
        if(e.getEntity().getKiller() instanceof Player){
            int coinsPerDeath = EntityCoins.valueOf(e.getEntity().getType().name()).getCoins();
            if(coinsPerDeath > 0){
                Player p = e.getEntity().getKiller();
                PlayerEntry pe = RelluEssentials.getInstance().getPlayerAPI().getPlayerEntry(p);
                pe.setPurse(pe.getPurse() + coinsPerDeath);
                pe.setUpdatedBy(pe.getID());
                pe.setToBeUpdated(true);
                ChatHelper.sendMessageInActionBar(p, String.format(Strings.PLUGIN_COMMAND_PURSE_GAIN, coinsPerDeath, StringHelper.formatDouble(pe.getPurse())));


                if(BagHelper.hasBags(pe.getID())){
                    List<ItemStack> li = new ArrayList<>();
                    li.addAll(e.getDrops());
                    e.getDrops().removeAll(BagHelper.collectItemStacks(li, p, pe));
                }


                if(p.getInventory().getItemInMainHand() != null && p.getInventory().getItemInMainHand().hasItemMeta() &&  p.getInventory().getItemInMainHand().getItemMeta().hasEnchant(CustomEnchants.telekinesis)){
                    List<ItemStack> lis = new ArrayList<>();
                    for(ItemStack is: e.getDrops()){
                        if(p.getInventory().firstEmpty() != -1){
                            p.getInventory().addItem(is);
                            lis.add(is);
                        }
                    }
                    e.getDrops().removeAll(lis);
                }                
            }
        }
    }

    @EventHandler
    public void onHit(EntityDamageByEntityEvent e) {
        if(e.getDamager() instanceof Creeper){
            e.setCancelled(true);
        }
        if (e.getEntity() instanceof Monster && e.getDamager() instanceof Player) {
            Monster m = (Monster) e.getEntity();
            Player p = (Player) e.getDamager();
            PlayerEntry pe = RelluEssentials.getInstance().getPlayerAPI().getPlayerEntry(p);
            if(pe.getPlayerState().equals(PlayerState.DAMAGE_INFO)){
                p.sendMessage(String.format(EventConstants.PLUGIN_EVENT_DAMAGE_SHOW, e.getDamage(), m.getLastDamage(), m.getHealth()));
            }
            if(p.getInventory().getItemInMainHand() != null && p.getInventory().getItemInMainHand().hasItemMeta() &&  p.getInventory().getItemInMainHand().getItemMeta().hasEnchant(CustomEnchants.thunderstrike)){
                m.getLocation().getWorld().strikeLightningEffect(m.getLocation());
            }
        }
    }

}