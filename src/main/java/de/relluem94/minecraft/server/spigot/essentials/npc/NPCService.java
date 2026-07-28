package de.relluem94.minecraft.server.spigot.essentials.npc;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;

import java.util.*;

public class NPCService {

    private final NPCRepository npcRepository;
    private final NPCSpawner npcSpawner;
    private final NPCValidator npcValidator;
    private final Map<UUID, NPC> loadedNPCs;

    public NPCService(NPCRepository npcRepository, NPCSpawner npcSpawner, NPCValidator npcValidator) {
        this.npcRepository = npcRepository;
        this.npcSpawner = npcSpawner;
        this.npcValidator = npcValidator;
        this.loadedNPCs = new LinkedHashMap<>();
    }

    public NPCOperationResult createNPC(String profileName, double x, double y, double z, String worldName, int actorPlayerId) {
        NPCValidator.ValidationResult profileValidation = npcValidator.validateProfileName(profileName);
        if (!profileValidation.valid()) {
            return NPCOperationResult.failure(profileValidation.errorMessage());
        }

        NPCValidator.ValidationResult coordinateValidation = npcValidator.validateCoordinates(x, y, z);
        if (!coordinateValidation.valid()) {
            return NPCOperationResult.failure(coordinateValidation.errorMessage());
        }

        NPC npc = new NPC(-1, UUID.randomUUID(), profileName, x, y, z, worldName);
        Optional<UUID> spawnedEntityUUID = npcSpawner.spawnMannequin(npc);
        spawnedEntityUUID.ifPresent(npc::setEntityUUID);

        npcRepository.save(npc, actorPlayerId);
        loadedNPCs.put(npc.getId(), npc);

        return NPCOperationResult.success(npc);
    }

    public NPCOperationResult updateNPCProfile(UUID npcId, String newProfileName, int actorPlayerId) {
        NPC npc = loadedNPCs.get(npcId);
        if (npc == null) {
            return NPCOperationResult.failure("NPC with ID " + npcId + " not found.");
        }

        NPCValidator.ValidationResult profileValidation = npcValidator.validateProfileName(newProfileName);
        if (!profileValidation.valid()) {
            return NPCOperationResult.failure(profileValidation.errorMessage());
        }

        if (npc.getEntityUUID() != null) {
            npcSpawner.despawnMannequin(npc.getEntityUUID());
        }

        npc.setProfileName(newProfileName);
        Optional<UUID> spawnedEntityUUID = npcSpawner.spawnMannequin(npc);
        spawnedEntityUUID.ifPresent(npc::setEntityUUID);

        npcRepository.save(npc, actorPlayerId);
        return NPCOperationResult.success(npc);
    }

    public NPCOperationResult updateNPCPosition(UUID npcId, double x, double y, double z, int actorPlayerId) {
        NPC npc = loadedNPCs.get(npcId);
        if (npc == null) {
            return NPCOperationResult.failure("NPC with ID " + npcId + " not found.");
        }

        NPCValidator.ValidationResult coordinateValidation = npcValidator.validateCoordinates(x, y, z);
        if (!coordinateValidation.valid()) {
            return NPCOperationResult.failure(coordinateValidation.errorMessage());
        }

        if (npc.getEntityUUID() != null) {
            npcSpawner.despawnMannequin(npc.getEntityUUID());
        }

        npc.setX(x);
        npc.setY(y);
        npc.setZ(z);

        Optional<UUID> spawnedEntityUUID = npcSpawner.spawnMannequin(npc);
        spawnedEntityUUID.ifPresent(npc::setEntityUUID);

        npcRepository.save(npc, actorPlayerId);
        return NPCOperationResult.success(npc);
    }

    public NPCOperationResult deleteNPC(UUID npcId, int actorPlayerId) {
        NPC npc = loadedNPCs.get(npcId);
        if (npc == null) {
            return NPCOperationResult.failure("NPC with ID " + npcId + " not found.");
        }

        if (npc.getEntityUUID() != null) {
            npcSpawner.despawnMannequin(npc.getEntityUUID());
        }

        npcRepository.delete(npcId, actorPlayerId);
        loadedNPCs.remove(npcId);
        RelluEssentials.getInstance().getNpcDialogueTracker().removeNPC(npcId);
        return NPCOperationResult.success(null);
    }

    public void loadAndRespawnAllNPCs(int systemPlayerId) {
        List<NPC> persistedNPCs = npcRepository.loadAll();
        for (NPC npc : persistedNPCs) {
            Optional<UUID> spawnedEntityUUID = npcSpawner.spawnMannequin(npc);
            spawnedEntityUUID.ifPresent(npc::setEntityUUID);
            npcRepository.save(npc, systemPlayerId);
            loadedNPCs.put(npc.getId(), npc);
        }
    }

    public void despawnAllNPCs() {
        for (NPC npc : loadedNPCs.values()) {
            if (npc.getEntityUUID() != null) {
                npcSpawner.despawnMannequin(npc.getEntityUUID());
            }
        }
        loadedNPCs.clear();
    }

    public void reloadNPCDialogue(UUID npcId) {
        NPC npc = loadedNPCs.get(npcId);
        if (npc == null) {
            return;
        }

        npcRepository.loadById(npcId).ifPresent(refreshedNPC -> {
            npc.setDialogueLines(refreshedNPC.getDialogueLines());
            loadedNPCs.put(npcId, npc);
        });
    }

    public List<NPC> getNPCs() {
        return new ArrayList<>(loadedNPCs.values());
    }

    public Optional<NPC> getNPCById(UUID npcId) {
        return Optional.ofNullable(loadedNPCs.get(npcId));
    }

    public Optional<NPC> getNearestNPC(double x, double y, double z, String worldName) {
        return loadedNPCs.values().stream()
                .filter(npc -> npc.getWorldName().equals(worldName))
                .min(Comparator.comparingDouble(npc ->
                        Math.pow(npc.getX() - x, 2) + Math.pow(npc.getY() - y, 2) + Math.pow(npc.getZ() - z, 2)
                ));
    }
}