package de.relluem94.minecraft.server.spigot.essentials.helpers.db.mapper;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.SettingEntry;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.WorldGroupSettingEntry;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;

import static de.relluem94.minecraft.server.spigot.essentials.constants.DatabaseMappings.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WorldGroupSettingMapperTest {

    @Mock
    private ResultSet resultSet;

    @BeforeEach
    void setUp() {
        RelluEssentials.settingEntriesList.clear();
    }

    @AfterEach
    void tearDown() {
        RelluEssentials.settingEntriesList.clear();
    }

    @Test
    void mapWorldGroupSettingReturnsEntryWithNoSettingEntryWhenSettingFkDoesNotMatch() throws SQLException {
        SettingEntry settingEntry = new SettingEntry();
        settingEntry.setId(7);
        RelluEssentials.settingEntriesList.add(settingEntry);

        when(resultSet.getInt(FIELD_ID)).thenReturn(3);
        when(resultSet.getString(FIELD_CREATED)).thenReturn("2024-03-01");
        when(resultSet.getInt(FIELD_CREATEDBY)).thenReturn(12);
        when(resultSet.getString(FIELD_UPDATED)).thenReturn("2024-08-01");
        when(resultSet.getInt(FIELD_UPDATEDBY)).thenReturn(22);
        when(resultSet.getBoolean(FIELD_VALUE)).thenReturn(true);
        when(resultSet.getInt(FIELD_WORLD_GORUP_FK)).thenReturn(7);
        when(resultSet.getInt(FIELD_SETTING_FK)).thenReturn(99);

        WorldGroupSettingEntry result = WorldGroupSettingMapper.mapWorldGroupSetting(resultSet);

        assertAll(
                () -> assertEquals(3, result.getId()),
                () -> assertEquals("2024-03-01", result.getCreated()),
                () -> assertEquals(12, result.getCreatedBy()),
                () -> assertEquals("2024-08-01", result.getUpdated()),
                () -> assertEquals(22, result.getUpdatedBy()),
                () -> assertTrue(result.isValue()),
                () -> assertEquals(7, result.getWorldGroupEntryFk()),
                () -> assertEquals(99, result.getSettingEntryFk()),
                () -> assertNull(result.getSettingEntry())
        );
    }


    @Test
    void mapWorldGroupSettingReturnsFullyMappedEntryWithMatchingSettingEntry() throws SQLException {
        SettingEntry settingEntry = new SettingEntry();
        settingEntry.setId(42);
        RelluEssentials.settingEntriesList.add(settingEntry);

        when(resultSet.getInt(FIELD_ID)).thenReturn(1);
        when(resultSet.getString(FIELD_CREATED)).thenReturn("2024-01-01");
        when(resultSet.getInt(FIELD_CREATEDBY)).thenReturn(10);
        when(resultSet.getString(FIELD_UPDATED)).thenReturn("2024-06-01");
        when(resultSet.getInt(FIELD_UPDATEDBY)).thenReturn(20);
        when(resultSet.getBoolean(FIELD_VALUE)).thenReturn(true);
        when(resultSet.getInt(FIELD_WORLD_GORUP_FK)).thenReturn(5);
        when(resultSet.getInt(FIELD_SETTING_FK)).thenReturn(42);

        WorldGroupSettingEntry result = WorldGroupSettingMapper.mapWorldGroupSetting(resultSet);

        assertAll(
                () -> assertEquals(1, result.getId()),
                () -> assertEquals("2024-01-01", result.getCreated()),
                () -> assertEquals(10, result.getCreatedBy()),
                () -> assertEquals("2024-06-01", result.getUpdated()),
                () -> assertEquals(20, result.getUpdatedBy()),
                () -> assertTrue(result.isValue()),
                () -> assertEquals(5, result.getWorldGroupEntryFk()),
                () -> assertEquals(42, result.getSettingEntryFk()),
                () -> assertNotNull(result.getSettingEntry()),
                () -> assertEquals(settingEntry, result.getSettingEntry())
        );
    }

    @Test
    void mapWorldGroupSettingReturnsEntryWithNoSettingEntryWhenNoMatchFound() throws SQLException {
        when(resultSet.getInt(FIELD_ID)).thenReturn(2);
        when(resultSet.getString(FIELD_CREATED)).thenReturn("2024-02-01");
        when(resultSet.getInt(FIELD_CREATEDBY)).thenReturn(11);
        when(resultSet.getString(FIELD_UPDATED)).thenReturn("2024-07-01");
        when(resultSet.getInt(FIELD_UPDATEDBY)).thenReturn(21);
        when(resultSet.getBoolean(FIELD_VALUE)).thenReturn(false);
        when(resultSet.getInt(FIELD_WORLD_GORUP_FK)).thenReturn(6);
        when(resultSet.getInt(FIELD_SETTING_FK)).thenReturn(99);

        WorldGroupSettingEntry result = WorldGroupSettingMapper.mapWorldGroupSetting(resultSet);

        assertAll(
                () -> assertEquals(2, result.getId()),
                () -> assertEquals("2024-02-01", result.getCreated()),
                () -> assertEquals(11, result.getCreatedBy()),
                () -> assertEquals("2024-07-01", result.getUpdated()),
                () -> assertEquals(21, result.getUpdatedBy()),
                () -> assertFalse(result.isValue()),
                () -> assertEquals(6, result.getWorldGroupEntryFk()),
                () -> assertEquals(99, result.getSettingEntryFk()),
                () -> assertNull(result.getSettingEntry())
        );
    }

    @Test
    void mapWorldGroupSettingPropagatesSQLException() throws SQLException {
        when(resultSet.getInt(FIELD_ID)).thenThrow(new SQLException("db error"));

        assertThrows(SQLException.class, () -> WorldGroupSettingMapper.mapWorldGroupSetting(resultSet));
    }

    @Test
    void constructorThrowsIllegalStateException() throws NoSuchMethodException {
        Constructor<WorldGroupSettingMapper> constructor = WorldGroupSettingMapper.class.getDeclaredConstructor();
        constructor.setAccessible(true);

        InvocationTargetException thrown = assertThrows(
                InvocationTargetException.class,
                constructor::newInstance
        );

        assertInstanceOf(IllegalStateException.class, thrown.getCause());
    }
}