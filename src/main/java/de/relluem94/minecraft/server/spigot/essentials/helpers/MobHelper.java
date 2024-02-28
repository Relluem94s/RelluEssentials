package de.relluem94.minecraft.server.spigot.essentials.helpers;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

import lombok.Setter;
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
    @Setter
    private boolean canPickupItems = true;
    @Setter
    private double health = 0;

    public MobHelper(Location location, EntityType entityType, String customName, boolean isCustomNameVisible) {
        this.location = location;
        this.entityType = entityType;
        this.customName = Objects.requireNonNullElseGet(customName, entityType::name);

        this.isCustomNameVisible = isCustomNameVisible;
    }

    public void setInvisible(boolean isInvisible) {
        this.isInvisible = isInvisible;
    }

    public void addPotionEffect(PotionEffect potionEffect) {
        potionEffects.add(potionEffect);
    }

    @SuppressWarnings("unused")
    public void addPotionEffect(Collection<PotionEffect> potionEffects) {
        this.potionEffects.addAll(potionEffects);
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
