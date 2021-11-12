package de.relluem94.minecraft.server.spigot.essentials.events;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.BlockHistoryEntry;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import de.relluem94.minecraft.server.spigot.essentials.permissions.Permission;
import de.relluem94.minecraft.server.spigot.essentials.permissions.Groups;
import static de.relluem94.minecraft.server.spigot.essentials.RelluEssentials.dBH;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.LocationEntry;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.LocationTypeEntry;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.PlayerEntry;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Bukkit;

public class BetterBlockDrop implements Listener {
    
    private final Material[] ores = {
        Material.DIAMOND_ORE,
        Material.LAPIS_ORE,
        Material.REDSTONE_ORE,
        Material.COAL_ORE,
        Material.IRON_ORE,
        Material.GOLD_ORE,
        Material.EMERALD_ORE,
        Material.NETHER_GOLD_ORE,
        Material.NETHER_QUARTZ_ORE
    };
    
    private final Material[] blocks2Drop = {
        Material.GLASS,
        Material.GLASS_PANE,
        Material.BLACK_STAINED_GLASS,
        Material.BLACK_STAINED_GLASS_PANE,
        Material.BLUE_STAINED_GLASS,
        Material.BLUE_STAINED_GLASS_PANE,
        Material.BROWN_STAINED_GLASS,
        Material.BROWN_STAINED_GLASS_PANE,
        Material.CYAN_STAINED_GLASS,
        Material.CYAN_STAINED_GLASS_PANE,
        Material.GRAY_STAINED_GLASS,
        Material.GRAY_STAINED_GLASS_PANE,
        Material.GREEN_STAINED_GLASS,
        Material.GREEN_STAINED_GLASS_PANE,
        Material.LIGHT_BLUE_STAINED_GLASS,
        Material.LIGHT_BLUE_STAINED_GLASS_PANE,
        Material.LIGHT_GRAY_STAINED_GLASS,
        Material.LIGHT_GRAY_STAINED_GLASS_PANE,
        Material.LIME_STAINED_GLASS,
        Material.LIME_STAINED_GLASS_PANE,
        Material.MAGENTA_STAINED_GLASS,
        Material.MAGENTA_STAINED_GLASS_PANE,
        Material.ORANGE_STAINED_GLASS,
        Material.ORANGE_STAINED_GLASS_PANE,
        Material.PINK_STAINED_GLASS,
        Material.PINK_STAINED_GLASS_PANE,
        Material.PURPLE_STAINED_GLASS,
        Material.PURPLE_STAINED_GLASS_PANE,
        Material.RED_STAINED_GLASS,
        Material.RED_STAINED_GLASS_PANE,
        Material.WHITE_STAINED_GLASS,
        Material.WHITE_STAINED_GLASS_PANE,
        Material.YELLOW_STAINED_GLASS,
        Material.YELLOW_STAINED_GLASS_PANE
    };

    public void runLater(Runnable r, long d){
        Bukkit.getScheduler().runTaskLater(RelluEssentials.getInstance(), r, d);
    }
    
    
    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        Material m = e.getBlock().getBlockData().getMaterial();
        
        e.getPlayer().sendMessage("broke: " + m.name());
        
        for(Material ore: ores){
            if(m == ore && RelluEssentials.isOreRespawnEnabled){
                runLater(() -> {
                     e.getPlayer().sendMessage("set: " + m.name());
                    e.getBlock().setType(m);
                }, 10000L);
                break;
            }
        }
        
        
        for (Material b2d : blocks2Drop) {
            if (m == b2d) {
                ItemStack is = new ItemStack(b2d, 1);
                Player p = (Player) e.getPlayer();
                if (Permission.isAuthorized(p, Groups.getGroup("user").getId())) {
                    p.getWorld().dropItem(e.getBlock().getLocation(), is);
                    e.setDropItems(false);
                }
            }
        }
        PlayerEntry p = RelluEssentials.playerEntryList.get(e.getPlayer().getUniqueId());
        BlockHistoryEntry bh = new BlockHistoryEntry();

        LocationEntry l = dBH.getLocation(e.getBlock().getLocation(), 4);
        if (l == null) {
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
        bh.setMaterial(e.getBlock().getType().name());

        bh.setLocation(l);
        dBH.insertBlockHistory(bh);
    }
}
