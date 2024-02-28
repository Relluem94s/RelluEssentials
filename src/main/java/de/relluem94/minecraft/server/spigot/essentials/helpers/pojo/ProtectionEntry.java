package de.relluem94.minecraft.server.spigot.essentials.helpers.pojo;

import lombok.Getter;
import lombok.Setter;
import org.json.JSONObject;

/**
 *
 * @author rellu
 */
@Setter
@Getter
public class ProtectionEntry {
    private int id;
    private String created;
    private int createdBy;
    private String updated;
    private int updatedBy;
    private String deleted;
    private int deletedBy;
    private LocationEntry locationEntry;
    private String materialName;
    private JSONObject flags;
    private JSONObject rights;
}