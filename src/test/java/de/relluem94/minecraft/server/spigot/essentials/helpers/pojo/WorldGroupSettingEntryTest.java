package de.relluem94.minecraft.server.spigot.essentials.helpers.pojo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WorldGroupSettingEntryTest {

    private WorldGroupSettingEntry worldGroupSettingEntry;

    @BeforeEach
    void setUp() {
        worldGroupSettingEntry = new WorldGroupSettingEntry();
    }

    @Test
    void shouldSetAndGetId() {
        worldGroupSettingEntry.setId(1);
        assertEquals(1, worldGroupSettingEntry.getId());
    }

    @Test
    void shouldSetAndGetCreated() {
        worldGroupSettingEntry.setCreated("2024-01-01");
        assertEquals("2024-01-01", worldGroupSettingEntry.getCreated());
    }

    @Test
    void shouldSetAndGetCreatedBy() {
        worldGroupSettingEntry.setCreatedBy(10);
        assertEquals(10, worldGroupSettingEntry.getCreatedBy());
    }

    @Test
    void shouldSetAndGetUpdated() {
        worldGroupSettingEntry.setUpdated("2024-06-01");
        assertEquals("2024-06-01", worldGroupSettingEntry.getUpdated());
    }

    @Test
    void shouldSetAndGetUpdatedBy() {
        worldGroupSettingEntry.setUpdatedBy(5);
        assertEquals(5, worldGroupSettingEntry.getUpdatedBy());
    }

    @Test
    void shouldSetAndGetSettingEntryFk() {
        worldGroupSettingEntry.setSettingEntryFk(99);
        assertEquals(99, worldGroupSettingEntry.getSettingEntryFk());
    }

    @Test
    void shouldSetAndGetSettingEntry() {
        SettingEntry settingEntry = new SettingEntry();
        worldGroupSettingEntry.setSettingEntry(settingEntry);
        assertEquals(settingEntry, worldGroupSettingEntry.getSettingEntry());
    }

    @Test
    void shouldSetAndGetWorldGroupEntryFk() {
        worldGroupSettingEntry.setWorldGroupEntryFk(88);
        assertEquals(88, worldGroupSettingEntry.getWorldGroupEntryFk());
    }

    @Test
    void shouldSetAndGetWorldGroupEntry() {
        WorldGroupEntry worldGroupEntry = new WorldGroupEntry();
        worldGroupSettingEntry.setWorldGroupEntry(worldGroupEntry);
        assertEquals(worldGroupEntry, worldGroupSettingEntry.getWorldGroupEntry());
    }

    @Test
    void shouldSetAndGetValueTrue() {
        worldGroupSettingEntry.setValue(true);
        assertTrue(worldGroupSettingEntry.isValue());
    }

    @Test
    void shouldSetAndGetValueFalse() {
        worldGroupSettingEntry.setValue(false);
        assertFalse(worldGroupSettingEntry.isValue());
    }
}