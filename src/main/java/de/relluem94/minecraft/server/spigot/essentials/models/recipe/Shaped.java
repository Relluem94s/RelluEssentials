package de.relluem94.minecraft.server.spigot.essentials.models.recipe;

import java.util.Map;
import org.bukkit.Material;

/**
 * Represents a shaped crafting recipe defined by row patterns and an ingredient mapping.
 */
public record Shaped(String[] rows, Map<Character, Material> ingredients) {}