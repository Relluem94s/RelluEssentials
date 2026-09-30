package de.relluem94.minecraft.server.spigot.essentials.models.pojo;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.bukkit.Material;

/**
 * Represents a single protection lock entry containing auditing metadata
 * and the protected material value.
 *
 * @author rellu
 */
@Data
@NoArgsConstructor
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