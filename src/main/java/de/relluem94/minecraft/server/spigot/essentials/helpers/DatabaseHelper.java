package de.relluem94.minecraft.server.spigot.essentials.helpers;

import java.io.IOException;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.google.common.base.Charsets;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import static de.relluem94.minecraft.server.spigot.essentials.RelluEssentials.pie;
import static de.relluem94.minecraft.server.spigot.essentials.Strings.PLUGIN_DATABASE_NAME;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.GroupEntry;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.LocationEntry;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.LocationTypeEntry;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.PlayerEntry;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.PluginInformationEntry;
import de.relluem94.minecraft.server.spigot.essentials.permissions.Group;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import static de.relluem94.minecraft.server.spigot.essentials.RelluEssentials.locationTypeEntryList;

/**
 *
 * @author rellu
 */
public class DatabaseHelper {

    private String host;
    private String user;
    private String password;
    private int port;
    private final boolean enabled;

    private String connector = "jdbc:mariadb";

    public DatabaseHelper() {
        FileConfiguration c = RelluEssentials.getPlugin(RelluEssentials.class).getConfig();
        connector = "jdbc:mysql";

        enabled = c.getBoolean("database.enabled");
        if (enabled) {
            host = c.getString("database.host") + "";
            user = c.getString("database.user") + "";
            password = c.getString("database.password") + "";
            port = c.getInt("database.port");
        }

    }

    public boolean enabled() {
        return enabled;
    }

    //*****************************************************************************************************************************************//
    //                                                                                                                                         //
    //                                                DUMMY                                                                                    //
    //                                                                                                                                         //
    //*****************************************************************************************************************************************//
    
