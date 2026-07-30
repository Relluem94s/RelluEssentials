package de.relluem94.minecraft.server.spigot.essentials.model.recipe;

import org.bukkit.Material;

import java.util.Map;

public record Shaped(String[] rows, Map<Character, Material> ingredients) {}