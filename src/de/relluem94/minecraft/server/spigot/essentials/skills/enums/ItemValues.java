package de.relluem94.minecraft.server.spigot.essentials.skills.enums;

import org.bukkit.Material;


public enum ItemValues {
	
	LEATHER_HELMET(Material.LEATHER, 5, 1) , LEATHER_CHESTPLATE(Material.LEATHER, 8, 1), LEATHER_LEGGINGS(Material.LEATHER, 7, 1), LEATHER_BOOTS(Material.LEATHER, 4, 1),
	IRON_HELMET(Material.IRON_INGOT, 5, 2), IRON_CHESTPLATE(Material.IRON_INGOT, 8, 2), IRON_LEGGINGS(Material.IRON_INGOT, 7, 2), IRON_BOOTS(Material.IRON_INGOT, 4, 2),
	GOLD_HELMET(Material.GOLD_INGOT, 5, 1), GOLD_CHESTPLATE(Material.GOLD_INGOT, 8, 1), GOLD_LEGGINGS(Material.GOLD_INGOT, 7, 1), GOLD_BOOTS(Material.GOLD_INGOT, 4, 1),
	DIAMOND_HELMET(Material.DIAMOND, 5, 3), DIAMOND_CHESTPLATE(Material.DIAMOND, 8, 3), DIAMOND_LEGGINGS(Material.DIAMOND, 7, 3), DIAMOND_BOOTS(Material.DIAMOND, 4, 3),
	
	DIAMOND_AXE(Material.DIAMOND, 3, 3), DIAMOND_PICKAXE(Material.DIAMOND, 3, 3), DIAMOND_HOE(Material.DIAMOND, 3, 3), DIAMOND_SHOVEL(Material.DIAMOND, 3, 3),
	IRON_AXE(Material.IRON_INGOT, 3, 2), IRON_PICKAXE(Material.IRON_INGOT, 3, 2), IRON_HOE(Material.IRON_INGOT, 3, 2), IRON_SHOVEL(Material.IRON_INGOT, 3, 2)
	;
	
	
	private int value;
	private int factor;
	private Material material;
	
	private ItemValues(Material material, int value, int factor) {
		this.value = value;
		this.material = material;
		this.factor = factor;
	}
	
	public Material getMaterial() {
		return material;
	}
	
	public int getValue() {
		return value;
	}
	
	public int getFactor() {
		return factor;
	}
	
	public static ItemValues getByMaterial(Material m) {
		try {
			 return ItemValues.valueOf(m.toString());
		}
		catch(Exception e) {
			return null;
		}
	}
}
