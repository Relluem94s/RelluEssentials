package de.relluem94.minecraft.server.spigot.essentials.helpers;

import org.junit.Assert;
import org.junit.Test;

public class InventoryHelperTest {
    @Test
    public void testGetNextSlot() {
        Assert.assertEquals(10, InventoryHelper.getNextSlot(9));
    }

    @Test
    public void testGetSkipsSize() {
        Assert.assertEquals(26, InventoryHelper.getSkipsSize());
    }

    @Test
    public void testInventorySize() {
        Assert.assertEquals(9, InventoryHelper.inventorySize(5));
        Assert.assertEquals(9, InventoryHelper.inventorySize(8));
        Assert.assertEquals(9, InventoryHelper.inventorySize(9));
        Assert.assertEquals(18, InventoryHelper.inventorySize(10));
        Assert.assertEquals(18, InventoryHelper.inventorySize(16));
        Assert.assertEquals(27, InventoryHelper.inventorySize(25));
        Assert.assertEquals(27, InventoryHelper.inventorySize(27));
        Assert.assertEquals(36, InventoryHelper.inventorySize(28));
    }
}
