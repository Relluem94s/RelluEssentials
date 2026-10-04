package de.relluem94.minecraft.server.spigot.essentials.models.pojo;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a configuration value of a specific setting for a given world group. Stores the boolean value as well as
 * audit information and foreign keys to the related {@link SettingEntry} and {@link WorldGroupEntry}.
 *
 * @author rellu
 */
@Data
@NoArgsConstructor
public class WorldGroupSettingEntry {

  private int id;
  private String created;
  private int createdBy;
  private String updated;
  private int updatedBy;
  private int settingEntryFk;
  private SettingEntry settingEntry;
  private int worldGroupEntryFk;
  private WorldGroupEntry worldGroupEntry;
  private boolean value;
}