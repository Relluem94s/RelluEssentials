package de.relluem94.minecraft.server.spigot.essentials.registries;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import de.relluem94.minecraft.server.spigot.essentials.helpers.ItemHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.ItemHelper.Type;
import de.relluem94.minecraft.server.spigot.essentials.models.RegistryKey;
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
    RegistryKey key = new RegistryKey("test", "inventory");
    String title = "Test Inventory";
    int size = 27;
    ItemHelper.Type filter = Type.GADGET;

    RegisteredInventory registeredInventory = inventoryRegistry.register(key, title, size, filter);

    assertEquals(key, registeredInventory.getRelluEssentialsNamespacedKey());
    assertEquals(title, registeredInventory.getTitle());
    assertEquals(size, registeredInventory.getSize());
    assertEquals(filter, registeredInventory.getItemFilter());
  }

  @Test
  void register_ShouldThrowException_WhenKeyAlreadyExists() {
    RegistryKey key = new RegistryKey("test", "inventory");
    inventoryRegistry.register(key, "First", 9, ItemHelper.Type.GADGET);

    assertThrows(IllegalArgumentException.class, () ->
        inventoryRegistry.register(key, "Second", 18, ItemHelper.Type.GADGET)
    );
  }

  @Test
  void find_ShouldReturnOptionalWithInventory_WhenKeyExists() {
    RegistryKey key = new RegistryKey("test", "inventory");
    inventoryRegistry.register(key, "Test", 9, ItemHelper.Type.GADGET);

    Optional<RegisteredInventory> result = inventoryRegistry.find(key);

    assertTrue(result.isPresent());
    assertEquals(key, result.get().getRelluEssentialsNamespacedKey());
  }

  @Test
  void find_ShouldReturnEmptyOptional_WhenKeyDoesNotExist() {
    RegistryKey key = new RegistryKey("non", "existent");

    Optional<RegisteredInventory> result = inventoryRegistry.find(key);

    assertFalse(result.isPresent());
  }

  @Test
  void findAllByNamespace_ShouldReturnOnlyInventoriesInNamespace() {
    inventoryRegistry.register(new RegistryKey("plugin", "inv1"), "Inv 1", 9,
        ItemHelper.Type.GADGET);
    inventoryRegistry.register(new RegistryKey("plugin", "inv2"), "Inv 2", 9,
        ItemHelper.Type.GADGET);
    inventoryRegistry.register(new RegistryKey("other", "inv3"), "Inv 3", 9,
        ItemHelper.Type.GADGET);

    List<RegisteredInventory> pluginInventories = inventoryRegistry.findAllByNamespace("plugin");

    assertEquals(2, pluginInventories.size());
  }

  @Test
  void findAllByNamespace_ShouldReturnEmptyList_WhenNamespaceDoesNotExist() {
    inventoryRegistry.register(new RegistryKey("plugin", "inv1"), "Inv 1", 9,
        ItemHelper.Type.GADGET);

    List<RegisteredInventory> result = inventoryRegistry.findAllByNamespace("unknown");

    assertTrue(result.isEmpty());
  }
}