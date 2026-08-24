package de.relluem94.minecraft.server.spigot.essentials.models;

import de.relluem94.minecraft.server.spigot.essentials.models.items.CustomItem;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents a custom inventory structure containing specific items and configuration.
 *
 * @author Relluem94
 */
@Setter
@Getter
public class CustomInventory {

  private List<CustomItem> customItems;
  private CustomItem.Type type;
  private int size;
  private String titleGui;

  /**
   * Constructs a new CustomInventory with specified type, size, and title.
   *
   * @param type     The type of the inventory.
   * @param size     The number of slots.
   * @param titleGui The title of the GUI.
   */
  public CustomInventory(CustomItem.Type type, int size, String titleGui) {
    this.type = type;
    this.size = size;
    this.titleGui = titleGui;
  }
}