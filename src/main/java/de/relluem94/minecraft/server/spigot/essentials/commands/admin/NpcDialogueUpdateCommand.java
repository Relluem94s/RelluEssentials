package de.relluem94.minecraft.server.spigot.essentials.commands.admin;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.commands.Admin;
import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.helpers.StringHelper;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.SubCommand;
import de.relluem94.minecraft.server.spigot.essentials.models.Npc;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.NpcDialogueEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.PlayerEntry;
import de.relluem94.minecraft.server.spigot.essentials.services.GroupService;
import de.relluem94.minecraft.server.spigot.essentials.services.TranslationService;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;

public class NpcDialogueUpdateCommand implements SubCommand {

  private static final int ARGS_SUBCOMMAND_INDEX = 0;
  private static final int ARGS_ACTION_INDEX = 1;
  private static final int ARGS_DIALOGUE_ACTION_INDEX = 2;
  private static final int ARGS_NPC_ID_INDEX = 3;
  private static final int ARGS_LIST_POSITION_INDEX = 4;
  private static final int ARGS_TEXT_START_INDEX = 5;
  private static final int REQUIRED_ARGS_LENGTH = 6;

  private final GroupService groupService;
  private final TranslationService translationService;

  public NpcDialogueUpdateCommand(ServiceContext context) {
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
      player.sendMessage(
          translationService.getWithPrefix(MessageKey.COMMAND_NPC_DIALOGUE_UPDATE_USAGE));
      return;
    }

    UUID npcId;
    int listPosition;

    try {
      npcId = UUID.fromString(args[ARGS_NPC_ID_INDEX]);
      listPosition = Integer.parseInt(args[ARGS_LIST_POSITION_INDEX]);
    } catch (IllegalArgumentException e) {
      player.sendMessage(translationService.getWithPrefix(MessageKey.COMMAND_INVALID));
      return;
    }

    Optional<Npc> npc = RelluEssentials.getInstance().getNpcService().getNPCById(npcId);
    npc.ifPresentOrElse(foundNpc -> {
      String text = Arrays.stream(args, ARGS_TEXT_START_INDEX, args.length)
          .collect(Collectors.joining(" "));

      PlayerEntry playerEntry = RelluEssentials.getInstance().getPlayerRegistry()
          .getPlayerEntry(player.getUniqueId());

      NpcDialogueEntry entry = new NpcDialogueEntry();
      entry.setNpcFk(foundNpc.getDbid());
      entry.setListPosition(listPosition);
      entry.setText(StringHelper.replaceColor(text));
      entry.setUpdatedBy(playerEntry.getId());

      boolean updated = RelluEssentials.getInstance().getDatabaseHelper()
          .updateNPCDialogue(entry, foundNpc.getId());

      if (!updated) {
        player.sendMessage(
            translationService.getWithPrefix(MessageKey.COMMAND_NPC_DIALOGUE_NOT_FOUND));
        return;
      }

      RelluEssentials.getInstance().getNpcService().reloadNPCDialogue(foundNpc.getId());
      player.sendMessage(translationService.getWithPrefix(MessageKey.COMMAND_NPC_DIALOGUE_UPDATED));
    }, () -> player.sendMessage(translationService.getWithPrefix(MessageKey.COMMAND_INVALID)));
  }

  @Override
  public boolean matches(String @NonNull [] args) {
    return args.length >= 3 && Admin.Commands.NPC.getName()
        .equalsIgnoreCase(args[ARGS_SUBCOMMAND_INDEX]) && "dialogue".equalsIgnoreCase(
        args[ARGS_ACTION_INDEX]) && "update".equalsIgnoreCase(args[ARGS_DIALOGUE_ACTION_INDEX]);
  }
}