package de.relluem94.minecraft.server.spigot.essentials.models.pojo;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a persisted world entry.
 *
 * <p>This entry contains audit information such as creation, update, and
 * deletion timestamps with user identifiers, as well as grouping metadata.</p>
 *
 * @author rellu
 */
@Data
@NoArgsConstructor
public class WorldEntry {

  private int id;
  private String created;
  private int createdBy;
  private String updated;
  private int updatedBy;
  private String deleted;
  private int deletedBy;
  private String name;
  private WorldGroupEntry worldGroupEntry;
  private GroupEntry groupEntry;
}