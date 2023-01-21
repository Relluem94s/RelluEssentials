package de.relluem94.minecraft.server.spigot.essentials.events;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Score;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;

public class BetterMobs implements Listener {

    @EventHandler
    public void onSpawn(CreatureSpawnEvent e) {
        EntityType et = e.getEntity().getType();
        if (et == EntityType.PHANTOM) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onDeath(EntityDeathEvent e) {
        if(e.getEntity().getKiller() != null && e.getEntity().getKiller() instanceof Player){
            Player p = e.getEntity().getKiller();
            p.sendMessage("COINS");
            Score coins = RelluEssentials.objective.getScore(p.getName());
            //coins.
            coins.setScore(coins.getScore() + 10);
            RelluEssentials.objective.setDisplaySlot(DisplaySlot.SIDEBAR);
            // GEN COINS
        }
    }
}
