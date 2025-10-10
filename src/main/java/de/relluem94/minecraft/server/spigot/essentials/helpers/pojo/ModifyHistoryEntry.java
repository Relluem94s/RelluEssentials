package de.relluem94.minecraft.server.spigot.essentials.helpers.pojo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.Material;

/**
 *
 * @author rellu
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ModifyHistoryEntry {
    private Location location;
    private Material material;
}