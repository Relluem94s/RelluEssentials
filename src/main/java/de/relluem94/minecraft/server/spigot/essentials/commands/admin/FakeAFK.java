package de.relluem94.minecraft.server.spigot.essentials.commands.admin;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.constants.PlayerState;
import de.relluem94.minecraft.server.spigot.essentials.helpers.PlayerHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.PlayerEntry;
import org.bukkit.entity.Player;

public class FakeAFK {

    public static void fakeAFK(Player p) {
        PlayerEntry pe = RelluEssentials.getInstance().getPlayerAPI().getPlayerEntry(p);

        if (pe.getPlayerState().equals(PlayerState.FAKE_AFK_ACTIVE)) {
            PlayerHelper.setAFK(p, false);
            pe.setPlayerState(PlayerState.DEFAULT);
        } else {
            pe.setPlayerState(PlayerState.FAKE_AFK_ON);
            PlayerHelper.setAFK(p, false);
            pe.setPlayerState(PlayerState.FAKE_AFK_ACTIVE);
        }
    }
}
