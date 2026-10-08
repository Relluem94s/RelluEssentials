package de.relluem94.minecraft.server.spigot.essentials.models.mobs;

import java.util.Collection;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.entity.EntityType;
import org.bukkit.potion.PotionEffect;

@AllArgsConstructor
@Getter
public class CustomMobDefinition {
  private final EntityType entityType;
  private final String customName;
  private final boolean isCustomNameVisible;
  private final Collection<PotionEffect> potionEffects;
  private final CustomMobEquipment equipment;
  private final double health;
  private final boolean canPickUpItems;
  private final boolean invisible;
}
