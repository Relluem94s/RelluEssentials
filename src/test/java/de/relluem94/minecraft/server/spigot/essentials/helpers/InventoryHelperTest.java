package de.relluem94.minecraft.server.spigot.essentials.helpers;

import de.relluem94.minecraft.server.spigot.essentials.utils.TestLogHandler;
import org.bukkit.Material;
import org.junit.jupiter.api.*;

import java.lang.reflect.Field;
import java.util.logging.Level;
import java.util.logging.Logger;

public class InventoryHelperTest {


    @BeforeEach
    protected void setUp() {

    }

    @AfterEach
    protected void tearDown() {

    }

    @Test
    void checkForUtilityClass(){
        Assertions.assertThrows(IllegalStateException.class, InventoryHelper::new);
    }

    public static class DummyNotItemHelper {
        public String dummyField = "test";
    }

    @Test
    public void testItemHelperAssignableCheck() throws Exception {
        Field dummyField = DummyNotItemHelper.class.getDeclaredField("dummyField");
        boolean result = ItemHelper.class.isAssignableFrom(dummyField.getType());
        Assertions.assertFalse(result);
    }

    public static class DummyItemHelper extends ItemHelper {
        public DummyItemHelper() {
            super(Material.STONE, 1, "dummy", Type.NONE, Rarity.COMMON);
        }
    }

    @Disabled
    @Test
    public void testIllegalAccessExceptionLogging() throws Exception {
        Logger logger = Logger.getLogger(de.relluem94.minecraft.server.spigot.essentials.helpers.InventoryHelper.class.getName());
        TestLogHandler handler = new TestLogHandler();
        logger.addHandler(handler);
        logger.setUseParentHandlers(false);

        Field field = String.class.getDeclaredField("value");
        field.setAccessible(false);

        ItemHelper dummy = new DummyItemHelper();
        try {
            @SuppressWarnings("unused")
            Object o = field.get(dummy);
        }
        catch (IllegalAccessException ex) {
            logger.log(Level.SEVERE, null, ex);
        }

        Assertions.assertFalse(handler.records.isEmpty());
        Assertions.assertTrue(handler.records.stream().anyMatch(r -> r.getLevel().equals(Level.SEVERE)));
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
}
