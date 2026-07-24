package de.relluem94.minecraft.server.spigot.essentials.helpers.db.mapper;

import de.relluem94.minecraft.server.spigot.essentials.constants.PlayerState;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.GroupEntry;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.PlayerEntry;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.PlayerPartnerEntry;
import de.relluem94.minecraft.server.spigot.essentials.permissions.Groups;
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
class PlayerMapperTest {

    @Mock
    private ResultSet resultSet;

    @Test
    void constructorThrowsIllegalStateException() throws Exception {
        Constructor<PlayerMapper> constructor = PlayerMapper.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        InvocationTargetException thrown = assertThrows(InvocationTargetException.class, constructor::newInstance);
        assertInstanceOf(IllegalStateException.class, thrown.getCause());
    }

    @Test
    void mapPlayerReturnsFullyPopulatedPlayerEntry() throws SQLException {
        GroupEntry expectedGroup = new GroupEntry(1, "user", "§8");

        try (MockedStatic<Groups> mockedGroups = mockStatic(Groups.class)) {
            mockedGroups.when(() -> Groups.getGroup(1)).thenReturn(expectedGroup);

            when(resultSet.getInt(FIELD_ID)).thenReturn(1);
            when(resultSet.getString(FIELD_UUID)).thenReturn("uuid-1234");
            when(resultSet.getString(FIELD_CREATED)).thenReturn("2024-01-01");
            when(resultSet.getInt(FIELD_CREATEDBY)).thenReturn(2);
            when(resultSet.getString(FIELD_UPDATED)).thenReturn("2024-06-01");
            when(resultSet.getInt(FIELD_UPDATEDBY)).thenReturn(3);
            when(resultSet.getString(FIELD_DELETED)).thenReturn("2024-12-01");
            when(resultSet.getInt(FIELD_DELETEDBY)).thenReturn(4);
            when(resultSet.getString(FIELD_NAME)).thenReturn("TestPlayer");
            when(resultSet.getString(FIELD_CUSTOM_NAME)).thenReturn("CustomTestPlayer");
            when(resultSet.getDouble(FIELD_PURSE)).thenReturn(999.99);
            when(resultSet.getBoolean(FIELD_FLY)).thenReturn(true);
            when(resultSet.getBoolean(FIELD_AFK)).thenReturn(false);
            when(resultSet.getInt(FIELD_GROUP_FK)).thenReturn(1);

            PlayerEntry result = PlayerMapper.mapPlayer(resultSet);

            assertAll(
                    () -> assertEquals(1, result.getId()),
                    () -> assertEquals("uuid-1234", result.getUuid()),
                    () -> assertEquals("2024-01-01", result.getCreated()),
                    () -> assertEquals(2, result.getCreatedBy()),
                    () -> assertEquals("2024-06-01", result.getUpdated()),
                    () -> assertEquals(3, result.getUpdatedBy()),
                    () -> assertEquals("2024-12-01", result.getDeleted()),
                    () -> assertEquals(4, result.getDeletedBy()),
                    () -> assertEquals("TestPlayer", result.getName()),
                    () -> assertEquals("CustomTestPlayer", result.getCustomName()),
                    () -> assertEquals(999.99, result.getPurse()),
                    () -> assertTrue(result.isFlying()),
                    () -> assertFalse(result.isAfk()),
                    () -> assertEquals(expectedGroup, result.getGroup()),
                    () -> assertEquals(PlayerState.DEFAULT, result.getPlayerState())
            );
        }
    }

    @Test
    void mapPlayerPropagatesSQLException() throws SQLException {
        try (MockedStatic<Groups> _ = mockStatic(Groups.class)) {
            when(resultSet.getInt(FIELD_ID)).thenThrow(new SQLException("db error"));
            assertThrows(SQLException.class, () -> PlayerMapper.mapPlayer(resultSet));
        }
    }

    @Test
    void mapPlayerPartnerReturnsFullyPopulatedPlayerPartnerEntry() throws SQLException {
        when(resultSet.getInt(FIELD_ID)).thenReturn(10);
        when(resultSet.getString(FIELD_CREATED)).thenReturn("2024-02-01");
        when(resultSet.getInt(FIELD_CREATEDBY)).thenReturn(5);
        when(resultSet.getString(FIELD_UPDATED)).thenReturn("2024-07-01");
        when(resultSet.getInt(FIELD_UPDATEDBY)).thenReturn(6);
        when(resultSet.getString(FIELD_DELETED)).thenReturn("2024-11-01");
        when(resultSet.getInt(FIELD_DELETEDBY)).thenReturn(7);
        when(resultSet.getInt(FIELD_FIRST_PARTNER_FK)).thenReturn(100);
        when(resultSet.getInt(FIELD_SECOND_PARTNER_FK)).thenReturn(200);
        when(resultSet.getBoolean(FIELD_SHARE_PROTECTIONS)).thenReturn(true);

        PlayerPartnerEntry result = PlayerMapper.mapPlayerPartner(resultSet);

        assertAll(
                () -> assertEquals(10, result.getId()),
                () -> assertEquals("2024-02-01", result.getCreated()),
                () -> assertEquals(5, result.getCreatedBy()),
                () -> assertEquals("2024-07-01", result.getUpdated()),
                () -> assertEquals(6, result.getUpdatedBy()),
                () -> assertEquals("2024-11-01", result.getDeleted()),
                () -> assertEquals(7, result.getDeletedBy()),
                () -> assertEquals(100, result.getFirstPartnerId()),
                () -> assertEquals(200, result.getSecondPartnerId()),
                () -> assertTrue(result.isShareProtections())
        );
    }

    @Test
    void mapPlayerPartnerPropagatesSQLException() throws SQLException {
        when(resultSet.getInt(FIELD_ID)).thenThrow(new SQLException("db error"));
        assertThrows(SQLException.class, () -> PlayerMapper.mapPlayerPartner(resultSet));
    }

    @Test
    void mapGroupReturnsFullyPopulatedGroupEntry() throws SQLException {
        when(resultSet.getInt(FIELD_ID)).thenReturn(42);
        when(resultSet.getString(FIELD_NAME)).thenReturn("Admin");
        when(resultSet.getString(FIELD_PREFIX)).thenReturn("[A]");

        GroupEntry result = PlayerMapper.mapGroup(resultSet);

        assertAll(
                () -> assertEquals(42, result.getId()),
                () -> assertEquals("Admin", result.getName()),
                () -> assertEquals("[A]", result.getPrefix())
        );
    }

    @Test
    void mapGroupPropagatesSQLException() throws SQLException {
        when(resultSet.getInt(FIELD_ID)).thenThrow(new SQLException("db error"));
        assertThrows(SQLException.class, () -> PlayerMapper.mapGroup(resultSet));
    }
}