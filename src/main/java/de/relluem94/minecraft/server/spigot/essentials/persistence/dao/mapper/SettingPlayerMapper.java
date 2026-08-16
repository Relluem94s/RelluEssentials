package de.relluem94.minecraft.server.spigot.essentials.persistence.dao.mapper;

import static de.relluem94.minecraft.server.spigot.essentials.constants.db.DatabaseMappings.FIELD_CREATED;
import static de.relluem94.minecraft.server.spigot.essentials.constants.db.DatabaseMappings.FIELD_CREATEDBY;
import static de.relluem94.minecraft.server.spigot.essentials.constants.db.DatabaseMappings.FIELD_DELETED;
import static de.relluem94.minecraft.server.spigot.essentials.constants.db.DatabaseMappings.FIELD_DELETEDBY;
import static de.relluem94.minecraft.server.spigot.essentials.constants.db.DatabaseMappings.FIELD_ID;
import static de.relluem94.minecraft.server.spigot.essentials.constants.db.DatabaseMappings.FIELD_PLAYER_FK;
import static de.relluem94.minecraft.server.spigot.essentials.constants.db.DatabaseMappings.FIELD_SETTING_FK;
import static de.relluem94.minecraft.server.spigot.essentials.constants.db.DatabaseMappings.FIELD_UPDATED;
import static de.relluem94.minecraft.server.spigot.essentials.constants.db.DatabaseMappings.FIELD_UPDATEDBY;
import static de.relluem94.minecraft.server.spigot.essentials.constants.db.DatabaseMappings.FIELD_VALUE;

import de.relluem94.minecraft.server.spigot.essentials.models.pojo.SettingEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.SettingPlayerEntry;
import de.relluem94.minecraft.server.spigot.essentials.services.SettingService;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class SettingPlayerMapper {

  private SettingPlayerMapper() {
  }

  public static SettingPlayerEntry mapSettingPlayer(ResultSet resultSet, SettingService settingService) throws SQLException {
    SettingPlayerEntry entry = new SettingPlayerEntry();
    entry.setId(resultSet.getInt(FIELD_ID));
    entry.setCreated(resultSet.getString(FIELD_CREATED));
    entry.setCreatedBy(resultSet.getInt(FIELD_CREATEDBY));
    entry.setUpdated(resultSet.getString(FIELD_UPDATED));
    entry.setUpdatedBy(resultSet.getInt(FIELD_UPDATEDBY));
    entry.setDeleted(resultSet.getString(FIELD_DELETED));
    entry.setDeletedBy(resultSet.getInt(FIELD_DELETEDBY));
    entry.setPlayerFk(resultSet.getInt(FIELD_PLAYER_FK));
    entry.setValue(resultSet.getBoolean(FIELD_VALUE));

    int settingFk = resultSet.getInt(FIELD_SETTING_FK);
    entry.setSettingFk(settingFk);

    Optional<SettingEntry> settingEntry = settingService.findById(settingFk);
    settingEntry.ifPresent(entry::setSettingEntry);

    return entry;
  }
}