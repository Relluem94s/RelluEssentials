package de.relluem94.minecraft.server.spigot.essentials.persistence.dao;

import static de.relluem94.minecraft.server.spigot.essentials.constants.db.DatabaseMappings.FIELD_CREATED;
import static de.relluem94.minecraft.server.spigot.essentials.constants.db.DatabaseMappings.FIELD_CREATEDBY;
import static de.relluem94.minecraft.server.spigot.essentials.constants.db.DatabaseMappings.FIELD_DELETED;
import static de.relluem94.minecraft.server.spigot.essentials.constants.db.DatabaseMappings.FIELD_DELETEDBY;
import static de.relluem94.minecraft.server.spigot.essentials.constants.db.DatabaseMappings.FIELD_FOOD;
import static de.relluem94.minecraft.server.spigot.essentials.constants.db.DatabaseMappings.FIELD_HEALTH;
import static de.relluem94.minecraft.server.spigot.essentials.constants.db.DatabaseMappings.FIELD_ID;
import static de.relluem94.minecraft.server.spigot.essentials.constants.db.DatabaseMappings.FIELD_INVENTORY;
import static de.relluem94.minecraft.server.spigot.essentials.constants.db.DatabaseMappings.FIELD_NAME;
import static de.relluem94.minecraft.server.spigot.essentials.constants.db.DatabaseMappings.FIELD_PLAYER_FK;
import static de.relluem94.minecraft.server.spigot.essentials.constants.db.DatabaseMappings.FIELD_SETTING_FK;
import static de.relluem94.minecraft.server.spigot.essentials.constants.db.DatabaseMappings.FIELD_TOTAL_EXPERIENCE;
import static de.relluem94.minecraft.server.spigot.essentials.constants.db.DatabaseMappings.FIELD_UPDATED;
import static de.relluem94.minecraft.server.spigot.essentials.constants.db.DatabaseMappings.FIELD_UPDATEDBY;
import static de.relluem94.minecraft.server.spigot.essentials.constants.db.DatabaseMappings.FIELD_VALUE;
import static de.relluem94.minecraft.server.spigot.essentials.constants.db.DatabaseMappings.FIELD_WORLD_GORUP_FK;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.GroupEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.PlayerEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.WorldEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.WorldGroupEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.WorldGroupInventoryEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.WorldGroupSettingEntry;
import de.relluem94.minecraft.server.spigot.essentials.persistence.jdbc.QueryExecutor;
import de.relluem94.minecraft.server.spigot.essentials.persistence.jdbc.RowMapper;
import de.relluem94.minecraft.server.spigot.essentials.persistence.jdbc.StatementConfigurer;
import de.relluem94.minecraft.server.spigot.essentials.services.SettingService;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class WorldGroupDaoTest {

  @Mock
  private QueryExecutor queryExecutor;

  @Mock
  private ServiceContext serviceContext;

  @Mock
  private SettingService settingService;

  @Mock
  private PreparedStatement preparedStatement;

  private WorldGroupDao worldGroupDao;

  @BeforeEach
  void setUp() {
    worldGroupDao = new WorldGroupDao(queryExecutor, serviceContext);
  }

  @Test
  void findAllWorldGroupSettingsReturnsListFromQueryExecutor() {
    List<WorldGroupSettingEntry> expected = Collections.singletonList(new WorldGroupSettingEntry());
    when(queryExecutor.queryList(eq("getAllWorldGroupSettings.sql"), any(StatementConfigurer.class),
        ArgumentMatchers.<RowMapper<WorldGroupSettingEntry>>any())).thenReturn(expected);

    lenient().when(serviceContext.getSettingService()).thenReturn(settingService);

    List<WorldGroupSettingEntry> result = worldGroupDao.findAllWorldGroupSettings();

    assertEquals(expected, result);
  }

  @Test
  void findAllWorldGroupSettingsMapsAllFieldsFromResultSet() throws SQLException {
    try (ResultSet resultSet = mock(ResultSet.class)) {
      when(serviceContext.getSettingService()).thenReturn(settingService);
      when(settingService.findById(5)).thenReturn(Optional.empty());

      when(resultSet.getInt(FIELD_ID)).thenReturn(1);
      when(resultSet.getString(FIELD_CREATED)).thenReturn("2024-01-01");
      when(resultSet.getInt(FIELD_CREATEDBY)).thenReturn(2);
      when(resultSet.getString(FIELD_UPDATED)).thenReturn("2024-01-02");
      when(resultSet.getInt(FIELD_UPDATEDBY)).thenReturn(3);
      when(resultSet.getBoolean(FIELD_VALUE)).thenReturn(true);
      when(resultSet.getInt(FIELD_WORLD_GORUP_FK)).thenReturn(10);
      when(resultSet.getInt(FIELD_SETTING_FK)).thenReturn(5);

      when(queryExecutor.queryList(eq("getAllWorldGroupSettings.sql"), any(StatementConfigurer.class),
          any())).thenAnswer(invocation -> {
        StatementConfigurer configurer = invocation.getArgument(1);
        configurer.configure(preparedStatement);
        RowMapper<WorldGroupSettingEntry> mapper = invocation.getArgument(2);
        return List.of(mapper.map(resultSet));
      });

      List<WorldGroupSettingEntry> result = worldGroupDao.findAllWorldGroupSettings();

      WorldGroupSettingEntry entry = result.getFirst();
      assertEquals(1, entry.getId());
      assertEquals("2024-01-01", entry.getCreated());
      assertEquals(2, entry.getCreatedBy());
      assertEquals("2024-01-02", entry.getUpdated());
      assertEquals(3, entry.getUpdatedBy());
      assertTrue(entry.isValue());
      assertEquals(10, entry.getWorldGroupEntryFk());
      assertEquals(5, entry.getSettingEntryFk());
    }
  }

  @Test
  void findAllWorldGroupSettingsPropagatesException() {
    lenient().when(serviceContext.getSettingService()).thenReturn(settingService);
    when(queryExecutor.queryList(eq("getAllWorldGroupSettings.sql"), any(StatementConfigurer.class),
        any())).thenThrow(new RuntimeException("db error"));

    assertThrows(RuntimeException.class, () -> worldGroupDao.findAllWorldGroupSettings());
  }

  @Test
  void findAllWorldGroupsReturnsListFromQueryExecutor() {
    List<WorldGroupEntry> expected = List.of(new WorldGroupEntry());
    lenient().when(serviceContext.getSettingService()).thenReturn(settingService);
    when(queryExecutor.queryList(eq("getAllWorldGroupSettings.sql"), any(StatementConfigurer.class),
        any())).thenReturn(List.of());

    when(queryExecutor.queryList(eq("getWorldGroups.sql"), any(StatementConfigurer.class),
        ArgumentMatchers.<RowMapper<WorldGroupEntry>>any())).thenReturn(expected);


    List<WorldGroupEntry> result = worldGroupDao.findAllWorldGroups();

    assertEquals(expected, result);
  }

  @Test
  void findAllWorldGroupsMapsAllFieldsFromResultSet() throws SQLException {
    try (ResultSet resultSet = mock(ResultSet.class)) {
      lenient().when(serviceContext.getSettingService()).thenReturn(settingService);

      when(resultSet.getInt(FIELD_ID)).thenReturn(7);
      when(resultSet.getString(FIELD_CREATED)).thenReturn("2024-01-01");
      when(resultSet.getInt(FIELD_CREATEDBY)).thenReturn(1);
      when(resultSet.getString(FIELD_UPDATED)).thenReturn("2024-01-02");
      when(resultSet.getInt(FIELD_UPDATEDBY)).thenReturn(2);
      when(resultSet.getString(FIELD_DELETED)).thenReturn(null);
      when(resultSet.getInt(FIELD_DELETEDBY)).thenReturn(0);
      when(resultSet.getString(FIELD_NAME)).thenReturn("GroupAlpha");

      when(queryExecutor.queryList(eq("getAllWorldGroupSettings.sql"), any(StatementConfigurer.class),
          any())).thenReturn(List.of());
      when(queryExecutor.queryList(eq("getWorldGroups.sql"), any(StatementConfigurer.class),
          any())).thenAnswer(invocation -> {
        StatementConfigurer configurer = invocation.getArgument(1);
        configurer.configure(preparedStatement);
        RowMapper<WorldGroupEntry> mapper = invocation.getArgument(2);
        return List.of(mapper.map(resultSet));
      });

      List<WorldGroupEntry> result = worldGroupDao.findAllWorldGroups();

      WorldGroupEntry entry = result.getFirst();
      assertEquals(7, entry.getId());
      assertEquals("2024-01-01", entry.getCreated());
      assertEquals(1, entry.getCreatedBy());
      assertEquals("2024-01-02", entry.getUpdated());
      assertEquals(2, entry.getUpdatedBy());
      assertNull(entry.getDeleted());
      assertEquals(0, entry.getDeletedBy());
      assertEquals("GroupAlpha", entry.getName());
      assertNotNull(entry.getSettings());
    }
  }

  @Test
  void findAllWorldGroupsPropagatesException() {
    lenient().when(serviceContext.getSettingService()).thenReturn(settingService);
    when(queryExecutor.queryList(eq("getAllWorldGroupSettings.sql"), any(StatementConfigurer.class),
        any())).thenReturn(List.of());
    when(queryExecutor.queryList(eq("getWorldGroups.sql"), any(StatementConfigurer.class),
        any())).thenThrow(new RuntimeException("db error"));

    assertThrows(RuntimeException.class, () -> worldGroupDao.findAllWorldGroups());
  }

  @Test
  void findWorldsByGroupReturnsListFromQueryExecutor() {
    WorldGroupEntry worldGroupEntry = buildWorldGroupEntry();
    List<WorldEntry> expected = List.of(new WorldEntry());
    when(queryExecutor.queryList(eq("getWorldByGroup.sql"), any(StatementConfigurer.class),
        ArgumentMatchers.<RowMapper<WorldEntry>>any())).thenReturn(expected);

    List<WorldEntry> result = worldGroupDao.findWorldsByGroup(worldGroupEntry);

    assertEquals(expected, result);
  }

  @Test
  void findWorldsByGroupSetsGroupIdOnPreparedStatement() throws SQLException {
    WorldGroupEntry worldGroupEntry = buildWorldGroupEntry();
    ArgumentCaptor<StatementConfigurer> captor = ArgumentCaptor.forClass(StatementConfigurer.class);
    when(queryExecutor.queryList(eq("getWorldByGroup.sql"), captor.capture(), any())).thenReturn(List.of());

    worldGroupDao.findWorldsByGroup(worldGroupEntry);

    captor.getValue().configure(preparedStatement);
    verify(preparedStatement).setInt(1, worldGroupEntry.getId());
  }

  @Test
  void findWorldsByGroupMapsAllFieldsFromResultSet() throws SQLException {
    WorldGroupEntry worldGroupEntry = buildWorldGroupEntry();

    try (ResultSet resultSet = mock(ResultSet.class)) {
      when(resultSet.getInt(FIELD_ID)).thenReturn(3);
      when(resultSet.getString(FIELD_CREATED)).thenReturn("2024-02-01");
      when(resultSet.getInt(FIELD_CREATEDBY)).thenReturn(1);
      when(resultSet.getString(FIELD_UPDATED)).thenReturn("2024-02-02");
      when(resultSet.getInt(FIELD_UPDATEDBY)).thenReturn(2);
      when(resultSet.getString(FIELD_DELETED)).thenReturn(null);
      when(resultSet.getInt(FIELD_DELETEDBY)).thenReturn(0);
      when(resultSet.getString(FIELD_NAME)).thenReturn("world_nether");

      when(queryExecutor.queryList(eq("getWorldByGroup.sql"), any(StatementConfigurer.class),
          any())).thenAnswer(invocation -> {
        StatementConfigurer configurer = invocation.getArgument(1);
        configurer.configure(preparedStatement);
        RowMapper<WorldEntry> mapper = invocation.getArgument(2);
        return List.of(mapper.map(resultSet));
      });

      List<WorldEntry> result = worldGroupDao.findWorldsByGroup(worldGroupEntry);

      WorldEntry entry = result.getFirst();
      assertEquals(3, entry.getId());
      assertEquals("2024-02-01", entry.getCreated());
      assertEquals(1, entry.getCreatedBy());
      assertEquals("2024-02-02", entry.getUpdated());
      assertEquals(2, entry.getUpdatedBy());
      assertNull(entry.getDeleted());
      assertEquals(0, entry.getDeletedBy());
      assertEquals("world_nether", entry.getName());
      assertEquals(worldGroupEntry, entry.getWorldGroupEntry());
    }
  }

  @Test
  void findWorldsByGroupPropagatesException() {
    WorldGroupEntry worldGroupEntry = buildWorldGroupEntry();
    when(queryExecutor.queryList(eq("getWorldByGroup.sql"), any(StatementConfigurer.class),
        any())).thenThrow(new RuntimeException("db error"));

    assertThrows(RuntimeException.class, () -> worldGroupDao.findWorldsByGroup(worldGroupEntry));
  }

  @Test
  void findInventoryByGroupAndPlayerReturnsSingleEntry() {
    PlayerEntry playerEntry = buildPlayerEntry();
    WorldGroupEntry worldGroupEntry = buildWorldGroupEntry();
    WorldGroupInventoryEntry expected = new WorldGroupInventoryEntry();
    when(queryExecutor.querySingle(eq("getWorldInventoryByGroupAndPlayer.sql"),
        any(StatementConfigurer.class), any())).thenReturn(expected);

    WorldGroupInventoryEntry result = worldGroupDao.findInventoryByGroupAndPlayer(playerEntry, worldGroupEntry);

    assertEquals(expected, result);
  }

  @Test
  void findInventoryByGroupAndPlayerSetsGroupIdAndPlayerIdOnPreparedStatement() throws SQLException {
    PlayerEntry playerEntry = buildPlayerEntry();
    WorldGroupEntry worldGroupEntry = buildWorldGroupEntry();
    ArgumentCaptor<StatementConfigurer> captor = ArgumentCaptor.forClass(StatementConfigurer.class);
    when(queryExecutor.querySingle(eq("getWorldInventoryByGroupAndPlayer.sql"), captor.capture(),
        any())).thenReturn(null);

    worldGroupDao.findInventoryByGroupAndPlayer(playerEntry, worldGroupEntry);

    captor.getValue().configure(preparedStatement);
    verify(preparedStatement).setInt(1, worldGroupEntry.getId());
    verify(preparedStatement).setInt(2, playerEntry.getId());
  }

  @Test
  void findInventoryByGroupAndPlayerMapsAllFieldsFromResultSet() throws SQLException {
    PlayerEntry playerEntry = buildPlayerEntry();
    WorldGroupEntry worldGroupEntry = buildWorldGroupEntry();

    try (ResultSet resultSet = mock(ResultSet.class)) {
      when(resultSet.getInt(FIELD_ID)).thenReturn(5);
      when(resultSet.getString(FIELD_CREATED)).thenReturn("2024-03-01");
      when(resultSet.getInt(FIELD_CREATEDBY)).thenReturn(1);
      when(resultSet.getString(FIELD_UPDATED)).thenReturn("2024-03-02");
      when(resultSet.getInt(FIELD_UPDATEDBY)).thenReturn(2);
      when(resultSet.getString(FIELD_DELETED)).thenReturn(null);
      when(resultSet.getInt(FIELD_DELETEDBY)).thenReturn(0);
      when(resultSet.getInt(FIELD_PLAYER_FK)).thenReturn(99);
      when(resultSet.getInt(FIELD_HEALTH)).thenReturn(20);
      when(resultSet.getInt(FIELD_TOTAL_EXPERIENCE)).thenReturn(500);
      when(resultSet.getInt(FIELD_FOOD)).thenReturn(18);
      when(resultSet.getString(FIELD_INVENTORY)).thenReturn("{}");

      when(queryExecutor.querySingle(eq("getWorldInventoryByGroupAndPlayer.sql"),
          any(StatementConfigurer.class), any())).thenAnswer(invocation -> {
        StatementConfigurer configurer = invocation.getArgument(1);
        configurer.configure(preparedStatement);
        RowMapper<WorldGroupInventoryEntry> mapper = invocation.getArgument(2);
        return mapper.map(resultSet);
      });

      WorldGroupInventoryEntry result = worldGroupDao.findInventoryByGroupAndPlayer(playerEntry, worldGroupEntry);

      assertEquals(5, result.getId());
      assertEquals("2024-03-01", result.getCreated());
      assertEquals(1, result.getCreatedBy());
      assertEquals("2024-03-02", result.getUpdated());
      assertEquals(2, result.getUpdatedBy());
      assertNull(result.getDeleted());
      assertEquals(0, result.getDeletedBy());
      assertEquals(99, result.getPlayerId());
      assertEquals(20, result.getHealth());
      assertEquals(500, result.getTotalExperience());
      assertEquals(18, result.getFoodLevel());
      assertNotNull(result.getInventory());
      assertEquals(worldGroupEntry, result.getWorldGroupEntry());
    }
  }

  @Test
  void findInventoryByGroupAndPlayerPropagatesException() {
    PlayerEntry playerEntry = buildPlayerEntry();
    WorldGroupEntry worldGroupEntry = buildWorldGroupEntry();
    when(queryExecutor.querySingle(eq("getWorldInventoryByGroupAndPlayer.sql"),
        any(StatementConfigurer.class), any())).thenThrow(new RuntimeException("db error"));

    assertThrows(RuntimeException.class,
        () -> worldGroupDao.findInventoryByGroupAndPlayer(playerEntry, worldGroupEntry));
  }

  @Test
  void insertInventorySetsAllFieldsOnPreparedStatement() throws SQLException {
    WorldGroupInventoryEntry inventoryEntry = buildWorldGroupInventoryEntry();
    ArgumentCaptor<StatementConfigurer> captor = ArgumentCaptor.forClass(StatementConfigurer.class);
    doNothing().when(queryExecutor).executeUpdate(eq("insertWorldInventoryByGroupAndPlayer.sql"),
        captor.capture());

    worldGroupDao.insertInventory(inventoryEntry);

    captor.getValue().configure(preparedStatement);
    verify(preparedStatement).setInt(1, inventoryEntry.getPlayerId());
    verify(preparedStatement).setInt(2, inventoryEntry.getPlayerId());
    verify(preparedStatement).setInt(3, inventoryEntry.getWorldGroupEntry().getId());
    verify(preparedStatement).setString(4, inventoryEntry.getInventory().toString());
    verify(preparedStatement).setDouble(5, inventoryEntry.getHealth());
    verify(preparedStatement).setInt(6, inventoryEntry.getFoodLevel());
    verify(preparedStatement).setInt(7, inventoryEntry.getTotalExperience());
  }

  @Test
  void insertInventoryPropagatesException() {
    WorldGroupInventoryEntry inventoryEntry = buildWorldGroupInventoryEntry();
    doThrow(new RuntimeException("db error")).when(queryExecutor)
        .executeUpdate(eq("insertWorldInventoryByGroupAndPlayer.sql"), any(StatementConfigurer.class));

    assertThrows(RuntimeException.class, () -> worldGroupDao.insertInventory(inventoryEntry));
  }

  @Test
  void updateInventorySetsAllFieldsOnPreparedStatement() throws SQLException {
    WorldGroupInventoryEntry inventoryEntry = buildWorldGroupInventoryEntry();
    ArgumentCaptor<StatementConfigurer> captor = ArgumentCaptor.forClass(StatementConfigurer.class);
    doNothing().when(queryExecutor).executeUpdate(eq("updateWorldInventoryByGroupAndPlayer.sql"),
        captor.capture());

    worldGroupDao.updateInventory(inventoryEntry);

    captor.getValue().configure(preparedStatement);
    verify(preparedStatement).setInt(1, inventoryEntry.getUpdatedBy());
    verify(preparedStatement).setString(2, inventoryEntry.getInventory().toString());
    verify(preparedStatement).setDouble(3, inventoryEntry.getHealth());
    verify(preparedStatement).setInt(4, inventoryEntry.getFoodLevel());
    verify(preparedStatement).setInt(5, inventoryEntry.getTotalExperience());
    verify(preparedStatement).setInt(6, inventoryEntry.getPlayerId());
    verify(preparedStatement).setInt(7, inventoryEntry.getWorldGroupEntry().getId());
  }

  @Test
  void updateInventoryPropagatesException() {
    WorldGroupInventoryEntry inventoryEntry = buildWorldGroupInventoryEntry();
    doThrow(new RuntimeException("db error")).when(queryExecutor)
        .executeUpdate(eq("updateWorldInventoryByGroupAndPlayer.sql"), any(StatementConfigurer.class));

    assertThrows(RuntimeException.class, () -> worldGroupDao.updateInventory(inventoryEntry));
  }

  @Test
  void insertWorldGroupSetsAllFieldsOnPreparedStatement() throws SQLException {
    WorldGroupEntry worldGroupEntry = buildWorldGroupEntry();
    ArgumentCaptor<StatementConfigurer> captor = ArgumentCaptor.forClass(StatementConfigurer.class);
    doNothing().when(queryExecutor).executeUpdate(eq("insertWorldGroup.sql"), captor.capture());

    worldGroupDao.insertWorldGroup(worldGroupEntry);

    captor.getValue().configure(preparedStatement);
    verify(preparedStatement).setInt(1, worldGroupEntry.getCreatedBy());
    verify(preparedStatement).setString(2, worldGroupEntry.getName());
  }

  @Test
  void insertWorldGroupPropagatesException() {
    WorldGroupEntry worldGroupEntry = buildWorldGroupEntry();
    doThrow(new RuntimeException("db error")).when(queryExecutor)
        .executeUpdate(eq("insertWorldGroup.sql"), any(StatementConfigurer.class));

    assertThrows(RuntimeException.class, () -> worldGroupDao.insertWorldGroup(worldGroupEntry));
  }

  @Test
  void findWorldGroupByNameReturnsSingleEntry() {
    lenient().when(serviceContext.getSettingService()).thenReturn(settingService);
    when(queryExecutor.queryList(eq("getAllWorldGroupSettings.sql"), any(StatementConfigurer.class),
        any())).thenReturn(List.of());
    WorldGroupEntry expected = buildWorldGroupEntry();
    when(queryExecutor.querySingle(eq("getWorldGroupByName.sql"), any(StatementConfigurer.class),
        any())).thenReturn(expected);

    WorldGroupEntry result = worldGroupDao.findWorldGroupByName("GroupAlpha");

    assertEquals(expected, result);
  }

  @Test
  void findWorldGroupByNameSetsNameOnPreparedStatement() throws SQLException {
    lenient().when(serviceContext.getSettingService()).thenReturn(settingService);
    when(queryExecutor.queryList(eq("getAllWorldGroupSettings.sql"), any(StatementConfigurer.class),
        any())).thenReturn(List.of());
    ArgumentCaptor<StatementConfigurer> captor = ArgumentCaptor.forClass(StatementConfigurer.class);
    when(queryExecutor.querySingle(eq("getWorldGroupByName.sql"), captor.capture(), any())).thenReturn(null);

    worldGroupDao.findWorldGroupByName("GroupAlpha");

    captor.getValue().configure(preparedStatement);
    verify(preparedStatement).setString(1, "GroupAlpha");
  }

  @Test
  void findWorldGroupByNameMapsAllFieldsFromResultSet() throws SQLException {
    lenient().when(serviceContext.getSettingService()).thenReturn(settingService);
    when(queryExecutor.queryList(eq("getAllWorldGroupSettings.sql"), any(StatementConfigurer.class),
        any())).thenReturn(List.of());

    try (ResultSet resultSet = mock(ResultSet.class)) {
      when(resultSet.getInt(FIELD_ID)).thenReturn(7);
      when(resultSet.getString(FIELD_CREATED)).thenReturn("2024-01-01");
      when(resultSet.getInt(FIELD_CREATEDBY)).thenReturn(1);
      when(resultSet.getString(FIELD_UPDATED)).thenReturn("2024-01-02");
      when(resultSet.getInt(FIELD_UPDATEDBY)).thenReturn(2);
      when(resultSet.getString(FIELD_DELETED)).thenReturn(null);
      when(resultSet.getInt(FIELD_DELETEDBY)).thenReturn(0);
      when(resultSet.getString(FIELD_NAME)).thenReturn("GroupAlpha");

      when(queryExecutor.querySingle(eq("getWorldGroupByName.sql"), any(StatementConfigurer.class),
          any())).thenAnswer(invocation -> {
        StatementConfigurer configurer = invocation.getArgument(1);
        configurer.configure(preparedStatement);
        RowMapper<WorldGroupEntry> mapper = invocation.getArgument(2);
        return mapper.map(resultSet);
      });

      WorldGroupEntry result = worldGroupDao.findWorldGroupByName("GroupAlpha");

      assertEquals(7, result.getId());
      assertEquals("2024-01-01", result.getCreated());
      assertEquals(1, result.getCreatedBy());
      assertEquals("2024-01-02", result.getUpdated());
      assertEquals(2, result.getUpdatedBy());
      assertNull(result.getDeleted());
      assertEquals(0, result.getDeletedBy());
      assertEquals("GroupAlpha", result.getName());
      assertNotNull(result.getSettings());
    }
  }

  @Test
  void findWorldGroupByNamePropagatesException() {
    lenient().when(serviceContext.getSettingService()).thenReturn(settingService);
    when(queryExecutor.queryList(eq("getAllWorldGroupSettings.sql"), any(StatementConfigurer.class),
        any())).thenReturn(List.of());
    when(queryExecutor.querySingle(eq("getWorldGroupByName.sql"), any(StatementConfigurer.class),
        any())).thenThrow(new RuntimeException("db error"));

    assertThrows(RuntimeException.class, () -> worldGroupDao.findWorldGroupByName("GroupAlpha"));
  }

  @Test
  void insertWorldSetsAllFieldsOnPreparedStatement() throws SQLException {
    WorldEntry worldEntry = buildWorldEntry();
    ArgumentCaptor<StatementConfigurer> captor = ArgumentCaptor.forClass(StatementConfigurer.class);
    doNothing().when(queryExecutor).executeUpdate(eq("insertWorld.sql"), captor.capture());

    worldGroupDao.insertWorld(worldEntry);

    captor.getValue().configure(preparedStatement);
    verify(preparedStatement).setInt(1, worldEntry.getCreatedBy());
    verify(preparedStatement).setString(2, worldEntry.getName());
    verify(preparedStatement).setInt(3, worldEntry.getWorldGroupEntry().getId());
    verify(preparedStatement).setInt(4, worldEntry.getGroupEntry().getId());
  }

  @Test
  void insertWorldPropagatesException() {
    WorldEntry worldEntry = buildWorldEntry();
    doThrow(new RuntimeException("db error")).when(queryExecutor)
        .executeUpdate(eq("insertWorld.sql"), any(StatementConfigurer.class));

    assertThrows(RuntimeException.class, () -> worldGroupDao.insertWorld(worldEntry));
  }

  private WorldGroupEntry buildWorldGroupEntry() {
    WorldGroupEntry entry = new WorldGroupEntry();
    entry.setId(42);
    entry.setCreatedBy(1);
    entry.setName("GroupAlpha");
    return entry;
  }

  private PlayerEntry buildPlayerEntry() {
    PlayerEntry entry = new PlayerEntry();
    entry.setId(99);
    return entry;
  }

  private WorldGroupInventoryEntry buildWorldGroupInventoryEntry() {
    WorldGroupInventoryEntry entry = new WorldGroupInventoryEntry();
    entry.setPlayerId(99);
    entry.setUpdatedBy(2);
    entry.setHealth(20.0);
    entry.setFoodLevel(18);
    entry.setTotalExperience(500);
    entry.setInventory(new JSONObject());
    entry.setWorldGroupEntry(buildWorldGroupEntry());
    return entry;
  }

  private WorldEntry buildWorldEntry() {
    WorldEntry entry = new WorldEntry();
    entry.setCreatedBy(1);
    entry.setName("world_nether");
    entry.setWorldGroupEntry(buildWorldGroupEntry());

    GroupEntry groupEntry = new GroupEntry();
    groupEntry.setId(10);
    entry.setGroupEntry(groupEntry);

    return entry;
  }
}