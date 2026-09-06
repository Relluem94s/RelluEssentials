package de.relluem94.minecraft.server.spigot.essentials.helpers;

import java.lang.reflect.Field;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.inventory.ItemFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class InventoryHelperTest {


  @BeforeEach
  protected void setUp() throws NoSuchFieldException, IllegalAccessException {
    Server server = Mockito.mock(Server.class);
    ItemFactory itemFactory = Mockito.mock(ItemFactory.class);

    Mockito.when(server.getItemFactory()).thenReturn(itemFactory);

    Field serverField = Bukkit.class.getDeclaredField("server");
    serverField.setAccessible(true);
    serverField.set(null, server);
  }

  @AfterEach
  protected void tearDown() {

  }

  @Test
  void checkForUtilityClass() {
    Assertions.assertThrows(IllegalStateException.class, InventoryHelper::new);
  }

  @Test
  public void testItemHelperAssignableCheck() throws Exception {
    Field dummyField = DummyNotItemHelper.class.getDeclaredField("dummyField");
    boolean result = ItemHelper.class.isAssignableFrom(dummyField.getType());
    Assertions.assertFalse(result);
  }

  @Test
  public void testGetNextSlot() {
    Assertions.assertEquals(10, InventoryHelper.getNextSlot(9));
  }

  @Test
  public void testGetSkipsSize() {
    Assertions.assertEquals(26, InventoryHelper.getSkipsSize());
  }

  @Test
  public void testInventorySize() {
    Assertions.assertEquals(9, InventoryHelper.inventorySize(5));
    Assertions.assertEquals(9, InventoryHelper.inventorySize(8));
    Assertions.assertEquals(9, InventoryHelper.inventorySize(9));
    Assertions.assertEquals(18, InventoryHelper.inventorySize(10));
    Assertions.assertEquals(18, InventoryHelper.inventorySize(16));
    Assertions.assertEquals(27, InventoryHelper.inventorySize(25));
    Assertions.assertEquals(27, InventoryHelper.inventorySize(27));
    Assertions.assertEquals(36, InventoryHelper.inventorySize(28));
  }

  public static class DummyNotItemHelper {

    public String dummyField = "test";
  }
}
