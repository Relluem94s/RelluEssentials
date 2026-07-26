package de.relluem94.minecraft.server.spigot.essentials.commands.admin;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.commands.Admin;
import de.relluem94.minecraft.server.spigot.essentials.constants.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.constants.PlayerState;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.PlayerEntry;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.SubCommand;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;

import static de.relluem94.minecraft.server.spigot.essentials.RelluEssentials.languageHelper;

public class LightToggleCommand implements SubCommand {

    @Override
    public void execute(Player player, String[] args) {
        PlayerEntry pe = RelluEssentials.getInstance().getPlayerAPI().getPlayerEntry(player);

        if (pe.getPlayerState().equals(PlayerState.LIGHT_TOGGLE)) {
            pe.setPlayerState(PlayerState.DEFAULT);
            player.sendMessage(languageHelper.getWithPrefix(MessageKey.COMMAND_ADMIN_LIGHT_TOGGLE_DISABLED));
        } else {
            player.sendMessage(languageHelper.getWithPrefix(MessageKey.COMMAND_ADMIN_LIGHT_TOGGLE));
            pe.setPlayerState(PlayerState.LIGHT_TOGGLE);
        }
    }

    @Override
    public boolean matches(String @NonNull [] args) {
        return args.length == 1 && Admin.Commands.LIGHT.getName().equalsIgnoreCase(args[0]);
    }
}