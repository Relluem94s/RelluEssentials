package de.relluem94.minecraft.server.spigot.essentials.helpers.dbmapper;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.SettingEntry;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.WorldGroupSettingEntry;
import org.jspecify.annotations.NonNull;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class WorldGroupSettingMapper {
    public static @NonNull WorldGroupSettingEntry mapWorldGroupSetting(@NonNull ResultSet rs) throws SQLException {
        WorldGroupSettingEntry wgse = new WorldGroupSettingEntry();

        wgse.setId(rs.getInt("ID"));
        wgse.setCreated(rs.getString("CREATED"));
        wgse.setCreatedBy(rs.getInt("CREATEDBY"));
        wgse.setUpdated(rs.getString("UPDATED"));
        wgse.setUpdatedBy(rs.getInt("UPDATEDBY"));
        wgse.setValue(rs.getBoolean("value"));

        int settingFk = rs.getInt("setting_fk");
        Optional<SettingEntry> settingEntry = RelluEssentials.settingEntriesList.stream()
                .filter(se -> se.getId() == settingFk)
                .findFirst();
        settingEntry.ifPresent(wgse::setSettingEntry);

        return wgse;
    }
}