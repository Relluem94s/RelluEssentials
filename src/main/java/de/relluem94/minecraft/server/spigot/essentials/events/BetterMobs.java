package de.relluem94.minecraft.server.spigot.essentials.events;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

import java.util.ArrayList;
import java.util.List;

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
import de.relluem94.minecraft.server.spigot.essentials.api.PlayerAPI;
import de.relluem94.minecraft.server.spigot.essentials.constants.EntityCoins;
import de.relluem94.minecraft.server.spigot.essentials.constants.EventConstants;
import de.relluem94.minecraft.server.spigot.essentials.constants.PlayerState;
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
        if(RelluEssentials.moneyLostOnDeath){
            Player p = e.getEntity();
            PlayerEntry pe = PlayerAPI.getPlayerEntry(p);
            
            double purse = pe.getPurse();
            double losses = purse / 2;
            if(purse - losses >= 1){
                pe.setPurse(purse - losses);
                p.sendMessage(String.format(EventConstants.PLUGIN_EVENT_PLAYER_DEATH_LOST_COINS, StringHelper.formatDouble(losses)));
            }
            else{
                pe.setPurse(0);
            }

            RelluEssentials.dBH.updatePlayer(pe);
        }
    }

    @EventHandler
    public void onDeath(EntityDeathEvent e) {
        if(e.getEntity().getKiller() != null && e.getEntity().getKiller() instanceof Player){
            int COINS_PER_DEATH = EntityCoins.valueOf(e.getEntity().getType().name()).getCoins();
            if(COINS_PER_DEATH > 0){
                Player p = e.getEntity().getKiller();
                PlayerEntry pe = PlayerAPI.getPlayerEntry(p);
                pe.setPurse(pe.getPurse() + COINS_PER_DEATH);
                RelluEssentials.dBH.updatePlayer(pe);
                p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(String.format(Strings.PLUGIN_COMMAND_PURSE_GAIN, COINS_PER_DEATH, StringHelper.formatDouble(pe.getPurse()))));

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
        if (e.getEntity() instanceof Monster && e.getDamager() instanceof Player) {
            Monster m = (Monster) e.getEntity();
            Player p = (Player) e.getDamager();
            PlayerEntry pe = PlayerAPI.getPlayerEntry(p);
            if(pe.getPlayerState().equals(PlayerState.DAMAGE_INFO)){
                p.sendMessage("Damage: " + e.getDamage() + " Last Damage: " + m.getLastDamage() + " Health: " + m.getHealth()); // TODO add to strings
            }
            if(p.getInventory().getItemInMainHand() != null && p.getInventory().getItemInMainHand().hasItemMeta() &&  p.getInventory().getItemInMainHand().getItemMeta().hasEnchant(CustomEnchants.thunderstrike)){
                m.getLocation().getWorld().strikeLightningEffect(m.getLocation());
                
                
            
            }
        }
    }

}