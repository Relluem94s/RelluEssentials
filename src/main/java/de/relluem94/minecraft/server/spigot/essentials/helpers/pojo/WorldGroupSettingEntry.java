package de.relluem94.minecraft.server.spigot.essentials.helpers.pojo;

import lombok.Getter;
import lombok.Setter;
import org.json.JSONObject;

import de.relluem94.rellulib.stores.DoubleStore;

/**
 *
 * @author rellu
 */

@Setter
@Getter
public class WorldGroupSettingEntry {
    private int id;
    private String created;
    private int createdBy;
    private String updated;
    private int updatedBy;
    private String deleted;
    private int deletedBy;
    private String name;
    private WorldGroupEntry worldGroupEntry;
}