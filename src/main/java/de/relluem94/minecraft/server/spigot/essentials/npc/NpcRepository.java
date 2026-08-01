package de.relluem94.minecraft.server.spigot.essentials.npc;

import de.relluem94.minecraft.server.spigot.essentials.helpers.DatabaseHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.db.mapper.NpcMapper;
import de.relluem94.minecraft.server.spigot.essentials.model.Npc;
import de.relluem94.minecraft.server.spigot.essentials.model.pojo.NpcEntry;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class NpcRepository {

  private final DatabaseHelper databaseHelper;

  public NpcRepository(DatabaseHelper databaseHelper) {
    this.databaseHelper = databaseHelper;
  }

  public List<Npc> loadAll() {
    return databaseHelper.getNPCs().stream()
        .map(entry -> NpcMapper.toDomain(entry, databaseHelper.getNPCDialogues(entry.getId())))
        .toList();
  }

  public Optional<Npc> loadById(UUID npcId) {
    NpcEntry entry = databaseHelper.getNPC(npcId);
    if (entry == null) {
      return Optional.empty();
    }
    return Optional.of(NpcMapper.toDomain(entry, databaseHelper.getNPCDialogues(entry.getId())));
  }


  public void save(Npc npc, int actorPlayerId) {
    NpcEntry existingEntry = databaseHelper.getNPC(npc.getId());
    NpcEntry entry = NpcMapper.toEntry(npc, actorPlayerId);
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