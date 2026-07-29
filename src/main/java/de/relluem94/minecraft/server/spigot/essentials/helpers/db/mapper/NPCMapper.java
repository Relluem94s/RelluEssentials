package de.relluem94.minecraft.server.spigot.essentials.helpers.db.mapper;

import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.NPCDialogueEntry;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.NPCEntry;
import de.relluem94.minecraft.server.spigot.essentials.npc.NPC;
import org.json.JSONObject;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

import static de.relluem94.minecraft.server.spigot.essentials.constants.DatabaseMappings.*;

public class NPCMapper {

    private NPCMapper() {}

    public static NPCEntry mapNPC(ResultSet rs) throws SQLException {
        NPCEntry entry = new NPCEntry();
        entry.setId(rs.getInt(FIELD_ID));
        entry.setUuid(UUID.fromString(rs.getString(FIELD_UUID)));
        entry.setProfileName(rs.getString(FIELD_PROFILE_NAME));
        entry.setInventory(rs.getString(FIELD_INVENTORY) == null ? null:new JSONObject(rs.getString(FIELD_INVENTORY)));
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

    public static NPCEntry toEntry(NPC npc, int actorPlayerId) {
        NPCEntry entry = new NPCEntry();
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

    public static NPC toDomain(NPCEntry entry) {
        NPC npc = new NPC(entry.getId(), entry.getUuid(), entry.getProfileName(), entry.getX(), entry.getY(), entry.getZ(), entry.getWorld());
        npc.setEntityUUID(entry.getEntityUuid());
        npc.setInventory(entry.getInventory());
        return npc;
    }

    public static NPC toDomain(NPCEntry entry, List<NPCDialogueEntry> dialogueLines) {
        NPC npc = toDomain(entry);
        npc.setDialogueLines(dialogueLines);
        return npc;
    }
}