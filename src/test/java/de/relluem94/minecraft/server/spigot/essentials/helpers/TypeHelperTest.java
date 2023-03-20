package de.relluem94.minecraft.server.spigot.essentials.helpers;

import org.junit.Assert;
import org.junit.Test;

public class TypeHelperTest {
    @Test
    public void testAreBlocksMaterial() {

    }

    @Test
    public void testIsBlockOnOfMaterials() {

    }

    @Test
    public void testIsCMDBlock() {

    }

    @Test
    public void testIsConsole() {

    }

    @Test
    public void testIsDouble1() {
        Assert.assertEquals(true, TypeHelper.isDouble("12.12"));
    }

    @Test
    public void testIsDouble2() {
        Assert.assertEquals(false, TypeHelper.isDouble("12,12"));
    }

    @Test
    public void testIsDouble3() {
        Assert.assertEquals(true, TypeHelper.isDouble("12.12F"));
    }

    @Test
    public void testIsFloat() {

    }

    @Test
    public void testIsInt() {

    }

    @Test
    public void testIsLong() {

    }

    @Test
    public void testIsMaterialInArray() {

    }

    @Test
    public void testIsMaterialInList() {

    }

    @Test
    public void testIsPlayer() {

    }
}
