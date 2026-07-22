package de.relluem94.minecraft.server.spigot.essentials.helpers.pojo;

import org.bukkit.Material;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DropEntryTest {

    private DropEntry dropEntry;

    @BeforeEach
    void setUp() {
        dropEntry = new DropEntry();
    }

    @Test
    void shouldSetAndGetId() {
        dropEntry.setId(1);
        assertEquals(1, dropEntry.getId());
    }

    @Test
    void shouldSetAndGetMaterial() {
        dropEntry.setMaterial(Material.DIAMOND);
        assertEquals(Material.DIAMOND, dropEntry.getMaterial());
    }

    @Test
    void shouldSetAndGetMin() {
        dropEntry.setMin(5);
        assertEquals(5, dropEntry.getMin());
    }

    @Test
    void shouldSetAndGetMax() {
        dropEntry.setMax(10);
        assertEquals(10, dropEntry.getMax());
    }
}