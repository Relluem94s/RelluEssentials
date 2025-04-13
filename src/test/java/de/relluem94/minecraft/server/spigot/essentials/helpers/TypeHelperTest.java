package de.relluem94.minecraft.server.spigot.essentials.helpers;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.bukkit.Material;

public class TypeHelperTest {

    @Test
    public void testIsDouble1() {
        Assertions.assertTrue(TypeHelper.isDouble("12.12"));
    }

    @Test
    public void testIsDouble2() {
        Assertions.assertFalse(TypeHelper.isDouble("12,12"));
    }

    @Test
    public void testIsDouble3() {
        Assertions.assertTrue(TypeHelper.isDouble("12.12F"));
    }

    @Test
    public void testIsFloat1() {
        Assertions.assertTrue(TypeHelper.isFloat("12.12F"));
    }

    @Test
    public void testIsFloat2() {
        Assertions.assertTrue(TypeHelper.isFloat("12.12"));
    }

    @Test
    public void testIsFloat3() {
        Assertions.assertFalse(TypeHelper.isFloat("test"));
    }

    @Test
    public void testIsInt1() {
        Assertions.assertFalse(TypeHelper.isInt("test"));
    }

    @Test
    public void testIsInt2() {
        Assertions.assertTrue(TypeHelper.isInt("123456"));
    }

    @Test
    public void testIsInt3() {
        Assertions.assertFalse(TypeHelper.isInt("19.94F"));
    }

    @Test
    public void testIsLong1() {
        Assertions.assertFalse(TypeHelper.isLong("19.94F"));
    }

    @Test
    public void testIsLong2() {
        Assertions.assertTrue(TypeHelper.isLong("1994"));
    }


    @Test
    public void testIsLong3() {
        Assertions.assertTrue(TypeHelper.isLong("192910291929192910"));
    }

    @Test
    public void testIsMaterialInList1() {
        List<Material> test = new ArrayList<>();
        test.add(Material.STONE);
        test.add(Material.COBBLESTONE);
        test.add(Material.STRIPPED_DARK_OAK_WOOD);

        Assertions.assertTrue(TypeHelper.isMaterialInList(Material.STONE, test));
    }

    @Test
    public void testIsMaterialInList2() {
        List<Material> test = new ArrayList<>();
        test.add(Material.STONE);
        test.add(Material.COBBLESTONE);
        test.add(Material.STRIPPED_DARK_OAK_WOOD);

        Assertions.assertTrue(TypeHelper.isMaterialInList(Material.STRIPPED_DARK_OAK_WOOD, test));
    }

    @Test
    public void testIsMaterialInList3() {
        List<Material> test = new ArrayList<>();
        test.add(Material.STONE);
        test.add(Material.COBBLESTONE);
        test.add(Material.STRIPPED_DARK_OAK_WOOD);

        Assertions.assertFalse(TypeHelper.isMaterialInList(Material.AIR, test));
    }
}