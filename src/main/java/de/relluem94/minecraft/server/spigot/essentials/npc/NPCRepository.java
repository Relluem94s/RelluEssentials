package de.relluem94.minecraft.server.spigot.essentials.npc;

import de.relluem94.minecraft.server.spigot.essentials.helpers.DatabaseHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.db.mapper.NPCMapper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.NPCEntry;

import java.util.List;
import java.util.UUID;

public class NPCRepository {

    private final DatabaseHelper databaseHelper;

    public NPCRepository(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }

    public List<NPC> loadAll() {
        return databaseHelper.getNPCs().stream()
                .map(NPCMapper::toDomain)
                .toList();
    }

    public void save(NPC npc, int actorPlayerId) {
        NPCEntry existingEntry = databaseHelper.getNPC(npc.getId());
        NPCEntry entry = NPCMapper.toEntry(npc, actorPlayerId);
        if (existingEntry == null) {
            databaseHelper.insertNPC(entry);
        } else {
            entry.setId(existingEntry.getId());
            databaseHelper.updateNPC(entry);
        }
    }

    public void delete(UUID npcId, int deletedByPlayerId) {
        databaseHelper.deleteNPC(npcId, deletedByPlayerId);
    }
}