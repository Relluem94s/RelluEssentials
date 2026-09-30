package de.relluem94.minecraft.server.spigot.essentials.models.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a permission or role group with an identifier, name, and chat prefix.
 *
 * @author rellu
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupEntry {

  private int id;
  private String name;
  private String prefix;
}