package de.relluem94.minecraft.server.spigot.essentials.commands.admin;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.commands.Admin;
import de.relluem94.minecraft.server.spigot.essentials.constants.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.PlayerEntry;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.SubCommand;
import de.relluem94.minecraft.server.spigot.essentials.npc.NPC;
import de.relluem94.minecraft.server.spigot.essentials.permissions.Groups;
import de.relluem94.minecraft.server.spigot.essentials.permissions.Permission;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;

import java.util.Optional;
import java.util.UUID;

import static de.relluem94.minecraft.server.spigot.essentials.RelluEssentials.languageHelper;

public class NPCDialogueDeleteCommand implements SubCommand {

    private static final int ARGS_SUBCOMMAND_INDEX = 0;
    private static final int ARGS_ACTION_INDEX = 1;
    private static final int ARGS_DIALOGUE_ACTION_INDEX = 2;
    private static final int ARGS_NPC_ID_INDEX = 3;
    private static final int ARGS_LIST_POSITION_INDEX = 4;
    private static final int REQUIRED_ARGS_LENGTH = 5;

    @Override
    public void execute(Player player, String[] args) {
        if (!Permission.isAuthorized(player, Groups.getGroup("admin").getId())) {
            player.sendMessage(languageHelper.getWithPrefix(MessageKey.COMMAND_PERMISSION_MISSING));
            return;
        }

        if (args.length < REQUIRED_ARGS_LENGTH) {
            player.sendMessage(languageHelper.getWithPrefix(MessageKey.COMMAND_NPC_DIALOGUE_DELETE_USAGE));
            return;
        }

        UUID npcId;
        int listPosition;

        try {
            npcId = UUID.fromString(args[ARGS_NPC_ID_INDEX]);
            listPosition = Integer.parseInt(args[ARGS_LIST_POSITION_INDEX]);
        } catch (IllegalArgumentException e) {
            player.sendMessage(languageHelper.getWithPrefix(MessageKey.COMMAND_INVALID));
            return;
        }

        Optional<NPC> npc = RelluEssentials.getInstance().getNpcService().getNPCById(npcId);
        npc.ifPresentOrElse(foundNpc -> {
                    PlayerEntry playerEntry = RelluEssentials.getInstance().getPlayerAPI().getPlayerEntry(player.getUniqueId());

                    RelluEssentials.getInstance().getDatabaseHelper().deleteNPCDialogueById(foundNpc.getId(), listPosition, playerEntry.getId());
                    RelluEssentials.getInstance().getNpcService().reloadNPCDialogue(foundNpc.getId());
                    player.sendMessage(languageHelper.getWithPrefix(MessageKey.COMMAND_NPC_DIALOGUE_DELETED));
                },
                () -> player.sendMessage(languageHelper.getWithPrefix(MessageKey.COMMAND_INVALID)));
    }

    @Override
    public boolean matches(String @NonNull [] args) {
        return args.length >= 3
                && Admin.Commands.NPC.getName().equalsIgnoreCase(args[ARGS_SUBCOMMAND_INDEX])
                && "dialogue".equalsIgnoreCase(args[ARGS_ACTION_INDEX])
                && "delete".equalsIgnoreCase(args[ARGS_DIALOGUE_ACTION_INDEX]);
    }
}