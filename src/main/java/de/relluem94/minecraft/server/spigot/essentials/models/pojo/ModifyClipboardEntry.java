package de.relluem94.minecraft.server.spigot.essentials.models.pojo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;

/**
 * Represents a single modification entry for a clipboard operation.
 * Each entry describes which block should be placed at a specific location,
 * including the target material and its detailed block data.
 *
 * @author rellu
 */
@Getter
@AllArgsConstructor
public class ModifyClipboardEntry {

  private Location location;
  private Material material;
  private BlockData data;
}