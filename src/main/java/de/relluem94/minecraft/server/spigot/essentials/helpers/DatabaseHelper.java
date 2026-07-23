package de.relluem94.minecraft.server.spigot.essentials.helpers;

import de.relluem94.minecraft.server.spigot.essentials.constants.DatabaseMappings;
import de.relluem94.minecraft.server.spigot.essentials.constants.PlayerState;
import de.relluem94.minecraft.server.spigot.essentials.helpers.db.loader.SqlResourceLoader;
import de.relluem94.minecraft.server.spigot.essentials.helpers.db.mapper.*;
import de.relluem94.minecraft.server.spigot.essentials.helpers.interfaces.IPatchHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.*;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.jetbrains.annotations.NotNull;

import javax.sql.DataSource;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_NAME_CONSOLE;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.ChatHelper.consoleSendMessage;

/**
 *
 * @author rellu
 */
public class DatabaseHelper {

    private final DataSource dataSource;
    private final DataSource dataSourceNoSchema;
    private final SqlResourceLoader sqlResourceLoader;
    @Setter
    private IPatchHelper patchHelper;

    public DatabaseHelper(DataSource dataSource, DataSource dataSourceNoSchema,
                          SqlResourceLoader sqlResourceLoader) {
        this.dataSource = dataSource;
        this.dataSourceNoSchema = dataSourceNoSchema;
        this.sqlResourceLoader = sqlResourceLoader;
    }

    public void init() {
        patchHelper.applyPatch(getPluginInformation().getDbVersion());
    }

    @FunctionalInterface
    private interface StatementConfigurer {
        void configure(PreparedStatement ps) throws SQLException;
    }

    @FunctionalInterface
    private interface RowMapper<T> {
        T map(ResultSet rs) throws SQLException;
    }

    @FunctionalInterface
    private interface RowConsumer {
        void consume(ResultSet rs) throws SQLException;
    }

