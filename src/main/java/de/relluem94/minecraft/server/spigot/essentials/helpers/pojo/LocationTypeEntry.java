package de.relluem94.minecraft.server.spigot.essentials.helpers.pojo;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author rellu
 */
@Setter
@Getter
public class LocationTypeEntry {

    public static final String FIELD_ID = "id";
    public static final String FIELD_TYPE = "location_type_fk";

    private int id;
    private String type;
}