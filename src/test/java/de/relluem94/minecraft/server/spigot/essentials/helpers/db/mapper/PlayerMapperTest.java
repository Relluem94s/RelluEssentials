package de.relluem94.minecraft.server.spigot.essentials.helpers.db.mapper;

import static de.relluem94.minecraft.server.spigot.essentials.constants.db.DatabaseMappings.FIELD_AFK;
import static de.relluem94.minecraft.server.spigot.essentials.constants.db.DatabaseMappings.FIELD_CREATED;
import static de.relluem94.minecraft.server.spigot.essentials.constants.db.DatabaseMappings.FIELD_CREATEDBY;
import static de.relluem94.minecraft.server.spigot.essentials.constants.db.DatabaseMappings.FIELD_CUSTOM_NAME;
import static de.relluem94.minecraft.server.spigot.essentials.constants.db.DatabaseMappings.FIELD_DELETED;
import static de.relluem94.minecraft.server.spigot.essentials.constants.db.DatabaseMappings.FIELD_DELETEDBY;
import static de.relluem94.minecraft.server.spigot.essentials.constants.db.DatabaseMappings.FIELD_FIRST_PARTNER_FK;
import static de.relluem94.minecraft.server.spigot.essentials.constants.db.DatabaseMappings.FIELD_FLY;
import static de.relluem94.minecraft.server.spigot.essentials.constants.db.DatabaseMappings.FIELD_GROUP_FK;
import static de.relluem94.minecraft.server.spigot.essentials.constants.db.DatabaseMappings.FIELD_ID;
import static de.relluem94.minecraft.server.spigot.essentials.constants.db.DatabaseMappings.FIELD_NAME;
import static de.relluem94.minecraft.server.spigot.essentials.constants.db.DatabaseMappings.FIELD_PREFIX;
import static de.relluem94.minecraft.server.spigot.essentials.constants.db.DatabaseMappings.FIELD_PURSE;
import static de.relluem94.minecraft.server.spigot.essentials.constants.db.DatabaseMappings.FIELD_SECOND_PARTNER_FK;
import static de.relluem94.minecraft.server.spigot.essentials.constants.db.DatabaseMappings.FIELD_SHARE_PROTECTIONS;
import static de.relluem94.minecraft.server.spigot.essentials.constants.db.DatabaseMappings.FIELD_UPDATED;
import static de.relluem94.minecraft.server.spigot.essentials.constants.db.DatabaseMappings.FIELD_UPDATEDBY;
import static de.relluem94.minecraft.server.spigot.essentials.constants.db.DatabaseMappings.FIELD_UUID;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import de.relluem94.minecraft.server.spigot.essentials.enums.PlayerState;
import de.relluem94.minecraft.server.spigot.essentials.model.pojo.GroupEntry;
import de.relluem94.minecraft.server.spigot.essentials.model.pojo.PlayerEntry;
import de.relluem94.minecraft.server.spigot.essentials.model.pojo.PlayerPartnerEntry;
import de.relluem94.minecraft.server.spigot.essentials.registry.GroupRegistry;
import de.relluem94.minecraft.server.spigot.essentials.registry.PlayerRegistry;
import de.relluem94.minecraft.server.spigot.essentials.repository.GroupRepository;
import de.relluem94.minecraft.server.spigot.essentials.services.GroupService;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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

        GroupRepository groupRepository = new GroupRepository(List.of(expectedGroup));
        GroupRegistry groupRegistry = new GroupRegistry(groupRepository);

        GroupService groupService = new GroupService(groupRegistry, groupRepository);
        groupService.setPlayerRegistry(new PlayerRegistry(List.of()));
        PlayerEntry result = PlayerMapper.mapPlayer(resultSet, groupService);

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

    @Test
    void mapPlayerPropagatesSQLException() throws SQLException {
        GroupRepository groupRepository = new GroupRepository(List.of());
        GroupRegistry groupRegistry = new GroupRegistry(groupRepository);

        GroupService groupService = new GroupService(groupRegistry, groupRepository);
        groupService.setPlayerRegistry(new PlayerRegistry(List.of()));

        when(resultSet.getInt(FIELD_ID)).thenThrow(new SQLException("db error"));
        assertThrows(SQLException.class, () -> PlayerMapper.mapPlayer(resultSet, groupService));
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