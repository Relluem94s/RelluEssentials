package de.relluem94.minecraft.server.spigot.essentials.helpers.pojo;

import lombok.Data;
import org.json.JSONObject;

import java.util.UUID;

@Data
public class NPCEntry {
    private int id;
    private UUID uuid;
    private UUID entityUuid;
    private String profileName;
    private JSONObject inventory;
    private String world;
    private double x;
    private double y;
    private double z;
    private int createdBy;
    private Integer updatedBy;
}