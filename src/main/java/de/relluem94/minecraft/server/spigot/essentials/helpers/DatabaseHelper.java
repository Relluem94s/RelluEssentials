package de.relluem94.minecraft.server.spigot.essentials.helpers;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.json.JSONObject;

import de.relluem94.rellulib.utils.TypeUtils;

import de.relluem94.minecraft.server.spigot.essentials.permissions.Groups;
import de.relluem94.minecraft.server.spigot.essentials.Strings;
import de.relluem94.minecraft.server.spigot.essentials.api.PlayerAPI;
import de.relluem94.minecraft.server.spigot.essentials.constants.PlayerState;

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
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.BagEntry;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.BagTypeEntry;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.BankAccountEntry;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.BankTierEntry;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.BankTransactionEntry;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.BlockHistoryEntry;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.CropEntry;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.DropEntry;

import static de.relluem94.minecraft.server.spigot.essentials.RelluEssentials.locationTypeEntryList;
import static de.relluem94.minecraft.server.spigot.essentials.constants.DatabaseConstants.PLUGIN_DATABASE_NAME;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.ChatHelper.consoleSendMessage;
import static de.relluem94.minecraft.server.spigot.essentials.RelluEssentials.pie;

/**
 *
 * @author rellu
 */
public class DatabaseHelper {

    private final String host;
    private final String user;
    private final String password;
    private final int port;

    private static final String CONNECTOR = "jdbc:mysql";
    private final String connectorString;
    private final String connectorStringInit;

    public DatabaseHelper(String host, String user, String password, int port) {
        this.host = host;
        this.user = user;
        this.password = password;
        this.port = port;
        connectorString = CONNECTOR + "://" + this.host + ":" + this.port + "/" + PLUGIN_DATABASE_NAME
                + "?useSSL=false&allowPublicKeyRetrieval=true";
        connectorStringInit = CONNECTOR + "://" + this.host + ":" + this.port
                + "?useSSL=false&allowPublicKeyRetrieval=true";
    }

    public String readResource(final String fileName) throws FileNotFoundException {

        String out = "";
        try (InputStream is = getClass().getResourceAsStream("/" + fileName);
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader br = new BufferedReader(isr)) {
            String line;
            StringBuilder sb = new StringBuilder();
            while ((line = br.readLine()) != null) {
                sb.append(line + Strings.PLUGIN_EOL);
            }

            out = sb.toString();
        } catch (IOException | NullPointerException e) {
            throw new FileNotFoundException(String.format("File %s was not found!", fileName));
        }

        return out;
    }

    public void select() {
        try (Connection connection = DriverManager.getConnection(connectorString, user, password)) {
            try (PreparedStatement ps = connection.prepareStatement(readResource("sqls/insertPlayer.sql"))) {
                ps.execute();
            }
        } catch (SQLException | FileNotFoundException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void patch1() {
        String v = "patches/v1/";
        consoleSendMessage(Strings.PLUGIN_NAME_CONSOLE, "applying " + v);
        executeScriptNoSchema(v + "createSchema.sql");
        executeScript(v + "createGroup.sql");
        executeScript(v + "createPlayer.sql");
        executeScript(v + "createLocationType.sql");
        executeScript(v + "createLocation.sql");
        executeScript(v + "createBlockHistory.sql");
        executeScript(v + "createPluginInformation.sql");
        executeScript(v + "insertGroups.sql");
        executeScript(v + "insertPlayers.sql");
        executeScript(v + "insertLocationTypes.sql");
        executeScript(v + "insertPluginInformation.sql");

        pie = getPluginInformation();

        List<PlayerEntry> pel = getPlayers();
        pel.forEach(p -> PlayerAPI.putPlayerEntry(UUID.fromString(p.getUUID()), p));

        ConfigHelper ch = new ConfigHelper("players");

        if (ch.isConfigFound()) {
            List<PlayerEntry> pe = ch.getPlayers();
            pe.forEach(this::insertPlayer);

            for (PlayerEntry p : pe) {
                PlayerEntry pu = PlayerAPI.getPlayerEntry(UUID.fromString(p.getUUID()));
                pu.setAFK(p.isAFK());
                pu.setFlying(p.isFlying());
                pu.setCustomName(p.getCustomName());
                pu.setUpdatedBy(1);
                updatePlayer(pu);

                List<LocationEntry> lel = ch.getHomes(pu);
                lel.forEach(this::insertLocation);
            }
        }
    }

    private void patch2() {
        String v = "patches/v2/";
        consoleSendMessage(Strings.PLUGIN_NAME_CONSOLE, "applying " + v);
        executeScript(v + "dropBlockHistory.sql");
        executeScript(v + "createBlockHistory.sql");
        executeScript(v + "insertNewDBVersion.sql");
        executeScript(v + "updateOldPluginInformation.sql");
    }

    private void patch3() {
        String v = "patches/v3/";
        executeScript(v + "dropPlayerConstraint.sql");
        executeScript(v + "updateAdminGroup.sql"); // changed id of Admin
        executeScript(v + "updateModGroup.sql"); // changed id of Mod
        executeScript(v + "updateVipGroup.sql"); // changed id of Vip
        executeScript(v + "updateAdminGroupPlayer.sql"); // changed id of Admin
        executeScript(v + "updateModGroupPlayer.sql"); // changed id of Mod
        executeScript(v + "updateVipGroupPlayer.sql"); // changed id of Vip
        executeScript(v + "addPlayerConstraint.sql");
        executeScript(v + "insertNewDBVersion.sql");
        executeScript(v + "updateOldPluginInformation.sql");
    }

    private void patch4() {
        String v = "patches/v4/";
        executeScript(v + "addBankTier.sql");
        executeScript(v + "addBankAccount.sql");
        executeScript(v + "addBagType.sql");
        executeScript(v + "addBag.sql");
        executeScript(v + "addBankTransaction.sql");
        executeScript(v + "addPermission.sql");
        executeScript(v + "addPermissionGroup.sql");
        executeScript(v + "addPermissionPlayer.sql");
        executeScript(v + "addProtections.sql");
        executeScript(v + "addSkills.sql");
        executeScript(v + "addSkillsPlayer.sql");
        executeScript(v + "addNPC.sql");
        executeScript(v + "addProtectionLocks.sql");

        executeScript(v + "insertProtectionLocks.sql");
        executeScript(v + "insertNPC.sql");
        executeScript(v + "insertSkills.sql");
        executeScript(v + "insertBankTier.sql");
        executeScript(v + "insertBagType.sql");
        executeScript(v + "insertLocationTypes.sql");
        executeScript(v + "alterPlayer.sql");
        executeScript(v + "alterBankAccount.sql");
        executeScript(v + "alterBankTier.sql");
        executeScript(v + "alterBankTransaction.sql");
        executeScript(v + "insertNewDBVersion.sql");
        executeScript(v + "updatePlayer.sql");
        executeScript(v + "updateOldPluginInformation.sql");
    }

    private void patch5() {
        String v = "patches/v5/";
        executeScript(v + "addSetting.sql");
        executeScript(v + "addPluginSetting.sql");
        executeScript(v + "addSettingPlayer.sql");
        executeScript(v + "addWorldGroup.sql");
        executeScript(v + "addWorld.sql");
        executeScript(v + "addWorldGroupInventory.sql");
        executeScript(v + "addWorldGroupSetting.sql");
        executeScript(v + "addCrops.sql");
        executeScript(v + "addDrops.sql");
        executeScript(v + "addPlayerPartner.sql");

        executeScript(v + "insertSkills.sql");
        executeScript(v + "insertSettings.sql");
        executeScript(v + "insertWorldGroup.sql");
        executeScript(v + "insertWorlds.sql");
        executeScript(v + "insertWorldGroupSetting.sql");
        executeScript(v + "insertBagType.sql");
        executeScript(v + "insertCrops.sql");
        executeScript(v + "insertDrops.sql");

        executeScript(v + "addPlayerName.sql");
        executeScript(v + "changePlayerCustomName.sql");

        executeScript(v + "insertNewDBVersion.sql");
        executeScript(v + "updateOldPluginInformation.sql");
    }

    public void init() {
        applyPatch(pie.getDbVersion());
    }

    private boolean insertScripts = false; // To add Scripts in Development without its own patch version

    private void applyPatch(int version) {
        switch (version) {
            case -1:
            case 0:
                patch1();
                patch2();
                patch3();
                patch4();
                patch5();
                break;
            case 1:
                patch2();
                patch3();
                patch4();
                patch5();
                break;
            case 2:
                patch3();
                patch4();
                patch5();
                break;
            case 3:
                patch4();
                patch5();
                break;
            case 4:
                patch5();
                break;
            default:

                if (insertScripts) {
                    String v = "patches/v5/";
                    executeScript(v + "script.sql");
                }
                break;
        }
    }

    // *****************************************************************************************************************************************//
    // //
    // PATCH END //
    // //
    // *****************************************************************************************************************************************//

    private void script(Connection connection, String script) {
        try (PreparedStatement ps = connection.prepareStatement(readResource("sqls/" + script))) {
            ps.execute();
        } catch (SQLException | FileNotFoundException ey) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, ey);
        }
    }

