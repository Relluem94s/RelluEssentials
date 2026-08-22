package de.relluem94.minecraft.server.spigot.essentials.registries;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import de.relluem94.minecraft.server.spigot.essentials.models.RelluEssentialsNamespacedKey;
import de.relluem94.minecraft.server.spigot.essentials.models.items.CustomItem;
import de.relluem94.minecraft.server.spigot.essentials.registries.model.RegisteredInventory;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class InventoryRegistryTest {

  private InventoryRegistry inventoryRegistry;

  @BeforeEach
  void setUp() {
    inventoryRegistry = new InventoryRegistry();
  }

  @Test
  void register_ShouldStoreAndReturnInventory() {
    RelluEssentialsNamespacedKey key = new RelluEssentialsNamespacedKey("test", "inventory");
    String title = "Test Inventory";
    int size = 27;
    CustomItem.Type filter = CustomItem.Type.GADGET;

    RegisteredInventory registeredInventory = inventoryRegistry.register(key, title, size, filter);

    assertEquals(key, registeredInventory.getRelluEssentialsNamespacedKey());
    assertEquals(title, registeredInventory.getTitle());
    assertEquals(size, registeredInventory.getSize());
    assertEquals(filter, registeredInventory.getItemFilter());
  }

  @Test
  void register_ShouldThrowException_WhenKeyAlreadyExists() {
    RelluEssentialsNamespacedKey key = new RelluEssentialsNamespacedKey("test", "inventory");
    inventoryRegistry.register(key, "First", 9, CustomItem.Type.GADGET);

    assertThrows(IllegalArgumentException.class,
        () -> inventoryRegistry.register(key, "Second", 18, CustomItem.Type.GADGET));
  }

  @Test
  void find_ShouldReturnOptionalWithInventory_WhenKeyExists() {
    RelluEssentialsNamespacedKey key = new RelluEssentialsNamespacedKey("test", "inventory");
    inventoryRegistry.register(key, "Test", 9, CustomItem.Type.GADGET);

    Optional<RegisteredInventory> result = inventoryRegistry.find(key);

    assertTrue(result.isPresent());
    assertEquals(key, result.get().getRelluEssentialsNamespacedKey());
  }

  @Test
  void find_ShouldReturnEmptyOptional_WhenKeyDoesNotExist() {
    RelluEssentialsNamespacedKey key = new RelluEssentialsNamespacedKey("non", "existent");

    Optional<RegisteredInventory> result = inventoryRegistry.find(key);

    assertFalse(result.isPresent());
  }

  @Test
  void findAllByNamespace_ShouldReturnOnlyInventoriesInNamespace() {
    inventoryRegistry.register(new RelluEssentialsNamespacedKey("plugin", "inv1"), "Inv 1", 9,
        CustomItem.Type.GADGET);
    inventoryRegistry.register(new RelluEssentialsNamespacedKey("plugin", "inv2"), "Inv 2", 9,
        CustomItem.Type.GADGET);
    inventoryRegistry.register(new RelluEssentialsNamespacedKey("other", "inv3"), "Inv 3", 9,
        CustomItem.Type.GADGET);

    List<RegisteredInventory> pluginInventories = inventoryRegistry.findAllByNamespace("plugin");

    assertEquals(2, pluginInventories.size());
  }

  @Test
  void findAllByNamespace_ShouldReturnEmptyList_WhenNamespaceDoesNotExist() {
    inventoryRegistry.register(new RelluEssentialsNamespacedKey("plugin", "inv1"), "Inv 1", 9,
        CustomItem.Type.GADGET);

    List<RegisteredInventory> result = inventoryRegistry.findAllByNamespace("unknown");

    assertTrue(result.isEmpty());
  }
}