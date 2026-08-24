package de.relluem94.minecraft.server.spigot.essentials.services;

import de.relluem94.minecraft.server.spigot.essentials.models.pojo.PluginInformationEntry;
import de.relluem94.minecraft.server.spigot.essentials.repositories.PluginInformationRepository;
import lombok.Getter;

/**
 * Service responsible for managing and updating plugin-related information.
 */
public class PluginInformationService {

  private final PluginInformationRepository pluginInformationRepository;

  @Getter
  private PluginInformationEntry pluginInformation;

  /**
   * Constructs a new PluginInformationService.
   *
   * @param pluginInformationRepository the repository used to persist and load plugin information
   */
  public PluginInformationService(PluginInformationRepository pluginInformationRepository) {
    this.pluginInformationRepository = pluginInformationRepository;
  }

  /**
   * Loads the plugin information from the repository.
   */
  public void load() {
    pluginInformation = pluginInformationRepository.load();
  }

  /**
   * Applies a new set of patched plugin information.
   *
   * @param patchedInformation the new plugin information to be applied
   */
  public void applyPatchedInformation(PluginInformationEntry patchedInformation) {
    pluginInformation = patchedInformation;
  }

  /**
   * Updates the tab header and persists the change.
   *
   * @param newHeader the new tab header text
   */
  @SuppressWarnings("unused")
  public void updateTabHeader(String newHeader) {
    pluginInformation.setTabHeader(newHeader);
    pluginInformationRepository.save(pluginInformation);
  }

  /**
   * Updates the tab footer and persists the change.
   *
   * @param newFooter the new tab footer text
   */
  @SuppressWarnings("unused")
  public void updateTabFooter(String newFooter) {
    pluginInformation.setTabFooter(newFooter);
    pluginInformationRepository.save(pluginInformation);
  }

  /**
   * Updates the MOTD message and persists the change.
   *
   * @param newMotd the new MOTD message
   */
  @SuppressWarnings("unused")
  public void updateMotd(String newMotd) {
    pluginInformation.setMotdMessage(newMotd);
    pluginInformationRepository.save(pluginInformation);
  }
}