package de.relluem94.minecraft.server.spigot.essentials.helpers.db.mapper;

import static de.relluem94.minecraft.server.spigot.essentials.constants.db.DatabaseMappings.FIELD_CREATEDBY;
import static de.relluem94.minecraft.server.spigot.essentials.constants.db.DatabaseMappings.FIELD_ENTITY_UUID;
import static de.relluem94.minecraft.server.spigot.essentials.constants.db.DatabaseMappings.FIELD_ID;
import static de.relluem94.minecraft.server.spigot.essentials.constants.db.DatabaseMappings.FIELD_INVENTORY;
import static de.relluem94.minecraft.server.spigot.essentials.constants.db.DatabaseMappings.FIELD_POS_X;
import static de.relluem94.minecraft.server.spigot.essentials.constants.db.DatabaseMappings.FIELD_POS_Y;
import static de.relluem94.minecraft.server.spigot.essentials.constants.db.DatabaseMappings.FIELD_POS_Z;
import static de.relluem94.minecraft.server.spigot.essentials.constants.db.DatabaseMappings.FIELD_PROFILE_NAME;
import static de.relluem94.minecraft.server.spigot.essentials.constants.db.DatabaseMappings.FIELD_UPDATED;
import static de.relluem94.minecraft.server.spigot.essentials.constants.db.DatabaseMappings.FIELD_UPDATEDBY;
import static de.relluem94.minecraft.server.spigot.essentials.constants.db.DatabaseMappings.FIELD_UUID;
import static de.relluem94.minecraft.server.spigot.essentials.constants.db.DatabaseMappings.FIELD_WORLD;

import de.relluem94.minecraft.server.spigot.essentials.models.Npc;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.NpcDialogueEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.NpcEntry;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;
import org.json.JSONObject;

public class NpcMapper {

  private NpcMapper() {
  }

  public static NpcEntry mapNPC(ResultSet rs) throws SQLException {
    NpcEntry entry = new NpcEntry();
    entry.setId(rs.getInt(FIELD_ID));
    entry.setUuid(UUID.fromString(rs.getString(FIELD_UUID)));
    entry.setProfileName(rs.getString(FIELD_PROFILE_NAME));
    entry.setInventory(rs.getString(FIELD_INVENTORY) == null ? null
        : new JSONObject(rs.getString(FIELD_INVENTORY)));
    entry.setWorld(rs.getString(FIELD_WORLD));
    entry.setX(rs.getDouble(FIELD_POS_X));
    entry.setY(rs.getDouble(FIELD_POS_Y));
    entry.setZ(rs.getDouble(FIELD_POS_Z));
    entry.setCreatedBy(rs.getInt(FIELD_CREATEDBY));

    String entityUuid = rs.getString(FIELD_ENTITY_UUID);
    if (entityUuid != null) {
      entry.setEntityUuid(UUID.fromString(entityUuid));
    }

    String updatedBy = rs.getString(FIELD_UPDATEDBY);
    if (updatedBy != null) {
      entry.setUpdatedBy(rs.getInt(FIELD_UPDATED));
    }

    return entry;
  }

  public static NpcEntry toEntry(Npc npc, int actorPlayerId) {
    NpcEntry entry = new NpcEntry();
    entry.setUuid(npc.getId());
    entry.setProfileName(npc.getProfileName());
    entry.setInventory(npc.getInventory());
    entry.setWorld(npc.getWorldName());
    entry.setX(npc.getX());
    entry.setY(npc.getY());
    entry.setZ(npc.getZ());
    entry.setCreatedBy(actorPlayerId);
    entry.setUpdatedBy(actorPlayerId);
    if (npc.getEntityUUID() != null) {
      entry.setEntityUuid(npc.getEntityUUID());
    }
    return entry;
  }

  public static Npc toDomain(NpcEntry entry) {
    Npc npc = new Npc(entry.getId(), entry.getUuid(), entry.getProfileName(), entry.getX(),
        entry.getY(), entry.getZ(), entry.getWorld());
    npc.setEntityUUID(entry.getEntityUuid());
    npc.setInventory(entry.getInventory());
    return npc;
  }

  public static Npc toDomain(NpcEntry entry, List<NpcDialogueEntry> dialogueLines) {
    Npc npc = toDomain(entry);
    npc.setDialogueLines(dialogueLines);
    return npc;
  }
}