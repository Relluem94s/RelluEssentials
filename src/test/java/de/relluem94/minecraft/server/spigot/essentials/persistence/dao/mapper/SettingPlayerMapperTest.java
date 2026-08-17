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
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import de.relluem94.minecraft.server.spigot.essentials.models.pojo.SettingEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.SettingPlayerEntry;
import de.relluem94.minecraft.server.spigot.essentials.services.SettingService;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SettingPlayerMapperTest {

  @Mock
  private ResultSet resultSet;

  @Mock
  private SettingService settingService;

  @Test
  void constructorThrowsIllegalStateException() throws Exception {
    Constructor<SettingPlayerMapper> constructor = SettingPlayerMapper.class.getDeclaredConstructor();
    constructor.setAccessible(true);
    InvocationTargetException thrown = assertThrows(InvocationTargetException.class, constructor::newInstance);
    assertInstanceOf(IllegalStateException.class, thrown.getCause());
  }

  @Test
  void mapSettingPlayerReturnsFullyPopulatedSettingPlayerEntry() throws SQLException {
    int settingFk = 50;
    SettingEntry expectedSettingEntry = new SettingEntry();

    when(resultSet.getInt(FIELD_ID)).thenReturn(1);
    when(resultSet.getString(FIELD_CREATED)).thenReturn("2024-01-01");
    when(resultSet.getInt(FIELD_CREATEDBY)).thenReturn(2);
    when(resultSet.getString(FIELD_UPDATED)).thenReturn("2024-06-01");
    when(resultSet.getInt(FIELD_UPDATEDBY)).thenReturn(3);
    when(resultSet.getString(FIELD_DELETED)).thenReturn("2024-12-01");
    when(resultSet.getInt(FIELD_DELETEDBY)).thenReturn(4);
    when(resultSet.getInt(FIELD_PLAYER_FK)).thenReturn(100);
    when(resultSet.getBoolean(FIELD_VALUE)).thenReturn(true);
    when(resultSet.getInt(FIELD_SETTING_FK)).thenReturn(settingFk);

    when(settingService.findById(settingFk)).thenReturn(Optional.of(expectedSettingEntry));

    SettingPlayerEntry result = SettingPlayerMapper.mapSettingPlayer(resultSet, settingService);

    assertAll(
        () -> assertEquals(1, result.getId()),
        () -> assertEquals("2024-01-01", result.getCreated()),
        () -> assertEquals(2, result.getCreatedBy()),
        () -> assertEquals("2024-06-01", result.getUpdated()),
        () -> assertEquals(3, result.getUpdatedBy()),
        () -> assertEquals("2024-12-01", result.getDeleted()),
        () -> assertEquals(4, result.getDeletedBy()),
        () -> assertEquals(100, result.getPlayerFk()),
        () -> assertEquals(true, result.isValue()),
        () -> assertEquals(settingFk, result.getSettingFk()),
        () -> assertNotNull(result.getSettingEntry()),
        () -> assertEquals(expectedSettingEntry, result.getSettingEntry())
    );
  }

  @Test
  void mapSettingPlayerWithMissingSettingEntry() throws SQLException {
    int settingFk = 999;

    when(resultSet.getInt(FIELD_ID)).thenReturn(1);
    when(resultSet.getString(FIELD_CREATED)).thenReturn("2024-01-01");
    when(resultSet.getInt(FIELD_CREATEDBY)).thenReturn(2);
    when(resultSet.getString(FIELD_UPDATED)).thenReturn("2024-06-01");
    when(resultSet.getInt(FIELD_UPDATEDBY)).thenReturn(3);
    when(resultSet.getString(FIELD_DELETED)).thenReturn("2024-12-01");
    when(resultSet.getInt(FIELD_DELETEDBY)).thenReturn(4);
    when(resultSet.getInt(FIELD_PLAYER_FK)).thenReturn(100);
    when(resultSet.getBoolean(FIELD_VALUE)).thenReturn(true);
    when(resultSet.getInt(FIELD_SETTING_FK)).thenReturn(settingFk);
    when(settingService.findById(settingFk)).thenReturn(Optional.empty());

    SettingPlayerEntry result = SettingPlayerMapper.mapSettingPlayer(resultSet, settingService);

    assertAll(
        () -> assertEquals(settingFk, result.getSettingFk()),
        () -> assertEquals(null, result.getSettingEntry())
    );
  }

  @Test
  void mapSettingPlayerPropagatesSQLException() throws SQLException {
    when(resultSet.getInt(FIELD_ID)).thenThrow(new SQLException("db error"));
    assertThrows(SQLException.class, () -> SettingPlayerMapper.mapSettingPlayer(resultSet, settingService));
  }
}