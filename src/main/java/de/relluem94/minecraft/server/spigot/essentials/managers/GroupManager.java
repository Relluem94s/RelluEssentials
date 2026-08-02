package de.relluem94.minecraft.server.spigot.essentials.managers;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.helpers.PlayerHelper;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.managers.Enable;
import de.relluem94.minecraft.server.spigot.essentials.model.pojo.PlayerEntry;
import java.util.List;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class GroupManager implements Enable {

  @Override
  public void enable(Plugin plugin) {
    RelluEssentials relluEssentialsPlugin = (RelluEssentials) plugin;

    List<PlayerEntry> pel = relluEssentialsPlugin.getDatabaseHelper().getPlayers();
    pel.forEach(p -> relluEssentialsPlugin.getPlayerRegistry()
        .putPlayerEntry(UUID.fromString(p.getUuid()), p));

    Bukkit.getOnlinePlayers().forEach(p -> {
      PlayerEntry pe = relluEssentialsPlugin.getPlayerRegistry().getPlayerEntry(p);
      PlayerHelper.setGroup(p, pe.getGroup());
    });
  }
}
