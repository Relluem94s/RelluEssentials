package de.relluem94.minecraft.server.spigot.essentials.helpers.dbmapper;

import de.relluem94.minecraft.server.spigot.essentials.constants.Constants;
import de.relluem94.minecraft.server.spigot.essentials.constants.DatabaseMappings;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.WorldEntry;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.WorldGroupEntry;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.WorldGroupInventoryEntry;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.WorldGroupSettingEntry;
import org.json.JSONObject;
import org.jspecify.annotations.NonNull;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

import static de.relluem94.minecraft.server.spigot.essentials.constants.DatabaseMappings.*;

public class WorldMapper {
    private WorldMapper() {
        throw new IllegalStateException(Constants.PLUGIN_INTERNAL_UTILITY_CLASS);
    }

    public static @NonNull WorldEntry mapWorld(@NonNull ResultSet rs) throws SQLException {
        WorldEntry worldEntry = new WorldEntry();
        worldEntry.setId(rs.getInt(DatabaseMappings.FIELD_ID));
        worldEntry.setCreated(rs.getString(DatabaseMappings.FIELD_CREATED));
        worldEntry.setCreatedBy(rs.getInt(DatabaseMappings.FIELD_CREATEDBY));
        worldEntry.setUpdated(rs.getString(DatabaseMappings.FIELD_UPDATED));
        worldEntry.setUpdatedBy(rs.getInt(DatabaseMappings.FIELD_UPDATEDBY));
        worldEntry.setDeleted(rs.getString(DatabaseMappings.FIELD_DELETED));
        worldEntry.setDeletedBy(rs.getInt(DatabaseMappings.FIELD_DELETEDBY));
        worldEntry.setName(rs.getString(DatabaseMappings.FIELD_NAME));
        return worldEntry;
    }

    public static @NonNull WorldGroupEntry mapWorldGroup(@NonNull ResultSet rs, @NonNull List<WorldGroupSettingEntry> groupSettings) throws SQLException {
        WorldGroupEntry worldGroupEntry = new WorldGroupEntry();
        worldGroupEntry.setId(rs.getInt(FIELD_ID));
        worldGroupEntry.setCreated(rs.getString(FIELD_CREATED));
        worldGroupEntry.setCreatedBy(rs.getInt(FIELD_CREATEDBY));
        worldGroupEntry.setUpdated(rs.getString(FIELD_UPDATED));
        worldGroupEntry.setUpdatedBy(rs.getInt(FIELD_UPDATEDBY));
        worldGroupEntry.setDeleted(rs.getString(FIELD_DELETED));
        worldGroupEntry.setDeletedBy(rs.getInt(FIELD_DELETEDBY));
        worldGroupEntry.setName(rs.getString(FIELD_NAME));
        worldGroupEntry.setSettings(groupSettings.stream()
                .filter(s -> s.getWorldGroupEntryFk() == worldGroupEntry.getId())
                .collect(Collectors.toList()));
        return worldGroupEntry;
    }

    public static @NonNull WorldGroupInventoryEntry mapWorldGroupInventory(@NonNull ResultSet rs) throws SQLException {
        WorldGroupInventoryEntry worldGroupInventoryEntry = new WorldGroupInventoryEntry();
        worldGroupInventoryEntry.setId(rs.getInt(FIELD_ID));
        worldGroupInventoryEntry.setCreated(rs.getString(FIELD_CREATED));
        worldGroupInventoryEntry.setCreatedBy(rs.getInt(FIELD_CREATEDBY));
        worldGroupInventoryEntry.setUpdated(rs.getString(FIELD_UPDATED));
        worldGroupInventoryEntry.setUpdatedBy(rs.getInt(FIELD_UPDATEDBY));
        worldGroupInventoryEntry.setDeleted(rs.getString(FIELD_DELETED));
        worldGroupInventoryEntry.setDeletedBy(rs.getInt(FIELD_DELETEDBY));
        worldGroupInventoryEntry.setPlayerId(rs.getInt(FIELD_PLAYER_FK));
        worldGroupInventoryEntry.setHealth(rs.getInt(FIELD_HEALTH));
        worldGroupInventoryEntry.setTotalExperience(rs.getInt(FIELD_TOTAL_EXPERIENCE));
        worldGroupInventoryEntry.setFoodLevel(rs.getInt(FIELD_FOOD));
        worldGroupInventoryEntry.setInventory(new JSONObject(rs.getString(FIELD_INVENTORY)));
        return worldGroupInventoryEntry;
    }

}
