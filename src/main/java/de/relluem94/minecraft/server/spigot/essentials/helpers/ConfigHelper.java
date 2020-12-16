/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.de.relluem94.minecraft.server.spigot.essentials.helpers;

import java.io.File;
import java.io.IOException;
import main.java.de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import org.bukkit.configuration.file.YamlConfiguration;

/**
 *
 * @author rellu
 */
public class ConfigHelper extends YamlConfiguration{
    private final String name;
    private final File file;
    private final YamlConfiguration config;
    
    public ConfigHelper(String name) throws IOException {
        this.name = name;
        
        
        file = new File(RelluEssentials.getInstance().getDataFolder(), name + ".yml");
            if (!file.exists()) {
                file.createNewFile();
                config = new YamlConfiguration();
                config.options().header("Configuration File - " + getName());
            }
            else{
                config = YamlConfiguration.loadConfiguration(file);
            }		
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
    
}
