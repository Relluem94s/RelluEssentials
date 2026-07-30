package de.relluem94.minecraft.server.spigot.essentials.model.pojo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NPCDialogueEntry {
    private int id;
    private int createdBy;
    private int updatedBy;
    private int listPosition;
    private String text;
    private int npcFk;
}