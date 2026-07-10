package de.relluem94.minecraft.server.spigot.essentials.helpers;

import static de.relluem94.minecraft.server.spigot.essentials.constants.Constants.PLUGIN_NAME_CONSOLE;
import static de.relluem94.minecraft.server.spigot.essentials.constants.DatabaseConstants.PLUGIN_DATABASE_NAME;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.ChatHelper.consoleSendMessage;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.relluem94.minecraft.server.spigot.essentials.constants.DatabaseMappings;
import de.relluem94.minecraft.server.spigot.essentials.helpers.dbmapper.*;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.constants.Constants;
import de.relluem94.minecraft.server.spigot.essentials.constants.PlayerState;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.BagEntry;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.BagTypeEntry;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.BankAccountEntry;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.BankTierEntry;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.BankTransactionEntry;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.CropEntry;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.DropEntry;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.GroupEntry;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.LocationEntry;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.LocationTypeEntry;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.NPCEntry;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.PlayerEntry;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.PlayerPartnerEntry;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.PluginInformationEntry;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.ProtectionEntry;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.ProtectionLockEntry;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.WorldEntry;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.WorldGroupEntry;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.WorldGroupInventoryEntry;

/**
 *
 * @author rellu
 */
public class DatabaseHelper {

    public static final int DB_TEST_PORT = 65065;

    private final String user;
    private final String password;

    private static final String CONNECTOR = "jdbc:mysql";
    private final String connectorString;
    private final String connectorStringInit;

    public DatabaseHelper(String host, String user, String password, int port) {
        if (RelluEssentials.getInstance().isUnitTest()) {
            this.user = "root";
            this.password = "";
            port = DB_TEST_PORT;
        } else {
            this.user = user;
            this.password = password;
        }

        connectorString = CONNECTOR + "://" + host + ":" + port + "/" + PLUGIN_DATABASE_NAME + "?useSSL=false&allowPublicKeyRetrieval=true";
        connectorStringInit = CONNECTOR + "://" + host + ":" + port + "?useSSL=false&allowPublicKeyRetrieval=true";
    }

    public String readResource(final String fileName) throws FileNotFoundException {

        String out;
        try (InputStream is = getClass().getResourceAsStream("/" + fileName);
             InputStreamReader isr = new InputStreamReader(Objects.requireNonNull(is));
             BufferedReader br = new BufferedReader(isr)) {
            String line;
            StringBuilder sb = new StringBuilder();
            while ((line = br.readLine()) != null) {
                sb.append(line).append(Constants.PLUGIN_EOL);
            }

            out = sb.toString();
        } catch (IOException | NullPointerException e) {
            throw new FileNotFoundException(String.format("File %s was not found!", fileName));
        }

        return out;
    }

    public void init() {
        PatchHelper ph = new PatchHelper(this);
        ph.applyPatch(getPluginInformation().getDbVersion());
    }

