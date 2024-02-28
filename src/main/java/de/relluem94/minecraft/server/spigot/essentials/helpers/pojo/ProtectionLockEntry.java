package de.relluem94.minecraft.server.spigot.essentials.helpers.pojo;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;

/**
 *
 * @author rellu
 */
@Setter
@Getter
public class ProtectionLockEntry {
    private int id;
    private String created;
    private int createdBy;
    private String updated;
    private int updatedBy;
    private String deleted;
    private int deletedBy;
    private Material value;
}