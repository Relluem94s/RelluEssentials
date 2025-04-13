package de.relluem94.minecraft.server.spigot.essentials.helpers;

import java.util.UUID;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class UUIDHelperTest {
    @Test
    public void testDashed() {
        String test = "ec0149f98b2144ee97318bff508087e7";
        Assertions.assertEquals(UUID.fromString("ec0149f9-8b21-44ee-9731-8bff508087e7"), UUIDHelper.dashed(test));
    }

    @Test
    public void testUnDashed() {
        UUID test = UUID.fromString("ec0149f9-8b21-44ee-9731-8bff508087e7") ;
        Assertions.assertEquals("ec0149f98b2144ee97318bff508087e7", UUIDHelper.unDashed(test));
    }
}