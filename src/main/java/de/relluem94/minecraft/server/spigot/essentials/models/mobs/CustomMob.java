package de.relluem94.minecraft.server.spigot.essentials.models.mobs;

import java.util.UUID;
import lombok.Getter;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.jspecify.annotations.NonNull;

/**
 * Represents a customizable mob that can be spawned into a Minecraft world with configurable properties such as potion
 * effects, equipment, visibility, health, and name display.
 *
 * @author rellu
 */
@Getter
public class CustomMob {
  private final UUID entityUuid;
  private final EntityType entityType;
  private final LivingEntity livingEntity;

  /**
   * Creates a new CustomMob from a spawned LivingEntity.
   *
   * @param livingEntity the already spawned entity this CustomMob wraps
   * @param entityType   the type of the spawned entity
   */
  public CustomMob(@NonNull LivingEntity livingEntity, @NonNull EntityType entityType) {
    this.livingEntity = livingEntity;
    this.entityUuid = livingEntity.getUniqueId();
    this.entityType = entityType;
  }

  /**
   * Returns whether the mob is still alive in the world.
   *
   * @return true if the entity is valid and not dead
   */
  public boolean isAlive() {
    return livingEntity.isValid() && !livingEntity.isDead();
  }
}
