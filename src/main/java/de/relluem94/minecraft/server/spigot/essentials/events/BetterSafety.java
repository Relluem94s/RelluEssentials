package de.relluem94.minecraft.server.spigot.essentials.events;

import de.relluem94.minecraft.server.spigot.essentials.constants.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.permissions.Groups;
import de.relluem94.minecraft.server.spigot.essentials.permissions.Permission;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import static de.relluem94.minecraft.server.spigot.essentials.RelluEssentials.languageHelper;

public class BetterSafety implements Listener {

    private final String[] strings2block = {"/pl", "/bukkit", "/ver"};

    @EventHandler
    public void onType(PlayerCommandPreprocessEvent e) {
        for (String s2b : strings2block) {
            if (!Permission.isAuthorized(e.getPlayer(), Groups.getGroup("admin").getId())) {
                if (e.getMessage().toLowerCase().startsWith(s2b)) {
                    e.setCancelled(true);
                    e.getPlayer().sendMessage(languageHelper.getWithPrefix(MessageKey.COMMAND_PERMISSION_MISSING));
                }
            }
        }
    }
}
