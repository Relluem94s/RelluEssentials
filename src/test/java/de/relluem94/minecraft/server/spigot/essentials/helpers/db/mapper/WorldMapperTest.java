package de.relluem94.minecraft.server.spigot.essentials.helpers.db.mapper;

import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.WorldEntry;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.WorldGroupEntry;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.WorldGroupInventoryEntry;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.WorldGroupSettingEntry;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static de.relluem94.minecraft.server.spigot.essentials.constants.DatabaseMappings.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WorldMapperTest {

    @Mock
    private ResultSet resultSet;

    private void stubAuditFields() throws SQLException {
        when(resultSet.getInt(FIELD_ID)).thenReturn(1);
        when(resultSet.getString(FIELD_CREATED)).thenReturn("2024-01-01 00:00:00");
        when(resultSet.getInt(FIELD_CREATEDBY)).thenReturn(10);
        when(resultSet.getString(FIELD_UPDATED)).thenReturn("2024-06-01 00:00:00");
        when(resultSet.getInt(FIELD_UPDATEDBY)).thenReturn(20);
        when(resultSet.getString(FIELD_DELETED)).thenReturn(null);
        when(resultSet.getInt(FIELD_DELETEDBY)).thenReturn(0);
    }

    @Test
    void mapWorld_shouldMapAllFieldsCorrectly() throws SQLException {
        stubAuditFields();
        when(resultSet.getString(FIELD_NAME)).thenReturn("world");

        WorldEntry result = WorldMapper.mapWorld(resultSet);

        assertEquals(1, result.getId());
        assertEquals("2024-01-01 00:00:00", result.getCreated());
        assertEquals(10, result.getCreatedBy());
        assertEquals("2024-06-01 00:00:00", result.getUpdated());
        assertEquals(20, result.getUpdatedBy());
        assertNull(result.getDeleted());
        assertEquals(0, result.getDeletedBy());
        assertEquals("world", result.getName());
    }

    @Test
    void mapWorld_shouldThrowSQLException_whenResultSetFails() throws SQLException {
        when(resultSet.getInt(FIELD_ID)).thenThrow(new SQLException("db error"));

        SQLException exception = assertThrows(SQLException.class, () -> WorldMapper.mapWorld(resultSet));
        assertEquals("db error", exception.getMessage());
    }

    @Test
    void mapWorldGroup_shouldMapAllFieldsAndFilterMatchingSettings() throws SQLException {
        stubAuditFields();
        when(resultSet.getString(FIELD_NAME)).thenReturn("overworld-group");

        WorldGroupSettingEntry matchingSetting = new WorldGroupSettingEntry();
        matchingSetting.setWorldGroupEntryFk(1);

        WorldGroupSettingEntry nonMatchingSetting = new WorldGroupSettingEntry();
        nonMatchingSetting.setWorldGroupEntryFk(99);

        WorldGroupEntry result = WorldMapper.mapWorldGroup(resultSet, List.of(matchingSetting, nonMatchingSetting));

        assertEquals(1, result.getId());
        assertEquals("2024-01-01 00:00:00", result.getCreated());
        assertEquals(10, result.getCreatedBy());
        assertEquals("2024-06-01 00:00:00", result.getUpdated());
        assertEquals(20, result.getUpdatedBy());
        assertNull(result.getDeleted());
        assertEquals(0, result.getDeletedBy());
        assertEquals("overworld-group", result.getName());
        assertEquals(List.of(matchingSetting), result.getSettings());
    }

    @Test
    void mapWorldGroup_shouldReturnEmptySettings_whenNoSettingsMatch() throws SQLException {
        stubAuditFields();
        when(resultSet.getString(FIELD_NAME)).thenReturn("nether-group");

        WorldGroupSettingEntry nonMatchingSetting = new WorldGroupSettingEntry();
        nonMatchingSetting.setWorldGroupEntryFk(99);

        WorldGroupEntry result = WorldMapper.mapWorldGroup(resultSet, List.of(nonMatchingSetting));

        assertTrue(result.getSettings().isEmpty());
    }

    @Test
    void mapWorldGroupInventory_shouldMapAllFieldsCorrectly() throws SQLException {
        stubAuditFields();
        String inventoryJson = "{\"slot_0\":\"diamond_sword\"}";

        when(resultSet.getInt(FIELD_PLAYER_FK)).thenReturn(42);
        when(resultSet.getInt(FIELD_HEALTH)).thenReturn(20);
        when(resultSet.getInt(FIELD_TOTAL_EXPERIENCE)).thenReturn(500);
        when(resultSet.getInt(FIELD_FOOD)).thenReturn(18);
        when(resultSet.getString(FIELD_INVENTORY)).thenReturn(inventoryJson);

        WorldGroupInventoryEntry result = WorldMapper.mapWorldGroupInventory(resultSet);

        assertEquals(1, result.getId());
        assertEquals("2024-01-01 00:00:00", result.getCreated());
        assertEquals(10, result.getCreatedBy());
        assertEquals("2024-06-01 00:00:00", result.getUpdated());
        assertEquals(20, result.getUpdatedBy());
        assertNull(result.getDeleted());
        assertEquals(0, result.getDeletedBy());
        assertEquals(42, result.getPlayerId());
        assertEquals(20, result.getHealth());
        assertEquals(500, result.getTotalExperience());
        assertEquals(18, result.getFoodLevel());
        assertEquals("diamond_sword", result.getInventory().getString("slot_0"));
    }

    @Test
    void mapWorldGroupInventory_shouldThrowException_whenInventoryJsonIsInvalid() throws SQLException {
        stubAuditFields();
        when(resultSet.getInt(FIELD_PLAYER_FK)).thenReturn(42);
        when(resultSet.getInt(FIELD_HEALTH)).thenReturn(20);
        when(resultSet.getInt(FIELD_TOTAL_EXPERIENCE)).thenReturn(500);
        when(resultSet.getInt(FIELD_FOOD)).thenReturn(18);
        when(resultSet.getString(FIELD_INVENTORY)).thenReturn("not-valid-json");

        assertThrows(Exception.class, () -> WorldMapper.mapWorldGroupInventory(resultSet));
    }

    @Test
    void constructor_shouldThrowIllegalStateException() throws Exception {
        var constructor = WorldMapper.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        Exception wrapper = assertThrows(Exception.class, constructor::newInstance);
        assertInstanceOf(IllegalStateException.class, wrapper.getCause());
    }
}