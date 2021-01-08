package de.relluem94.minecraft.server.spigot.essentials.helpers.pojo;

import de.relluem94.minecraft.server.spigot.essentials.permissions.Group;

/**
 *
 * @author rellu
 */
public class GroupEntry {
    private int id;
    private String name;
    private String prefix;
    
    public GroupEntry(Group group){
        setGroup(group);
    }
    
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
    
    private void setGroup(Group g){
        this.id = g.getId();
        this.name = g.getName();
        this.prefix = g.getPrefix();
    }
    
}
