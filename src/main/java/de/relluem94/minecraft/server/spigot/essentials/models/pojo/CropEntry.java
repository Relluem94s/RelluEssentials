package de.relluem94.minecraft.server.spigot.essentials.models.pojo;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.bukkit.Material;

/**
 * Represents a crop configuration with a unique identifier, the plant material, and its seed material.
 *
 * <p>This entry is used to link a specific plant type to the corresponding seed type in the plugin
 * configuration or runtime logic.</p>
 *
 * @author rellu
 */
@Data
@NoArgsConstructor
public class CropEntry {

  private int id;
  private Material plant;
  private Material seed;
}