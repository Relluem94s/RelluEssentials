package de.relluem94.minecraft.server.spigot.essentials.services;

import de.relluem94.minecraft.server.spigot.essentials.helpers.InventoryHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.NpcEquipmentInventoryHelper;
import de.relluem94.minecraft.server.spigot.essentials.models.Npc;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.NpcDialogueEntry;
import de.relluem94.minecraft.server.spigot.essentials.npcs.NpcOperationResult;
import de.relluem94.minecraft.server.spigot.essentials.npcs.NpcSpawner;
import de.relluem94.minecraft.server.spigot.essentials.npcs.NpcValidator;
import de.relluem94.minecraft.server.spigot.essentials.repositories.NpcRepository;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.inventory.Inventory;
import org.jspecify.annotations.NonNull;

/**
 * Service responsible for managing the lifecycle, state, and persistence of NPCs.
 */
public class NpcService {

  private final NpcRepository npcRepository;
  private final NpcSpawner npcSpawner;
  private final NpcValidator npcValidator;
  private final Map<UUID, Npc> loadedNpcs;
  private final NpcDialogueProgressService npcDialogueProgressService;

  /**
   * Constructs a new NpcService.
   *
   * @param npcRepository the repository for NPC persistence
   * @param npcSpawner the spawner for NPC entities
   * @param npcValidator the validator for NPC data
   * @param npcDialogueProgressService the service managing dialogue progress
   */
  public NpcService(NpcRepository npcRepository, NpcSpawner npcSpawner,
      NpcValidator npcValidator, NpcDialogueProgressService npcDialogueProgressService) {
    this.npcRepository = npcRepository;
    this.npcSpawner = npcSpawner;
    this.npcValidator = npcValidator;
    this.npcDialogueProgressService = npcDialogueProgressService;
    this.loadedNpcs = new LinkedHashMap<>();
  }

  /**
   * Checks if a specific entity UUID is currently being tracked as an NPC.
   *
   * @param entityUuid the UUID of the entity to check
   * @return true if the entity is a tracked NPC, false otherwise
   */
  public boolean isTrackedNpcEntity(UUID entityUuid) {
    return loadedNpcs.values().stream()
        .anyMatch(npc -> entityUuid.equals(npc.getEntityUUID()));
  }

  /**
   * Creates a new NPC and persists it.
   *
   * @param profileName the name of the NPC profile
   * @param x the x coordinate
   * @param y the y coordinate
   * @param z the z coordinate
   * @param yaw the yaw rotation
   * @param pitch the pitch rotation
   * @param worldName the name of the world
   * @param actorPlayerId the ID of the player performing the action
   * @return an operation result containing the created NPC or an error message
   */
  public NpcOperationResult createNpc(String profileName, double x, double y, double z,
      float yaw, float pitch, String worldName, int actorPlayerId) {
    NpcValidator.ValidationResult profileValidation = npcValidator.validateProfileName(profileName);
    if (!profileValidation.valid()) {
      return NpcOperationResult.failure(profileValidation.errorMessage());
    }

    NpcValidator.ValidationResult coordinateValidation = npcValidator.validateCoordinates(x, y, z);
    if (!coordinateValidation.valid()) {
      return NpcOperationResult.failure(coordinateValidation.errorMessage());
    }

    Npc npc = new Npc(-1, UUID.randomUUID(), profileName, x, y, z, yaw, pitch, worldName);
    npcRepository.save(npc, actorPlayerId);
    loadedNpcs.put(npc.getId(), npc);

    npcSpawner.spawnMannequin(npc).ifPresent(uuid -> {
      npc.setEntityUUID(uuid);
      npcRepository.save(npc, actorPlayerId);
    });

    return NpcOperationResult.success(npc);
  }

  /**
   * Updates the profile name of an existing NPC.
   *
   * @param npcId the UUID of the NPC
   * @param newProfileName the new name to assign
   * @param actorPlayerId the ID of the player performing the action
   * @return an operation result indicating success or failure
   */
  public NpcOperationResult updateNpcProfile(UUID npcId, String newProfileName, int actorPlayerId) {
    Npc npc = loadedNpcs.get(npcId);
    if (npc == null) {
      return NpcOperationResult.failure("NPC with ID " + npcId + " not found.");
    }

    NpcValidator.ValidationResult profileValidation = npcValidator.validateProfileName(
        newProfileName);
    if (!profileValidation.valid()) {
      return NpcOperationResult.failure(profileValidation.errorMessage());
    }

    if (npc.getEntityUUID() != null) {
      npcSpawner.despawnMannequin(npc.getEntityUUID());
    }

    npc.setProfileName(newProfileName);
    npcRepository.save(npc, actorPlayerId);

    npcSpawner.spawnMannequin(npc).ifPresent(uuid -> {
      npc.setEntityUUID(uuid);
      restoreNpcEquipment(npc);
      npcRepository.save(npc, actorPlayerId);
    });

    return NpcOperationResult.success(npc);
  }

