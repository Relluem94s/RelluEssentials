package de.relluem94.minecraft.server.spigot.essentials.helpers.recipe;

import java.util.Map;
import org.bukkit.Material;

public record Shaped(String[] rows, Map<Character, Material> ingredients) {}