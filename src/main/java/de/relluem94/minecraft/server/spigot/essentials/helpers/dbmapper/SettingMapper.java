package de.relluem94.minecraft.server.spigot.essentials.helpers.dbmapper;

import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.SettingEntry;
import org.jspecify.annotations.NonNull;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class SettingMapper {
    public static @NonNull SettingEntry mapSetting(@NonNull ResultSet rs) throws SQLException {
        Timestamp updated = rs.getTimestamp("UPDATED");

        return new SettingEntry(
                rs.getInt("ID"),
                rs.getTimestamp("CREATED").toLocalDateTime(),
                rs.getInt("CREATEDBY"),
                updated != null ? updated.toLocalDateTime() : null,
                rs.getObject("UPDATEDBY", Integer.class),
                rs.getString("name")
        );
    }
}