package de.relluem94.minecraft.server.spigot.essentials.helpers.db.mapper;

import de.relluem94.minecraft.server.spigot.essentials.constants.Constants;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.SettingEntry;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SettingMapperTest {

    @Mock
    private ResultSet resultSet;

    @Test
    void constructorThrowsIllegalStateException() throws Exception {
        Constructor<SettingMapper> constructor = SettingMapper.class.getDeclaredConstructor();
        constructor.setAccessible(true);

        InvocationTargetException thrown = assertThrows(InvocationTargetException.class, constructor::newInstance);

        assertInstanceOf(IllegalStateException.class, thrown.getCause());
        assertEquals(Constants.PLUGIN_INTERNAL_UTILITY_CLASS, thrown.getCause().getMessage());
    }

    @Test
    void mapSettingReturnsFullyMappedEntryWhenUpdatedIsPresent() throws SQLException {
        LocalDateTime createdAt = LocalDateTime.of(2024, 1, 10, 12, 0, 0);
        LocalDateTime updatedAt = LocalDateTime.of(2024, 6, 15, 8, 30, 0);

        Timestamp createdTimestamp = Timestamp.valueOf(createdAt);
        Timestamp updatedTimestamp = Timestamp.valueOf(updatedAt);

        when(resultSet.getInt("ID")).thenReturn(42);
        when(resultSet.getTimestamp("CREATED")).thenReturn(createdTimestamp);
        when(resultSet.getInt("CREATEDBY")).thenReturn(7);
        when(resultSet.getTimestamp("UPDATED")).thenReturn(updatedTimestamp);
        when(resultSet.getObject("UPDATEDBY", Integer.class)).thenReturn(99);
        when(resultSet.getString("name")).thenReturn("spawn-protection");

        SettingEntry result = SettingMapper.mapSetting(resultSet);

        assertAll(
                () -> assertEquals(42, result.getId()),
                () -> assertEquals(createdAt, result.getCreated()),
                () -> assertEquals(7, result.getCreatedBy()),
                () -> assertEquals(updatedAt, result.getUpdated()),
                () -> assertEquals(99, result.getUpdatedBy()),
                () -> assertEquals("spawn-protection", result.getName())
        );
    }

    @Test
    void mapSettingReturnsNullUpdatedWhenUpdatedTimestampIsNull() throws SQLException {
        LocalDateTime createdAt = LocalDateTime.of(2023, 3, 5, 9, 0, 0);
        Timestamp createdTimestamp = Timestamp.valueOf(createdAt);

        when(resultSet.getInt("ID")).thenReturn(1);
        when(resultSet.getTimestamp("CREATED")).thenReturn(createdTimestamp);
        when(resultSet.getInt("CREATEDBY")).thenReturn(3);
        when(resultSet.getTimestamp("UPDATED")).thenReturn(null);
        when(resultSet.getObject("UPDATEDBY", Integer.class)).thenReturn(null);
        when(resultSet.getString("name")).thenReturn("pvp-enabled");

        SettingEntry result = SettingMapper.mapSetting(resultSet);

        assertAll(
                () -> assertEquals(1, result.getId()),
                () -> assertEquals(createdAt, result.getCreated()),
                () -> assertEquals(3, result.getCreatedBy()),
                () -> assertNull(result.getUpdated()),
                () -> assertNull(result.getUpdatedBy()),
                () -> assertEquals("pvp-enabled", result.getName())
        );
    }

    @Test
    void mapSettingPropagatesSQLExceptionWhenResultSetThrows() throws SQLException {
        when(resultSet.getTimestamp("UPDATED")).thenThrow(new SQLException("connection lost"));

        assertThrows(SQLException.class, () -> SettingMapper.mapSetting(resultSet));
    }
}