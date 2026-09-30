package de.relluem94.minecraft.server.spigot.essentials.models.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a skill configuration entry with its identifier, internal name,
 * display name and maximum achievable level.
 *
 * @author rellu
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SkillsEntry {

  private int id;
  private String name;
  private String displayName;
  private int maxLevel;
}