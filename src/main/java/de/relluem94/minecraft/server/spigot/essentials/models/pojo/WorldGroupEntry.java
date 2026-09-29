package de.relluem94.minecraft.server.spigot.essentials.models.pojo;

import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a logical group of worlds that share common configuration settings.
 * The entry contains audit metadata for creation, update, and deletion, as well
 * as a human-readable name and the associated settings entries.
 *
 * @author rellu
 */
@Data
@NoArgsConstructor
public class WorldGroupEntry {

  private int id;
  private String created;
  private int createdBy;
  private String updated;
  private int updatedBy;
  private String deleted;
  private int deletedBy;
  private String name;
  private List<WorldGroupSettingEntry> settings;
}