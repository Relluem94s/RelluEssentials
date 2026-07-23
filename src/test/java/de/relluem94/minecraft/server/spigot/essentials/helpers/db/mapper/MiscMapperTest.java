package de.relluem94.minecraft.server.spigot.essentials.helpers.db.mapper;

import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.CropEntry;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.DropEntry;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.PluginInformationEntry;
import org.bukkit.Material;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;

import static de.relluem94.minecraft.server.spigot.essentials.constants.DatabaseMappings.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MiscMapperTest {

    @Mock
    private ResultSet resultSet;

    @Test
    void constructorThrowsIllegalStateException() throws Exception {
        Constructor<MiscMapper> constructor = MiscMapper.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        InvocationTargetException thrown = assertThrows(InvocationTargetException.class, constructor::newInstance);
        assertInstanceOf(IllegalStateException.class, thrown.getCause());
    }

    @Test
    void mapPluginInformationReturnsFullyMappedEntry() throws SQLException {
        when(resultSet.getInt(FIELD_ID)).thenReturn(1);
        when(resultSet.getString(FIELD_CREATED)).thenReturn("2024-01-01");
        when(resultSet.getInt(FIELD_CREATEDBY)).thenReturn(42);
        when(resultSet.getString(FIELD_TAB_HEADER)).thenReturn("Header");
        when(resultSet.getString(FIELD_TAB_FOOTER)).thenReturn("Footer");
        when(resultSet.getString(FIELD_MOTD_MESSAGE)).thenReturn("Welcome");
        when(resultSet.getInt(FIELD_MOTD_PLAYERS)).thenReturn(100);
        when(resultSet.getInt(FIELD_DB_VERSION)).thenReturn(5);

        PluginInformationEntry result = MiscMapper.mapPluginInformation(resultSet);

        assertAll(
                () -> assertEquals(1, result.getId()),
                () -> assertEquals("2024-01-01", result.getCreated()),
                () -> assertEquals(42, result.getCreatedBy()),
                () -> assertEquals("Header", result.getTabHeader()),
                () -> assertEquals("Footer", result.getTabFooter()),
                () -> assertEquals("Welcome", result.getMotdMessage()),
                () -> assertEquals(100, result.getMotdPlayers()),
                () -> assertEquals(5, result.getDbVersion())
        );
    }

    @Test
    void mapPluginInformationPropagatesSQLException() throws SQLException {
        when(resultSet.getInt(FIELD_ID)).thenThrow(new SQLException("db error"));
        assertThrows(SQLException.class, () -> MiscMapper.mapPluginInformation(resultSet));
    }

    @Test
    void mapCropReturnsFullyMappedEntry() throws SQLException {
        when(resultSet.getInt(FIELD_ID)).thenReturn(10);
        when(resultSet.getString(FIELD_PLANT)).thenReturn("WHEAT");
        when(resultSet.getString(FIELD_SEED)).thenReturn("WHEAT_SEEDS");

        try (MockedStatic<Material> materialMock = mockStatic(Material.class)) {
            materialMock.when(() -> Material.getMaterial("WHEAT")).thenReturn(Material.WHEAT);
            materialMock.when(() -> Material.getMaterial("WHEAT_SEEDS")).thenReturn(Material.WHEAT_SEEDS);

            CropEntry result = MiscMapper.mapCrop(resultSet);

            assertAll(
                    () -> assertEquals(10, result.getId()),
                    () -> assertEquals(Material.WHEAT, result.getPlant()),
                    () -> assertEquals(Material.WHEAT_SEEDS, result.getSeed())
            );
        }
    }

    @Test
    void mapCropPropagatesSQLException() throws SQLException {
        when(resultSet.getInt(FIELD_ID)).thenThrow(new SQLException("db error"));
        assertThrows(SQLException.class, () -> MiscMapper.mapCrop(resultSet));
    }

    @Test
    void mapDropReturnsFullyMappedEntry() throws SQLException {
        when(resultSet.getInt(FIELD_ID)).thenReturn(20);
        when(resultSet.getString(FIELD_MATERIAL)).thenReturn("DIAMOND");
        when(resultSet.getInt(FIELD_MIN_INT)).thenReturn(1);
        when(resultSet.getInt(FIELD_MAX_INT)).thenReturn(3);

        try (MockedStatic<Material> materialMock = mockStatic(Material.class)) {
            materialMock.when(() -> Material.getMaterial("DIAMOND")).thenReturn(Material.DIAMOND);

            DropEntry result = MiscMapper.mapDrop(resultSet);

            assertAll(
                    () -> assertEquals(20, result.getId()),
                    () -> assertEquals(Material.DIAMOND, result.getMaterial()),
                    () -> assertEquals(1, result.getMin()),
                    () -> assertEquals(3, result.getMax())
            );
        }
    }

    @Test
    void mapDropPropagatesSQLException() throws SQLException {
        when(resultSet.getInt(FIELD_ID)).thenThrow(new SQLException("db error"));
        assertThrows(SQLException.class, () -> MiscMapper.mapDrop(resultSet));
    }
}