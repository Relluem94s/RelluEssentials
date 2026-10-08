package de.relluem94.minecraft.server.spigot.essentials.models.mobs;

import de.relluem94.minecraft.server.spigot.essentials.models.RelluEssentialsNamespacedKey;
import java.util.Collection;
import org.bukkit.entity.EntityType;
import org.bukkit.potion.PotionEffect;

public record CustomMobDefinition(RelluEssentialsNamespacedKey key, EntityType entityType, String customName,
                                  boolean isCustomNameVisible, Collection<PotionEffect> potionEffects,
                                  CustomMobEquipment equipment, double health, boolean canPickUpItems,
                                  boolean invisible) {

}
