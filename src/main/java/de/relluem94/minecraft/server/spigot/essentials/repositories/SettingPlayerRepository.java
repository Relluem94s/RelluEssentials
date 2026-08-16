package de.relluem94.minecraft.server.spigot.essentials.repositories;

import de.relluem94.minecraft.server.spigot.essentials.models.pojo.SettingPlayerEntry;
import de.relluem94.minecraft.server.spigot.essentials.persistence.dao.SettingPlayerDao;
import java.util.List;
import java.util.Optional;

public class SettingPlayerRepository {

  private final SettingPlayerDao settingPlayerDao;

  public SettingPlayerRepository(SettingPlayerDao settingPlayerDao) {
    this.settingPlayerDao = settingPlayerDao;
  }

  public List<SettingPlayerEntry> findAllByPlayerId(int playerId) {
    return settingPlayerDao.findAllByPlayerId(playerId);
  }

  public Optional<SettingPlayerEntry> findById(int id) {
    return settingPlayerDao.findById(id);
  }

  public void insert(SettingPlayerEntry entry) {
    settingPlayerDao.insert(entry);
  }

  public void update(SettingPlayerEntry entry) {
    settingPlayerDao.update(entry);
  }

  public void softDelete(int id, int deletedBy) {
    settingPlayerDao.softDelete(id, deletedBy);
  }
}