package de.relluem94.minecraft.server.spigot.essentials.helpers.pojo;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;

@Setter
@Getter
public class DropEntry {
    private int id;
    private Material material;
    private int min;
    private int max;
}