    private void executeScript(String script) {
        try (Connection connection = DriverManager.getConnection(connectorString, user, password)) {
            script(connection, script);
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void executeScriptNoSchema(String script) {
        try (Connection connection = DriverManager.getConnection(connectorStringInit, user,
                password)) {
            script(connection, script);
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<CropEntry> getCrops() {
        List<CropEntry> lce = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(connectorString, user, password)) {
            try (PreparedStatement ps = connection.prepareStatement(readResource("sqls/getCrops.sql"))) {
                ps.execute();
                try (ResultSet rs = ps.getResultSet()) {
                    while (rs.next()) {
                        CropEntry ce = new CropEntry();
                        ce.setId(rs.getInt("id"));
                        ce.setPlant(rs.getString("PLANT"));
                        ce.setSeed(rs.getString("SEED"));

                        lce.add(ce);
                    }
                }
            }
        } catch (SQLException | FileNotFoundException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);
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
                        DropEntry de = new DropEntry();
                        de.setId(rs.getInt("id"));
                        de.setMaterial(rs.getString("MATERIAL"));
                        de.setMin(rs.getInt("MIN_INT"));
                        de.setMax(rs.getInt("MAX_INT"));

                        lde.add(de);
                    }
                }
            }
        } catch (SQLException | FileNotFoundException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lde;
    }

