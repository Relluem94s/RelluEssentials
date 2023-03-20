package de.relluem94.minecraft.server.spigot.essentials.helpers;

import org.junit.Assert;
import org.junit.Test;

public class StringHelperTest {
    @Test
    public void testFirstCharToUpper() {
        String test = StringHelper.firstCharToUpper("test");
        Assert.assertEquals("Test", test);
    }

    @Test
    public void testFormatDouble() {
        String test = StringHelper.formatDouble(1234.12);
        Assert.assertEquals("1.23K", test);

        String test2 = StringHelper.formatDouble(12341.2);
        Assert.assertEquals("12.34K", test2);
    }

    @Test
    public void testFormatInt() {
        String test = StringHelper.formatInt(1234);
        Assert.assertEquals("1K", test);

        String test2 = StringHelper.formatInt(12341);
        Assert.assertEquals("12K", test2);
    }

    @Test
    public void testFormatLong() {
        String test = StringHelper.formatLong(12345678901L);
        Assert.assertEquals("12.345678901B", test);
    }

    @Test
    public void testReplaceColor() {
        String test = StringHelper.replaceColor("&6Test");
        Assert.assertEquals("§6Test", test);
    }
}
