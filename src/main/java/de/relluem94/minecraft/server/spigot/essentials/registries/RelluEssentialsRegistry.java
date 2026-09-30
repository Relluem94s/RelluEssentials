package de.relluem94.minecraft.server.spigot.essentials.registries;

import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_FORMS_COMMAND_PREFIX;

import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.RelluEssentialsIntegration;
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

  private final ServiceContext serviceContext;

  /**
   * Constructs a new RelluEssentialsRegistry.
   *
   * @param serviceContext the service used for retrieving translated messages
   */
  public RelluEssentialsRegistry(ServiceContext serviceContext) {
    this.serviceContext = serviceContext;
  }

  /**
   * Initializes the singleton instance of the registry.
   *
   * @param serviceContext the service context
   */
  public static void initialize(ServiceContext serviceContext) {
    instance = new RelluEssentialsRegistry(serviceContext);
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
    serviceContext.getServerService().getConsoleSender().sendMessage(
        PLUGIN_FORMS_COMMAND_PREFIX + " " + serviceContext.getTranslationService()
            .get(MessageKey.INTEGRATION_REGISTERED, integration.getPluginName(),
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
    serviceContext.getServerService().getConsoleSender().sendMessage(PLUGIN_FORMS_COMMAND_PREFIX,
        serviceContext.getTranslationService()
            .get(MessageKey.INTEGRATION_UNREGISTERED, integration.getPluginName()));
    integration.onRelluEssentialsShutdown();
  }
}