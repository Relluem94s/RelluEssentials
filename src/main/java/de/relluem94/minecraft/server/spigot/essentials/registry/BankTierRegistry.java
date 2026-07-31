package de.relluem94.minecraft.server.spigot.essentials.registry;

import de.relluem94.minecraft.server.spigot.essentials.model.pojo.BankTierEntry;
import java.util.ArrayList;
import java.util.List;

public class BankTierRegistry {

  private final List<BankTierEntry> bankTiersList = new ArrayList<>();

  /**
   *
   * @param bankTierEntries List of BankTierEntry
   */
  public BankTierRegistry(List<BankTierEntry> bankTierEntries) {
    bankTiersList.addAll(bankTierEntries);
  }

  /**
   * Gives back a List of all BankTiers.
   *
   * @return List of BankTierEntry
   */
  public List<BankTierEntry> getBankTiers() {
    return bankTiersList;
  }
}