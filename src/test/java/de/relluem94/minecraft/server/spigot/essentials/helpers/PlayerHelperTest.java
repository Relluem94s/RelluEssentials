package de.relluem94.minecraft.server.spigot.essentials.helpers;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.UUID;

import org.junit.Assert;
import org.junit.Test;

import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.OfflinePlayerEntry;

public class PlayerHelperTest {
    @Test
    public void testGetOfflinePlayerByName1() {
        OfflinePlayerEntry ope = PlayerHelper.getOfflinePlayerByName("Relluem94");
        Assert.assertTrue(ope != null);
    }

    @Test
    public void testGetOfflinePlayerByName2() {
        OfflinePlayerEntry ope = PlayerHelper.getOfflinePlayerByName("Relluem94");
        Assert.assertEquals(UUID.fromString("ec0149f9-8b21-44ee-9731-8bff508087e7"), ope.getId());
    }

    @Test
    public void testGetOfflinePlayerByName3() {
        PrintStream sysOut = System.out;

        PrintStream noOut = new PrintStream(new OutputStream(){
            public void write(int b) {}
        });

        System.setOut(noOut); // To disable Error Message from NetworkUtils

        OfflinePlayerEntry ope = PlayerHelper.getOfflinePlayerByName("Relluem9494");
        System.setOut(sysOut);

        Assert.assertFalse(ope != null);
    }

    @Test
    public void testGetOfflinePlayerByUUID1() {
        OfflinePlayerEntry ope = PlayerHelper.getOfflinePlayerByUUID(UUID.fromString("ec0149f9-8b21-44ee-9731-8bff508087e7"));
        Assert.assertTrue(ope != null);
    }

    @Test
    public void testGetOfflinePlayerByUUID2() {
        OfflinePlayerEntry ope = PlayerHelper.getOfflinePlayerByUUID(UUID.fromString("ec0149f9-8b21-44ee-9731-8bff508087e7"));
        Assert.assertEquals("Relluem94", ope.getName());
    }

    @Test
    public void testGetOfflinePlayerByUUID3() {
        OfflinePlayerEntry ope = PlayerHelper.getOfflinePlayerByUUID(UUID.fromString("00000000-AAAA-AAAA-0000-AAFFAAAAAAAA"));
        Assert.assertFalse(ope != null);
    }
}