package de.relluem94.minecraft.server.spigot.essentials.helpers.db.mapper;

import de.relluem94.minecraft.server.spigot.essentials.model.pojo.NPCDialogueEntry;

import java.sql.ResultSet;
import java.sql.SQLException;

import static de.relluem94.minecraft.server.spigot.essentials.constants.db.DatabaseMappings.*;

public class NPCDialogueMapper {

    private NPCDialogueMapper() {}

    public static NPCDialogueEntry mapNPCDialogue(ResultSet rs) throws SQLException {
        NPCDialogueEntry entry = new NPCDialogueEntry();
        entry.setId(rs.getInt(FIELD_ID));
        entry.setListPosition(rs.getInt(FIELD_LIST_POSITION));
        entry.setText(rs.getString(FIELD_TEXT));
        entry.setNpcFk(rs.getInt(FIELD_CUSTOM_NPC_FK));
        entry.setCreatedBy(rs.getInt(FIELD_CREATEDBY));

        String updatedBy = rs.getString(FIELD_UPDATEDBY);
        if (updatedBy != null) {
            entry.setUpdatedBy(rs.getInt(FIELD_UPDATEDBY));
        }

        return entry;
    }
}