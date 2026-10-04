package de.relluem94.minecraft.server.spigot.essentials.models.pojo;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.json.JSONObject;

/**
 * Represents a persisted snapshot of a player's inventory and player state
 * within a specific world group.
 *
 * <p>Contains metadata for creation, update and deletion, a reference to the
 * associated world group, and the player's health, food level and experience
 * at the time of the snapshot.</p>
 *
 * @author rellu
 */
@Data
@NoArgsConstructor
public class WorldGroupInventoryEntry {

  private int id;
  private String created;
  private int createdBy;
  private String updated;
  private int updatedBy;
  private String deleted;
  private int deletedBy;
  private WorldGroupEntry worldGroupEntry;
  private int playerId;
  private JSONObject inventory;
  private double health;
  private int foodLevel;
  private int totalExperience;
}