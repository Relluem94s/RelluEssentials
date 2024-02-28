package de.relluem94.minecraft.server.spigot.essentials.helpers;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import org.bukkit.Material;

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
        Assert.assertTrue(TypeHelper.isDouble("12.12"));
    }

    @Test
    public void testIsDouble2() {
        Assert.assertFalse(TypeHelper.isDouble("12,12"));
    }

    @Test
    public void testIsDouble3() {
        Assert.assertTrue(TypeHelper.isDouble("12.12F"));
    }

    @Test
    public void testIsFloat1() {
        Assert.assertTrue(TypeHelper.isFloat("12.12F"));
    }

    @Test
    public void testIsFloat2() {
        Assert.assertTrue(TypeHelper.isFloat("12.12"));
    }

    @Test
    public void testIsFloat3() {
        Assert.assertFalse(TypeHelper.isFloat("test"));
    }

    @Test
    public void testIsInt1() {
        Assert.assertFalse(TypeHelper.isInt("test"));
    }

    @Test
    public void testIsInt2() {
        Assert.assertTrue(TypeHelper.isInt("123456"));
    }

    @Test
    public void testIsInt3() {
        Assert.assertFalse(TypeHelper.isInt("19.94F"));
    }

    @Test
    public void testIsLong1() {
        Assert.assertFalse(TypeHelper.isLong("19.94F"));
    }

    @Test
    public void testIsLong2() {
        Assert.assertTrue(TypeHelper.isLong("1994"));
    }


    @Test
    public void testIsLong3() {
        Assert.assertTrue(TypeHelper.isLong("192910291929192910"));
    }

    @Test
    public void testIsMaterialInList1() {
        List<Material> test = new ArrayList<>();
        test.add(Material.STONE);
        test.add(Material.COBBLESTONE);
        test.add(Material.STRIPPED_DARK_OAK_WOOD);

        Assert.assertTrue(TypeHelper.isMaterialInList(Material.STONE, test));
    }

    @Test
    public void testIsMaterialInList2() {
        List<Material> test = new ArrayList<>();
        test.add(Material.STONE);
        test.add(Material.COBBLESTONE);
        test.add(Material.STRIPPED_DARK_OAK_WOOD);

        Assert.assertTrue(TypeHelper.isMaterialInList(Material.STRIPPED_DARK_OAK_WOOD, test));
    }

    @Test
    public void testIsMaterialInList3() {
        List<Material> test = new ArrayList<>();
        test.add(Material.STONE);
        test.add(Material.COBBLESTONE);
        test.add(Material.STRIPPED_DARK_OAK_WOOD);

        Assert.assertFalse(TypeHelper.isMaterialInList(Material.AIR, test));
    }
}