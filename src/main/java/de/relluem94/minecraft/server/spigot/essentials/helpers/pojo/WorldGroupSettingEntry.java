package de.relluem94.minecraft.server.spigot.essentials.helpers.pojo;

import lombok.Getter;
import lombok.Setter;

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
    private SettingEntry settingEntry;
    private WorldGroupEntry worldGroupEntry;
    private boolean value;
}