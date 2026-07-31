package de.relluem94.minecraft.server.spigot.essentials.commands.admin;

import static de.relluem94.minecraft.server.spigot.essentials.RelluEssentials.languageHelper;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.commands.Admin;
import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.helpers.PermissionHelper;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.SubCommand;
import de.relluem94.minecraft.server.spigot.essentials.model.pojo.PlayerEntry;
import de.relluem94.minecraft.server.spigot.essentials.npc.NpcOperationResult;
import de.relluem94.minecraft.server.spigot.essentials.registry.GroupRegistry;
import java.util.UUID;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;

public class NpcUpdateCommand implements SubCommand {

  private static final int ARGS_SUBCOMMAND_INDEX = 0;
  private static final int ARGS_ACTION_INDEX = 1;
  private static final int ARGS_ID_INDEX = 2;
  private static final int ARGS_FIELD_INDEX = 3;
  private static final int ARGS_PROFILE_VALUE_INDEX = 4;
  private static final int ARGS_X_INDEX = 4;
  private static final int ARGS_Y_INDEX = 5;
  private static final int ARGS_Z_INDEX = 6;
  private static final int REQUIRED_ARGS_PROFILE_LENGTH = 5;
  private static final int REQUIRED_ARGS_POSITION_LENGTH = 7;

  @Override
  public void execute(Player player, String[] args) {
    if (!PermissionHelper.isAuthorized(player, GroupRegistry.getGroup("admin").getId())) {
      player.sendMessage(languageHelper.getWithPrefix(MessageKey.COMMAND_PERMISSION_MISSING));
      return;
    }

    if (args.length < REQUIRED_ARGS_PROFILE_LENGTH) {
      player.sendMessage(languageHelper.getWithPrefix(MessageKey.COMMAND_NPC_UPDATE_USAGE));
      return;
    }

    UUID npcId;
    try {
      npcId = UUID.fromString(args[ARGS_ID_INDEX]);
    } catch (IllegalArgumentException e) {
      player.sendMessage(languageHelper.getWithPrefix(MessageKey.COMMAND_NPC_INVALID_ID));
      return;
    }

    String field = args[ARGS_FIELD_INDEX];

    if ("profile".equalsIgnoreCase(field)) {
      handleProfileUpdate(player, npcId, args);
    } else if ("position".equalsIgnoreCase(field)) {
      handlePositionUpdate(player, npcId, args);
    } else {
      player.sendMessage(languageHelper.getWithPrefix(MessageKey.COMMAND_NPC_UPDATE_USAGE));
    }
  }

  private void handleProfileUpdate(Player player, UUID npcId, String[] args) {
    if (args.length < REQUIRED_ARGS_PROFILE_LENGTH) {
      player.sendMessage(languageHelper.getWithPrefix(MessageKey.COMMAND_NPC_UPDATE_USAGE));
      return;
    }
    String newProfile = args[ARGS_PROFILE_VALUE_INDEX];
    PlayerEntry playerEntry = RelluEssentials.getInstance().getPlayerRegistry()
        .getPlayerEntry(player.getUniqueId());
    NpcOperationResult result = RelluEssentials.getInstance().getNpcService()
        .updateNPCProfile(npcId, newProfile, playerEntry.getId());
    sendOperationFeedback(player, result, MessageKey.COMMAND_NPC_UPDATED,
        MessageKey.COMMAND_NPC_OPERATION_FAILED);
  }

  private void handlePositionUpdate(Player player, UUID npcId, String[] args) {
    if (args.length < REQUIRED_ARGS_POSITION_LENGTH) {
      player.sendMessage(languageHelper.getWithPrefix(MessageKey.COMMAND_NPC_UPDATE_USAGE));
      return;
    }
    double x;
    double y;
    double z;
    try {
      x = Double.parseDouble(args[ARGS_X_INDEX]);
      y = Double.parseDouble(args[ARGS_Y_INDEX]);
      z = Double.parseDouble(args[ARGS_Z_INDEX]);
    } catch (NumberFormatException e) {
      player.sendMessage(languageHelper.getWithPrefix(MessageKey.COMMAND_NPC_INVALID_COORDINATES));
      return;
    }

    PlayerEntry playerEntry = RelluEssentials.getInstance().getPlayerRegistry()
        .getPlayerEntry(player.getUniqueId());
    NpcOperationResult result = RelluEssentials.getInstance().getNpcService()
        .updateNPCPosition(npcId, x, y, z, playerEntry.getId());
    sendOperationFeedback(player, result, MessageKey.COMMAND_NPC_UPDATED,
        MessageKey.COMMAND_NPC_OPERATION_FAILED);
  }

  private void sendOperationFeedback(Player player, NpcOperationResult result,
      MessageKey successKey, MessageKey failureKey) {
    if (!result.isSuccessful()) {
      player.sendMessage(languageHelper.getWithPrefix(failureKey) + " " + result.getErrorMessage());
      return;
    }
    player.sendMessage(languageHelper.getWithPrefix(successKey));
  }

  @Override
  public boolean matches(String @NonNull [] args) {
    return args.length >= 2
        && Admin.Commands.NPC.getName().equalsIgnoreCase(args[ARGS_SUBCOMMAND_INDEX])
        && "update".equalsIgnoreCase(args[ARGS_ACTION_INDEX]);
  }
}