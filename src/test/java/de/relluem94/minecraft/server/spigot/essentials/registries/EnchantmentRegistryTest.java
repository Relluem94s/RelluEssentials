package de.relluem94.minecraft.server.spigot.essentials.registries;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import de.relluem94.minecraft.server.spigot.essentials.helpers.EnchantmentHelper;
import de.relluem94.minecraft.server.spigot.essentials.models.RelluEssentialsNamespacedKey;
import java.util.List;
import java.util.Optional;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class EnchantmentRegistryTest {

  @Mock
  private Plugin mockPlugin;

  @Mock
  private EnchantmentHelper mockEnchantment;

  @Mock
  private ItemStack mockItemStack;

  @Mock
  private EnchantmentStorageMeta mockMeta;

  @Mock
  private PersistentDataContainer mockPdc;

  private EnchantmentRegistry registry;

  @BeforeEach
  void setUp() {
    registry = new EnchantmentRegistry();
  }

  @Test
  void testRegisterAndFind() {
    when(mockPlugin.getName()).thenReturn("test_plugin");
    String key = "test_enchant";
    RelluEssentialsNamespacedKey registryKey = new RelluEssentialsNamespacedKey("test_plugin", key);

    registry.register(mockPlugin, key, mockEnchantment);

    Optional<EnchantmentHelper> found = registry.find(registryKey);

    assertTrue(found.isPresent());
    assertEquals(mockEnchantment, found.get());
  }

  @Test
  void testFindNonExistent() {
    RelluEssentialsNamespacedKey randomKey = new RelluEssentialsNamespacedKey("namespace", "unknown");
    Optional<EnchantmentHelper> found = registry.find(randomKey);

    assertFalse(found.isPresent());
  }

  @Test
  void testFindAll() {
    when(mockPlugin.getName()).thenReturn("test_plugin");
    String key1 = "enchant_1";
    String key2 = "enchant_2";
    EnchantmentHelper enchant1 = mock(EnchantmentHelper.class);
    EnchantmentHelper enchant2 = mock(EnchantmentHelper.class);

    registry.register(mockPlugin, key1, enchant1);
    registry.register(mockPlugin, key2, enchant2);

    List<EnchantmentHelper> all = registry.findAll();

    assertTrue(all.contains(enchant1));
    assertTrue(all.contains(enchant2));
    assertEquals(2, all.size());
  }

  @Test
  void testCount() {
    when(mockPlugin.getName()).thenReturn("test_plugin");
    registry.register(mockPlugin, "count_test", mockEnchantment);

    assertEquals(1, registry.count());
  }

  @Test
  void testFindByBookItemStack_Success() {
    when(mockPlugin.getName()).thenReturn("test_plugin");
    NamespacedKey enchantmentKey = new NamespacedKey("test", "magic");
    when(mockEnchantment.getKey()).thenReturn(enchantmentKey);

    registry.register(mockPlugin, "magic", mockEnchantment);

    when(mockItemStack.getItemMeta()).thenReturn(mockMeta);
    when(mockMeta.getPersistentDataContainer()).thenReturn(mockPdc);

    when(mockPdc.has(enchantmentKey, PersistentDataType.INTEGER)).thenReturn(true);
    when(mockMeta.getStoredEnchants()).thenReturn(java.util.Collections.emptyMap());

    Optional<EnchantmentHelper> result = registry.findByBookItemStack(mockItemStack);

    assertTrue(result.isPresent());
    assertEquals(mockEnchantment, result.get());
  }

  @Test
  void testFindByBookItemStack_NotABook() {
    when(mockItemStack.getItemMeta()).thenReturn(mock(org.bukkit.inventory.meta.ItemMeta.class));
    Optional<EnchantmentHelper> result = registry.findByBookItemStack(mockItemStack);
    assertFalse(result.isPresent());
  }

  @Test
  void testFindByBookItemStack_NoDataInPdc() {
    when(mockPlugin.getName()).thenReturn("test_plugin");
    when(mockEnchantment.getKey()).thenReturn(new NamespacedKey("test", "magic"));
    registry.register(mockPlugin, "magic", mockEnchantment);

    when(mockItemStack.getItemMeta()).thenReturn(mockMeta);
    when(mockMeta.getPersistentDataContainer()).thenReturn(mockPdc);
    when(mockPdc.has(any(NamespacedKey.class), eq(PersistentDataType.INTEGER))).thenReturn(false);

    Optional<EnchantmentHelper> result = registry.findByBookItemStack(mockItemStack);

    assertFalse(result.isPresent());
  }
}