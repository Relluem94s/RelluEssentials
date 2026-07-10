package de.relluem94.minecraft.server.spigot.essentials.helpers.dbmapper;

import de.relluem94.minecraft.server.spigot.essentials.constants.Constants;
import de.relluem94.minecraft.server.spigot.essentials.constants.DatabaseMappings;
import de.relluem94.minecraft.server.spigot.essentials.helpers.BagHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.BagEntry;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.BagTypeEntry;
import org.jspecify.annotations.NonNull;

import java.sql.ResultSet;
import java.sql.SQLException;

import static de.relluem94.minecraft.server.spigot.essentials.constants.DatabaseMappings.*;

public class BagMapper {
    private BagMapper() {
        throw new IllegalStateException(Constants.PLUGIN_INTERNAL_UTILITY_CLASS);
    }

    public static @NonNull BagEntry mapBag(@NonNull ResultSet rs) throws SQLException {
        BagEntry bagEntry = new BagEntry();
        bagEntry.setId(rs.getInt(FIELD_ID));
        bagEntry.setCreated(rs.getString(FIELD_CREATED));
        bagEntry.setCreatedBy(rs.getInt(FIELD_CREATEDBY));
        bagEntry.setUpdated(rs.getString(FIELD_UPDATED));
        bagEntry.setUpdatedBy(rs.getInt(FIELD_UPDATEDBY));
        bagEntry.setDeleted(rs.getString(FIELD_DELETED));
        bagEntry.setDeletedBy(rs.getInt(FIELD_DELETEDBY));
        bagEntry.setPlayerId(rs.getInt(FIELD_PLAYER_FK));
        bagEntry.setBagTypeId(rs.getInt(FIELD_BAG_TYPE_FK));
        for (int i = 0; i <= BagHelper.BAG_SIZE-1; i++) {
            bagEntry.setSlotValue(i, rs.getInt(String.format(DatabaseMappings.FIELD_SLOT_VAR_VALUE, (i + 1))));
        }
        return bagEntry;
    }

    public static @NonNull BagTypeEntry mapBagType(@NonNull ResultSet rs) throws SQLException {
        BagTypeEntry bagTypeEntry = new BagTypeEntry();
        bagTypeEntry.setId(rs.getInt(FIELD_ID));
        bagTypeEntry.setDisplayName(rs.getString(FIELD_DISPLAY_NAME));
        bagTypeEntry.setName(rs.getString(FIELD_NAME));
        bagTypeEntry.setCost(rs.getInt(FIELD_COST));
        for (int i = 0; i <= BagHelper.BAG_SIZE-1; i++) {
            bagTypeEntry.setSlotName(i, rs.getString(String.format(DatabaseMappings.FIELD_SLOT_VAR_NAME, (i + 1))));
        }
        return bagTypeEntry;
    }
}
