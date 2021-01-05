package de.relluem94.minecraft.server.spigot.essentials.helpers;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import static de.relluem94.minecraft.server.spigot.essentials.Strings.PLUGIN_DATABASE_NAME;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

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
            PreparedStatement ps = connection.prepareStatement("");

            ps.execute();
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
