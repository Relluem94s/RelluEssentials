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

    public static final String FIELD_ID = "id";
    public static final String FIELD_CREATED = "created";
    public static final String FIELD_CREATEDBY = "createdby";
    public static final String FIELD_UPDATED = "updated";
    public static final String FIELD_UPDATEDBY = "updatedby";
    public static final String FIELD_DELETED = "deleted";
    public static final String FIELD_DELETEDBY = "deletedby";
    public static final String FIELD_FLAGS = "flags";
    public static final String FIELD_RIGHTS = "rights";
    public static final String FIELD_MATERIAL_NAME = "material_name";

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
