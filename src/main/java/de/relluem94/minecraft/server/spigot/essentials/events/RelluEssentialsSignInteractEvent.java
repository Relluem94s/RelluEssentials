package de.relluem94.minecraft.server.spigot.essentials.events;

import de.relluem94.minecraft.server.spigot.essentials.models.RelluEssentialsNamespacedKey;
import de.relluem94.minecraft.server.spigot.essentials.models.SignAction;
import lombok.Getter;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Event fired when a player interacts with a RelluEssentials managed sign.
 *
 * <p>This event exposes the interacting player, the clicked block, the associated action key,
 * the resolved sign action and any custom input supplied by the player.</p>
 *
 * @author rellu
 * @version 1.0
 * @since 4.4
 */
@Getter
public class RelluEssentialsSignInteractEvent extends Event {

  private static final HandlerList HANDLERS = new HandlerList();
  private final Player player;
  private final Block clickedBlock;
  private final RelluEssentialsNamespacedKey actionKey;
  private final SignAction signAction;
  private final String customInput;

  /**
   * Creates a new sign interaction event for the given player and sign context.
   *
   * @param player       the player who triggered the sign interaction
   * @param clickedBlock the block that was clicked, expected to represent the sign
   * @param actionKey    the namespaced key that identifies the configured sign action
   * @param signAction   the resolved sign action to execute, may be {@code null} if not defined
   * @param customInput  optional custom input provided by the player, may be {@code null}
   */
  public RelluEssentialsSignInteractEvent(@NotNull Player player, @NotNull Block clickedBlock,
      @NotNull RelluEssentialsNamespacedKey actionKey, SignAction signAction, String customInput) {
    this.player = player;
    this.clickedBlock = clickedBlock;
    this.actionKey = actionKey;
    this.signAction = signAction;
    this.customInput = customInput;
  }

  /**
   * Gets the list of handlers registered for this event type.
   *
   * @return the handler list for {@link RelluEssentialsSignInteractEvent}
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