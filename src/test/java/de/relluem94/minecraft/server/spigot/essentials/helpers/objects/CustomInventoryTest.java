package de.relluem94.minecraft.server.spigot.essentials.helpers.objects;

import de.relluem94.minecraft.server.spigot.essentials.helpers.ItemHelper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


class CustomInventoryTest {

    private CustomInventory customInventory;

    @BeforeEach
    void setUp() {
        customInventory = new CustomInventory(ItemHelper.Type.BUILDING, 9, "BUILD_BLOCKS_TITLE");
    }

    @AfterEach
    void tearDown() {
        customInventory = null;
    }

    @Test
    void getType() {
        Assertions.assertEquals(ItemHelper.Type.BUILDING, customInventory.getType());
        Assertions.assertNotEquals(ItemHelper.Type.DECORATION, customInventory.getType());
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
        Assertions.assertEquals(ItemHelper.Type.BUILDING, customInventory.getType());
        customInventory.setType(ItemHelper.Type.DECORATION);
        Assertions.assertEquals(ItemHelper.Type.DECORATION, customInventory.getType());
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
        customInventory.setTitleGUI("DECORATION_BLOCKS_TITLE");
        Assertions.assertEquals("DECORATION_BLOCKS_TITLE", customInventory.getTitleGUI());
    }
}