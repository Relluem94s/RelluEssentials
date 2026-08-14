package de.relluem94.minecraft.server.spigot.essentials.persistence.dao.mapper;

import static de.relluem94.minecraft.server.spigot.essentials.constants.db.DatabaseMappings.FIELD_CREATED;
import static de.relluem94.minecraft.server.spigot.essentials.constants.db.DatabaseMappings.FIELD_CREATEDBY;
import static de.relluem94.minecraft.server.spigot.essentials.constants.db.DatabaseMappings.FIELD_DELETED;
import static de.relluem94.minecraft.server.spigot.essentials.constants.db.DatabaseMappings.FIELD_DELETEDBY;
import static de.relluem94.minecraft.server.spigot.essentials.constants.db.DatabaseMappings.FIELD_FLAGS;
import static de.relluem94.minecraft.server.spigot.essentials.constants.db.DatabaseMappings.FIELD_ID;
import static de.relluem94.minecraft.server.spigot.essentials.constants.db.DatabaseMappings.FIELD_LOCATION_FK;
import static de.relluem94.minecraft.server.spigot.essentials.constants.db.DatabaseMappings.FIELD_MATERIAL_NAME;
import static de.relluem94.minecraft.server.spigot.essentials.constants.db.DatabaseMappings.FIELD_RIGHTS;
import static de.relluem94.minecraft.server.spigot.essentials.constants.db.DatabaseMappings.FIELD_UPDATED;
import static de.relluem94.minecraft.server.spigot.essentials.constants.db.DatabaseMappings.FIELD_UPDATEDBY;
import static de.relluem94.minecraft.server.spigot.essentials.constants.db.DatabaseMappings.FIELD_VALUE;

import de.relluem94.minecraft.server.spigot.essentials.constants.Constants;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.ProtectionEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.ProtectionLockEntry;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.bukkit.Material;
import org.json.JSONObject;
import org.jspecify.annotations.NonNull;

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
    pe.setLocationFk(rs.getInt(FIELD_LOCATION_FK));
    String flagsJson = rs.getString(FIELD_FLAGS);
    pe.setFlags(flagsJson != null ? new JSONObject(flagsJson) : new JSONObject());
    String rightsJson = rs.getString(FIELD_RIGHTS);
    pe.setRights(rightsJson != null ? new JSONObject(rightsJson) : new JSONObject());
    pe.setMaterialName(rs.getString(FIELD_MATERIAL_NAME));
    return pe;
  }

  public static @NonNull ProtectionLockEntry mapProtectionLock(@NonNull ResultSet rs)
      throws SQLException {
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
