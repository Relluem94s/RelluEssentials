package de.relluem94.minecraft.server.spigot.essentials.model.pojo;


import de.relluem94.minecraft.server.spigot.essentials.helpers.InventoryHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.NPCHelper;
import de.relluem94.minecraft.server.spigot.essentials.npc.trader.TraderNPC.Type;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.entity.Villager.Profession;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.lang.reflect.Field;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TraderTraderNPCEntryTest {

    private TraderNPCEntry traderNpcEntry;

    @Mock
    private Profession profession;

    private static final int EXPECTED_SLOT_NAMES_SIZE = NPCHelper.INV_SIZE - InventoryHelper.getSkipsSize();

    @BeforeAll
    static void stubBukkitServer() throws NoSuchFieldException, IllegalAccessException {
        Field serverField = Bukkit.class.getDeclaredField("server");
        serverField.setAccessible(true);
        serverField.set(null, null);

        Server server = mock(Server.class);
        when(server.getLogger()).thenReturn(Logger.getLogger("test"));
        Bukkit.setServer(server);
    }

    @BeforeEach
    void setUp() {
        traderNpcEntry = new TraderNPCEntry();
    }

    @Test
    void shouldInitializeSlotNamesWithCorrectSize() {
        assertNotNull(traderNpcEntry.getSlotNames());
        assertEquals(EXPECTED_SLOT_NAMES_SIZE, traderNpcEntry.getSlotNames().length);
    }

    @Test
    void shouldSetAndGetId() {
        traderNpcEntry.setId(42);
        assertEquals(42, traderNpcEntry.getId());
    }

    @Test
    void shouldSetAndGetName() {
        traderNpcEntry.setName("TestNPC");
        assertEquals("TestNPC", traderNpcEntry.getName());
    }

    @Test
    void shouldSetAndGetProfession() {
        traderNpcEntry.setProfession(profession);
        assertEquals(profession, traderNpcEntry.getProfession());
    }

    @Test
    void shouldSetAndGetType() {
        traderNpcEntry.setType(Type.TRADER);
        assertEquals(Type.TRADER, traderNpcEntry.getType());
    }

    @Test
    void shouldSetAndGetCreated() {
        traderNpcEntry.setCreated("2024-01-01");
        assertEquals("2024-01-01", traderNpcEntry.getCreated());
    }

    @Test
    void shouldSetAndGetCreatedBy() {
        traderNpcEntry.setCreatedBy(1);
        assertEquals(1, traderNpcEntry.getCreatedBy());
    }

    @Test
    void shouldSetAndGetUpdated() {
        traderNpcEntry.setUpdated("2024-01-02");
        assertEquals("2024-01-02", traderNpcEntry.getUpdated());
    }

    @Test
    void shouldSetAndGetUpdatedBy() {
        traderNpcEntry.setUpdatedBy(2);
        assertEquals(2, traderNpcEntry.getUpdatedBy());
    }

    @Test
    void shouldSetAndGetDeleted() {
        traderNpcEntry.setDeleted("2024-01-03");
        assertEquals("2024-01-03", traderNpcEntry.getDeleted());
    }

    @Test
    void shouldSetAndGetDeletedBy() {
        traderNpcEntry.setDeletedBy(3);
        assertEquals(3, traderNpcEntry.getDeletedBy());
    }

    @Test
    void shouldSetAndGetSlotName() {
        traderNpcEntry.setSlotName(0, "SlotZero");
        assertEquals("SlotZero", traderNpcEntry.getSlotName(0));
    }

    @Test
    void shouldOverwriteExistingSlotName() {
        traderNpcEntry.setSlotName(0, "Initial");
        traderNpcEntry.setSlotName(0, "Updated");
        assertEquals("Updated", traderNpcEntry.getSlotName(0));
    }

    @Test
    void shouldReturnNullForUninitializedSlot() {
        assertNull(traderNpcEntry.getSlotName(0));
    }

    @Test
    void shouldThrowExceptionForOutOfBoundsSlot() {
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> traderNpcEntry.setSlotName(EXPECTED_SLOT_NAMES_SIZE, "OutOfBounds"));
    }
}