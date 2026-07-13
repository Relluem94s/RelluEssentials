package de.relluem94.minecraft.server.spigot.essentials.events;

import de.relluem94.minecraft.server.spigot.essentials.commands.Sudo;
import de.relluem94.minecraft.server.spigot.essentials.constants.Constants;
import de.relluem94.minecraft.server.spigot.essentials.constants.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.helpers.PlayerHelper;
import de.relluem94.minecraft.server.spigot.essentials.managers.SudoManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Objects;

import static de.relluem94.minecraft.server.spigot.essentials.RelluEssentials.languageHelper;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.TeleportHelper.teleportWorld;


public class BetterPlayerQuit implements Listener {

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        e.setQuitMessage(null);
        Player p = e.getPlayer();

        if(SudoManager.sudoers.containsKey(p.getUniqueId())){
            Sudo.exitSudo(Objects.requireNonNull(Bukkit.getPlayer(p.getUniqueId())));
        }

        PlayerHelper.savePlayer(p);
        
        Bukkit.broadcastMessage(languageHelper.get(MessageKey.PLUGIN_EVENT_QUIT_MESSAGE, p.getCustomName()));
        teleportWorld(p, Constants.PLUGIN_WORLD_LOBBY, true);
    }
}