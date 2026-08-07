package de.relluem94.minecraft.server.spigot.essentials.models;

import de.relluem94.minecraft.server.spigot.essentials.helpers.ItemHelper;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.NpcDialogueEntry;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import org.json.JSONObject;

@Getter
@Setter
public class Npc {

  private final UUID id;
  private int dbid;
  private String profileName;
  private JSONObject inventory;
  private double x;
  private double y;
  private double z;
  private String worldName;
  private UUID entityUUID;
  private ItemHelper itemHelper;
  private List<NpcDialogueEntry> dialogueLines;

  public Npc(int dbid, UUID id, String profileName, double x, double y, double z,
      String worldName) {
    this.dbid = dbid;
    this.id = id;
    this.profileName = profileName;
    this.x = x;
    this.y = y;
    this.z = z;
    this.worldName = worldName;
    this.dialogueLines = new ArrayList<>();
  }

  public Npc(int dbid, UUID id, String profileName, JSONObject inventory, double x, double y,
      double z, String worldName) {
    this(dbid, id, profileName, x, y, z, worldName);
    this.inventory = inventory;
  }
}