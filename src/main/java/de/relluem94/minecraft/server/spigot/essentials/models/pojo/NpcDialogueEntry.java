package de.relluem94.minecraft.server.spigot.essentials.models.pojo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NpcDialogueEntry {

  private int id;
  private int createdBy;
  private int updatedBy;
  private int listPosition;
  private String text;
  private int npcFk;
}