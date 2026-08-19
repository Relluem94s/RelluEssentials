package de.relluem94.minecraft.server.spigot.essentials.registries;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.helpers.ItemHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.ItemHelper.Type;
import de.relluem94.minecraft.server.spigot.essentials.models.RegistryKey;
import java.util.Optional;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.Plugin;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

class ItemRegistryTest {

  @Mock
  private Plugin mockPlugin;

  @Mock
  private ItemHelper mockItemHelper;

  @Mock
  private RelluEssentials mockRelluEssentials;

  @Mock
  private RegistryKey mockRegistryKey;

  private ItemRegistry itemRegistry;

  private MockedStatic<RegistryKey> mockedRegistryKeyStatic;
  private MockedStatic<RelluEssentials> mockedRelluEssentialsStatic;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    when(mockPlugin.getName()).thenReturn("TestPlugin");
    when(mockRelluEssentials.getName()).thenReturn("TestPlugin");

    mockedRelluEssentialsStatic = Mockito.mockStatic(RelluEssentials.class);
    mockedRelluEssentialsStatic.when(RelluEssentials::getInstance).thenReturn(mockRelluEssentials);

    mockedRegistryKeyStatic = Mockito.mockStatic(RegistryKey.class);
    mockedRegistryKeyStatic.when(() -> RegistryKey.initializeInternalPlugin(any(Plugin.class)))
        .thenAnswer(invocation -> null);

    itemRegistry = new ItemRegistry(mockPlugin);

    when(mockRegistryKey.toString()).thenReturn("test_key");
  }

  @AfterEach
  void tearDown() {
    if (mockedRegistryKeyStatic != null) {
      mockedRegistryKeyStatic.close();
    }
    if (mockedRelluEssentialsStatic != null) {
      mockedRelluEssentialsStatic.close();
    }
  }


  @Test
  void register_ShouldAddItemsToMap() {
    itemRegistry.register(mockRegistryKey, mockItemHelper);

    Optional<ItemHelper> found = itemRegistry.findByIdentifier("test_key");
    assertTrue(found.isPresent());
    assertEquals(mockItemHelper, found.get());

    verify(mockItemHelper).setData(any(NamespacedKey.class), eq("test_key"));
  }

  @Test
  void register_ShouldThrowException_WhenKeyAlreadyExists() {
    itemRegistry.register(mockRegistryKey, mockItemHelper);

    assertThrows(IllegalArgumentException.class, () -> {
      itemRegistry.register(mockRegistryKey, mockItemHelper);
    });
  }

  @Test
  void find_ShouldReturnEmpty_WhenKeyDoesNotExist() {
    RegistryKey unknownKey = mock(RegistryKey.class);
    when(unknownKey.toString()).thenReturn("unknown");

    Optional<ItemHelper> result = itemRegistry.find(unknownKey);

    assertTrue(result.isEmpty());
  }

  @Test
  void findByIdentifier_ShouldReturnCorrectItem() {
    itemRegistry.register(mockRegistryKey, mockItemHelper);

    Optional<ItemHelper> result = itemRegistry.findByIdentifier("test_key");

    assertTrue(result.isPresent());
    assertEquals(mockItemHelper, result.get());
  }

  @Test
  void getAll_ShouldReturnAllRegisteredItems() {
    itemRegistry.register(mockRegistryKey, mockItemHelper);

    assertEquals(1, itemRegistry.getAll().size());
    assertTrue(itemRegistry.getAll().containsKey("test_key"));
  }

  @Test
  void getAllByType_ShouldFilterItemsCorrectly() {
    ItemHelper typeAItem = mock(ItemHelper.class);
    ItemHelper typeBItem = mock(ItemHelper.class);
    RegistryKey keyA = mock(RegistryKey.class);
    RegistryKey keyB = mock(RegistryKey.class);

    when(keyA.toString()).thenReturn("key_a");
    when(keyB.toString()).thenReturn("key_b");
    when(typeAItem.getItemType()).thenReturn(Type.GADGET);
    when(typeBItem.getItemType()).thenReturn(ItemHelper.Type.TOOL);

    itemRegistry.register(keyA, typeAItem);
    itemRegistry.register(keyB, typeBItem);

    var results = itemRegistry.getAllByType(ItemHelper.Type.GADGET);

    assertEquals(1, results.size());
    assertEquals(typeAItem, results.get(0));
  }

  @Test
  void findByItemStack_ShouldReturnItem_WhenMatchFound() {
    org.bukkit.inventory.ItemStack mockItemStack = mock(org.bukkit.inventory.ItemStack.class);
    itemRegistry.register(mockRegistryKey, mockItemHelper);

    when(mockItemHelper.almostEquals(mockItemStack)).thenReturn(true);

    Optional<ItemHelper> result = itemRegistry.findByItemStack(mockItemStack);

    assertTrue(result.isPresent());
    assertEquals(mockItemHelper, result.get());
  }

  @Test
  void findByItemStack_ShouldReturnEmpty_WhenNoMatchFound() {
    org.bukkit.inventory.ItemStack mockItemStack = mock(org.bukkit.inventory.ItemStack.class);
    itemRegistry.register(mockRegistryKey, mockItemHelper);

    when(mockItemHelper.almostEquals(mockItemStack)).thenReturn(false);

    Optional<ItemHelper> result = itemRegistry.findByItemStack(mockItemStack);

    assertTrue(result.isEmpty());
  }
}