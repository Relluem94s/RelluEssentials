package de.relluem94.minecraft.server.spigot.essentials.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;

import de.relluem94.minecraft.server.spigot.essentials.Strings;
import de.relluem94.minecraft.server.spigot.essentials.helpers.PlayerHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.WorldHelper;

public class BetterWorlds implements Listener {

    @EventHandler
    public void onWorldChange(PlayerChangedWorldEvent e){
        Player p = e.getPlayer();
        
        WorldHelper.saveWorldGroupInventory(p, e.getFrom(), true);
        WorldHelper.loadWorldGroupInventory(p);


        if(WorldHelper.isInWorld(p,Strings.PLUGIN_WORLD_LOBBY)){
            PlayerHelper.setLobbyItems(p);
        }
    }
}