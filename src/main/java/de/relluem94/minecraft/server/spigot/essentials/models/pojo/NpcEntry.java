package de.relluem94.minecraft.server.spigot.essentials.models.pojo;

import java.util.UUID;
import lombok.Data;
import org.json.JSONObject;

/**
 * Represents a persistent non-player character entry including identification,
 * profile information, inventory data, world location and creator metadata.
 *
 * @author rellu
 */
@SuppressWarnings("checkstyle:GoogleNonConstantFieldName")
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
  private float yaw;
  private float pitch;
  private int createdBy;
  private Integer updatedBy;
}