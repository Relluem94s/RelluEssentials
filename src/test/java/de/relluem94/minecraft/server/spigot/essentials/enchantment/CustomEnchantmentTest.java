package de.relluem94.minecraft.server.spigot.essentials.enchantment;

import com.google.common.collect.Multimap;
import de.relluem94.minecraft.server.spigot.essentials.helpers.ItemHelper;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.EnchantmentTarget;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class CustomEnchantmentTest {

    @Mock
    private NamespacedKey namespacedKey;

    @Mock
    private Multimap<Attribute, AttributeModifier> attributes;

    private CustomEnchantment customEnchantment;

    @BeforeEach
    void setUp() {
        customEnchantment = new CustomEnchantment(namespacedKey);
    }

    @Test
    void constructorInitializesKeyCorrectly() {
        assertAll(
                () -> assertNotNull(customEnchantment.getKey()),
                () -> assertEquals(namespacedKey, customEnchantment.getKey())
        );
    }

    @Test
    void constructorLeavesOptionalFieldsAsNull() throws Exception {
        Field loreField = CustomEnchantment.class.getDeclaredField("lore");
        loreField.setAccessible(true);

        Field rarityField = CustomEnchantment.class.getDeclaredField("rarity");
        rarityField.setAccessible(true);

        Field targetField = CustomEnchantment.class.getDeclaredField("target");
        targetField.setAccessible(true);

        Field levelField = CustomEnchantment.class.getDeclaredField("level");
        levelField.setAccessible(true);

        Field enchantNameField = CustomEnchantment.class.getDeclaredField("enchantName");
        enchantNameField.setAccessible(true);

        Field attributesField = CustomEnchantment.class.getDeclaredField("attributes");
        attributesField.setAccessible(true);

        assertAll(
                () -> assertNull(loreField.get(customEnchantment)),
                () -> assertNull(rarityField.get(customEnchantment)),
                () -> assertNull(targetField.get(customEnchantment)),
                () -> assertNull(levelField.get(customEnchantment)),
                () -> assertNull(enchantNameField.get(customEnchantment)),
                () -> assertNull(attributesField.get(customEnchantment))
        );
    }

    @Test
    void constructorInitializesMultiplyAndActualLevelToDefaults() throws Exception {
        Field multiplyField = CustomEnchantment.class.getDeclaredField("multiply");
        multiplyField.setAccessible(true);

        Field actualLevelField = CustomEnchantment.class.getDeclaredField("actualLevel");
        actualLevelField.setAccessible(true);

        assertAll(
                () -> assertEquals(0.0, (double) multiplyField.get(customEnchantment)),
                () -> assertEquals(0, (int) actualLevelField.get(customEnchantment))
        );
    }

    @Test
    void getLoreReturnsAssignedLore() throws Exception {
        Field loreField = CustomEnchantment.class.getDeclaredField("lore");
        loreField.setAccessible(true);
        loreField.set(customEnchantment, "Test Lore");

        assertEquals("Test Lore", customEnchantment.getLore());
    }

    @Test
    void getRarityReturnsAssignedRarity() throws Exception {
        Field rarityField = CustomEnchantment.class.getDeclaredField("rarity");
        rarityField.setAccessible(true);
        rarityField.set(customEnchantment, ItemHelper.Rarity.COMMON);

        assertEquals(ItemHelper.Rarity.COMMON, customEnchantment.getRarity());
    }

    @Test
    void getAttributesReturnsAssignedAttributes() throws Exception {
        Field attributesField = CustomEnchantment.class.getDeclaredField("attributes");
        attributesField.setAccessible(true);
        attributesField.set(customEnchantment, attributes);

        assertEquals(attributes, customEnchantment.getAttributes());
    }

    @Test
    void getKeyReturnsNullWhenConstructedWithNull() {
        CustomEnchantment enchantmentWithNullKey = new CustomEnchantment(null);

        assertNull(enchantmentWithNullKey.getKey());
    }

    @Test
    void constructorWithDifferentKeyStoresCorrectReference() {
        NamespacedKey anotherKey = mock(NamespacedKey.class);
        CustomEnchantment anotherEnchantment = new CustomEnchantment(anotherKey);

        assertAll(
                () -> assertEquals(anotherKey, anotherEnchantment.getKey()),
                () -> assertNotEquals(namespacedKey, anotherEnchantment.getKey())
        );
    }

    @Test
    void targetFieldCanBeSetAndRetrievedViaReflection() throws Exception {
        Field targetField = CustomEnchantment.class.getDeclaredField("target");
        targetField.setAccessible(true);
        targetField.set(customEnchantment, EnchantmentTarget.WEAPON);

        assertEquals(EnchantmentTarget.WEAPON, targetField.get(customEnchantment));
    }

    @Test
    void multiplyFieldCanBeSetAndRetrievedViaReflection() throws Exception {
        Field multiplyField = CustomEnchantment.class.getDeclaredField("multiply");
        multiplyField.setAccessible(true);
        multiplyField.set(customEnchantment, 2.5);

        assertEquals(2.5, (double) multiplyField.get(customEnchantment));
    }

    @Test
    void actualLevelFieldCanBeSetAndRetrievedViaReflection() throws Exception {
        Field actualLevelField = CustomEnchantment.class.getDeclaredField("actualLevel");
        actualLevelField.setAccessible(true);
        actualLevelField.set(customEnchantment, 3);

        assertEquals(3, (int) actualLevelField.get(customEnchantment));
    }
}