    public void insertPlayerPartner(PlayerPartnerEntry ppe) {
        try (
                Connection connection = DriverManager.getConnection(connectorString, user, password)) {
            try (PreparedStatement ps = connection.prepareStatement(readResource("sqls/insertPlayerPartner.sql"))) {
                ps.setInt(1, ppe.getCreatedBy());
                ps.setInt(2, ppe.getFirstPlayerID());
                ps.setInt(3, ppe.getSecondPlayerID());
                ps.setBoolean(4, ppe.getShareProtections());

                ps.execute();
            }
        } catch (SQLException | FileNotFoundException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void deletePlayerPartner(PlayerPartnerEntry ppe) {
        try (Connection connection = DriverManager.getConnection(connectorString, user, password)) {
            try (PreparedStatement ps = connection.prepareStatement(readResource("sqls/deletePlayerPartner.sql"))) {
                ps.setInt(1, ppe.getDeletedBy());
                ps.setInt(2, ppe.getID());
                ps.execute();
            }
        } catch (SQLException | FileNotFoundException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void updatePlayerPartner(PlayerPartnerEntry ppe) {
        try (Connection connection = DriverManager.getConnection(connectorString, user, password)) {
            try (PreparedStatement ps = connection.prepareStatement(readResource("sqls/updatePlayerPartner.sql"))) {
                ps.setInt(1, ppe.getUpdatedBy());
                ps.setBoolean(2, ppe.getShareProtections());
                ps.setInt(1, ppe.getID());
                ps.execute();
            }
        } catch (SQLException | FileNotFoundException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public PlayerPartnerEntry getPlayerPartner(int playerFK) {
        try (Connection connection = DriverManager.getConnection(connectorString, user, password)) {
            try (PreparedStatement ps = connection.prepareStatement(readResource("sqls/getPlayerPartner.sql"))) {
                ps.setInt(1, playerFK);
                ps.setInt(2, playerFK);
                ps.execute();
                try (ResultSet rs = ps.getResultSet()) {
                    while (rs.next()) {
                        PlayerPartnerEntry ppe = new PlayerPartnerEntry();
                        ppe.setID(rs.getInt("id"));
                        ppe.setCreated(rs.getString("created"));
                        ppe.setCreatedby(rs.getInt("createdby"));
                        ppe.setUpdated(rs.getString("updated"));
                        ppe.setUpdatedBy(rs.getInt("updatedby"));
                        ppe.setDeleted(rs.getString("deleted"));
                        ppe.setDeletedBy(rs.getInt("deletedby"));
                        ppe.setFirstPlayerID(rs.getInt("first_partner_fk"));
                        ppe.setSecondPlayerID(rs.getInt("second_partner_fk"));
                        ppe.setShareProtections(rs.getBoolean("shareProtections"));

                        return ppe;
                    }
                }
            }
        } catch (SQLException | FileNotFoundException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public List<WorldGroupEntry> getWorldGroups() {
        List<WorldGroupEntry> lbte = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(connectorString, user, password)) {
            try (PreparedStatement ps = connection.prepareStatement(readResource("sqls/getWorldGroups.sql"))) {
                ps.execute();
                try (ResultSet rs = ps.getResultSet()) {
                    while (rs.next()) {
                        WorldGroupEntry wge = new WorldGroupEntry();
                        wge.setId(rs.getInt("id"));
                        wge.setCreated(rs.getString("created"));
                        wge.setCreatedby(rs.getInt("createdby"));
                        wge.setUpdated(rs.getString("updated"));
                        wge.setUpdatedBy(rs.getInt("updatedby"));
                        wge.setDeleted(rs.getString("deleted"));
                        wge.setDeletedBy(rs.getInt("deletedby"));
                        wge.setName(rs.getString("name"));

                        lbte.add(wge);
                    }
                }
            }
        } catch (SQLException | FileNotFoundException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lbte;
    }

    public List<WorldEntry> getWorldByGroup(WorldGroupEntry wge) {
        List<WorldEntry> lwe = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(connectorString, user, password)) {
            try (PreparedStatement ps = connection.prepareStatement(readResource("sqls/getWorldByGroup.sql"))) {
                ps.setInt(1, wge.getId());
                ps.execute();
                try (ResultSet rs = ps.getResultSet()) {
                    while (rs.next()) {
                        WorldEntry we = new WorldEntry();
                        we.setId(rs.getInt("id"));
                        we.setCreated(rs.getString("created"));
                        we.setCreatedby(rs.getInt("createdby"));
                        we.setUpdated(rs.getString("updated"));
                        we.setUpdatedBy(rs.getInt("updatedby"));
                        we.setDeleted(rs.getString("deleted"));
                        we.setDeletedBy(rs.getInt("deletedby"));
                        we.setName(rs.getString("name"));
                        we.setWorldGroup(wge);
                        lwe.add(we);
                    }
                }
            }
        } catch (SQLException | FileNotFoundException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lwe;
    }

    public void insertWorldGroupInventory(WorldGroupInventoryEntry wgie) {
        try (
                Connection connection = DriverManager.getConnection(connectorString, user, password)) {
            try (PreparedStatement ps = connection.prepareStatement(readResource("sqls/insertWorldInventoryByGroupAndPlayer.sql"))) {
                ps.setInt(1, wgie.getPlayerId());
                ps.setInt(2, wgie.getPlayerId());
                ps.setInt(3, wgie.getWorldGroupEntry().getId());
                ps.setString(4, wgie.getInventory().toString());
                ps.setDouble(5, wgie.getHealth());
                ps.setInt(6, wgie.getFoodLevel());
                ps.setInt(7, wgie.getTotalExperience());

                ps.execute();
            }
        } catch (SQLException | FileNotFoundException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void updateWorldGroupInventory(WorldGroupInventoryEntry wgie) {
        try (
                Connection connection = DriverManager.getConnection(connectorString, user, password)) {
            try (PreparedStatement ps = connection.prepareStatement(readResource("sqls/updateWorldInventoryByGroupAndPlayer.sql"))) {
                ps.setInt(1, wgie.getUpdatedBy());
                ps.setString(2, wgie.getInventory().toString());
                ps.setDouble(3, wgie.getHealth());
                ps.setInt(4, wgie.getFoodLevel());
                ps.setInt(5, wgie.getTotalExperience());
                ps.setInt(6, wgie.getPlayerId());
                ps.setInt(7, wgie.getWorldGroupEntry().getId());

                ps.execute();
            }
        } catch (SQLException | FileNotFoundException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public WorldGroupInventoryEntry getWorldGroupInventory(PlayerEntry pe, WorldGroupEntry wge) {
        try (Connection connection = DriverManager.getConnection(connectorString, user, password)) {
            try (PreparedStatement ps = connection.prepareStatement(readResource("sqls/getWorldInventoryByGroupAndPlayer.sql"))) {
                ps.setInt(1, wge.getId());
                ps.setInt(2, pe.getID());
                ps.execute();
                try (ResultSet rs = ps.getResultSet()) {
                    while (rs.next()) {
                        WorldGroupInventoryEntry wgie = new WorldGroupInventoryEntry();
                        wgie.setId(rs.getInt("id"));
                        wgie.setCreated(rs.getString("created"));
                        wgie.setCreatedby(rs.getInt("createdby"));
                        wgie.setUpdated(rs.getString("updated"));
                        wgie.setUpdatedBy(rs.getInt("updatedby"));
                        wgie.setDeleted(rs.getString("deleted"));
                        wgie.setDeletedBy(rs.getInt("deletedby"));
                        wgie.setPlayerId(rs.getInt("player_fk"));
                        wgie.setHealth(rs.getInt("health"));
                        wgie.setTotalExperience(rs.getInt("totalExperience"));
                        wgie.setFoodLevel(rs.getInt("food"));
                        wgie.setWorldGroup(wge);
                        wgie.setInventory(new JSONObject(rs.getString("inventory")));

                        return wgie;
                    }
                }
            }
        } catch (SQLException | FileNotFoundException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public void insertWorld(WorldEntry we) {
        try (
                Connection connection = DriverManager.getConnection(connectorString, user, password)) {
            try (PreparedStatement ps = connection.prepareStatement(readResource("sqls/insertWorld.sql"))) {

                ps.setInt(1, we.getCreatedBy());
                ps.setString(2, we.getName());
                ps.setInt(3, we.getWorldGroupEntry().getId());
                ps.setInt(4, we.getBuildGroup().getId());

                ps.execute();
            }
        } catch (SQLException | FileNotFoundException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void insertWorldGroup(WorldGroupEntry wge) {
        try (
                Connection connection = DriverManager.getConnection(connectorString, user, password)) {
            try (PreparedStatement ps = connection.prepareStatement(readResource("sqls/insertWorldGroup.sql"))) {
                ps.setInt(1, wge.getCreatedBy());
                ps.setString(2, wge.getName());

                ps.execute();
            }
        } catch (SQLException | FileNotFoundException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public WorldGroupEntry getWorldGroup(String name) {
        try (Connection connection = DriverManager.getConnection(connectorString, user, password)) {
            try (PreparedStatement ps = connection.prepareStatement(readResource("sqls/getWorldGroupByName.sql"))) {
                ps.setString(1, name);
                ps.execute();
                try (ResultSet rs = ps.getResultSet()) {
                    while (rs.next()) {
                        WorldGroupEntry wge = new WorldGroupEntry();
                        wge.setId(rs.getInt("id"));
                        wge.setCreated(rs.getString("created"));
                        wge.setCreatedby(rs.getInt("createdby"));
                        wge.setUpdated(rs.getString("updated"));
                        wge.setUpdatedBy(rs.getInt("updatedby"));
                        wge.setDeleted(rs.getString("deleted"));
                        wge.setDeletedBy(rs.getInt("deletedby"));
                        wge.setName(rs.getString("name"));

                        return wge;
                    }
                }
            }
        } catch (SQLException | FileNotFoundException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public LocationEntry getLocation(PlayerEntry pe, int type) {
        try (Connection connection = DriverManager.getConnection(connectorString, user, password)) {
            try (PreparedStatement ps = connection.prepareStatement(readResource("sqls/getLocationsByPlayer.sql"))) {
                ps.setInt(1, pe.getID());
                ps.execute();
                try (ResultSet rs = ps.getResultSet()) {
                    while (rs.next()) {
                        LocationEntry le = new LocationEntry();
                        le.setId(rs.getInt("id"));
                        le.setPlayerId(rs.getInt("player_fk"));
                        le.setLocationName(rs.getString("location_name"));
                        le.setLocation(new Location(Bukkit.getWorld(rs.getString("world")), rs.getFloat("x"),
                                rs.getFloat("y"), rs.getFloat("z"), rs.getFloat("yaw"), rs.getFloat("pitch")));
                        for (LocationTypeEntry lte : locationTypeEntryList) {
                            if (lte.getId() == type) {
                                le.setLocationType(lte);
                            }
                        }
                        return le;
                    }
                }
            }
        } catch (SQLException | FileNotFoundException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public List<LocationEntry> getLocations(PlayerEntry pe, int type) {
        List<LocationEntry> lle = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(connectorString, user, password)) {
            try (PreparedStatement ps = connection.prepareStatement(readResource("sqls/getLocationsByPlayer.sql"))) {
                ps.setInt(1, pe.getID());
                ps.execute();
                try (ResultSet rs = ps.getResultSet()) {
                    while (rs.next()) {
                        if (type != rs.getInt("location_type_fk")) {
                            continue;
                        }
                        LocationEntry le = new LocationEntry();
                        le.setId(rs.getInt("id"));
                        le.setPlayerId(rs.getInt("player_fk"));
                        le.setLocationName(rs.getString("location_name"));
                        le.setLocation(new Location(Bukkit.getWorld(rs.getString("world")), rs.getFloat("x"),
                                rs.getFloat("y"), rs.getFloat("z"), rs.getFloat("yaw"), rs.getFloat("pitch")));
                        for (LocationTypeEntry lte : locationTypeEntryList) {
                            if (type == lte.getId()) {
                                le.setLocationType(lte);
                            }
                        }
                        lle.add(le);
                    }
                }
            }
        } catch (SQLException | FileNotFoundException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lle;
    }

    public LocationEntry getLocation(Location l, int type) {
        try (Connection connection = DriverManager.getConnection(connectorString, user, password)) {
            try (PreparedStatement ps = connection.prepareStatement(readResource("sqls/getLocationByLocation.sql"))) {
                ps.setFloat(1, (float) l.getX());
                ps.setFloat(2, (float) l.getY());
                ps.setFloat(3, (float) l.getZ());
                ps.setInt(4, type);
                ps.execute();
                try (ResultSet rs = ps.getResultSet()) {
                    while (rs.next()) {
                        LocationEntry le = new LocationEntry();
                        le.setId(rs.getInt("id"));
                        le.setLocation(new Location(Bukkit.getWorld(rs.getString("world")), rs.getFloat("x"),
                                rs.getFloat("y"), rs.getFloat("z"), rs.getFloat("yaw"), rs.getFloat("pitch")));
                        le.setPlayerId(rs.getInt("player_fk"));
                        for (LocationTypeEntry lte : locationTypeEntryList) {
                            if (lte.getId() == type) {
                                le.setLocationType(lte);
                            }
                        }
                        return le;
                    }
                }
            }
        } catch (SQLException | FileNotFoundException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public LocationEntry getLocation(int id) {
        try (Connection connection = DriverManager.getConnection(connectorString, user, password)) {
            try (PreparedStatement ps = connection.prepareStatement(readResource("sqls/getLocationById.sql"))) {
                ps.setInt(1, id);

                ps.execute();
                try (ResultSet rs = ps.getResultSet()) {
                    while (rs.next()) {
                        LocationEntry le = new LocationEntry();
                        le.setId(rs.getInt("id"));
                        le.setLocation(new Location(Bukkit.getWorld(rs.getString("world")), rs.getFloat("x"),
                                rs.getFloat("y"), rs.getFloat("z"), rs.getFloat("yaw"), rs.getFloat("pitch")));
                        le.setPlayerId(rs.getInt("player_fk"));
                        for (LocationTypeEntry lte : locationTypeEntryList) {
                            if (lte.getId() == rs.getInt("location_type_fk")) {
                                le.setLocationType(lte);
                            }
                        }
                        return le;
                    }
                }
            }
        } catch (SQLException | FileNotFoundException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public PluginInformationEntry getPluginInformation() {
        PluginInformationEntry pie = new PluginInformationEntry();
        try (Connection connection = DriverManager.getConnection(connectorStringInit, user, password)) {

            try (PreparedStatement ps = connection.prepareStatement(readResource("sqls/getPluginInformation.sql"))) {
                ps.execute();
                try (ResultSet rs = ps.getResultSet()) {
                    while (rs.next()) {
                        pie.setId(rs.getInt("id"));
                        pie.setCreated(rs.getString("created"));
                        pie.setCreatedby(rs.getInt("createdby"));
                        pie.setTabheader(rs.getString("tab_header"));
                        pie.setTabfooter(rs.getString("tab_footer"));
                        pie.setMotdMessage(rs.getString("motd_message"));
                        pie.setMotdPlayers(rs.getInt("motd_players"));
                        pie.setDbVersion(rs.getInt("db_version"));
                        return pie;
                    }
                }
            }
        } catch (SQLException | FileNotFoundException ex) {
            consoleSendMessage(ex.getClass().getName(), ex.getMessage());
            pie.setDbVersion(-1); // standard pie if no Database version was found
            return pie;
        }
        return null;
    }

    public PlayerEntry getPlayer(String UUID) {
        try (Connection connection = DriverManager.getConnection(connectorString, user, password)) {
            try (PreparedStatement ps = connection.prepareStatement(readResource("sqls/getPlayer.sql"))) {
                ps.setString(1, UUID);
                ps.execute();
                try (ResultSet rs = ps.getResultSet()) {
                    while (rs.next()) {
                        PlayerEntry p = new PlayerEntry();
                        p.setCreated(rs.getString("created"));
                        p.setCreatedby(rs.getInt("createdby"));
                        p.setUpdated(rs.getString("updated"));
                        p.setUpdatedBy(rs.getInt("updatedby"));
                        p.setDeleted(rs.getString("deleted"));
                        p.setDeletedBy(rs.getInt("deletedby"));
                        p.setName(rs.getString("name"));
                        p.setCustomName(rs.getString("customname"));
                        p.setPurse(rs.getDouble("purse"));
                        p.setFlying(rs.getBoolean("fly"));
                        p.setAFK(rs.getBoolean("afk"));
                        p.setGroup(Groups.getGroup(rs.getInt("group_fk")));
                        p.setID(rs.getInt("id"));
                        p.setUUID(rs.getString("uuid"));
                        PlayerPartnerEntry ppe = getPlayerPartner(rs.getInt("id"));
                        p.setHomes(getLocations(p, 1));
                        p.setDeaths(getLocations(p, 2));
                        p.setPartner(ppe);
                        p.setPlayerState(PlayerState.DEFAULT);
                        return p;
                    }
                }
            }
        } catch (SQLException | FileNotFoundException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public BankAccountEntry getPlayerBankAccount(int playerFK) {
        try (Connection connection = DriverManager.getConnection(connectorString, user, password)) {
            try (PreparedStatement ps = connection.prepareStatement(readResource("sqls/getBankAccountByPlayer.sql"))) {
                ps.setInt(1, playerFK);
                ps.execute();
                try (ResultSet rs = ps.getResultSet()) {
                    while (rs.next()) {
                        BankAccountEntry bae = new BankAccountEntry();
                        bae.setId(rs.getInt("id"));
                        bae.setCreated(rs.getString("created"));
                        bae.setCreatedby(rs.getInt("createdby"));
                        bae.setUpdated(rs.getString("updated"));
                        bae.setUpdatedBy(rs.getInt("updatedby"));
                        bae.setDeleted(rs.getString("deleted"));
                        bae.setDeletedBy(rs.getInt("deletedby"));
                        bae.setValue(rs.getDouble("value"));
                        bae.setPlayerId(rs.getInt("player_fk"));
                        bae.setTier(getBankTier(rs.getInt("bank_tier_fk")));

                        return bae;
                    }
                }
            }
        } catch (SQLException | FileNotFoundException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public BankTierEntry getBankTier(int id) {
        try (Connection connection = DriverManager.getConnection(connectorString, user, password)) {
            try (PreparedStatement ps = connection.prepareStatement(readResource("sqls/getBankTier.sql"))) {
                ps.setInt(1, id);
                ps.execute();
                try (ResultSet rs = ps.getResultSet()) {
                    while (rs.next()) {
                        BankTierEntry bte = new BankTierEntry();
                        bte.setId(rs.getInt("id"));
                        bte.setName(rs.getString("name"));
                        bte.setLimit(rs.getLong("limit"));
                        bte.setInterest(rs.getDouble("interest"));
                        bte.setCost(rs.getLong("cost"));
                        return bte;
                    }
                }
            }
        } catch (SQLException | FileNotFoundException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public List<BankTierEntry> getBankTiers() {
        List<BankTierEntry> lbte = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(connectorString, user, password)) {
            try (PreparedStatement ps = connection.prepareStatement(readResource("sqls/getBankTiers.sql"))) {
                ps.execute();
                try (ResultSet rs = ps.getResultSet()) {
                    while (rs.next()) {
                        BankTierEntry bte = new BankTierEntry();
                        bte.setId(rs.getInt("id"));
                        bte.setName(rs.getString("name"));
                        bte.setLimit(rs.getLong("limit"));
                        bte.setInterest(rs.getDouble("interest"));
                        bte.setCost(rs.getLong("cost"));
                        lbte.add(bte);
                    }
                }
            }
        } catch (SQLException | FileNotFoundException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lbte;
    }

    public void insertBankAccount(BankAccountEntry bae) {
        try (
                Connection connection = DriverManager.getConnection(connectorString, user, password)) {
            try (PreparedStatement ps = connection.prepareStatement(readResource("sqls/insertBankAccount.sql"))) {
                ps.setInt(1, 1);
                ps.setInt(2, bae.getPlayerId());
                ps.setDouble(3, bae.getValue());
                ps.setInt(4, bae.getTier().getId());

                ps.execute();
            }
        } catch (SQLException | FileNotFoundException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void addTransactionToBank(int playerFK, int bankAccountFK, double transactionValue, double bankaccountTotal,
            int tier) {
        try (
                Connection connection = DriverManager.getConnection(connectorString, user, password)) {
            try (PreparedStatement ps = connection.prepareStatement(readResource("sqls/insertBankTransaction.sql"))) {
                ps.setInt(1, playerFK);
                ps.setInt(2, bankAccountFK);
                ps.setDouble(3, transactionValue);

                ps.execute();
                updateBankAccount(playerFK, transactionValue, bankaccountTotal, tier);
            }
        } catch (SQLException | FileNotFoundException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void updateBankAccount(int playerFK, double transactionValue, double bankaccountTotal, int tier) {
        try (
                Connection connection = DriverManager.getConnection(connectorString, user, password)) {
            try (PreparedStatement ps = connection.prepareStatement(readResource("sqls/updateBankAccount.sql"))) {

                ps.setInt(1, playerFK);
                ps.setDouble(2, (bankaccountTotal + transactionValue));
                ps.setInt(3, tier);
                ps.setInt(4, playerFK);
                ps.execute();

            }
        } catch (SQLException | FileNotFoundException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);
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
                        BankTransactionEntry b = new BankTransactionEntry();

                        b.setID(rs.getInt("id"));
                        b.setCreated(rs.getString("created"));
                        b.setCreatedby(rs.getInt("createdby"));
                        b.setUpdated(rs.getString("updated"));
                        b.setUpdatedBy(rs.getInt("updatedby"));
                        b.setDeleted(rs.getString("deleted"));
                        b.setDeletedBy(rs.getInt("deletedby"));
                        b.setBankAccountId(rs.getInt("bank_account_fk"));
                        b.setValue(rs.getDouble("value"));
                        bte.add(b);
                    }
                }
            }
        } catch (SQLException | FileNotFoundException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return bte;
    }

    public ProtectionEntry getProtectionByLocation(Location l) {
        try (Connection connection = DriverManager.getConnection(connectorString, user, password)) {
            try (PreparedStatement ps = connection.prepareStatement(readResource("sqls/getProtectionByLocation.sql"))) {
                ps.setFloat(1, (float) l.getX());
                ps.setFloat(2, (float) l.getY());
                ps.setFloat(3, (float) l.getZ());
                ps.execute();
                try (ResultSet rs = ps.getResultSet()) {
                    while (rs.next()) {
                        ProtectionEntry pe = new ProtectionEntry();
                        pe.setId(rs.getInt("id"));
                        pe.setCreated(rs.getString("created"));
                        pe.setCreatedby(rs.getInt("createdby"));
                        pe.setUpdated(rs.getString("updated"));
                        pe.setUpdatedBy(rs.getInt("updatedby"));
                        pe.setDeletedBy(rs.getInt("deletedby"));
                        pe.setFlags(new JSONObject(rs.getString("flags")));
                        pe.setRights(new JSONObject(rs.getString("rights")));
                        pe.setMaterialName(rs.getString("material_name"));
                        pe.setLocationEntry(getLocation(l, 5));

                        return pe;
                    }
                }
            }
        } catch (SQLException | FileNotFoundException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public void insertProtection(ProtectionEntry pe) {
        try (Connection connection = DriverManager.getConnection(connectorString, user, password)) {
            try (PreparedStatement ps = connection.prepareStatement(readResource("sqls/insertProtection.sql"))) {

                ps.setInt(1, pe.getLocation().getPlayerId());
                ps.setInt(2, pe.getLocation().getId());
                ps.setString(3, pe.getMaterialName());
                ps.setString(4, pe.getFlags().toString());
                ps.setString(5, pe.getRights().toString());

                ps.execute();
            }
        } catch (SQLException | FileNotFoundException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void deleteProtection(ProtectionEntry pe) {
        deleteLocation(pe.getLocation());

        try (Connection connection = DriverManager.getConnection(connectorString, user, password)) {
            try (PreparedStatement ps = connection.prepareStatement(readResource("sqls/deleteProtection.sql"))) {
                ps.setInt(1, pe.getLocation().getPlayerId());
                ps.setInt(2, pe.getId());
                ps.execute();
            }
        } catch (SQLException | FileNotFoundException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void updateProtectionFlag(ProtectionEntry pe) {
        deleteLocation(pe.getLocation());

        try (Connection connection = DriverManager.getConnection(connectorString, user, password)) {
            try (PreparedStatement ps = connection.prepareStatement(readResource("sqls/updateProtectionFlags.sql"))) {
                ps.setInt(1, pe.getLocation().getPlayerId());
                ps.setString(2, pe.getFlags().toString());
                ps.setInt(3, pe.getId());
                ps.execute();
            }
        } catch (SQLException | FileNotFoundException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void updateProtectionRight(ProtectionEntry pe) {
        deleteLocation(pe.getLocation());

        try (Connection connection = DriverManager.getConnection(connectorString, user, password)) {
            try (PreparedStatement ps = connection.prepareStatement(readResource("sqls/updateProtectionRights.sql"))) {
                ps.setInt(1, pe.getLocation().getPlayerId());
                ps.setString(2, pe.getRights().toString());
                ps.setInt(3, pe.getId());
                ps.execute();
            }
        } catch (SQLException | FileNotFoundException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Map<Location, ProtectionEntry> getProtections() {
        HashMap<Location, ProtectionEntry> pel = new HashMap<>();
        try (Connection connection = DriverManager.getConnection(connectorString, user, password)) {
            try (PreparedStatement ps = connection.prepareStatement(readResource("sqls/getProtections.sql"))) {
                ps.execute();
                try (ResultSet rs = ps.getResultSet()) {
                    while (rs.next()) {
                        LocationEntry loc = getLocation(rs.getInt("location_fk"));

                        if (loc != null) {
                            ProtectionEntry pe = new ProtectionEntry();
                            pe.setId(rs.getInt("id"));
                            pe.setCreated(rs.getString("created"));
                            pe.setCreatedby(rs.getInt("createdby"));
                            pe.setUpdated(rs.getString("updated"));
                            pe.setUpdatedBy(rs.getInt("updatedby"));
                            pe.setDeletedBy(rs.getInt("deletedby"));
                            pe.setFlags(new JSONObject(rs.getString("flags")));
                            pe.setRights(new JSONObject(rs.getString("rights")));
                            pe.setMaterialName(rs.getString("material_name"));
                            pe.setLocationEntry(loc);
                            pel.put(loc.getLocation(), pe);
                        } else {
                            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE,
                                    "Protection Entry ({0}) without LocationEntry found, this should be due a bug.",
                                    rs.getInt("id"));
                        }
                    }
                }
            }
        } catch (SQLException | FileNotFoundException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);
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
                        PlayerEntry p = new PlayerEntry();
                        p.setCreated(rs.getString("created"));
                        p.setCreatedby(rs.getInt("createdby"));
                        p.setUpdated(rs.getString("updated"));
                        p.setUpdatedBy(rs.getInt("updatedby"));
                        p.setDeleted(rs.getString("deleted"));
                        p.setDeletedBy(rs.getInt("deletedby"));
                        p.setName(rs.getString("name"));
                        p.setCustomName(rs.getString("customname"));
                        p.setPurse(rs.getDouble("purse"));
                        p.setFlying(rs.getBoolean("fly"));
                        p.setAFK(rs.getBoolean("afk"));
                        p.setGroup(de.relluem94.minecraft.server.spigot.essentials.permissions.Groups
                                .getGroup(rs.getInt("group_fk")));
                        p.setID(rs.getInt("id"));
                        p.setUUID(rs.getString("uuid"));
                        PlayerPartnerEntry ppe = getPlayerPartner(rs.getInt("id"));
                        p.setHomes(getLocations(p, 1));
                        p.setDeaths(getLocations(p, 2));
                        p.setPartner(ppe);
                        p.setPlayerState(PlayerState.DEFAULT);

                        pel.add(p);
                    }
                }
            }
        } catch (SQLException | FileNotFoundException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return pel;
    }

    public void insertPlayer(PlayerEntry pe) {
        try (Connection connection = DriverManager.getConnection(connectorString, user, password)) {
            try (PreparedStatement ps = connection.prepareStatement(readResource("sqls/insertPlayer.sql"))) {
                ps.setInt(1, pe.getCreatedBy());
                ps.setString(2, pe.getUUID());
                ps.setString(3, pe.getName());
                ps.setString(4, pe.getCustomName());
                ps.setInt(5, pe.getGroup().getId());

                ps.execute();
            }
        } catch (SQLException | FileNotFoundException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void insertGroup(GroupEntry ge) {
        try (Connection connection = DriverManager.getConnection(connectorString, user, password)) {
            try (PreparedStatement ps = connection.prepareStatement(readResource("sqls/insertGroup.sql"))) {
                ps.setInt(1, ge.getId());
                ps.setString(2, ge.getName());
                ps.setString(3, ge.getPrefix());

                ps.execute();
            }
        } catch (SQLException | FileNotFoundException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void updatePlayer(PlayerEntry pe) {
        try (Connection connection = DriverManager.getConnection(connectorString, user, password)) {
            try (PreparedStatement ps = connection.prepareStatement(readResource("sqls/updatePlayer.sql"))) {
                ps.setInt(1, pe.getID());
                ps.setInt(2, pe.getGroup().getId());
                ps.setBoolean(3, pe.isAFK());
                ps.setBoolean(4, pe.isFlying());
                ps.setString(5, pe.getName());
                ps.setString(6, pe.getCustomName());
                ps.setDouble(7, pe.getPurse());
                ps.setString(8, pe.getUUID());
                ps.execute();
            }
        } catch (SQLException | FileNotFoundException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<LocationTypeEntry> getLocationTypes() {
        List<LocationTypeEntry> ll = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(connectorString, user, password)) {
            try (PreparedStatement ps = connection.prepareStatement(readResource("sqls/getLocationTypes.sql"))) {
                ps.execute();
                try (ResultSet rs = ps.getResultSet()) {
                    while (rs.next()) {
                        LocationTypeEntry lte = new LocationTypeEntry();
                        lte.setId(rs.getInt("id"));
                        lte.setType(rs.getString("location_type"));
                        ll.add(lte);
                    }
                }
            }
        } catch (SQLException | FileNotFoundException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ll;
    }

    public List<LocationEntry> getLocations() {
        List<LocationEntry> ll = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(connectorString, user, password)) {
            try (PreparedStatement ps = connection.prepareStatement(readResource("sqls/getLocations.sql"))) {
                ps.execute();
                try (ResultSet rs = ps.getResultSet()) {
                    while (rs.next()) {
                        LocationEntry p = new LocationEntry();
                        p.setLocation(new Location(Bukkit.getWorld(rs.getString("world")), rs.getFloat("x"),
                                rs.getFloat("y"), rs.getFloat("z"), rs.getFloat("yaw"), rs.getFloat("pitch")));
                        p.setLocationType(locationTypeEntryList.get(rs.getInt("location_type_fk") - 1));
                        p.setPlayerId(rs.getInt("player_fk"));
                        p.setLocationName(rs.getString("location_name"));
                        p.setId(rs.getInt("id"));
                        ll.add(p);
                    }
                }
            }
        } catch (SQLException | FileNotFoundException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);
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
                        LocationEntry p = new LocationEntry();
                        p.setLocation(new Location(Bukkit.getWorld(rs.getString("world")), rs.getFloat("x"),
                                rs.getFloat("y"), rs.getFloat("z"), rs.getFloat("yaw"), rs.getFloat("pitch")));
                        p.setLocationType(locationTypeEntryList.get(rs.getInt("location_type_fk") - 1));
                        p.setPlayerId(rs.getInt("player_fk"));
                        p.setLocationName(rs.getString("location_name"));
                        p.setId(rs.getInt("id"));
                        ll.add(p);
                    }
                }
            }
        } catch (SQLException | FileNotFoundException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ll;
    }

    public void insertLocation(LocationEntry le) {

        try (Connection connection = DriverManager.getConnection(connectorString, user, password)) {
            try (PreparedStatement ps = connection.prepareStatement(readResource("sqls/insertLocation.sql"))) {
                Location l = le.getLocation();

                ps.setInt(1, le.getPlayerId());
                ps.setFloat(2, (float) l.getX());
                ps.setFloat(3, (float) l.getY());
                ps.setFloat(4, (float) l.getZ());
                ps.setFloat(5, l.getYaw());
                ps.setFloat(6, l.getPitch());
                ps.setString(7, l.getWorld().getName());
                ps.setString(8, le.getLocationName());
                ps.setInt(9, le.getLocationType().getId());
                ps.setInt(10, le.getPlayerId());

                ps.execute();
            }
        } catch (SQLException | FileNotFoundException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void deleteLocation(LocationEntry le) {
        try (Connection connection = DriverManager.getConnection(connectorString, user, password)) {
            try (PreparedStatement ps = connection.prepareStatement(readResource("sqls/deleteLocation.sql"))) {
                ps.setInt(1, le.getPlayerId());
                ps.setInt(2, le.getId());
                ps.execute();
            }
        } catch (SQLException | FileNotFoundException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public BlockHistoryEntry getBlockHistoryByLocation(Location l) {
        try (Connection connection = DriverManager.getConnection(connectorString, user, password)) {
            try (PreparedStatement ps = connection.prepareStatement(readResource("sqls/getBlockHistoryByLocation.sql"))) {
                ps.setFloat(1, l.getBlockX());
                ps.setFloat(2, l.getBlockY());
                ps.setFloat(3, l.getBlockZ());

                ps.execute();

                try (ResultSet rs = ps.getResultSet()) {
                    while (rs.next()) {
                        BlockHistoryEntry bh = new BlockHistoryEntry();
                        bh.setId(rs.getInt("id"));
                        bh.setCreated(rs.getString("created"));
                        bh.setCreatedby(rs.getInt("createdby"));
                        bh.setDeleted(rs.getString("deleted"));
                        bh.setDeletedby(rs.getInt("deletedby"));

                        LocationEntry le = new LocationEntry();
                        le.setLocation(new Location(Bukkit.getWorld(rs.getString("world")), rs.getFloat("x"),
                                rs.getFloat("y"), rs.getFloat("z"), rs.getFloat("yaw"), rs.getFloat("pitch")));
                        le.setLocationType(locationTypeEntryList.get(rs.getInt("location_type_fk") - 1));
                        le.setPlayerId(rs.getInt("player_fk"));
                        le.setLocationName(rs.getString("location_name"));
                        le.setId(rs.getInt("l.id"));

                        bh.setLocation(le);
                        bh.setMaterial(rs.getString("material"));
                        return bh;
                    }
                }
            }
        } catch (SQLException | FileNotFoundException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    public void insertBlockHistory(BlockHistoryEntry bh) {
        try (Connection connection = DriverManager.getConnection(connectorString, user, password)) {
            try (PreparedStatement ps = connection.prepareStatement(readResource("sqls/insertBlockHistory.sql"))) {
                ps.setInt(1, bh.getCreatedby());
                ps.setInt(2, bh.getLocation().getId());
                ps.setString(3, bh.getMaterial());

                ps.execute();
            }
        } catch (SQLException | FileNotFoundException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<BlockHistoryEntry> getBlockHistoryByPlayer(PlayerEntry p) {
        List<BlockHistoryEntry> bhe = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(connectorString, user, password)) {
            try (PreparedStatement ps = connection.prepareStatement(readResource("sqls/getBlockHistoryByPlayer.sql"))) {
                ps.setFloat(1, p.getID());

                ps.execute();

                try (ResultSet rs = ps.getResultSet()) {
                    while (rs.next()) {
                        BlockHistoryEntry bh = new BlockHistoryEntry();
                        bh.setId(rs.getInt("id"));
                        bh.setCreated(rs.getString("created"));
                        bh.setCreatedby(rs.getInt("createdby"));
                        bh.setDeleted(rs.getString("deleted"));
                        bh.setDeletedby(rs.getInt("deletedby"));

                        LocationEntry le = new LocationEntry();
                        le.setLocation(new Location(Bukkit.getWorld(rs.getString("world")), rs.getFloat("x"),
                                rs.getFloat("y"), rs.getFloat("z"), rs.getFloat("yaw"), rs.getFloat("pitch")));
                        le.setLocationType(locationTypeEntryList.get(rs.getInt("location_type_fk") - 1));
                        le.setPlayerId(rs.getInt("player_fk"));
                        le.setLocationName(rs.getString("location_name"));
                        le.setId(rs.getInt("l.id"));

                        bh.setLocation(le);
                        bh.setMaterial(rs.getString("material"));
                        bhe.add(bh);
                    }
                }
            }
        } catch (SQLException | FileNotFoundException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);
        }

        return bhe;
    }

    public List<BlockHistoryEntry> getBlockHistoryByPlayerAndTime(PlayerEntry p, String time, boolean deleted) {
        List<BlockHistoryEntry> bhe = new ArrayList<>();
        int year = 0;
        int month = 0;
        int day = 0;
        int hour = 0;
        int minute = 0;

        try (Connection connection = DriverManager.getConnection(connectorString, user, password)) {
            try (PreparedStatement ps = connection.prepareStatement(readResource(deleted ? "sqls/getBlockHistoryByPlayerAndTimeIsDeleted.sql"
                            : "sqls/getBlockHistoryByPlayerAndTime.sql"))) {
                String[] times = time.split("(?<!\\d){1,6}");

                for (String t : times) {
                    if (t.endsWith("Y") && TypeUtils.isInt(t.replaceAll("[^\\d.]", ""))) {
                        year += Integer.parseInt(t.replaceAll("[^\\d.]", ""));
                    } else if (t.endsWith("M") && TypeUtils.isInt(t.replaceAll("[^\\d.]", ""))) {
                        month += Integer.parseInt(t.replaceAll("[^\\d.]", ""));
                    } else if (t.endsWith("D") && TypeUtils.isInt(t.replaceAll("[^\\d.]", ""))) {
                        day += Integer.parseInt(t.replaceAll("[^\\d.]", ""));
                    } else if (t.endsWith("h") && TypeUtils.isInt(t.replaceAll("[^\\d.]", ""))) {
                        hour += Integer.parseInt(t.replaceAll("[^\\d.]", ""));
                    } else if (t.endsWith("m") && TypeUtils.isInt(t.replaceAll("[^\\d.]", ""))) {
                        minute += Integer.parseInt(t.replaceAll("[^\\d.]", ""));
                    }
                }

                ps.setFloat(1, p.getID());
                ps.setInt(2, minute);
                ps.setInt(3, hour);
                ps.setInt(4, day);
                ps.setInt(5, month);
                ps.setInt(6, year);

                ps.execute();

                try (ResultSet rs = ps.getResultSet()) {
                    while (rs.next()) {
                        BlockHistoryEntry bh = new BlockHistoryEntry();
                        bh.setId(rs.getInt("id"));
                        bh.setCreated(rs.getString("created"));
                        bh.setCreatedby(rs.getInt("createdby"));
                        if (!deleted) {
                            bh.setDeleted(rs.getString("deleted"));
                            bh.setDeletedby(rs.getInt("deletedby"));
                        }

                        LocationEntry le = new LocationEntry();
                        le.setLocation(new Location(Bukkit.getWorld(rs.getString("world")), rs.getFloat("x"),
                                rs.getFloat("y"), rs.getFloat("z"), rs.getFloat("yaw"), rs.getFloat("pitch")));
                        le.setLocationType(locationTypeEntryList.get(rs.getInt("location_type_fk") - 1));
                        le.setPlayerId(rs.getInt("player_fk"));
                        le.setLocationName(rs.getString("location_name"));
                        le.setId(rs.getInt("l.id"));

                        bh.setLocation(le);
                        bh.setMaterial(rs.getString("material"));
                        bhe.add(bh);
                    }
                }
            }
        } catch (SQLException | FileNotFoundException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return bhe;
    }

    public void deleteBlockHistory(BlockHistoryEntry bh) {
        try (Connection connection = DriverManager.getConnection(connectorString, user, password)) {
            try (PreparedStatement ps = connection.prepareStatement(readResource("sqls/deleteBlockHistory.sql"))) {
                ps.setInt(1, bh.getDeletedby());
                ps.setInt(2, bh.getLocation().getId());

                ps.execute();
            }
        } catch (SQLException | FileNotFoundException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<GroupEntry> getGroups() {
        List<GroupEntry> gel = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(connectorString, user, password)) {
            try (PreparedStatement ps = connection.prepareStatement(readResource("sqls/getGroups.sql"))) {
                ps.execute();
                try (ResultSet rs = ps.getResultSet()) {
                    while (rs.next()) {
                        GroupEntry g = new GroupEntry();
                        g.setId(rs.getInt("id"));
                        g.setName(rs.getString("name"));
                        g.setPrefix(rs.getString("prefix"));
                        gel.add(g);
                    }
                }
            }
        } catch (SQLException | FileNotFoundException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return gel;
    }

    public BagTypeEntry getBagType(int type) {
        try (Connection connection = DriverManager.getConnection(connectorString, user, password)) {
            try (PreparedStatement ps = connection.prepareStatement(readResource("sqls/getBagTypeById.sql"))) {
                ps.setInt(1, type);
                ps.execute();
                try (ResultSet rs = ps.getResultSet()) {
                    while (rs.next()) {
                        BagTypeEntry bte = new BagTypeEntry();

                        bte.setId(rs.getInt("id"));
                        bte.setDisplayName(rs.getString("displayname"));
                        bte.setName(rs.getString("name"));
                        bte.setCost(rs.getInt("cost"));

                        for (int i = 0; i <= 13; i++) {
                            bte.setSlotName(i, rs.getString("slot_" + (i + 1) + "_name"));
                        }
                        return bte;
                    }
                }
            }
        } catch (SQLException | FileNotFoundException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);
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
                    while (rs.next()) {
                        BagEntry be = new BagEntry();
                        be.setId(rs.getInt("id"));
                        be.setCreated(rs.getString("created"));
                        be.setCreatedby(rs.getInt("createdby"));
                        be.setUpdated(rs.getString("updated"));
                        be.setUpdatedBy(rs.getInt("updatedby"));
                        be.setDeletedBy(rs.getInt("deletedby"));

                        be.setBagTypeId(rs.getInt("bag_type_fk"));
                        be.setPlayerId(rs.getInt("player_fk"));

                        be.setBagType(getBagType(type));

                        for (int i = 0; i <= 13; i++) {
                            be.setSlotValue(i, rs.getInt("slot_" + (i + 1) + "_value"));
                        }

                        return be;
                    }
                }
            }
        } catch (SQLException | FileNotFoundException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<BagTypeEntry> getBagTypes() {
        List<BagTypeEntry> btel = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(connectorString, user, password)) {
            try (PreparedStatement ps = connection.prepareStatement(readResource("sqls/getBagTypes.sql"))) {
                ps.execute();

                try (ResultSet rs = ps.getResultSet()) {
                    while (rs.next()) {
                        BagTypeEntry bte = new BagTypeEntry();

                        bte.setId(rs.getInt("id"));
                        bte.setDisplayName(rs.getString("displayname"));
                        bte.setName(rs.getString("name"));
                        bte.setCost(rs.getInt("cost"));

                        for (int i = 0; i <= 13; i++) {
                            bte.setSlotName(i, rs.getString("slot_" + (i + 1) + "_name"));
                        }

                        btel.add(bte);
                    }
                }
            }
        } catch (SQLException | FileNotFoundException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return btel;
    }

    public List<BagEntry> getBags() {
        List<BagEntry> bel = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(connectorString, user, password)) {
            try (PreparedStatement ps = connection.prepareStatement(readResource("sqls/getBags.sql"))) {
                ps.execute();

                try (ResultSet rs = ps.getResultSet()) {
                    while (rs.next()) {
                        BagEntry be = new BagEntry();

                        be.setId(rs.getInt("id"));
                        be.setCreated(rs.getString("created"));
                        be.setCreatedby(rs.getInt("createdby"));
                        be.setUpdated(rs.getString("updated"));
                        be.setUpdatedBy(rs.getInt("updatedby"));
                        be.setDeletedBy(rs.getInt("deletedby"));

                        be.setBagTypeId(rs.getInt("bag_type_fk"));
                        be.setPlayerId(rs.getInt("player_fk"));

                        be.setBagType(getBagType(rs.getInt("bag_type_fk")));

                        for (int i = 0; i <= 13; i++) {
                            be.setSlotValue(i, rs.getInt("slot_" + (i + 1) + "_value"));
                        }

                        bel.add(be);
                    }
                }
            }
        } catch (SQLException | FileNotFoundException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return bel;
    }

    public void updateBagEntry(BagEntry be) {
        try (Connection connection = DriverManager.getConnection(connectorString, user, password)) {
            try (PreparedStatement ps = connection.prepareStatement(readResource("sqls/updateBag.sql"))) {

                ps.setInt(1, be.getPlayerId());

                for (int i = 0; i <= 13; i++) {
                    ps.setInt(i+2, be.getSlotValue(i));
                }

                ps.setInt(16, be.getId());

                ps.execute();
            }
        } catch (SQLException | FileNotFoundException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<NPCEntry> getNPCs() {
        List<NPCEntry> bel = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(connectorString, user, password)) {
            try (PreparedStatement ps = connection.prepareStatement(readResource("sqls/getNPCs.sql"))) {
                ps.execute();

                try (ResultSet rs = ps.getResultSet()) {
                    while (rs.next()) {
                        NPCEntry be = new NPCEntry();

                        be.setId(rs.getInt("id"));
                        be.setCreated(rs.getString("created"));
                        be.setCreatedby(rs.getInt("createdby"));
                        be.setUpdated(rs.getString("updated"));
                        be.setUpdatedBy(rs.getInt("updatedby"));
                        be.setDeletedBy(rs.getInt("deletedby"));

                        be.setName(rs.getString("name"));
                        be.setProfession(rs.getString("profession"));
                        be.setType(rs.getString("type"));

                        for (int i = 0; i <= 27; i++) {
                            be.setSlotName(i, rs.getString("slot_" + (i + 1) + "_name"));
                        }

                        bel.add(be);
                    }
                }
            }
        } catch (SQLException | FileNotFoundException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);
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
                        ProtectionLockEntry be = new ProtectionLockEntry();

                        be.setId(rs.getInt("id"));
                        be.setCreated(rs.getString("created"));
                        be.setCreatedby(rs.getInt("createdby"));
                        be.setUpdated(rs.getString("updated"));
                        be.setUpdatedBy(rs.getInt("updatedby"));
                        be.setDeletedBy(rs.getInt("deletedby"));
                        be.setValue(rs.getString("value"));

                        bel.add(be);
                    }
                }
            }
        } catch (SQLException | FileNotFoundException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return bel;
    }

}