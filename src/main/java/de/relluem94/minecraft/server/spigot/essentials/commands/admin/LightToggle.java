package de.relluem94.minecraft.server.spigot.essentials.commands.admin;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.constants.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.constants.PlayerState;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.PlayerEntry;
import org.bukkit.entity.Player;

import static de.relluem94.minecraft.server.spigot.essentials.RelluEssentials.languageHelper;

public class LightToggle {
    public static void lightToggle(Player p) {
        PlayerEntry pe = RelluEssentials.getInstance().getPlayerAPI().getPlayerEntry(p);

        if (pe.getPlayerState().equals(PlayerState.LIGHT_TOGGLE)) {
            pe.setPlayerState(PlayerState.DEFAULT);
            p.sendMessage(languageHelper.getWithPrefix(MessageKey.COMMAND_ADMIN_LIGHT_TOGGLE_DISABLED));
        } else {
            p.sendMessage(languageHelper.getWithPrefix(MessageKey.COMMAND_ADMIN_LIGHT_TOGGLE));
            pe.setPlayerState(PlayerState.LIGHT_TOGGLE);
        }
    }
}
