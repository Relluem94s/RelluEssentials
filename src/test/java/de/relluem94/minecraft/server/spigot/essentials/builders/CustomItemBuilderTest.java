package de.relluem94.minecraft.server.spigot.essentials.builders;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import de.relluem94.minecraft.server.spigot.essentials.models.RelluEssentialsNamespacedKey;
import de.relluem94.minecraft.server.spigot.essentials.models.items.CustomItem;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.inventory.meta.ItemMeta;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CustomItemBuilderTest {

  @Mock
  private RelluEssentialsNamespacedKey relluEssentialsNamespacedKey;

  @Mock
  private Server server;

  private CustomItemBuilder builder;

  @BeforeEach
  void setUp() {
    builder = new CustomItemBuilder(relluEssentialsNamespacedKey, Material.DIAMOND);
  }

  @Test
  void buildReturnsCustomItemWithDefaultValues() {
    CustomItem result = builder.build();

    assertAll(() -> assertEquals(Material.DIAMOND, result.material()),
        () -> assertEquals(1, result.amount()), () -> assertEquals("", result.displayName()),
        () -> assertTrue(result.lore().isEmpty()),
        () -> assertEquals(CustomItem.Type.NONE, result.type()),
        () -> assertEquals(CustomItem.Rarity.NONE, result.rarity()),
        () -> assertNull(result.cost()), () -> assertTrue(result.enchantments().isEmpty()),
        () -> assertTrue(result.persistentData().isEmpty()),
        () -> assertTrue(result.metaModifiers().isEmpty()),
        () -> assertEquals(relluEssentialsNamespacedKey, result.relluEssentialsNamespacedKey()));
  }

  @Test
  void buildReturnsCustomItemWithAllFieldsSet() {
    List<String> lore = List.of("Line 1", "Line 2");
    List<CustomItem.EnchantmentData> enchantments = List.of(
        new CustomItem.EnchantmentData("minecraft:sharpness", 5));
    Map<String, Object> persistentData = Map.of("test:key", "value");
    Consumer<ItemMeta> modifier = _ -> {
    };

    CustomItem result = builder.amount(3).displayName("Test Item").lore(lore)
        .type(CustomItem.Type.WEAPON).rarity(CustomItem.Rarity.LEGENDARY).cost(100)
        .enchantments(enchantments, server).persistentData(persistentData).metaModifier(modifier)
        .build();

    assertAll(() -> assertEquals(Material.DIAMOND, result.material()),
        () -> assertEquals(3, result.amount()),
        () -> assertEquals("Test Item", result.displayName()),
        () -> assertEquals(List.of("Line 1", "Line 2"), result.lore()),
        () -> assertEquals(CustomItem.Type.WEAPON, result.type()),
        () -> assertEquals(CustomItem.Rarity.LEGENDARY, result.rarity()),
        () -> assertEquals(100, result.cost()), () -> assertEquals(1, result.enchantments().size()),
        () -> assertEquals("minecraft:sharpness", result.enchantments().getFirst().key()),
        () -> assertEquals(5, result.enchantments().getFirst().level()),
        () -> assertEquals(1, result.persistentData().size()),
        () -> assertEquals("value", result.persistentData().get("test:key")),
        () -> assertEquals(1, result.metaModifiers().size()),
        () -> assertEquals(relluEssentialsNamespacedKey, result.relluEssentialsNamespacedKey()));
  }

  @Test
  void amountSetsAmountCorrectly() {
    CustomItem result = builder.amount(64).build();
    assertEquals(64, result.amount());
  }

  @Test
  void displayNameSetsDisplayNameCorrectly() {
    CustomItem result = builder.displayName("My Custom Item").build();
    assertEquals("My Custom Item", result.displayName());
  }

  @Test
  void loreSetsLoreCorrectly() {
    List<String> lore = List.of("First", "Second", "Third");
    CustomItem result = builder.lore(lore).build();
    assertEquals(lore, result.lore());
  }

  @Test
  void loreInputMutationDoesNotAffectBuiltItem() {
    List<String> mutableLore = new java.util.ArrayList<>(List.of("Original"));
    builder.lore(mutableLore);
    mutableLore.add("Added After");
    CustomItem result = builder.build();
    assertEquals(1, result.lore().size());
  }

  @Test
  void typeSetsTypeCorrectly() {
    CustomItem result = builder.type(CustomItem.Type.ARMOR).build();
    assertEquals(CustomItem.Type.ARMOR, result.type());
  }

  @Test
  void raritySetsRarityCorrectly() {
    CustomItem result = builder.rarity(CustomItem.Rarity.EPIC).build();
    assertEquals(CustomItem.Rarity.EPIC, result.rarity());
  }

  @Test
  void costSetsCostCorrectly() {
    CustomItem result = builder.cost(250).build();
    assertEquals(250, result.cost());
  }

  @Test
  void enchantmentsAddsAllEnchantments() {
    List<CustomItem.EnchantmentData> enchantments = List.of(
        new CustomItem.EnchantmentData("minecraft:sharpness", 3),
        new CustomItem.EnchantmentData("minecraft:unbreaking", 2));
    CustomItem result = builder.enchantments(enchantments, server).build();

    assertAll(() -> assertEquals(2, result.enchantments().size()),
        () -> assertEquals("minecraft:sharpness", result.enchantments().getFirst().key()),
        () -> assertEquals(3, result.enchantments().getFirst().level()),
        () -> assertEquals("minecraft:unbreaking", result.enchantments().get(1).key()),
        () -> assertEquals(2, result.enchantments().get(1).level()));
  }

  @Test
  void enchantmentsAccumulatesAcrossMultipleCalls() {
    builder.enchantments(List.of(new CustomItem.EnchantmentData("minecraft:sharpness", 1)), server);
    builder.enchantments(List.of(new CustomItem.EnchantmentData("minecraft:unbreaking", 1)),
        server);
    CustomItem result = builder.build();
    assertEquals(2, result.enchantments().size());
  }

  @Test
  void persistentDataAddsAllEntries() {
    Map<String, Object> data = Map.of("plugin:flag", true, "plugin:count", 42);
    CustomItem result = builder.persistentData(data).build();

    assertAll(() -> assertEquals(2, result.persistentData().size()),
        () -> assertEquals(true, result.persistentData().get("plugin:flag")),
        () -> assertEquals(42, result.persistentData().get("plugin:count")));
  }

  @Test
  void persistentDataAccumulatesAcrossMultipleCalls() {
    builder.persistentData(Map.of("plugin:first", "one"));
    builder.persistentData(Map.of("plugin:second", "two"));
    CustomItem result = builder.build();
    assertEquals(2, result.persistentData().size());
  }

  @Test
  void addPersistentDataAddsSingleEntry() {
    CustomItem result = builder.addPersistentData("plugin:key", "singleValue").build();

    assertAll(() -> assertEquals(1, result.persistentData().size()),
        () -> assertEquals("singleValue", result.persistentData().get("plugin:key")));
  }

  @Test
  void addPersistentDataOverwritesExistingKey() {
    builder.addPersistentData("plugin:key", "original");
    builder.addPersistentData("plugin:key", "overwritten");
    CustomItem result = builder.build();

    assertAll(() -> assertEquals(1, result.persistentData().size()),
        () -> assertEquals("overwritten", result.persistentData().get("plugin:key")));
  }

  @Test
  void metaModifierAddsModifierToList() {
    AtomicBoolean wasInvoked = new AtomicBoolean(false);
    Consumer<ItemMeta> modifier = _ -> wasInvoked.set(true);

    CustomItem result = builder.metaModifier(modifier).build();

    assertEquals(1, result.metaModifiers().size());
    result.metaModifiers().getFirst().accept(null);
    assertTrue(wasInvoked.get());
  }

  @Test
  void metaModifierAccumulatesAcrossMultipleCalls() {
    builder.metaModifier(_ -> {
    });
    builder.metaModifier(_ -> {
    });
    builder.metaModifier(_ -> {
    });
    CustomItem result = builder.build();
    assertEquals(3, result.metaModifiers().size());
  }

  @Test
  void buildReturnedLoreIsUnmodifiable() {
    CustomItem result = builder.lore(List.of("Line")).build();
    assertThrows(UnsupportedOperationException.class, () -> result.lore().add("New Line"));
  }

  @Test
  void buildReturnedEnchantmentsIsUnmodifiable() {
    builder.enchantments(List.of(new CustomItem.EnchantmentData("minecraft:sharpness", 1)), server);
    CustomItem result = builder.build();
    assertThrows(UnsupportedOperationException.class,
        () -> result.enchantments().add(new CustomItem.EnchantmentData("minecraft:unbreaking", 1)));
  }

  @Test
  void buildReturnedPersistentDataIsUnmodifiable() {
    builder.addPersistentData("plugin:key", "value");
    CustomItem result = builder.build();
    assertThrows(UnsupportedOperationException.class,
        () -> result.persistentData().put("plugin:new", "extra"));
  }

  @Test
  void buildReturnedMetaModifiersIsUnmodifiable() {
    builder.metaModifier(_ -> {
    });
    CustomItem result = builder.build();
    assertThrows(UnsupportedOperationException.class, () -> result.metaModifiers().add(_ -> {
    }));
  }

  @Test
  void buildPreservesNamespacedKeyReference() {
    CustomItem result = builder.build();
    assertSame(relluEssentialsNamespacedKey, result.relluEssentialsNamespacedKey());
  }

  @Test
  void buildPreservesMaterialReference() {
    CustomItem result = new CustomItemBuilder(relluEssentialsNamespacedKey,
        Material.GOLD_INGOT).build();
    assertEquals(Material.GOLD_INGOT, result.material());
  }

  @Test
  void builderMethodsReturnSameBuilderInstance() {
    Consumer<ItemMeta> modifier = _ -> {
    };
    assertAll(() -> assertSame(builder, builder.amount(1)),
        () -> assertSame(builder, builder.displayName("name")),
        () -> assertSame(builder, builder.lore(List.of())),
        () -> assertSame(builder, builder.type(CustomItem.Type.NONE)),
        () -> assertSame(builder, builder.rarity(CustomItem.Rarity.NONE)),
        () -> assertSame(builder, builder.cost(0)),
        () -> assertSame(builder, builder.enchantments(List.of(), server)),
        () -> assertSame(builder, builder.persistentData(Map.of())),
        () -> assertSame(builder, builder.addPersistentData("k", "v")),
        () -> assertSame(builder, builder.metaModifier(modifier)));
  }
}