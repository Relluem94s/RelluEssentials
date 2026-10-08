package de.relluem94.minecraft.server.spigot.essentials.models.mobs;

import org.bukkit.inventory.ItemStack;

public record CustomMobEquipment(ItemStack mainHand, ItemStack offHand, ItemStack helmet, ItemStack chestplate,
                                 ItemStack leggings, ItemStack boots) {

}
