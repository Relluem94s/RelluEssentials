package de.relluem94.minecraft.server.spigot.essentials.repositories;

import de.relluem94.minecraft.server.spigot.essentials.models.pojo.PluginInformationEntry;
import de.relluem94.minecraft.server.spigot.essentials.persistence.dao.PluginInformationDao;
import org.apache.commons.lang3.NotImplementedException;

public class PluginInformationRepository {

  private final PluginInformationDao pluginInformationDao;

  public PluginInformationRepository(PluginInformationDao pluginInformationDao) {
    this.pluginInformationDao = pluginInformationDao;
  }

  public PluginInformationEntry load() {
    return pluginInformationDao.find();
  }

  public void save(PluginInformationEntry pluginInformation) {
    throw new NotImplementedException("PluginInformation can't be saved atm!");
  }
}