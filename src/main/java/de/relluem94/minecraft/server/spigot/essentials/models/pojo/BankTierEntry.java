package de.relluem94.minecraft.server.spigot.essentials.models.pojo;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a single bank tier configuration entry.
 *
 * <p>Each tier defines the properties of a bank level, including its unique identifier,
 * display name, deposit limit, interest rate, and upgrade cost.</p>
 *
 * @author rellu
 */
@Data
@NoArgsConstructor
public class BankTierEntry {

  private int id;
  private String name;
  private long limit;
  private double interest;
  private long cost;
}