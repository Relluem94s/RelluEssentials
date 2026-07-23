package de.relluem94.minecraft.server.spigot.essentials.enchantment;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class EnchantNameTest {

    @Test
    void constructorStoresAllFieldsCorrectly() {
        EnchantName enchantName = new EnchantName("sharpness", "Sharpness");

        assertAll(
                () -> assertEquals("sharpness", enchantName.name()),
                () -> assertEquals("Sharpness", enchantName.displayName())
        );
    }

    @Test
    void constructorAllowsNullName() {
        EnchantName enchantName = new EnchantName(null, "Sharpness");

        assertAll(
                () -> assertNull(enchantName.name()),
                () -> assertEquals("Sharpness", enchantName.displayName())
        );
    }

    @Test
    void constructorAllowsNullDisplayName() {
        EnchantName enchantName = new EnchantName("sharpness", null);

        assertAll(
                () -> assertEquals("sharpness", enchantName.name()),
                () -> assertNull(enchantName.displayName())
        );
    }

    @Test
    void constructorAllowsEmptyStrings() {
        EnchantName enchantName = new EnchantName("", "");

        assertAll(
                () -> assertEquals("", enchantName.name()),
                () -> assertEquals("", enchantName.displayName())
        );
    }

    @Test
    void equalsTrueForSameFieldValues() {
        EnchantName first = new EnchantName("sharpness", "Sharpness");
        EnchantName second = new EnchantName("sharpness", "Sharpness");

        assertEquals(first, second);
    }

    @Test
    void hashCodeConsistentForSameFieldValues() {
        EnchantName first = new EnchantName("sharpness", "Sharpness");
        EnchantName second = new EnchantName("sharpness", "Sharpness");

        assertEquals(first.hashCode(), second.hashCode());
    }

    @Test
    void toStringContainsAllFields() {
        EnchantName enchantName = new EnchantName("sharpness", "Sharpness");

        assertAll(
                () -> assertTrue(enchantName.toString().contains("sharpness")),
                () -> assertTrue(enchantName.toString().contains("Sharpness"))
        );
    }
}