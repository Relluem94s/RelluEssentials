package de.relluem94.minecraft.server.spigot.essentials.models.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a persisted player-specific setting entry including
 * audit metadata and a reference to the related setting definition.
 *
 * @author rellu
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SettingPlayerEntry {

  private int id;
  private String created;
  private int createdBy;
  private String updated;
  private Integer updatedBy;
  private String deleted;
  private Integer deletedBy;
  private int playerFk;
  private int settingFk;
  private SettingEntry settingEntry;
  private boolean value;
}