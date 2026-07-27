package de.relluem94.minecraft.server.spigot.essentials.helpers.db.mapper;

import de.relluem94.minecraft.server.spigot.essentials.constants.Constants;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.TraderNPCEntry;
import de.relluem94.minecraft.server.spigot.essentials.npc.trader.TraderNPC;
import org.bukkit.entity.Villager;
import org.jspecify.annotations.NonNull;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Function;

import static de.relluem94.minecraft.server.spigot.essentials.constants.DatabaseMappings.*;

public class TraderNPCMapper {
    private TraderNPCMapper() {
        throw new IllegalStateException(Constants.PLUGIN_INTERNAL_UTILITY_CLASS);
    }

    public static @NonNull TraderNPCEntry mapNPC(@NonNull ResultSet rs, @NonNull Function<String, Villager.Profession> professionResolver) throws SQLException {
        TraderNPCEntry traderNpcEntry = new TraderNPCEntry();

        traderNpcEntry.setId(rs.getInt(FIELD_ID));
        traderNpcEntry.setCreated(rs.getString(FIELD_CREATED));
        traderNpcEntry.setCreatedBy(rs.getInt(FIELD_CREATEDBY));
        traderNpcEntry.setUpdated(rs.getString(FIELD_UPDATED));
        traderNpcEntry.setUpdatedBy(rs.getInt(FIELD_UPDATEDBY));
        traderNpcEntry.setDeleted(rs.getString(FIELD_DELETED));
        traderNpcEntry.setDeletedBy(rs.getInt(FIELD_DELETEDBY));

        traderNpcEntry.setName(rs.getString(FIELD_NAME));
        traderNpcEntry.setProfession(professionResolver.apply(rs.getString(FIELD_PROFESSION).toLowerCase()));
        traderNpcEntry.setType(TraderNPC.Type.valueOf(rs.getString(FIELD_TYPE)));

        for (int i = 0; i <= 27; i++) {
            traderNpcEntry.setSlotName(i, rs.getString(String.format(FIELD_SLOT_VAR_NAME, (i + 1))));
        }
        return traderNpcEntry;
    }
}
