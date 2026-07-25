package de.relluem94.minecraft.server.spigot.essentials.commands.admin;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.constants.MessageKey;
import lombok.NonNull;
import org.bukkit.entity.Player;

import static de.relluem94.minecraft.server.spigot.essentials.RelluEssentials.languageHelper;

public class CleanUpLocations {

    public static void cleanUpLocations(@NonNull Player p) {
        int deleted = RelluEssentials.getInstance().getDatabaseHelper().cleanupLocations();
        p.sendMessage(languageHelper.getWithPrefix(MessageKey.COMMAND_ADMIN_CLEAN_OLD_LOCATIONS_END, deleted));
    }
}
