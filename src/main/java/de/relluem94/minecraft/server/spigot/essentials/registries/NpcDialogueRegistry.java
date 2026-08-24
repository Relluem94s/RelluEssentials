package de.relluem94.minecraft.server.spigot.essentials.registries;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Registry responsible for managing the dialogue progress of players for various NPCs. It stores
 * how far each player has progressed in conversations with specific NPCs.
 */
public class NpcDialogueRegistry {

  private final Map<UUID, Map<UUID, Integer>> playerProgressPerNpc = new ConcurrentHashMap<>();

  /**
   * Retrieves the progress map for a specific NPC, creating a new one if it does not exist.
   *
   * @param npcId The unique identifier of the NPC.
   * @return A map containing the progress levels of players for the given NPC.
   */
  public Map<UUID, Integer> getOrCreateProgressForNpc(UUID npcId) {
    return playerProgressPerNpc.computeIfAbsent(npcId, _ -> new ConcurrentHashMap<>());
  }

  /**
   * Removes all dialogue progress data associated with a specific NPC.
   *
   * @param npcId The unique identifier of the NPC to be removed.
   */
  public void removeNpc(UUID npcId) {
    playerProgressPerNpc.remove(npcId);
  }

  /**
   * Retrieves all stored NPC progress maps.
   *
   * @return An iterable collection of maps, where each map represents player progress for a single
   *     NPC.
   */
  public Iterable<Map<UUID, Integer>> getAllNpcProgressMaps() {
    return playerProgressPerNpc.values();
  }
}