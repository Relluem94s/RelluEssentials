/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.de.relluem94.minecraft.server.spigot.essentials.helpers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import main.java.de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

/**
 *
 * @author rellu
 */
public class ConfigHelper extends YamlConfiguration{
    private String name;
    private File file;
    private YamlConfiguration config;
    
    public ConfigHelper(String name) throws IOException {
        this.name = name;
        
        
        file = new File(RelluEssentials.dataFolder, name + ".yml");
        if (!file.exists()) {
            file.createNewFile();
            file.getParentFile().mkdirs();
            config = new YamlConfiguration();
            config.options().header("Configuration File - " + name);
        }
        
        config = YamlConfiguration.loadConfiguration(file);
    }

    public String getConfigName() {
        return name;
    }
    
    public File getFile(){
        return file;
    }
    
    public YamlConfiguration getConfig(){
        return config;
    }
    
    public void save() throws IOException {
        config.save(file);
    }
    
    public void reload() throws IOException, FileNotFoundException, InvalidConfigurationException{
        config.load(file);
    }
}
