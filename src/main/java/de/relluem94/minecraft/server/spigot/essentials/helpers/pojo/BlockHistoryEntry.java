package de.relluem94.minecraft.server.spigot.essentials.helpers.pojo;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author rellu
 */
@Setter
@Getter
public class BlockHistoryEntry {
    private int id;
    private LocationEntry location;
    private String material;
    private String created;
    private int createdby;
    private String updated;
    private int updatedBy;
    private String deleted;
    private int deletedby;
}