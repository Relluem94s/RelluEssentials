package de.relluem94.minecraft.server.spigot.essentials.helpers.db.mapper;

import de.relluem94.minecraft.server.spigot.essentials.constants.Constants;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.NPCEntry;
import de.relluem94.minecraft.server.spigot.essentials.npc.NPC;
import org.bukkit.entity.Villager;
import org.jspecify.annotations.NonNull;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Function;

import static de.relluem94.minecraft.server.spigot.essentials.constants.DatabaseMappings.*;

public class NPCMapper {
    private NPCMapper() {
        throw new IllegalStateException(Constants.PLUGIN_INTERNAL_UTILITY_CLASS);
    }

    public static @NonNull NPCEntry mapNPC(@NonNull ResultSet rs, @NonNull Function<String, Villager.Profession> professionResolver) throws SQLException {
        NPCEntry npcEntry = new NPCEntry();

        npcEntry.setId(rs.getInt(FIELD_ID));
        npcEntry.setCreated(rs.getString(FIELD_CREATED));
        npcEntry.setCreatedBy(rs.getInt(FIELD_CREATEDBY));
        npcEntry.setUpdated(rs.getString(FIELD_UPDATED));
        npcEntry.setUpdatedBy(rs.getInt(FIELD_UPDATEDBY));
        npcEntry.setDeleted(rs.getString(FIELD_DELETED));
        npcEntry.setDeletedBy(rs.getInt(FIELD_DELETEDBY));

        npcEntry.setName(rs.getString(FIELD_NAME));
        npcEntry.setProfession(professionResolver.apply(rs.getString(FIELD_PROFESSION).toLowerCase()));
        npcEntry.setType(NPC.Type.valueOf(rs.getString(FIELD_TYPE)));

        for (int i = 0; i <= 27; i++) {
            npcEntry.setSlotName(i, rs.getString(String.format(FIELD_SLOT_VAR_NAME, (i + 1))));
        }
        return npcEntry;
    }
}
