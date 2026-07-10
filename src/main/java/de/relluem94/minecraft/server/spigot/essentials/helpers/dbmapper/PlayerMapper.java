package de.relluem94.minecraft.server.spigot.essentials.helpers.dbmapper;

import de.relluem94.minecraft.server.spigot.essentials.constants.Constants;
import de.relluem94.minecraft.server.spigot.essentials.constants.PlayerState;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.GroupEntry;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.PlayerEntry;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.PlayerPartnerEntry;
import de.relluem94.minecraft.server.spigot.essentials.permissions.Groups;

import static de.relluem94.minecraft.server.spigot.essentials.constants.DatabaseMappings.*;

import org.jspecify.annotations.NonNull;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PlayerMapper {
    private PlayerMapper() {
        throw new IllegalStateException(Constants.PLUGIN_INTERNAL_UTILITY_CLASS);
    }

    public static @NonNull PlayerEntry mapPlayer(@NonNull ResultSet rs) throws SQLException {
        PlayerEntry playerEntry = new PlayerEntry();
        playerEntry.setId(rs.getInt(FIELD_ID));
        playerEntry.setUuid(rs.getString(FIELD_UUID));
        playerEntry.setCreated(rs.getString(FIELD_CREATED));
        playerEntry.setCreatedBy(rs.getInt(FIELD_CREATEDBY));
        playerEntry.setUpdated(rs.getString(FIELD_UPDATED));
        playerEntry.setUpdatedBy(rs.getInt(FIELD_UPDATEDBY));
        playerEntry.setDeleted(rs.getString(FIELD_DELETED));
        playerEntry.setDeletedBy(rs.getInt(FIELD_DELETEDBY));
        playerEntry.setName(rs.getString(FIELD_NAME));
        playerEntry.setCustomName(rs.getString(FIELD_CUSTOM_NAME));
        playerEntry.setPurse(rs.getDouble(FIELD_PURSE));
        playerEntry.setFlying(rs.getBoolean(FIELD_FLY));
        playerEntry.setAfk(rs.getBoolean(FIELD_AFK));
        playerEntry.setGroup(Groups.getGroup(rs.getInt(FIELD_GROUP_FK)));
        playerEntry.setPlayerState(PlayerState.DEFAULT);
        return playerEntry;
    }

    public static @NonNull PlayerPartnerEntry mapPlayerPartner(@NonNull ResultSet rs) throws SQLException {
        PlayerPartnerEntry playerPartnerEntry = new PlayerPartnerEntry();
        playerPartnerEntry.setId(rs.getInt(FIELD_ID));
        playerPartnerEntry.setCreated(rs.getString(FIELD_CREATED));
        playerPartnerEntry.setCreatedBy(rs.getInt(FIELD_CREATEDBY));
        playerPartnerEntry.setUpdated(rs.getString(FIELD_UPDATED));
        playerPartnerEntry.setUpdatedBy(rs.getInt(FIELD_UPDATEDBY));
        playerPartnerEntry.setDeleted(rs.getString(FIELD_DELETED));
        playerPartnerEntry.setDeletedBy(rs.getInt(FIELD_DELETEDBY));
        playerPartnerEntry.setFirstPartnerId(rs.getInt(FIELD_FIRST_PARTNER_FK));
        playerPartnerEntry.setSecondPartnerId(rs.getInt(FIELD_SECOND_PARTNER_FK));
        playerPartnerEntry.setShareProtections(rs.getBoolean(FIELD_SHARE_PROTECTIONS));
        return playerPartnerEntry;
    }

    public static @NonNull GroupEntry mapGroup(@NonNull ResultSet rs) throws SQLException {
        GroupEntry g = new GroupEntry();
        g.setId(rs.getInt(FIELD_ID));
        g.setName(rs.getString(FIELD_NAME));
        g.setPrefix(rs.getString(FIELD_PREFIX));
        return g;
    }
}
