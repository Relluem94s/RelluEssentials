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

    public static final String FIELD_ID = "id";
    public static final String FIELD_CREATED = "created";
    public static final String FIELD_CREATEDBY = "createdby";
    public static final String FIELD_UPDATED = "updated";
    public static final String FIELD_UPDATEDBY = "updatedby";
    public static final String FIELD_DELETED = "deleted";
    public static final String FIELD_DELETEDBY = "deletedby";

    private int id;
    private String created;
    private int createdBy;
    private String updated;
    private int updatedBy;
    private String deleted;
    private int deletedBy;
    private String name;
    private WorldGroupEntry worldGroupEntry;
    public void setSetting(int setting){
        int settingFK = setting;
        JSONObject value = null;
        DoubleStore ds = new DoubleStore(settingFK, value);
        ds.toString(); // TODO remove this nonsense and add pojo for setting
    }
}