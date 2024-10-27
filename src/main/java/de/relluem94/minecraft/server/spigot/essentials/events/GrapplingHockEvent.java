package de.relluem94.minecraft.server.spigot.essentials.events;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerFishEvent.State;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.constants.Constants;

import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_WORLD_LOBBY;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.WorldHelper.isInWorld;

public class GrapplingHockEvent implements Listener {

    protected static final List<Player> COOL_DOWN = new ArrayList<>();

    @EventHandler
    public void grapple(PlayerFishEvent e){
        if(!isInWorld(e.getPlayer(), PLUGIN_WORLD_LOBBY)){
            return;
        }

        // TODO Check auf item

        if(e.getState().equals(State.IN_GROUND) || e.getState().equals(State.REEL_IN)){
            if(!COOL_DOWN.contains(e.getPlayer())){
                Location hookLocation = e.getHook().getLocation();
                Location playerLocation = e.getPlayer().getLocation();

                Vector playerVelocity = new Vector(
                    hookLocation.getX()-playerLocation.getX(), 
                    (hookLocation.getY()-playerLocation.getY()) +0.001,  
                    hookLocation.getZ()-playerLocation.getZ()
                );

                e.getPlayer().setVelocity(playerVelocity);
                COOL_DOWN.add(e.getPlayer());
                
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        COOL_DOWN.remove(e.getPlayer());
                    }
                }.runTaskLater(RelluEssentials.getInstance(), 50L);
            }
            else{
                e.getPlayer().sendMessage(Constants.PLUGIN_GRAPPLING_HOOK_COOLDOWN);
            }
            
        }
    }
}