package de.relluem94.minecraft.server.spigot.essentials.persistence.dao;

import de.relluem94.minecraft.server.spigot.essentials.helpers.db.mapper.MiscMapper;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.CropEntry;
import de.relluem94.minecraft.server.spigot.essentials.persistence.jdbc.QueryExecutor;
import java.util.List;

public class CropDao {

  private final QueryExecutor queryExecutor;

  public CropDao(QueryExecutor queryExecutor) {
    this.queryExecutor = queryExecutor;
  }

  public List<CropEntry> findAll() {
    return queryExecutor.queryList("getCrops.sql", _ -> {
    }, MiscMapper::mapCrop);
  }
}