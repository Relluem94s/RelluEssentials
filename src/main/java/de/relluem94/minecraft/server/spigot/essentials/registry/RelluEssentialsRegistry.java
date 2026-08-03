package de.relluem94.minecraft.server.spigot.essentials.registry;

import static de.relluem94.minecraft.server.spigot.essentials.RelluEssentials.translationService;
import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_FORMS_COMMAND_PREFIX;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.ChatHelper.consoleSendMessage;

import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.RelluEssentialsIntegration;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

public class RelluEssentialsRegistry {

  private static RelluEssentialsRegistry instance;
  @Getter
  private final List<RelluEssentialsIntegration> integrations = new ArrayList<>();

  private RelluEssentialsRegistry() {
  }

  public static RelluEssentialsRegistry getInstance() {
    if (instance == null) {
      instance = new RelluEssentialsRegistry();
    }
    return instance;
  }

  public void registerIntegration(RelluEssentialsIntegration integration) {
    integrations.add(integration);
    consoleSendMessage(PLUGIN_FORMS_COMMAND_PREFIX,
        translationService.get(MessageKey.INTEGRATION_REGISTERED, integration.getPluginName(),
            integration.getPluginVersion()));
    integration.onRelluEssentialsInit(this);
  }

  public void unregisterIntegration(RelluEssentialsIntegration integration) {
    integrations.remove(integration);
    consoleSendMessage(PLUGIN_FORMS_COMMAND_PREFIX,
        translationService.get(MessageKey.INTEGRATION_UNREGISTERED, integration.getPluginName()));
    integration.onRelluEssentialsShutdown();
  }
}