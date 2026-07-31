package de.relluem94.minecraft.server.spigot.essentials.model.pojo;

import de.relluem94.minecraft.server.spigot.essentials.helpers.InventoryHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.NpcHelper;
import de.relluem94.minecraft.server.spigot.essentials.npc.trader.TraderNpc.Type;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Villager.Profession;

/**
 *
 * @author rellu
 */
@Setter
@Getter
public class TraderNPCEntry {

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

  public TraderNPCEntry() {
    slotNames = new String[NpcHelper.INV_SIZE - InventoryHelper.getSkipsSize()];
  }

  public String getSlotName(int slot) {
    return slotNames[slot];
  }

  public void setSlotName(int slot, String name) {
    slotNames[slot] = name;
  }
}