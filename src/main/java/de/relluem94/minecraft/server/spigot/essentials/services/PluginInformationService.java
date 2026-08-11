package de.relluem94.minecraft.server.spigot.essentials.services;

import de.relluem94.minecraft.server.spigot.essentials.models.pojo.PluginInformationEntry;
import de.relluem94.minecraft.server.spigot.essentials.repositories.PluginInformationRepository;
import lombok.Getter;

public class PluginInformationService {

  private final PluginInformationRepository pluginInformationRepository;

  @Getter
  private PluginInformationEntry pluginInformation;

  public PluginInformationService(PluginInformationRepository pluginInformationRepository) {
    this.pluginInformationRepository = pluginInformationRepository;
  }

  public void load() {
    pluginInformation = pluginInformationRepository.load();
  }

  public void applyPatchedInformation(PluginInformationEntry patchedInformation) {
    pluginInformation = patchedInformation;
  }

  public void updateTabHeader(String newHeader) {
    pluginInformation.setTabHeader(newHeader);
    pluginInformationRepository.save(pluginInformation);
  }

  public void updateTabFooter(String newFooter) {
    pluginInformation.setTabFooter(newFooter);
    pluginInformationRepository.save(pluginInformation);
  }

  public void updateMotd(String newMotd) {
    pluginInformation.setMotdMessage(newMotd);
    pluginInformationRepository.save(pluginInformation);
  }
}