  /**
   * Updates the spatial coordinates and rotation of an existing NPC.
   *
   * @param npcId the UUID of the NPC
   * @param x the new x coordinate
   * @param y the new y coordinate
   * @param z the new z coordinate
   * @param yaw the new yaw rotation
   * @param pitch the new pitch rotation
   * @param actorPlayerId the ID of the player performing the action
   * @return an operation result indicating success or failure
   */
  public NpcOperationResult updateNpcPosition(UUID npcId, double x, double y, double z,
      float yaw, float pitch, int actorPlayerId) {
    Npc npc = loadedNpcs.get(npcId);
    if (npc == null) {
      return NpcOperationResult.failure("NPC with ID " + npcId + " not found.");
    }

    NpcValidator.ValidationResult coordinateValidation = npcValidator.validateCoordinates(x, y, z);
    if (!coordinateValidation.valid()) {
      return NpcOperationResult.failure(coordinateValidation.errorMessage());
    }

    if (npc.getEntityUUID() != null) {
      npcSpawner.despawnMannequin(npc.getEntityUUID());
    }

    npc.setX(x);
    npc.setY(y);
    npc.setZ(z);
    npc.setYaw(yaw);
    npc.setPitch(pitch);
    npcRepository.save(npc, actorPlayerId);

    npcSpawner.spawnMannequin(npc).ifPresent(uuid -> {
      npc.setEntityUUID(uuid);
      restoreNpcEquipment(npc);
      npcRepository.save(npc, actorPlayerId);
    });

    return NpcOperationResult.success(npc);
  }

  /**
   * Saves the current inventory of an NPC to its persistent storage.
   *
   * @param npc the NPC whose inventory is being saved
   * @param inventory the inventory containing the equipment
   */
  public void saveNpcInventory(@NonNull Npc npc, Inventory inventory) {
    npc.setInventory(InventoryHelper.saveInventoryToJSON(inventory));
    npcRepository.save(npc, -1);
  }

  /**
   * Deletes an NPC from the system and despawns its entity.
   *
   * @param npcId the UUID of the NPC to delete
   * @param actorPlayerId the ID of the player performing the action
   * @return an operation result indicating success or failure
   */
  public NpcOperationResult deleteNpc(UUID npcId, int actorPlayerId) {
    Npc npc = loadedNpcs.get(npcId);
    if (npc == null) {
      return NpcOperationResult.failure("NPC with ID " + npcId + " not found.");
    }

    if (npc.getEntityUUID() != null) {
      npcSpawner.despawnMannequin(npc.getEntityUUID());
    }

    npcDialogueProgressService.removeNpc(npcId);
    npcRepository.delete(npcId, actorPlayerId);
    loadedNpcs.remove(npcId);

    return NpcOperationResult.success(npc);
  }

  private void restoreNpcEquipment(@NonNull Npc npc) {
    if (npc.getInventory() == null || npc.getEntityUUID() == null) {
      return;
    }
    Inventory equipmentInventory = Bukkit.createInventory(null, 54);
    InventoryHelper.loadInventoryFromJSON(equipmentInventory, npc.getInventory());
    NpcEquipmentInventoryHelper.applyInventoryEquipmentToEntity(equipmentInventory,
        npc.getEntityUUID());
  }

  /**
   * Despawns all currently loaded NPC entities and clears the loaded cache.
   */
  public void despawnAllNpcs() {
    for (Npc npc : loadedNpcs.values()) {
      if (npc.getEntityUUID() != null) {
        npcSpawner.despawnMannequin(npc.getEntityUUID());
      }
    }
    loadedNpcs.clear();
  }

  /**
   * Reloads the dialogue lines for a specific NPC from the repository.
   *
   * @param npcId the UUID of the NPC
   */
  public void reloadNpcDialogue(UUID npcId) {
    Npc npc = loadedNpcs.get(npcId);
    if (npc == null) {
      return;
    }

    npcRepository.loadById(npcId).ifPresent(refreshedNPC -> {
      npc.setDialogueLines(refreshedNPC.getDialogueLines());
      loadedNpcs.put(npcId, npc);
    });
  }

