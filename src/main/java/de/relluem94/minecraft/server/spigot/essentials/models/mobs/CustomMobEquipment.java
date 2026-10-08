package de.relluem94.minecraft.server.spigot.essentials.models.mobs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.inventory.ItemStack;

@AllArgsConstructor
@Getter
public class CustomMobEquipment {
  private final ItemStack mainHand;
  private final ItemStack offHand;
  private final ItemStack helmet;
  private final ItemStack chestplate;
  private final ItemStack leggings;
  private final ItemStack boots;
}