    //DUMMY 
    public void select() {
        try (
            Connection connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + PLUGIN_DATABASE_NAME, user, password)) {
            PreparedStatement ps = connection.prepareStatement(readResource("sqls/insertPlayer.sql", Charsets.UTF_8));

            ps.execute();
        } catch (SQLException | IOException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
        
    //DUMMY
    public LocationEntry getLocation(PlayerEntry pe, int type) {
        try (
            Connection connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + PLUGIN_DATABASE_NAME, user, password)) {
            PreparedStatement ps = connection.prepareStatement(readResource("sqls/getLocationsByPlayer.sql", Charsets.UTF_8));
            ps.setInt(1, pe.getId());
            ps.execute();
            try (ResultSet rs = ps.getResultSet()) {
                while(rs.next()){
                    LocationEntry p = new LocationEntry();
                    p.setLocation(new Location(Bukkit.getWorld(rs.getString("world")), rs.getFloat("x"), rs.getFloat("y"), rs.getFloat("z"), rs.getFloat("yaw"), rs.getFloat("pitch")));
                    p.setLocationType(locationTypeEntryList.get(type)); //TODO WRONG
                    return p;
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
    
    
    
    public PluginInformationEntry getPluginInformation() {
        try (
            Connection connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port , user, password)) {
            PreparedStatement ps = connection.prepareStatement(readResource("sqls/getPluginInformation.sql", Charsets.UTF_8));
            ps.execute();
            try (ResultSet rs = ps.getResultSet()) {
                while(rs.next()){
                    PluginInformationEntry pie = new PluginInformationEntry();
                    
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
        } catch (SQLException | IOException ex) {
            PluginInformationEntry pie = new PluginInformationEntry(); // standard pie if no Database version was found
            pie.setDbVersion(0);
            return pie;
        }
        return null;
    }
    
    public PlayerEntry getPlayer(String UUID) {
        try (
            Connection connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + PLUGIN_DATABASE_NAME, user, password)) {
            PreparedStatement ps = connection.prepareStatement(readResource("sqls/getPlayer.sql", Charsets.UTF_8));
            ps.setString(1, UUID);
            ps.execute();
            try (ResultSet rs = ps.getResultSet()) {
                while(rs.next()){
                    PlayerEntry p = new PlayerEntry();
                    p.setCreated(rs.getString("created"));
                    p.setCreatedby(rs.getInt("createdby"));
                    p.setCustomname(rs.getString("customname"));
                    p.setDeletedby(rs.getInt("deletedby"));
                    p.setFly(rs.getBoolean("fly"));
                    p.setAFK(rs.getBoolean("afk"));
                    p.setGroup(new GroupEntry(Group.getGroupFromId(rs.getInt("group_fk"))));
                    p.setId(rs.getInt("id"));
                    p.setUuid(rs.getString("uuid"));
                    return p;
                }
            }
        } catch (SQLException | IOException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public List<PlayerEntry> getPlayers() {
        List<PlayerEntry> pel = new ArrayList<>();   
        try (   
            Connection connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + PLUGIN_DATABASE_NAME, user, password)) {
            PreparedStatement ps = connection.prepareStatement(readResource("sqls/getPlayers.sql", Charsets.UTF_8));
            ps.execute();
            try (ResultSet rs = ps.getResultSet()) {
                while(rs.next()){
                    PlayerEntry p = new PlayerEntry();
                    p.setCreated(rs.getString("created"));
                    p.setCreatedby(rs.getInt("createdby"));
                    p.setCustomname(rs.getString("customname"));
                    p.setDeletedby(rs.getInt("deletedby"));
                    p.setFly(rs.getBoolean("fly"));
                    p.setAFK(rs.getBoolean("afk"));
                    p.setGroup(new GroupEntry(Group.getGroupFromId(rs.getInt("group_fk"))));
                    p.setId(rs.getInt("id"));
                    p.setUuid(rs.getString("uuid"));
                    
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
            Connection connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + PLUGIN_DATABASE_NAME, user, password)) {
            PreparedStatement ps = connection.prepareStatement(readResource("sqls/insertPlayer.sql", Charsets.UTF_8));
            ps.setInt(1, pe.getCreatedby());
            ps.setString(2, pe.getUUID());
            ps.setInt(3, pe.getGroup().getId());

            ps.execute();
        } catch (SQLException | IOException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void updatePlayer(PlayerEntry pe) {
        try (
            Connection connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + PLUGIN_DATABASE_NAME, user, password)) {
            PreparedStatement ps = connection.prepareStatement(readResource("sqls/updatePlayer.sql", Charsets.UTF_8));
            ps.setInt(1, pe.getId());
            ps.setInt(2, pe.getGroup().getId());
            ps.setBoolean(3, pe.isAfk());
            ps.setBoolean(4, pe.isFlying());
            ps.setString(5, pe.getCustomname());
            ps.setString(6, pe.getUUID());
            ps.execute();
        } catch (SQLException | IOException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public List<LocationTypeEntry> getLocationTypes() {
        List<LocationTypeEntry> ll = new ArrayList<>();   
        try (   
            Connection connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + PLUGIN_DATABASE_NAME, user, password)) {
            PreparedStatement ps = connection.prepareStatement(readResource("sqls/getLocationTypes.sql", Charsets.UTF_8));
            ps.execute();
            try (ResultSet rs = ps.getResultSet()) {
                while(rs.next()){
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
            Connection connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + PLUGIN_DATABASE_NAME, user, password)) {
            PreparedStatement ps = connection.prepareStatement(readResource("sqls/getLocations.sql", Charsets.UTF_8));
 
            ps.execute();
            try (ResultSet rs = ps.getResultSet()) {
                while(rs.next()){
                    LocationEntry p = new LocationEntry();
                    p.setLocation(new Location(Bukkit.getWorld(rs.getString("world")), rs.getFloat("x"), rs.getFloat("y"), rs.getFloat("z"), rs.getFloat("yaw"), rs.getFloat("pitch")));
                    p.setLocationType(locationTypeEntryList.get(rs.getInt("location_type_fk")-1));
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
            Connection connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + PLUGIN_DATABASE_NAME, user, password)) {
            PreparedStatement ps = connection.prepareStatement(readResource("sqls/insertLocation.sql", Charsets.UTF_8));
            Location l = le.getLocation();
            
            ps.setInt(1, le.getPlayerId());
            ps.setFloat(2, (float)l.getX());
            ps.setFloat(3, (float)l.getY());
            ps.setFloat(4, (float)l.getZ());
            ps.setFloat(5, (float)l.getYaw());
            ps.setFloat(6, (float)l.getPitch());
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
            Connection connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + PLUGIN_DATABASE_NAME, user, password)) {
            PreparedStatement ps = connection.prepareStatement(readResource("sqls/deleteLocation.sql", Charsets.UTF_8));
            ps.setInt(1, le.getPlayerId());
            ps.setInt(2, le.getId());
            ps.execute();
        } catch (SQLException | IOException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    
    
    
    
    
    
    
    

    private void applyPatch(int version) {
        String v;
        switch (version) {
            case 0:
                v = "patches/v1.0/";
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
                break;
            default:
                break;
        }
    }

    public void init() {
        applyPatch(pie.getDbVersion());
    }

    private void executeScript(String script) {
        try (
                Connection connection = DriverManager.getConnection(connector + "://" + host + ":" + port + "/" + PLUGIN_DATABASE_NAME, user, password)) {
            PreparedStatement ps = connection.prepareStatement(readResource("sqls/" + script, Charsets.UTF_8));

            ps.execute();
        } catch (SQLException | IOException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void executeScriptNoSchema(String script) {

        try (
                Connection connection = DriverManager.getConnection(connector + "://" + host + ":" + port, user, password)) {
            PreparedStatement ps = connection.prepareStatement(readResource("sqls/" + script, Charsets.UTF_8));

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
}
