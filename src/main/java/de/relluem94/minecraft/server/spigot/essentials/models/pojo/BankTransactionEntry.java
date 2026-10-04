package de.relluem94.minecraft.server.spigot.essentials.models.pojo;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a single transaction entry associated with a bank account,
 * including audit information for creation, updates, and deletion.
 *
 * @author rellu
 */
@Data
@NoArgsConstructor
public class BankTransactionEntry {

  private int id;
  private String created;
  private int createdBy;
  private String updated;
  private int updatedBy;
  private String deleted;
  private int deletedBy;
  private int bankAccountId;
  private double value;
}