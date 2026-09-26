package de.relluem94.minecraft.server.spigot.essentials.repositories;

import de.relluem94.minecraft.server.spigot.essentials.models.Npc;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.NpcDialogueEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.NpcEntry;
import de.relluem94.minecraft.server.spigot.essentials.persistence.dao.NpcDao;
import de.relluem94.minecraft.server.spigot.essentials.persistence.dao.mapper.NpcMapper;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository for managing {@link Npc} entities and their associated dialogues.
 *
 * <p>Acts as an abstraction layer between the domain model and the persistence layer,
 * delegating all database operations to {@link NpcDao} and using {@link NpcMapper}
 * for object mapping.</p>
 *
 * @author rellu
 */
public class NpcRepository {

  private final NpcDao npcDao;

  /**
   * Creates a new {@code NpcRepository} with the given data access object.
   *
   * @param npcDao the DAO used for all NPC-related database operations
   */
  public NpcRepository(NpcDao npcDao) {
    this.npcDao = npcDao;
  }

  /**
   * Loads all persisted {@link Npc} entities including their associated dialogues.
   *
   * @return a list of all {@link Npc} domain objects; empty if none exist
   */
  public List<Npc> loadAll() {
    return npcDao.findAll().stream()
        .map(entry -> NpcMapper.toDomain(entry, npcDao.findDialoguesByNpcId(entry.getId())))
        .toList();
  }

  /**
   * Loads a single {@link Npc} by its unique identifier including its associated dialogues.
   *
   * @param npcId the UUID of the NPC to load
   * @return an {@link Optional} containing the {@link Npc} if found, or empty if not
   */
  public Optional<Npc> loadById(UUID npcId) {
    NpcEntry entry = npcDao.findByUuid(npcId);
    if (entry == null) {
      return Optional.empty();
    }
    return Optional.of(NpcMapper.toDomain(entry, npcDao.findDialoguesByNpcId(entry.getId())));
  }

  /**
   * Persists the given {@link Npc}, inserting it if it does not yet exist or updating it otherwise.
   *
   * <p>After a successful insert, the generated database ID is written back to the
   * given {@link Npc} via {@link Npc#setDbid(int)}.</p>
   *
   * @param npc            the NPC to save
   * @param actorPlayerId  the database ID of the player performing this operation
   */
  public void save(Npc npc, int actorPlayerId) {
    NpcEntry existingEntry = npcDao.findByUuid(npc.getId());
    NpcEntry entry = NpcMapper.toEntry(npc, actorPlayerId);
    if (existingEntry == null) {
      int generatedId = npcDao.insertNpc(entry);
      npc.setDbid(generatedId);
    } else {
      entry.setId(existingEntry.getId());
      npcDao.updateNpc(entry);
      npc.setDbid(existingEntry.getId());
    }
  }

  /**
   * Deletes the {@link Npc} with the given UUID and all of its associated dialogues.
   *
   * @param npcId             the UUID of the NPC to delete
   * @param deletedByPlayerId the database ID of the player performing this operation
   */
  public void delete(UUID npcId, int deletedByPlayerId) {
    npcDao.deleteNpcDialogueByNpcId(npcId, deletedByPlayerId);
    npcDao.deleteNpc(npcId, deletedByPlayerId);
  }

  /**
   * Loads all dialogue entries associated with the given NPC database ID.
   *
   * @param npcDbId the database ID of the NPC whose dialogues are to be loaded
   * @return a list of {@link NpcDialogueEntry} objects; empty if none exist
   */
  public List<NpcDialogueEntry> loadDialoguesByNpcDbId(int npcDbId) {
    return npcDao.findDialoguesByNpcId(npcDbId);
  }

  /**
   * Persists a new dialogue entry.
   *
   * @param entry the {@link NpcDialogueEntry} to insert
   */
  public void addDialogue(NpcDialogueEntry entry) {
    npcDao.insertNpcDialogue(entry);
  }

  /**
   * Updates an existing dialogue entry identified by the given UUID.
   *
   * @param entry        the {@link NpcDialogueEntry} containing the updated values
   * @param dialogueUuid the UUID of the dialogue entry to update
   * @return {@code true} if the update affected at least one record, {@code false} otherwise
   */
  public boolean updateDialogue(NpcDialogueEntry entry, UUID dialogueUuid) {
    return npcDao.updateNpcDialogue(entry, dialogueUuid);
  }

  /**
   * Deletes a single dialogue entry identified by its position within the NPC's dialogue list.
   *
   * @param npcUuid           the UUID of the NPC whose dialogue entry is to be deleted
   * @param listPosition      the position of the dialogue entry to delete
   * @param deletedByPlayerId the database ID of the player performing this operation
   */
  public void deleteDialogueByPosition(UUID npcUuid, int listPosition, int deletedByPlayerId) {
    npcDao.deleteNpcDialogueById(npcUuid, listPosition, deletedByPlayerId);
  }

  /**
   * Deletes all dialogue entries associated with the given NPC UUID.
   *
   * @param npcUuid           the UUID of the NPC whose dialogues are to be deleted
   * @param deletedByPlayerId the database ID of the player performing this operation
   */
  public void deleteAllDialoguesByNpcUuid(UUID npcUuid, int deletedByPlayerId) {
    npcDao.deleteNpcDialogueByNpcId(npcUuid, deletedByPlayerId);
  }
}