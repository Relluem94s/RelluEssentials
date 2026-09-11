package de.relluem94.minecraft.server.spigot.essentials.constants;

/**
 * Holds constant values related to inventory configuration within the plugin.
 *
 * @author rellu
 */
public class InventoryConstants {

  private InventoryConstants() {
    throw new IllegalStateException(Constants.PLUGIN_INTERNAL_UTILITY_CLASS);
  }

  public static final String PLUGIN_INVENTORY_ADMIN_TOOLS = "admin_tools";

  /**
   * Fixed Value Bag Size defines the size of usable Slots in an Inventory.
   */
  public static final int BAG_SIZE = 28;
}
