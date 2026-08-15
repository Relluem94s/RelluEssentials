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
  private final Map<UUID, Npc> loadedNPCs;

  public NpcService(NpcRepository npcRepository, NpcSpawner npcSpawner, NpcValidator npcValidator) {
    this.npcRepository = npcRepository;
    this.npcSpawner = npcSpawner;
    this.npcValidator = npcValidator;
    this.loadedNPCs = new LinkedHashMap<>();
  }

  public boolean isTrackedNpcEntity(UUID entityUUID) {
    return loadedNPCs.values().stream()
        .anyMatch(npc -> entityUUID.equals(npc.getEntityUUID()));
  }


  public NpcOperationResult createNPC(String profileName, double x, double y, double z,
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
    loadedNPCs.put(npc.getId(), npc);

    npcSpawner.spawnMannequin(npc).ifPresent(uuid -> {
      npc.setEntityUUID(uuid);
      npcRepository.save(npc, actorPlayerId);
    });

    return NpcOperationResult.success(npc);
  }

  public NpcOperationResult updateNPCProfile(UUID npcId, String newProfileName, int actorPlayerId) {
    Npc npc = loadedNPCs.get(npcId);
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
      restoreNPCEquipment(npc);
      npcRepository.save(npc, actorPlayerId);
    });

    return NpcOperationResult.success(npc);
  }

  public NpcOperationResult updateNPCPosition(UUID npcId, double x, double y, double z,
      float yaw, float pitch, int actorPlayerId) {
    Npc npc = loadedNPCs.get(npcId);
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
      restoreNPCEquipment(npc);
      npcRepository.save(npc, actorPlayerId);
    });

    return NpcOperationResult.success(npc);
  }

  public void saveNPCInventory(@NonNull Npc npc, Inventory inventory) {
    npc.setInventory(InventoryHelper.saveInventoryToJSON(inventory));
    npcRepository.save(npc, -1);
  }

  public NpcOperationResult deleteNPC(UUID npcId, int actorPlayerId) {
    Npc npc = loadedNPCs.get(npcId);
    if (npc == null) {
      return NpcOperationResult.failure("NPC with ID " + npcId + " not found.");
    }

    if (npc.getEntityUUID() != null) {
      npcSpawner.despawnMannequin(npc.getEntityUUID());
    }

    npcRepository.delete(npcId, actorPlayerId);
    loadedNPCs.remove(npcId);

    return NpcOperationResult.success(npc);
  }

  private void restoreNPCEquipment(@NonNull Npc npc) {
    if (npc.getInventory() == null || npc.getEntityUUID() == null) {
      return;
    }
    Inventory equipmentInventory = Bukkit.createInventory(null, 54);
    InventoryHelper.loadInventoryFromJSON(equipmentInventory, npc.getInventory());
    NpcEquipmentInventoryHelper.applyInventoryEquipmentToEntity(equipmentInventory,
        npc.getEntityUUID());
  }


  public void despawnAllNPCs() {
    for (Npc npc : loadedNPCs.values()) {
      if (npc.getEntityUUID() != null) {
        npcSpawner.despawnMannequin(npc.getEntityUUID());
      }
    }
    loadedNPCs.clear();
  }

  public void reloadNPCDialogue(UUID npcId) {
    Npc npc = loadedNPCs.get(npcId);
    if (npc == null) {
      return;
    }

    npcRepository.loadById(npcId).ifPresent(refreshedNPC -> {
      npc.setDialogueLines(refreshedNPC.getDialogueLines());
      loadedNPCs.put(npcId, npc);
    });
  }

  public List<Npc> getNPCs() {
    return new ArrayList<>(loadedNPCs.values());
  }

  public Optional<Npc> getNPCById(UUID npcId) {
    return Optional.ofNullable(loadedNPCs.get(npcId));
  }

  public Optional<Npc> getNearestNPC(double x, double y, double z, String worldName) {
    return loadedNPCs.values().stream()
        .filter(npc -> npc.getWorldName().equals(worldName))
        .min(Comparator.comparingDouble(npc ->
            Math.pow(npc.getX() - x, 2) + Math.pow(npc.getY() - y, 2) + Math.pow(npc.getZ() - z, 2)
        ));
  }

  public void spawnNpc(Npc npc) {
    npcSpawner.spawnMannequin(npc).ifPresent(uuid -> {
      npc.setEntityUUID(uuid);
      restoreNPCEquipment(npc);
      loadedNPCs.putIfAbsent(npc.getId(), npc);
    });
  }

  public void despawnNpc(UUID npcId) {
    Npc npc = loadedNPCs.get(npcId);
    if (npc != null && npc.getEntityUUID() != null) {
      npcSpawner.despawnMannequin(npc.getEntityUUID());
      npc.setEntityUUID(null);
    }
  }

  public List<Npc> getAllNpcs() {
    return new ArrayList<>(loadedNPCs.values());
  }

  public void loadAndSpawnNpcsInLoadedChunks() {
    npcRepository.loadAll().forEach(npc -> {
      loadedNPCs.put(npc.getId(), npc);
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

  public List<NpcDialogueEntry> getNPCDialogues(int npcDbId) {
    return npcRepository.loadDialoguesByNpcDbId(npcDbId);
  }

  public void addNPCDialogue(NpcDialogueEntry entry) {
    npcRepository.addDialogue(entry);
  }

  public boolean updateNPCDialogue(NpcDialogueEntry entry, UUID dialogueUuid) {
    return npcRepository.updateDialogue(entry, dialogueUuid);
  }

  public void deleteNPCDialogueByPosition(UUID npcId, int listPosition, int deletedByPlayerId) {
    Npc npc = loadedNPCs.get(npcId);
    if (npc == null) {
      return;
    }
    npcRepository.deleteDialogueByPosition(npcId, listPosition, deletedByPlayerId);
  }
}