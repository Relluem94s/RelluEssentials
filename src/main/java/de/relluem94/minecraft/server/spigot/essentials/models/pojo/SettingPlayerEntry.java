package de.relluem94.minecraft.server.spigot.essentials.models.pojo;

import java.time.LocalDateTime;
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
  private LocalDateTime created;
  private int createdBy;
  private LocalDateTime updated;
  private Integer updatedBy;
  private LocalDateTime deleted;
  private Integer deletedBy;
  private int playerFk;
  private int settingFk;
  private String value;
}