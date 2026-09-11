package de.relluem94.minecraft.server.spigot.essentials.models.pojo;

import de.relluem94.minecraft.server.spigot.essentials.helpers.InventoryHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.NpcHelper;
import de.relluem94.minecraft.server.spigot.essentials.npcs.trader.TraderNpc.Type;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Villager.Profession;

/**
 * Represents a Trader NPC configuration entry retrieved from the data source.
 * Holds all metadata and configuration properties required to identify, display,
 * and manage a Trader NPC, including its name, profession, type, and slot assignments.
 *
 * @author rellu
 */
@Setter
@Getter
public class TraderNpcEntry {

  private int id;
  private String created;
  private int createdBy;
  private String updated;
  private int updatedBy;
  private String deleted;
  private int deletedBy;
  private String name;
  private Profession profession;
  private String[] slotNames;
  private Type type;

  /**
   * Constructs a new {@code TraderNPCEntry} and initializes the slot names array
   * with a size derived from the NPC inventory size minus the number of skipped slots.
   */
  public TraderNpcEntry() {
    slotNames = new String[NpcHelper.INV_SIZE - InventoryHelper.getSkipsSize()];
  }

  /**
   * Returns the display name assigned to the given slot index.
   *
   * @param slot the index of the slot whose name should be retrieved
   * @return the name assigned to the specified slot, or {@code null} if none is set
   */
  public String getSlotName(int slot) {
    return slotNames[slot];
  }

  /**
   * Assigns a display name to the given slot index.
   *
   * @param slot the index of the slot to assign the name to
   * @param name the display name to assign to the specified slot
   */
  public void setSlotName(int slot, String name) {
    slotNames[slot] = name;
  }
}