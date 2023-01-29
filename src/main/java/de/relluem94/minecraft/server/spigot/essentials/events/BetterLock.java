package de.relluem94.minecraft.server.spigot.essentials.events;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import static de.relluem94.minecraft.server.spigot.essentials.RelluEssentials.dBH;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.LocationEntry;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.LocationTypeEntry;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.PlayerEntry;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.ProtectionEntry;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.Openable;
import org.bukkit.block.data.Bisected.Half;
import org.bukkit.block.data.type.Chest;
import org.bukkit.block.data.type.Door;
import org.bukkit.block.data.type.Furnace;
import org.bukkit.block.data.type.Lectern;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.json.JSONObject;

/**
 *
 * @author rellu
 */
public class BetterLock implements Listener {

    private boolean isLockable(Block b){
        return (
            b.getBlockData() instanceof Openable || 
            b.getBlockData() instanceof Chest ||
            b.getBlockData() instanceof Furnace || 
            b.getBlockData() instanceof Lectern ||
            b.getType().equals(Material.JUKEBOX) || 
            b.getType().equals(Material.CAULDRON) || 
            b.getType().equals(Material.ENCHANTING_TABLE) || 
            b.getType().equals(Material.BEACON) || 
            b.getType().equals(Material.LODESTONE) || 
            b.getType().equals(Material.COMPOSTER) || 
            b.getType().equals(Material.BARREL) || 
            b.getType().equals(Material.SHULKER_BOX)
        );

    }



    @EventHandler
    public void placeBlocks(BlockPlaceEvent e) {
        Block b = e.getBlock();
        if(isLockable(b)){
            PlayerEntry p = RelluEssentials.playerEntryList.get(e.getPlayer().getUniqueId());
            ProtectionEntry bpe = new ProtectionEntry();
    
            LocationEntry l = dBH.getLocation(e.getBlock().getLocation(), 5);
    
            if (l == null) {
                l = new LocationEntry();
                l.setLocation(e.getBlock().getLocation());
                LocationTypeEntry lt = new LocationTypeEntry();
                lt.setId(5);
                l.setLocationType(lt);
                l.setPlayerId(p.getID());

                dBH.insertLocation(l);

                LocationEntry le = dBH.getLocation(e.getBlock().getLocation(), 5);

                bpe.setCreatedby(p.getID());
                bpe.setMaterialName(e.getBlock().getType().name());
                bpe.setLocationEntry(le);
                bpe.setPlayerId(p.getID());

                JSONObject rights = new JSONObject();
                rights.put("IDs", p.getID());
                bpe.setRights(rights);

                JSONObject flags = new JSONObject();
                bpe.setFlags(flags);

                dBH.insertProtection(bpe);
            }
            else{
                // THERE IS A PROTECTION
            }
        }
    }

      
    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        Block b = e.getBlock();
        if(isLockable(b)){
            PlayerEntry p = RelluEssentials.playerEntryList.get(e.getPlayer().getUniqueId());
            ProtectionEntry bpe = dBH.getProtectionByLocation(b.getLocation());

            if (bpe != null) {
               if(bpe.getPlayerId() != p.getID()){
                    e.setCancelled(true);
                    e.getPlayer().sendMessage("Nicht deine Protection");
               }
               else{
                    e.getPlayer().sendMessage("Deine Protection");
                    // DOES NOT DELETE LOCATION.. ERROR
                    dBH.deleteProtection(bpe);
                    // You can procced
               }
            }
            else{
                // THERE IS NO PROTECTION
            }
        }
    }

    @EventHandler
    public void onOpen(PlayerInteractEvent e) {
        Block b = e.getClickedBlock();
       
        if(b != null){
            Location l = b.getLocation();
            if(b.getBlockData() instanceof Openable){
               
                Openable openable = (Openable) b.getBlockData();
                if(openable instanceof Door){
                    Door door = (Door) b.getBlockData();

                    if (door.getHalf().equals(Half.TOP)) {
                        l.add(0, -1, 0);
                    }
                }  

                ProtectionEntry protection = dBH.getProtectionByLocation(l);
                PlayerEntry p = RelluEssentials.playerEntryList.get(e.getPlayer().getUniqueId());
                if (protection != null && p != null) { 
                    if ( protection.getPlayerId() != p.getID()) { 
                        e.setCancelled(true); 
                        e.getPlayer().sendMessage("Test22");
                    }
                    else{
                        e.getPlayer().sendMessage("Test44");
                    }
                }
                else{
                    e.getPlayer().sendMessage("Test66");
                }
            }else{
                if(isLockable(b)){

                    if(b.getBlockData() instanceof Chest){
                        e.getPlayer().sendMessage("22");
                    }

                    if(b.getBlockData() instanceof org.bukkit.block.data.type.Lectern){
                        e.getPlayer().sendMessage("44");
                    }

                    if(b.getBlockData() instanceof org.bukkit.block.data.type.Bamboo){
                        e.getPlayer().sendMessage("66");
                    }

                    if(b.getBlockData() instanceof Furnace){
                        e.getPlayer().sendMessage("88");
                    }


                    ProtectionEntry protection = dBH.getProtectionByLocation(l);
                    PlayerEntry p = RelluEssentials.playerEntryList.get(e.getPlayer().getUniqueId());
                    if (protection != null && p != null) { 
                        if ( protection.getPlayerId() != p.getID()) { 
                            e.setCancelled(true); 
                            e.getPlayer().sendMessage("Test22");
                        }
                        else{
                            e.getPlayer().sendMessage("Test44");
                        }
                    }
                    else{
                        e.getPlayer().sendMessage("Test66");
                    }
                }
                else{
                    e.getPlayer().sendMessage(b.getType().name());
                }
            }
        }
        else{
            e.getPlayer().sendMessage("Test66");
        }
    }
}