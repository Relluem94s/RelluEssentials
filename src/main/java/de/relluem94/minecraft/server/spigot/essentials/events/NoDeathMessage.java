package de.relluem94.minecraft.server.spigot.essentials.events;

import static de.relluem94.minecraft.server.spigot.essentials.constants.EventConstants.PLUGIN_EVENT_DEATH;
import static de.relluem94.minecraft.server.spigot.essentials.constants.EventConstants.PLUGIN_EVENT_DEATH_TP;
import static de.relluem94.minecraft.server.spigot.essentials.constants.EventConstants.PLUGIN_EVENT_NO_DEATH_MESSAGE;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import de.relluem94.minecraft.server.spigot.essentials.CustomItems;
import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.LocationEntry;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.LocationTypeEntry;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.PlayerEntry;

public class NoDeathMessage implements Listener {

    private Random random = new Random();

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        e.setKeepLevel(true);
        e.setDroppedExp(0);
        e.setDeathMessage(null);

        Player p = e.getEntity().getPlayer();
        Location ploc = p.getLocation();
        Location location = new Location(ploc.getWorld(), ploc.getBlockX(), ploc.getBlockY(), ploc.getBlockZ(), ploc.getYaw(), ploc.getPitch());

        PlayerEntry pe = RelluEssentials.getInstance().getPlayerAPI().getPlayerEntry(p.getUniqueId());
        LocationEntry le = new LocationEntry();
        le.setLocation(location);
        le.setLocationName(String.format(PLUGIN_EVENT_NO_DEATH_MESSAGE, random.nextInt(994)));
        LocationTypeEntry locationType = RelluEssentials.getInstance().locationTypeEntryList.get(1);
        le.setLocationType(locationType);
        le.setPlayerId(pe.getId());

        p.sendMessage(String.format(PLUGIN_EVENT_DEATH, le.getLocationName(), (int) le.getLocation().getX(), (int) le.getLocation().getY(), (int) le.getLocation().getZ(), le.getLocation().getWorld().getName()));
        Bukkit.getConsoleSender().getServer().dispatchCommand(Bukkit.getConsoleSender(), "tellraw " + p.getName() + " [\"\",{\"text\":\"" + PLUGIN_EVENT_DEATH_TP + "\",\"color\":\"aqua\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/home " + le.getLocationName() + "\"}}]");
        
        RelluEssentials.getInstance().getDatabaseHelper().insertLocation(le);
        le = RelluEssentials.getInstance().getDatabaseHelper().getLocation(location, locationType.getId());

        if(le != null){
            pe.getHomes().add(le);
        }
       
        for(ItemStack is : p.getInventory().getContents()){
            if(CustomItems.coins.almostEquals(is) && is.getItemMeta().getPersistentDataContainer().has(ItemConstants.PLUGIN_ITEM_COINS_NAMESPACE, PersistentDataType.INTEGER)){
                p.getInventory().remove(is);
            }
        }
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent e) {
        Player p = e.getPlayer();
        PlayerEntry pe = RelluEssentials.getInstance().getPlayerAPI().getPlayerEntry(p.getUniqueId());
        if(pe != null){
            p.setAllowFlight(pe.isFlying());
            p.setFlying(pe.isFlying());
        }
    }

    @EventHandler
    public void onWorldChange(PlayerChangedWorldEvent e) {
        Player p = e.getPlayer();
        PlayerEntry pe = RelluEssentials.getInstance().getPlayerAPI().getPlayerEntry(p.getUniqueId());
        if(pe != null){
            p.setAllowFlight(pe.isFlying());
            p.setFlying(pe.isFlying());
        }
    }

    @EventHandler
    public void onWorldChange(PlayerTeleportEvent e) {
        Player p = e.getPlayer();
        PlayerEntry pe = RelluEssentials.getInstance().getPlayerAPI().getPlayerEntry(p.getUniqueId());
        if(pe != null){
            p.setAllowFlight(pe.isFlying());
            p.setFlying(pe.isFlying());
        }
    }
}
