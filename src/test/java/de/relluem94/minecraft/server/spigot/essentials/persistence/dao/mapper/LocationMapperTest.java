package de.relluem94.minecraft.server.spigot.essentials.persistence.dao.mapper;

import static de.relluem94.minecraft.server.spigot.essentials.constants.db.DatabaseMappings.FIELD_ID;
import static de.relluem94.minecraft.server.spigot.essentials.constants.db.DatabaseMappings.FIELD_LOCATION_NAME;
import static de.relluem94.minecraft.server.spigot.essentials.constants.db.DatabaseMappings.FIELD_LOCATION_TYPE;
import static de.relluem94.minecraft.server.spigot.essentials.constants.db.DatabaseMappings.FIELD_LOCATION_TYPE_FK;
import static de.relluem94.minecraft.server.spigot.essentials.constants.db.DatabaseMappings.FIELD_PITCH;
import static de.relluem94.minecraft.server.spigot.essentials.constants.db.DatabaseMappings.FIELD_PLAYER_FK;
import static de.relluem94.minecraft.server.spigot.essentials.constants.db.DatabaseMappings.FIELD_POS_X;
import static de.relluem94.minecraft.server.spigot.essentials.constants.db.DatabaseMappings.FIELD_POS_Y;
import static de.relluem94.minecraft.server.spigot.essentials.constants.db.DatabaseMappings.FIELD_POS_Z;
import static de.relluem94.minecraft.server.spigot.essentials.constants.db.DatabaseMappings.FIELD_WORLD;
import static de.relluem94.minecraft.server.spigot.essentials.constants.db.DatabaseMappings.FIELD_YAW;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import de.relluem94.minecraft.server.spigot.essentials.models.pojo.LocationEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.LocationTypeEntry;
import de.relluem94.minecraft.server.spigot.essentials.registries.LocationTypeRegistry;
import de.relluem94.minecraft.server.spigot.essentials.services.LocationTypeService;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class LocationMapperTest {

    @Mock
    private ResultSet resultSet;

    private LocationTypeService locationTypeService;

    private LocationTypeEntry matchingLocationType;
    private LocationTypeEntry nonMatchingLocationType;

    @BeforeEach
    void setUp() {
        matchingLocationType = new LocationTypeEntry();
        matchingLocationType.setId(3);
        matchingLocationType.setType("HOME");

        nonMatchingLocationType = new LocationTypeEntry();
        nonMatchingLocationType.setId(99);
        nonMatchingLocationType.setType("WARP");
    }

    private LocationTypeService buildServiceWith(List<LocationTypeEntry> types) {
        LocationTypeRegistry registry = new LocationTypeRegistry();
        registry.initialize(types);
        return new LocationTypeService(registry);
    }

    @Test
    void privateConstructorThrowsIllegalStateException() throws NoSuchMethodException {
        Constructor<LocationMapper> constructor = LocationMapper.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        InvocationTargetException thrown = assertThrows(InvocationTargetException.class, constructor::newInstance);
        assertInstanceOf(IllegalStateException.class, thrown.getCause());
    }

    @Test
    void mapLocationMapsAllFieldsCorrectly() throws SQLException {
        locationTypeService = buildServiceWith(List.of(nonMatchingLocationType, matchingLocationType));

        when(resultSet.getInt(FIELD_ID)).thenReturn(1);
        when(resultSet.getInt(FIELD_PLAYER_FK)).thenReturn(42);
        when(resultSet.getString(FIELD_LOCATION_NAME)).thenReturn("MyHome");
        when(resultSet.getString(FIELD_WORLD)).thenReturn("world");
        when(resultSet.getFloat(FIELD_POS_X)).thenReturn(10.5f);
        when(resultSet.getFloat(FIELD_POS_Y)).thenReturn(64.0f);
        when(resultSet.getFloat(FIELD_POS_Z)).thenReturn(-30.25f);
        when(resultSet.getFloat(FIELD_PITCH)).thenReturn(0.5f);
        when(resultSet.getFloat(FIELD_YAW)).thenReturn(180.0f);
        when(resultSet.getInt(FIELD_LOCATION_TYPE_FK)).thenReturn(3);

        LocationEntry result = LocationMapper.mapLocation(resultSet, locationTypeService);

        assertAll(
            () -> assertEquals(1, result.getId()),
            () -> assertEquals(42, result.getPlayerId()),
            () -> assertEquals("MyHome", result.getLocationName()),
            () -> assertEquals("world", result.getWorld()),
            () -> assertEquals(10.5f, result.getX()),
            () -> assertEquals(64.0f, result.getY()),
            () -> assertEquals(-30.25f, result.getZ()),
            () -> assertEquals(0.5f, result.getPitch()),
            () -> assertEquals(180.0f, result.getYaw()),
            () -> assertEquals(matchingLocationType, result.getLocationType())
        );
    }

    @Test
    void mapLocationSetsNoLocationTypeWhenNoMatchFound() throws SQLException {
        locationTypeService = buildServiceWith(List.of(nonMatchingLocationType));

        when(resultSet.getInt(FIELD_ID)).thenReturn(1);
        when(resultSet.getInt(FIELD_PLAYER_FK)).thenReturn(42);
        when(resultSet.getString(FIELD_LOCATION_NAME)).thenReturn("MyHome");
        when(resultSet.getString(FIELD_WORLD)).thenReturn("world");
        when(resultSet.getFloat(FIELD_POS_X)).thenReturn(10.5f);
        when(resultSet.getFloat(FIELD_POS_Y)).thenReturn(64.0f);
        when(resultSet.getFloat(FIELD_POS_Z)).thenReturn(-30.25f);
        when(resultSet.getFloat(FIELD_PITCH)).thenReturn(0.5f);
        when(resultSet.getFloat(FIELD_YAW)).thenReturn(180.0f);
        when(resultSet.getInt(FIELD_LOCATION_TYPE_FK)).thenReturn(1);

        LocationEntry result = LocationMapper.mapLocation(resultSet, locationTypeService);

        assertNull(result.getLocationType());
    }

    @Test
    void mapLocationPropagatesSQLException() throws SQLException {
        locationTypeService = buildServiceWith(List.of());
        when(resultSet.getInt(FIELD_ID)).thenThrow(new SQLException("DB error"));
        assertThrows(SQLException.class, () -> LocationMapper.mapLocation(resultSet, locationTypeService));
    }

    @Test
    void mapLocationTypeMapsAllFieldsCorrectly() throws SQLException {
        when(resultSet.getInt(FIELD_ID)).thenReturn(5);
        when(resultSet.getString(FIELD_LOCATION_TYPE)).thenReturn("WARP");

        LocationTypeEntry result = LocationMapper.mapLocationType(resultSet);

        assertAll(
            () -> assertEquals(5, result.getId()),
            () -> assertEquals("WARP", result.getType())
        );
    }

    @Test
    void mapLocationTypePropagatesSQLException() throws SQLException {
        when(resultSet.getInt(FIELD_ID)).thenThrow(new SQLException("DB error"));
        assertThrows(SQLException.class, () -> LocationMapper.mapLocationType(resultSet));
    }
}