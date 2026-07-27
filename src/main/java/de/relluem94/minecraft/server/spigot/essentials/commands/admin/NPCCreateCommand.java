package de.relluem94.minecraft.server.spigot.essentials.commands.admin;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.commands.Admin;
import de.relluem94.minecraft.server.spigot.essentials.constants.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.SubCommand;
import de.relluem94.minecraft.server.spigot.essentials.npc.NPCOperationResult;
import de.relluem94.minecraft.server.spigot.essentials.permissions.Groups;
import de.relluem94.minecraft.server.spigot.essentials.permissions.Permission;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;

import static de.relluem94.minecraft.server.spigot.essentials.RelluEssentials.languageHelper;

public class NPCCreateCommand implements SubCommand {

    private static final int ARGS_SUBCOMMAND_INDEX = 0;
    private static final int ARGS_ACTION_INDEX = 1;
    private static final int ARGS_PROFILE_INDEX = 2;
    private static final int ARGS_X_INDEX = 3;
    private static final int ARGS_Y_INDEX = 4;
    private static final int ARGS_Z_INDEX = 5;
    private static final int REQUIRED_ARGS_LENGTH = 6;

    @Override
    public void execute(Player player, String[] args) {
        if (!Permission.isAuthorized(player, Groups.getGroup("admin").getId())) {
            player.sendMessage(languageHelper.getWithPrefix(MessageKey.COMMAND_PERMISSION_MISSING));
            return;
        }

        if (args.length < REQUIRED_ARGS_LENGTH) {
            player.sendMessage(languageHelper.getWithPrefix(MessageKey.COMMAND_NPC_CREATE_USAGE));
            return;
        }

        String profileName = args[ARGS_PROFILE_INDEX];
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

        String worldName = player.getWorld().getName();
        NPCOperationResult result = RelluEssentials.getInstance().getNpcService().createNPC(profileName, x, y, z, worldName);

        if (!result.isSuccessful()) {
            player.sendMessage(languageHelper.getWithPrefix(MessageKey.COMMAND_NPC_OPERATION_FAILED) + " " + result.getErrorMessage());
            return;
        }

        player.sendMessage(languageHelper.getWithPrefix(MessageKey.COMMAND_NPC_CREATED));
    }

    @Override
    public boolean matches(String @NonNull [] args) {
        return args.length >= 2
                && Admin.Commands.NPC.getName().equalsIgnoreCase(args[ARGS_SUBCOMMAND_INDEX])
                && "create".equalsIgnoreCase(args[ARGS_ACTION_INDEX]);
    }
}