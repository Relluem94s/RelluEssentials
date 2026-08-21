package de.relluem94.minecraft.server.spigot.essentials.models;

import de.relluem94.minecraft.server.spigot.essentials.models.items.CustomItem;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


class CustomInventoryTest {

    private CustomInventory customInventory;

    @BeforeEach
    void setUp() {
        customInventory = new CustomInventory(CustomItem.Type.BUILDING, 9, "BUILD_BLOCKS_TITLE");
    }

    @AfterEach
    void tearDown() {
        customInventory = null;
    }

    @Test
    void getType() {
        Assertions.assertEquals(CustomItem.Type.BUILDING, customInventory.getType());
        Assertions.assertNotEquals(CustomItem.Type.DECORATION, customInventory.getType());
    }

    @Test
    void getSize() {
        Assertions.assertEquals(9, customInventory.getSize());
    }

    @Test
    void getTitleGUI() {
        Assertions.assertEquals("BUILD_BLOCKS_TITLE", customInventory.getTitleGUI());
    }

    @Test
    void setType() {
        Assertions.assertEquals(CustomItem.Type.BUILDING, customInventory.getType());
        customInventory.setType(CustomItem.Type.DECORATION);
        Assertions.assertEquals(CustomItem.Type.DECORATION, customInventory.getType());
    }

    @Test
    void setSize() {
        Assertions.assertEquals(9, customInventory.getSize());
        customInventory.setSize(18);
        Assertions.assertEquals(18, customInventory.getSize());
    }

    @Test
    void setTitleGUI() {
        Assertions.assertEquals("BUILD_BLOCKS_TITLE", customInventory.getTitleGUI());
        customInventory.setTitleGui("DECORATION_BLOCKS_TITLE");
        Assertions.assertEquals("DECORATION_BLOCKS_TITLE", customInventory.getTitleGUI());
    }
}