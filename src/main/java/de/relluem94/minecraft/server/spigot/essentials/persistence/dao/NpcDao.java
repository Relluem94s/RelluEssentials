package de.relluem94.minecraft.server.spigot.essentials.persistence.dao;

import de.relluem94.minecraft.server.spigot.essentials.models.pojo.NpcDialogueEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.NpcEntry;
import de.relluem94.minecraft.server.spigot.essentials.persistence.dao.mapper.NpcDialogueMapper;
import de.relluem94.minecraft.server.spigot.essentials.persistence.dao.mapper.NpcMapper;
import de.relluem94.minecraft.server.spigot.essentials.persistence.jdbc.QueryExecutor;
import java.util.List;
import java.util.UUID;

/**
 * Data Access Object for managing NPC and NPC dialogue persistence operations.
 *
 * @author rellu
 */
public class NpcDao {

  private final QueryExecutor queryExecutor;

  /**
   * Creates a new {@code NpcDao} with the given {@link QueryExecutor}.
   *
   * @param queryExecutor the executor used to run SQL queries and updates
   */
  public NpcDao(QueryExecutor queryExecutor) {
    this.queryExecutor = queryExecutor;
  }

  /**
   * Retrieves all NPC entries from the database.
   *
   * @return a list of all {@link NpcEntry} records
   */
  public List<NpcEntry> findAll() {
    return queryExecutor.queryList("getCustomNPCs.sql",
        _ -> {},
        NpcMapper::mapNpc);
  }

  /**
   * Retrieves a single NPC entry identified by its UUID.
   *
   * @param uuid the UUID of the NPC to retrieve
   * @return the matching {@link NpcEntry}, or {@code null} if no match is found
   */
  public NpcEntry findByUuid(UUID uuid) {
    return queryExecutor.querySingle("getCustomNPCByUuid.sql",
        ps -> ps.setString(1, uuid.toString()),
        NpcMapper::mapNpc);
  }

  /**
   * Retrieves all dialogue entries associated with the given NPC database ID.
   *
   * @param npcId the database ID of the NPC whose dialogues are to be retrieved
   * @return a list of {@link NpcDialogueEntry} records belonging to the specified NPC
   */
  public List<NpcDialogueEntry> findDialoguesByNpcId(int npcId) {
    return queryExecutor.queryList("getCustomNPCDialoguesByNpcId.sql",
        ps -> ps.setInt(1, npcId),
        NpcDialogueMapper::mapNPCDialogue);
  }

  /**
   * Inserts a new NPC entry into the database and returns the generated primary key.
   *
   * @param npcEntry the {@link NpcEntry} containing the data to persist
   * @return the generated database ID of the newly inserted NPC
   */
  public int insertNpc(NpcEntry npcEntry) {
    return queryExecutor.executeInsertWithGeneratedKey("insertCustomNPC.sql", ps -> {
      ps.setString(1, npcEntry.getUuid().toString());
      ps.setString(2, npcEntry.getProfileName());
      ps.setString(3, npcEntry.getInventory() != null ? npcEntry.getInventory().toString() : null);
      ps.setString(4, npcEntry.getWorld());
      ps.setDouble(5, npcEntry.getX());
      ps.setDouble(6, npcEntry.getY());
      ps.setDouble(7, npcEntry.getZ());
      ps.setFloat(8, npcEntry.getYaw());
      ps.setFloat(9, npcEntry.getPitch());
      ps.setInt(10, npcEntry.getCreatedBy());
    });
  }