    void script(@NotNull Connection connection, String script) {
        try (PreparedStatement ps = connection.prepareStatement(readResource("sqls/" + script))) {
            ps.execute();
        } catch (SQLException | FileNotFoundException ey) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, ey);
        }
    }

    void executeScript(String script) {
        try (Connection connection = DriverManager.getConnection(connectorString, user, password)) {
            script(connection, script);
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    void executeScriptNoSchema(String script) {
        try (Connection connection = DriverManager.getConnection(connectorStringInit, user,
                password)) {
            script(connection, script);
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    public List<CropEntry> getCrops() {
        List<CropEntry> lce = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(connectorString, user, password)) {
            try (PreparedStatement ps = connection.prepareStatement(readResource("sqls/getCrops.sql"))) {
                ps.execute();
                try (ResultSet rs = ps.getResultSet()) {
                    while (rs.next()) {
                        lce.add(MiscMapper.mapCrop(rs));
                    }
                }
            }
        } catch (SQLException | FileNotFoundException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
        return lce;
    }

    public List<DropEntry> getDrops() {
        List<DropEntry> lde = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(connectorString, user, password)) {
            try (PreparedStatement ps = connection.prepareStatement(readResource("sqls/getDrops.sql"))) {
                ps.execute();
                try (ResultSet rs = ps.getResultSet()) {
                    while (rs.next()) {
                        lde.add(MiscMapper.mapDrop(rs));
                    }
                }
            }
        } catch (SQLException | FileNotFoundException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
        return lde;
    }

    public void insertPlayerPartner(@NotNull PlayerPartnerEntry ppe) {
        try (Connection connection = DriverManager.getConnection(connectorString, user, password)) {
            try (PreparedStatement ps = connection.prepareStatement(readResource("sqls/insertPlayerPartner.sql"))) {
                ps.setInt(1, ppe.getCreatedBy());
                ps.setInt(2, ppe.getFirstPartnerId());
                ps.setInt(3, ppe.getSecondPartnerId());
                ps.setBoolean(4, ppe.isShareProtections());

                ps.execute();
            }
        } catch (SQLException | FileNotFoundException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    public void deletePlayerPartner(@NotNull PlayerPartnerEntry ppe) {
        try (Connection connection = DriverManager.getConnection(connectorString, user, password)) {
            try (PreparedStatement ps = connection.prepareStatement(readResource("sqls/deletePlayerPartner.sql"))) {
                ps.setInt(1, ppe.getDeletedBy());
                ps.setInt(2, ppe.getId());
                ps.execute();
            }
        } catch (SQLException | FileNotFoundException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    @SuppressWarnings("unused")
    public void updatePlayerPartner(@NotNull PlayerPartnerEntry ppe) {
        try (Connection connection = DriverManager.getConnection(connectorString, user, password)) {
            try (PreparedStatement ps = connection.prepareStatement(readResource("sqls/updatePlayerPartner.sql"))) {
                ps.setInt(1, ppe.getUpdatedBy());
                ps.setBoolean(2, ppe.isShareProtections());
                ps.setInt(1, ppe.getId());
                ps.execute();
            }
        } catch (SQLException | FileNotFoundException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    public PlayerPartnerEntry getPlayerPartner(int playerFK) {
        try (Connection connection = DriverManager.getConnection(connectorString, user, password)) {
            try (PreparedStatement ps = connection.prepareStatement(readResource("sqls/getPlayerPartner.sql"))) {
                ps.setInt(1, playerFK);
                ps.setInt(2, playerFK);
                ps.execute();
                try (ResultSet rs = ps.getResultSet()) {
                    if (rs.next()) {
                        return PlayerMapper.mapPlayerPartner(rs);
                    }
                }
            }
        } catch (SQLException | FileNotFoundException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
        return null;
    }

    public List<WorldGroupEntry> getWorldGroups() {
        List<WorldGroupEntry> worldGroupEntryList = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(connectorString, user, password)) {
            try (PreparedStatement ps = connection.prepareStatement(readResource("sqls/getWorldGroups.sql"))) {
                ps.execute();
                try (ResultSet rs = ps.getResultSet()) {
                    while (rs.next()) {
                        worldGroupEntryList.add(WorldMapper.mapWorldGroup(rs));
                    }
                }
            }
        } catch (SQLException | FileNotFoundException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
        return worldGroupEntryList;
    }

    public List<WorldEntry> getWorldByGroup(@NotNull WorldGroupEntry wge) {
        List<WorldEntry> lwe = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(connectorString, user, password)) {
            try (PreparedStatement ps = connection.prepareStatement(readResource("sqls/getWorldByGroup.sql"))) {
                ps.setInt(1, wge.getId());
                ps.execute();
                try (ResultSet rs = ps.getResultSet()) {
                    while (rs.next()) {
                        WorldEntry we = WorldMapper.mapWorld(rs);
                        we.setWorldGroupEntry(wge);
                        lwe.add(we);
                    }
                }
            }
        } catch (SQLException | FileNotFoundException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
        return lwe;
    }

    public void insertWorldGroupInventory(@NotNull WorldGroupInventoryEntry worldGroupInventoryEntry) {
        try (Connection connection = DriverManager.getConnection(connectorString, user, password)) {
            try (PreparedStatement ps = connection.prepareStatement(readResource("sqls/insertWorldInventoryByGroupAndPlayer.sql"))) {
                ps.setInt(1, worldGroupInventoryEntry.getPlayerId());
                ps.setInt(2, worldGroupInventoryEntry.getPlayerId());
                ps.setInt(3, worldGroupInventoryEntry.getWorldGroupEntry().getId());
                ps.setString(4, worldGroupInventoryEntry.getInventory().toString());
                ps.setDouble(5, worldGroupInventoryEntry.getHealth());
                ps.setInt(6, worldGroupInventoryEntry.getFoodLevel());
                ps.setInt(7, worldGroupInventoryEntry.getTotalExperience());

                ps.execute();
            }
        } catch (SQLException | FileNotFoundException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    public void updateWorldGroupInventory(@NotNull WorldGroupInventoryEntry worldGroupInventoryEntry) {
        try (Connection connection = DriverManager.getConnection(connectorString, user, password)) {
            try (PreparedStatement ps = connection.prepareStatement(readResource("sqls/updateWorldInventoryByGroupAndPlayer.sql"))) {
                ps.setInt(1, worldGroupInventoryEntry.getUpdatedBy());
                ps.setString(2, worldGroupInventoryEntry.getInventory().toString());
                ps.setDouble(3, worldGroupInventoryEntry.getHealth());
                ps.setInt(4, worldGroupInventoryEntry.getFoodLevel());
                ps.setInt(5, worldGroupInventoryEntry.getTotalExperience());
                ps.setInt(6, worldGroupInventoryEntry.getPlayerId());
                ps.setInt(7, worldGroupInventoryEntry.getWorldGroupEntry().getId());

                ps.execute();
            }
        } catch (SQLException | FileNotFoundException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    public WorldGroupInventoryEntry getWorldGroupInventory(@NotNull PlayerEntry pe, @NotNull WorldGroupEntry wge) {
        try (Connection connection = DriverManager.getConnection(connectorString, user, password)) {
            try (PreparedStatement ps = connection.prepareStatement(readResource("sqls/getWorldInventoryByGroupAndPlayer.sql"))) {
                ps.setInt(1, wge.getId());
                ps.setInt(2, pe.getId());
                ps.execute();
                try (ResultSet rs = ps.getResultSet()) {
                    if (rs.next()) {
                        WorldGroupInventoryEntry worldGroupInventoryEntry = WorldMapper.mapWorldGroupInventory(rs);
                        worldGroupInventoryEntry.setWorldGroupEntry(wge);
                        return worldGroupInventoryEntry;
                    }
                }
            }
        } catch (SQLException | FileNotFoundException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
        return null;
    }

    @SuppressWarnings("unused")
    public void insertWorld(@NotNull WorldEntry we) {
        try (Connection connection = DriverManager.getConnection(connectorString, user, password)) {
            try (PreparedStatement ps = connection.prepareStatement(readResource("sqls/insertWorld.sql"))) {

                ps.setInt(1, we.getCreatedBy());
                ps.setString(2, we.getName());
                ps.setInt(3, we.getWorldGroupEntry().getId());
                ps.setInt(4, we.getGroupEntry().getId());

                ps.execute();
            }
        } catch (SQLException | FileNotFoundException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    @SuppressWarnings("unused")
    public void insertWorldGroup(@NotNull WorldGroupEntry wge) {
        try (Connection connection = DriverManager.getConnection(connectorString, user, password)) {
            try (PreparedStatement ps = connection.prepareStatement(readResource("sqls/insertWorldGroup.sql"))) {
                ps.setInt(1, wge.getCreatedBy());
                ps.setString(2, wge.getName());

                ps.execute();
            }
        } catch (SQLException | FileNotFoundException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    @SuppressWarnings("unused")
    public WorldGroupEntry getWorldGroup(String name) {
        try (Connection connection = DriverManager.getConnection(connectorString, user, password)) {
            try (PreparedStatement ps = connection.prepareStatement(readResource("sqls/getWorldGroupByName.sql"))) {
                ps.setString(1, name);
                ps.execute();
                try (ResultSet rs = ps.getResultSet()) {
                    if (rs.next()) {
                        return WorldMapper.mapWorldGroup(rs);
                    }
                }
            }
        } catch (SQLException | FileNotFoundException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
        return null;
    }

    public List<LocationEntry> getLocations(int id, int type) {
        List<LocationEntry> lle = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(connectorString, user, password)) {
            try (PreparedStatement ps = connection.prepareStatement(readResource("sqls/getLocationsByPlayer.sql"))) {
                ps.setInt(1, id);
                ps.execute();
                try (ResultSet rs = ps.getResultSet()) {
                    while (rs.next()) {
                        if (type != rs.getInt(DatabaseMappings.FIELD_LOCATION_TYPE_FK)) {
                            continue;
                        }
                        lle.add(LocationMapper.mapLocation(rs));
                    }
                }
            }
        } catch (SQLException | FileNotFoundException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
        return lle;
    }

    public LocationEntry getLocation(@NotNull Location l, int type) {
        try (Connection connection = DriverManager.getConnection(connectorString, user, password)) {
            try (PreparedStatement ps = connection.prepareStatement(readResource("sqls/getLocationByLocation.sql"))) {
                ps.setFloat(1, (float) l.getX());
                ps.setFloat(2, (float) l.getY());
                ps.setFloat(3, (float) l.getZ());
                ps.setInt(4, type);
                ps.execute();
                try (ResultSet rs = ps.getResultSet()) {
                    if (rs.next()) {
                        return LocationMapper.mapLocation(rs);
                    }
                }
            }
        } catch (SQLException | FileNotFoundException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
        return null;
    }

    public LocationEntry getLocation(int id) {
        try (Connection connection = DriverManager.getConnection(connectorString, user, password)) {
            try (PreparedStatement ps = connection.prepareStatement(readResource("sqls/getLocationById.sql"))) {
                ps.setInt(1, id);

                ps.execute();
                try (ResultSet rs = ps.getResultSet()) {
                    if (rs.next()) {
                        return LocationMapper.mapLocation(rs);
                    }
                }
            }
        } catch (SQLException | FileNotFoundException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
        return null;
    }

    public PluginInformationEntry getPluginInformation() {
        PluginInformationEntry pie = new PluginInformationEntry();
        try (Connection connection = DriverManager.getConnection(connectorStringInit, user, password)) {
            try (PreparedStatement ps = connection.prepareStatement(readResource("sqls/getPluginInformation.sql"))) {
                ps.execute();
                try (ResultSet rs = ps.getResultSet()) {
                    if (rs.next()) {
                        return MiscMapper.mapPluginInformation(rs);
                    }
                }
            }
        } catch (SQLException | FileNotFoundException ex) {
            consoleSendMessage(PLUGIN_NAME_CONSOLE, "Init Database..");
            pie.setDbVersion(-1); // standard pie if no Database version was found
            return pie;
        }
        return null;
    }

    public PlayerEntry getPlayer(String uuid) {
        try (Connection connection = DriverManager.getConnection(connectorString, user, password)) {
            try (PreparedStatement ps = connection.prepareStatement(readResource("sqls/getPlayer.sql"))) {
                ps.setString(1, uuid);
                ps.execute();
                try (ResultSet rs = ps.getResultSet()) {
                    if (rs.next()) {
                        PlayerEntry p = PlayerMapper.mapPlayer(rs);
                        p.setHomes(getLocations(p.getId(), 1));
                        p.setDeaths(getLocations(p.getId(), 2));
                        p.setPartner(getPlayerPartner(p.getId()));
                        p.setPlayerState(PlayerState.DEFAULT);
                        return p;
                    }
                }
            }
        } catch (SQLException | FileNotFoundException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
        return null;
    }

    public BankAccountEntry getPlayerBankAccount(int playerFK) {
        try (Connection connection = DriverManager.getConnection(connectorString, user, password)) {
            try (PreparedStatement ps = connection.prepareStatement(readResource("sqls/getBankAccountByPlayer.sql"))) {
                ps.setInt(1, playerFK);
                ps.execute();
                try (ResultSet rs = ps.getResultSet()) {
                    if (rs.next()) {
                        BankAccountEntry bae = BankMapper.mapBankAccount(rs);
                        bae.setTier(getBankTier(rs.getInt(DatabaseMappings.FIELD_BANK_TIER_FK)));
                        return bae;
                    }
                }
            }
        } catch (SQLException | FileNotFoundException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
        return null;
    }

    public BankTierEntry getBankTier(int id) {
        try (Connection connection = DriverManager.getConnection(connectorString, user, password)) {
            try (PreparedStatement ps = connection.prepareStatement(readResource("sqls/getBankTier.sql"))) {
                ps.setInt(1, id);
                ps.execute();
                try (ResultSet rs = ps.getResultSet()) {
                    if (rs.next()) {
                        return BankMapper.mapBankTier(rs);
                    }
                }
            }
        } catch (SQLException | FileNotFoundException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
        return null;
    }

    public List<BankTierEntry> getBankTiers() {
        List<BankTierEntry> bankTierEntryList = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(connectorString, user, password)) {
            try (PreparedStatement ps = connection.prepareStatement(readResource("sqls/getBankTiers.sql"))) {
                ps.execute();
                try (ResultSet rs = ps.getResultSet()) {
                    while (rs.next()) {
                        bankTierEntryList.add(BankMapper.mapBankTier(rs));
                    }
                }
            }
        } catch (SQLException | FileNotFoundException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
        return bankTierEntryList;
    }

    public void insertBankAccount(@NotNull BankAccountEntry bae) {
        try (Connection connection = DriverManager.getConnection(connectorString, user, password)) {
            try (PreparedStatement ps = connection.prepareStatement(readResource("sqls/insertBankAccount.sql"))) {
                ps.setInt(1, 1);
                ps.setInt(2, bae.getPlayerId());
                ps.setDouble(3, bae.getValue());
                ps.setInt(4, bae.getTier().getId());

                ps.execute();
            }
        } catch (SQLException | FileNotFoundException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    public void addTransactionToBank(int playerFK, int bankAccountFK, double transactionValue, double bankaccountTotal, int tier) {
        try (Connection connection = DriverManager.getConnection(connectorString, user, password)) {
            try (PreparedStatement ps = connection.prepareStatement(readResource("sqls/insertBankTransaction.sql"))) {
                ps.setInt(1, playerFK);
                ps.setInt(2, bankAccountFK);
                ps.setDouble(3, transactionValue);

                ps.execute();
                updateBankAccount(playerFK, transactionValue, bankaccountTotal, tier);
            }
        } catch (SQLException | FileNotFoundException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    public void updateBankAccount(int playerFK, double transactionValue, double bankaccountTotal, int tier) {
        try (Connection connection = DriverManager.getConnection(connectorString, user, password)) {
            try (PreparedStatement ps = connection.prepareStatement(readResource("sqls/updateBankAccount.sql"))) {

                ps.setInt(1, playerFK);
                ps.setDouble(2, (bankaccountTotal + transactionValue));
                ps.setInt(3, tier);
                ps.setInt(4, playerFK);
                ps.execute();

            }
        } catch (SQLException | FileNotFoundException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    public List<BankTransactionEntry> getTransactionsToBankFromPlayer(int bankAccountFK) {
        List<BankTransactionEntry> bte = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(connectorString, user, password)) {
            try (PreparedStatement ps = connection.prepareStatement(readResource("sqls/getBankAccountTransactionsByPlayer.sql"))) {
                ps.setInt(1, bankAccountFK);
                ps.execute();
                try (ResultSet rs = ps.getResultSet()) {
                    while (rs.next()) {
                        bte.add(BankMapper.mapBankTransaction(rs));
                    }
                }
            }
        } catch (SQLException | FileNotFoundException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
        return bte;
    }

    public ProtectionEntry getProtectionByLocation(@NotNull Location l) {
        try (Connection connection = DriverManager.getConnection(connectorString, user, password)) {
            try (PreparedStatement ps = connection.prepareStatement(readResource("sqls/getProtectionByLocation.sql"))) {
                ps.setFloat(1, (float) l.getX());
                ps.setFloat(2, (float) l.getY());
                ps.setFloat(3, (float) l.getZ());
                ps.setString(4, Objects.requireNonNull(l.getWorld()).getName());
                ps.execute();
                try (ResultSet rs = ps.getResultSet()) {
                    if (rs.next()) {
                        ProtectionEntry pe = ProtectionMapper.mapProtection(rs);
                        pe.setLocationEntry(getLocation(l, 5));
                        return pe;
                    }
                }
            }
        } catch (SQLException | FileNotFoundException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
        return null;
    }

    public void insertProtection(@NotNull ProtectionEntry pe) {
        try (Connection connection = DriverManager.getConnection(connectorString, user, password)) {
            try (PreparedStatement ps = connection.prepareStatement(readResource("sqls/insertProtection.sql"))) {

                ps.setInt(1, pe.getLocationEntry().getPlayerId());
                ps.setInt(2, pe.getLocationEntry().getId());
                ps.setString(3, pe.getMaterialName());
                ps.setString(4, pe.getFlags().toString());
                ps.setString(5, pe.getRights().toString());

                ps.execute();
            }
        } catch (SQLException | FileNotFoundException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    public void deleteProtection(@NotNull ProtectionEntry pe) {
        deleteLocation(pe.getLocationEntry());
        try (Connection connection = DriverManager.getConnection(connectorString, user, password)) {
            try (PreparedStatement ps = connection.prepareStatement(readResource("sqls/deleteProtection.sql"))) {
                ps.setInt(1, pe.getLocationEntry().getPlayerId());
                ps.setInt(2, pe.getId());
                ps.execute();
            }
        } catch (SQLException | FileNotFoundException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    public void updateProtectionFlag(@NotNull ProtectionEntry pe) {
        try (Connection connection = DriverManager.getConnection(connectorString, user, password)) {
            try (PreparedStatement ps = connection.prepareStatement(readResource("sqls/updateProtectionFlags.sql"))) {
                ps.setInt(1, pe.getLocationEntry().getPlayerId());
                ps.setString(2, pe.getFlags().toString());
                ps.setInt(3, pe.getId());
                ps.execute();
            }
        } catch (SQLException | FileNotFoundException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    public void updateProtectionRight(@NotNull ProtectionEntry pe) {
        try (Connection connection = DriverManager.getConnection(connectorString, user, password)) {
            try (PreparedStatement ps = connection.prepareStatement(readResource("sqls/updateProtectionRights.sql"))) {
                ps.setInt(1, pe.getLocationEntry().getPlayerId());
                ps.setString(2, pe.getRights().toString());
                ps.setInt(3, pe.getId());
                ps.execute();
            }
        } catch (SQLException | FileNotFoundException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    public Map<Location, ProtectionEntry> getProtections() {
        Map<Location, ProtectionEntry> pel = new HashMap<>();
        try (Connection connection = DriverManager.getConnection(connectorString, user, password)) {
            try (PreparedStatement ps = connection.prepareStatement(readResource("sqls/getProtections.sql"))) {
                ps.execute();
                try (ResultSet rs = ps.getResultSet()) {
                    while (rs.next()) {
                        LocationEntry loc = getLocation(rs.getInt(DatabaseMappings.FIELD_LOCATION_FK));

                        if (loc != null) {
                            ProtectionEntry pe = ProtectionMapper.mapProtection(rs);
                            pe.setLocationEntry(loc);
                            pel.put(loc.getLocation(), pe);
                        } else {
                            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE,
                                    "Protection Entry ({0}) without LocationEntry found, this should be due a bug.",
                                    rs.getInt(DatabaseMappings.FIELD_ID));
                        }
                    }
                }
            }
        } catch (SQLException | FileNotFoundException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
        return pel;
    }

    public List<PlayerEntry> getPlayers() {
        List<PlayerEntry> pel = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(connectorString, user, password)) {
            try (PreparedStatement ps = connection.prepareStatement(readResource("sqls/getPlayers.sql"))) {
                ps.execute();
                try (ResultSet rs = ps.getResultSet()) {
                    while (rs.next()) {
                        PlayerEntry p = PlayerMapper.mapPlayer(rs);
                        p.setHomes(getLocations(p.getId(), 1));
                        p.setDeaths(getLocations(p.getId(), 2));
                        p.setPartner(getPlayerPartner(p.getId()));
                        p.setPlayerState(PlayerState.DEFAULT);

                        pel.add(p);
                    }
                }
            }
        } catch (SQLException | FileNotFoundException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
        return pel;
    }

    public void insertPlayer(@NotNull PlayerEntry pe) {
        try (Connection connection = DriverManager.getConnection(connectorString, user, password)) {
            try (PreparedStatement ps = connection.prepareStatement(readResource("sqls/insertPlayer.sql"))) {
                ps.setInt(1, pe.getCreatedBy());
                ps.setString(2, pe.getUuid());
                ps.setString(3, pe.getName());
                ps.setString(4, pe.getCustomName());
                ps.setInt(5, pe.getGroup().getId());

                ps.execute();
            }
        } catch (SQLException | FileNotFoundException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    public void insertGroup(@NotNull GroupEntry ge) {
        try (Connection connection = DriverManager.getConnection(connectorString, user, password)) {
            try (PreparedStatement ps = connection.prepareStatement(readResource("sqls/insertGroup.sql"))) {
                ps.setInt(1, ge.getId());
                ps.setString(2, ge.getName());
                ps.setString(3, ge.getPrefix());

                ps.execute();
            }
        } catch (SQLException | FileNotFoundException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    public void updatePlayer(@NotNull PlayerEntry pe) {
        try (Connection connection = DriverManager.getConnection(connectorString, user, password)) {
            try (PreparedStatement ps = connection.prepareStatement(readResource("sqls/updatePlayer.sql"))) {
                ps.setInt(1, pe.getId());
                ps.setInt(2, pe.getGroup().getId());
                ps.setBoolean(3, pe.isAfk());
                ps.setBoolean(4, pe.isFlying());
                ps.setString(5, pe.getName());
                ps.setString(6, pe.getCustomName());
                ps.setDouble(7, pe.getPurse());
                ps.setString(8, pe.getUuid());
                ps.execute();
            }
        } catch (SQLException | FileNotFoundException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    public List<LocationTypeEntry> getLocationTypes() {
        List<LocationTypeEntry> ll = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(connectorString, user, password)) {
            try (PreparedStatement ps = connection.prepareStatement(readResource("sqls/getLocationTypes.sql"))) {
                ps.execute();
                try (ResultSet rs = ps.getResultSet()) {
                    while (rs.next()) {
                        ll.add(LocationMapper.mapLocationType(rs));
                    }
                }
            }
        } catch (SQLException | FileNotFoundException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
        return ll;
    }

    public List<LocationEntry> getWarps() {
        List<LocationEntry> ll = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(connectorString, user, password)) {
            try (PreparedStatement ps = connection.prepareStatement(readResource("sqls/getWarps.sql"))) {
                ps.execute();
                try (ResultSet rs = ps.getResultSet()) {
                    while (rs.next()) {
                        ll.add(LocationMapper.mapLocation(rs));
                    }
                }
            }
        } catch (SQLException | FileNotFoundException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
        return ll;
    }

    public void insertLocation(@NotNull LocationEntry le) {
        try (Connection connection = DriverManager.getConnection(connectorString, user, password)) {
            try (PreparedStatement ps = connection.prepareStatement(readResource("sqls/insertLocation.sql"))) {
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

                ps.execute();
            }
        } catch (SQLException | FileNotFoundException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    public void deleteLocation(@NotNull LocationEntry le) {
        try (Connection connection = DriverManager.getConnection(connectorString, user, password)) {
            try (PreparedStatement ps = connection.prepareStatement(readResource("sqls/deleteLocation.sql"))) {
                ps.setInt(1, le.getPlayerId());
                ps.setInt(2, le.getId());
                ps.execute();
            }
        } catch (SQLException | FileNotFoundException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    public List<GroupEntry> getGroups() {
        List<GroupEntry> gel = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(connectorString, user, password)) {
            try (PreparedStatement ps = connection.prepareStatement(readResource("sqls/getGroups.sql"))) {
                ps.execute();
                try (ResultSet rs = ps.getResultSet()) {
                    while (rs.next()) {
                        gel.add(PlayerMapper.mapGroup(rs));
                    }
                }
            }
        } catch (SQLException | FileNotFoundException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
        return gel;
    }

    public BagTypeEntry getBagType(int type) {
        try (Connection connection = DriverManager.getConnection(connectorString, user, password)) {
            try (PreparedStatement ps = connection.prepareStatement(readResource("sqls/getBagTypeById.sql"))) {
                ps.setInt(1, type);
                ps.execute();
                try (ResultSet rs = ps.getResultSet()) {
                    if (rs.next()) {
                        return BagMapper.mapBagType(rs);
                    }
                }
            }
        } catch (SQLException | FileNotFoundException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
        return null;
    }

    public BagEntry getBag(int type, int playerFK) {
        try (Connection connection = DriverManager.getConnection(connectorString, user, password)) {
            try (PreparedStatement ps = connection.prepareStatement(readResource("sqls/getBagByPlayerAndType.sql"))) {
                ps.setInt(1, type);
                ps.setInt(2, playerFK);
                ps.execute();
                try (ResultSet rs = ps.getResultSet()) {
                    if (rs.next()) {
                        BagEntry be = BagMapper.mapBag(rs);
                        be.setBagType(getBagType(rs.getInt(DatabaseMappings.FIELD_BAG_TYPE_FK)));
                        return be;
                    }
                }
            }
        } catch (SQLException | FileNotFoundException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
        return null;
    }

    public void insertBag(int type, int id) {
        try (Connection connection = DriverManager.getConnection(connectorString, user, password)) {
            try (PreparedStatement ps = connection.prepareStatement(readResource("sqls/insertBag.sql"))) {
                ps.setInt(1, id);
                ps.setInt(2, id);
                ps.setInt(3, type);

                ps.execute();
            }
        } catch (SQLException | FileNotFoundException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    public List<BagTypeEntry> getBagTypes() {
        List<BagTypeEntry> bagTypeEntryList = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(connectorString, user, password)) {
            try (PreparedStatement ps = connection.prepareStatement(readResource("sqls/getBagTypes.sql"))) {
                ps.execute();

                try (ResultSet rs = ps.getResultSet()) {
                    while (rs.next()) {
                        bagTypeEntryList.add(BagMapper.mapBagType(rs));
                    }
                }
            }
        } catch (SQLException | FileNotFoundException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
        return bagTypeEntryList;
    }

    public List<BagEntry> getBags() {
        List<BagEntry> bel = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(connectorString, user, password)) {
            try (PreparedStatement ps = connection.prepareStatement(readResource("sqls/getBags.sql"))) {
                ps.execute();

                try (ResultSet rs = ps.getResultSet()) {
                    while (rs.next()) {
                        BagEntry be = BagMapper.mapBag(rs);
                        be.setBagType(getBagType(rs.getInt(DatabaseMappings.FIELD_BAG_TYPE_FK)));
                        bel.add(be);
                    }
                }
            }
        } catch (SQLException | FileNotFoundException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
        return bel;
    }

    public void updateBagEntry(@NotNull BagEntry be) {
        try (Connection connection = DriverManager.getConnection(connectorString, user, password)) {
            try (PreparedStatement ps = connection.prepareStatement(readResource("sqls/updateBag.sql"))) {
                ps.setInt(1, be.getPlayerId());
                for (int i = 0; i < BagHelper.BAG_SIZE; i++) {
                    ps.setInt(i + 2, be.getSlotValue(i));
                }
                ps.setInt(BagHelper.BAG_SIZE + 2, be.getId());
                ps.execute();
            }
        } catch (SQLException | FileNotFoundException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    public List<NPCEntry> getNPCs() {
        List<NPCEntry> bel = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(connectorString, user, password)) {
            try (PreparedStatement ps = connection.prepareStatement(readResource("sqls/getNPCs.sql"))) {
                ps.execute();

                try (ResultSet rs = ps.getResultSet()) {
                    while (rs.next()) {
                        bel.add(NPCMapper.mapNPC(rs));
                    }
                }
            }
        } catch (SQLException | FileNotFoundException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
        return bel;
    }

    public List<ProtectionLockEntry> getProtectionLocks() {
        List<ProtectionLockEntry> bel = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(connectorString, user, password)) {
            try (PreparedStatement ps = connection.prepareStatement(readResource("sqls/getProtectionLocks.sql"))) {
                ps.execute();
                try (ResultSet rs = ps.getResultSet()) {
                    while (rs.next()) {
                        bel.add(ProtectionMapper.mapProtectionLock(rs));
                    }
                }
            }
        } catch (SQLException | FileNotFoundException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
        return bel;
    }

    public int cleanupLocations() {
        try (Connection connection = DriverManager.getConnection(connectorString, user, password)) {
            try (PreparedStatement ps = connection.prepareStatement(readResource("sqls/cleanupLocations.sql"))) {
                return ps.executeUpdate();
            }
        } catch (SQLException | FileNotFoundException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            return 0;
        }
    }

    public int cleanupProtections() {
        try (Connection connection = DriverManager.getConnection(connectorString, user, password)) {
            try (PreparedStatement ps = connection.prepareStatement(readResource("sqls/cleanupProtections.sql"))) {
                return ps.executeUpdate();
            }
        } catch (SQLException | FileNotFoundException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            return 0;
        }
    }
}