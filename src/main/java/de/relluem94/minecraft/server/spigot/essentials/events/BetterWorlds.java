package de.relluem94.minecraft.server.spigot.essentials.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;

import de.relluem94.minecraft.server.spigot.essentials.Strings;
import de.relluem94.minecraft.server.spigot.essentials.helpers.WorldHelper;
import de.relluem94.minecraft.server.spigot.essentials.items.RelluSword;

public class BetterWorlds implements Listener {

    @EventHandler
    public void onWorldChange(PlayerChangedWorldEvent e){
        Player p = e.getPlayer();
        
        WorldHelper.saveWorldGroupInventory(p, e.getFrom());
        WorldHelper.loadWorldGroupInventory(p);


        p.sendMessage(e.getFrom().getName() + " " + p.getWorld().getName());
        if(WorldHelper.isInWorld(p,Strings.PLUGIN_WORLD_LOBBY)){
            p.getInventory().addItem(new RelluSword().getCustomItem());
        }
    }
}