package de.relluem94.minecraft.server.spigot.essentials.helpers;

import java.io.IOException;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.google.common.io.Resources;
import com.google.common.base.Charsets;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import static de.relluem94.minecraft.server.spigot.essentials.Strings.PLUGIN_DATABASE_NAME;
import de.relluem94.minecraft.server.spigot.essentials.permissions.Group;
import org.bukkit.entity.Player;

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

    public DatabaseHelper() {
        enabled = RelluEssentials.getPlugin(RelluEssentials.class).getConfig().getBoolean("database.enabled");
        if (enabled) {
            host = RelluEssentials.getPlugin(RelluEssentials.class).getConfig().getString("database.host") + "";
            user = RelluEssentials.getPlugin(RelluEssentials.class).getConfig().getString("database.user") + "";
            password = RelluEssentials.getPlugin(RelluEssentials.class).getConfig().getString("database.password") + "";
            port = RelluEssentials.getPlugin(RelluEssentials.class).getConfig().getInt("database.port");
        }

    }
    
    public boolean enabled(){
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

    
    public void insertPlayer(int createdby, Player p, Group g) {
        try (
            Connection connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + PLUGIN_DATABASE_NAME, user, password)) {
            PreparedStatement ps = connection.prepareStatement(readResource("sqls/insertPlayer.sql", Charsets.UTF_8));
            ps.setInt(1, createdby);
            ps.setString(2, p.getUniqueId().toString());
            ps.setInt(3, g.getId());

            ps.execute();
        } catch (SQLException | IOException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    
    
    
    public String readResource(final String fileName, Charset charset) throws IOException {
        return Resources.toString(Resources.getResource(fileName), charset);
    }
    
    private void applyPatch(int version){
        switch(version){
            case 1:
                executeScript("createSchema.sql");
                executeScript("createGroup.sql");
                executeScript("createPlayer.sql");
                executeScript("createLocationType.sql");
                executeScript("createLocation.sql");
                executeScript("createBlockHistory.sql");
                executeScript("createPluginInformation.sql");
                executeScript("insertGroups.sql");
                executeScript("insertPlayers.sql");
                executeScript("insertLocationTypes.sql");
                executeScript("insertPluginInformation.sql");
                break;
            default: 
                break;
        }
    }
    
    public void init(){
        applyPatch(1);
    }
    
    


    private void executeScript(String script) {
        try (
            Connection connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + PLUGIN_DATABASE_NAME, user, password)) {
            PreparedStatement ps = connection.prepareStatement(readResource("sqls/" + script, Charsets.UTF_8));

            ps.execute();
        } catch (SQLException | IOException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
