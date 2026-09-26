package de.relluem94.minecraft.server.spigot.essentials.persistence.dao;

import static de.relluem94.minecraft.server.spigot.essentials.constants.db.DatabaseMappings.FIELD_ENTITY_UUID;
import static de.relluem94.minecraft.server.spigot.essentials.constants.db.DatabaseMappings.FIELD_INVENTORY;
import static de.relluem94.minecraft.server.spigot.essentials.constants.db.DatabaseMappings.FIELD_PROFILE_NAME;
import static de.relluem94.minecraft.server.spigot.essentials.constants.db.DatabaseMappings.FIELD_UPDATEDBY;
import static de.relluem94.minecraft.server.spigot.essentials.constants.db.DatabaseMappings.FIELD_UUID;
import static de.relluem94.minecraft.server.spigot.essentials.constants.db.DatabaseMappings.FIELD_WORLD;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.relluem94.minecraft.server.spigot.essentials.models.pojo.NpcDialogueEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.NpcEntry;
import de.relluem94.minecraft.server.spigot.essentials.persistence.jdbc.QueryExecutor;
import de.relluem94.minecraft.server.spigot.essentials.persistence.jdbc.RowMapper;
import de.relluem94.minecraft.server.spigot.essentials.persistence.jdbc.StatementConfigurer;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class NpcDaoTest {

  @Mock
  private QueryExecutor queryExecutor;

  @Mock
  private PreparedStatement preparedStatement;

  private NpcDao npcDao;

  @BeforeEach
  void setUp() {
    npcDao = new NpcDao(queryExecutor);
  }

  @Test
  void findAllReturnsListFromQueryExecutor() {
    List<NpcEntry> expectedList = List.of(new NpcEntry());
    when(queryExecutor.queryList(eq("getCustomNPCs.sql"), any(StatementConfigurer.class),
        any())).thenReturn(Collections.singletonList(expectedList.getFirst()));

    List<NpcEntry> result = npcDao.findAll();

    assertEquals(expectedList, result);
  }

  @Test
  void findAllUsesNpcMapper() throws SQLException {
    UUID uuid = UUID.randomUUID();

    try (ResultSet resultSet = mock(ResultSet.class)) {
      when(resultSet.getInt(anyString())).thenReturn(1);
      when(resultSet.getString(FIELD_UUID)).thenReturn(uuid.toString());
      when(resultSet.getString(FIELD_PROFILE_NAME)).thenReturn("TestProfile");
      when(resultSet.getString(FIELD_INVENTORY)).thenReturn(null);
      when(resultSet.getString(FIELD_WORLD)).thenReturn("world");
      when(resultSet.getDouble(anyString())).thenReturn(0.0);
      when(resultSet.getFloat(anyString())).thenReturn(0.0f);
      when(resultSet.getString(FIELD_ENTITY_UUID)).thenReturn(null);
      when(resultSet.getString(FIELD_UPDATEDBY)).thenReturn(null);

      when(queryExecutor.queryList(eq("getCustomNPCs.sql"), any(StatementConfigurer.class),
          any())).thenAnswer(invocation -> {
        StatementConfigurer configurer = invocation.getArgument(1);
        configurer.configure(preparedStatement);
        RowMapper<NpcEntry> mapper = invocation.getArgument(2);
        return List.of(mapper.map(resultSet));
      });

      List<NpcEntry> result = npcDao.findAll();

      assertEquals(uuid, result.getFirst().getUuid());
    }
  }



  @Test
  void findAllPropagatesExceptionFromQueryExecutor() {
    when(queryExecutor.queryList(eq("getCustomNPCs.sql"), any(StatementConfigurer.class),
        any())).thenThrow(new RuntimeException("db error"));

    assertThrows(RuntimeException.class, () -> npcDao.findAll());
  }

  @Test
  void findByUuidReturnsSingleNpcEntry() {
    UUID uuid = UUID.randomUUID();
    NpcEntry expected = new NpcEntry();
    when(queryExecutor.querySingle(eq("getCustomNPCByUuid.sql"), any(StatementConfigurer.class),
        any())).thenReturn(expected);

    NpcEntry result = npcDao.findByUuid(uuid);

    assertEquals(expected, result);
  }

  @Test
  void findByUuidSetsUuidStringOnPreparedStatement() throws SQLException {
    UUID uuid = UUID.randomUUID();
    ArgumentCaptor<StatementConfigurer> captor = ArgumentCaptor.forClass(StatementConfigurer.class);
    when(queryExecutor.querySingle(eq("getCustomNPCByUuid.sql"), captor.capture(),
        any())).thenReturn(null);

    npcDao.findByUuid(uuid);

    captor.getValue().configure(preparedStatement);
    verify(preparedStatement).setString(1, uuid.toString());
  }

  @Test
  void findByUuidPropagatesExceptionFromQueryExecutor() {
    UUID uuid = UUID.randomUUID();
    when(queryExecutor.querySingle(eq("getCustomNPCByUuid.sql"), any(StatementConfigurer.class),
        any())).thenThrow(new RuntimeException("db error"));

    assertThrows(RuntimeException.class, () -> npcDao.findByUuid(uuid));
  }

  @Test
  void findDialoguesByNpcIdReturnsDialogueList() {
    List<NpcDialogueEntry> expected = List.of(new NpcDialogueEntry());
    when(queryExecutor.queryList(eq("getCustomNPCDialoguesByNpcId.sql"),
        any(StatementConfigurer.class),
        ArgumentMatchers.<RowMapper<NpcDialogueEntry>>any())).thenReturn(expected);

    List<NpcDialogueEntry> result = npcDao.findDialoguesByNpcId(42);

    assertEquals(expected, result);
  }

  @Test
  void findDialoguesByNpcIdSetsNpcIdOnPreparedStatement() throws SQLException {
    ArgumentCaptor<StatementConfigurer> captor = ArgumentCaptor.forClass(StatementConfigurer.class);
    when(queryExecutor.queryList(eq("getCustomNPCDialoguesByNpcId.sql"), captor.capture(),
        any())).thenReturn(List.of());

    npcDao.findDialoguesByNpcId(42);

    captor.getValue().configure(preparedStatement);
    verify(preparedStatement).setInt(1, 42);
  }

  @Test
  void findDialoguesByNpcIdPropagatesException() {
    when(queryExecutor.queryList(eq("getCustomNPCDialoguesByNpcId.sql"),
        any(StatementConfigurer.class), any())).thenThrow(new RuntimeException("db error"));

    assertThrows(RuntimeException.class, () -> npcDao.findDialoguesByNpcId(1));
  }

  @Test
  void getNPCReturnsSingleNpcEntry() {
    UUID uuid = UUID.randomUUID();
    NpcEntry expected = new NpcEntry();
    when(queryExecutor.querySingle(eq("getCustomNPCByUuid.sql"), any(StatementConfigurer.class),
        any())).thenReturn(expected);

    NpcEntry result = npcDao.findByUuid(uuid);

    assertEquals(expected, result);
  }

  @Test
  void getNPCSetsUuidStringOnPreparedStatement() throws SQLException {
    UUID uuid = UUID.randomUUID();
    ArgumentCaptor<StatementConfigurer> captor = ArgumentCaptor.forClass(StatementConfigurer.class);
    when(queryExecutor.querySingle(eq("getCustomNPCByUuid.sql"), captor.capture(),
        any())).thenReturn(null);

    npcDao.findByUuid(uuid);

    captor.getValue().configure(preparedStatement);
    verify(preparedStatement).setString(1, uuid.toString());
  }

  @Test
  void getNPCPropagatesException() {
    UUID uuid = UUID.randomUUID();
    when(queryExecutor.querySingle(eq("getCustomNPCByUuid.sql"), any(StatementConfigurer.class),
        any())).thenThrow(new RuntimeException("db error"));

    assertThrows(RuntimeException.class, () -> npcDao.findByUuid(uuid));
  }

  @Test
  void insertNpcReturnsGeneratedKey() {
    NpcEntry npcEntry = buildFullNpcEntry();
    when(queryExecutor.executeInsertWithGeneratedKey(eq("insertCustomNPC.sql"),
        any(StatementConfigurer.class))).thenReturn(99);

    int result = npcDao.insertNpc(npcEntry);

    assertEquals(99, result);
  }

  @Test
  void insertNpcSetsAllFieldsOnPreparedStatement() throws SQLException {
    NpcEntry npcEntry = buildFullNpcEntry();
    ArgumentCaptor<StatementConfigurer> captor = ArgumentCaptor.forClass(StatementConfigurer.class);
    when(queryExecutor.executeInsertWithGeneratedKey(eq("insertCustomNPC.sql"),
        captor.capture())).thenReturn(1);

    npcDao.insertNpc(npcEntry);

    captor.getValue().configure(preparedStatement);
    assertAll(() -> verify(preparedStatement).setString(1, npcEntry.getUuid().toString()),
        () -> verify(preparedStatement).setString(2, npcEntry.getProfileName()),
        () -> verify(preparedStatement).setString(3, npcEntry.getInventory().toString()),
        () -> verify(preparedStatement).setString(4, npcEntry.getWorld()),
        () -> verify(preparedStatement).setDouble(5, npcEntry.getX()),
        () -> verify(preparedStatement).setDouble(6, npcEntry.getY()),
        () -> verify(preparedStatement).setDouble(7, npcEntry.getZ()),
        () -> verify(preparedStatement).setFloat(8, npcEntry.getYaw()),
        () -> verify(preparedStatement).setFloat(9, npcEntry.getPitch()),
        () -> verify(preparedStatement).setInt(10, npcEntry.getCreatedBy()));
  }

  @Test
  void insertNpcSetsNullInventoryWhenInventoryIsNull() throws SQLException {
    NpcEntry npcEntry = buildFullNpcEntry();
    npcEntry.setInventory(null);
    ArgumentCaptor<StatementConfigurer> captor = ArgumentCaptor.forClass(StatementConfigurer.class);
    when(queryExecutor.executeInsertWithGeneratedKey(eq("insertCustomNPC.sql"),
        captor.capture())).thenReturn(1);

    npcDao.insertNpc(npcEntry);

    captor.getValue().configure(preparedStatement);
    verify(preparedStatement).setString(3, null);
  }

  @Test
  void insertNpcPropagatesException() {
    NpcEntry npcEntry = buildFullNpcEntry();
    when(queryExecutor.executeInsertWithGeneratedKey(eq("insertCustomNPC.sql"),
        any(StatementConfigurer.class))).thenThrow(new RuntimeException("db error"));

    assertThrows(RuntimeException.class, () -> npcDao.insertNpc(npcEntry));
  }

  @Test
  void updateNpcSetsAllFieldsOnPreparedStatement() throws SQLException {
    NpcEntry npcEntry = buildFullNpcEntry();
    ArgumentCaptor<StatementConfigurer> captor = ArgumentCaptor.forClass(StatementConfigurer.class);
    doNothing().when(queryExecutor).executeUpdate(eq("updateCustomNPC.sql"), captor.capture());

    npcDao.updateNpc(npcEntry);

    captor.getValue().configure(preparedStatement);
    assertAll(() -> verify(preparedStatement).setString(1, npcEntry.getEntityUuid().toString()),
        () -> verify(preparedStatement).setString(2, npcEntry.getProfileName()),
        () -> verify(preparedStatement).setString(3, npcEntry.getInventory().toString()),
        () -> verify(preparedStatement).setString(4, npcEntry.getWorld()),
        () -> verify(preparedStatement).setDouble(5, npcEntry.getX()),
        () -> verify(preparedStatement).setDouble(6, npcEntry.getY()),
        () -> verify(preparedStatement).setDouble(7, npcEntry.getZ()),
        () -> verify(preparedStatement).setFloat(8, npcEntry.getYaw()),
        () -> verify(preparedStatement).setFloat(9, npcEntry.getPitch()),
        () -> verify(preparedStatement).setInt(10, npcEntry.getUpdatedBy()),
        () -> verify(preparedStatement).setInt(11, npcEntry.getId()));
  }

  @Test
  void updateNpcSetsNullEntityUuidWhenEntityUuidIsNull() throws SQLException {
    NpcEntry npcEntry = buildFullNpcEntry();
    npcEntry.setEntityUuid(null);
    ArgumentCaptor<StatementConfigurer> captor = ArgumentCaptor.forClass(StatementConfigurer.class);
    doNothing().when(queryExecutor).executeUpdate(eq("updateCustomNPC.sql"), captor.capture());

    npcDao.updateNpc(npcEntry);

    captor.getValue().configure(preparedStatement);
    verify(preparedStatement).setString(1, null);
  }

  @Test
  void updateNpcSetsNullInventoryWhenInventoryIsNull() throws SQLException {
    NpcEntry npcEntry = buildFullNpcEntry();
    npcEntry.setInventory(null);
    ArgumentCaptor<StatementConfigurer> captor = ArgumentCaptor.forClass(StatementConfigurer.class);
    doNothing().when(queryExecutor).executeUpdate(eq("updateCustomNPC.sql"), captor.capture());

    npcDao.updateNpc(npcEntry);

    captor.getValue().configure(preparedStatement);
    verify(preparedStatement).setString(3, null);
  }

  @Test
  void updateNpcPropagatesException() {
    NpcEntry npcEntry = buildFullNpcEntry();
    doThrow(new RuntimeException("db error")).when(queryExecutor)
        .executeUpdate(eq("updateCustomNPC.sql"), any(StatementConfigurer.class));

    assertThrows(RuntimeException.class, () -> npcDao.updateNpc(npcEntry));
  }

  @Test
  void deleteNpcSetsPlayerIdAndUuidOnPreparedStatement() throws SQLException {
    UUID npcUuid = UUID.randomUUID();
    int deletedByPlayerId = 7;
    ArgumentCaptor<StatementConfigurer> captor = ArgumentCaptor.forClass(StatementConfigurer.class);
    doNothing().when(queryExecutor).executeUpdate(eq("deleteCustomNPC.sql"), captor.capture());

    npcDao.deleteNpc(npcUuid, deletedByPlayerId);

    captor.getValue().configure(preparedStatement);
    assertAll(() -> verify(preparedStatement).setInt(1, deletedByPlayerId),
        () -> verify(preparedStatement).setString(2, npcUuid.toString()));
  }

  @Test
  void deleteNpcPropagatesException() {
    UUID npcUuid = UUID.randomUUID();
    doThrow(new RuntimeException("db error")).when(queryExecutor)
        .executeUpdate(eq("deleteCustomNPC.sql"), any(StatementConfigurer.class));

    assertThrows(RuntimeException.class, () -> npcDao.deleteNpc(npcUuid, 1));
  }

  @Test
  void insertNpcDialogueSetsAllFieldsOnPreparedStatement() throws SQLException {
    NpcDialogueEntry entry = buildFullNpcDialogueEntry();
    ArgumentCaptor<StatementConfigurer> captor = ArgumentCaptor.forClass(StatementConfigurer.class);
    doNothing().when(queryExecutor)
        .executeUpdate(eq("insertCustomNPCDialogue.sql"), captor.capture());

    npcDao.insertNpcDialogue(entry);

    captor.getValue().configure(preparedStatement);
    assertAll(() -> verify(preparedStatement).setInt(1, entry.getCreatedBy()),
        () -> verify(preparedStatement).setInt(2, entry.getListPosition()),
        () -> verify(preparedStatement).setString(3, entry.getText()),
        () -> verify(preparedStatement).setInt(4, entry.getNpcFk()));
  }

  @Test
  void insertNpcDialoguePropagatesException() {
    NpcDialogueEntry entry = buildFullNpcDialogueEntry();
    doThrow(new RuntimeException("db error")).when(queryExecutor)
        .executeUpdate(eq("insertCustomNPCDialogue.sql"), any(StatementConfigurer.class));

    assertThrows(RuntimeException.class, () -> npcDao.insertNpcDialogue(entry));
  }

  @Test
  void updateNpcDialogueReturnsTrueWhenRowsAffected() {
    NpcDialogueEntry entry = buildFullNpcDialogueEntry();
    UUID uuid = UUID.randomUUID();
    when(queryExecutor.executeUpdateWithCount(eq("updateCustomNPCDialogue.sql"),
        any(StatementConfigurer.class))).thenReturn(1);

    boolean result = npcDao.updateNpcDialogue(entry, uuid);

    assertTrue(result);
  }

  @Test
  void updateNpcDialogueReturnsFalseWhenNoRowsAffected() {
    NpcDialogueEntry entry = buildFullNpcDialogueEntry();
    UUID uuid = UUID.randomUUID();
    when(queryExecutor.executeUpdateWithCount(eq("updateCustomNPCDialogue.sql"),
        any(StatementConfigurer.class))).thenReturn(0);

    boolean result = npcDao.updateNpcDialogue(entry, uuid);

    assertFalse(result);
  }

  @Test
  void updateNpcDialogueSetsAllFieldsOnPreparedStatement() throws SQLException {
    NpcDialogueEntry entry = buildFullNpcDialogueEntry();
    UUID uuid = UUID.randomUUID();
    ArgumentCaptor<StatementConfigurer> captor = ArgumentCaptor.forClass(StatementConfigurer.class);
    when(queryExecutor.executeUpdateWithCount(eq("updateCustomNPCDialogue.sql"),
        captor.capture())).thenReturn(1);

    npcDao.updateNpcDialogue(entry, uuid);

    captor.getValue().configure(preparedStatement);
    assertAll(() -> verify(preparedStatement).setInt(1, entry.getUpdatedBy()),
        () -> verify(preparedStatement).setString(2, entry.getText()),
        () -> verify(preparedStatement).setString(3, uuid.toString()),
        () -> verify(preparedStatement).setInt(4, entry.getListPosition()));
  }

  @Test
  void updateNpcDialoguePropagatesException() {
    NpcDialogueEntry entry = buildFullNpcDialogueEntry();
    UUID uuid = UUID.randomUUID();
    when(queryExecutor.executeUpdateWithCount(eq("updateCustomNPCDialogue.sql"),
        any(StatementConfigurer.class))).thenThrow(new RuntimeException("db error"));

    assertThrows(RuntimeException.class, () -> npcDao.updateNpcDialogue(entry, uuid));
  }

  @Test
  void deleteNpcDialogueByIdSetsAllFieldsOnPreparedStatement() throws SQLException {
    UUID npcUuid = UUID.randomUUID();
    int listPosition = 3;
    int deletedByPlayerId = 5;
    ArgumentCaptor<StatementConfigurer> captor = ArgumentCaptor.forClass(StatementConfigurer.class);
    doNothing().when(queryExecutor)
        .executeUpdate(eq("deleteCustomNPCDialogueById.sql"), captor.capture());

    npcDao.deleteNpcDialogueById(npcUuid, listPosition, deletedByPlayerId);

    captor.getValue().configure(preparedStatement);
    assertAll(() -> verify(preparedStatement).setInt(1, deletedByPlayerId),
        () -> verify(preparedStatement).setString(2, npcUuid.toString()),
        () -> verify(preparedStatement).setInt(3, listPosition));
  }

  @Test
  void deleteNpcDialogueByIdPropagatesException() {
    UUID npcUuid = UUID.randomUUID();
    doThrow(new RuntimeException("db error")).when(queryExecutor)
        .executeUpdate(eq("deleteCustomNPCDialogueById.sql"), any(StatementConfigurer.class));

    assertThrows(RuntimeException.class, () -> npcDao.deleteNpcDialogueById(npcUuid, 1, 1));
  }

  @Test
  void deleteNpcDialogueByNpcIdSetsAllFieldsOnPreparedStatement() throws SQLException {
    UUID npcUuid = UUID.randomUUID();
    int deletedByPlayerId = 8;
    ArgumentCaptor<StatementConfigurer> captor = ArgumentCaptor.forClass(StatementConfigurer.class);
    doNothing().when(queryExecutor)
        .executeUpdate(eq("deleteCustomNPCDialogueByNpcId.sql"), captor.capture());

    npcDao.deleteNpcDialogueByNpcId(npcUuid, deletedByPlayerId);

    captor.getValue().configure(preparedStatement);
    assertAll(() -> verify(preparedStatement).setInt(1, deletedByPlayerId),
        () -> verify(preparedStatement).setString(2, npcUuid.toString()));
  }

  @Test
  void deleteNpcDialogueByNpcIdPropagatesException() {
    UUID npcUuid = UUID.randomUUID();
    doThrow(new RuntimeException("db error")).when(queryExecutor)
        .executeUpdate(eq("deleteCustomNPCDialogueByNpcId.sql"), any(StatementConfigurer.class));

    assertThrows(RuntimeException.class, () -> npcDao.deleteNpcDialogueByNpcId(npcUuid, 1));
  }

  @Test
  void getNPCDialoguesReturnsDialogueList() {
    List<NpcDialogueEntry> expected = List.of(new NpcDialogueEntry());
    when(queryExecutor.queryList(eq("getCustomNPCDialoguesByNpcId.sql"),
        any(StatementConfigurer.class),
        ArgumentMatchers.<RowMapper<NpcDialogueEntry>>any())).thenReturn(expected);

    List<NpcDialogueEntry> result = npcDao.findDialoguesByNpcId(10);

    assertEquals(expected, result);
  }

  @Test
  void getNPCDialoguesSetsNpcIdOnPreparedStatement() throws SQLException {
    ArgumentCaptor<StatementConfigurer> captor = ArgumentCaptor.forClass(StatementConfigurer.class);
    when(queryExecutor.queryList(eq("getCustomNPCDialoguesByNpcId.sql"), captor.capture(),
        any())).thenReturn(List.of());

    npcDao.findDialoguesByNpcId(10);

    captor.getValue().configure(preparedStatement);
    verify(preparedStatement).setInt(1, 10);
  }

  @Test
  void getNPCDialoguesPropagatesException() {
    when(queryExecutor.queryList(eq("getCustomNPCDialoguesByNpcId.sql"),
        any(StatementConfigurer.class), any())).thenThrow(new RuntimeException("db error"));

    assertThrows(RuntimeException.class, () -> npcDao.findDialoguesByNpcId(1));
  }

  private NpcEntry buildFullNpcEntry() {
    NpcEntry entry = new NpcEntry();
    entry.setId(1);
    entry.setUuid(UUID.randomUUID());
    entry.setEntityUuid(UUID.randomUUID());
    entry.setProfileName("TestProfile");
    entry.setInventory(new JSONObject());
    entry.setWorld("world");
    entry.setX(1.0);
    entry.setY(64.0);
    entry.setZ(-1.0);
    entry.setYaw(90.0f);
    entry.setPitch(0.0f);
    entry.setCreatedBy(1);
    entry.setUpdatedBy(2);
    return entry;
  }

  private NpcDialogueEntry buildFullNpcDialogueEntry() {
    NpcDialogueEntry entry = new NpcDialogueEntry();
    entry.setCreatedBy(1);
    entry.setUpdatedBy(2);
    entry.setListPosition(1);
    entry.setText("Hello, traveler!");
    entry.setNpcFk(42);
    return entry;
  }
}