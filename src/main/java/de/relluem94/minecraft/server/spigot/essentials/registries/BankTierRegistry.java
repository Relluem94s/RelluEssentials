package de.relluem94.minecraft.server.spigot.essentials.registries;

import de.relluem94.minecraft.server.spigot.essentials.models.pojo.BankTierEntry;
import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.Nullable;

/**
 * In-memory registry holding all available bank tiers loaded at startup.
 */
public class BankTierRegistry {

  private final List<BankTierEntry> bankTiersList = new ArrayList<>();

  /**
   * @param bankTierEntries the list of bank tiers to register
   */
  public BankTierRegistry(List<BankTierEntry> bankTierEntries) {
    bankTiersList.addAll(bankTierEntries);
  }

  /**
   * Returns all registered bank tiers.
   *
   * @return list of {@link BankTierEntry}
   */
  public List<BankTierEntry> getBankTiers() {
    return bankTiersList;
  }

  /**
   * Returns the bank tier with the given id.
   *
   * @param id the primary key of the bank tier to look up
   * @return the matching {@link BankTierEntry}, or {@code null} if none found
   */
  public @Nullable BankTierEntry getBankTierById(int id) {
    return bankTiersList.stream()
        .filter(bankTierEntry -> bankTierEntry.getId() == id)
        .findFirst()
        .orElse(null);
  }
}