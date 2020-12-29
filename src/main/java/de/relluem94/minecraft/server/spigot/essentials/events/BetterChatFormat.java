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

    public static final String VIP_CHANNEL = "#v ";
    public static final String MOD_CHANNEL = "#m ";
    public static final String ADMIN_CHANNEL = "#a ";

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        e.setCancelled(true);
        Player p = e.getPlayer();
        if (Permission.isAuthorized(p, Groups.VIP.getId())) {
            e.setMessage(stringHelper.replaceSymbols(e.getMessage()));

            
            if (e.getMessage().startsWith(VIP_CHANNEL)) {
                channel(e.getMessage(), p, VIP_CHANNEL, Groups.VIP);
            }
            else if (e.getMessage().startsWith(MOD_CHANNEL) && Permission.isAuthorized(p, Groups.MOD.getId())) {
                channel(e.getMessage(), p, MOD_CHANNEL, Groups.MOD);
            }
            else if (e.getMessage().startsWith(ADMIN_CHANNEL) && Permission.isAuthorized(p, Groups.ADMIN.getId())) {
                channel(e.getMessage(), p, ADMIN_CHANNEL, Groups.ADMIN);
            }
            else {
                Bukkit.broadcastMessage(p.getCustomName() + PLUGIN_SPACER + PLUGIN_MESSAGE_COLOR + replaceColor(e.getMessage()));
            }
        } else {
            Bukkit.broadcastMessage(p.getCustomName() + PLUGIN_SPACER + PLUGIN_MESSAGE_COLOR + e.getMessage());
        }
    }

    private void channel(String message, Player p, String channel, Groups group) {
        message = message.replaceFirst(channel, "");
        for (Player op : Bukkit.getOnlinePlayers()) {
            if (Permission.isAuthorized(op, group.getId())) {
                op.sendMessage(p.getCustomName() + group.getPrefix() + PLUGIN_SPACER_CHANNEL + PLUGIN_MESSAGE_COLOR + replaceColor(message));
            }
        }
    }
}
