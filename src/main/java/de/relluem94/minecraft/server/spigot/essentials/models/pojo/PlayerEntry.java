package de.relluem94.minecraft.server.spigot.essentials.models.pojo;

import de.relluem94.minecraft.server.spigot.essentials.enums.PlayerState;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jspecify.annotations.NonNull;

/**
 * Represents a stored player profile in the Essentials plugin.
 *
 * <p>A PlayerEntry holds persistent metadata about a Minecraft player, including
 * audit information (creation, update and deletion data), economic balance,
 * group membership, current state flags (AFK, flying), homes and death locations,
 * partner information, and state-specific parameters.</p>
 *
 * @author rellu
 */
@Data
@NoArgsConstructor
public class PlayerEntry {

  private int id;
  private String created;
  private int createdBy;
  private String updated;
  private int updatedBy;
  private String deleted;
  private int deletedBy;
  private double purse;
  private String uuid;
  private GroupEntry group;
  private boolean afk;
  private boolean flying;
  private String name;
  private String customName;
  private List<LocationEntry> homes = new ArrayList<>();
  private List<LocationEntry> deaths = new ArrayList<>();
  private PlayerPartnerEntry partner;
  private PlayerState playerState;
  private Object playerStateParameter;

  private boolean hasToBeUpdated = false;

  /**
   * Creates a new PlayerEntry by copying all properties from the given source entry.
   * This constructor is useful for creating modified snapshots or detached copies
   * of an existing player profile.
   *
   * @param sourcePlayerEntry the player entry whose values are copied into this instance
   */
  public PlayerEntry(@NonNull PlayerEntry sourcePlayerEntry) {
    setId(sourcePlayerEntry.getId());
    setCreated(sourcePlayerEntry.getCreated());
    setCreatedBy(sourcePlayerEntry.getCreatedBy());
    setUpdated(sourcePlayerEntry.getUpdated());
    setUpdatedBy(sourcePlayerEntry.getUpdatedBy());
    setDeleted(sourcePlayerEntry.getDeleted());
    setDeletedBy(sourcePlayerEntry.getDeletedBy());
    setPurse(sourcePlayerEntry.getPurse());
    setUuid(sourcePlayerEntry.getUuid());
    setGroup(sourcePlayerEntry.getGroup());
    setAfk(sourcePlayerEntry.isAfk());
    setName(sourcePlayerEntry.getName());
    setCustomName(sourcePlayerEntry.getCustomName());
    setHasToBeUpdated(sourcePlayerEntry.isHasToBeUpdated());
    setFlying(sourcePlayerEntry.isFlying());
    setHomes(sourcePlayerEntry.getHomes());
    setDeaths(sourcePlayerEntry.getDeaths());
    setPlayerState(sourcePlayerEntry.getPlayerState());
    setPlayerStateParameter(sourcePlayerEntry.getPlayerStateParameter());
    setPartner(sourcePlayerEntry.getPartner());
  }
}