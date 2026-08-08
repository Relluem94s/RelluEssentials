package de.relluem94.minecraft.server.spigot.essentials.registries;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class NpcDialogueRegistry {

  private final Map<UUID, Map<UUID, Integer>> playerProgressPerNpc = new ConcurrentHashMap<>();

  public Map<UUID, Integer> getOrCreateProgressForNpc(UUID npcId) {
    return playerProgressPerNpc.computeIfAbsent(npcId, _ -> new ConcurrentHashMap<>());
  }

  public void removeNpc(UUID npcId) {
    playerProgressPerNpc.remove(npcId);
  }

  public Iterable<Map<UUID, Integer>> getAllNpcProgressMaps() {
    return playerProgressPerNpc.values();
  }
}