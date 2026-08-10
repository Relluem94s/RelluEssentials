package de.relluem94.minecraft.server.spigot.essentials.repositories;

import de.relluem94.minecraft.server.spigot.essentials.helpers.DatabaseHelper;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.SettingEntry;
import java.util.List;

public class SettingRepository {

  private final DatabaseHelper databaseHelper;

  public SettingRepository(DatabaseHelper databaseHelper) {
    this.databaseHelper = databaseHelper;
  }

  public List<SettingEntry> findAll() {
    return databaseHelper.getAllSettings();
  }
}