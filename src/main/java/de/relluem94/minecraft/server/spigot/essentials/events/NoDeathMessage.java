package de.relluem94.minecraft.server.spigot.essentials.events;

import de.relluem94.minecraft.server.spigot.essentials.CustomItems;
import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.commands.Home;
import de.relluem94.minecraft.server.spigot.essentials.constants.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.LocationEntry;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.LocationTypeEntry;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.PlayerEntry;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

import static de.relluem94.minecraft.server.spigot.essentials.RelluEssentials.languageHelper;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_EVENT_NO_DEATH_MESSAGE;
import static de.relluem94.minecraft.server.spigot.essentials.constants.NamespacedKeyConstants.itemCoins;

public class NoDeathMessage implements Listener {

    private final Random random = new Random();

    @EventHandler
    public void onDeath(@NotNull PlayerDeathEvent e) {
        e.setKeepLevel(true);
        e.setDroppedExp(0);
        e.setDeathMessage(null);

        Player p = e.getEntity();
        Location ploc = p.getLocation();
        Location location = new Location(ploc.getWorld(), ploc.getBlockX(), ploc.getBlockY(), ploc.getBlockZ(), ploc.getYaw(), ploc.getPitch());

        PlayerEntry pe = RelluEssentials.getInstance().getPlayerAPI().getPlayerEntry(p.getUniqueId());
        LocationEntry le = new LocationEntry();
        le.setLocation(location);
        le.setLocationName(String.format(PLUGIN_EVENT_NO_DEATH_MESSAGE, random.nextInt(994)));
        LocationTypeEntry locationType = RelluEssentials.getInstance().locationTypeEntryList.get(1);
        le.setLocationType(locationType);
        le.setPlayerId(pe.getId());

        World world = le.getLocation().getWorld();
        String locationName = le.getLocationName();
        Location leLocation = le.getLocation();

        TextComponent message = new TextComponent(languageHelper.get(MessageKey.PLUGIN_EVENT_DEATH_TP));
        message.setColor(ChatColor.AQUA);
        message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/home " + Home.Commands.TP.getName() + " " + le.getLocationName()));
        p.spigot().sendMessage(message);

        RelluEssentials.getInstance().getDatabaseHelper().insertLocation(le);
        le = RelluEssentials.getInstance().getDatabaseHelper().getLocation(location, locationType.getId());

        if(le != null){
            pe.getHomes().add(le);
        }
       
        for(ItemStack is : p.getInventory().getContents()){
            if(is != null && is.getItemMeta() != null && CustomItems.coins.almostEquals(is) && is.getItemMeta().getPersistentDataContainer().has(itemCoins, PersistentDataType.INTEGER)){
                p.getInventory().remove(is);
            }
        }

        if(world == null){
            return;
        }

        p.sendMessage(languageHelper.getWithPrefix(MessageKey.PLUGIN_EVENT_DEATH, locationName, languageHelper.get(MessageKey.COMMAND_WHERE_STRING, (int) leLocation.getX(), (int) leLocation.getY(), (int) leLocation.getZ(), world.getName())));
    }

    @EventHandler
    public void onRespawn(@NotNull PlayerRespawnEvent e) {
        Player p = e.getPlayer();
        PlayerEntry pe = RelluEssentials.getInstance().getPlayerAPI().getPlayerEntry(p.getUniqueId());
        if(pe != null){
            p.setAllowFlight(pe.isFlying());
            p.setFlying(pe.isFlying());
        }
    }

    @EventHandler
    public void onWorldChange(@NotNull PlayerChangedWorldEvent e) {
        Player p = e.getPlayer();
        PlayerEntry pe = RelluEssentials.getInstance().getPlayerAPI().getPlayerEntry(p.getUniqueId());
        if(pe != null){
            p.setAllowFlight(pe.isFlying());
            p.setFlying(pe.isFlying());
        }
    }

    @EventHandler
    public void onWorldChange(@NotNull PlayerTeleportEvent e) {
        Player p = e.getPlayer();
        PlayerEntry pe = RelluEssentials.getInstance().getPlayerAPI().getPlayerEntry(p.getUniqueId());
        if(pe != null){
            p.setAllowFlight(pe.isFlying());
            p.setFlying(pe.isFlying());
        }
    }
}