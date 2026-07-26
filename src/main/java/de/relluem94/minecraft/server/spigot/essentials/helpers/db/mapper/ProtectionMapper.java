package de.relluem94.minecraft.server.spigot.essentials.helpers.db.mapper;

import de.relluem94.minecraft.server.spigot.essentials.constants.Constants;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.ProtectionEntry;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.ProtectionLockEntry;
import org.bukkit.Material;
import org.json.JSONObject;
import org.jspecify.annotations.NonNull;

import java.sql.ResultSet;
import java.sql.SQLException;

import static de.relluem94.minecraft.server.spigot.essentials.constants.DatabaseMappings.*;

public class ProtectionMapper {
    private ProtectionMapper() {
        throw new IllegalStateException(Constants.PLUGIN_INTERNAL_UTILITY_CLASS);
    }

    public static @NonNull ProtectionEntry mapProtection(@NonNull ResultSet rs) throws SQLException {
        ProtectionEntry pe = new ProtectionEntry();
        pe.setId(rs.getInt(FIELD_ID));
        pe.setCreated(rs.getString(FIELD_CREATED));
        pe.setCreatedBy(rs.getInt(FIELD_CREATEDBY));
        pe.setUpdated(rs.getString(FIELD_UPDATED));
        pe.setUpdatedBy(rs.getInt(FIELD_UPDATEDBY));
        pe.setDeleted(rs.getString(FIELD_DELETED));
        pe.setDeletedBy(rs.getInt(FIELD_DELETEDBY));
        String flagsJson = rs.getString(FIELD_FLAGS);
        pe.setFlags(flagsJson != null ? new JSONObject(flagsJson) : new JSONObject());
        String rightsJson = rs.getString(FIELD_RIGHTS);
        pe.setRights(rightsJson != null ? new JSONObject(rightsJson) : new JSONObject());
        pe.setMaterialName(rs.getString(FIELD_MATERIAL_NAME));
        return pe;
    }

    public static @NonNull ProtectionLockEntry mapProtectionLock(@NonNull ResultSet rs) throws SQLException {
        ProtectionLockEntry protectionLockEntry = new ProtectionLockEntry();
        protectionLockEntry.setId(rs.getInt(FIELD_ID));
        protectionLockEntry.setCreated(rs.getString(FIELD_CREATED));
        protectionLockEntry.setCreatedBy(rs.getInt(FIELD_CREATEDBY));
        protectionLockEntry.setUpdated(rs.getString(FIELD_UPDATED));
        protectionLockEntry.setUpdatedBy(rs.getInt(FIELD_UPDATEDBY));
        protectionLockEntry.setDeleted(rs.getString(FIELD_DELETED));
        protectionLockEntry.setDeletedBy(rs.getInt(FIELD_DELETEDBY));
        protectionLockEntry.setValue(Material.getMaterial(rs.getString(FIELD_VALUE)));
        return protectionLockEntry;
    }
}
