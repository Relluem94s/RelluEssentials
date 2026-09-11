package de.relluem94.minecraft.server.spigot.essentials.models.pojo;

import static de.relluem94.minecraft.server.spigot.essentials.constants.InventoryConstants.BAG_SIZE;

import lombok.Getter;
import lombok.Setter;

/**
 * Represents a single bag type configuration entry loaded from the plugin configuration.
 * Holds metadata such as display name, cost, and the names assigned to each inventory slot.
 *
 * @author rellu
 */
@Getter
@Setter
public class BagTypeEntry {

  private int id;
  private String displayName;
  private String name;
  private int cost;
  private String[] slotNames;

  /**
   * Constructs a new BagTypeEntry and initializes the slot names array with the default bag size.
   */
  public BagTypeEntry() {
    slotNames = new String[BAG_SIZE];
  }

  /**
   * Returns the display name assigned to the given inventory slot.
   *
   * @param slot the zero-based index of the inventory slot
   * @return the name assigned to the specified slot
   */
  public String getSlotName(int slot) {
    return slotNames[slot];
  }

  /**
   * Assigns a display name to the given inventory slot.
   *
   * @param slot the zero-based index of the inventory slot
   * @param name the name to assign to the specified slot
   */
  public void setSlotName(int slot, String name) {
    slotNames[slot] = name;
  }
}
