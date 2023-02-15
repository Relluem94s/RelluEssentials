package de.relluem94.minecraft.server.spigot.essentials.helpers.pojo;

import java.util.Properties;
import java.util.UUID;

/**
 *
 * @author rellu
 */
public class OfflinePlayerEntry {

    public OfflinePlayerEntry() {
    }

    private UUID id;
    private String name;
    private Properties properties;

    public UUID getID() {
        return id;
    }

    public void setID(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Properties getProperties(){
        return properties;
    }

    public void setProperties(Properties properties){
        this.properties = properties;
    }
}