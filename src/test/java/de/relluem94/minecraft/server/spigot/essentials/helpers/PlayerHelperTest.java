package de.relluem94.minecraft.server.spigot.essentials.helpers;

import java.util.UUID;

import org.junit.Assert;
import org.junit.Test;

import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.OfflinePlayerEntry;

public class PlayerHelperTest {
    @Test
    public void testGetOfflinePlayerByName1() {
        OfflinePlayerEntry ope = PlayerHelper.getOfflinePlayerByName("Relluem94");
        Assert.assertEquals(true, ope != null);
    }

    @Test
    public void testGetOfflinePlayerByName2() {
        OfflinePlayerEntry ope = PlayerHelper.getOfflinePlayerByName("Relluem94");
        Assert.assertEquals(UUID.fromString("ec0149f9-8b21-44ee-9731-8bff508087e7"), ope.getID());
    }

    @Test
    public void testGetOfflinePlayerByUUID1() {
        OfflinePlayerEntry ope = PlayerHelper.getOfflinePlayerByUUID(UUID.fromString("ec0149f9-8b21-44ee-9731-8bff508087e7"));
        Assert.assertEquals(true, ope != null);
    }

    @Test
    public void testGetOfflinePlayerByUUID2() {
        OfflinePlayerEntry ope = PlayerHelper.getOfflinePlayerByUUID(UUID.fromString("ec0149f9-8b21-44ee-9731-8bff508087e7"));
        Assert.assertEquals("Relluem94", ope.getName());
    }
}
