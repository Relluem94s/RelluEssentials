package de.relluem94.minecraft.server.spigot.essentials.helpers.pojo;

import org.bukkit.Material;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CropEntryTest {

    private CropEntry cropEntry;

    @BeforeEach
    void setUp() {
        cropEntry = new CropEntry();
    }

    @Test
    void shouldSetAndGetId() {
        cropEntry.setId(1);
        assertEquals(1, cropEntry.getId());
    }

    @Test
    void shouldSetAndGetPlant() {
        cropEntry.setPlant(Material.WHEAT);
        assertEquals(Material.WHEAT, cropEntry.getPlant());
    }

    @Test
    void shouldSetAndGetSeed() {
        cropEntry.setSeed(Material.WHEAT_SEEDS);
        assertEquals(Material.WHEAT_SEEDS, cropEntry.getSeed());
    }
}