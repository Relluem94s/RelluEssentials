package de.relluem94.minecraft.server.spigot.essentials.registries;

import de.relluem94.minecraft.server.spigot.essentials.models.mobs.CustomMob;
import java.util.List;
import java.util.UUID;

public class MobRegistry {
  public void register(CustomMob mob) { ... }
  public void unregister(UUID entityUuid) { ... }
  public List<CustomMob> findAll() { ... }
  public List<CustomMob> findAllAlive() { ... }
}