    private void queryForEach(String sqlFile, StatementConfigurer configurer, RowConsumer consumer) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sqlResourceLoader.load("sqls/" + sqlFile))) {
            configurer.configure(ps);
            ps.execute();
            try (ResultSet rs = ps.getResultSet()) {
                while (rs.next()) {
                    consumer.consume(rs);
                }
            }
        } catch (SQLException | FileNotFoundException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    private <T> List<T> queryList(String sqlFile, StatementConfigurer configurer, RowMapper<T> mapper) {
        List<T> results = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sqlResourceLoader.load("sqls/" + sqlFile))) {
            configurer.configure(ps);
            ps.execute();
            try (ResultSet rs = ps.getResultSet()) {
                while (rs.next()) {
                    results.add(mapper.map(rs));
                }
            }
        } catch (SQLException | FileNotFoundException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
        return results;
    }

    private <T> T querySingle(String sqlFile, StatementConfigurer configurer, RowMapper<T> mapper) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sqlResourceLoader.load("sqls/" + sqlFile))) {
            configurer.configure(ps);
            ps.execute();
            try (ResultSet rs = ps.getResultSet()) {
                if (rs.next()) {
                    return mapper.map(rs);
                }
            }
        } catch (SQLException | FileNotFoundException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
        return null;
    }

    private <T> T querySingleNoSchema(String sqlFile, StatementConfigurer configurer, RowMapper<T> mapper) {
        try (Connection connection = dataSourceNoSchema.getConnection();
             PreparedStatement ps = connection.prepareStatement(sqlResourceLoader.load("sqls/" + sqlFile))) {
            configurer.configure(ps);
            ps.execute();
            try (ResultSet rs = ps.getResultSet()) {
                if (rs.next()) {
                    return mapper.map(rs);
                }
            }
        } catch (SQLException | FileNotFoundException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            throw new RuntimeException(ex);
        }
        return null;
    }

    private void executeUpdate(String sqlFile, StatementConfigurer configurer) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sqlResourceLoader.load("sqls/" + sqlFile))) {
            configurer.configure(ps);
            ps.execute();
        } catch (SQLException | FileNotFoundException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    private int executeUpdateWithCount(String sqlFile) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sqlResourceLoader.load("sqls/" + sqlFile))) {
            return ps.executeUpdate();
        } catch (SQLException | FileNotFoundException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            return 0;
        }
    }

    private void executeUpdateNoSchema(String sqlFile) {
        try (Connection connection = dataSourceNoSchema.getConnection();
             PreparedStatement ps = connection.prepareStatement(sqlResourceLoader.load("sqls/" + sqlFile))) {
            ps.execute();
        } catch (SQLException | FileNotFoundException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    void executeScript(String script) {
        executeUpdate(script, _ -> {});
    }

    void executeScriptNoSchema(String script) {
        executeUpdateNoSchema(script);
    }

    public LocationEntry getLocation(@NotNull Location l, int type) {
        return querySingle("getLocationByLocation.sql", ps -> {
            ps.setFloat(1, (float) l.getX());
            ps.setFloat(2, (float) l.getY());
            ps.setFloat(3, (float) l.getZ());
            ps.setInt(4, type);
        }, LocationMapper::mapLocation);
    }

    public PlayerPartnerEntry getPlayerPartner(int playerFK) {
        return querySingle("getPlayerPartner.sql", ps -> {
            ps.setInt(1, playerFK);
            ps.setInt(2, playerFK);
        }, PlayerMapper::mapPlayerPartner);
    }

    public PluginInformationEntry getPluginInformation() {
        PluginInformationEntry fallback = new PluginInformationEntry();
        try {
            return querySingleNoSchema("getPluginInformation.sql", _ -> {}, MiscMapper::mapPluginInformation);
        } catch (Exception ex) {
            consoleSendMessage(PLUGIN_NAME_CONSOLE, "Init Database..");
            fallback.setDbVersion(-1);
            return fallback;
        }
    }

    public List<CropEntry> getCrops() {
        return queryList("getCrops.sql", _ -> {}, MiscMapper::mapCrop);
    }

    public List<DropEntry> getDrops() {
        return queryList("getDrops.sql", _ -> {}, MiscMapper::mapDrop);
    }

    public List<SettingEntry> getAllSettings() {
        return queryList("getAllSettings.sql", _ -> {}, SettingMapper::mapSetting);
    }

    public List<WorldGroupSettingEntry> getAllWorldGroupSettings() {
        return queryList("getAllWorldGroupSettings.sql", _ -> {}, WorldGroupSettingMapper::mapWorldGroupSetting);
    }

    public List<LocationTypeEntry> getLocationTypes() {
        return queryList("getLocationTypes.sql", _ -> {}, LocationMapper::mapLocationType);
    }

    public List<LocationEntry> getWarps() {
        return queryList("getWarps.sql", _ -> {}, LocationMapper::mapLocation);
    }

    public List<GroupEntry> getGroups() {
        return queryList("getGroups.sql", _ -> {}, PlayerMapper::mapGroup);
    }

    public List<NPCEntry> getNPCs() {
        return queryList("getNPCs.sql", _ -> {}, rs -> NPCMapper.mapNPC(rs, key -> Registry.VILLAGER_PROFESSION.get(NamespacedKey.minecraft(key))));
    }

    public List<ProtectionLockEntry> getProtectionLocks() {
        return queryList("getProtectionLocks.sql", _ -> {}, ProtectionMapper::mapProtectionLock);
    }

    public List<BankTierEntry> getBankTiers() {
        return queryList("getBankTiers.sql", _ -> {}, BankMapper::mapBankTier);
    }

    public List<BagTypeEntry> getBagTypes() {
        return queryList("getBagTypes.sql", _ -> {}, BagMapper::mapBagType);
    }

    public BagTypeEntry getBagType(int type) {
        return querySingle("getBagTypeById.sql", ps -> ps.setInt(1, type), BagMapper::mapBagType);
    }

    public BankTierEntry getBankTier(int id) {
        return querySingle("getBankTier.sql", ps -> ps.setInt(1, id), BankMapper::mapBankTier);
    }

    public LocationEntry getLocation(int id) {
        return querySingle("getLocationById.sql", ps -> ps.setInt(1, id), LocationMapper::mapLocation);
    }

    public void insertPlayerPartner(@NotNull PlayerPartnerEntry ppe) {
        executeUpdate("insertPlayerPartner.sql", ps -> {
            ps.setInt(1, ppe.getCreatedBy());
            ps.setInt(2, ppe.getFirstPartnerId());
            ps.setInt(3, ppe.getSecondPartnerId());
            ps.setBoolean(4, ppe.isShareProtections());
        });
    }

    public void deletePlayerPartner(@NotNull PlayerPartnerEntry ppe) {
        executeUpdate("deletePlayerPartner.sql", ps -> {
            ps.setInt(1, ppe.getDeletedBy());
            ps.setInt(2, ppe.getId());
        });
    }

    public void insertPlayer(@NotNull PlayerEntry pe) {
        executeUpdate("insertPlayer.sql", ps -> {
            ps.setInt(1, pe.getCreatedBy());
            ps.setString(2, pe.getUuid());
            ps.setString(3, pe.getName());
            ps.setString(4, pe.getCustomName());
            ps.setInt(5, pe.getGroup().getId());
        });
    }

    public void updatePlayer(@NotNull PlayerEntry pe) {
        executeUpdate("updatePlayer.sql", ps -> {
            ps.setInt(1, pe.getId());
            ps.setInt(2, pe.getGroup().getId());
            ps.setBoolean(3, pe.isAfk());
            ps.setBoolean(4, pe.isFlying());
            ps.setString(5, pe.getName());
            ps.setString(6, pe.getCustomName());
            ps.setDouble(7, pe.getPurse());
            ps.setString(8, pe.getUuid());
        });
    }

    public void insertLocation(@NotNull LocationEntry le) {
        executeUpdate("insertLocation.sql", ps -> {
            Location l = le.getLocation();
            ps.setInt(1, le.getPlayerId());
            ps.setFloat(2, (float) l.getX());
            ps.setFloat(3, (float) l.getY());
            ps.setFloat(4, (float) l.getZ());
            ps.setFloat(5, l.getYaw());
            ps.setFloat(6, l.getPitch());
            ps.setString(7, Objects.requireNonNull(l.getWorld()).getName());
            ps.setString(8, le.getLocationName());
            ps.setInt(9, le.getLocationType().getId());
            ps.setInt(10, le.getPlayerId());
        });
    }

    public void deleteLocation(@NotNull LocationEntry le) {
        executeUpdate("deleteLocation.sql", ps -> {
            ps.setInt(1, le.getPlayerId());
            ps.setInt(2, le.getId());
        });
    }

    public void insertProtection(@NotNull ProtectionEntry pe) {
        executeUpdate("insertProtection.sql", ps -> {
            ps.setInt(1, pe.getLocationEntry().getPlayerId());
            ps.setInt(2, pe.getLocationEntry().getId());
            ps.setString(3, pe.getMaterialName());
            ps.setString(4, pe.getFlags().toString());
            ps.setString(5, pe.getRights().toString());
        });
    }

    public void updateProtectionFlag(@NotNull ProtectionEntry pe) {
        executeUpdate("updateProtectionFlags.sql", ps -> {
            ps.setInt(1, pe.getLocationEntry().getPlayerId());
            ps.setString(2, pe.getFlags().toString());
            ps.setInt(3, pe.getId());
        });
    }

    public void updateProtectionRight(@NotNull ProtectionEntry pe) {
        executeUpdate("updateProtectionRights.sql", ps -> {
            ps.setInt(1, pe.getLocationEntry().getPlayerId());
            ps.setString(2, pe.getRights().toString());
            ps.setInt(3, pe.getId());
        });
    }

    @SuppressWarnings("unused")
    public void updatePlayerPartner(@NotNull PlayerPartnerEntry ppe) {
        executeUpdate("updatePlayerPartner.sql", ps -> {
            ps.setInt(1, ppe.getUpdatedBy());
            ps.setBoolean(2, ppe.isShareProtections());
            ps.setInt(3, ppe.getId());
        });
    }

    public List<WorldGroupEntry> getWorldGroups() {
        List<WorldGroupSettingEntry> allWorldGroupSettings = getAllWorldGroupSettings();
        return queryList("getWorldGroups.sql", _ -> {
        }, rs -> WorldMapper.mapWorldGroup(rs, allWorldGroupSettings));
    }

    public List<WorldEntry> getWorldByGroup(@NotNull WorldGroupEntry wge) {
        return queryList("getWorldByGroup.sql", ps -> ps.setInt(1, wge.getId()), rs -> {
            WorldEntry we = WorldMapper.mapWorld(rs);
            we.setWorldGroupEntry(wge);
            return we;
        });
    }

    public void insertWorldGroupInventory(@NotNull WorldGroupInventoryEntry worldGroupInventoryEntry) {
        executeUpdate("insertWorldInventoryByGroupAndPlayer.sql", ps -> {
            ps.setInt(1, worldGroupInventoryEntry.getPlayerId());
            ps.setInt(2, worldGroupInventoryEntry.getPlayerId());
            ps.setInt(3, worldGroupInventoryEntry.getWorldGroupEntry().getId());
            ps.setString(4, worldGroupInventoryEntry.getInventory().toString());
            ps.setDouble(5, worldGroupInventoryEntry.getHealth());
            ps.setInt(6, worldGroupInventoryEntry.getFoodLevel());
            ps.setInt(7, worldGroupInventoryEntry.getTotalExperience());
        });
    }

    public void updateWorldGroupInventory(@NotNull WorldGroupInventoryEntry worldGroupInventoryEntry) {
        executeUpdate("updateWorldInventoryByGroupAndPlayer.sql", ps -> {
            ps.setInt(1, worldGroupInventoryEntry.getUpdatedBy());
            ps.setString(2, worldGroupInventoryEntry.getInventory().toString());
            ps.setDouble(3, worldGroupInventoryEntry.getHealth());
            ps.setInt(4, worldGroupInventoryEntry.getFoodLevel());
            ps.setInt(5, worldGroupInventoryEntry.getTotalExperience());
            ps.setInt(6, worldGroupInventoryEntry.getPlayerId());
            ps.setInt(7, worldGroupInventoryEntry.getWorldGroupEntry().getId());
        });
    }

    public WorldGroupInventoryEntry getWorldGroupInventory(@NotNull PlayerEntry pe, @NotNull WorldGroupEntry wge) {
        return querySingle("getWorldInventoryByGroupAndPlayer.sql", ps -> {
            ps.setInt(1, wge.getId());
            ps.setInt(2, pe.getId());
        }, rs -> {
            WorldGroupInventoryEntry worldGroupInventoryEntry = WorldMapper.mapWorldGroupInventory(rs);
            worldGroupInventoryEntry.setWorldGroupEntry(wge);
            return worldGroupInventoryEntry;
        });
    }

    @SuppressWarnings("unused")
    public void insertWorld(@NotNull WorldEntry we) {
        executeUpdate("insertWorld.sql", ps -> {
            ps.setInt(1, we.getCreatedBy());
            ps.setString(2, we.getName());
            ps.setInt(3, we.getWorldGroupEntry().getId());
            ps.setInt(4, we.getGroupEntry().getId());
        });
    }

    @SuppressWarnings("unused")
    public void insertWorldGroup(@NotNull WorldGroupEntry wge) {
        executeUpdate("insertWorldGroup.sql", ps -> {
            ps.setInt(1, wge.getCreatedBy());
            ps.setString(2, wge.getName());
        });
    }

    @SuppressWarnings("unused")
    public WorldGroupEntry getWorldGroup(String name) {
        return querySingle("getWorldGroupByName.sql", ps -> ps.setString(1, name),
                rs -> WorldMapper.mapWorldGroup(rs, getAllWorldGroupSettings()));
    }

    public List<LocationEntry> getLocations(int id, int type) {
        return queryList("getLocationsByPlayer.sql", ps -> ps.setInt(1, id), rs -> {
            if (type != rs.getInt(DatabaseMappings.FIELD_LOCATION_TYPE_FK)) {
                return null;
            }
            return LocationMapper.mapLocation(rs);
        }).stream().filter(Objects::nonNull).toList();
    }

    public PlayerEntry getPlayer(String uuid) {
        return querySingle("getPlayer.sql", ps -> ps.setString(1, uuid), rs -> {
            PlayerEntry p = PlayerMapper.mapPlayer(rs);
            p.setHomes(getLocations(p.getId(), 1));
            p.setDeaths(getLocations(p.getId(), 2));
            p.setPartner(getPlayerPartner(p.getId()));
            p.setPlayerState(PlayerState.DEFAULT);
            return p;
        });
    }

    public BankAccountEntry getPlayerBankAccount(int playerFK) {
        return querySingle("getBankAccountByPlayer.sql", ps -> ps.setInt(1, playerFK), rs -> {
            BankAccountEntry bae = BankMapper.mapBankAccount(rs);
            bae.setTier(getBankTier(rs.getInt(DatabaseMappings.FIELD_BANK_TIER_FK)));
            return bae;
        });
    }

    public void insertBankAccount(@NotNull BankAccountEntry bae) {
        executeUpdate("insertBankAccount.sql", ps -> {
            ps.setInt(1, 1);
            ps.setInt(2, bae.getPlayerId());
            ps.setDouble(3, bae.getValue());
            ps.setInt(4, bae.getTier().getId());
        });
    }

    public void addTransactionToBank(int playerFK, int bankAccountFK, double transactionValue, double bankaccountTotal, int tier) {
        executeUpdate("insertBankTransaction.sql", ps -> {
            ps.setInt(1, playerFK);
            ps.setInt(2, bankAccountFK);
            ps.setDouble(3, transactionValue);
        });
        updateBankAccount(playerFK, transactionValue, bankaccountTotal, tier);
    }

    public void updateBankAccount(int playerFK, double transactionValue, double bankaccountTotal, int tier) {
        executeUpdate("updateBankAccount.sql", ps -> {
            ps.setInt(1, playerFK);
            ps.setDouble(2, bankaccountTotal + transactionValue);
            ps.setInt(3, tier);
            ps.setInt(4, playerFK);
        });
    }

    public List<BankTransactionEntry> getTransactionsToBankFromPlayer(int bankAccountFK) {
        return queryList("getBankAccountTransactionsByPlayer.sql", ps -> ps.setInt(1, bankAccountFK), BankMapper::mapBankTransaction);
    }

    public ProtectionEntry getProtectionByLocation(@NotNull Location l) {
        return querySingle("getProtectionByLocation.sql", ps -> {
            ps.setFloat(1, (float) l.getX());
            ps.setFloat(2, (float) l.getY());
            ps.setFloat(3, (float) l.getZ());
            ps.setString(4, Objects.requireNonNull(l.getWorld()).getName());
        }, rs -> {
            ProtectionEntry pe = ProtectionMapper.mapProtection(rs);
            pe.setLocationEntry(getLocation(l, 5));
            return pe;
        });
    }

    public void deleteProtection(@NotNull ProtectionEntry pe) {
        deleteLocation(pe.getLocationEntry());
        executeUpdate("deleteProtection.sql", ps -> {
            ps.setInt(1, pe.getLocationEntry().getPlayerId());
            ps.setInt(2, pe.getId());
        });
    }

    public Map<Location, ProtectionEntry> getProtections() {
        Map<Location, ProtectionEntry> protectionsByLocation = new HashMap<>();
        queryForEach("getProtections.sql", _ -> {
        }, rs -> {
            LocationEntry loc = getLocation(rs.getInt(DatabaseMappings.FIELD_LOCATION_FK));
            if (loc != null) {
                ProtectionEntry pe = ProtectionMapper.mapProtection(rs);
                pe.setLocationEntry(loc);
                protectionsByLocation.put(loc.getLocation(), pe);
            } else {
                Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE,
                        "Protection Entry ({0}) without LocationEntry found, this should be due a bug.",
                        rs.getInt(DatabaseMappings.FIELD_ID));
            }
        });
        return protectionsByLocation;
    }

    public List<PlayerEntry> getPlayers() {
        return queryList("getPlayers.sql", _ -> {
        }, rs -> {
            PlayerEntry p = PlayerMapper.mapPlayer(rs);
            p.setHomes(getLocations(p.getId(), 1));
            p.setDeaths(getLocations(p.getId(), 2));
            p.setPartner(getPlayerPartner(p.getId()));
            p.setPlayerState(PlayerState.DEFAULT);
            return p;
        });
    }

    public void insertGroup(@NotNull GroupEntry ge) {
        executeUpdate("insertGroup.sql", ps -> {
            ps.setInt(1, ge.getId());
            ps.setString(2, ge.getName());
            ps.setString(3, ge.getPrefix());
        });
    }

    public BagEntry getBag(int type, int playerFK) {
        return querySingle("getBagByPlayerAndType.sql", ps -> {
            ps.setInt(1, type);
            ps.setInt(2, playerFK);
        }, rs -> {
            BagEntry be = BagMapper.mapBag(rs);
            be.setBagType(getBagType(rs.getInt(DatabaseMappings.FIELD_BAG_TYPE_FK)));
            return be;
        });
    }

    public void insertBag(int type, int id) {
        executeUpdate("insertBag.sql", ps -> {
            ps.setInt(1, id);
            ps.setInt(2, id);
            ps.setInt(3, type);
        });
    }

    public List<BagEntry> getBags() {
        return queryList("getBags.sql", _ -> {
        }, rs -> {
            BagEntry be = BagMapper.mapBag(rs);
            be.setBagType(getBagType(rs.getInt(DatabaseMappings.FIELD_BAG_TYPE_FK)));
            return be;
        });
    }

    public void updateBagEntry(@NotNull BagEntry be) {
        executeUpdate("updateBag.sql", ps -> {
            ps.setInt(1, be.getPlayerId());
            for (int i = 0; i < BagHelper.BAG_SIZE; i++) {
                ps.setInt(i + 2, be.getSlotValue(i));
            }
            ps.setInt(BagHelper.BAG_SIZE + 2, be.getId());
        });
    }

    public int cleanupLocations() {
        return executeUpdateWithCount("cleanupLocations.sql");
    }

    public int cleanupProtections() {
        return executeUpdateWithCount("cleanupProtections.sql");
    }
}