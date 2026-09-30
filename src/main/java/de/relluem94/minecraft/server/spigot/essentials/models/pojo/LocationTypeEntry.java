package de.relluem94.minecraft.server.spigot.essentials.models.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a location type entry with a unique identifier and a descriptive type name. This model is used to
 * categorize and reference different kinds of locations in the system.
 *
 * @author rellu
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LocationTypeEntry {

  private int id;
  private String type;
}