package de.relluem94.minecraft.server.spigot.essentials.persistence.dao.mapper;

import static de.relluem94.minecraft.server.spigot.essentials.constants.db.DatabaseMappings.FIELD_CREATEDBY;
import static de.relluem94.minecraft.server.spigot.essentials.constants.db.DatabaseMappings.FIELD_ENTITY_UUID;
import static de.relluem94.minecraft.server.spigot.essentials.constants.db.DatabaseMappings.FIELD_ID;
import static de.relluem94.minecraft.server.spigot.essentials.constants.db.DatabaseMappings.FIELD_INVENTORY;
import static de.relluem94.minecraft.server.spigot.essentials.constants.db.DatabaseMappings.FIELD_PITCH;
import static de.relluem94.minecraft.server.spigot.essentials.constants.db.DatabaseMappings.FIELD_POS_X;
import static de.relluem94.minecraft.server.spigot.essentials.constants.db.DatabaseMappings.FIELD_POS_Y;
import static de.relluem94.minecraft.server.spigot.essentials.constants.db.DatabaseMappings.FIELD_POS_Z;
import static de.relluem94.minecraft.server.spigot.essentials.constants.db.DatabaseMappings.FIELD_PROFILE_NAME;
import static de.relluem94.minecraft.server.spigot.essentials.constants.db.DatabaseMappings.FIELD_UPDATED;
import static de.relluem94.minecraft.server.spigot.essentials.constants.db.DatabaseMappings.FIELD_UPDATEDBY;
import static de.relluem94.minecraft.server.spigot.essentials.constants.db.DatabaseMappings.FIELD_UUID;
import static de.relluem94.minecraft.server.spigot.essentials.constants.db.DatabaseMappings.FIELD_WORLD;
import static de.relluem94.minecraft.server.spigot.essentials.constants.db.DatabaseMappings.FIELD_YAW;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import de.relluem94.minecraft.server.spigot.essentials.models.Npc;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.NpcDialogueEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.NpcEntry;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class NpcMapperTest {

  @Mock
  private ResultSet resultSet;

  @Test
  void privateConstructorThrowsIllegalAccessException() throws Exception {
    Constructor<NpcMapper> constructor = NpcMapper.class.getDeclaredConstructor();
    constructor.setAccessible(true);
    InvocationTargetException thrown = assertThrows(InvocationTargetException.class, constructor::newInstance);
    assertInstanceOf(IllegalStateException.class, thrown.getCause());
  }

  @Test
  void mapNPCMapsAllFieldsIncludingEntityUuidAndUpdatedBy() throws SQLException {
    UUID npcUuid = UUID.randomUUID();
    UUID entityUuid = UUID.randomUUID();
    String inventoryJson = "{\"key\":\"value\"}";

    when(resultSet.getInt(FIELD_ID)).thenReturn(1);
    when(resultSet.getString(FIELD_UUID)).thenReturn(npcUuid.toString());
    when(resultSet.getString(FIELD_PROFILE_NAME)).thenReturn("TestProfile");
    when(resultSet.getString(FIELD_INVENTORY)).thenReturn(inventoryJson);
    when(resultSet.getString(FIELD_WORLD)).thenReturn("world");
    when(resultSet.getDouble(FIELD_POS_X)).thenReturn(1.0);
    when(resultSet.getDouble(FIELD_POS_Y)).thenReturn(2.0);
    when(resultSet.getDouble(FIELD_POS_Z)).thenReturn(3.0);
    when(resultSet.getFloat(FIELD_YAW)).thenReturn(90.0f);
    when(resultSet.getFloat(FIELD_PITCH)).thenReturn(45.0f);
    when(resultSet.getInt(FIELD_CREATEDBY)).thenReturn(10);
    when(resultSet.getString(FIELD_ENTITY_UUID)).thenReturn(entityUuid.toString());
    when(resultSet.getString(FIELD_UPDATEDBY)).thenReturn("20");
    when(resultSet.getInt(FIELD_UPDATED)).thenReturn(20);

    NpcEntry entry = NpcMapper.mapNPC(resultSet);

    assertAll(
        () -> assertEquals(1, entry.getId()),
        () -> assertEquals(npcUuid, entry.getUuid()),
        () -> assertEquals("TestProfile", entry.getProfileName()),
        () -> assertNotNull(entry.getInventory()),
        () -> assertEquals("value", entry.getInventory().getString("key")),
        () -> assertEquals("world", entry.getWorld()),
        () -> assertEquals(1.0, entry.getX()),
        () -> assertEquals(2.0, entry.getY()),
        () -> assertEquals(3.0, entry.getZ()),
        () -> assertEquals(90.0f, entry.getYaw()),
        () -> assertEquals(45.0f, entry.getPitch()),
        () -> assertEquals(10, entry.getCreatedBy()),
        () -> assertEquals(entityUuid, entry.getEntityUuid()),
        () -> assertEquals(20, entry.getUpdatedBy())
    );
  }

  @Test
  void mapNPCMapsNullInventoryAsNull() throws SQLException {
    UUID npcUuid = UUID.randomUUID();

    when(resultSet.getInt(FIELD_ID)).thenReturn(1);
    when(resultSet.getString(FIELD_UUID)).thenReturn(npcUuid.toString());
    when(resultSet.getString(FIELD_PROFILE_NAME)).thenReturn("TestProfile");
    when(resultSet.getString(FIELD_INVENTORY)).thenReturn(null);
    when(resultSet.getString(FIELD_WORLD)).thenReturn("world");
    when(resultSet.getDouble(FIELD_POS_X)).thenReturn(1.0);
    when(resultSet.getDouble(FIELD_POS_Y)).thenReturn(2.0);
    when(resultSet.getDouble(FIELD_POS_Z)).thenReturn(3.0);
    when(resultSet.getFloat(FIELD_YAW)).thenReturn(90.0f);
    when(resultSet.getFloat(FIELD_PITCH)).thenReturn(45.0f);
    when(resultSet.getInt(FIELD_CREATEDBY)).thenReturn(10);
    when(resultSet.getString(FIELD_ENTITY_UUID)).thenReturn(null);
    when(resultSet.getString(FIELD_UPDATEDBY)).thenReturn(null);

    NpcEntry entry = NpcMapper.mapNPC(resultSet);

    assertAll(
        () -> assertNull(entry.getInventory()),
        () -> assertNull(entry.getEntityUuid()),
        () -> assertNull(entry.getUpdatedBy())
    );
  }

  @Test
  void mapNPCPropagatesSQLException() throws SQLException {
    when(resultSet.getInt(FIELD_ID)).thenThrow(new SQLException("DB error"));

    assertThrows(SQLException.class, () -> NpcMapper.mapNPC(resultSet));
  }

  @Test
  void toEntryMapsAllFieldsFromNpcAndActorPlayerId() {
    UUID npcUuid = UUID.randomUUID();
    UUID entityUuid = UUID.randomUUID();
    JSONObject inventory = new JSONObject("{\"slot\":\"item\"}");

    Npc npc = new Npc(1, npcUuid, "ProfileName", inventory, 10.0, 20.0, 30.0, 180.0f, 90.0f, "world_nether");
    npc.setEntityUUID(entityUuid);

    NpcEntry entry = NpcMapper.toEntry(npc, 42);

    assertAll(
        () -> assertEquals(npcUuid, entry.getUuid()),
        () -> assertEquals("ProfileName", entry.getProfileName()),
        () -> assertEquals(inventory, entry.getInventory()),
        () -> assertEquals("world_nether", entry.getWorld()),
        () -> assertEquals(10.0, entry.getX()),
        () -> assertEquals(20.0, entry.getY()),
        () -> assertEquals(30.0, entry.getZ()),
        () -> assertEquals(180.0f, entry.getYaw()),
        () -> assertEquals(90.0f, entry.getPitch()),
        () -> assertEquals(42, entry.getCreatedBy()),
        () -> assertEquals(42, entry.getUpdatedBy()),
        () -> assertEquals(entityUuid, entry.getEntityUuid())
    );
  }

  @Test
  void toEntryOmitsEntityUuidWhenNpcEntityUuidIsNull() {
    UUID npcUuid = UUID.randomUUID();
    Npc npc = new Npc(1, npcUuid, "ProfileName", 10.0, 20.0, 30.0, 180.0f, 90.0f, "world");
    npc.setEntityUUID(null);

    NpcEntry entry = NpcMapper.toEntry(npc, 5);

    assertNull(entry.getEntityUuid());
  }

  @Test
  void toDomainFromEntryMapsAllFields() {
    UUID npcUuid = UUID.randomUUID();
    UUID entityUuid = UUID.randomUUID();
    JSONObject inventory = new JSONObject("{\"a\":\"b\"}");

    NpcEntry entry = new NpcEntry();
    entry.setId(7);
    entry.setUuid(npcUuid);
    entry.setProfileName("DomainProfile");
    entry.setInventory(inventory);
    entry.setWorld("world_end");
    entry.setX(5.5);
    entry.setY(6.6);
    entry.setZ(7.7);
    entry.setYaw(45.0f);
    entry.setPitch(15.0f);
    entry.setEntityUuid(entityUuid);

    Npc npc = NpcMapper.toDomain(entry);

    assertAll(
        () -> assertEquals(7, npc.getDbid()),
        () -> assertEquals(npcUuid, npc.getId()),
        () -> assertEquals("DomainProfile", npc.getProfileName()),
        () -> assertEquals(inventory, npc.getInventory()),
        () -> assertEquals("world_end", npc.getWorldName()),
        () -> assertEquals(5.5, npc.getX()),
        () -> assertEquals(6.6, npc.getY()),
        () -> assertEquals(7.7, npc.getZ()),
        () -> assertEquals(45.0f, npc.getYaw()),
        () -> assertEquals(15.0f, npc.getPitch()),
        () -> assertEquals(entityUuid, npc.getEntityUUID())
    );
  }

  @Test
  void toDomainFromEntryAndDialogueLinesIncludesDialogueLines() {
    UUID npcUuid = UUID.randomUUID();

    NpcEntry entry = new NpcEntry();
    entry.setId(3);
    entry.setUuid(npcUuid);
    entry.setProfileName("DialogueProfile");
    entry.setWorld("world");
    entry.setX(0.0);
    entry.setY(0.0);
    entry.setZ(0.0);
    entry.setYaw(0.0f);
    entry.setPitch(0.0f);

    NpcDialogueEntry dialogueLine = new NpcDialogueEntry();
    List<NpcDialogueEntry> dialogueLines = List.of(dialogueLine);

    Npc npc = NpcMapper.toDomain(entry, dialogueLines);

    assertAll(
        () -> assertEquals(3, npc.getDbid()),
        () -> assertEquals(npcUuid, npc.getId()),
        () -> assertEquals("DialogueProfile", npc.getProfileName()),
        () -> assertEquals("world", npc.getWorldName()),
        () -> assertEquals(0.0, npc.getX()),
        () -> assertEquals(0.0, npc.getY()),
        () -> assertEquals(0.0, npc.getZ()),
        () -> assertEquals(0.0f, npc.getYaw()),
        () -> assertEquals(0.0f, npc.getPitch()),
        () -> assertEquals(dialogueLines, npc.getDialogueLines()),
        () -> assertEquals(1, npc.getDialogueLines().size())
    );
  }
}