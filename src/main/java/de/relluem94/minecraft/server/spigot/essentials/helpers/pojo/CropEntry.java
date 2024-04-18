package de.relluem94.minecraft.server.spigot.essentials.helpers.pojo;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;

@Setter
@Getter
public class CropEntry {
    private int id;
    private Material plant;
    private Material seed;
}