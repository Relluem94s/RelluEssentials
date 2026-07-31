package de.relluem94.minecraft.server.spigot.essentials.helpers.db.mapper;

import static de.relluem94.minecraft.server.spigot.essentials.constants.db.DatabaseMappings.FIELD_CREATEDBY;
import static de.relluem94.minecraft.server.spigot.essentials.constants.db.DatabaseMappings.FIELD_CUSTOM_NPC_FK;
import static de.relluem94.minecraft.server.spigot.essentials.constants.db.DatabaseMappings.FIELD_ID;
import static de.relluem94.minecraft.server.spigot.essentials.constants.db.DatabaseMappings.FIELD_LIST_POSITION;
import static de.relluem94.minecraft.server.spigot.essentials.constants.db.DatabaseMappings.FIELD_TEXT;
import static de.relluem94.minecraft.server.spigot.essentials.constants.db.DatabaseMappings.FIELD_UPDATEDBY;

import de.relluem94.minecraft.server.spigot.essentials.constants.Constants;
import de.relluem94.minecraft.server.spigot.essentials.model.pojo.NpcDialogueEntry;
import java.sql.ResultSet;
import java.sql.SQLException;

public class NpcDialogueMapper {

  private NpcDialogueMapper() {throw new IllegalStateException(Constants.PLUGIN_INTERNAL_UTILITY_CLASS);}

  public static NpcDialogueEntry mapNPCDialogue(ResultSet rs) throws SQLException {
    NpcDialogueEntry entry = new NpcDialogueEntry();
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