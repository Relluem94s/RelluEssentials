package de.relluem94.minecraft.server.spigot.essentials.helpers.pojo;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author rellu
 */
@Setter
@Getter
public class GroupEntry {

    public static final String FIELD_ID = "id";
    public static final String FIELD_NAME = "name";
    public static final String FIELD_PREFIX = "prefix";

    private int id;
    private String name;
    private String prefix;

    public GroupEntry() {
    }

    public GroupEntry(int id, String name, String prefix) {
        this.id = id;
        this.name = name;
        this.prefix = prefix;
    }
}