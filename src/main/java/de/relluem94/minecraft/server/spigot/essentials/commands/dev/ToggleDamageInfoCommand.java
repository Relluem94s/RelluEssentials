package de.relluem94.minecraft.server.spigot.essentials.commands.dev;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.commands.DevCommand;
import de.relluem94.minecraft.server.spigot.essentials.constants.PlayerState;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.PlayerEntry;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.SubCommand;
import lombok.NonNull;
import org.bukkit.entity.Player;

public class ToggleDamageInfoCommand implements SubCommand {

    @Override
    public void execute(Player player, String[] args) {
        PlayerEntry playerEntry = RelluEssentials.getInstance().getPlayerAPI().getPlayerEntry(player);
        if (playerEntry.getPlayerState().equals(PlayerState.DEFAULT)) {
            playerEntry.setPlayerState(PlayerState.DAMAGE_INFO);
        } else {
            playerEntry.setPlayerState(PlayerState.DEFAULT);
        }
    }

    @Override
    public boolean matches(String @NonNull [] args) {
        return args.length == 1 && DevCommand.Commands.DAMAGE_INFO.getName().equalsIgnoreCase(args[0]);
    }
}