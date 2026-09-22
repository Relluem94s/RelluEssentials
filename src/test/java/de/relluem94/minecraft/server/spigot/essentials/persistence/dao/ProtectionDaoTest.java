package de.relluem94.minecraft.server.spigot.essentials.persistence.dao;

import static de.relluem94.minecraft.server.spigot.essentials.constants.db.DatabaseMappings.FIELD_CREATED;
import static de.relluem94.minecraft.server.spigot.essentials.constants.db.DatabaseMappings.FIELD_CREATEDBY;
import static de.relluem94.minecraft.server.spigot.essentials.constants.db.DatabaseMappings.FIELD_DELETED;
import static de.relluem94.minecraft.server.spigot.essentials.constants.db.DatabaseMappings.FIELD_DELETEDBY;
import static de.relluem94.minecraft.server.spigot.essentials.constants.db.DatabaseMappings.FIELD_FLAGS;
import static de.relluem94.minecraft.server.spigot.essentials.constants.db.DatabaseMappings.FIELD_ID;
import static de.relluem94.minecraft.server.spigot.essentials.constants.db.DatabaseMappings.FIELD_LOCATION_FK;
import static de.relluem94.minecraft.server.spigot.essentials.constants.db.DatabaseMappings.FIELD_MATERIAL_NAME;
import static de.relluem94.minecraft.server.spigot.essentials.constants.db.DatabaseMappings.FIELD_RIGHTS;
import static de.relluem94.minecraft.server.spigot.essentials.constants.db.DatabaseMappings.FIELD_UPDATED;
import static de.relluem94.minecraft.server.spigot.essentials.constants.db.DatabaseMappings.FIELD_UPDATEDBY;
import static de.relluem94.minecraft.server.spigot.essentials.constants.db.DatabaseMappings.FIELD_VALUE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.relluem94.minecraft.server.spigot.essentials.models.pojo.LocationEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.ProtectionEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.ProtectionLockEntry;
import de.relluem94.minecraft.server.spigot.essentials.persistence.jdbc.QueryExecutor;
import de.relluem94.minecraft.server.spigot.essentials.persistence.jdbc.RowMapper;
import de.relluem94.minecraft.server.spigot.essentials.persistence.jdbc.StatementConfigurer;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProtectionDaoTest {

  @Mock
  private QueryExecutor queryExecutor;

  @Mock
  private PreparedStatement preparedStatement;

  private ProtectionDao protectionDao;

  @BeforeEach
  void setUp() {
    protectionDao = new ProtectionDao(queryExecutor);
  }

  @Test
  void deleteOutdatedProtectionsReturnsCountFromQueryExecutor() {
    when(queryExecutor.executeUpdateWithCount(eq("cleanupProtections.sql"), any(StatementConfigurer.class))).thenReturn(5);

    int result = protectionDao.deleteOutdatedProtections();

    assertEquals(5, result);
  }

  @Test
  void deleteOutdatedProtectionsPropagatesException() {
    when(queryExecutor.executeUpdateWithCount(eq("cleanupProtections.sql"), any(StatementConfigurer.class)))
        .thenThrow(new RuntimeException("db error"));

    assertThrows(RuntimeException.class, () -> protectionDao.deleteOutdatedProtections());
  }

  @Test
  void findOutdatedProtectionIdsReturnsListFromQueryExecutor() {
    List<Long> expected = List.of(1L, 2L, 3L);
    when(queryExecutor.queryList(eq("findOutdatedProtectionIds.sql"), any(StatementConfigurer.class),
        ArgumentMatchers.<RowMapper<Long>>any())).thenReturn(expected);

    List<Long> result = protectionDao.findOutdatedProtectionIds();

    assertEquals(expected, result);
  }

  @Test
  void findOutdatedProtectionIdsPropagatesException() {
    when(queryExecutor.queryList(eq("findOutdatedProtectionIds.sql"), any(StatementConfigurer.class),
        ArgumentMatchers.<RowMapper<Long>>any())).thenThrow(new RuntimeException("db error"));

    assertThrows(RuntimeException.class, () -> protectionDao.findOutdatedProtectionIds());
  }

  @Test
  void findOutdatedProtectionIdsMapsIdFromResultSet() throws SQLException {
    try (ResultSet resultSet = mock(ResultSet.class)) {
      when(resultSet.getLong("id")).thenReturn(42L);

      when(queryExecutor.queryList(eq("findOutdatedProtectionIds.sql"), any(StatementConfigurer.class),
          any())).thenAnswer(invocation -> {
        StatementConfigurer configurer = invocation.getArgument(1);
        configurer.configure(preparedStatement);
        RowMapper<Long> mapper = invocation.getArgument(2);
        return List.of(mapper.map(resultSet));
      });

      List<Long> result = protectionDao.findOutdatedProtectionIds();

      assertEquals(1, result.size());
      assertEquals(42L, result.getFirst());
    }
  }

  @Test
  void findAllLocksReturnsListFromQueryExecutor() {
    List<ProtectionLockEntry> expected = Collections.singletonList(new ProtectionLockEntry());
    when(queryExecutor.queryList(eq("getProtectionLocks.sql"), any(StatementConfigurer.class),
        ArgumentMatchers.<RowMapper<ProtectionLockEntry>>any())).thenReturn(expected);

    List<ProtectionLockEntry> result = protectionDao.findAllLocks();

    assertEquals(expected, result);
  }

  @Test
  void findAllLocksPropagatesException() {
    when(queryExecutor.queryList(eq("getProtectionLocks.sql"), any(StatementConfigurer.class),
        ArgumentMatchers.<RowMapper<ProtectionLockEntry>>any())).thenThrow(new RuntimeException("db error"));

    assertThrows(RuntimeException.class, () -> protectionDao.findAllLocks());
  }

  @Test
  void findAllLocksMapsAllFieldsFromResultSet() throws SQLException {
    try (ResultSet resultSet = mock(ResultSet.class)) {
      when(resultSet.getInt(FIELD_ID)).thenReturn(1);
      when(resultSet.getString(FIELD_CREATED)).thenReturn("2024-01-01");
      when(resultSet.getInt(FIELD_CREATEDBY)).thenReturn(2);
      when(resultSet.getString(FIELD_UPDATED)).thenReturn("2024-01-02");
      when(resultSet.getInt(FIELD_UPDATEDBY)).thenReturn(3);
      when(resultSet.getString(FIELD_DELETED)).thenReturn(null);
      when(resultSet.getInt(FIELD_DELETEDBY)).thenReturn(0);
      when(resultSet.getString(FIELD_VALUE)).thenReturn("CHEST");

      when(queryExecutor.queryList(eq("getProtectionLocks.sql"), any(StatementConfigurer.class),
          any())).thenAnswer(invocation -> {
        StatementConfigurer configurer = invocation.getArgument(1);
        configurer.configure(preparedStatement);
        RowMapper<ProtectionLockEntry> mapper = invocation.getArgument(2);
        return List.of(mapper.map(resultSet));
      });

      List<ProtectionLockEntry> result = protectionDao.findAllLocks();

      ProtectionLockEntry entry = result.getFirst();
      assertEquals(1, entry.getId());
      assertEquals("2024-01-01", entry.getCreated());
      assertEquals(2, entry.getCreatedBy());
      assertEquals("2024-01-02", entry.getUpdated());
      assertEquals(3, entry.getUpdatedBy());
      assertNull(entry.getDeleted());
      assertEquals(0, entry.getDeletedBy());
      assertEquals(Material.CHEST, entry.getValue());
    }
  }

  @Test
  void deleteByIdSetsPlayerIdAndIdOnPreparedStatement() throws SQLException {
    ArgumentCaptor<StatementConfigurer> captor = ArgumentCaptor.forClass(StatementConfigurer.class);
    doNothing().when(queryExecutor).executeUpdate(eq("deleteProtection.sql"), captor.capture());

    protectionDao.deleteById(10, 99);

    captor.getValue().configure(preparedStatement);
    verify(preparedStatement).setInt(1, 99);
    verify(preparedStatement).setInt(2, 10);
  }

  @Test
  void deleteByIdPropagatesException() {
    doThrow(new RuntimeException("db error")).when(queryExecutor)
        .executeUpdate(eq("deleteProtection.sql"), any(StatementConfigurer.class));

    assertThrows(RuntimeException.class, () -> protectionDao.deleteById(10, 99));
  }

  @Test
  void findAllReturnsListFromQueryExecutor() {
    List<ProtectionEntry> expected = Collections.singletonList(new ProtectionEntry());
    when(queryExecutor.queryList(eq("getProtections.sql"), any(StatementConfigurer.class),
        ArgumentMatchers.<RowMapper<ProtectionEntry>>any())).thenReturn(expected);

    List<ProtectionEntry> result = protectionDao.findAll();

    assertEquals(expected, result);
  }

  @Test
  void findAllPropagatesException() {
    when(queryExecutor.queryList(eq("getProtections.sql"), any(StatementConfigurer.class),
        ArgumentMatchers.<RowMapper<ProtectionEntry>>any())).thenThrow(new RuntimeException("db error"));

    assertThrows(RuntimeException.class, () -> protectionDao.findAll());
  }

  @Test
  void findAllMapsAllFieldsFromResultSet() throws SQLException {
    try (ResultSet resultSet = mock(ResultSet.class)) {
      when(resultSet.getInt(FIELD_ID)).thenReturn(7);
      when(resultSet.getString(FIELD_CREATED)).thenReturn("2024-02-01");
      when(resultSet.getInt(FIELD_CREATEDBY)).thenReturn(1);
      when(resultSet.getString(FIELD_UPDATED)).thenReturn("2024-02-02");
      when(resultSet.getInt(FIELD_UPDATEDBY)).thenReturn(2);
      when(resultSet.getString(FIELD_DELETED)).thenReturn(null);
      when(resultSet.getInt(FIELD_DELETEDBY)).thenReturn(0);
      when(resultSet.getInt(FIELD_LOCATION_FK)).thenReturn(5);
      when(resultSet.getString(FIELD_FLAGS)).thenReturn("{\"public\":true}");
      when(resultSet.getString(FIELD_RIGHTS)).thenReturn("{\"admin\":true}");
      when(resultSet.getString(FIELD_MATERIAL_NAME)).thenReturn("CHEST");

      when(queryExecutor.queryList(eq("getProtections.sql"), any(StatementConfigurer.class),
          any())).thenAnswer(invocation -> {
        StatementConfigurer configurer = invocation.getArgument(1);
        configurer.configure(preparedStatement);
        RowMapper<ProtectionEntry> mapper = invocation.getArgument(2);
        return List.of(mapper.map(resultSet));
      });

      List<ProtectionEntry> result = protectionDao.findAll();

      ProtectionEntry entry = result.getFirst();
      assertEquals(7, entry.getId());
      assertEquals("2024-02-01", entry.getCreated());
      assertEquals(1, entry.getCreatedBy());
      assertEquals("2024-02-02", entry.getUpdated());
      assertEquals(2, entry.getUpdatedBy());
      assertNull(entry.getDeleted());
      assertEquals(0, entry.getDeletedBy());
      assertEquals(5, entry.getLocationFk());
      assertNotNull(entry.getFlags());
      assertNotNull(entry.getRights());
      assertEquals("CHEST", entry.getMaterialName());
    }
  }

  @Test
  void findAllMapsNullFlagsToEmptyJsonObject() throws SQLException {
    try (ResultSet resultSet = mock(ResultSet.class)) {
      when(resultSet.getInt(FIELD_ID)).thenReturn(1);
      when(resultSet.getString(FIELD_CREATED)).thenReturn(null);
      when(resultSet.getInt(FIELD_CREATEDBY)).thenReturn(0);
      when(resultSet.getString(FIELD_UPDATED)).thenReturn(null);
      when(resultSet.getInt(FIELD_UPDATEDBY)).thenReturn(0);
      when(resultSet.getString(FIELD_DELETED)).thenReturn(null);
      when(resultSet.getInt(FIELD_DELETEDBY)).thenReturn(0);
      when(resultSet.getInt(FIELD_LOCATION_FK)).thenReturn(0);
      when(resultSet.getString(FIELD_FLAGS)).thenReturn(null);
      when(resultSet.getString(FIELD_RIGHTS)).thenReturn(null);
      when(resultSet.getString(FIELD_MATERIAL_NAME)).thenReturn(null);

      when(queryExecutor.queryList(eq("getProtections.sql"), any(StatementConfigurer.class),
          any())).thenAnswer(invocation -> {
        StatementConfigurer configurer = invocation.getArgument(1);
        configurer.configure(preparedStatement);
        RowMapper<ProtectionEntry> mapper = invocation.getArgument(2);
        return List.of(mapper.map(resultSet));
      });

      List<ProtectionEntry> result = protectionDao.findAll();

      ProtectionEntry entry = result.getFirst();
      assertNotNull(entry.getFlags());
      assertNotNull(entry.getRights());
      assertEquals(0, entry.getFlags().length());
      assertEquals(0, entry.getRights().length());
    }
  }

  @Test
  void insertProtectionSetsAllFieldsOnPreparedStatement() throws SQLException {
    ProtectionEntry protectionEntry = buildProtectionEntry();
    ArgumentCaptor<StatementConfigurer> captor = ArgumentCaptor.forClass(StatementConfigurer.class);
    doNothing().when(queryExecutor).executeUpdate(eq("insertProtection.sql"), captor.capture());

    protectionDao.insertProtection(protectionEntry);

    captor.getValue().configure(preparedStatement);
    verify(preparedStatement).setInt(1, protectionEntry.getCreatedBy());
    verify(preparedStatement).setInt(2, protectionEntry.getLocationEntry().getId());
    verify(preparedStatement).setString(3, protectionEntry.getMaterialName());
    verify(preparedStatement).setString(4, protectionEntry.getFlags().toString());
    verify(preparedStatement).setString(5, protectionEntry.getRights().toString());
  }

  @Test
  void insertProtectionPropagatesException() {
    ProtectionEntry protectionEntry = buildProtectionEntry();
    doThrow(new RuntimeException("db error")).when(queryExecutor)
        .executeUpdate(eq("insertProtection.sql"), any(StatementConfigurer.class));

    assertThrows(RuntimeException.class, () -> protectionDao.insertProtection(protectionEntry));
  }

  @Test
  void updateProtectionFlagSetsAllFieldsOnPreparedStatement() throws SQLException {
    ProtectionEntry protectionEntry = buildProtectionEntry();
    ArgumentCaptor<StatementConfigurer> captor = ArgumentCaptor.forClass(StatementConfigurer.class);
    doNothing().when(queryExecutor).executeUpdate(eq("updateProtectionFlags.sql"), captor.capture());

    protectionDao.updateProtectionFlag(protectionEntry);

    captor.getValue().configure(preparedStatement);
    verify(preparedStatement).setInt(1, protectionEntry.getLocationEntry().getPlayerId());
    verify(preparedStatement).setString(2, protectionEntry.getFlags().toString());
    verify(preparedStatement).setInt(3, protectionEntry.getId());
  }

  @Test
  void updateProtectionFlagPropagatesException() {
    ProtectionEntry protectionEntry = buildProtectionEntry();
    doThrow(new RuntimeException("db error")).when(queryExecutor)
        .executeUpdate(eq("updateProtectionFlags.sql"), any(StatementConfigurer.class));

    assertThrows(RuntimeException.class, () -> protectionDao.updateProtectionFlag(protectionEntry));
  }

  @Test
  void updateProtectionRightSetsAllFieldsOnPreparedStatement() throws SQLException {
    ProtectionEntry protectionEntry = buildProtectionEntry();
    ArgumentCaptor<StatementConfigurer> captor = ArgumentCaptor.forClass(StatementConfigurer.class);
    doNothing().when(queryExecutor).executeUpdate(eq("updateProtectionRights.sql"), captor.capture());

    protectionDao.updateProtectionRight(protectionEntry);

    captor.getValue().configure(preparedStatement);
    verify(preparedStatement).setInt(1, protectionEntry.getLocationEntry().getPlayerId());
    verify(preparedStatement).setString(2, protectionEntry.getRights().toString());
    verify(preparedStatement).setInt(3, protectionEntry.getId());
  }

  @Test
  void updateProtectionRightPropagatesException() {
    ProtectionEntry protectionEntry = buildProtectionEntry();
    doThrow(new RuntimeException("db error")).when(queryExecutor)
        .executeUpdate(eq("updateProtectionRights.sql"), any(StatementConfigurer.class));

    assertThrows(RuntimeException.class, () -> protectionDao.updateProtectionRight(protectionEntry));
  }

  @Test
  void getProtectionByLocationReturnsSingleEntry() {
    World world = mock(World.class);
    lenient().when(world.getName()).thenReturn("world");
    Location location = new Location(world, 1.0, 64.0, -1.0);
    ProtectionEntry expected = buildProtectionEntry();
    when(queryExecutor.querySingle(eq("getProtectionByLocation.sql"), any(StatementConfigurer.class),
        any())).thenReturn(expected);

    ProtectionEntry result = protectionDao.getProtectionByLocation(location);

    assertEquals(expected, result);
  }

  @Test
  void getProtectionByLocationSetsAllCoordinatesAndWorldNameOnPreparedStatement() throws SQLException {
    World world = mock(World.class);
    when(world.getName()).thenReturn("world");
    Location location = new Location(world, 10.5, 64.0, -5.5);
    ArgumentCaptor<StatementConfigurer> captor = ArgumentCaptor.forClass(StatementConfigurer.class);
    when(queryExecutor.querySingle(eq("getProtectionByLocation.sql"), captor.capture(), any())).thenReturn(null);

    protectionDao.getProtectionByLocation(location);

    captor.getValue().configure(preparedStatement);
    verify(preparedStatement).setFloat(1, (float) location.getX());
    verify(preparedStatement).setFloat(2, (float) location.getY());
    verify(preparedStatement).setFloat(3, (float) location.getZ());
    verify(preparedStatement).setString(4, "world");
  }

  @Test
  void getProtectionByLocationMapsAllFieldsFromResultSet() throws SQLException {
    World world = mock(World.class);
    when(world.getName()).thenReturn("world");
    Location location = new Location(world, 1.0, 64.0, -1.0);

    try (ResultSet resultSet = mock(ResultSet.class)) {
      when(resultSet.getInt(FIELD_ID)).thenReturn(3);
      when(resultSet.getString(FIELD_CREATED)).thenReturn("2024-03-01");
      when(resultSet.getInt(FIELD_CREATEDBY)).thenReturn(1);
      when(resultSet.getString(FIELD_UPDATED)).thenReturn("2024-03-02");
      when(resultSet.getInt(FIELD_UPDATEDBY)).thenReturn(2);
      when(resultSet.getString(FIELD_DELETED)).thenReturn(null);
      when(resultSet.getInt(FIELD_DELETEDBY)).thenReturn(0);
      when(resultSet.getInt(FIELD_LOCATION_FK)).thenReturn(8);
      when(resultSet.getString(FIELD_FLAGS)).thenReturn("{\"locked\":true}");
      when(resultSet.getString(FIELD_RIGHTS)).thenReturn("{\"owner\":1}");
      when(resultSet.getString(FIELD_MATERIAL_NAME)).thenReturn("FURNACE");

      when(queryExecutor.querySingle(eq("getProtectionByLocation.sql"), any(StatementConfigurer.class),
          any())).thenAnswer(invocation -> {
        StatementConfigurer configurer = invocation.getArgument(1);
        configurer.configure(preparedStatement);
        RowMapper<ProtectionEntry> mapper = invocation.getArgument(2);
        return mapper.map(resultSet);
      });

      ProtectionEntry result = protectionDao.getProtectionByLocation(location);

      assertEquals(3, result.getId());
      assertEquals("2024-03-01", result.getCreated());
      assertEquals(1, result.getCreatedBy());
      assertEquals("2024-03-02", result.getUpdated());
      assertEquals(2, result.getUpdatedBy());
      assertNull(result.getDeleted());
      assertEquals(0, result.getDeletedBy());
      assertEquals(8, result.getLocationFk());
      assertNotNull(result.getFlags());
      assertNotNull(result.getRights());
      assertEquals("FURNACE", result.getMaterialName());
    }
  }

  @Test
  void getProtectionByLocationPropagatesException() {
    World world = mock(World.class);
    lenient().when(world.getName()).thenReturn("world");
    Location location = new Location(world, 1.0, 64.0, -1.0);
    when(queryExecutor.querySingle(eq("getProtectionByLocation.sql"), any(StatementConfigurer.class),
        any())).thenThrow(new RuntimeException("db error"));

    assertThrows(RuntimeException.class, () -> protectionDao.getProtectionByLocation(location));
  }

  @Test
  void deleteOutdatedProtectionsPassesNoOpStatementConfigurer() throws SQLException {
    ArgumentCaptor<StatementConfigurer> captor = ArgumentCaptor.forClass(StatementConfigurer.class);
    when(queryExecutor.executeUpdateWithCount(eq("cleanupProtections.sql"), captor.capture())).thenReturn(0);

    protectionDao.deleteOutdatedProtections();

    captor.getValue().configure(preparedStatement);
    verify(preparedStatement, org.mockito.Mockito.never()).setInt(any(Integer.class), any(Integer.class));
    verify(preparedStatement, org.mockito.Mockito.never()).setString(any(Integer.class), any(String.class));
  }

  private ProtectionEntry buildProtectionEntry() {
    ProtectionEntry entry = new ProtectionEntry();
    entry.setId(1);
    entry.setCreatedBy(10);
    entry.setMaterialName("CHEST");
    entry.setFlags(new JSONObject("{\"public\":false}"));
    entry.setRights(new JSONObject("{\"owner\":10}"));

    LocationEntry locationEntry = new LocationEntry();
    locationEntry.setId(5);
    locationEntry.setPlayerId(10);
    entry.setLocationEntry(locationEntry);

    return entry;
  }
}