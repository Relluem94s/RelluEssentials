package de.relluem94.minecraft.server.spigot.essentials.persistence.dao.mapper;

import de.relluem94.minecraft.server.spigot.essentials.models.pojo.SettingPlayerEntry;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SettingPlayerMapper {

  private SettingPlayerMapper() {
  }

  public static SettingPlayerEntry mapSettingPlayer(ResultSet resultSet) throws SQLException {
    SettingPlayerEntry entry = new SettingPlayerEntry();
    entry.setId(resultSet.getInt("ID"));
    entry.setCreated(resultSet.getTimestamp("CREATED").toLocalDateTime());
    entry.setCreatedBy(resultSet.getInt("CREATEDBY"));
    entry.setUpdated(resultSet.getTimestamp("UPDATED") != null ? resultSet.getTimestamp("UPDATED").toLocalDateTime() : null);
    entry.setUpdatedBy(resultSet.getObject("UPDATEDBY") != null ? resultSet.getInt("UPDATEDBY") : null);
    entry.setDeleted(resultSet.getTimestamp("DELETED") != null ? resultSet.getTimestamp("DELETED").toLocalDateTime() : null);
    entry.setDeletedBy(resultSet.getObject("DELETEDBY") != null ? resultSet.getInt("DELETEDBY") : null);
    entry.setPlayerFk(resultSet.getInt("player_fk"));
    entry.setSettingFk(resultSet.getInt("setting_fk"));
    entry.setValue(resultSet.getString("value"));
    return entry;
  }
}