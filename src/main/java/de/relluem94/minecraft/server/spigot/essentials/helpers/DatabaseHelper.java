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
import static de.relluem94.minecraft.server.spigot.essentials.Strings.PLUGIN_DATABASE_NAME;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.PlayerEntry;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.ResultSet;
import org.bukkit.configuration.file.FileConfiguration;

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
    
    public PlayerEntry getPlayer(String UUID) {
        try (
                Connection connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + PLUGIN_DATABASE_NAME, user, password)) {
            PreparedStatement ps = connection.prepareStatement(readResource("sqls/getPlayer.sql", Charsets.UTF_8));

            ps.execute();
            try (ResultSet rs = ps.getResultSet()) {
                while(rs.next()){
                    PlayerEntry p = new PlayerEntry();
                    p.setCreated(rs.getString("created"));
                    p.setCreatedby(rs.getInt("createdby"));
                    p.setCustomname(rs.getString("customname"));
                    p.setDeletedby(rs.getInt("deletedby"));
                    p.setFly(rs.getBoolean("fly"));
                    p.setAfk(rs.getBoolean("afk"));
                    p.setGroupID(rs.getInt("group_fk"));
                    p.setId(rs.getInt("id"));
                    p.setUuid(rs.getString("uuid"));
                    return p;
                }
            }
        } catch (SQLException | IOException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new PlayerEntry();
    }


    public void insertPlayer(PlayerEntry pe) {
        try (
                Connection connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + PLUGIN_DATABASE_NAME, user, password)) {
            PreparedStatement ps = connection.prepareStatement(readResource("sqls/insertPlayer.sql", Charsets.UTF_8));
            ps.setInt(1, pe.getCreatedby());
            ps.setString(2, pe.getUUID());
            ps.setInt(3, pe.getGroupID());

            ps.execute();
        } catch (SQLException | IOException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void updatePlayerAFK(int updatedby, int player_id, boolean afk) {
        try (
            Connection connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + PLUGIN_DATABASE_NAME, user, password)) {
            PreparedStatement ps = connection.prepareStatement(readResource("sqls/updatePlayerAFK.sql", Charsets.UTF_8));
            ps.setInt(1, updatedby);
            ps.setBoolean(2, afk);
            ps.setInt(3, player_id);

            ps.execute();
        } catch (SQLException | IOException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void updatePlayerCustomName(int updatedby, int player_id, String name) {
        try (
            Connection connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + PLUGIN_DATABASE_NAME, user, password)) {
            PreparedStatement ps = connection.prepareStatement(readResource("sqls/updatePlayerCustomName.sql", Charsets.UTF_8));
            ps.setInt(1, updatedby);
            ps.setString(2, name);
            ps.setInt(3, player_id);

            ps.execute();
        } catch (SQLException | IOException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void applyPatch(int version) {
        String v;
        switch (version) {
            case 1:
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
        applyPatch(1);
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
