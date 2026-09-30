package de.relluem94.minecraft.server.spigot.essentials.models.pojo;

import lombok.Data;

/**
 * Represents a single dialogue entry for a non-player character in the essentials module.
 * Each entry contains the dialogue text, its position in the dialogue list,
 * and metadata about creation and updates.
 *
 * @author rellu
 */
@Data
public class NpcDialogueEntry {

  private int id;
  private int createdBy;
  private int updatedBy;
  private int listPosition;
  private String text;
  private int npcFk;
}