  /**
   * Retrieves all currently loaded NPCs.
   *
   * @return a list of loaded NPCs
   */
  public List<Npc> getNpcs() {
    return new ArrayList<>(loadedNpcs.values());
  }

  /**
   * Finds a loaded NPC by its unique identifier.
   *
   * @param npcId the UUID of the NPC
   * @return an Optional containing the NPC if found, or empty otherwise
   */
  public Optional<Npc> getNpcById(UUID npcId) {
    return Optional.ofNullable(loadedNpcs.get(npcId));
  }

  /**
   * Finds the nearest loaded NPC to the specified coordinates within a specific world.
   *
   * @param x the x coordinate
   * @param y the y coordinate
   * @param z the z coordinate
   * @param worldName the name of the world to search in
   * @return an Optional containing the nearest NPC, or empty if none found
   */
  public Optional<Npc> getNearestNpc(double x, double y, double z, String worldName) {
    return loadedNpcs.values().stream()
        .filter(npc -> npc.getWorldName().equals(worldName))
        .min(Comparator.comparingDouble(npc ->
            Math.pow(npc.getX() - x, 2) + Math.pow(npc.getY() - y, 2) + Math.pow(npc.getZ() - z, 2)
        ));
  }

  /**
   * Spawns an NPC entity into the world and loads its equipment.
   *
   * @param npc the NPC to spawn
   */
  public void spawnNpc(Npc npc) {
    npcSpawner.spawnMannequin(npc).ifPresent(uuid -> {
      npc.setEntityUUID(uuid);
      restoreNpcEquipment(npc);
      loadedNpcs.putIfAbsent(npc.getId(), npc);
    });
  }

  /**
   * Despawns the entity associated with the given NPC ID.
   *
   * @param npcId the UUID of the NPC
   */
  public void despawnNpc(UUID npcId) {
    Npc npc = loadedNpcs.get(npcId);
    if (npc != null && npc.getEntityUUID() != null) {
      npcSpawner.despawnMannequin(npc.getEntityUUID());
      npc.setEntityUUID(null);
    }
  }

  /**
   * Retrieves all NPCs currently managed by the service.
   *
   * @return a list of all NPCs
   */
  public List<Npc> getAllNpcs() {
    return new ArrayList<>(loadedNpcs.values());
  }

  /**
   * Loads all NPCs from the repository and spawns them if their respective chunks are loaded.
   */
  public void loadAndSpawnNpcsInLoadedChunks() {
    npcRepository.loadAll().forEach(npc -> {
      loadedNpcs.put(npc.getId(), npc);
      World world = Bukkit.getWorld(npc.getWorldName());
      if (world == null) {
        return;
      }
      int chunkX = ((int) npc.getX()) >> 4;
      int chunkZ = ((int) npc.getZ()) >> 4;
      if (world.isChunkLoaded(chunkX, chunkZ)) {
        spawnNpc(npc);
      }
    });
  }

  /**
   * Retrieves the dialogue entries for a specific NPC database ID.
   *
   * @param npcDbId the database ID of the NPC
   * @return a list of dialogue entries
   */
  public List<NpcDialogueEntry> getNpcDialogues(int npcDbId) {
    return npcRepository.loadDialoguesByNpcDbId(npcDbId);
  }

  /**
   * Adds a new dialogue entry to the repository.
   *
   * @param entry the dialogue entry to add
   */
  public void addNpcDialogue(NpcDialogueEntry entry) {
    npcRepository.addDialogue(entry);
  }

  /**
   * Updates an existing dialogue entry.
   *
   * @param entry the updated dialogue entry
   * @param dialogueUuid the UUID of the dialogue to update
   * @return true if the update was successful, false otherwise
   */
  public boolean updateNpcDialogue(NpcDialogueEntry entry, UUID dialogueUuid) {
    return npcRepository.updateDialogue(entry, dialogueUuid);
  }

  /**
   * Deletes a dialogue entry at a specific position for a given NPC.
   *
   * @param npcId the UUID of the NPC
   * @param listPosition the index of the dialogue to delete
   * @param deletedByPlayerId the ID of the player performing the action
   */
  public void deleteNpcDialogueByPosition(UUID npcId, int listPosition, int deletedByPlayerId) {
    Npc npc = loadedNpcs.get(npcId);
    if (npc == null) {
      return;
    }
    npcRepository.deleteDialogueByPosition(npcId, listPosition, deletedByPlayerId);
  }
}