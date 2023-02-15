package de.relluem94.minecraft.server.spigot.essentials.helpers.pojo;

/**
 *
 * @author rellu
 */
public class SkillsEntry {

    public SkillsEntry() {}

    private int id;
    private String name;
    private String displayname;
    private int max_level;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String displayname) {
        this.displayname = displayname;
    }

    public String getDisplayName() {
        return displayname;
    }

    public void setDisplayName(String displayname) {
        this.displayname = displayname;
    }

    public int getMaxLevel() {
        return max_level;
    }

    public void setMaxLevel(int max_level) {
        this.max_level = max_level;
    }
}
