package de.relluem94.minecraft.server.spigot.essentials.helpers.pojo;

import lombok.Getter;
import lombok.Setter;
import org.json.JSONObject;

/**
 *
 * @author rellu
 */

@Setter
@Getter
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