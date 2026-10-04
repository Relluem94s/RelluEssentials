package de.relluem94.minecraft.server.spigot.essentials.persistence.dao;

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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.SettingEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.SettingPlayerEntry;
import de.relluem94.minecraft.server.spigot.essentials.persistence.jdbc.QueryExecutor;
import de.relluem94.minecraft.server.spigot.essentials.persistence.jdbc.RowMapper;
import de.relluem94.minecraft.server.spigot.essentials.persistence.jdbc.StatementConfigurer;
import de.relluem94.minecraft.server.spigot.essentials.services.SettingService;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SettingPlayerDaoTest {

  @Mock
  private QueryExecutor queryExecutor;

  @Mock
  private ServiceContext serviceContext;

  @Mock
  private SettingService settingService;

  @Mock
  private PreparedStatement preparedStatement;

  private SettingPlayerDao settingPlayerDao;

  @BeforeEach
  void setUp() {
    lenient().when(serviceContext.getSettingService()).thenReturn(settingService);
    settingPlayerDao = new SettingPlayerDao(queryExecutor, serviceContext);
  }

  @Test
  void findAllByPlayerIdReturnsListFromQueryExecutor() {
    SettingPlayerEntry entry = new SettingPlayerEntry();
    when(queryExecutor.queryList(eq("getAllSettingPlayersByPlayerId.sql"), any(StatementConfigurer.class), any()))
        .thenReturn(List.of(entry));

    List<SettingPlayerEntry> result = settingPlayerDao.findAllByPlayerId(1);

    assertEquals(1, result.size());
    assertEquals(entry, result.getFirst());
  }

  @Test
  void findAllByPlayerIdSetsPlayerIdOnPreparedStatement() throws SQLException {
    ArgumentCaptor<StatementConfigurer> captor = ArgumentCaptor.forClass(StatementConfigurer.class);
    when(queryExecutor.queryList(eq("getAllSettingPlayersByPlayerId.sql"), captor.capture(), any()))
        .thenReturn(List.of());

    settingPlayerDao.findAllByPlayerId(42);

    captor.getValue().configure(preparedStatement);
    verify(preparedStatement).setInt(1, 42);
  }

  @Test
  void findAllByPlayerIdReturnsEmptyListWhenNoneFound() {
    when(queryExecutor.queryList(eq("getAllSettingPlayersByPlayerId.sql"), any(StatementConfigurer.class), any()))
        .thenReturn(List.of());

    List<SettingPlayerEntry> result = settingPlayerDao.findAllByPlayerId(99);

    assertEquals(0, result.size());
  }

  @Test
  void findAllByPlayerIdPropagatesException() {
    when(queryExecutor.queryList(eq("getAllSettingPlayersByPlayerId.sql"), any(StatementConfigurer.class), any()))
        .thenThrow(new RuntimeException("db error"));

    assertThrows(RuntimeException.class, () -> settingPlayerDao.findAllByPlayerId(1));
  }

  @Test
  void findByIdReturnsPopulatedOptionalWhenEntryExists() {
    SettingPlayerEntry entry = new SettingPlayerEntry();
    when(queryExecutor.querySingle(eq("getSettingPlayerById.sql"), any(StatementConfigurer.class), any()))
        .thenReturn(entry);

    Optional<SettingPlayerEntry> result = settingPlayerDao.findById(1);

    assertTrue(result.isPresent());
    assertEquals(entry, result.get());
  }

  @Test
  void findByIdSetsIdOnPreparedStatement() throws SQLException {
    ArgumentCaptor<StatementConfigurer> captor = ArgumentCaptor.forClass(StatementConfigurer.class);
    when(queryExecutor.querySingle(eq("getSettingPlayerById.sql"), captor.capture(), any()))
        .thenReturn(null);

    settingPlayerDao.findById(7);

    captor.getValue().configure(preparedStatement);
    verify(preparedStatement).setInt(1, 7);
  }

  @Test
  void findByIdReturnsEmptyOptionalWhenNotFound() {
    when(queryExecutor.querySingle(eq("getSettingPlayerById.sql"), any(StatementConfigurer.class), any()))
        .thenReturn(null);

    Optional<SettingPlayerEntry> result = settingPlayerDao.findById(99);

    assertTrue(result.isEmpty());
  }

  @Test
  void findByIdPropagatesException() {
    when(queryExecutor.querySingle(eq("getSettingPlayerById.sql"), any(StatementConfigurer.class), any()))
        .thenThrow(new RuntimeException("db error"));

    assertThrows(RuntimeException.class, () -> settingPlayerDao.findById(1));
  }

  @Test
  void insertSetsAllParametersOnPreparedStatement() throws SQLException {
    SettingPlayerEntry entry = new SettingPlayerEntry();
    entry.setCreatedBy(1);
    entry.setPlayerFk(10);
    entry.setSettingFk(5);
    entry.setValue(true);

    ArgumentCaptor<StatementConfigurer> captor = ArgumentCaptor.forClass(StatementConfigurer.class);
    doNothing().when(queryExecutor).executeUpdate(eq("insertSettingPlayer.sql"), captor.capture());

    settingPlayerDao.insert(entry);

    captor.getValue().configure(preparedStatement);
    verify(preparedStatement).setInt(1, entry.getCreatedBy());
    verify(preparedStatement).setInt(2, entry.getPlayerFk());
    verify(preparedStatement).setInt(3, entry.getSettingFk());
    verify(preparedStatement).setBoolean(4, entry.isValue());
  }

  @Test
  void insertPropagatesException() {
    SettingPlayerEntry entry = new SettingPlayerEntry();
    entry.setCreatedBy(1);
    entry.setPlayerFk(10);
    entry.setSettingFk(5);
    entry.setValue(false);

    doThrow(new RuntimeException("db error")).when(queryExecutor)
        .executeUpdate(eq("insertSettingPlayer.sql"), any(StatementConfigurer.class));

    assertThrows(RuntimeException.class, () -> settingPlayerDao.insert(entry));
  }

  @Test
  void updateSetsAllParametersOnPreparedStatement() throws SQLException {
    SettingPlayerEntry entry = new SettingPlayerEntry();
    entry.setId(3);
    entry.setUpdatedBy(2);
    entry.setValue(false);

    ArgumentCaptor<StatementConfigurer> captor = ArgumentCaptor.forClass(StatementConfigurer.class);
    doNothing().when(queryExecutor).executeUpdate(eq("updateSettingPlayer.sql"), captor.capture());

    settingPlayerDao.update(entry);

    captor.getValue().configure(preparedStatement);
    verify(preparedStatement).setInt(1, entry.getUpdatedBy());
    verify(preparedStatement).setBoolean(2, entry.isValue());
    verify(preparedStatement).setInt(3, entry.getId());
  }

  @Test
  void updatePropagatesException() {
    SettingPlayerEntry entry = new SettingPlayerEntry();
    entry.setId(3);
    entry.setUpdatedBy(2);
    entry.setValue(true);

    doThrow(new RuntimeException("db error")).when(queryExecutor)
        .executeUpdate(eq("updateSettingPlayer.sql"), any(StatementConfigurer.class));

    assertThrows(RuntimeException.class, () -> settingPlayerDao.update(entry));
  }

  @Test
  void softDeleteSetsAllParametersOnPreparedStatement() throws SQLException {
    ArgumentCaptor<StatementConfigurer> captor = ArgumentCaptor.forClass(StatementConfigurer.class);
    doNothing().when(queryExecutor).executeUpdate(eq("deleteSettingPlayer.sql"), captor.capture());

    settingPlayerDao.softDelete(8, 4);

    captor.getValue().configure(preparedStatement);
    verify(preparedStatement).setInt(1, 4);
    verify(preparedStatement).setInt(2, 8);
  }

  @Test
  void softDeletePropagatesException() {
    doThrow(new RuntimeException("db error")).when(queryExecutor)
        .executeUpdate(eq("deleteSettingPlayer.sql"), any(StatementConfigurer.class));

    assertThrows(RuntimeException.class, () -> settingPlayerDao.softDelete(8, 4));
  }

  @Test
  void findAllByPlayerIdInvokesMapperWithResultSet() throws SQLException {
    SettingEntry settingEntry = new SettingEntry();

    try (ResultSet resultSet = mock(ResultSet.class)) {
      lenient().when(resultSet.getInt(FIELD_ID)).thenReturn(1);
      lenient().when(resultSet.getString(FIELD_CREATED)).thenReturn("2024-01-01");
      lenient().when(resultSet.getInt(FIELD_CREATEDBY)).thenReturn(1);
      lenient().when(resultSet.getString(FIELD_UPDATED)).thenReturn("2024-01-02");
      lenient().when(resultSet.getInt(FIELD_UPDATEDBY)).thenReturn(2);
      lenient().when(resultSet.getString(FIELD_DELETED)).thenReturn(null);
      lenient().when(resultSet.getInt(FIELD_DELETEDBY)).thenReturn(0);
      lenient().when(resultSet.getInt(FIELD_PLAYER_FK)).thenReturn(10);
      lenient().when(resultSet.getInt(FIELD_SETTING_FK)).thenReturn(5);
      lenient().when(resultSet.getBoolean(FIELD_VALUE)).thenReturn(true);
      when(settingService.findById(5)).thenReturn(Optional.of(settingEntry));

      when(queryExecutor.queryList(eq("getAllSettingPlayersByPlayerId.sql"), any(StatementConfigurer.class), any()))
          .thenAnswer(invocation -> {
            RowMapper<SettingPlayerEntry> mapper = invocation.getArgument(2);
            return List.of(mapper.map(resultSet));
          });

      List<SettingPlayerEntry> result = settingPlayerDao.findAllByPlayerId(1);

      assertEquals(1, result.size());
      SettingPlayerEntry mapped = result.getFirst();
      assertEquals(1, mapped.getId());
      assertEquals("2024-01-01", mapped.getCreated());
      assertEquals(1, mapped.getCreatedBy());
      assertEquals("2024-01-02", mapped.getUpdated());
      assertEquals(2, mapped.getUpdatedBy());
      assertNull(mapped.getDeleted());
      assertEquals(0, mapped.getDeletedBy());
      assertEquals(10, mapped.getPlayerFk());
      assertEquals(5, mapped.getSettingFk());
      assertTrue(mapped.isValue());
      assertEquals(settingEntry, mapped.getSettingEntry());
    }
  }

  @Test
  void findByIdInvokesMapperWithResultSet() throws SQLException {
    SettingEntry settingEntry = new SettingEntry();

    try (ResultSet resultSet = mock(ResultSet.class)) {
      lenient().when(resultSet.getInt(FIELD_ID)).thenReturn(2);
      lenient().when(resultSet.getString(FIELD_CREATED)).thenReturn("2024-02-01");
      lenient().when(resultSet.getInt(FIELD_CREATEDBY)).thenReturn(3);
      lenient().when(resultSet.getString(FIELD_UPDATED)).thenReturn("2024-02-02");
      lenient().when(resultSet.getInt(FIELD_UPDATEDBY)).thenReturn(4);
      lenient().when(resultSet.getString(FIELD_DELETED)).thenReturn(null);
      lenient().when(resultSet.getInt(FIELD_DELETEDBY)).thenReturn(0);
      lenient().when(resultSet.getInt(FIELD_PLAYER_FK)).thenReturn(20);
      lenient().when(resultSet.getInt(FIELD_SETTING_FK)).thenReturn(8);
      lenient().when(resultSet.getBoolean(FIELD_VALUE)).thenReturn(false);
      when(settingService.findById(8)).thenReturn(Optional.of(settingEntry));

      when(queryExecutor.querySingle(eq("getSettingPlayerById.sql"), any(StatementConfigurer.class), any()))
          .thenAnswer(invocation -> {
            RowMapper<SettingPlayerEntry> mapper = invocation.getArgument(2);
            return mapper.map(resultSet);
          });

      Optional<SettingPlayerEntry> result = settingPlayerDao.findById(2);

      assertTrue(result.isPresent());
      SettingPlayerEntry mapped = result.get();
      assertEquals(2, mapped.getId());
      assertEquals("2024-02-01", mapped.getCreated());
      assertEquals(3, mapped.getCreatedBy());
      assertEquals("2024-02-02", mapped.getUpdated());
      assertEquals(4, mapped.getUpdatedBy());
      assertNull(mapped.getDeleted());
      assertEquals(0, mapped.getDeletedBy());
      assertEquals(20, mapped.getPlayerFk());
      assertEquals(8, mapped.getSettingFk());
      assertFalse(mapped.isValue());
      assertEquals(settingEntry, mapped.getSettingEntry());
    }
  }
}