package de.relluem94.minecraft.server.spigot.essentials.commands.modify.shared;

import static de.relluem94.minecraft.server.spigot.essentials.RelluEssentials.languageHelper;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.model.Selection;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

public class SelectionResolver {

  public @Nullable Selection resolve(Player player) {
    if (!RelluEssentials.getInstance().position.containsKey(player)) {
      player.sendMessage(languageHelper.getWithPrefix(MessageKey.COMMAND_MODIFY_NO_POSITIONS));
      return null;
    }

    Location pos1 = RelluEssentials.getInstance().position.get(player).getValue();
    Location pos2 = RelluEssentials.getInstance().position.get(player).getSecondValue();

    if (pos1 == null) {
      player.sendMessage(languageHelper.getWithPrefix(MessageKey.COMMAND_MODIFY_POS_1_EMPTY));
      return null;
    }

    if (pos2 == null) {
      player.sendMessage(languageHelper.getWithPrefix(MessageKey.COMMAND_MODIFY_POS_2_EMPTY));
      return null;
    }

    if (pos1.getWorld() != pos2.getWorld()) {
      player.sendMessage(languageHelper.getWithPrefix(MessageKey.COMMAND_MODIFY_DIFFERENT_WORLDS));
      return null;
    }

    return new Selection(pos1, pos2);
  }
}