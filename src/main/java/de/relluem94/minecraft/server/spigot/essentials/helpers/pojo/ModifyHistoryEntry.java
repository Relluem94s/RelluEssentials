package de.relluem94.minecraft.server.spigot.essentials.helpers.pojo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;

/**
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