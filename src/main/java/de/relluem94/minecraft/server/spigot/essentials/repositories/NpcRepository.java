package de.relluem94.minecraft.server.spigot.essentials.repositories;

import de.relluem94.minecraft.server.spigot.essentials.helpers.db.mapper.NpcMapper;
import de.relluem94.minecraft.server.spigot.essentials.models.Npc;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.NpcDialogueEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.NpcEntry;
import de.relluem94.minecraft.server.spigot.essentials.persistence.dao.NpcDao;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class NpcRepository {

  private final NpcDao npcDao;

  public NpcRepository(NpcDao npcDao) {
    this.npcDao = npcDao;
  }

  public List<Npc> loadAll() {
    return npcDao.findAll().stream()
        .map(entry -> NpcMapper.toDomain(entry, npcDao.getNPCDialogues(entry.getId())))
        .toList();
  }

  public Optional<Npc> loadById(UUID npcId) {
    NpcEntry entry = npcDao.getNPC(npcId);
    if (entry == null) {
      return Optional.empty();
    }
    return Optional.of(NpcMapper.toDomain(entry, npcDao.getNPCDialogues(entry.getId())));
  }


  public void save(Npc npc, int actorPlayerId) {
    NpcEntry existingEntry = npcDao.getNPC(npc.getId());
    NpcEntry entry = NpcMapper.toEntry(npc, actorPlayerId);
    if (existingEntry == null) {
      int generatedId = npcDao.insertNPC(entry);
      npc.setDbid(generatedId);
    } else {
      entry.setId(existingEntry.getId());
      npcDao.updateNPC(entry);
      npc.setDbid(existingEntry.getId());
    }
  }

  public void delete(UUID npcId, int deletedByPlayerId) {
    npcDao.deleteNPCDialogueByNpcId(npcId, deletedByPlayerId);
    npcDao.deleteNPC(npcId, deletedByPlayerId);
  }

  public List<NpcDialogueEntry> loadDialoguesByNpcDbId(int npcDbId) {
    return npcDao.findDialoguesByNpcId(npcDbId);
  }

  public void addDialogue(NpcDialogueEntry entry) {
    npcDao.insertNPCDialogue(entry);
  }

  public boolean updateDialogue(NpcDialogueEntry entry, UUID dialogueUuid) {
    return npcDao.updateNPCDialogue(entry, dialogueUuid);
  }

  public void deleteDialogueByPosition(UUID npcUuid, int listPosition, int deletedByPlayerId) {
    npcDao.deleteNPCDialogueById(npcUuid, listPosition, deletedByPlayerId);
  }

  public void deleteAllDialoguesByNpcUuid(UUID npcUuid, int deletedByPlayerId) {
    npcDao.deleteNPCDialogueByNpcId(npcUuid, deletedByPlayerId);
  }
}