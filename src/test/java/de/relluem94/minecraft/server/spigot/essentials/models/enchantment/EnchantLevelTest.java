package de.relluem94.minecraft.server.spigot.essentials.models.enchantment;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class EnchantLevelTest {

    @Test
    void constructorStoresStartLevelAndMaxLevel() {
        EnchantLevel enchantLevel = new EnchantLevel(1, 5);

        assertAll(
                () -> assertEquals(1, enchantLevel.startLevel()),
                () -> assertEquals(5, enchantLevel.maxLevel())
        );
    }

    @Test
    void constructorStoresZeroStartLevelAndZeroMaxLevel() {
        EnchantLevel enchantLevel = new EnchantLevel(0, 0);

        assertAll(
                () -> assertEquals(0, enchantLevel.startLevel()),
                () -> assertEquals(0, enchantLevel.maxLevel())
        );
    }

    @Test
    void constructorStoresNegativeStartLevelAndNegativeMaxLevel() {
        EnchantLevel enchantLevel = new EnchantLevel(-3, -1);

        assertAll(
                () -> assertEquals(-3, enchantLevel.startLevel()),
                () -> assertEquals(-1, enchantLevel.maxLevel())
        );
    }
}