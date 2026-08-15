package de.relluem94.minecraft.server.spigot.essentials.repositories;

import de.relluem94.minecraft.server.spigot.essentials.models.pojo.SettingEntry;
import de.relluem94.minecraft.server.spigot.essentials.persistence.dao.SettingDao;
import java.util.List;

public class SettingRepository {

  private final SettingDao settingDao;

  public SettingRepository(SettingDao settingDao) {
    this.settingDao = settingDao;
  }
  public List<SettingEntry> findAll() {
    return settingDao.findAll();
  }
}