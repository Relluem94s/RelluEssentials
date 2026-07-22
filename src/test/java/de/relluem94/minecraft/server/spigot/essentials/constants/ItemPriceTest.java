package de.relluem94.minecraft.server.spigot.essentials.constants;

import org.bukkit.Material;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.junit.jupiter.api.Assertions.*;

class ItemPriceTest {

    @ParameterizedTest
    @EnumSource(ItemPrice.class)
    void eachItemPriceHasNonNegativeBuyPrice(ItemPrice itemPrice) {
        assertTrue(itemPrice.getBuyPrice() >= 0);
    }

    @ParameterizedTest
    @EnumSource(ItemPrice.class)
    void eachItemPriceHasNonNegativeSellPrice(ItemPrice itemPrice) {
        assertTrue(itemPrice.getSellPrice() >= 0);
    }

    @Test
    void unknownItemPriceHasZeroBuyPrice() {
        assertEquals(0, ItemPrice.UNKNOWN.getBuyPrice());
    }

    @Test
    void unknownItemPriceHasZeroSellPrice() {
        assertEquals(0, ItemPrice.UNKNOWN.getSellPrice());
    }

    @Test
    void fromReturnsUnknownForUnmappedMaterial() {
        ItemPrice result = ItemPrice.from(Material.AIR);
        assertEquals(ItemPrice.AIR, result);
    }

    @Test
    void allMaterialsAreMappable() {
        for (Material material : Material.values()) {
            ItemPrice result = ItemPrice.from(material);
            assertNotNull(result);
        }
    }

    @Test
    void fromReturnsUnknownForUnrecognizedMaterial() {
        ItemPrice result = ItemPrice.from(Material.LEGACY_AIR);
        assertEquals(ItemPrice.UNKNOWN, result);
    }
}