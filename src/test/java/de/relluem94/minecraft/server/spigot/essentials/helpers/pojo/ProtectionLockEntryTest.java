package de.relluem94.minecraft.server.spigot.essentials.helpers.pojo;

import org.bukkit.Material;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ProtectionLockEntryTest {

    private ProtectionLockEntry protectionLockEntry;

    @BeforeEach
    void setUp() {
        protectionLockEntry = new ProtectionLockEntry();
    }

    @Test
    void shouldSetAndGetId() {
        protectionLockEntry.setId(1);
        assertEquals(1, protectionLockEntry.getId());
    }

    @Test
    void shouldSetAndGetCreated() {
        protectionLockEntry.setCreated("2024-01-01");
        assertEquals("2024-01-01", protectionLockEntry.getCreated());
    }

    @Test
    void shouldSetAndGetCreatedBy() {
        protectionLockEntry.setCreatedBy(42);
        assertEquals(42, protectionLockEntry.getCreatedBy());
    }

    @Test
    void shouldSetAndGetUpdated() {
        protectionLockEntry.setUpdated("2024-06-01");
        assertEquals("2024-06-01", protectionLockEntry.getUpdated());
    }

    @Test
    void shouldSetAndGetUpdatedBy() {
        protectionLockEntry.setUpdatedBy(7);
        assertEquals(7, protectionLockEntry.getUpdatedBy());
    }

    @Test
    void shouldSetAndGetDeleted() {
        protectionLockEntry.setDeleted("2024-12-01");
        assertEquals("2024-12-01", protectionLockEntry.getDeleted());
    }

    @Test
    void shouldSetAndGetDeletedBy() {
        protectionLockEntry.setDeletedBy(3);
        assertEquals(3, protectionLockEntry.getDeletedBy());
    }

    @Test
    void shouldSetAndGetValue() {
        protectionLockEntry.setValue(Material.CHEST);
        assertEquals(Material.CHEST, protectionLockEntry.getValue());
    }
}