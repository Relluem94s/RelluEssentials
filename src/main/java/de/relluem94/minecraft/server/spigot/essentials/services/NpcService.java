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

public class NpcService {

  private final NpcRepository npcRepository;
  private final NpcSpawner npcSpawner;
  private final NpcValidator npcValidator;
  private final Map<UUID, Npc> loadedNpcs;
  private final NpcDialogueProgressService npcDialogueProgressService;

  public NpcService(NpcRepository npcRepository, NpcSpawner npcSpawner, NpcValidator npcValidator, NpcDialogueProgressService npcDialogueProgressService) {
    this.npcRepository = npcRepository;
    this.npcSpawner = npcSpawner;
    this.npcValidator = npcValidator;
    this.npcDialogueProgressService = npcDialogueProgressService;
    this.loadedNpcs = new LinkedHashMap<>();
  }

  public boolean isTrackedNpcEntity(UUID entityUuid) {
    return loadedNpcs.values().stream()
        .anyMatch(npc -> entityUuid.equals(npc.getEntityUUID()));
  }


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

  public void saveNpcInventory(@NonNull Npc npc, Inventory inventory) {
    npc.setInventory(InventoryHelper.saveInventoryToJSON(inventory));
    npcRepository.save(npc, -1);
  }

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

  public void despawnAllNpcs() {
    for (Npc npc : loadedNpcs.values()) {
      if (npc.getEntityUUID() != null) {
        npcSpawner.despawnMannequin(npc.getEntityUUID());
      }
    }
    loadedNpcs.clear();
  }

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

  public List<Npc> getNpcs() {
    return new ArrayList<>(loadedNpcs.values());
  }

  public Optional<Npc> getNpcById(UUID npcId) {
    return Optional.ofNullable(loadedNpcs.get(npcId));
  }

  public Optional<Npc> getNearestNpc(double x, double y, double z, String worldName) {
    return loadedNpcs.values().stream()
        .filter(npc -> npc.getWorldName().equals(worldName))
        .min(Comparator.comparingDouble(npc ->
            Math.pow(npc.getX() - x, 2) + Math.pow(npc.getY() - y, 2) + Math.pow(npc.getZ() - z, 2)
        ));
  }

  public void spawnNpc(Npc npc) {
    npcSpawner.spawnMannequin(npc).ifPresent(uuid -> {
      npc.setEntityUUID(uuid);
      restoreNpcEquipment(npc);
      loadedNpcs.putIfAbsent(npc.getId(), npc);
    });
  }

  public void despawnNpc(UUID npcId) {
    Npc npc = loadedNpcs.get(npcId);
    if (npc != null && npc.getEntityUUID() != null) {
      npcSpawner.despawnMannequin(npc.getEntityUUID());
      npc.setEntityUUID(null);
    }
  }

  public List<Npc> getAllNpcs() {
    return new ArrayList<>(loadedNpcs.values());
  }

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

  public List<NpcDialogueEntry> getNpcDialogues(int npcDbId) {
    return npcRepository.loadDialoguesByNpcDbId(npcDbId);
  }

  public void addNpcDialogue(NpcDialogueEntry entry) {
    npcRepository.addDialogue(entry);
  }

  public boolean updateNpcDialogue(NpcDialogueEntry entry, UUID dialogueUuid) {
    return npcRepository.updateDialogue(entry, dialogueUuid);
  }

  public void deleteNpcDialogueByPosition(UUID npcId, int listPosition, int deletedByPlayerId) {
    Npc npc = loadedNpcs.get(npcId);
    if (npc == null) {
      return;
    }
    npcRepository.deleteDialogueByPosition(npcId, listPosition, deletedByPlayerId);
  }
}