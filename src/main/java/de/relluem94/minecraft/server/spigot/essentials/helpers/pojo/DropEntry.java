package de.relluem94.minecraft.server.spigot.essentials.helpers.pojo;

import org.bukkit.Material;

public class DropEntry {
    private int id;
    private Material material;
    private int min;
    private int max;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public void setMaterial(String material) {
        this.material = Material.valueOf(material);
    }


    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }
}
