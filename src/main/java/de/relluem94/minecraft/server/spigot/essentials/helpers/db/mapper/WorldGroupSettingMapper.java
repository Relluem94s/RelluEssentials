package de.relluem94.minecraft.server.spigot.essentials.helpers.db.mapper;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.SettingEntry;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.WorldGroupSettingEntry;
import org.jspecify.annotations.NonNull;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import static de.relluem94.minecraft.server.spigot.essentials.constants.DatabaseMappings.*;

public class WorldGroupSettingMapper {
    public static @NonNull WorldGroupSettingEntry mapWorldGroupSetting(@NonNull ResultSet rs) throws SQLException {
        WorldGroupSettingEntry wgse = new WorldGroupSettingEntry();

        wgse.setId(rs.getInt(FIELD_ID));
        wgse.setCreated(rs.getString(FIELD_CREATED));
        wgse.setCreatedBy(rs.getInt(FIELD_CREATEDBY));
        wgse.setUpdated(rs.getString(FIELD_UPDATED));
        wgse.setUpdatedBy(rs.getInt(FIELD_UPDATEDBY));
        wgse.setValue(rs.getBoolean(FIELD_VALUE));
        wgse.setWorldGroupEntryFk(rs.getInt(FIELD_WORLD_GORUP_FK));
        int settingFk = rs.getInt(FIELD_SETTING_FK);
        wgse.setSettingEntryFk(settingFk);

        Optional<SettingEntry> settingEntry = RelluEssentials.settingEntriesList.stream()
                .filter(se -> se.getId() == settingFk)
                .findFirst();
        settingEntry.ifPresent(wgse::setSettingEntry);

        return wgse;
    }
}