package de.relluem94.minecraft.server.spigot.essentials.helpers.pojo;

import de.relluem94.minecraft.server.spigot.essentials.helpers.BagHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BagTypeEntryTest {

    private BagTypeEntry bagTypeEntry;

    private static final int EXPECTED_BAG_SIZE = BagHelper.BAG_SIZE;

    @BeforeEach
    void setUp() {
        bagTypeEntry = new BagTypeEntry();
    }

    @Test
    void shouldInitializeSlotNamesWithCorrectSize() {
        assertNotNull(bagTypeEntry.getSlotNames());
        assertEquals(EXPECTED_BAG_SIZE, bagTypeEntry.getSlotNames().length);
    }

    @Test
    void shouldThrowExceptionForOutOfBoundsSlot() {
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> bagTypeEntry.setSlotName(EXPECTED_BAG_SIZE, "OutOfBounds"));
    }

    @Test
    void shouldSetAndGetLastSlotName() {
        bagTypeEntry.setSlotName(EXPECTED_BAG_SIZE - 1, "LastSlot");
        assertEquals("LastSlot", bagTypeEntry.getSlotName(EXPECTED_BAG_SIZE - 1));
    }
}