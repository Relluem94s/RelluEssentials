package de.relluem94.minecraft.server.spigot.essentials.models.pojo;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a single configuration setting entry with audit information.
 *
 * @author rellu
 */
@Data
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
