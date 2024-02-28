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

    public static final String FIELD_ID = "id";
    public static final String FIELD_CREATED = "created";
    public static final String FIELD_CREATEDBY = "createdby";
    public static final String FIELD_UPDATED = "updated";
    public static final String FIELD_UPDATEDBY = "updatedby";
    public static final String FIELD_DELETED = "deleted";
    public static final String FIELD_DELETEDBY = "deletedby";
    public static final String FIELD_PLAYER_FK = "player_fk";
    public static final String FIELD_HEALTH = "health";
    public static final String FIELD_TOTAL_EXPERIENCE = "totalExperience";
    public static final String FIELD_FOOD = "food";
    public static final String FIELD_INVENTORY = "inventory";

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