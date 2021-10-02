package de.relluem94.minecraft.server.spigot.essentials.helpers.pojo;

/**
 *
 * @author rellu
 */
public class GroupEntry {
    private int id;
    private String name;
    private String prefix;
    
    public GroupEntry(){}
    
    public GroupEntry(int id, String name, String prefix){
        this.id = id;
        this.name = name;
        this.prefix = prefix;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }    
}
