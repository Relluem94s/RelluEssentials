package de.relluem94.minecraft.server.spigot.essentials.models.pojo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;

/**
 * Represents a single block modification in the world, including its location,
 * material, and detailed block data. Instances of this class can be used to
 * record, track, and restore changes for history, rollback, or undo operations.
 *
 * @author rellu
 */
@Getter
@AllArgsConstructor
public class ModifyHistoryEntry {

  private Location location;
  private Material material;
  private BlockData data;
}