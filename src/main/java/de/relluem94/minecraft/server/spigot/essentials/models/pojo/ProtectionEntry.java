package de.relluem94.minecraft.server.spigot.essentials.models.pojo;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.json.JSONObject;

/**
 * Represents a protection configuration for a specific location in the world.
 *
 * <p>A protection entry contains metadata about its lifecycle (creation, update,
 * deletion), the associated location, the protected material, and JSON-based flag and rights configurations.</p>
 *
 * @author rellu
 */
@Data
@NoArgsConstructor
public class ProtectionEntry {

  private int id;
  private String created;
  private int createdBy;
  private String updated;
  private int updatedBy;
  private String deleted;
  private int deletedBy;
  private int locationFk;
  private LocationEntry locationEntry;
  private String materialName;
  private JSONObject flags;
  private JSONObject rights;
}