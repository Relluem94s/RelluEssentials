package de.relluem94.minecraft.server.spigot.essentials.events;

import org.bukkit.Bukkit;
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
import de.relluem94.minecraft.server.spigot.essentials.api.PlayerAPI;
import de.relluem94.minecraft.server.spigot.essentials.constants.ItemConstants;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.LocationEntry;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.LocationTypeEntry;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.PlayerEntry;

import static de.relluem94.minecraft.server.spigot.essentials.RelluEssentials.dBH;
import static de.relluem94.minecraft.server.spigot.essentials.RelluEssentials.locationTypeEntryList;
import static de.relluem94.minecraft.server.spigot.essentials.constants.EventConstants.PLUGIN_EVENT_DEATH;
import static de.relluem94.minecraft.server.spigot.essentials.constants.EventConstants.PLUGIN_EVENT_DEATH_TP;
import static de.relluem94.minecraft.server.spigot.essentials.constants.EventConstants.PLUGIN_EVENT_NO_DEATH_MESSAGE;

public class NoDeathMessage implements Listener {

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        e.setKeepLevel(true);
        e.setDroppedExp(0);
        e.setDeathMessage(null);

        Player p = e.getEntity().getPlayer();

        PlayerEntry pe = PlayerAPI.getPlayerEntry(p.getUniqueId());
        LocationEntry le = new LocationEntry();
        le.setLocation(p.getLocation());
        le.setLocationName(String.format(PLUGIN_EVENT_NO_DEATH_MESSAGE, (int) (Math.random() * 94 - 1) + 1));
        LocationTypeEntry location_type = locationTypeEntryList.get(1);
        le.setLocationType(location_type);
        le.setPlayerId(pe.getID());
        dBH.insertLocation(le);
        le = dBH.getLocation(p.getLocation(), location_type.getId());
        pe.getHomes().add(le);

        for(ItemStack is : p.getInventory().getContents()){
            if(CustomItems.coins.almostEquals(is)){
                if(is.getItemMeta().getPersistentDataContainer().has(ItemConstants.PLUGIN_ITEM_COINS_NAMESPACE, PersistentDataType.INTEGER)){
                    p.getInventory().remove(is);
                }
            }
        }

        p.sendMessage(String.format(PLUGIN_EVENT_DEATH, le.getLocationName(), (int) le.getLocation().getX(), (int) le.getLocation().getY(), (int) le.getLocation().getZ(), le.getLocation().getWorld().getName()));
        Bukkit.getConsoleSender().getServer().dispatchCommand(Bukkit.getConsoleSender(), "tellraw " + p.getName() + " [\"\",{\"text\":\"" + PLUGIN_EVENT_DEATH_TP + "\",\"color\":\"aqua\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/home " + le.getLocationName() + "\"}}]");
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent e) {
        Player p = e.getPlayer();
        PlayerEntry pe = PlayerAPI.getPlayerEntry(p.getUniqueId());
        p.setAllowFlight(pe.isFlying());
        p.setFlying(pe.isFlying());
    }

    @EventHandler
    public void onWorldChange(PlayerChangedWorldEvent e) {
        Player p = e.getPlayer();
        PlayerEntry pe = PlayerAPI.getPlayerEntry(p.getUniqueId());
        p.setAllowFlight(pe.isFlying());
        p.setFlying(pe.isFlying());
    }

    @EventHandler
    public void onWorldChange(PlayerTeleportEvent e) {
        Player p = e.getPlayer();
        PlayerEntry pe = PlayerAPI.getPlayerEntry(p.getUniqueId());
        p.setAllowFlight(pe.isFlying());
        p.setFlying(pe.isFlying());
    }
}
