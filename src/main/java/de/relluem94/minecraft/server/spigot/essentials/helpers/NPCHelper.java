package de.relluem94.minecraft.server.spigot.essentials.helpers;

import java.util.Collection;
import java.util.HashSet;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Villager.Profession;
import org.bukkit.potion.PotionEffect;

/**
 *
 * @author rellu
 */
public class NPCHelper {

    private LivingEntity livingEntity;
    private final Location location;
    private final EntityType entityType;
    private final Collection<PotionEffect> potionEffects = new HashSet<PotionEffect>();
    private final String customName;
    private final boolean isCustomNameVisible;
    private boolean isInvisible;
    private boolean canPickupItems = false;
    private double health = 0;
    private Profession profession;

    public NPCHelper(Location location, String customName, Profession profession, boolean isCustomNameVisible) {
        this.location = location;
        this.entityType = EntityType.VILLAGER;
        this.profession = profession;

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

    public void setHealth(double health) {
        this.health = health;
    }

    public void spawn() {
        livingEntity = (LivingEntity) location.getWorld().spawnEntity(location, entityType);
        livingEntity.setCustomName(customName);
        livingEntity.setCustomNameVisible(isCustomNameVisible);
        livingEntity.setAI(false);
        livingEntity.setCollidable(true);
        livingEntity.setPersistent(true);

        if (health == 0 || health > livingEntity.getHealth()) {
            health = livingEntity.getHealth();
        }

        livingEntity.setHealth(health);
        livingEntity.addPotionEffects(potionEffects);
        livingEntity.setInvisible(isInvisible);
        livingEntity.setCanPickupItems(canPickupItems);

        Villager villager = (Villager) livingEntity;
        villager.setProfession(profession);
    }
}
