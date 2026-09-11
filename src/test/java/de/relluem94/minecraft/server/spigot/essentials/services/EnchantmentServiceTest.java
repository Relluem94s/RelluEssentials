package de.relluem94.minecraft.server.spigot.essentials.services;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.relluem94.minecraft.server.spigot.essentials.helpers.EnchantmentHelper;
import de.relluem94.minecraft.server.spigot.essentials.models.RelluEssentialsNamespacedKey;
import de.relluem94.minecraft.server.spigot.essentials.registries.EnchantmentRegistry;
import java.util.List;
import java.util.Optional;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class EnchantmentServiceTest {

  @Mock
  private EnchantmentRegistry enchantmentRegistry;

  @Mock
  private Plugin plugin;

  @Mock
  private EnchantmentHelper enchantmentHelper;

  @Mock
  private RelluEssentialsNamespacedKey namespacedKey;

  @Mock
  private ItemStack itemStack;

  private EnchantmentService enchantmentService;

  @BeforeEach
  void setUp() {
    enchantmentService = new EnchantmentService(enchantmentRegistry);
  }

  @Test
  void registerDelegatesToRegistry() {
    String key = "test:enchantment";

    enchantmentService.register(plugin, key, enchantmentHelper);

    verify(enchantmentRegistry).register(plugin, key, enchantmentHelper);
  }

  @Test
  void registerPropagatesExceptionFromRegistry() {
    String key = "test:enchantment";
    doThrow(new RuntimeException("registry error")).when(enchantmentRegistry).register(plugin, key, enchantmentHelper);

    assertThrows(RuntimeException.class, () -> enchantmentService.register(plugin, key, enchantmentHelper));
  }

  @Test
  void findReturnsEnchantmentFromRegistry() {
    when(enchantmentRegistry.find(namespacedKey)).thenReturn(Optional.of(enchantmentHelper));

    Optional<EnchantmentHelper> result = enchantmentService.find(namespacedKey);

     assertTrue(result.isPresent());
     assertEquals(enchantmentHelper, result.get());
  }

  @Test
  void findReturnsEmptyWhenRegistryReturnsEmpty() {
    when(enchantmentRegistry.find(namespacedKey)).thenReturn(Optional.empty());

    Optional<EnchantmentHelper> result = enchantmentService.find(namespacedKey);

    assertAll(
        () -> assertNotNull(result),
        () -> assertFalse(result.isPresent())
    );
  }

  @Test
  void findPropagatesExceptionFromRegistry() {
    when(enchantmentRegistry.find(namespacedKey)).thenThrow(new RuntimeException("find error"));

    assertThrows(RuntimeException.class, () -> enchantmentService.find(namespacedKey));
  }

  @Test
  void findAllReturnsAllEnchantmentsFromRegistry() {
    List<EnchantmentHelper> expectedList = List.of(enchantmentHelper);
    when(enchantmentRegistry.findAll()).thenReturn(expectedList);

    List<EnchantmentHelper> result = enchantmentService.findAll();

    assertAll(
        () -> assertNotNull(result),
        () -> assertEquals(1, result.size()),
        () -> assertEquals(enchantmentHelper, result.getFirst())
    );
  }

  @Test
  void findAllPropagatesExceptionFromRegistry() {
    when(enchantmentRegistry.findAll()).thenThrow(new RuntimeException("findAll error"));

    assertThrows(RuntimeException.class, () -> enchantmentService.findAll());
  }

  @Test
  void findByBookItemStackReturnsEnchantmentFromRegistry() {
    when(enchantmentRegistry.findByBookItemStack(itemStack)).thenReturn(Optional.of(enchantmentHelper));

    Optional<EnchantmentHelper> result = enchantmentService.findByBookItemStack(itemStack);

    assertTrue(result.isPresent());
    assertEquals(enchantmentHelper, result.get());
  }

  @Test
  void findByBookItemStackReturnsEmptyWhenRegistryReturnsEmpty() {
    when(enchantmentRegistry.findByBookItemStack(itemStack)).thenReturn(Optional.empty());

    Optional<EnchantmentHelper> result = enchantmentService.findByBookItemStack(itemStack);

    assertAll(
        () -> assertNotNull(result),
        () -> assertFalse(result.isPresent())
    );
  }

  @Test
  void findByBookItemStackPropagatesExceptionFromRegistry() {
    when(enchantmentRegistry.findByBookItemStack(itemStack)).thenThrow(new RuntimeException("book lookup error"));

    assertThrows(RuntimeException.class, () -> enchantmentService.findByBookItemStack(itemStack));
  }

  @Test
  void clearDelegatesToRegistry() {
    enchantmentService.clear();

    verify(enchantmentRegistry).clear();
  }

  @Test
  void clearPropagatesExceptionFromRegistry() {
    doThrow(new RuntimeException("clear error")).when(enchantmentRegistry).clear();

    assertThrows(RuntimeException.class, () -> enchantmentService.clear());
  }

  @Test
  void countReturnsValueFromRegistry() {
    when(enchantmentRegistry.count()).thenReturn(5);

    int result = enchantmentService.count();

    assertEquals(5, result);
  }

  @Test
  void countPropagatesExceptionFromRegistry() {
    when(enchantmentRegistry.count()).thenThrow(new RuntimeException("count error"));

    assertThrows(RuntimeException.class, () -> enchantmentService.count());
  }
}