package de.relluem94.minecraft.server.spigot.essentials.models.pojo;

import static de.relluem94.minecraft.server.spigot.essentials.constants.InventoryConstants.BAG_SIZE;

import lombok.Getter;
import lombok.Setter;

/**
 * Represents a bag entry stored in the database, holding metadata, ownership information,
 * and the slot values of a player's bag.
 *
 * @author rellu
 */

@Setter
@Getter
public class BagEntry {

  private int id;
  private String created;
  private int createdBy;
  private String updated;
  private int updatedBy;
  private String deleted;
  private int deletedBy;
  private int playerId;
  private int bagTypeId;
  private int[] slotValues;
  private BagTypeEntry bagType;
  private boolean hasToBeUpdated = false;

  /**
   * Creates a new BagEntry and initializes the slot values array with the default bag size.
   *
   * @author rellu
   */
  public BagEntry() {
    slotValues = new int[BAG_SIZE];
  }

  /**
   * Sets the value of a specific slot in the bag.
   *
   * @param slot  the index of the slot to update
   * @param value the value to assign to the slot
   */
  public void setSlotValue(int slot, int value) {
    this.slotValues[slot] = value;
  }

  /**
   * Returns the value of a specific slot in the bag.
   *
   * @param slot the index of the slot to retrieve
   * @return the value stored at the given slot index
   */
  public int getSlotValue(int slot) {
    return slotValues[slot];
  }
}