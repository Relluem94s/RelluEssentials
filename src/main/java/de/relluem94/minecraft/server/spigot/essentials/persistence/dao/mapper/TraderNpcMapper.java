package de.relluem94.minecraft.server.spigot.essentials.persistence.dao.mapper;

import static de.relluem94.minecraft.server.spigot.essentials.constants.db.DatabaseMappings.FIELD_CREATED;
import static de.relluem94.minecraft.server.spigot.essentials.constants.db.DatabaseMappings.FIELD_CREATEDBY;
import static de.relluem94.minecraft.server.spigot.essentials.constants.db.DatabaseMappings.FIELD_DELETED;
import static de.relluem94.minecraft.server.spigot.essentials.constants.db.DatabaseMappings.FIELD_DELETEDBY;
import static de.relluem94.minecraft.server.spigot.essentials.constants.db.DatabaseMappings.FIELD_ID;
import static de.relluem94.minecraft.server.spigot.essentials.constants.db.DatabaseMappings.FIELD_NAME;
import static de.relluem94.minecraft.server.spigot.essentials.constants.db.DatabaseMappings.FIELD_PROFESSION;
import static de.relluem94.minecraft.server.spigot.essentials.constants.db.DatabaseMappings.FIELD_SLOT_VAR_NAME;
import static de.relluem94.minecraft.server.spigot.essentials.constants.db.DatabaseMappings.FIELD_TYPE;
import static de.relluem94.minecraft.server.spigot.essentials.constants.db.DatabaseMappings.FIELD_UPDATED;
import static de.relluem94.minecraft.server.spigot.essentials.constants.db.DatabaseMappings.FIELD_UPDATEDBY;

import de.relluem94.minecraft.server.spigot.essentials.constants.Constants;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.TraderNpcEntry;
import de.relluem94.minecraft.server.spigot.essentials.npcs.trader.TraderNpc;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Function;
import org.bukkit.entity.Villager;
import org.jspecify.annotations.NonNull;

/**
 * Utility class providing mapping functionality for trader NPC database records.
 *
 * <p>Maps {@link java.sql.ResultSet} rows to {@link TraderNpcEntry} instances,
 * resolving professions and slot configurations from the database fields.
 */
public class TraderNpcMapper {

  private TraderNpcMapper() {
    throw new IllegalStateException(Constants.PLUGIN_INTERNAL_UTILITY_CLASS);
  }

  /**
   * Maps a single database row from the given {@link java.sql.ResultSet}
   * to a {@link TraderNpcEntry}.
   *
   * <p>Reads all standard audit fields, NPC-specific fields, and up to 28 slot name entries.
   * The profession string is resolved to a {@link Villager.Profession} using the
   * provided resolver function.
   *
   * @param rs                 the {@link java.sql.ResultSet} positioned at the row to map
   * @param professionResolver a {@link Function} that resolves a lowercase profession string
   *                           to the corresponding {@link Villager.Profession}
   * @return a fully populated {@link TraderNpcEntry} representing the current row
   * @throws SQLException if a database access error occurs while reading
   *     the {@link java.sql.ResultSet}
   */
  public static @NonNull TraderNpcEntry mapNpc(@NonNull ResultSet rs,
      @NonNull Function<String, Villager.Profession> professionResolver) throws SQLException {
    TraderNpcEntry traderNpcEntry = new TraderNpcEntry();

    traderNpcEntry.setId(rs.getInt(FIELD_ID));
    traderNpcEntry.setCreated(rs.getString(FIELD_CREATED));
    traderNpcEntry.setCreatedBy(rs.getInt(FIELD_CREATEDBY));
    traderNpcEntry.setUpdated(rs.getString(FIELD_UPDATED));
    traderNpcEntry.setUpdatedBy(rs.getInt(FIELD_UPDATEDBY));
    traderNpcEntry.setDeleted(rs.getString(FIELD_DELETED));
    traderNpcEntry.setDeletedBy(rs.getInt(FIELD_DELETEDBY));

    traderNpcEntry.setName(rs.getString(FIELD_NAME));
    traderNpcEntry.setProfession(
        professionResolver.apply(rs.getString(FIELD_PROFESSION).toLowerCase()));
    traderNpcEntry.setType(TraderNpc.Type.valueOf(rs.getString(FIELD_TYPE)));

    for (int i = 0; i <= 27; i++) {
      traderNpcEntry.setSlotName(i, rs.getString(String.format(FIELD_SLOT_VAR_NAME, i + 1)));
    }
    return traderNpcEntry;
  }
}
