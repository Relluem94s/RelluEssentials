package de.relluem94.minecraft.server.spigot.essentials.persistence.dao;

import de.relluem94.minecraft.server.spigot.essentials.models.pojo.SettingEntry;
import de.relluem94.minecraft.server.spigot.essentials.persistence.dao.mapper.SettingMapper;
import de.relluem94.minecraft.server.spigot.essentials.persistence.jdbc.QueryExecutor;
import java.util.List;

public class SettingDao {

  private final QueryExecutor queryExecutor;

  public SettingDao(QueryExecutor queryExecutor) {
    this.queryExecutor = queryExecutor;
  }

  public List<SettingEntry> findAll() {
    return queryExecutor.queryList("getAllSettings.sql", _ -> {
    }, SettingMapper::mapSetting);
  }
}