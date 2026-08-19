package de.relluem94.minecraft.server.spigot.essentials.registries;

import de.relluem94.minecraft.server.spigot.essentials.models.pojo.BankTierEntry;
import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.Nullable;

/**
 * In-memory registry holding all available bank tiers loaded at startup.
 * This registry provides methods to retrieve the full list of tiers or
 * look up a specific tier by its unique identifier.
 */
public class BankTierRegistry {

  private final List<BankTierEntry> bankTiersList = new ArrayList<>();

  /**
   * Constructs a new BankTierRegistry with the provided list of bank tier entries.
   *
   * @param bankTierEntries the list of bank tiers to register
   */
  public BankTierRegistry(List<BankTierEntry> bankTierEntries) {
    bankTiersList.addAll(bankTierEntries);
  }

  /**
   * Returns all registered bank tiers.
   *
   * @return a list containing all {@link BankTierEntry} objects currently in the registry
   */
  public List<BankTierEntry> getBankTiers() {
    return bankTiersList;
  }

  /**
   * Returns the bank tier with the given id.
   *
   * @param id the primary key of the bank tier to look up
   * @return the matching {@link BankTierEntry}, or {@code null} if no tier with the given id exists
   */
  public @Nullable BankTierEntry getBankTierById(int id) {
    return bankTiersList.stream()
        .filter(bankTierEntry -> bankTierEntry.getId() == id)
        .findFirst()
        .orElse(null);
  }
}