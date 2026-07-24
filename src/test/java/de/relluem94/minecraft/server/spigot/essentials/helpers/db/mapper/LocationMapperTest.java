package de.relluem94.minecraft.server.spigot.essentials.helpers.db.mapper;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.LocationEntry;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.LocationTypeEntry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static de.relluem94.minecraft.server.spigot.essentials.constants.DatabaseMappings.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LocationMapperTest {

    @Mock
    private ResultSet resultSet;

    @Mock
    private RelluEssentials relluEssentials;

    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {
        Field instanceField = RelluEssentials.class.getDeclaredField("instance");
        instanceField.setAccessible(true);
        instanceField.set(null, relluEssentials);

        Field locationTypeEntryListField = RelluEssentials.class.getDeclaredField("locationTypeEntryList");
        locationTypeEntryListField.setAccessible(true);
        locationTypeEntryListField.set(relluEssentials, new ArrayList<>());
    }

    @Test
    void privateConstructorThrowsIllegalStateException() throws NoSuchMethodException {
        Constructor<LocationMapper> constructor = LocationMapper.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        InvocationTargetException thrown = assertThrows(InvocationTargetException.class, constructor::newInstance);
        assertInstanceOf(IllegalStateException.class, thrown.getCause());
    }

    @Test
    void mapLocationMapsAllFieldsCorrectly() throws SQLException, NoSuchFieldException, IllegalAccessException {
        LocationTypeEntry matchingLocationType = new LocationTypeEntry();
        matchingLocationType.setId(3);
        matchingLocationType.setType("HOME");

        LocationTypeEntry nonMatchingLocationType = new LocationTypeEntry();
        nonMatchingLocationType.setId(99);
        nonMatchingLocationType.setType("WARP");

        Field locationTypeEntryListField = RelluEssentials.class.getDeclaredField("locationTypeEntryList");
        locationTypeEntryListField.setAccessible(true);
        locationTypeEntryListField.set(relluEssentials, List.of(nonMatchingLocationType, matchingLocationType));


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

        LocationEntry result = LocationMapper.mapLocation(resultSet);

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
    void mapLocationSetsNoLocationTypeWhenNoMatchFound() throws SQLException, NoSuchFieldException, IllegalAccessException {
        LocationTypeEntry nonMatchingLocationType = new LocationTypeEntry();
        nonMatchingLocationType.setId(99);
        nonMatchingLocationType.setType("WARP");

        Field locationTypeEntryListField = RelluEssentials.class.getDeclaredField("locationTypeEntryList");
        locationTypeEntryListField.setAccessible(true);
        locationTypeEntryListField.set(relluEssentials, List.of(nonMatchingLocationType));

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

        LocationEntry result = LocationMapper.mapLocation(resultSet);

        assertNull(result.getLocationType());
    }

    @Test
    void mapLocationPropagatesSQLException() throws SQLException {
        when(resultSet.getInt(FIELD_ID)).thenThrow(new SQLException("DB error"));
        assertThrows(SQLException.class, () -> LocationMapper.mapLocation(resultSet));
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