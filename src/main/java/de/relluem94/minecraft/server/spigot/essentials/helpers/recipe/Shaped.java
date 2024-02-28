package de.relluem94.minecraft.server.spigot.essentials.helpers.recipe;

import java.util.Map;

import org.bukkit.Material;

public class Shaped {
    private final String[] rows;

    private final Map<Character, Material> ingredients;

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
