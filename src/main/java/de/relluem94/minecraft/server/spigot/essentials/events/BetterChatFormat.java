package main.java.de.relluem94.minecraft.server.spigot.essentials.events;

import static main.java.de.relluem94.minecraft.server.spigot.essentials.RelluEssentials.stringHelper;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import static main.java.de.relluem94.minecraft.server.spigot.essentials.Strings.*;
import static main.java.de.relluem94.minecraft.server.spigot.essentials.helpers.StringHelper.replaceColor;
import main.java.de.relluem94.minecraft.server.spigot.essentials.permissions.Permission;
import main.java.de.relluem94.minecraft.server.spigot.essentials.permissions.enums.Groups;

public class BetterChatFormat implements Listener {

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        e.setCancelled(true);        
        Player p = e.getPlayer();
        if (Permission.isAuthorized(p, Groups.VIP.getId())) {
            e.setMessage(stringHelper.replaceSymbols(e.getMessage()));
            Bukkit.broadcastMessage(p.getCustomName() + PLUGIN_SPACER + PLUGIN_MESSAGE_COLOR + replaceColor(e.getMessage()));
        }
        else{
            Bukkit.broadcastMessage(p.getCustomName() + PLUGIN_SPACER + PLUGIN_MESSAGE_COLOR + e.getMessage());
        }
    }
}
