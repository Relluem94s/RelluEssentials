package de.relluem94.minecraft.server.spigot.essentials.helpers.recipe;

import java.util.Map;

import org.bukkit.Material;

public class Shaped {
    private String[] rows;

    private Map<Character, Material> ingredients;

    public Shaped(String[] rows, Map<Character, Material> ingredients){
        this.ingredients = ingredients;

        this.rows = rows;
    }

    public String[] getRows(){
        return rows;
    }

    public Map<Character, Material> getIngredients() {
        return ingredients;
    }
}
