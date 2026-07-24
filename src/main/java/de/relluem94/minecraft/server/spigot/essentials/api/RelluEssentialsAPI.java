package de.relluem94.minecraft.server.spigot.essentials.api;

import de.relluem94.minecraft.server.spigot.essentials.constants.MessageKey;
import lombok.Getter;
import java.util.ArrayList;
import java.util.List;

import static de.relluem94.minecraft.server.spigot.essentials.RelluEssentials.languageHelper;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_FORMS_COMMAND_PREFIX;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.ChatHelper.consoleSendMessage;

public class RelluEssentialsAPI {

    private static RelluEssentialsAPI instance;
    @Getter
    private final List<RelluEssentialsIntegration> integrations = new ArrayList<>();

    private RelluEssentialsAPI() {}

    public static RelluEssentialsAPI getInstance() {
        if (instance == null) {
            instance = new RelluEssentialsAPI();
        }
        return instance;
    }

    public void registerIntegration(RelluEssentialsIntegration integration) {
        integrations.add(integration);
        consoleSendMessage(PLUGIN_FORMS_COMMAND_PREFIX, languageHelper.get(MessageKey.INTEGRATION_REGISTERED, integration.getPluginName(), integration.getPluginVersion()));
        integration.onRelluEssentialsInit(this);
    }

    public void unregisterIntegration(RelluEssentialsIntegration integration) {
        integrations.remove(integration);
        consoleSendMessage(PLUGIN_FORMS_COMMAND_PREFIX, languageHelper.get(MessageKey.INTEGRATION_UNREGISTERED, integration.getPluginName()));
        integration.onRelluEssentialsShutdown();
    }
}