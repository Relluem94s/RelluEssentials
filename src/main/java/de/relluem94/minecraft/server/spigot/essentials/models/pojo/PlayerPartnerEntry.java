package de.relluem94.minecraft.server.spigot.essentials.models.pojo;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a partnership relationship between two player profiles in the Essentials plugin.
 *
 * <p>A PlayerPartnerEntry holds persistent metadata about a partnership between two players,
 * including audit information (creation, update and deletion data), whether protections are shared between the
 * partners, and the identifiers of both partner players.</p>
 *
 * @author rellu
 */
@Data
@NoArgsConstructor
public class PlayerPartnerEntry {

  private int id;
  private String created;
  private int createdBy;
  private String updated;
  private int updatedBy;
  private String deleted;
  private int deletedBy;
  private boolean shareProtections;
  private int firstPartnerId;
  private int secondPartnerId;
}