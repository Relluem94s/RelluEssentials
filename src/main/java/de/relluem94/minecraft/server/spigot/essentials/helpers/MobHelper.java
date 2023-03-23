package de.relluem94.minecraft.server.spigot.essentials.helpers;

import java.util.Collection;
import java.util.HashSet;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

/**
 *
 * @author rellu
 */
public class MobHelper {

    private LivingEntity livingEntity;
    private final Location location;
    private final EntityType entityType;
    private final Collection<PotionEffect> potionEffects = new HashSet<>();
    private final String customName;
    private final boolean isCustomNameVisible;
    private boolean isInvisible;
    private boolean canPickupItems = true;
    private double health = 0;

    public MobHelper(Location location, EntityType entityType, String customName, boolean isCustomNameVisible) {
        this.location = location;
        this.entityType = entityType;
        if (customName != null) {
            this.customName = customName;
        } else {
            this.customName = entityType.name();
        }

        this.isCustomNameVisible = isCustomNameVisible;
    }

    public void setInvisible(boolean isInvisible) {
        this.isInvisible = isInvisible;
    }

    public void setCanPickupItems(boolean canPickupItems) {
        this.canPickupItems = canPickupItems;
    }

    public void addPotionEffect(PotionEffect potionEffect) {
        potionEffects.add(potionEffect);
    }

    public void addPotionEffect(Collection<PotionEffect> potionEffects) {
        potionEffects.addAll(potionEffects);
    }

    public void setHealth(double health) {
        this.health = health;
    }

    public void spawn(ItemStack mainHand, ItemStack offHand, ItemStack helmet, ItemStack chest, ItemStack leggings, ItemStack boots) {
        spawn();

        if (livingEntity.getEquipment() != null) {
            livingEntity.getEquipment().setItemInMainHand(mainHand);
            livingEntity.getEquipment().setItemInOffHand(offHand);

            livingEntity.getEquipment().setBoots(boots);
            livingEntity.getEquipment().setLeggings(leggings);
            livingEntity.getEquipment().setChestplate(chest);
            livingEntity.getEquipment().setHelmet(helmet);
        }
    }

    public void spawn() {
        livingEntity = (LivingEntity) location.getWorld().spawnEntity(location, entityType);
        livingEntity.setCustomName(customName);
        livingEntity.setCustomNameVisible(isCustomNameVisible);

        if (health == 0 || health > livingEntity.getHealth()) {
            health = livingEntity.getHealth();
        }

        livingEntity.setHealth(health);
        livingEntity.addPotionEffects(potionEffects);
        livingEntity.setInvisible(isInvisible);
        livingEntity.setCanPickupItems(canPickupItems);

    }
}
