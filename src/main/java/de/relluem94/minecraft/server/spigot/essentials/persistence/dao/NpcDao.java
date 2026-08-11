package de.relluem94.minecraft.server.spigot.essentials.persistence.dao;

import de.relluem94.minecraft.server.spigot.essentials.helpers.db.mapper.NpcDialogueMapper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.db.mapper.NpcMapper;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.NpcDialogueEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.NpcEntry;
import de.relluem94.minecraft.server.spigot.essentials.persistence.jdbc.QueryExecutor;
import java.util.List;
import java.util.UUID;

public class NpcDao {

  private final QueryExecutor queryExecutor;

  public NpcDao(QueryExecutor queryExecutor) {
    this.queryExecutor = queryExecutor;
  }

  public List<NpcEntry> findAll() {
    return queryExecutor.queryList("getCustomNPCs.sql", _ -> {
    }, NpcMapper::mapNPC);
  }

  public NpcEntry findByUuid(UUID uuid) {
    return queryExecutor.querySingle("getCustomNPCByUuid.sql",
        ps -> ps.setString(1, uuid.toString()),
        NpcMapper::mapNPC);
  }

  public List<NpcDialogueEntry> findDialoguesByNpcId(int npcId) {
    return queryExecutor.queryList("getCustomNPCDialoguesByNpcId.sql",
        ps -> ps.setInt(1, npcId),
        NpcDialogueMapper::mapNPCDialogue);
  }

  public NpcEntry getNPC(UUID uuid) {
    return queryExecutor.querySingle("getCustomNPCByUuid.sql",
        ps -> ps.setString(1, uuid.toString()),
        NpcMapper::mapNPC);
  }

  public int insertNPC(NpcEntry npcEntry) {
    return queryExecutor.executeInsertWithGeneratedKey("insertCustomNPC.sql", ps -> {
      ps.setString(1, npcEntry.getUuid().toString());
      ps.setString(2, npcEntry.getProfileName());
      ps.setString(3, npcEntry.getInventory() != null ? npcEntry.getInventory().toString() : null);
      ps.setString(4, npcEntry.getWorld());
      ps.setDouble(5, npcEntry.getX());
      ps.setDouble(6, npcEntry.getY());
      ps.setDouble(7, npcEntry.getZ());
      ps.setInt(8, npcEntry.getCreatedBy());
    });
  }

  public void updateNPC(NpcEntry npcEntry) {
    queryExecutor.executeUpdate("updateCustomNPC.sql", ps -> {
      ps.setString(1,
          npcEntry.getEntityUuid() != null ? npcEntry.getEntityUuid().toString() : null);
      ps.setString(2, npcEntry.getProfileName());
      ps.setString(3, npcEntry.getInventory() != null ? npcEntry.getInventory().toString() : null);
      ps.setString(4, npcEntry.getWorld());
      ps.setDouble(5, npcEntry.getX());
      ps.setDouble(6, npcEntry.getY());
      ps.setDouble(7, npcEntry.getZ());
      ps.setInt(8, npcEntry.getUpdatedBy());
      ps.setInt(9, npcEntry.getId());
    });
  }

  public void deleteNPC(UUID npcUuid, int deletedByPlayerId) {
    queryExecutor.executeUpdate("deleteCustomNPC.sql", ps -> {
      ps.setInt(1, deletedByPlayerId);
      ps.setString(2, npcUuid.toString());
    });
  }


  public void insertNPCDialogue(NpcDialogueEntry entry) {
    queryExecutor.executeUpdate("insertCustomNPCDialogue.sql", ps -> {
      ps.setInt(1, entry.getCreatedBy());
      ps.setInt(2, entry.getListPosition());
      ps.setString(3, entry.getText());
      ps.setInt(4, entry.getNpcFk());
    });
  }

  public boolean updateNPCDialogue(NpcDialogueEntry entry, UUID uuid) {
    int affectedRows = queryExecutor.executeUpdateWithCount("updateCustomNPCDialogue.sql", ps -> {
      ps.setInt(1, entry.getUpdatedBy());
      ps.setString(2, entry.getText());
      ps.setString(3, uuid.toString());
      ps.setInt(4, entry.getListPosition());
    });
    return affectedRows > 0;
  }

  public void deleteNPCDialogueById(UUID npcUuid, int listPosition, int deletedByPlayerId) {
    queryExecutor.executeUpdate("deleteCustomNPCDialogueById.sql", ps -> {
      ps.setInt(1, deletedByPlayerId);
      ps.setString(2, npcUuid.toString());
      ps.setInt(3, listPosition);
    });
  }

  public void deleteNPCDialogueByNpcId(UUID npcUuid, int deletedByPlayerId) {
    queryExecutor.executeUpdate("deleteCustomNPCDialogueByNpcId.sql", ps -> {
      ps.setInt(1, deletedByPlayerId);
      ps.setString(2, npcUuid.toString());
    });
  }

  public List<NpcDialogueEntry> getNPCDialogues(int npcId) {
    return queryExecutor.queryList("getCustomNPCDialoguesByNpcId.sql", ps -> ps.setInt(1, npcId),
        NpcDialogueMapper::mapNPCDialogue);
  }



}