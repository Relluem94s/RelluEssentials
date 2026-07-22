package de.relluem94.minecraft.server.spigot.essentials.helpers.pojo;


import de.relluem94.minecraft.server.spigot.essentials.helpers.InventoryHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.NPCHelper;
import de.relluem94.minecraft.server.spigot.essentials.npc.NPC.Type;
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

class NPCEntryTest {

    private NPCEntry npcEntry;

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
        npcEntry = new NPCEntry();
    }

    @Test
    void shouldInitializeSlotNamesWithCorrectSize() {
        assertNotNull(npcEntry.getSlotNames());
        assertEquals(EXPECTED_SLOT_NAMES_SIZE, npcEntry.getSlotNames().length);
    }

    @Test
    void shouldSetAndGetId() {
        npcEntry.setId(42);
        assertEquals(42, npcEntry.getId());
    }

    @Test
    void shouldSetAndGetName() {
        npcEntry.setName("TestNPC");
        assertEquals("TestNPC", npcEntry.getName());
    }

    @Test
    void shouldSetAndGetProfession() {
        npcEntry.setProfession(profession);
        assertEquals(profession, npcEntry.getProfession());
    }

    @Test
    void shouldSetAndGetType() {
        npcEntry.setType(Type.TRADER);
        assertEquals(Type.TRADER, npcEntry.getType());
    }

    @Test
    void shouldSetAndGetCreated() {
        npcEntry.setCreated("2024-01-01");
        assertEquals("2024-01-01", npcEntry.getCreated());
    }

    @Test
    void shouldSetAndGetCreatedBy() {
        npcEntry.setCreatedBy(1);
        assertEquals(1, npcEntry.getCreatedBy());
    }

    @Test
    void shouldSetAndGetUpdated() {
        npcEntry.setUpdated("2024-01-02");
        assertEquals("2024-01-02", npcEntry.getUpdated());
    }

    @Test
    void shouldSetAndGetUpdatedBy() {
        npcEntry.setUpdatedBy(2);
        assertEquals(2, npcEntry.getUpdatedBy());
    }

    @Test
    void shouldSetAndGetDeleted() {
        npcEntry.setDeleted("2024-01-03");
        assertEquals("2024-01-03", npcEntry.getDeleted());
    }

    @Test
    void shouldSetAndGetDeletedBy() {
        npcEntry.setDeletedBy(3);
        assertEquals(3, npcEntry.getDeletedBy());
    }

    @Test
    void shouldSetAndGetSlotName() {
        npcEntry.setSlotName(0, "SlotZero");
        assertEquals("SlotZero", npcEntry.getSlotName(0));
    }

    @Test
    void shouldOverwriteExistingSlotName() {
        npcEntry.setSlotName(0, "Initial");
        npcEntry.setSlotName(0, "Updated");
        assertEquals("Updated", npcEntry.getSlotName(0));
    }

    @Test
    void shouldReturnNullForUninitializedSlot() {
        assertNull(npcEntry.getSlotName(0));
    }

    @Test
    void shouldThrowExceptionForOutOfBoundsSlot() {
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> npcEntry.setSlotName(EXPECTED_SLOT_NAMES_SIZE, "OutOfBounds"));
    }
}