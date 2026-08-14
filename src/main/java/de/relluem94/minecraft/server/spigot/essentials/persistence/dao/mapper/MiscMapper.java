package de.relluem94.minecraft.server.spigot.essentials.persistence.dao.mapper;

import static de.relluem94.minecraft.server.spigot.essentials.constants.db.DatabaseMappings.FIELD_CREATED;
import static de.relluem94.minecraft.server.spigot.essentials.constants.db.DatabaseMappings.FIELD_CREATEDBY;
import static de.relluem94.minecraft.server.spigot.essentials.constants.db.DatabaseMappings.FIELD_DB_VERSION;
import static de.relluem94.minecraft.server.spigot.essentials.constants.db.DatabaseMappings.FIELD_ID;
import static de.relluem94.minecraft.server.spigot.essentials.constants.db.DatabaseMappings.FIELD_MATERIAL;
import static de.relluem94.minecraft.server.spigot.essentials.constants.db.DatabaseMappings.FIELD_MAX_INT;
import static de.relluem94.minecraft.server.spigot.essentials.constants.db.DatabaseMappings.FIELD_MIN_INT;
import static de.relluem94.minecraft.server.spigot.essentials.constants.db.DatabaseMappings.FIELD_MOTD_MESSAGE;
import static de.relluem94.minecraft.server.spigot.essentials.constants.db.DatabaseMappings.FIELD_MOTD_PLAYERS;
import static de.relluem94.minecraft.server.spigot.essentials.constants.db.DatabaseMappings.FIELD_PLANT;
import static de.relluem94.minecraft.server.spigot.essentials.constants.db.DatabaseMappings.FIELD_SEED;
import static de.relluem94.minecraft.server.spigot.essentials.constants.db.DatabaseMappings.FIELD_TAB_FOOTER;
import static de.relluem94.minecraft.server.spigot.essentials.constants.db.DatabaseMappings.FIELD_TAB_HEADER;

import de.relluem94.minecraft.server.spigot.essentials.constants.Constants;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.CropEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.DropEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.PluginInformationEntry;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.bukkit.Material;
import org.jspecify.annotations.NonNull;

public class MiscMapper {

  private MiscMapper() {
    throw new IllegalStateException(Constants.PLUGIN_INTERNAL_UTILITY_CLASS);
  }

  public static @NonNull PluginInformationEntry mapPluginInformation(@NonNull ResultSet rs)
      throws SQLException {
    PluginInformationEntry pluginInformationEntry = new PluginInformationEntry();
    pluginInformationEntry.setId(rs.getInt(FIELD_ID));
    pluginInformationEntry.setCreated(rs.getString(FIELD_CREATED));
    pluginInformationEntry.setCreatedBy(rs.getInt(FIELD_CREATEDBY));
    pluginInformationEntry.setTabHeader(rs.getString(FIELD_TAB_HEADER));
    pluginInformationEntry.setTabFooter(rs.getString(FIELD_TAB_FOOTER));
    pluginInformationEntry.setMotdMessage(rs.getString(FIELD_MOTD_MESSAGE));
    pluginInformationEntry.setMotdPlayers(rs.getInt(FIELD_MOTD_PLAYERS));
    pluginInformationEntry.setDbVersion(rs.getInt(FIELD_DB_VERSION));
    return pluginInformationEntry;
  }

  public static @NonNull CropEntry mapCrop(@NonNull ResultSet rs) throws SQLException {
    CropEntry ce = new CropEntry();
    ce.setId(rs.getInt(FIELD_ID));
    ce.setPlant(Material.getMaterial(rs.getString(FIELD_PLANT)));
    ce.setSeed(Material.getMaterial(rs.getString(FIELD_SEED)));
    return ce;
  }

  public static @NonNull DropEntry mapDrop(@NonNull ResultSet rs) throws SQLException {
    DropEntry de = new DropEntry();
    de.setId(rs.getInt(FIELD_ID));
    de.setMaterial(Material.getMaterial(rs.getString(FIELD_MATERIAL)));
    de.setMin(rs.getInt(FIELD_MIN_INT));
    de.setMax(rs.getInt(FIELD_MAX_INT));
    return de;
  }
}
