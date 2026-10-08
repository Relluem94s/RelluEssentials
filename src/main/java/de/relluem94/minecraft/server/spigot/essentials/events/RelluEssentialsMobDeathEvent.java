package de.relluem94.minecraft.server.spigot.essentials.events;

import de.relluem94.minecraft.server.spigot.essentials.models.mobs.CustomMob;
import de.relluem94.minecraft.server.spigot.essentials.models.mobs.CustomMobDefinition;
import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Event fired when a custom mob dies.
 *
 * <p>This event is not cancellable since the death already occurred in the Bukkit lifecycle.</p>
 *
 * @author rellu
 * @version 1.0
 * @since 4.6
 */
@Getter
public class RelluEssentialsMobDeathEvent extends Event {

  private static final HandlerList HANDLERS = new HandlerList();
  private final CustomMob customMob;
  private final CustomMobDefinition definition;

  /**
   * Creates a new mob death event.
   *
   * @param customMob  the mob that died
   * @param definition the definition the mob was spawned from
   */
  public RelluEssentialsMobDeathEvent(@NotNull CustomMob customMob, @NotNull CustomMobDefinition definition) {
    this.customMob = customMob;
    this.definition = definition;
  }

  /**
   * Gets the list of handlers registered for this event type.
   *
   * @return the handler list for {@link RelluEssentialsMobDeathEvent}
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
}