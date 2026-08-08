package de.relluem94.minecraft.server.spigot.essentials.listeners.npc;

import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.ListenerConstruct;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.jetbrains.annotations.NotNull;

public class DamgeTraderNpc implements ListenerConstruct {

  private ServiceContext serviceContext;

  @Override
  public void injectContext(ServiceContext context) {
    this.serviceContext = context;
  }

  @EventHandler
  public void onNPCDamage(@NotNull EntityDamageByEntityEvent e) {
    if (!(e.getEntity() instanceof Villager)) {
      return;
    }

    if (e.getEntity().getCustomName() == null) {
      return;
    }

    if (!serviceContext.getTraderNpcService().getNpcNames()
        .contains(e.getEntity().getCustomName())) {
      return;
    }

    if (!(e.getDamager() instanceof Player p)) {
      e.setCancelled(true);
      return;
    }

    if (!serviceContext.getGroupService().isSenderAuthorized(p, "admin")) {
      e.setCancelled(true);
    }
  }
}