  /**
   * Updates an existing NPC entry in the database.
   *
   * @param npcEntry the {@link NpcEntry} containing the updated data; the entry's ID is used to
   *                 identify the record to update
   */
  public void updateNpc(NpcEntry npcEntry) {
    queryExecutor.executeUpdate("updateCustomNPC.sql", ps -> {
      ps.setString(1,
          npcEntry.getEntityUuid() != null ? npcEntry.getEntityUuid().toString() : null);
      ps.setString(2, npcEntry.getProfileName());
      ps.setString(3, npcEntry.getInventory() != null ? npcEntry.getInventory().toString() : null);
      ps.setString(4, npcEntry.getWorld());
      ps.setDouble(5, npcEntry.getX());
      ps.setDouble(6, npcEntry.getY());
      ps.setDouble(7, npcEntry.getZ());
      ps.setFloat(8, npcEntry.getYaw());
      ps.setFloat(9, npcEntry.getPitch());
      ps.setInt(10, npcEntry.getUpdatedBy());
      ps.setInt(11, npcEntry.getId());
    });
  }

  /**
   * Soft-deletes an NPC entry identified by its UUID,
   * recording which player performed the deletion.
   *
   * @param npcUuid          the UUID of the NPC to delete
   * @param deletedByPlayerId the database ID of the player who performed the deletion
   */
  public void deleteNpc(UUID npcUuid, int deletedByPlayerId) {
    queryExecutor.executeUpdate("deleteCustomNPC.sql", ps -> {
      ps.setInt(1, deletedByPlayerId);
      ps.setString(2, npcUuid.toString());
    });
  }

  /**
   * Inserts a new NPC dialogue entry into the database.
   *
   * @param entry the {@link NpcDialogueEntry} containing the dialogue data to persist
   */
  public void insertNpcDialogue(NpcDialogueEntry entry) {
    queryExecutor.executeUpdate("insertCustomNPCDialogue.sql", ps -> {
      ps.setInt(1, entry.getCreatedBy());
      ps.setInt(2, entry.getListPosition());
      ps.setString(3, entry.getText());
      ps.setInt(4, entry.getNpcFk());
    });
  }

  /**
   * Updates an existing NPC dialogue entry identified by its UUID and list position.
   *
   * @param entry the {@link NpcDialogueEntry} containing the updated dialogue data
   * @param uuid  the UUID of the dialogue entry to update
   * @return {@code true} if at least one row was affected; {@code false} otherwise
   */
  public boolean updateNpcDialogue(NpcDialogueEntry entry, UUID uuid) {
    int affectedRows = queryExecutor.executeUpdateWithCount("updateCustomNPCDialogue.sql", ps -> {
      ps.setInt(1, entry.getUpdatedBy());
      ps.setString(2, entry.getText());
      ps.setString(3, uuid.toString());
      ps.setInt(4, entry.getListPosition());
    });
    return affectedRows > 0;
  }

  /**
   * Soft-deletes a specific NPC dialogue entry identified by the NPC UUID and its list position,
   * recording which player performed the deletion.
   *
   * @param npcUuid           the UUID of the NPC whose dialogue entry is to be deleted
   * @param listPosition      the position of the dialogue entry within the NPC's dialogue list
   * @param deletedByPlayerId the database ID of the player who performed the deletion
   */
  public void deleteNpcDialogueById(UUID npcUuid, int listPosition, int deletedByPlayerId) {
    queryExecutor.executeUpdate("deleteCustomNPCDialogueById.sql", ps -> {
      ps.setInt(1, deletedByPlayerId);
      ps.setString(2, npcUuid.toString());
      ps.setInt(3, listPosition);
    });
  }

  /**
   * Soft-deletes all dialogue entries belonging to the NPC identified by the given UUID,
   * recording which player performed the deletion.
   *
   * @param npcUuid           the UUID of the NPC whose dialogues are to be deleted
   * @param deletedByPlayerId the database ID of the player who performed the deletion
   */
  public void deleteNpcDialogueByNpcId(UUID npcUuid, int deletedByPlayerId) {
    queryExecutor.executeUpdate("deleteCustomNPCDialogueByNpcId.sql", ps -> {
      ps.setInt(1, deletedByPlayerId);
      ps.setString(2, npcUuid.toString());
    });
  }
}