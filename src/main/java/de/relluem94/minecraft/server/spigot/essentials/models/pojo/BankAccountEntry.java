package de.relluem94.minecraft.server.spigot.essentials.models.pojo;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a bank account entry with its associated metadata and financial information.
 *
 * @author rellu
 */
@Data
@NoArgsConstructor
public class BankAccountEntry {

  private int id;
  private String created;
  private int createdBy;
  private String updated;
  private int updatedBy;
  private String deleted;
  private int deletedBy;
  private int playerId;
  private double value;
  private BankTierEntry tier;
}