package de.relluem94.minecraft.server.spigot.essentials.helpers.pojo;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author rellu
 */
@Setter
@Getter
public class SkillsEntry {
    private int id;
    private String name;
    private String displayName;
    private int maxLevel;
}