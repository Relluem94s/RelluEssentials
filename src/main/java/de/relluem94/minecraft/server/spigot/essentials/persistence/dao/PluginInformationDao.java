package de.relluem94.minecraft.server.spigot.essentials.persistence.dao;

import de.relluem94.minecraft.server.spigot.essentials.helpers.db.mapper.MiscMapper;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.PluginInformationEntry;
import de.relluem94.minecraft.server.spigot.essentials.persistence.jdbc.QueryExecutor;

public class PluginInformationDao {

  private final QueryExecutor queryExecutor;

  public PluginInformationDao(QueryExecutor queryExecutor) {
    this.queryExecutor = queryExecutor;
  }

  public PluginInformationEntry find() {
    return queryExecutor.querySingle("getPluginInformation.sql", _ -> {}, MiscMapper::mapPluginInformation);
  }
}