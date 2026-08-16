package de.relluem94.minecraft.server.spigot.essentials.persistence.dao;

import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.SettingPlayerEntry;
import de.relluem94.minecraft.server.spigot.essentials.persistence.dao.mapper.SettingPlayerMapper;
import de.relluem94.minecraft.server.spigot.essentials.persistence.jdbc.QueryExecutor;
import java.util.List;
import java.util.Optional;

public class SettingPlayerDao {

  private final QueryExecutor queryExecutor;
  private final ServiceContext serviceContext;

  public SettingPlayerDao(QueryExecutor queryExecutor, ServiceContext serviceContext) {
    this.queryExecutor = queryExecutor;
    this.serviceContext = serviceContext;
  }

  public List<SettingPlayerEntry> findAllByPlayerId(int playerId) {
    return queryExecutor.queryList("getAllSettingPlayersByPlayerId.sql",
        statement -> statement.setInt(1, playerId),
        (rs) -> SettingPlayerMapper.mapSettingPlayer(rs, serviceContext.getSettingService()));
  }

  public Optional<SettingPlayerEntry> findById(int id) {
    return Optional.ofNullable(queryExecutor.querySingle("getSettingPlayerById.sql",
        statement -> statement.setInt(1, id),
        (rs) -> SettingPlayerMapper.mapSettingPlayer(rs, serviceContext.getSettingService())));
  }

  public void insert(SettingPlayerEntry entry) {
    queryExecutor.executeUpdate("insertSettingPlayer.sql", statement -> {
      statement.setInt(1, entry.getCreatedBy());
      statement.setInt(2, entry.getPlayerFk());
      statement.setInt(3, entry.getSettingFk());
      statement.setBoolean(4, entry.isValue());
    });
  }

  public void update(SettingPlayerEntry entry) {
    queryExecutor.executeUpdate("updateSettingPlayer.sql", statement -> {
      statement.setInt(1, entry.getUpdatedBy());
      statement.setBoolean(2, entry.isValue());
      statement.setInt(3, entry.getId());
    });
  }

  public void softDelete(int id, int deletedBy) {
    queryExecutor.executeUpdate("deleteSettingPlayer.sql", statement -> {
      statement.setInt(1, deletedBy);
      statement.setInt(2, id);
    });
  }
}