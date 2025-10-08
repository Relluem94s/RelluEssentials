package de.relluem94.minecraft.server.spigot.essentials.helpers.pojo;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.Material;

/**
 *
 * @author rellu
 */
@Setter
@Getter
public class ModifyHistoryEntry {
    private Location location;
    private Material material;
}