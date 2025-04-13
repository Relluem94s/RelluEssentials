package de.relluem94.minecraft.server.spigot.essentials.helpers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class InventoryHelperTest {
    @Test
    public void testGetNextSlot() {
        Assertions.assertEquals(10, InventoryHelper.getNextSlot(9));
    }

    @Test
    public void testGetSkipsSize() {
        Assertions.assertEquals(26, InventoryHelper.getSkipsSize());
    }

    @Test
    public void testInventorySize() {
        Assertions.assertEquals(9, InventoryHelper.inventorySize(5));
        Assertions.assertEquals(9, InventoryHelper.inventorySize(8));
        Assertions.assertEquals(9, InventoryHelper.inventorySize(9));
        Assertions.assertEquals(18, InventoryHelper.inventorySize(10));
        Assertions.assertEquals(18, InventoryHelper.inventorySize(16));
        Assertions.assertEquals(27, InventoryHelper.inventorySize(25));
        Assertions.assertEquals(27, InventoryHelper.inventorySize(27));
        Assertions.assertEquals(36, InventoryHelper.inventorySize(28));
    }
}
