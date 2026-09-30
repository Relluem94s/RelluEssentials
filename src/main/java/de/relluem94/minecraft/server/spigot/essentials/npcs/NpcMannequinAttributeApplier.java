package de.relluem94.minecraft.server.spigot.essentials.npcs;

import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import org.bukkit.entity.Mannequin;

/**
 * Applies immutability and interaction attributes to a {@link Mannequin} entity
 * to ensure it behaves as a static, non-interactive NPC.
 *
 * @author rellu
 */
public class NpcMannequinAttributeApplier {

  private final ServiceContext serviceContext;

  /**
   * Creates a new {@code NpcMannequinAttributeApplier} with the given {@link ServiceContext}.
   *
   * @param serviceContext the service context used to access the scheduler service
   */
  public NpcMannequinAttributeApplier(ServiceContext serviceContext) {
    this.serviceContext = serviceContext;
  }

  /**
   * Applies static NPC attributes to the given {@link Mannequin} with a delay of one second.
   *
   * <p>The following attributes are applied:</p>
   * <ul>
   *   <li>Invulnerable – the mannequin cannot receive damage</li>
   *   <li>Non-collidable – the mannequin does not collide with other entities</li>
   *   <li>Cannot pick up items – the mannequin ignores dropped items</li>
   *   <li>Immovable – the mannequin cannot be pushed or displaced</li>
   * </ul>
   *
   * @param mannequin the {@link Mannequin} entity to apply the attributes to
   */
  public void applyAttributes(Mannequin mannequin) {
    serviceContext.getSchedulerService().runTaskLater(() -> {
      mannequin.setInvulnerable(true);
      mannequin.setCollidable(false);
      mannequin.setCanPickupItems(false);
      mannequin.setImmovable(true);
    }, 20L);
  }
}
