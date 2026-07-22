package de.relluem94.minecraft.server.spigot.essentials.helpers.pojo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Properties;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OfflinePlayerEntryTest {

    private OfflinePlayerEntry offlinePlayerEntry;

    @BeforeEach
    void setUp() {
        offlinePlayerEntry = new OfflinePlayerEntry();
    }

    @Test
    void shouldSetAndGetId() {
        UUID uuid = UUID.randomUUID();
        offlinePlayerEntry.setId(uuid);
        assertEquals(uuid, offlinePlayerEntry.getId());
    }

    @Test
    void shouldSetAndGetName() {
        offlinePlayerEntry.setName("TestPlayer");
        assertEquals("TestPlayer", offlinePlayerEntry.getName());
    }

    @Test
    void shouldSetAndGetProperties() {
        Properties properties = new Properties();
        properties.setProperty("key", "value");
        offlinePlayerEntry.setProperties(properties);
        assertEquals(properties, offlinePlayerEntry.getProperties());
        assertEquals("value", offlinePlayerEntry.getProperties().getProperty("key"));
    }
}