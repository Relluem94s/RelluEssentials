package de.relluem94.minecraft.server.spigot.essentials.events;

import de.relluem94.rellulib.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import static de.relluem94.minecraft.server.spigot.essentials.Strings.*;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.ChatHelper.sendMessageInChannel;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.StringHelper.replaceColor;
import de.relluem94.minecraft.server.spigot.essentials.permissions.Permission;
import de.relluem94.minecraft.server.spigot.essentials.permissions.Groups;

public class BetterChatFormat implements Listener {

    public static final String VIP_CHANNEL = "#v ";
    public static final String MOD_CHANNEL = "#m ";
    public static final String ADMIN_CHANNEL = "#a ";

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        e.setCancelled(true);
        Player p = e.getPlayer();
        if (Permission.isAuthorized(p, Groups.getGroup("vip").getId())) {
            e.setMessage(StringUtils.replaceSymbols(e.getMessage()));

            if (e.getMessage().startsWith(VIP_CHANNEL)) {
                sendMessageInChannel(e.getMessage(), p, VIP_CHANNEL, Groups.getGroup("vip"));
            } else if (e.getMessage().startsWith(MOD_CHANNEL) && Permission.isAuthorized(p, Groups.getGroup("mod").getId())) {
                sendMessageInChannel(e.getMessage(), p, MOD_CHANNEL, Groups.getGroup("mod"));
            } else if (e.getMessage().startsWith(ADMIN_CHANNEL) && Permission.isAuthorized(p, Groups.getGroup("admin").getId())) {
                sendMessageInChannel(e.getMessage(), p, ADMIN_CHANNEL, Groups.getGroup("admin"));
            } else {
                Bukkit.broadcastMessage(p.getCustomName() + PLUGIN_SPACER + PLUGIN_MESSAGE_COLOR + replaceColor(e.getMessage()));
            }
        } else {
            Bukkit.broadcastMessage(p.getCustomName() + PLUGIN_SPACER + PLUGIN_MESSAGE_COLOR + e.getMessage());
        }
    }
}