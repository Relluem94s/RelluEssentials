package de.relluem94.minecraft.server.spigot.essentials.commands.admin;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.commands.Admin;
import de.relluem94.minecraft.server.spigot.essentials.constants.PlayerState;
import de.relluem94.minecraft.server.spigot.essentials.helpers.PlayerHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.PlayerEntry;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.SubCommand;
import lombok.NonNull;
import org.bukkit.entity.Player;

public class FakeAFKCommand implements SubCommand {

    @Override
    public void execute(Player player, String[] args) {
        PlayerEntry pe = RelluEssentials.getInstance().getPlayerAPI().getPlayerEntry(player);

        if (pe.getPlayerState().equals(PlayerState.FAKE_AFK_ACTIVE)) {
            PlayerHelper.setAFK(player, false);
            pe.setPlayerState(PlayerState.DEFAULT);
        } else {
            pe.setPlayerState(PlayerState.FAKE_AFK_ON);
            PlayerHelper.setAFK(player, false);
            pe.setPlayerState(PlayerState.FAKE_AFK_ACTIVE);
        }
    }

    @Override
    public boolean matches(String @NonNull [] args) {
        return args.length == 1 && Admin.Commands.AFK.getName().equalsIgnoreCase(args[0]);
    }
}