package de.relluem94.minecraft.server.spigot.essentials.helpers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.GroupEntry;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.LocationEntry;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.LocationTypeEntry;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.PlayerEntry;
import de.relluem94.minecraft.server.spigot.essentials.permissions.Group;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

/**
 *
 * @author rellu
 */
public class ConfigHelper {

    private String name;
    private File file;
    private YamlConfiguration config;
    
    //TODO Make an migrator from config files to db
    public ConfigHelper(String name) {
        this.name = name;

        file = new File(RelluEssentials.dataFolder, name + ".yml");
        if (file.exists()) {
            config = YamlConfiguration.loadConfiguration(file);
        }
        else{
            
        }
    }
    
    public List<PlayerEntry> getPlayers(){
        List<PlayerEntry> list = new ArrayList();
        ConfigurationSection cs = config.getConfigurationSection("player");
        
        for(String uuid : cs.getKeys(false)){
            ConfigurationSection player = cs.getConfigurationSection(uuid);
            
            String group_name = player.getString("group").toLowerCase();
            int group_fk = Group.getGroupFromName(group_name).getId();
            boolean fly = player.getBoolean("fly");
            boolean afk = player.getBoolean("afk");
            String customname = player.getString("customname");

            System.out.println("Found Player: " + uuid + " customname:" + customname + " afk:" + afk + " fly:" + fly + " group id:" + group_fk + " group:" + group_name);
            
            PlayerEntry p = new PlayerEntry();
            p.setGroup(new GroupEntry(Group.getGroupFromName(group_name)));
            p.setAFK(afk);
            p.setFly(fly);
            p.setCreatedby(1);
            p.setCustomname(customname);
            p.setUuid(uuid);
            
            list.add(p);
        }
        return list;
    }
    
    public List<LocationEntry> getHomes(PlayerEntry p){
        List<LocationEntry> list = new ArrayList();
        ConfigurationSection homes = config.getConfigurationSection("player." + p.getUUID() + ".home");
        for(String home : homes.getKeys(false)){
            ConfigurationSection h = homes.getConfigurationSection(home);

            float x,y,z,yaw,pitch;
            x = (float)h.getDouble("x");
            y = (float)h.getDouble("y");
            z = (float)h.getDouble("z");
            yaw = (float)h.getDouble("yaw");
            pitch = (float)h.getDouble("pitch");
            int type = home.equals("death") ? 2 : 1;
            String world_name = h.getString("world");
            
            World world = Bukkit.getServer().getWorld(world_name);
            
            System.out.println("Found Home: " + home + " x:" + x + " y:" + y + " z:" + z + " yaw:" + yaw + " pitch:" + pitch + " world:" + world);

            LocationEntry l = new LocationEntry();
            l.setLocation(new Location(world, x,y,z,yaw,pitch));
            l.setLocationName(home);
            l.setPlayerId(p.getId());
            LocationTypeEntry lt = new LocationTypeEntry();
            lt.setId(type);
            l.setLocationType(lt);
            
            list.add(l);
        }
        return list;
    }
    

    /**
     * Returns the Config Name
     * @return String Config Name
     */
    public String getConfigName() {
        return name;
    }

    /**
     * Returns the File
     * @return File
     */
    public File getFile() {
        return file;
    }

    /**
     * Returns the Config
     * @return YamlConfiguration
     */
    public YamlConfiguration getConfig() {
        return config;
    }

    /**
     * Saves the Config
     * @throws IOException 
     */
    public void save() throws IOException {
        config.save(file);
    }

    /**
     * Reloads the Config 
     * @throws IOException
     * @throws FileNotFoundException
     * @throws InvalidConfigurationException 
     */
    public void reload() throws IOException, FileNotFoundException, InvalidConfigurationException {
        config.load(file);
    }

    /**
     * 
     * @param path where the Location gets saved
     * @param l the Location it self
     */
    public void setLocation(String path, Location l) {
        config.set(path + ".x", l.getX());
        config.set(path + ".y", l.getBlockY());
        config.set(path + ".z", l.getBlockZ());
        config.set(path + ".yaw", l.getYaw());
        config.set(path + ".pitch", l.getPitch());
        config.set(path + ".world", l.getWorld().getName());
    }

    /**
     * 
     * @param path where the Location is saved
     * @return Location from the Config Path
     */
    public Location getLocation(String path) {
        double x, y, z;
        float yaw, pitch;
        String world;
        x = config.getDouble(path + ".x");
        y = config.getDouble(path + ".y");
        z = config.getDouble(path + ".z");
        yaw = (float) config.getDouble(path + ".yaw");
        pitch = (float) config.getDouble(path + ".pitch");
        world = config.getString(path + ".world");

        return new Location(Bukkit.getWorld(world), x, y, z, yaw, pitch);
    }

}
