package de.relluem94.minecraft.server.spigot.essentials.helpers.db.mapper;

import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.NPCEntry;
import de.relluem94.minecraft.server.spigot.essentials.npc.NPC;
import org.bukkit.entity.Villager;
import org.jspecify.annotations.NonNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Proxy;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Function;

import static de.relluem94.minecraft.server.spigot.essentials.constants.DatabaseMappings.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NPCMapperTest {

    @Mock
    private ResultSet resultSet;

    private static Villager.@NonNull Profession createFakeProfession() {
        return (Villager.Profession) Proxy.newProxyInstance(
                NPCMapperTest.class.getClassLoader(),
                new Class[]{Villager.Profession.class},
                (_, _, _) -> null
        );
    }

    private static final Villager.Profession FAKE_PROFESSION = createFakeProfession();
    private static final Function<String, Villager.Profession> PROFESSION_RESOLVER = _ -> FAKE_PROFESSION;


    @Test
    void constructorThrowsIllegalStateException() throws Exception {
        Constructor<NPCMapper> constructor = NPCMapper.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        InvocationTargetException thrown = assertThrows(InvocationTargetException.class, constructor::newInstance);
        assertInstanceOf(IllegalStateException.class, thrown.getCause());
    }

    @Test
    void mapNPCReturnsFullyPopulatedEntry() throws SQLException {
        when(resultSet.getInt(FIELD_ID)).thenReturn(1);
        when(resultSet.getString(FIELD_CREATED)).thenReturn("2024-01-01");
        when(resultSet.getInt(FIELD_CREATEDBY)).thenReturn(2);
        when(resultSet.getString(FIELD_UPDATED)).thenReturn("2024-01-02");
        when(resultSet.getInt(FIELD_UPDATEDBY)).thenReturn(3);
        when(resultSet.getString(FIELD_DELETED)).thenReturn("2024-01-03");
        when(resultSet.getInt(FIELD_DELETEDBY)).thenReturn(4);
        when(resultSet.getString(FIELD_NAME)).thenReturn("TestNPC");
        when(resultSet.getString(FIELD_PROFESSION)).thenReturn("FARMER");
        when(resultSet.getString(FIELD_TYPE)).thenReturn("TRADER");

        for (int i = 0; i <= 27; i++) {
            when(resultSet.getString(String.format(FIELD_SLOT_VAR_NAME, (i + 1)))).thenReturn("slot_" + i);
        }

        NPCEntry result = NPCMapper.mapNPC(resultSet, PROFESSION_RESOLVER);

        assertAll(
                () -> assertEquals(1, result.getId()),
                () -> assertEquals("2024-01-01", result.getCreated()),
                () -> assertEquals(2, result.getCreatedBy()),
                () -> assertEquals("2024-01-02", result.getUpdated()),
                () -> assertEquals(3, result.getUpdatedBy()),
                () -> assertEquals("2024-01-03", result.getDeleted()),
                () -> assertEquals(4, result.getDeletedBy()),
                () -> assertEquals("TestNPC", result.getName()),
                () -> assertNotNull(result.getProfession()),
                () -> assertEquals(NPC.Type.TRADER, result.getType())
        );

        for (int i = 0; i <= 27; i++) {
            final int index = i;
            assertAll(() -> assertEquals("slot_" + index, result.getSlotName(index)));
        }
    }

    @Test
    void mapNPCPropagatesSQLException() throws SQLException {
        when(resultSet.getInt(FIELD_ID)).thenThrow(new SQLException("DB error"));

        assertThrows(SQLException.class, () -> NPCMapper.mapNPC(resultSet, PROFESSION_RESOLVER));
    }
}