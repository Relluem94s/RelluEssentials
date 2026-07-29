package de.relluem94.minecraft.server.spigot.essentials.npc;

import de.relluem94.minecraft.server.spigot.essentials.helpers.DatabaseHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.db.mapper.NPCMapper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.NPCEntry;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class NPCRepository {

    private final DatabaseHelper databaseHelper;

    public NPCRepository(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }

    public List<NPC> loadAll() {
        return databaseHelper.getNPCs().stream()
                .map(entry -> NPCMapper.toDomain(entry, databaseHelper.getNPCDialogues(entry.getId())))
                .toList();
    }

    public Optional<NPC> loadById(UUID npcId) {
        NPCEntry entry = databaseHelper.getNPC(npcId);
        if (entry == null) {
            return Optional.empty();
        }
        return Optional.of(NPCMapper.toDomain(entry, databaseHelper.getNPCDialogues(entry.getId())));
    }


    public void save(NPC npc, int actorPlayerId) {
        NPCEntry existingEntry = databaseHelper.getNPC(npc.getId());
        NPCEntry entry = NPCMapper.toEntry(npc, actorPlayerId);
        if (existingEntry == null) {
            int generatedId = databaseHelper.insertNPC(entry);
            npc.setDbid(generatedId);
        } else {
            entry.setId(existingEntry.getId());
            databaseHelper.updateNPC(entry);
            npc.setDbid(existingEntry.getId());
        }
    }

    public void delete(UUID npcId, int deletedByPlayerId) {
        databaseHelper.deleteNPCDialogueByNpcId(npcId, deletedByPlayerId);
        databaseHelper.deleteNPC(npcId, deletedByPlayerId);
    }
}