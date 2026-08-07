package de.relluem94.minecraft.server.spigot.essentials.commands.admin;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.commands.Admin;
import de.relluem94.minecraft.server.spigot.essentials.context.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.SubCommand;
import de.relluem94.minecraft.server.spigot.essentials.model.pojo.PlayerEntry;
import de.relluem94.minecraft.server.spigot.essentials.npc.NpcOperationResult;
import de.relluem94.minecraft.server.spigot.essentials.services.GroupService;
import de.relluem94.minecraft.server.spigot.essentials.services.TranslationService;
import java.util.UUID;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;

public class NpcDeleteCommand implements SubCommand {

  private static final int ARGS_SUBCOMMAND_INDEX = 0;
  private static final int ARGS_ACTION_INDEX = 1;
  private static final int ARGS_ID_INDEX = 2;
  private static final int REQUIRED_ARGS_LENGTH = 3;

  private final GroupService groupService;
  private final TranslationService translationService;

  public NpcDeleteCommand(ServiceContext context) {
    this.groupService = context.getGroupService();
    this.translationService = context.getTranslationService();
  }

  @Override
  public void execute(Player player, String[] args) {
    if (!groupService.isSenderAuthorized(player, "admin")) {
      player.sendMessage(translationService.getWithPrefix(MessageKey.COMMAND_PERMISSION_MISSING));
      return;
    }

    if (args.length < REQUIRED_ARGS_LENGTH) {
      player.sendMessage(translationService.getWithPrefix(MessageKey.COMMAND_NPC_DELETE_USAGE));
      return;
    }

    UUID npcId;
    try {
      npcId = UUID.fromString(args[ARGS_ID_INDEX]);
    } catch (IllegalArgumentException e) {
      player.sendMessage(translationService.getWithPrefix(MessageKey.COMMAND_NPC_INVALID_ID));
      return;
    }
    PlayerEntry playerEntry = RelluEssentials.getInstance().getPlayerRegistry()
        .getPlayerEntry(player.getUniqueId());
    NpcOperationResult result = RelluEssentials.getInstance().getNpcService()
        .deleteNPC(npcId, playerEntry.getId());

    if (!result.isSuccessful()) {
      player.sendMessage(translationService.getWithPrefix(MessageKey.COMMAND_NPC_NOT_FOUND) + " "
          + result.getErrorMessage());
      return;
    }

    player.sendMessage(translationService.getWithPrefix(MessageKey.COMMAND_NPC_DELETED));
  }

  @Override
  public boolean matches(String @NonNull [] args) {
    return args.length >= 2
        && Admin.Commands.NPC.getName().equalsIgnoreCase(args[ARGS_SUBCOMMAND_INDEX])
        && "delete".equalsIgnoreCase(args[ARGS_ACTION_INDEX]);
  }
}