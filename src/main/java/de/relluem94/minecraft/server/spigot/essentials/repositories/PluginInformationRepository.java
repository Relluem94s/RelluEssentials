package de.relluem94.minecraft.server.spigot.essentials.repositories;

import de.relluem94.minecraft.server.spigot.essentials.models.pojo.PluginInformationEntry;
import de.relluem94.minecraft.server.spigot.essentials.persistence.dao.PluginInformationDao;
import org.apache.commons.lang3.NotImplementedException;

/**
 * Repository for accessing and managing {@link PluginInformationEntry} data.
 * Acts as an abstraction layer between the service layer and the underlying
 * {@link PluginInformationDao}.
 *
 * @author rellu
 */
public class PluginInformationRepository {

  private final PluginInformationDao pluginInformationDao;

  /**
   * Creates a new {@code PluginInformationRepository} with the given data access object.
   *
   * @param pluginInformationDao the DAO used to retrieve plugin information
   */
  public PluginInformationRepository(PluginInformationDao pluginInformationDao) {
    this.pluginInformationDao = pluginInformationDao;
  }

  /**
   * Loads the plugin information entry from the underlying data source.
   *
   * @return the {@link PluginInformationEntry} retrieved from the data source
   */
  public PluginInformationEntry load() {
    return pluginInformationDao.find();
  }

  /**
   * Saving plugin information is currently not supported.
   *
   * @param pluginInformation the {@link PluginInformationEntry} to save
   * @throws org.apache.commons.lang3.NotImplementedException always, as this operation is not
   *     yet implemented
   */
  public void save(PluginInformationEntry pluginInformation) {
    throw new NotImplementedException(
        "PluginInformation with id " + pluginInformation.getId() + " can't be saved atm!");
  }
}