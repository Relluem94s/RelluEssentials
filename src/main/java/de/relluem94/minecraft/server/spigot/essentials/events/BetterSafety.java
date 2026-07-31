package de.relluem94.minecraft.server.spigot.essentials.events;

import static de.relluem94.minecraft.server.spigot.essentials.RelluEssentials.languageHelper;

import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.helpers.PermissionHelper;
import de.relluem94.minecraft.server.spigot.essentials.registry.GroupRegistry;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class BetterSafety implements Listener {

  private final String[] strings2block = {"/pl", "/bukkit", "/ver"};

  @EventHandler
  public void onType(PlayerCommandPreprocessEvent e) {
    for (String s2b : strings2block) {
      if (!PermissionHelper.isAuthorized(e.getPlayer(), GroupRegistry.getGroup("admin").getId())) {
        if (e.getMessage().toLowerCase().startsWith(s2b)) {
          e.setCancelled(true);
          e.getPlayer()
              .sendMessage(languageHelper.getWithPrefix(MessageKey.COMMAND_PERMISSION_MISSING));
        }
      }
    }
  }
}
