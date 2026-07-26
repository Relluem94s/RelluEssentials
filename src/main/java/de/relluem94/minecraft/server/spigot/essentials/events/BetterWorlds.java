package de.relluem94.minecraft.server.spigot.essentials.events;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.constants.Constants;
import de.relluem94.minecraft.server.spigot.essentials.helpers.PlayerHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.WorldHelper;
import de.relluem94.minecraft.server.spigot.essentials.managers.ScoreBoardManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.jspecify.annotations.NonNull;

public class BetterWorlds implements Listener {

    @EventHandler
    public void onWorldChange(@NonNull PlayerChangedWorldEvent e){
        Player p = e.getPlayer();
        
        WorldHelper.saveWorldGroupInventory(p, e.getFrom(), true);
        WorldHelper.loadWorldGroupInventory(p);

        String newWorld = p.getWorld().getName();
        ScoreBoardManager.setScoreboardVisible(p, RelluEssentials.getInstance().scoreboardShow.contains(newWorld));

        if(WorldHelper.isInWorld(p, Constants.PLUGIN_WORLD_LOBBY)){
            PlayerHelper.setLobbyItems(p);
        }
    }
}