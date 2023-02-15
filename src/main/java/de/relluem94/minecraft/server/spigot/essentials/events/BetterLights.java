package de.relluem94.minecraft.server.spigot.essentials.events;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.Lightable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.constants.EventConstants;
import de.relluem94.minecraft.server.spigot.essentials.constants.PlayerState;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.PlayerEntry;


public class BetterLights implements Listener {

     

    @EventHandler
    public void onChangeSignCreateActionSign(PlayerInteractEvent e) {
        PlayerEntry pe = RelluEssentials.playerEntryList.get(e.getPlayer().getUniqueId());
        Block b = e.getClickedBlock();
        if(pe.getPlayerState().equals(PlayerState.LIGHT_TOOGLE)){
            if(b != null && b.getType().equals(Material.REDSTONE_LAMP) && b.getBlockData() instanceof Lightable){
                Lightable lightable = (Lightable) b.getBlockData();
                lightable.setLit(!lightable.isLit());
                b.setBlockData(lightable);
                e.getPlayer().sendMessage(EventConstants.PLUGIN_EVENT_LIGHTS_TOOGLE);
                pe.setPlayerState(PlayerState.DEFAULT);
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        pe.setPlayerState(PlayerState.LIGHT_TOOGLE);
                    }
                }.runTaskLater(RelluEssentials.getInstance(), 2);
            }
        }
    }
}
