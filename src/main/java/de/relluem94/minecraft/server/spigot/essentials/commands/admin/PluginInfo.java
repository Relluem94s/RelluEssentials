package de.relluem94.minecraft.server.spigot.essentials.commands.admin;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.api.RelluEssentialsAPI;
import de.relluem94.minecraft.server.spigot.essentials.api.RelluEssentialsIntegration;
import de.relluem94.minecraft.server.spigot.essentials.constants.MessageKey;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;

import java.util.List;

import static de.relluem94.minecraft.server.spigot.essentials.RelluEssentials.languageHelper;

public class PluginInfo {
    public static void pluginInfo(@NonNull Player p) {
        p.sendMessage(languageHelper.getWithPrefix(MessageKey.COMMAND_ADMIN_INFO_VERSION,
                RelluEssentials.getInstance().getDescription().getVersion()));

        List<RelluEssentialsIntegration> integrations = RelluEssentialsAPI.getInstance().getIntegrations();
        if (integrations.isEmpty()) {
            p.sendMessage(languageHelper.getWithPrefix(MessageKey.COMMAND_ADMIN_INFO_INTEGRATIONS_NONE));
        } else {
            p.sendMessage(languageHelper.getWithPrefix(MessageKey.COMMAND_ADMIN_INFO_INTEGRATIONS_HEADER, integrations.size()));
            for (RelluEssentialsIntegration integration : integrations) {
                p.sendMessage(languageHelper.getWithPrefix(MessageKey.COMMAND_ADMIN_INFO_INTEGRATIONS_ENTRY,
                        integration.getPluginName(), integration.getPluginVersion()));
            }
        }
    }
}
