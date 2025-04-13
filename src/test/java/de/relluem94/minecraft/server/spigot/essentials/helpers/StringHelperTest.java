package de.relluem94.minecraft.server.spigot.essentials.helpers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class StringHelperTest {
    @Test
    public void testFirstCharToUpper() {
        String test = StringHelper.firstCharToUpper("test");
        Assertions.assertEquals("Test", test);
    }

    @Test
    public void testFormatDouble1() {
        String test = StringHelper.formatDouble(1234.12);
        Assertions.assertEquals("1.23K", test);
    }

    @Test
    public void testFormatDouble2() {
        String test2 = StringHelper.formatDouble(12341.2);
        Assertions.assertEquals("12.34K", test2);
    }

    @Test
    public void testFormatInt1() {
        String test = StringHelper.formatInt(1234);
        Assertions.assertEquals("1K", test);
    }

    @Test
    public void testFormatInt2() {
        String test2 = StringHelper.formatInt(12341);
        Assertions.assertEquals("12K", test2);
    }

    @Test
    public void testFormatLong() {
        String test = StringHelper.formatLong(12345678901L);
        Assertions.assertEquals("12.345678901B", test);
    }

    @Test
    public void testReplaceColor() {
        String test = StringHelper.replaceColor("&6Test");
        Assertions.assertEquals("§6Test", test);
    }
}
