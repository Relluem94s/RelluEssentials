package de.relluem94.minecraft.server.spigot.essentials.models.mobs;

import org.bukkit.inventory.ItemStack;

/**
 * Represents the equipment configuration for a custom mob.
 *
 * <p>Each slot corresponds to a specific equipment position on the mob.
 * A slot may be {@code null} if no item is assigned to that position.</p>
 *
 * @param mainHand   the item held in the mainHand
 * @param offHand    the item held in the offHand
 * @param helmet     the item worn as helmet
 * @param chestplate the item worn as chestplate
 * @param leggings   the item worn as leggings
 * @param boots      the item worn as boots
 *
 * @author rellu
 */
public record CustomMobEquipment(ItemStack mainHand, ItemStack offHand, ItemStack helmet, ItemStack chestplate,
                                 ItemStack leggings, ItemStack boots) {

}
