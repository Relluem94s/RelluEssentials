package de.relluem94.minecraft.server.spigot.essentials.helpers.recipe;

import java.util.HashMap;

import org.bukkit.Material;

public class Shaped {
    private String[] rows;

    private HashMap<Character, Material> ingredients;

    public Shaped(String[] rows, HashMap<Character, Material> ingredients){
        this.ingredients = ingredients;

        this.rows = rows;
    }

    public String[] getRows(){
        return rows;
    }

    public HashMap<Character, Material> getIngredients() {
        return ingredients;
    }
}
