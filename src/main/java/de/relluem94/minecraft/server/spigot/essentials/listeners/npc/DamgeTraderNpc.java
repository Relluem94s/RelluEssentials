package de.relluem94.minecraft.server.spigot.essentials.listeners.npc;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.context.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.ListenerConstruct;
import de.relluem94.minecraft.server.spigot.essentials.services.GroupService;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.jetbrains.annotations.NotNull;

public class DamgeTraderNpc implements ListenerConstruct {

  GroupService groupService;

  @Override
  public void injectContext(ServiceContext context) {
    this.groupService = context.getGroupService();
  }

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

    if (!groupService.isSenderAuthorized(p, "admin")) {
      e.setCancelled(true);
    }
  }
}
