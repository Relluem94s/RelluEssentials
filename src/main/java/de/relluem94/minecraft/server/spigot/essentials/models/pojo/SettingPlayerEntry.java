package de.relluem94.minecraft.server.spigot.essentials.models.pojo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
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