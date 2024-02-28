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
    private int id;
    private String name;
    private String prefix;

    public GroupEntry() {}

    public GroupEntry(int id, String name, String prefix) {
        this.id = id;
        this.name = name;
        this.prefix = prefix;
    }
}