package de.relluem94.minecraft.server.spigot.essentials.services;

import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.models.Selection;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

public class SelectionService {

  private final ServiceContext serviceContext;

  public SelectionService(ServiceContext serviceContext) {
    this.serviceContext = serviceContext;
  }

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