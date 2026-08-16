package de.relluem94.minecraft.server.spigot.essentials.persistence.dao;

import de.relluem94.minecraft.server.spigot.essentials.models.pojo.SettingPlayerEntry;
import de.relluem94.minecraft.server.spigot.essentials.persistence.dao.mapper.SettingPlayerMapper;
import de.relluem94.minecraft.server.spigot.essentials.persistence.jdbc.QueryExecutor;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class SettingPlayerDao {

  private final QueryExecutor queryExecutor;

  public SettingPlayerDao(QueryExecutor queryExecutor) {
    this.queryExecutor = queryExecutor;
  }

  public List<SettingPlayerEntry> findAllByPlayerId(int playerId) {
    return queryExecutor.queryList("getAllSettingPlayersByPlayerId.sql",
        statement -> statement.setInt(1, playerId),
        SettingPlayerMapper::mapSettingPlayer);
  }

  public Optional<SettingPlayerEntry> findById(int id) {
    return Optional.ofNullable(queryExecutor.querySingle("getSettingPlayerById.sql",
        statement -> statement.setInt(1, id),
        SettingPlayerMapper::mapSettingPlayer));
  }

  public void insert(SettingPlayerEntry entry) {
    queryExecutor.executeUpdate("insertSettingPlayer.sql", statement -> {
      statement.setTimestamp(1, Timestamp.valueOf(entry.getCreated()));
      statement.setInt(2, entry.getCreatedBy());
      statement.setInt(3, entry.getPlayerFk());
      statement.setInt(4, entry.getSettingFk());
      statement.setString(5, entry.getValue());
    });
  }

  public void update(SettingPlayerEntry entry) {
    queryExecutor.executeUpdate("updateSettingPlayer.sql", statement -> {
      statement.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
      statement.setInt(2, entry.getUpdatedBy());
      statement.setString(3, entry.getValue());
      statement.setInt(4, entry.getId());
    });
  }

  public void softDelete(int id, int deletedBy) {
    queryExecutor.executeUpdate("deleteSettingPlayer.sql", statement -> {
      statement.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
      statement.setInt(2, deletedBy);
      statement.setInt(3, id);
    });
  }
}