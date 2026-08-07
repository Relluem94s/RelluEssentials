package de.relluem94.minecraft.server.spigot.essentials.models.pojo;

import java.util.UUID;
import lombok.Data;
import org.json.JSONObject;

@Data
public class NpcEntry {

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