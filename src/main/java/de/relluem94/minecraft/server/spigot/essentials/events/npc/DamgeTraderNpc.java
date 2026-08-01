package de.relluem94.minecraft.server.spigot.essentials.events.npc;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.helpers.PermissionHelper;
import de.relluem94.minecraft.server.spigot.essentials.registry.GroupRegistry;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.jetbrains.annotations.NotNull;

public class DamgeTraderNpc implements Listener {

  @EventHandler
  public void onNPCDamage(@NotNull EntityDamageByEntityEvent e) {
    if (!(e.getEntity() instanceof Villager)) {
      return;
    }

    if (e.getEntity().getCustomName() == null) {
      return;
    }

    if (!RelluEssentials.getInstance().getTraderNpcRegistry().getNPCNameList()
        .contains(e.getEntity().getCustomName())) {
      return;
    }

    if (!(e.getDamager() instanceof Player p)) {
      e.setCancelled(true);
      return;
    }

    if (!PermissionHelper.isAuthorized(p, GroupRegistry.getGroup("admin").getId())) {
      e.setCancelled(true);
    }
  }
}
