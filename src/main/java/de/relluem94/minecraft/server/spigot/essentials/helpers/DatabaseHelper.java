package de.relluem94.minecraft.server.spigot.essentials.helpers;

import java.io.IOException;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import de.relluem94.rellulib.utils.LogUtils;
import de.relluem94.rellulib.utils.TypeUtils;

import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.GroupEntry;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.LocationEntry;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.LocationTypeEntry;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.PlayerEntry;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.PluginInformationEntry;
import de.relluem94.minecraft.server.spigot.essentials.Strings;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.BankAccountEntry;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.BankTierEntry;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.BankTransactionEntry;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.BlockHistoryEntry;
import de.relluem94.minecraft.server.spigot.essentials.permissions.Groups;

import static de.relluem94.minecraft.server.spigot.essentials.RelluEssentials.locationTypeEntryList;
import static de.relluem94.minecraft.server.spigot.essentials.RelluEssentials.playerEntryList;
import static de.relluem94.minecraft.server.spigot.essentials.constants.DatabaseConstants.PLUGIN_DATABASE_NAME;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.ChatHelper.consoleSendMessage;
import static de.relluem94.minecraft.server.spigot.essentials.RelluEssentials.DEBUG;
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

    private final String connector = "jdbc:mysql";
    private final String connectorString;
    private final String connectorStringInit;

    public DatabaseHelper(String host, String user, String password, int port) {
        this.host = host;
        this.user = user;
        this.password = password;
        this.port = port;
        connectorString = connector + "://" + this.host + ":" + this.port + "/" + PLUGIN_DATABASE_NAME + "?useSSL=false&allowPublicKeyRetrieval=true";
        connectorStringInit = connector + "://" + this.host + ":" + this.port + "?useSSL=false&allowPublicKeyRetrieval=true";
    }

    //*****************************************************************************************************************************************//
    //                                                                                                                                         //
    //                                                DUMMY                                                                                    //
    //                                                                                                                                         //
    //*****************************************************************************************************************************************//
    //DUMMY 
    public void select() {
        try (
                Connection connection = DriverManager.getConnection(connectorString, user, password)) {
            PreparedStatement ps = connection.prepareStatement(readResource("sqls/insertPlayer.sql", StandardCharsets.UTF_8));

            ps.execute();
        } catch (SQLException | IOException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public LocationEntry getLocation(PlayerEntry pe, int type) {
        try (
                Connection connection = DriverManager.getConnection(connectorString, user, password)) {
            PreparedStatement ps = connection.prepareStatement(readResource("sqls/getLocationsByPlayer.sql", StandardCharsets.UTF_8));
            ps.setInt(1, pe.getID());
            ps.execute();
            try (ResultSet rs = ps.getResultSet()) {
                while (rs.next()) {
                    LocationEntry le = new LocationEntry();
                    le.setId(rs.getInt("id"));
                    le.setLocationName(rs.getString("location_name"));
                    le.setLocation(new Location(Bukkit.getWorld(rs.getString("world")), rs.getFloat("x"), rs.getFloat("y"), rs.getFloat("z"), rs.getFloat("yaw"), rs.getFloat("pitch")));
                    le.setLocationType(locationTypeEntryList.get(type - 1)); //TODO WRONG
                    return le;
                }
            }
        } catch (SQLException | IOException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    //*****************************************************************************************************************************************//
    //                                                                                                                                         //
    //                                               DUMMY END                                                                                 //
    //                                                                                                                                         //
    //*****************************************************************************************************************************************//
    public LocationEntry getLocation(Location l, int type) {
        try (
                Connection connection = DriverManager.getConnection(connectorString, user, password)) {
            PreparedStatement ps = connection.prepareStatement(readResource("sqls/getLocationByLocation.sql", StandardCharsets.UTF_8));
            ps.setFloat(1, (float) l.getX());
            ps.setFloat(2, (float) l.getY());
            ps.setFloat(3, (float) l.getZ());
            ps.execute();
            try (ResultSet rs = ps.getResultSet()) {
                while (rs.next()) {
                    LocationEntry le = new LocationEntry();
                    le.setId(rs.getInt("id"));
                    le.setLocation(new Location(Bukkit.getWorld(rs.getString("world")), rs.getFloat("x"), rs.getFloat("y"), rs.getFloat("z"), rs.getFloat("yaw"), rs.getFloat("pitch")));
                    le.setLocationType(locationTypeEntryList.get(type - 1));
                    return le;
                }
            }
        } catch (SQLException | IOException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public PluginInformationEntry getPluginInformation() {
        PluginInformationEntry pie = new PluginInformationEntry();
        try (
                Connection connection = DriverManager.getConnection(connectorStringInit, user, password)) {
            // @TODO Remove Debug Strings if error is gone
            consoleSendMessage("1 - pie version: ", pie.getDbVersion() + "");
            PreparedStatement ps = connection.prepareStatement(readResource("sqls/getPluginInformation.sql", StandardCharsets.UTF_8));
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
                    consoleSendMessage("2 - pie version: ", pie.getDbVersion() + "");
                    return pie;
                }
            }
        } catch (SQLException | IOException ex) {
            consoleSendMessage(ex.getClass().getName(), ex.getMessage());
            pie.setDbVersion(-1); // standard pie if no Database version was found
            return pie;
        }
        consoleSendMessage("3 - pie version: ", pie.getDbVersion() + "");
        return null;
    }

    public PlayerEntry getPlayer(String UUID) {
        try (
                Connection connection = DriverManager.getConnection(connectorString, user, password)) {
            PreparedStatement ps = connection.prepareStatement(readResource("sqls/getPlayer.sql", StandardCharsets.UTF_8));
            ps.setString(1, UUID);
            ps.execute();
            try (ResultSet rs = ps.getResultSet()) {
                while (rs.next()) {
                    PlayerEntry p = new PlayerEntry();
                    p.setCreated(rs.getString("created"));
                    p.setCreatedby(rs.getInt("createdby"));
                    p.setUpdated(rs.getString("updated"));
                    p.setUpdatedBy(rs.getInt("updatedby"));
                    p.setCustomName(rs.getString("customname"));
                    p.setPurse(rs.getLong("purse"));
                    p.setDeletedBy(rs.getInt("deletedby"));
                    p.setFlying(rs.getBoolean("fly"));
                    p.setAFK(rs.getBoolean("afk"));
                    p.setGroup(Groups.getGroup(rs.getInt("group_fk")));
                    p.setID(rs.getInt("id"));
                    p.setUUID(rs.getString("uuid"));
                    return p;
                }
            }
        } catch (SQLException | IOException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }





















    public BankAccountEntry getPlayerBankAccount(int player_fk) {
        try (
                Connection connection = DriverManager.getConnection(connectorString, user, password)) {
            PreparedStatement ps = connection.prepareStatement(readResource("sqls/getBankAccountByPlayer.sql", StandardCharsets.UTF_8));
            ps.setInt(1, player_fk);
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
                    bae.setValue(rs.getFloat("value"));
                    bae.setTier(getBankTier(rs.getInt("bank_tier_fk")));
                    
                    return bae;
                }
            }
        } catch (SQLException | IOException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }



    public BankTierEntry getBankTier(int id) {
        try (
                Connection connection = DriverManager.getConnection(connectorString, user, password)) {
            PreparedStatement ps = connection.prepareStatement(readResource("sqls/getBankTier.sql", StandardCharsets.UTF_8));
            ps.setInt(1, id);
            ps.execute();
            try (ResultSet rs = ps.getResultSet()) {
                while (rs.next()) {
                    BankTierEntry bte = new BankTierEntry();
                    bte.setId(rs.getInt("id"));
                    bte.setName(rs.getString("name"));
                    bte.setLimit(rs.getInt("limit"));
                    bte.setInterest(rs.getFloat("interest"));
                    bte.setCost(rs.getInt("cost"));
                    return bte;
                }
            }
        } catch (SQLException | IOException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public void insertBankAccount(BankAccountEntry bae) {
        try (
            Connection connection = DriverManager.getConnection(connectorString, user, password)) {
            PreparedStatement ps = connection.prepareStatement(readResource("sqls/insertBankAccount.sql", StandardCharsets.UTF_8));
            ps.setInt(1, 1);
            ps.setInt(2, bae.getPlayerId());
            ps.setFloat(3, bae.getValue());
            ps.setInt(4, bae.getTier().getId());

            ps.execute();
        } catch (SQLException | IOException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void addTransactionToBank(int player_fk, int bank_account_fk, float transaction_value, float bankaccount_total, int tier) {
        try (
            Connection connection = DriverManager.getConnection(connectorString, user, password)) {
            PreparedStatement ps = connection.prepareStatement(readResource("sqls/insertBankTransaction.sql", StandardCharsets.UTF_8));
            ps.setInt(1, player_fk);
            ps.setInt(2, bank_account_fk);
            ps.setFloat(3, transaction_value);

            ps.execute();
            ps.close();

            ps = connection.prepareStatement(readResource("sqls/updateBankAccount.sql", StandardCharsets.UTF_8));
            ps.setInt(1, player_fk);
            ps.setFloat(2, bankaccount_total + transaction_value);
            ps.setInt(3, tier);
            ps.setInt(4, player_fk);
            ps.execute();


        } catch (SQLException | IOException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<BankTransactionEntry> getTransactionsToBankFromPlayer(int bank_account_fk) {
        List<BankTransactionEntry> bte = new ArrayList<>();
        try (
                Connection connection = DriverManager.getConnection(connectorString, user, password)) {
            PreparedStatement ps = connection.prepareStatement(readResource("sqls/getBankAccountTransactionsByPlayer.sql", StandardCharsets.UTF_8));
            ps.setInt(1, bank_account_fk);
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
                    b.setValue(rs.getFloat("value"));
                    bte.add(b);
                }
            }
        } catch (SQLException | IOException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return bte;
    }

























    public List<PlayerEntry> getPlayers() {
        List<PlayerEntry> pel = new ArrayList<>();
        try (
                Connection connection = DriverManager.getConnection(connectorString, user, password)) {
            PreparedStatement ps = connection.prepareStatement(readResource("sqls/getPlayers.sql", StandardCharsets.UTF_8));
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
                    p.setCustomName(rs.getString("customname"));
                    p.setPurse(rs.getLong("purse"));
                    p.setFlying(rs.getBoolean("fly"));
                    p.setAFK(rs.getBoolean("afk"));
                    p.setGroup(de.relluem94.minecraft.server.spigot.essentials.permissions.Groups.getGroup(rs.getInt("group_fk")));
                    p.setID(rs.getInt("id"));
                    p.setUUID(rs.getString("uuid"));

                    pel.add(p);
                }
            }
        } catch (SQLException | IOException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return pel;
    }

    public void insertPlayer(PlayerEntry pe) {
        try (
                Connection connection = DriverManager.getConnection(connectorString, user, password)) {
            PreparedStatement ps = connection.prepareStatement(readResource("sqls/insertPlayer.sql", StandardCharsets.UTF_8));
            ps.setInt(1, pe.getCreatedBy());
            ps.setString(2, pe.getUUID());
            ps.setInt(3, pe.getGroup().getId());

            ps.execute();
        } catch (SQLException | IOException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void insertGroup(GroupEntry ge) {
        try (
                Connection connection = DriverManager.getConnection(connectorString, user, password)) {
            PreparedStatement ps = connection.prepareStatement(readResource("sqls/insertGroup.sql", StandardCharsets.UTF_8));
            ps.setInt(1, ge.getId());
            ps.setString(2, ge.getName());
            ps.setString(3, ge.getPrefix());

            ps.execute();
        } catch (SQLException | IOException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void updatePlayer(PlayerEntry pe) {
        try (
                Connection connection = DriverManager.getConnection(connectorString, user, password)) {
            PreparedStatement ps = connection.prepareStatement(readResource("sqls/updatePlayer.sql", StandardCharsets.UTF_8));
            ps.setInt(1, pe.getID());
            ps.setInt(2, pe.getGroup().getId());
            ps.setBoolean(3, pe.isAFK());
            ps.setBoolean(4, pe.isFlying());
            ps.setString(5, pe.getCustomName());
            ps.setFloat(6, pe.getPurse());
            ps.setString(7, pe.getUUID());
            ps.execute();
        } catch (SQLException | IOException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<LocationTypeEntry> getLocationTypes() {
        List<LocationTypeEntry> ll = new ArrayList<>();
        try (
                Connection connection = DriverManager.getConnection(connectorString, user, password)) {
            PreparedStatement ps = connection.prepareStatement(readResource("sqls/getLocationTypes.sql", StandardCharsets.UTF_8));
            ps.execute();
            try (ResultSet rs = ps.getResultSet()) {
                while (rs.next()) {
                    LocationTypeEntry lte = new LocationTypeEntry();
                    lte.setId(rs.getInt("id"));
                    lte.setType(rs.getString("location_type"));
                    ll.add(lte);
                }
            }
        } catch (SQLException | IOException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ll;
    }

    public List<LocationEntry> getLocations() {
        List<LocationEntry> ll = new ArrayList<>();
        try (
                Connection connection = DriverManager.getConnection(connectorString, user, password)) {
            PreparedStatement ps = connection.prepareStatement(readResource("sqls/getLocations.sql", StandardCharsets.UTF_8));

            ps.execute();
            try (ResultSet rs = ps.getResultSet()) {
                while (rs.next()) {
                    LocationEntry p = new LocationEntry();
                    p.setLocation(new Location(Bukkit.getWorld(rs.getString("world")), rs.getFloat("x"), rs.getFloat("y"), rs.getFloat("z"), rs.getFloat("yaw"), rs.getFloat("pitch")));
                    p.setLocationType(locationTypeEntryList.get(rs.getInt("location_type_fk") - 1));
                    p.setPlayerId(rs.getInt("player_fk"));
                    p.setLocationName(rs.getString("location_name"));
                    p.setId(rs.getInt("id"));
                    ll.add(p);
                }
            }
        } catch (SQLException | IOException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ll;
    }

    public void insertLocation(LocationEntry le) {
        try (
                Connection connection = DriverManager.getConnection(connectorString, user, password)) {
            PreparedStatement ps = connection.prepareStatement(readResource("sqls/insertLocation.sql", StandardCharsets.UTF_8));
            Location l = le.getLocation();

            ps.setInt(1, le.getPlayerId());
            ps.setFloat(2, (float) l.getX());
            ps.setFloat(3, (float) l.getY());
            ps.setFloat(4, (float) l.getZ());
            ps.setFloat(5, (float) l.getYaw());
            ps.setFloat(6, (float) l.getPitch());
            ps.setString(7, l.getWorld().getName());
            ps.setString(8, le.getLocationName());
            ps.setInt(9, le.getLocationType().getId());
            ps.setInt(10, le.getPlayerId());

            ps.execute();
        } catch (SQLException | IOException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void deleteLocation(LocationEntry le) {
        try (
                Connection connection = DriverManager.getConnection(connectorString, user, password)) {
            PreparedStatement ps = connection.prepareStatement(readResource("sqls/deleteLocation.sql", StandardCharsets.UTF_8));
            ps.setInt(1, le.getPlayerId());
            ps.setInt(2, le.getId());
            ps.execute();
        } catch (SQLException | IOException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public BlockHistoryEntry getBlockHistoryByLocation(Location l) {
        try (
                Connection connection = DriverManager.getConnection(connectorString, user, password)) {
            PreparedStatement ps = connection.prepareStatement(readResource("sqls/getBlockHistoryByLocation.sql", StandardCharsets.UTF_8));
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
                    le.setLocation(new Location(Bukkit.getWorld(rs.getString("world")), rs.getFloat("x"), rs.getFloat("y"), rs.getFloat("z"), rs.getFloat("yaw"), rs.getFloat("pitch")));
                    le.setLocationType(locationTypeEntryList.get(rs.getInt("location_type_fk") - 1));
                    le.setPlayerId(rs.getInt("player_fk"));
                    le.setLocationName(rs.getString("location_name"));
                    le.setId(rs.getInt("l.id"));

                    bh.setLocation(le);
                    bh.setMaterial(rs.getString("material"));
                    return bh;
                }
            }
        } catch (SQLException | IOException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    public void insertBlockHistory(BlockHistoryEntry bh) {
        try (
                Connection connection = DriverManager.getConnection(connectorString, user, password)) {
            PreparedStatement ps = connection.prepareStatement(readResource("sqls/insertBlockHistory.sql", StandardCharsets.UTF_8));
            ps.setInt(1, bh.getCreatedby());
            ps.setInt(2, bh.getLocation().getId());
            ps.setString(3, bh.getMaterial());

            ps.execute();
        } catch (SQLException | IOException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<BlockHistoryEntry> getBlockHistoryByPlayer(PlayerEntry p) {
        List<BlockHistoryEntry> bhe = new ArrayList<>();
        try (
                Connection connection = DriverManager.getConnection(connectorString, user, password)) {
            PreparedStatement ps = connection.prepareStatement(readResource("sqls/getBlockHistoryByPlayer.sql", StandardCharsets.UTF_8));
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
                    le.setLocation(new Location(Bukkit.getWorld(rs.getString("world")), rs.getFloat("x"), rs.getFloat("y"), rs.getFloat("z"), rs.getFloat("yaw"), rs.getFloat("pitch")));
                    le.setLocationType(locationTypeEntryList.get(rs.getInt("location_type_fk") - 1));
                    le.setPlayerId(rs.getInt("player_fk"));
                    le.setLocationName(rs.getString("location_name"));
                    le.setId(rs.getInt("l.id"));

                    bh.setLocation(le);
                    bh.setMaterial(rs.getString("material"));
                    bhe.add(bh);
                }
            }
        } catch (SQLException | IOException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);
        }

        return bhe;
    }

    public List<BlockHistoryEntry> getBlockHistoryByPlayerAndTime(PlayerEntry p, String time, boolean deleted) {
        List<BlockHistoryEntry> bhe = new ArrayList<>();
        int year = 0, month = 0, day = 0, hour = 0, minute = 0;

        try (
                Connection connection = DriverManager.getConnection(connectorString, user, password)) {
            PreparedStatement ps = connection.prepareStatement(readResource(deleted ? "sqls/getBlockHistoryByPlayerAndTimeIsDeleted.sql" : "sqls/getBlockHistoryByPlayerAndTime.sql", StandardCharsets.UTF_8));
            String[] times = time.split("(?<![0-9]){1,6}");

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
                    le.setLocation(new Location(Bukkit.getWorld(rs.getString("world")), rs.getFloat("x"), rs.getFloat("y"), rs.getFloat("z"), rs.getFloat("yaw"), rs.getFloat("pitch")));
                    le.setLocationType(locationTypeEntryList.get(rs.getInt("location_type_fk") - 1));
                    le.setPlayerId(rs.getInt("player_fk"));
                    le.setLocationName(rs.getString("location_name"));
                    le.setId(rs.getInt("l.id"));

                    bh.setLocation(le);
                    bh.setMaterial(rs.getString("material"));
                    bhe.add(bh);
                }
            }
        } catch (SQLException | IOException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        LogUtils.debug("Amount: " + bhe.size() + " Years: " + year + " Months: " + month + " Days: " + day + " Hours: " + hour + " Minutes: " + minute + " UUID: " + p.getUUID(), DEBUG);
        return bhe;
    }

    public void deleteBlockHistory(BlockHistoryEntry bh) {
        try (
                Connection connection = DriverManager.getConnection(connectorString, user, password)) {
            PreparedStatement ps = connection.prepareStatement(readResource("sqls/deleteBlockHistory.sql", StandardCharsets.UTF_8));
            ps.setInt(1, bh.getDeletedby());
            ps.setInt(2, bh.getLocation().getId());

            ps.execute();
        } catch (SQLException | IOException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void applyPatch(int version) {
        switch (version) {
            case 0:
                patch1();
                patch2();
                patch2_1();
                patch3();
                break;
            case 1:
                patch2();
                patch2_1();
                patch3();
                break;
            case 2:
                patch2_1();
                patch3();
                break;
            case 3:
                patch3();
            default:
                break;
        }
    }

    private void patch1() {
        String v = "patches/v1.0/";
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

        ConfigHelper ch = new ConfigHelper("players");
        List<PlayerEntry> pe = ch.getPlayers();
        pe.forEach(p -> {
            insertPlayer(p);
        });

        List<PlayerEntry> pel = getPlayers();
        pel.forEach(p -> {
            playerEntryList.put(UUID.fromString(p.getUUID()), p);
        });

        for (PlayerEntry p : pe) {
            PlayerEntry pu = playerEntryList.get(UUID.fromString(p.getUUID()));
            pu.setAFK(p.isAFK());
            pu.setFlying(p.isFlying());
            pu.setCustomName(p.getCustomName());
            pu.setUpdatedBy(1);
            updatePlayer(pu);

            List<LocationEntry> lel = ch.getHomes(pu);
            lel.forEach(le -> {
                insertLocation(le);
            });
        }
    }

    private void patch2() {
        String v = "patches/v2.0/";
        consoleSendMessage(Strings.PLUGIN_NAME_CONSOLE, "applying " + v);
        executeScript(v + "dropBlockHistory.sql");
        executeScript(v + "createBlockHistory.sql");
        executeScript(v + "insertNewDBVersion.sql");
        executeScript(v + "updateOldPluginInformation.sql");
    }

    private void patch2_1() {
        String v = "patches/v2.1/";
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

    private void patch3() {
        String v = "patches/v3.0/";
        executeScript(v + "addBankTier.sql");
        executeScript(v + "addBankAccount.sql");
        executeScript(v + "addBankTransaction.sql"); 
        executeScript(v + "insertBankTier.sql"); 
        executeScript(v + "updatePlayer.sql");
        executeScript(v + "insertLocationTypes.sql");
        executeScript(v + "insertNewDBVersion.sql");
        executeScript(v + "updateOldPluginInformation.sql");
    }


    public void init() {
        applyPatch(pie.getDbVersion());
    }

    private void executeScript(String script) {
        try (
                Connection connection = DriverManager.getConnection(connectorString, user, password)) {
            PreparedStatement ps = connection.prepareStatement(readResource("sqls/" + script, StandardCharsets.UTF_8));

            ps.execute();
        } catch (SQLException | IOException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void executeScriptNoSchema(String script) {

        try (
                Connection connection = DriverManager.getConnection(connector + "://" + host + ":" + port, user, password)) {
            PreparedStatement ps = connection.prepareStatement(readResource("sqls/" + script, StandardCharsets.UTF_8));

            ps.execute();
        } catch (SQLException | IOException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String readResource(final String fileName, Charset charset) throws IOException {

        String out = "";
        try (InputStream is = getClass().getResourceAsStream("/" + fileName); InputStreamReader isr = new InputStreamReader(is); BufferedReader br = new BufferedReader(isr)) {
            String line;
            while ((line = br.readLine()) != null) {
                out += line + System.lineSeparator();
            }
        }

        return out;
    }

    public List<GroupEntry> getGroups() {
        List<GroupEntry> gel = new ArrayList<>();
        try (
                Connection connection = DriverManager.getConnection(connectorString, user, password)) {
            PreparedStatement ps = connection.prepareStatement(readResource("sqls/getGroups.sql", StandardCharsets.UTF_8));
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
        } catch (SQLException | IOException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return gel;
    }
}
