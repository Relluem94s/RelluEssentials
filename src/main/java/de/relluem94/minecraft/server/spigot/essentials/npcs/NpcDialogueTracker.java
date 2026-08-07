package de.relluem94.minecraft.server.spigot.essentials.npcs;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class NpcDialogueTracker {

  private final Map<UUID, Map<UUID, Integer>> playerProgressPerNPC;

  public NpcDialogueTracker() {
    this.playerProgressPerNPC = new ConcurrentHashMap<>();
  }

  public int getNextLineIndexAndAdvance(UUID npcId, UUID playerId, int totalLines) {
    playerProgressPerNPC.computeIfAbsent(npcId, _ -> new ConcurrentHashMap<>());
    Map<UUID, Integer> progressForNPC = playerProgressPerNPC.get(npcId);

    int currentIndex = progressForNPC.getOrDefault(playerId, 0);
    int nextIndex = (currentIndex + 1) % totalLines;
    progressForNPC.put(playerId, nextIndex);

    return currentIndex;
  }

  public void resetPlayerProgress(UUID playerId) {
    playerProgressPerNPC.values().forEach(progress -> progress.remove(playerId));
  }

  public void removeNPC(UUID npcId) {
    playerProgressPerNPC.remove(npcId);
  }
}