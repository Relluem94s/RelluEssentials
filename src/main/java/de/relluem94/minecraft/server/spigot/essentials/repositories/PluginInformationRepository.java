package de.relluem94.minecraft.server.spigot.essentials.repositories;

import de.relluem94.minecraft.server.spigot.essentials.helpers.DatabaseHelper;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.PluginInformationEntry;

public class PluginInformationRepository {

  private final DatabaseHelper databaseHelper;

  public PluginInformationRepository(DatabaseHelper databaseHelper) {
    this.databaseHelper = databaseHelper;
  }

  public PluginInformationEntry load() {
    return databaseHelper.getPluginInformation();
  }

  public void save(PluginInformationEntry entry) {
    //databaseHelper.updatePluginInformation(entry);
  }
}