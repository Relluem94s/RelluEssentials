package de.relluem94.minecraft.server.spigot.essentials.registries;

import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_FORMS_COMMAND_PREFIX;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.ChatHelper.consoleSendMessage;

import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.RelluEssentialsIntegration;
import de.relluem94.minecraft.server.spigot.essentials.services.TranslationService;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

/**
 * Registry responsible for managing integrations within the RelluEssentials ecosystem.
 */
public class RelluEssentialsRegistry {

  private static RelluEssentialsRegistry instance;
  @Getter
  private final List<RelluEssentialsIntegration> integrations = new ArrayList<>();

  private final TranslationService translationService;

  /**
   * Constructs a new RelluEssentialsRegistry.
   *
   * @param translationService the service used for retrieving translated messages
   */
  public RelluEssentialsRegistry(TranslationService translationService) {
    this.translationService = translationService;
  }

  /**
   * Initializes the singleton instance of the registry.
   *
   * @param translationService the service used for retrieving translated messages
   */
  public static void initialize(TranslationService translationService) {
    instance = new RelluEssentialsRegistry(translationService);
  }

  /**
   * Retrieves the singleton instance of the registry.
   *
   * @return the current RelluEssentialsRegistry instance
   * @throws IllegalStateException if the registry has not been initialized
   */
  public static RelluEssentialsRegistry getInstance() {
    if (instance == null) {
      throw new IllegalStateException("RelluEssentialsRegistry not initialized");
    }
    return instance;
  }

  /**
   * Registers a new integration and triggers its initialization lifecycle.
   *
   * @param integration the integration to be registered
   */
  public void registerIntegration(RelluEssentialsIntegration integration) {
    integrations.add(integration);
    consoleSendMessage(PLUGIN_FORMS_COMMAND_PREFIX,
        translationService.get(MessageKey.INTEGRATION_REGISTERED, integration.getPluginName(),
            integration.getPluginVersion()));
    integration.onRelluEssentialsInit(this);
  }

  /**
   * Unregisters an existing integration and triggers its shutdown lifecycle.
   *
   * @param integration the integration to be unregistered
   */
  public void unregisterIntegration(RelluEssentialsIntegration integration) {
    integrations.remove(integration);
    consoleSendMessage(PLUGIN_FORMS_COMMAND_PREFIX,
        translationService.get(MessageKey.INTEGRATION_UNREGISTERED, integration.getPluginName()));
    integration.onRelluEssentialsShutdown();
  }
}