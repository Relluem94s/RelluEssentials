package de.relluem94.minecraft.server.spigot.essentials.models.mobs;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.jspecify.annotations.NonNull;

/**
 * Represents a customizable mob that can be spawned into a Minecraft world with configurable properties such as potion
 * effects, equipment, visibility, health, and name display.
 *
 * @author rellu
 */
public class CustomMob {

  private final Location location;
  private final EntityType entityType;
  private final Collection<PotionEffect> potionEffects = new HashSet<>();
  private final String customName;
  private final boolean isCustomNameVisible;
  private LivingEntity livingEntity;
  @Setter
  private boolean isInvisible;
  @Setter
  private boolean canPickupItems = true;
  @Setter
  private double health = 0;

  /**
   * Creates a new CustomMob with the given location, entity type, custom name, and name visibility.
   *
   * @param location            the location where the mob will be spawned
   * @param entityType          the type of entity to spawn, must not be null
   * @param customName          the custom display name for the mob, or null to use the entity type name
   * @param isCustomNameVisible whether the custom name is visible above the mob
   */
  public CustomMob(Location location, @NonNull EntityType entityType, String customName, boolean isCustomNameVisible) {
    this.location = location;
    this.entityType = entityType;
    this.customName = Objects.requireNonNullElseGet(customName, entityType::name);

    this.isCustomNameVisible = isCustomNameVisible;
  }

  /**
   * Adds a single potion effect to be applied to the mob upon spawning.
   *
   * @param potionEffect the potion effect to add
   */
  public void addPotionEffect(PotionEffect potionEffect) {
    potionEffects.add(potionEffect);
  }

  /**
   * Adds multiple potion effects to be applied to the mob upon spawning.
   *
   * @param potionEffects the collection of potion effects to add
   */
  @SuppressWarnings("unused")
  public void addPotionEffect(Collection<PotionEffect> potionEffects) {
    this.potionEffects.addAll(potionEffects);
  }

  /**
   * Spawns the mob into the world and equips it with the given armor and hand items. If the mob has no equipment slot,
   * the items are silently ignored.
   *
   * @param mainHand the item to place in the main hand slot
   * @param offHand  the item to place in the offhand slot
   * @param helmet   the item to place in the helmet slot
   * @param chest    the item to place in the chestplate slot
   * @param leggings the item to place in the leggings slot
   * @param boots    the item to place in the boots slot
   */
  public void spawn(ItemStack mainHand, ItemStack offHand, ItemStack helmet, ItemStack chest, ItemStack leggings,
      ItemStack boots) {
    spawn();

    if (livingEntity == null || livingEntity.getEquipment() == null) {
      return;
    }

    livingEntity.getEquipment().setItemInMainHand(mainHand);
    livingEntity.getEquipment().setItemInOffHand(offHand);

    livingEntity.getEquipment().setBoots(boots);
    livingEntity.getEquipment().setLeggings(leggings);
    livingEntity.getEquipment().setChestplate(chest);
    livingEntity.getEquipment().setHelmet(helmet);
  }

  /**
   * Spawns the mob into the world at the configured location, applying all configured properties such as custom name,
   * health, potion effects, invisibility, and item pickup ability. Does nothing if the location has no associated
   * world.
   */
  public void spawn() {
    World world = location.getWorld();

    if (world == null) {
      return;
    }

    livingEntity = (LivingEntity) world.spawnEntity(location, entityType);
    livingEntity.setCustomName(customName);
    livingEntity.setCustomNameVisible(isCustomNameVisible);

    if (health == 0) {
      health = livingEntity.getHealth();
    }

    livingEntity.setHealth(health);
    livingEntity.addPotionEffects(potionEffects);
    livingEntity.setInvisible(isInvisible);
    livingEntity.setCanPickupItems(canPickupItems);
  }
}
