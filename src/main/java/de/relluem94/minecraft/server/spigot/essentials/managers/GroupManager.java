package de.relluem94.minecraft.server.spigot.essentials.managers;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.helpers.PlayerHelper;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.managers.Enable;
import de.relluem94.minecraft.server.spigot.essentials.model.pojo.PlayerEntry;
import java.util.List;
import java.util.UUID;
import org.bukkit.Bukkit;

public class GroupManager implements Enable {

  @Override
  public void enable(RelluEssentials plugin) {
    List<PlayerEntry> pel = plugin.getDatabaseHelper().getPlayers();
    pel.forEach(p -> plugin.getPlayerRegistry()
        .putPlayerEntry(UUID.fromString(p.getUuid()), p));

    Bukkit.getOnlinePlayers().forEach(p -> {
      PlayerEntry pe = plugin.getPlayerRegistry().getPlayerEntry(p);
      PlayerHelper.setGroup(p, pe.getGroup());
    });
  }
}
