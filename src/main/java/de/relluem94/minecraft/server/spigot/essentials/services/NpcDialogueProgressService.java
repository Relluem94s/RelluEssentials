package de.relluem94.minecraft.server.spigot.essentials.services;

import de.relluem94.minecraft.server.spigot.essentials.registries.NpcDialogueRegistry;
import java.util.Map;
import java.util.UUID;

/**
 * Service responsible for managing.
 * And advancing the dialogue progress of players interacting with NPCs.
 */
public class NpcDialogueProgressService {

  private final NpcDialogueRegistry npcDialogueRegistry;

  /**
   * Constructs a new NpcDialogueProgressService.
   *
   * @param npcDialogueRegistry The registry used to store and manage NPC dialogue progress.
   */
  public NpcDialogueProgressService(NpcDialogueRegistry npcDialogueRegistry) {
    this.npcDialogueRegistry = npcDialogueRegistry;
  }

  /**
   * Retrieves the current dialogue line index for a player.
   * And advances their progress to the next line.
   * If the player reaches the end of the dialogue, the index wraps back to zero.
   *
   * @param npcId      The unique identifier of the NPC.
   * @param playerId    The unique identifier of the player.
   * @param totalLines The total number of dialogue lines available.
   * @return The index of the current dialogue line before advancing.
   */
  public int getNextLineIndexAndAdvance(UUID npcId, UUID playerId, int totalLines) {
    Map<UUID, Integer> progressForNpc = npcDialogueRegistry.getOrCreateProgressForNpc(npcId);
    int currentIndex = progressForNpc.getOrDefault(playerId, 0);
    progressForNpc.put(playerId, (currentIndex + 1) % totalLines);
    return currentIndex;
  }

  /**
   * Removes all dialogue progress associated with a specific player across all NPCs.
   *
   * @param playerId The unique identifier of the player.
   */
  public void resetPlayerProgress(UUID playerId) {
    npcDialogueRegistry.getAllNpcProgressMaps()
        .forEach(progress -> progress.remove(playerId));
  }

  /**
   * Removes all dialogue progress data associated with a specific NPC.
   *
   * @param npcId The unique identifier of the NPC.
   */
  public void removeNpc(UUID npcId) {
    npcDialogueRegistry.removeNpc(npcId);
  }
}