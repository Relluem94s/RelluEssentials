package de.relluem94.minecraft.server.spigot.essentials.helpers.recipe;

import org.bukkit.Material;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ShapedTest {

    @Test
    void rowsReturnsExpectedRows() {
        String[] rows = {"AAA", "BBB", "CCC"};
        Map<Character, Material> ingredients = Map.of('A', Material.STONE);
        Shaped shaped = new Shaped(rows, ingredients);

        assertAll(
                () -> assertArrayEquals(rows, shaped.rows())
        );
    }

    @Test
    void ingredientsReturnsExpectedIngredients() {
        String[] rows = {"AAA"};
        Map<Character, Material> ingredients = Map.of('A', Material.DIRT, 'B', Material.STONE);
        Shaped shaped = new Shaped(rows, ingredients);

        assertAll(
                () -> assertEquals(ingredients, shaped.ingredients())
        );
    }

    @Test
    void constructorStoresAllFieldsCorrectly() {
        String[] rows = {"ABC", "DEF", "GHI"};
        Map<Character, Material> ingredients = Map.of(
                'A', Material.STONE,
                'B', Material.DIRT,
                'C', Material.GRASS_BLOCK
        );
        Shaped shaped = new Shaped(rows, ingredients);

        assertAll(
                () -> assertArrayEquals(rows, shaped.rows()),
                () -> assertEquals(ingredients, shaped.ingredients())
        );
    }

    @Test
    void constructorAllowsNullRows() {
        Map<Character, Material> ingredients = Map.of('A', Material.STONE);
        Shaped shaped = new Shaped(null, ingredients);

        assertAll(
                () -> assertNull(shaped.rows()),
                () -> assertEquals(ingredients, shaped.ingredients())
        );
    }

    @Test
    void constructorAllowsNullIngredients() {
        String[] rows = {"AAA"};
        Shaped shaped = new Shaped(rows, null);

        assertAll(
                () -> assertArrayEquals(rows, shaped.rows()),
                () -> assertNull(shaped.ingredients())
        );
    }

    @Test
    void equalsReturnsTrueForIdenticalRecords() {
        String[] rows = {"AAA"};
        Map<Character, Material> ingredients = Map.of('A', Material.STONE);
        Shaped shaped1 = new Shaped(rows, ingredients);
        Shaped shaped2 = new Shaped(rows, ingredients);

        assertAll(
                () -> assertEquals(shaped1, shaped2)
        );
    }

    @Test
    void equalsReturnsFalseForDifferentIngredients() {
        String[] rows = {"AAA"};
        Shaped shaped1 = new Shaped(rows, Map.of('A', Material.STONE));
        Shaped shaped2 = new Shaped(rows, Map.of('A', Material.DIRT));

        assertAll(
                () -> assertNotEquals(shaped1, shaped2)
        );
    }

    @Test
    void toStringContainsFieldValues() {
        String[] rows = {"AAA"};
        Map<Character, Material> ingredients = Map.of('A', Material.STONE);
        Shaped shaped = new Shaped(rows, ingredients);

        assertAll(
                () -> assertNotNull(shaped.toString()),
                () -> assertTrue(shaped.toString().contains("Shaped"))
        );
    }

    @Test
    void hashCodeIsConsistentForEqualRecords() {
        String[] rows = {"AAA"};
        Map<Character, Material> ingredients = Map.of('A', Material.STONE);
        Shaped shaped1 = new Shaped(rows, ingredients);
        Shaped shaped2 = new Shaped(rows, ingredients);

        assertAll(
                () -> assertEquals(shaped1.hashCode(), shaped2.hashCode())
        );
    }
}