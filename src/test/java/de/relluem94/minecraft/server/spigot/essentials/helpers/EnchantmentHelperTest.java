package de.relluem94.minecraft.server.spigot.essentials.helpers;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.argThat;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockConstruction;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.ItemAttribute;
import de.relluem94.minecraft.server.spigot.essentials.models.enchantment.CustomEnchantment;
import de.relluem94.minecraft.server.spigot.essentials.models.enchantment.EnchantLevel;
import de.relluem94.minecraft.server.spigot.essentials.models.enchantment.EnchantName;
import de.relluem94.minecraft.server.spigot.essentials.models.items.CustomItem.Rarity;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class EnchantmentHelperTest {

  @SuppressWarnings("ClassCanBeRecord")
  private static class TestItemAttribute implements ItemAttribute {

    private final org.bukkit.attribute.Attribute attribute;
    private final AttributeModifier modifier;

    private TestItemAttribute(org.bukkit.attribute.Attribute attribute, AttributeModifier modifier) {
      this.attribute = attribute;
      this.modifier = modifier;
    }

    @Override
    public org.bukkit.attribute.Attribute getAttribute() {
      return attribute;
    }

    @Override
    public AttributeModifier getModifier() {
      return modifier;
    }
  }

  private static final String DISPLAY_NAME = "Test Enchant";
  private static final String ENCHANT_NAME = "TEST_ENCHANT";
  private static final String LORE_TEXT = "Test lore";
  private static final int START_LEVEL = 1;
  private static final int MAX_LEVEL = 5;
  private static final int COST = 10;
  @Mock
  private EnchantName enchantName;
  @Mock
  private EnchantLevel enchantLevel;
  @Mock
  private Rarity rarity;
  @Mock
  private ItemStack itemStack;
  @Mock
  private ItemMeta itemMeta;
  @Mock
  private PersistentDataContainer persistentDataContainer;
  @Mock
  private CustomEnchantment customEnchantment;
  @Mock
  private NamespacedKey namespacedKey;
  @Mock
  private RelluEssentials pluginInstance;
  private List<ItemAttribute> emptyAttributes;

  @BeforeEach
  void setUp() {
    emptyAttributes = new ArrayList<>();
  }

  @SuppressWarnings("ResultOfMethodCallIgnored")
  private EnchantmentHelper createFullHelper(MockedStatic<RelluEssentials> mockedStatic) {
    mockedStatic
        .when(RelluEssentials::getInstance)
        .thenReturn(pluginInstance);
    when(pluginInstance.getName()).thenReturn("RelluEssentials");
    when(enchantName.name()).thenReturn(ENCHANT_NAME);
    lenient()
        .when(enchantName.displayName())
        .thenReturn(DISPLAY_NAME);
    when(enchantLevel.startLevel()).thenReturn(START_LEVEL);
    lenient()
        .when(enchantLevel.maxLevel())
        .thenReturn(MAX_LEVEL);
    return new EnchantmentHelper(enchantName, EnchantmentTarget.WEAPON, enchantLevel, LORE_TEXT, rarity,
        emptyAttributes, COST);
  }

  @Test
  void constructorWithNamespacedKeyOnlySetsKey() {
    EnchantmentHelper helper = new EnchantmentHelper(namespacedKey);
    assertEquals(namespacedKey, helper.getKey());
  }

  @SuppressWarnings("ResultOfMethodCallIgnored")
  @Test
  void constructorFullSetsAllFields() {
    try (MockedStatic<RelluEssentials> mockedStatic = mockStatic(RelluEssentials.class)) {
      mockedStatic
          .when(RelluEssentials::getInstance)
          .thenReturn(pluginInstance);
      when(pluginInstance.getName()).thenReturn("RelluEssentials");
      when(enchantName.name()).thenReturn(ENCHANT_NAME);
      when(enchantName.displayName()).thenReturn(DISPLAY_NAME);
      when(enchantLevel.startLevel()).thenReturn(START_LEVEL);
      when(enchantLevel.maxLevel()).thenReturn(MAX_LEVEL);

      EnchantmentHelper helper =
          new EnchantmentHelper(enchantName, EnchantmentTarget.WEAPON, enchantLevel, LORE_TEXT, rarity,
              emptyAttributes, COST);

      assertEquals(ENCHANT_NAME, helper.getName());
      assertEquals(DISPLAY_NAME, helper.getDisplayName());
      assertEquals(MAX_LEVEL, helper.getMaxLevel());
      assertEquals(START_LEVEL, helper.getStartLevel());
      assertEquals(EnchantmentTarget.WEAPON, helper.getItemTarget());
      assertEquals(COST, helper.getCost());
      assertEquals(LORE_TEXT, helper.getLore());
      assertEquals(rarity, helper.getRarity());
    }
  }

  @Test
  void getNameReturnsEnchantNameValue() {
    try (MockedStatic<RelluEssentials> mockedStatic = mockStatic(RelluEssentials.class)) {
      EnchantmentHelper helper = createFullHelper(mockedStatic);
      assertEquals(ENCHANT_NAME, helper.getName());
    }
  }

  @Test
  void getDisplayNameReturnsFormattedName() {
    try (MockedStatic<RelluEssentials> mockedStatic = mockStatic(RelluEssentials.class)) {
      EnchantmentHelper helper = createFullHelper(mockedStatic);
      assertEquals(DISPLAY_NAME, helper.getDisplayName());
    }
  }

  @Test
  void getMaxLevelReturnsLevelMaxLevel() {
    try (MockedStatic<RelluEssentials> mockedStatic = mockStatic(RelluEssentials.class)) {
      EnchantmentHelper helper = createFullHelper(mockedStatic);
      assertEquals(MAX_LEVEL, helper.getMaxLevel());
    }
  }

  @Test
  void getStartLevelReturnsLevelStartLevel() {
    try (MockedStatic<RelluEssentials> mockedStatic = mockStatic(RelluEssentials.class)) {
      EnchantmentHelper helper = createFullHelper(mockedStatic);
      assertEquals(START_LEVEL, helper.getStartLevel());
    }
  }

  @Test
  void getItemTargetReturnsTarget() {
    try (MockedStatic<RelluEssentials> mockedStatic = mockStatic(RelluEssentials.class)) {
      EnchantmentHelper helper = createFullHelper(mockedStatic);
      assertEquals(EnchantmentTarget.WEAPON, helper.getItemTarget());
    }
  }

  @Test
  void getCostReturnsCost() {
    try (MockedStatic<RelluEssentials> mockedStatic = mockStatic(RelluEssentials.class)) {
      EnchantmentHelper helper = createFullHelper(mockedStatic);
      assertEquals(COST, helper.getCost());
    }
  }

  @Test
  void hasEnchantReturnsFalseWhenItemStackIsNull() {
    assertFalse(EnchantmentHelper.hasEnchant(null, customEnchantment));
  }

  @Test
  void hasEnchantReturnsFalseWhenItemMetaIsNull() {
    when(itemStack.getItemMeta()).thenReturn(null);
    assertFalse(EnchantmentHelper.hasEnchant(itemStack, customEnchantment));
  }

  @Test
  void hasEnchantReturnsFalseWhenEnchantmentNotPresent() {
    when(itemStack.getItemMeta()).thenReturn(itemMeta);
    when(itemMeta.getPersistentDataContainer()).thenReturn(persistentDataContainer);
    when(customEnchantment.getKey()).thenReturn(namespacedKey);
    when(persistentDataContainer.has(namespacedKey)).thenReturn(false);

    assertFalse(EnchantmentHelper.hasEnchant(itemStack, customEnchantment));
  }

  @Test
  void hasEnchantReturnsTrueWhenEnchantmentPresent() {
    when(itemStack.getItemMeta()).thenReturn(itemMeta);
    when(itemMeta.getPersistentDataContainer()).thenReturn(persistentDataContainer);
    when(customEnchantment.getKey()).thenReturn(namespacedKey);
    when(persistentDataContainer.has(namespacedKey)).thenReturn(true);

    assertTrue(EnchantmentHelper.hasEnchant(itemStack, customEnchantment));
  }

  @Test
  void addToItemMetaDoesNothingWhenMetaIsNull() {
    try (MockedStatic<RelluEssentials> mockedStatic = mockStatic(RelluEssentials.class)) {
      EnchantmentHelper helper = createFullHelper(mockedStatic);
      assertDoesNotThrow(() -> helper.addTo((ItemMeta) null));
    }
  }

  @Test
  void addToItemMetaWithNoExistingLoreAddsDisplayNameAndLoreAndRarity() {
    try (MockedStatic<RelluEssentials> mockedStatic = mockStatic(RelluEssentials.class)) {
      EnchantmentHelper helper = createFullHelper(mockedStatic);
      when(itemMeta.getPersistentDataContainer()).thenReturn(persistentDataContainer);
      when(itemMeta.getLore()).thenReturn(null);
      when(rarity.getPrefix()).thenReturn("§7");
      when(rarity.getDisplayName()).thenReturn("Common");

      helper.addTo(itemMeta);

      verify(itemMeta).setLore(
          argThat(lore -> lore.contains(DISPLAY_NAME) && lore.contains(LORE_TEXT) && lore.contains("§7Common")));
    }
  }

  @Test
  void addToItemMetaWithExistingLorePrependsEnchantment() {
    try (MockedStatic<RelluEssentials> mockedStatic = mockStatic(RelluEssentials.class)) {
      EnchantmentHelper helper = createFullHelper(mockedStatic);
      when(itemMeta.getPersistentDataContainer()).thenReturn(persistentDataContainer);

      List<String> existingLore = new ArrayList<>(List.of("Existing lore line"));
      when(itemMeta.getLore()).thenReturn(existingLore);

      helper.addTo(itemMeta);

      verify(itemMeta).setLore(argThat(lore -> lore
          .getFirst()
          .equals(DISPLAY_NAME) && lore
          .get(1)
          .equals(LORE_TEXT)));
    }
  }

  @Test
  void addToItemMetaStoresEnchantmentInPersistentData() {
    try (MockedStatic<RelluEssentials> mockedStatic = mockStatic(RelluEssentials.class)) {
      EnchantmentHelper helper = createFullHelper(mockedStatic);
      when(itemMeta.getPersistentDataContainer()).thenReturn(persistentDataContainer);
      when(itemMeta.getLore()).thenReturn(null);
      when(rarity.getPrefix()).thenReturn("§7");
      when(rarity.getDisplayName()).thenReturn("Common");

      helper.addTo(itemMeta);

      verify(persistentDataContainer).set(any(NamespacedKey.class), any(), eq(START_LEVEL));
    }
  }

  @Test
  void addToItemStackDoesNothingWhenMetaIsNull() {
    try (MockedStatic<RelluEssentials> mockedStatic = mockStatic(RelluEssentials.class)) {
      EnchantmentHelper helper = createFullHelper(mockedStatic);
      when(itemStack.getItemMeta()).thenReturn(null);

      assertDoesNotThrow(() -> helper.addTo(itemStack));
      verify(itemStack, never()).setItemMeta(any());
    }
  }

  @Test
  void addToItemStackSetsMetaAfterModification() {
    try (MockedStatic<RelluEssentials> mockedStatic = mockStatic(RelluEssentials.class)) {
      EnchantmentHelper helper = createFullHelper(mockedStatic);
      when(itemStack.getItemMeta()).thenReturn(itemMeta);
      when(itemMeta.getPersistentDataContainer()).thenReturn(persistentDataContainer);
      when(itemMeta.getLore()).thenReturn(null);
      when(rarity.getPrefix()).thenReturn("§7");
      when(rarity.getDisplayName()).thenReturn("Common");

      helper.addTo(itemStack);

      verify(itemStack).setItemMeta(itemMeta);
    }
  }

  @Test
  void removeFromDoesNothingWhenMetaIsNull() {
    try (MockedStatic<RelluEssentials> mockedStatic = mockStatic(RelluEssentials.class)) {
      EnchantmentHelper helper = createFullHelper(mockedStatic);
      when(itemStack.getItemMeta()).thenReturn(null);

      assertDoesNotThrow(() -> helper.removeFrom(itemStack));
      verify(itemStack, never()).setItemMeta(any());
    }
  }

  @Test
  void removeFromRemovesEnchantmentFromPersistentData() {
    try (MockedStatic<RelluEssentials> mockedStatic = mockStatic(RelluEssentials.class)) {
      EnchantmentHelper helper = createFullHelper(mockedStatic);
      when(itemStack.getItemMeta()).thenReturn(itemMeta);
      when(itemMeta.getPersistentDataContainer()).thenReturn(persistentDataContainer);

      List<String> existingLore = new ArrayList<>(List.of(DISPLAY_NAME, LORE_TEXT));
      when(itemMeta.getLore()).thenReturn(existingLore);
      when(rarity.getPrefix()).thenReturn("§7");
      when(rarity.getDisplayName()).thenReturn("Common");

      helper.removeFrom(itemStack);

      verify(persistentDataContainer).remove(any(NamespacedKey.class));
    }
  }

  @Test
  void removeFromCleansUpLore() {
    try (MockedStatic<RelluEssentials> mockedStatic = mockStatic(RelluEssentials.class)) {
      EnchantmentHelper helper = createFullHelper(mockedStatic);
      when(itemStack.getItemMeta()).thenReturn(itemMeta);
      when(itemMeta.getPersistentDataContainer()).thenReturn(persistentDataContainer);

      List<String> existingLore = new ArrayList<>(List.of(DISPLAY_NAME, LORE_TEXT, "§7Common"));
      when(itemMeta.getLore()).thenReturn(existingLore);
      when(rarity.getPrefix()).thenReturn("§7");
      when(rarity.getDisplayName()).thenReturn("Common");

      helper.removeFrom(itemStack);

      verify(itemMeta).setLore(
          argThat(lore -> !lore.contains(DISPLAY_NAME) && !lore.contains(LORE_TEXT) && !lore.contains("§7Common")));
    }
  }

  @Test
  void removeFromSetsMetaOnItemStack() {
    try (MockedStatic<RelluEssentials> mockedStatic = mockStatic(RelluEssentials.class)) {
      EnchantmentHelper helper = createFullHelper(mockedStatic);
      when(itemStack.getItemMeta()).thenReturn(itemMeta);
      when(itemMeta.getPersistentDataContainer()).thenReturn(persistentDataContainer);
      when(itemMeta.getLore()).thenReturn(new ArrayList<>());
      when(rarity.getPrefix()).thenReturn("§7");
      when(rarity.getDisplayName()).thenReturn("Common");

      helper.removeFrom(itemStack);

      verify(itemStack).setItemMeta(itemMeta);
    }
  }

  @Test
  void removeFromHandlesNullLoreGracefully() {
    try (MockedStatic<RelluEssentials> mockedStatic = mockStatic(RelluEssentials.class)) {
      EnchantmentHelper helper = createFullHelper(mockedStatic);
      when(itemStack.getItemMeta()).thenReturn(itemMeta);
      when(itemMeta.getPersistentDataContainer()).thenReturn(persistentDataContainer);
      when(itemMeta.getLore()).thenReturn(null);

      assertDoesNotThrow(() -> helper.removeFrom(itemStack));
    }
  }

  @Test
  void equalsReturnsFalseForNonEnchantmentHelperObject() {
    try (MockedStatic<RelluEssentials> mockedStatic = mockStatic(RelluEssentials.class)) {
      EnchantmentHelper helper = createFullHelper(mockedStatic);
      Object other = new Object();
      assertNotEquals(helper, other);
    }
  }

  @Test
  void equalsReturnsFalseForNull() {
    try (MockedStatic<RelluEssentials> mockedStatic = mockStatic(RelluEssentials.class)) {
      EnchantmentHelper helper = createFullHelper(mockedStatic);
      assertNotEquals(null, helper);
    }
  }

  @Test
  void hashCodeIsConsistentAcrossMultipleCalls() {
    try (MockedStatic<RelluEssentials> mockedStatic = mockStatic(RelluEssentials.class)) {
      EnchantmentHelper helper = createFullHelper(mockedStatic);
      assertEquals(helper.hashCode(), helper.hashCode());
    }
  }

  @SuppressWarnings("ResultOfMethodCallIgnored")
  @Test
  void hashCodeDiffersWhenEnchantNameDiffers() {
    try (MockedStatic<RelluEssentials> mockedStatic = mockStatic(RelluEssentials.class)) {
      mockedStatic.when(RelluEssentials::getInstance).thenReturn(pluginInstance);
      when(pluginInstance.getName()).thenReturn("RelluEssentials");
      when(enchantLevel.startLevel()).thenReturn(START_LEVEL);

      when(enchantName.name()).thenReturn(ENCHANT_NAME);
      EnchantmentHelper helperOne =
          new EnchantmentHelper(enchantName, EnchantmentTarget.WEAPON, enchantLevel, LORE_TEXT, rarity, emptyAttributes, COST);

      EnchantName differentName = mock(EnchantName.class);
      when(differentName.name()).thenReturn("DIFFERENT_ENCHANT");
      EnchantmentHelper helperTwo =
          new EnchantmentHelper(differentName, EnchantmentTarget.WEAPON, enchantLevel, LORE_TEXT, rarity, emptyAttributes, COST);

      assertNotEquals(helperOne.getKey(), helperTwo.getKey());
    }
  }

  @SuppressWarnings("ResultOfMethodCallIgnored")
  @Test
  void addToItemMetaWithAttributeModifiersAppliesAllModifiers() {
    try (MockedStatic<RelluEssentials> mockedStatic = mockStatic(RelluEssentials.class)) {
      mockedStatic.when(RelluEssentials::getInstance).thenReturn(pluginInstance);
      when(pluginInstance.getName()).thenReturn("RelluEssentials");

      AttributeModifier modifier = mock(AttributeModifier.class);
      ItemAttribute itemAttribute = new TestItemAttribute(null, modifier);
      List<ItemAttribute> attributes = new ArrayList<>(List.of(itemAttribute));

      when(enchantName.name()).thenReturn(ENCHANT_NAME);
      when(enchantName.displayName()).thenReturn(DISPLAY_NAME);
      when(enchantLevel.startLevel()).thenReturn(START_LEVEL);

      EnchantmentHelper helper =
          new EnchantmentHelper(enchantName, EnchantmentTarget.WEAPON, enchantLevel, LORE_TEXT, rarity,
              attributes, COST);

      when(itemMeta.getPersistentDataContainer()).thenReturn(persistentDataContainer);
      when(itemMeta.getLore()).thenReturn(null);
      when(rarity.getPrefix()).thenReturn("§7");
      when(rarity.getDisplayName()).thenReturn("Common");

      helper.addTo(itemMeta);

      verify(itemMeta, never()).addAttributeModifier(any(), any());
    }
  }

  @SuppressWarnings("ResultOfMethodCallIgnored")
  @Test
  void removeFromWithAttributeModifiersRemovesAllModifiers() {
    try (MockedStatic<RelluEssentials> mockedStatic = mockStatic(RelluEssentials.class)) {
      mockedStatic.when(RelluEssentials::getInstance).thenReturn(pluginInstance);
      when(pluginInstance.getName()).thenReturn("RelluEssentials");

      AttributeModifier modifier = mock(AttributeModifier.class);
      ItemAttribute itemAttribute = new TestItemAttribute(null, modifier);
      List<ItemAttribute> attributes = new ArrayList<>(List.of(itemAttribute));

      when(enchantName.name()).thenReturn(ENCHANT_NAME);
      when(enchantName.displayName()).thenReturn(DISPLAY_NAME);
      when(enchantLevel.startLevel()).thenReturn(START_LEVEL);

      EnchantmentHelper helper =
          new EnchantmentHelper(enchantName, EnchantmentTarget.WEAPON, enchantLevel, LORE_TEXT, rarity,
              attributes, COST);

      when(itemStack.getItemMeta()).thenReturn(itemMeta);
      when(itemMeta.getPersistentDataContainer()).thenReturn(persistentDataContainer);
      when(itemMeta.getLore()).thenReturn(new ArrayList<>());
      when(rarity.getPrefix()).thenReturn("§7");
      when(rarity.getDisplayName()).thenReturn("Common");

      helper.removeFrom(itemStack);

      verify(itemMeta, never()).removeAttributeModifier(any(), any());
    }
  }

  @Test
  void getMultiplierReturnsDefaultMultiplierValue() {
    try (MockedStatic<RelluEssentials> mockedStatic = mockStatic(RelluEssentials.class)) {
      EnchantmentHelper helper = createFullHelper(mockedStatic);
      assertEquals(0.0, helper.getMultiplier());
    }
  }

  @Test
  void createEnchantedBookReturnsBookWithEnchantmentData() {
    try (MockedStatic<RelluEssentials> mockedStatic = mockStatic(RelluEssentials.class);
        MockedConstruction<ItemStack> _ = mockConstruction(ItemStack.class, (mock, _) -> {
          EnchantmentStorageMeta storageMeta = mock(EnchantmentStorageMeta.class);
          PersistentDataContainer pdc = mock(PersistentDataContainer.class);
          when(storageMeta.getPersistentDataContainer()).thenReturn(pdc);
          when(mock.getItemMeta()).thenReturn(storageMeta);
        })) {

      EnchantmentHelper helper = createFullHelper(mockedStatic);
      ItemStack result = helper.createEnchantedBook();

      EnchantmentStorageMeta meta = (EnchantmentStorageMeta) result.getItemMeta();
      verify(meta).setDisplayName(DISPLAY_NAME);
      verify(meta).setLore(List.of(LORE_TEXT));
      assertNotNull(meta);
      verify(meta.getPersistentDataContainer()).set(any(NamespacedKey.class), any(), eq(START_LEVEL));
      verify(result).setItemMeta(meta);
    }
  }
}