package de.relluem94.minecraft.server.spigot.essentials.events;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import static de.relluem94.minecraft.server.spigot.essentials.RelluEssentials.blockHistoryList;
import static de.relluem94.minecraft.server.spigot.essentials.RelluEssentials.dBH;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.BlockHistoryEntry;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.LocationEntry;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.LocationTypeEntry;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.PlayerEntry;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

/**
 *
 * @author rellu
 */
public class BlockPlace implements Listener{
        
    @EventHandler
    public void placeBlocks(BlockPlaceEvent e){
        PlayerEntry p = RelluEssentials.playerEntryList.get(e.getPlayer().getUniqueId());
        BlockHistoryEntry bh = new BlockHistoryEntry();
        
        
        
        LocationEntry l = dBH.getLocation(e.getBlock().getLocation(), 4);

        if(l == null){
            l = new LocationEntry();
            l.setLocation(e.getBlock().getLocation());
            LocationTypeEntry lt = new LocationTypeEntry();
            lt.setId(4);
            l.setLocationType(lt);
            l.setPlayerId(1);
            dBH.insertLocation(l);
            l = dBH.getLocation(e.getBlock().getLocation(), 4);
        }        

        bh.setCreatedby(p.getId());
        bh.setMaterial(Material.AIR.name());

        bh.setLocation(l);
        dBH.insertBlockHistory(bh);
    }
}
