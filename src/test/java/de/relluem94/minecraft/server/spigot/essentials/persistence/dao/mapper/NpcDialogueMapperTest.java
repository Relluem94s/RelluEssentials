package de.relluem94.minecraft.server.spigot.essentials.persistence.dao.mapper;

import static de.relluem94.minecraft.server.spigot.essentials.constants.db.DatabaseMappings.FIELD_CREATEDBY;
import static de.relluem94.minecraft.server.spigot.essentials.constants.db.DatabaseMappings.FIELD_CUSTOM_NPC_FK;
import static de.relluem94.minecraft.server.spigot.essentials.constants.db.DatabaseMappings.FIELD_ID;
import static de.relluem94.minecraft.server.spigot.essentials.constants.db.DatabaseMappings.FIELD_LIST_POSITION;
import static de.relluem94.minecraft.server.spigot.essentials.constants.db.DatabaseMappings.FIELD_TEXT;
import static de.relluem94.minecraft.server.spigot.essentials.constants.db.DatabaseMappings.FIELD_UPDATEDBY;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import de.relluem94.minecraft.server.spigot.essentials.models.pojo.NpcDialogueEntry;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class NpcDialogueMapperTest {

  @Mock
  private ResultSet resultSet;

  @Test
  void mapNPCDialogueMapsAllFieldsWhenUpdatedByIsPresent() throws SQLException {
    when(resultSet.getInt(FIELD_ID)).thenReturn(1);
    when(resultSet.getInt(FIELD_LIST_POSITION)).thenReturn(2);
    when(resultSet.getString(FIELD_TEXT)).thenReturn("Hello traveler");
    when(resultSet.getInt(FIELD_CUSTOM_NPC_FK)).thenReturn(10);
    when(resultSet.getInt(FIELD_CREATEDBY)).thenReturn(42);
    when(resultSet.getString(FIELD_UPDATEDBY)).thenReturn("99");
    when(resultSet.getInt(FIELD_UPDATEDBY)).thenReturn(99);

    NpcDialogueEntry entry = NpcDialogueMapper.mapNPCDialogue(resultSet);

    assertAll(
        () -> assertEquals(1, entry.getId()),
        () -> assertEquals(2, entry.getListPosition()),
        () -> assertEquals("Hello traveler", entry.getText()),
        () -> assertEquals(10, entry.getNpcFk()),
        () -> assertEquals(42, entry.getCreatedBy()),
        () -> assertEquals(99, entry.getUpdatedBy())
    );
  }

  @Test
  void mapNPCDialogueMapsAllFieldsWhenUpdatedByIsNull() throws SQLException {
    when(resultSet.getInt(FIELD_ID)).thenReturn(5);
    when(resultSet.getInt(FIELD_LIST_POSITION)).thenReturn(3);
    when(resultSet.getString(FIELD_TEXT)).thenReturn("Farewell");
    when(resultSet.getInt(FIELD_CUSTOM_NPC_FK)).thenReturn(20);
    when(resultSet.getInt(FIELD_CREATEDBY)).thenReturn(7);
    when(resultSet.getString(FIELD_UPDATEDBY)).thenReturn(null);

    NpcDialogueEntry entry = NpcDialogueMapper.mapNPCDialogue(resultSet);

    assertAll(
        () -> assertEquals(5, entry.getId()),
        () -> assertEquals(3, entry.getListPosition()),
        () -> assertEquals("Farewell", entry.getText()),
        () -> assertEquals(20, entry.getNpcFk()),
        () -> assertEquals(7, entry.getCreatedBy()),
        () -> assertEquals(0, entry.getUpdatedBy())
    );
  }

  @Test
  void mapNPCDialoguePropagatesSQLExceptionWhenResultSetThrows() throws SQLException {
    when(resultSet.getInt(FIELD_ID)).thenThrow(new SQLException("DB error"));

    assertThrows(SQLException.class, () -> NpcDialogueMapper.mapNPCDialogue(resultSet));
  }

  @Test
  void privateConstructorThrowsIllegalStateException() throws Exception {
    Constructor<NpcDialogueMapper> constructor = NpcDialogueMapper.class.getDeclaredConstructor();
    constructor.setAccessible(true);

    InvocationTargetException thrownException = assertThrows(
        InvocationTargetException.class,
        constructor::newInstance
    );

    assertInstanceOf(IllegalStateException.class, thrownException.getCause());
  }
}