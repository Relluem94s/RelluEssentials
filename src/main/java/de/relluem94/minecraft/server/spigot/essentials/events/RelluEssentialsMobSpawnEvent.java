package de.relluem94.minecraft.server.spigot.essentials.events;

import de.relluem94.minecraft.server.spigot.essentials.models.mobs.CustomMob;
import de.relluem94.minecraft.server.spigot.essentials.models.mobs.CustomMobDefinition;
import de.relluem94.minecraft.server.spigot.essentials.services.MobService;
import lombok.Getter;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Event fired when a custom mob is spawned through the {@link MobService}.
 *
 * <p>This event is cancellable. If canceled, the mob will be removed and not registered.</p>
 *
 * @author rellu
 * @version 1.0
 * @since 4.6
 */
@Getter
public class RelluEssentialsMobSpawnEvent extends Event implements Cancellable {

  private static final HandlerList HANDLERS = new HandlerList();
  private final CustomMob customMob;
  private final CustomMobDefinition definition;
  private boolean cancelled;

  /**
   * Creates a new mob spawn event.
   *
   * @param customMob  the mob that was spawned
   * @param definition the definition used to spawn the mob
   */
  public RelluEssentialsMobSpawnEvent(@NotNull CustomMob customMob, @NotNull CustomMobDefinition definition) {
    this.customMob = customMob;
    this.definition = definition;
    this.cancelled = false;
  }

  /**
   * Gets the list of handlers registered for this event type.
   *
   * @return the handler list for {@link RelluEssentialsMobSpawnEvent}
   */
  public static HandlerList getHandlerList() {
    return HANDLERS;
  }

  /**
   * Gets the handlers registered for this event instance.
   *
   * @return the handler list associated with this event
   */
  @Override
  public @NotNull HandlerList getHandlers() {
    return HANDLERS;
  }

  /**
   * Sets whether this event is canceled.
   *
   * @param cancelled {@code true} to cancel the spawn, {@code false} otherwise
   */
  @Override
  public void setCancelled(boolean cancelled) {
    this.cancelled = cancelled;
  }
}