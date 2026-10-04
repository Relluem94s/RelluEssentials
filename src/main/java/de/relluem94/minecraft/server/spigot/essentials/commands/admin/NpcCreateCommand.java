package de.relluem94.minecraft.server.spigot.essentials.commands.admin;

import de.relluem94.minecraft.server.spigot.essentials.commands.Admin;
import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.SubCommand;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.PlayerEntry;
import de.relluem94.minecraft.server.spigot.essentials.npcs.NpcOperationResult;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;

/**
 * Sub-command implementation that handles the creation of NPCs via the admin command interface. Validates player
 * authorization, parses coordinate arguments, and delegates NPC creation to the
 * {@link de.relluem94.minecraft.server.spigot.essentials.services.NpcService}.
 *
 * @author rellu
 */
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

  /**
   * Creates a new {@code NpcCreateCommand} with the given service context.
   *
   * @param context the {@link ServiceContext} providing access to all required services
   */
  public NpcCreateCommand(ServiceContext context) {
    this.serviceContext = context;
  }

  /**
   * Executes the NPC creation command for the given player. Validates authorization, parses the profile name and
   * coordinates from the argument array, and delegates creation to the NPC service. Sends appropriate feedback messages
   * to the player.
   *
   * @param player the player executing the command
   * @param args   the command arguments containing subcommand, action, profile name, x, y, z coordinates, yaw, and
   *               pitch in that order
   */
  @Override
  public void execute(Player player, String[] args) {
    if (!serviceContext
        .getGroupService()
        .isSenderAuthorized(player, "admin")) {
      player.sendMessage(serviceContext
          .getTranslationService()
          .getWithPrefix(MessageKey.COMMAND_PERMISSION_MISSING));
      return;
    }

    if (args.length < REQUIRED_ARGS_LENGTH) {
      player.sendMessage(serviceContext
          .getTranslationService()
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
      player.sendMessage(serviceContext
          .getTranslationService()
          .getWithPrefix(MessageKey.COMMAND_NPC_INVALID_COORDINATES));
      return;
    }
    PlayerEntry playerEntry = serviceContext
        .getPlayerService()
        .getPlayerEntry(player.getUniqueId());
    String worldName = player
        .getWorld()
        .getName();
    NpcOperationResult result = serviceContext
        .getNpcService()
        .createNpc(profileName, x, y, z, yaw, pitch, worldName, playerEntry.getId());

    if (!result.isSuccessful()) {
      player.sendMessage(serviceContext
          .getTranslationService()
          .getWithPrefix(MessageKey.COMMAND_NPC_OPERATION_FAILED) + " " + serviceContext
          .getTranslationService()
          .get(result
              .getValidationResult()
              .messageKey(), result
              .getValidationResult()
              .params()));
      return;
    }

    player.sendMessage(serviceContext
        .getTranslationService()
        .getWithPrefix(MessageKey.COMMAND_NPC_CREATED));
  }

  /**
   * Determines whether this sub-command matches the given argument array. Returns {@code true} if the arguments contain
   * at least two entries where the first matches the NPC subcommand name and the second equals {@code "create"}.
   *
   * @param args the command arguments to evaluate
   * @return {@code true} if this sub-command is responsible for handling the given arguments, {@code false} otherwise
   */
  @Override
  public boolean matches(String @NonNull [] args) {
    return args.length >= 2 && Admin.Commands.NPC
        .getName()
        .equalsIgnoreCase(args[ARGS_SUBCOMMAND_INDEX]) && "create".equalsIgnoreCase(args[ARGS_ACTION_INDEX]);
  }
}