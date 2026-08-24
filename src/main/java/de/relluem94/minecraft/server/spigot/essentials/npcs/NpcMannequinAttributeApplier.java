package de.relluem94.minecraft.server.spigot.essentials.npcs;

import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import org.bukkit.entity.Mannequin;

public class NpcMannequinAttributeApplier {

  private final ServiceContext serviceContext;

  public NpcMannequinAttributeApplier(ServiceContext serviceContext) {
    this.serviceContext = serviceContext;
  }

  public void applyAttributes(Mannequin mannequin) {
    serviceContext.getSchedulerService().runTaskLater(() -> {
      mannequin.setInvulnerable(true);
      mannequin.setCollidable(false);
      mannequin.setCanPickupItems(false);
      mannequin.setImmovable(true);
    }, 20L);
  }
}
