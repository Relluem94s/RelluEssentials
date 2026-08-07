package de.relluem94.minecraft.server.spigot.essentials.models.pojo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;

/**
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