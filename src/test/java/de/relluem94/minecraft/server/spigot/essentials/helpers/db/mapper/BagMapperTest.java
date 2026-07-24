package de.relluem94.minecraft.server.spigot.essentials.helpers.db.mapper;

import de.relluem94.minecraft.server.spigot.essentials.constants.Constants;
import de.relluem94.minecraft.server.spigot.essentials.constants.DatabaseMappings;
import de.relluem94.minecraft.server.spigot.essentials.helpers.BagHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.BagEntry;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.BagTypeEntry;
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
class BagMapperTest {

    @Mock
    private ResultSet resultSet;

    @Test
    void constructorThrowsIllegalStateException() throws Exception {
        Constructor<BagMapper> constructor = BagMapper.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        InvocationTargetException thrown = assertThrows(InvocationTargetException.class, constructor::newInstance);
        assertInstanceOf(IllegalStateException.class, thrown.getCause());
        assertEquals(Constants.PLUGIN_INTERNAL_UTILITY_CLASS, thrown.getCause().getMessage());
    }

    @Test
    void mapBagMapsAllFieldsCorrectly() throws SQLException {
        when(resultSet.getInt(FIELD_ID)).thenReturn(1);
        when(resultSet.getString(FIELD_CREATED)).thenReturn("2024-01-01");
        when(resultSet.getInt(FIELD_CREATEDBY)).thenReturn(2);
        when(resultSet.getString(FIELD_UPDATED)).thenReturn("2024-01-02");
        when(resultSet.getInt(FIELD_UPDATEDBY)).thenReturn(3);
        when(resultSet.getString(FIELD_DELETED)).thenReturn("2024-01-03");
        when(resultSet.getInt(FIELD_DELETEDBY)).thenReturn(4);
        when(resultSet.getInt(FIELD_PLAYER_FK)).thenReturn(5);
        when(resultSet.getInt(FIELD_BAG_TYPE_FK)).thenReturn(6);

        for (int i = 0; i <= BagHelper.BAG_SIZE - 1; i++) {
            when(resultSet.getInt(String.format(DatabaseMappings.FIELD_SLOT_VAR_VALUE, (i + 1)))).thenReturn(i + 10);
        }

        BagEntry bagEntry = BagMapper.mapBag(resultSet);

        assertAll(
                () -> assertEquals(1, bagEntry.getId()),
                () -> assertEquals("2024-01-01", bagEntry.getCreated()),
                () -> assertEquals(2, bagEntry.getCreatedBy()),
                () -> assertEquals("2024-01-02", bagEntry.getUpdated()),
                () -> assertEquals(3, bagEntry.getUpdatedBy()),
                () -> assertEquals("2024-01-03", bagEntry.getDeleted()),
                () -> assertEquals(4, bagEntry.getDeletedBy()),
                () -> assertEquals(5, bagEntry.getPlayerId()),
                () -> assertEquals(6, bagEntry.getBagTypeId())
        );

        for (int i = 0; i <= BagHelper.BAG_SIZE - 1; i++) {
            final int slotIndex = i;
            assertAll(
                    () -> assertEquals(slotIndex + 10, bagEntry.getSlotValue(slotIndex))
            );
        }
    }

    @Test
    void mapBagPropagatesSQLException() throws SQLException {
        when(resultSet.getInt(FIELD_ID)).thenThrow(new SQLException("db error"));
        assertThrows(SQLException.class, () -> BagMapper.mapBag(resultSet));
    }

    @Test
    void mapBagTypeMapsAllFieldsCorrectly() throws SQLException {
        when(resultSet.getInt(FIELD_ID)).thenReturn(10);
        when(resultSet.getString(FIELD_DISPLAY_NAME)).thenReturn("Small Bag");
        when(resultSet.getString(FIELD_NAME)).thenReturn("small_bag");
        when(resultSet.getInt(FIELD_COST)).thenReturn(500);

        for (int i = 0; i <= BagHelper.BAG_SIZE - 1; i++) {
            when(resultSet.getString(String.format(DatabaseMappings.FIELD_SLOT_VAR_NAME, (i + 1)))).thenReturn("slot_" + (i + 1));
        }

        BagTypeEntry bagTypeEntry = BagMapper.mapBagType(resultSet);

        assertAll(
                () -> assertEquals(10, bagTypeEntry.getId()),
                () -> assertEquals("Small Bag", bagTypeEntry.getDisplayName()),
                () -> assertEquals("small_bag", bagTypeEntry.getName()),
                () -> assertEquals(500, bagTypeEntry.getCost())
        );

        for (int i = 0; i <= BagHelper.BAG_SIZE - 1; i++) {
            final int slotIndex = i;
            assertAll(
                    () -> assertEquals("slot_" + (slotIndex + 1), bagTypeEntry.getSlotName(slotIndex))
            );
        }
    }

    @Test
    void mapBagTypePropagatesSQLException() throws SQLException {
        when(resultSet.getInt(FIELD_ID)).thenThrow(new SQLException("db error"));
        assertThrows(SQLException.class, () -> BagMapper.mapBagType(resultSet));
    }
}