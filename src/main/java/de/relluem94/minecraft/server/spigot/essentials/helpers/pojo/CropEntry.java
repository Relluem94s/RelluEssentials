package de.relluem94.minecraft.server.spigot.essentials.helpers.pojo;

import org.bukkit.Material;

public class CropEntry {
    private int id;
    private Material plant;
    private Material seed;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Material getPlant() {
        return plant;
    }

    public void setPlant(Material plant) {
        this.plant = plant;
    }

    public void setPlant(String plant) {
        this.plant = Material.valueOf(plant);
    }

    public Material getSeed() {
        return seed;
    }

    public void setSeed(Material seed) {
        this.seed = seed;
    }   

    public void setSeed(String seed) {
        this.seed = Material.valueOf(seed);
    }
}
