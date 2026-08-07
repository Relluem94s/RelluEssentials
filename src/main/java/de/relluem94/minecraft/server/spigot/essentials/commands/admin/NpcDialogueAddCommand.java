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
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;

public class NpcDialogueAddCommand implements SubCommand {

  private static final int ARGS_SUBCOMMAND_INDEX = 0;
  private static final int ARGS_ACTION_INDEX = 1;
  private static final int ARGS_DIALOGUE_ACTION_INDEX = 2;
  private static final int ARGS_NPC_ID_INDEX = 3;
  private static final int ARGS_LIST_POSITION_INDEX = 4;
  private static final int ARGS_TEXT_START_INDEX = 5;
  private static final int REQUIRED_ARGS_LENGTH = 6;
  private final ServiceContext serviceContext;

  public NpcDialogueAddCommand(ServiceContext context) {
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
      player.sendMessage(
          serviceContext.getTranslationService()
              .getWithPrefix(MessageKey.COMMAND_NPC_DIALOGUE_ADD_USAGE));
      return;
    }

    UUID npcId;
    int listPosition;

    try {
      npcId = UUID.fromString(args[ARGS_NPC_ID_INDEX]);
      listPosition = Integer.parseInt(args[ARGS_LIST_POSITION_INDEX]);
    } catch (IllegalArgumentException e) {
      player.sendMessage(
          serviceContext.getTranslationService().getWithPrefix(MessageKey.COMMAND_INVALID));
      return;
    }

    Optional<Npc> npc = RelluEssentials.getInstance().getNpcService().getNPCById(npcId);
    npc.ifPresentOrElse(npc1 -> {
          String text = Arrays.stream(args, ARGS_TEXT_START_INDEX, args.length)
              .collect(Collectors.joining(" "));

          PlayerEntry playerEntry = RelluEssentials.getInstance().getPlayerRegistry()
              .getPlayerEntry(player.getUniqueId());

          NpcDialogueEntry entry = new NpcDialogueEntry();
          entry.setNpcFk(npc1.getDbid());
          entry.setListPosition(listPosition);
          entry.setText(StringHelper.replaceColor(text));
          entry.setCreatedBy(playerEntry.getId());

          RelluEssentials.getInstance().getDatabaseHelper().insertNPCDialogue(entry);
          RelluEssentials.getInstance().getNpcService().reloadNPCDialogue(npc1.getId());
          player.sendMessage(serviceContext.getTranslationService()
              .getWithPrefix(MessageKey.COMMAND_NPC_DIALOGUE_ADDED));
        },
        () -> player.sendMessage(
            serviceContext.getTranslationService().getWithPrefix(MessageKey.COMMAND_INVALID)));
  }

  @Override
  public boolean matches(String @NonNull [] args) {
    return args.length >= 3
        && Admin.Commands.NPC.getName().equalsIgnoreCase(args[ARGS_SUBCOMMAND_INDEX])
        && "dialogue".equalsIgnoreCase(args[ARGS_ACTION_INDEX])
        && "add".equalsIgnoreCase(args[ARGS_DIALOGUE_ACTION_INDEX]);
  }
}