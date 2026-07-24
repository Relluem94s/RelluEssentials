package de.relluem94.minecraft.server.spigot.essentials.events;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.constants.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.constants.PlayerState;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.PlayerEntry;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.Lightable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;

import static de.relluem94.minecraft.server.spigot.essentials.RelluEssentials.languageHelper;


public class BetterLights implements Listener {

     

    @EventHandler
    public void onChangeSignCreateActionSign(PlayerInteractEvent e) {
        PlayerEntry pe = RelluEssentials.getInstance().getPlayerAPI().getPlayerEntry(e.getPlayer().getUniqueId());
        Block b = e.getClickedBlock();
        if(pe.getPlayerState().equals(PlayerState.LIGHT_TOGGLE) && b != null && b.getType().equals(Material.REDSTONE_LAMP) && b.getBlockData() instanceof Lightable lightable){
            lightable.setLit(!lightable.isLit());
            b.setBlockData(lightable);
            e.getPlayer().sendMessage(languageHelper.getWithPrefix(MessageKey.PLUGIN_EVENT_LIGHTS_TOGGLE));
            pe.setPlayerState(PlayerState.DEFAULT);
            new BukkitRunnable() {
                @Override
                public void run() {
                    pe.setPlayerState(PlayerState.LIGHT_TOGGLE);
                }
            }.runTaskLater(RelluEssentials.getInstance(), 2);
        }
    }
}