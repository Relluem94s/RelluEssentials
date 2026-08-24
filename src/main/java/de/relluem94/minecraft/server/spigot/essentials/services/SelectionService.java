package de.relluem94.minecraft.server.spigot.essentials.services;

import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.models.Selection;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

/**
 * Service responsible for resolving player selections based on their stored positions.
 */
public class SelectionService {

  private final ServiceContext serviceContext;

  /**
   * Creates a new SelectionService.
   *
   * @param serviceContext The service context providing access to other services.
   */
  public SelectionService(ServiceContext serviceContext) {
    this.serviceContext = serviceContext;
  }

  /**
   * Attempts to resolve a selection for the given player.
   *
   * <p>This method validates that the player has stored positions, that both positions are set,
   * and that both positions belong to the same world. If any validation fails, a message
   * is sent to the player and {@code null} is returned.
   *
   * @param player The player for whom to resolve the selection.
   * @return The resolved {@link Selection} if valid, or {@code null} if validation fails.
   */
  public @Nullable Selection resolve(Player player) {
    if (!serviceContext.getPositionService().hasPositions(player)) {
      player.sendMessage(serviceContext.getTranslationService().getWithPrefix(MessageKey.COMMAND_MODIFY_NO_POSITIONS));
      return null;
    }

    Location pos1 = serviceContext.getPositionService().getPositions(player).getValue();
    Location pos2 = serviceContext.getPositionService().getPositions(player).getSecondValue();

    if (pos1 == null) {
      player.sendMessage(serviceContext.getTranslationService().getWithPrefix(MessageKey.COMMAND_MODIFY_POS_1_EMPTY));
      return null;
    }

    if (pos2 == null) {
      player.sendMessage(serviceContext.getTranslationService().getWithPrefix(MessageKey.COMMAND_MODIFY_POS_2_EMPTY));
      return null;
    }

    if (pos1.getWorld() != pos2.getWorld()) {
      player.sendMessage(
          serviceContext.getTranslationService().getWithPrefix(MessageKey.COMMAND_MODIFY_DIFFERENT_WORLDS));
      return null;
    }

    return new Selection(pos1, pos2);
  }
}