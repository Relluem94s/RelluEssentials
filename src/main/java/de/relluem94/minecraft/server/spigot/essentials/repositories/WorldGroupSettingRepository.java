package de.relluem94.minecraft.server.spigot.essentials.repositories;

import de.relluem94.minecraft.server.spigot.essentials.helpers.DatabaseHelper;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.WorldEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.WorldGroupEntry;
import java.util.List;

public class WorldGroupSettingRepository {

  private final DatabaseHelper databaseHelper;

  public WorldGroupSettingRepository(DatabaseHelper databaseHelper) {
    this.databaseHelper = databaseHelper;
  }

  public List<WorldGroupEntry> findAllWorldGroups() {
    return databaseHelper.getWorldGroups();
  }

  public List<WorldEntry> findWorldsByGroup(WorldGroupEntry worldGroupEntry) {
    return databaseHelper.getWorldByGroup(worldGroupEntry);
  }
}