package de.relluem94.minecraft.server.spigot.essentials.helpers;

import java.util.Collection;
import java.util.HashSet;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;

/**
 *
 * @author rellu
 */
public class MobHelper {

    private LivingEntity livingEntity;
    private final Location location;
    private final EntityType entityType;
    private final Collection<PotionEffect> potionEffects = new HashSet();
    private final String customName;
    private final boolean isCustomNameVisible;
    private boolean isInvisible;
    private boolean canPickupItems = true;
    private final double health;

    public MobHelper(Location location, EntityType entityType, double health, String customName, boolean isCustomNameVisible) {
        this.location = location;
        this.entityType = entityType;
        if(customName != null){
            this.customName = customName;
        }
        else{
            this.customName = entityType.name();
        }
        
        this.isCustomNameVisible = isCustomNameVisible;
        this.health = health;
    }
    
    public void setInvisible(boolean isInvisible){
        this.isInvisible = isInvisible;
    }
    
    public void setCanPickupItems(boolean canPickupItems){
        this.canPickupItems = canPickupItems;
    }
    
    public void addPotionEffect(PotionEffect potionEffect){
        potionEffects.add(potionEffect);
    }
    
    public void addPotionEffect(Collection<PotionEffect> potionEffects){
        potionEffects.addAll(potionEffects);
    }
    
    
    
    
    
    public void spawn() {
        livingEntity = (LivingEntity) location.getWorld().spawnEntity(location, entityType);
        livingEntity.setCustomName(customName);
        livingEntity.setCustomNameVisible(isCustomNameVisible);
        livingEntity.setHealth(health);
        //le.getEquipment().setArmorContents();
        livingEntity.addPotionEffects(potionEffects);
        livingEntity.setInvisible(isInvisible);
        livingEntity.setCanPickupItems(canPickupItems);

    }
}
