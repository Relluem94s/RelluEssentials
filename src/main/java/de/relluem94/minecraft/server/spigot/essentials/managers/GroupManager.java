package de.relluem94.minecraft.server.spigot.essentials.managers;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.managers.Enable;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.PlayerEntry;
import java.util.List;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class GroupManager implements Enable {

  @Override
  public void enable(Plugin plugin) {
    RelluEssentials relluEssentialsPlugin = (RelluEssentials) plugin;

    List<PlayerEntry> pel = relluEssentialsPlugin.getServiceContext().getDatabaseHelper().getPlayers();
    pel.forEach(p -> relluEssentialsPlugin.getServiceContext().getPlayerService()
        .putPlayerEntry(UUID.fromString(p.getUuid()), p));

    Bukkit.getOnlinePlayers().forEach(p -> {
      PlayerEntry pe = relluEssentialsPlugin.getServiceContext().getPlayerService().getPlayerEntry(p);
      relluEssentialsPlugin.getServiceContext().getPlayerService().setGroup(p, pe.getGroup());
    });
  }
}
