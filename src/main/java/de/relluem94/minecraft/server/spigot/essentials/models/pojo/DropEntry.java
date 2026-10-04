package de.relluem94.minecraft.server.spigot.essentials.models.pojo;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.bukkit.Material;

/**
 * Represents a single drop configuration entry with material and minimum and maximum amounts.
 *
 * @author rellu
 */
@Data
@NoArgsConstructor
public class DropEntry {

  private int id;
  private Material material;
  private int min;
  private int max;
}