package de.relluem94.minecraft.server.spigot.essentials.managers;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.commands.Sudo;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.managers.Disable;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.PlayerEntry;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

/**
 * Manages active sudo sessions for players.
 * Tracks which players are currently in a sudo session and ensures
 * all active sessions are properly terminated on plugin disable.
 */
public class SudoManager implements Disable {

  public static final Map<UUID, PlayerEntry> sudoers = new HashMap<>();

  @Override
  public void disable(Plugin plugin) {
    for (UUID uuid : sudoers.keySet()) {
      Player player = plugin.getServer().getPlayer(uuid);
      if (player == null) {
        continue;
      }
      RelluEssentials relluEssentialsPlugin = (RelluEssentials) plugin;
      Sudo.exitSudo(player, relluEssentialsPlugin.getServiceContext());
    }
  }
}