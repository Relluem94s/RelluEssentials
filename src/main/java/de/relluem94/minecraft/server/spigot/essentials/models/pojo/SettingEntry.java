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
public class SettingEntry {

  private int id;
  private LocalDateTime created;
  private int createdBy;
  private LocalDateTime updated;
  private Integer updatedBy;
  private String name;
}
