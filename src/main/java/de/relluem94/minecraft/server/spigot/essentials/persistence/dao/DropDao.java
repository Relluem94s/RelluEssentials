package de.relluem94.minecraft.server.spigot.essentials.persistence.dao;

import de.relluem94.minecraft.server.spigot.essentials.helpers.db.mapper.MiscMapper;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.DropEntry;
import de.relluem94.minecraft.server.spigot.essentials.persistence.jdbc.QueryExecutor;
import java.util.List;

public class DropDao {

  private final QueryExecutor queryExecutor;

  public DropDao(QueryExecutor queryExecutor) {
    this.queryExecutor = queryExecutor;
  }

  public List<DropEntry> findAll() {
    return queryExecutor.queryList("getDrops.sql", _ -> {
    }, MiscMapper::mapDrop);
  }
}