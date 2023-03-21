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
    public void testIsFloat1() {
        Assert.assertEquals(true, TypeHelper.isFloat("12.12F"));
    }

    @Test
    public void testIsFloat2() {
        Assert.assertEquals(true, TypeHelper.isFloat("12.12"));
    }

    @Test
    public void testIsFloat3() {
        Assert.assertEquals(false, TypeHelper.isFloat("test"));
    }

    @Test
    public void testIsInt1() {
        Assert.assertEquals(false, TypeHelper.isInt("test"));
    }

    @Test
    public void testIsInt2() {
        Assert.assertEquals(true, TypeHelper.isInt("123456"));
    }

    @Test
    public void testIsInt3() {
        Assert.assertEquals(false, TypeHelper.isInt("19.94F"));
    }

    @Test
    public void testIsLong1() {
        Assert.assertEquals(false, TypeHelper.isLong("19.94F"));
    }

    @Test
    public void testIsLong2() {
        Assert.assertEquals(true, TypeHelper.isLong("1994"));
    }


    @Test
    public void testIsLong3() {
        Assert.assertEquals(true, TypeHelper.isLong("192910291929192910"));
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
