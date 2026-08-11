package de.relluem94.minecraft.server.spigot.essentials.services;

import de.relluem94.minecraft.server.spigot.essentials.registries.NpcDialogueRegistry;
import java.util.Map;
import java.util.UUID;

public class NpcDialogueProgressService {

  private final NpcDialogueRegistry npcDialogueRegistry;

  public NpcDialogueProgressService(NpcDialogueRegistry npcDialogueRegistry) {
    this.npcDialogueRegistry = npcDialogueRegistry;
  }

  public int getNextLineIndexAndAdvance(UUID npcId, UUID playerId, int totalLines) {
    Map<UUID, Integer> progressForNpc = npcDialogueRegistry.getOrCreateProgressForNpc(npcId);
    int currentIndex = progressForNpc.getOrDefault(playerId, 0);
    progressForNpc.put(playerId, (currentIndex + 1) % totalLines);
    return currentIndex;
  }

  public void resetPlayerProgress(UUID playerId) {
    npcDialogueRegistry.getAllNpcProgressMaps()
        .forEach(progress -> progress.remove(playerId));
  }

  public void removeNpc(UUID npcId) {
    npcDialogueRegistry.removeNpc(npcId);
  }
}