package de.relluem94.minecraft.server.spigot.essentials.services;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.helpers.InventoryHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.NpcEquipmentInventoryHelper;
import de.relluem94.minecraft.server.spigot.essentials.model.Npc;
import de.relluem94.minecraft.server.spigot.essentials.npc.NpcOperationResult;
import de.relluem94.minecraft.server.spigot.essentials.npc.NpcRepository;
import de.relluem94.minecraft.server.spigot.essentials.npc.NpcSpawner;
import de.relluem94.minecraft.server.spigot.essentials.npc.NpcValidator;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import org.bukkit.Bukkit;
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

  public NpcOperationResult createNPC(String profileName, double x, double y, double z,
      String worldName, int actorPlayerId) {
    NpcValidator.ValidationResult profileValidation = npcValidator.validateProfileName(profileName);
    if (!profileValidation.valid()) {
      return NpcOperationResult.failure(profileValidation.errorMessage());
    }

    NpcValidator.ValidationResult coordinateValidation = npcValidator.validateCoordinates(x, y, z);
    if (!coordinateValidation.valid()) {
      return NpcOperationResult.failure(coordinateValidation.errorMessage());
    }

    Npc npc = new Npc(-1, UUID.randomUUID(), profileName, x, y, z, worldName);
    npcRepository.save(npc, actorPlayerId);
    loadedNPCs.put(npc.getId(), npc);

    Set<UUID> managedUUIDs = getCurrentlyManagedEntityUUIDs();
    npcSpawner.spawnMannequinAsync(npc, managedUUIDs,
        spawnedEntityUUID -> spawnedEntityUUID.ifPresent(uuid -> {
          npc.setEntityUUID(uuid);
          npcRepository.save(npc, actorPlayerId);
        }));

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

    npcSpawner.spawnMannequinAsync(npc, getCurrentlyManagedEntityUUIDs(),
        spawnedEntityUUID -> spawnedEntityUUID.ifPresent(uuid -> {
          npc.setEntityUUID(uuid);
          npcRepository.save(npc, actorPlayerId);
        }));
    return NpcOperationResult.success(npc);
  }

  public NpcOperationResult updateNPCPosition(UUID npcId, double x, double y, double z,
      int actorPlayerId) {
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

    npcRepository.save(npc, actorPlayerId);

    npcSpawner.spawnMannequinAsync(npc, getCurrentlyManagedEntityUUIDs(),
        spawnedEntityUUID -> spawnedEntityUUID.ifPresent(uuid -> {
          npc.setEntityUUID(uuid);
          npcRepository.save(npc, actorPlayerId);
        }));
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
    RelluEssentials.getInstance().getNpcDialogueTracker().removeNPC(npcId);
    return NpcOperationResult.success(null);
  }

  public void loadAndRespawnAllNPCs(int systemPlayerId) {
    List<Npc> persistedNPCs = npcRepository.loadAll();
    Set<String> knownProfileNames = persistedNPCs.stream()
        .map(Npc::getProfileName)
        .collect(java.util.stream.Collectors.toSet());
    npcSpawner.despawnAllMannequinsInAllWorlds(knownProfileNames);
    for (Npc npc : persistedNPCs) {
      Optional<UUID> spawnedEntityUUID = npcSpawner.spawnMannequin(npc,
          getCurrentlyManagedEntityUUIDs());
      spawnedEntityUUID.ifPresent(uuid -> {
        npc.setEntityUUID(uuid);
        restoreNPCEquipment(npc);
      });
      npcRepository.save(npc, systemPlayerId);
      loadedNPCs.put(npc.getId(), npc);
    }
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

  private Set<UUID> getCurrentlyManagedEntityUUIDs() {
    return loadedNPCs.values().stream()
        .map(Npc::getEntityUUID)
        .filter(Objects::nonNull)
        .collect(java.util.stream.Collectors.toSet());
  }
}