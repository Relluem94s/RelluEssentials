package de.relluem94.minecraft.server.spigot.essentials.registries;

import de.relluem94.minecraft.server.spigot.essentials.models.mobs.CustomMob;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MobRegistry {

  private final List<CustomMob> registeredMobs = new ArrayList<>();

  public void register(CustomMob mob) {
    registeredMobs.add(mob);
  }

  public void unregister(UUID entityUuid) {
    registeredMobs.removeIf(mob -> mob.getLivingEntity().getUniqueId().equals(entityUuid));
  }

  public List<CustomMob> findAll() {
    return List.copyOf(registeredMobs);
  }

  public List<CustomMob> findAllAlive() {
    return registeredMobs.stream().filter(CustomMob::isAlive).toList();
  }
}