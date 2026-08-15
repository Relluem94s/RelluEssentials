package de.relluem94.minecraft.server.spigot.essentials.commands.admin;

import de.relluem94.minecraft.server.spigot.essentials.commands.Admin;
import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.SubCommand;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.PlayerEntry;
import de.relluem94.minecraft.server.spigot.essentials.npcs.NpcOperationResult;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;

public class NpcCreateCommand implements SubCommand {

  private static final int ARGS_SUBCOMMAND_INDEX = 0;
  private static final int ARGS_ACTION_INDEX = 1;
  private static final int ARGS_PROFILE_INDEX = 2;
  private static final int ARGS_X_INDEX = 3;
  private static final int ARGS_Y_INDEX = 4;
  private static final int ARGS_Z_INDEX = 5;
  private static final int ARGS_YAW_INDEX = 6;
  private static final int ARGS_PITCH_INDEX = 7;
  private static final int REQUIRED_ARGS_LENGTH = 8;

  private final ServiceContext serviceContext;

  public NpcCreateCommand(ServiceContext context) {
    this.serviceContext = context;
  }

  @Override
  public void execute(Player player, String[] args) {
    if (!serviceContext.getGroupService().isSenderAuthorized(player, "admin")) {
      player.sendMessage(serviceContext.getTranslationService()
          .getWithPrefix(MessageKey.COMMAND_PERMISSION_MISSING));
      return;
    }

    if (args.length < REQUIRED_ARGS_LENGTH) {
      player.sendMessage(serviceContext.getTranslationService()
          .getWithPrefix(MessageKey.COMMAND_NPC_CREATE_USAGE));
      return;
    }

    String profileName = args[ARGS_PROFILE_INDEX];
    double x;
    double y;
    double z;
    float yaw;
    float pitch;

    try {
      x = Double.parseDouble(args[ARGS_X_INDEX]);
      y = Double.parseDouble(args[ARGS_Y_INDEX]);
      z = Double.parseDouble(args[ARGS_Z_INDEX]);
      yaw = Float.parseFloat(args[ARGS_YAW_INDEX]);
      pitch = Float.parseFloat(args[ARGS_PITCH_INDEX]);
    } catch (NumberFormatException e) {
      player.sendMessage(
          serviceContext.getTranslationService()
              .getWithPrefix(MessageKey.COMMAND_NPC_INVALID_COORDINATES));
      return;
    }
    PlayerEntry playerEntry = serviceContext.getPlayerService()
        .getPlayerEntry(player.getUniqueId());
    String worldName = player.getWorld().getName();
    NpcOperationResult result = serviceContext.getNpcService()
        .createNPC(profileName, x, y, z, yaw, pitch, worldName, playerEntry.getId());

    if (!result.isSuccessful()) {
      player.sendMessage(
          serviceContext.getTranslationService()
              .getWithPrefix(MessageKey.COMMAND_NPC_OPERATION_FAILED) + " "
              + result.getErrorMessage());
      return;
    }

    player.sendMessage(
        serviceContext.getTranslationService().getWithPrefix(MessageKey.COMMAND_NPC_CREATED));
  }

  @Override
  public boolean matches(String @NonNull [] args) {
    return args.length >= 2
        && Admin.Commands.NPC.getName().equalsIgnoreCase(args[ARGS_SUBCOMMAND_INDEX])
        && "create".equalsIgnoreCase(args[ARGS_ACTION_INDEX]);
  }
}