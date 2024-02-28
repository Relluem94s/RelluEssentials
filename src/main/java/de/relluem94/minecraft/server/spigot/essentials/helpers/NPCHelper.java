package de.relluem94.minecraft.server.spigot.essentials.helpers;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Villager.Profession;
import org.bukkit.potion.PotionEffect;

import de.relluem94.minecraft.server.spigot.essentials.npc.NPC;

/**
 *
 * @author rellu
 */
public class NPCHelper {

    private final Location location;
    private final EntityType entityType;
    private final Collection<PotionEffect> potionEffects = new HashSet<>();
    @Getter
    private final String customName;
    private final boolean isCustomNameVisible;
    private boolean isInvisible;
    private boolean isCollideAble = true;
    @Setter
    private double health = 0;
    private final Profession profession;

    public static final int INV_SIZE = 54;

    public NPCHelper(Location location, NPC npc) {
        this.location = location;
        this.entityType = EntityType.VILLAGER;
        this.profession = npc.getProfession();

        if (npc.getName() != null) {
            this.customName = npc.getName();
        } else {
            this.customName = entityType.name();
        }

        this.isCustomNameVisible = true;
    }

    public NPCHelper(Location location, String customName, Profession profession, boolean isCustomNameVisible) {
        this.location = location;
        this.entityType = EntityType.VILLAGER;
        this.profession = profession;

        this.customName = Objects.requireNonNullElseGet(customName, entityType::name);

        this.isCustomNameVisible = isCustomNameVisible;
    }

    @SuppressWarnings("unused")
    public void setInvisible(boolean isInvisible) {
        this.isInvisible = isInvisible;
    }

    @SuppressWarnings("unused")
    public void setCollideAble(boolean b) {
        this.isCollideAble = b;
    }

    public void spawn() {
        LivingEntity livingEntity = (LivingEntity) Objects.requireNonNull(location.getWorld()).spawnEntity(location, entityType);
        livingEntity.setCustomName(customName);
        livingEntity.setCustomNameVisible(isCustomNameVisible);
        livingEntity.setAI(false);
        livingEntity.setCollidable(isCollideAble);
        livingEntity.setPersistent(true);

        if (health == 0 || health > livingEntity.getHealth()) {
            health = livingEntity.getHealth();
        }

        livingEntity.setHealth(health);
        livingEntity.addPotionEffects(potionEffects);
        livingEntity.setInvisible(isInvisible);
        boolean canPickupItems = false;
        livingEntity.setCanPickupItems(canPickupItems);

        Villager villager = (Villager) livingEntity;
        villager.setProfession(profession);
    }
}