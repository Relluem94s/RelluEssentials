package de.relluem94.minecraft.server.spigot.essentials.models.mobs;

import de.relluem94.minecraft.server.spigot.essentials.models.RelluEssentialsNamespacedKey;
import java.util.Collection;
import org.bukkit.entity.EntityType;
import org.bukkit.potion.PotionEffect;

/**
 * Represents the full definition of a custom mob, including its identity,
 * appearance, attributes, effects, and equipment.
 *
 * @param key                the namespaced key uniquely identifying this custom mob
 * @param entityType         the base Bukkit entity type this mob is based on
 * @param customName         the display name shown above the mob
 * @param isCustomNameVisible whether the custom name is always visible to players
 * @param potionEffects      the collection of potion effects permanently applied to this mob
 * @param equipment          the equipment configuration defining items worn or held by this mob
 * @param health             the maximum health of this mob
 * @param canPickUpItems     whether this mob is allowed to pick up items from the ground
 * @param invisible          whether this mob is permanently invisible
 *
 * @author rellu
 */
public record CustomMobDefinition(RelluEssentialsNamespacedKey key, EntityType entityType, String customName,
                                  boolean isCustomNameVisible, Collection<PotionEffect> potionEffects,
                                  CustomMobEquipment equipment, double health, boolean canPickUpItems,
                                  boolean invisible) {

}
