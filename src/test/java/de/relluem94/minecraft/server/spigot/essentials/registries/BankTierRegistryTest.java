package de.relluem94.minecraft.server.spigot.essentials.registries;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import de.relluem94.minecraft.server.spigot.essentials.models.pojo.BankTierEntry;
import java.util.List;
import org.junit.jupiter.api.Test;

class BankTierRegistryTest {

  @Test
  void shouldReturnAllRegisteredBankTiers() {
    BankTierEntry tier1 = createTier(1, "Bronze");
    BankTierEntry tier2 = createTier(2, "Silver");
    BankTierRegistry registry = new BankTierRegistry(List.of(tier1, tier2));

    List<BankTierEntry> result = registry.getBankTiers();

    assertEquals(2, result.size());
    assertEquals(tier1, result.get(0));
    assertEquals(tier2, result.get(1));
  }

  @Test
  void shouldReturnBankTierWhenIdExists() {
    BankTierEntry tier1 = createTier(1, "Bronze");
    BankTierEntry tier2 = createTier(2, "Silver");
    BankTierRegistry registry = new BankTierRegistry(List.of(tier1, tier2));

    BankTierEntry result = registry.getBankTierById(2);

    assertEquals(tier2, result);
  }

  @Test
  void shouldReturnNullWhenIdDoesNotExist() {
    BankTierEntry tier1 = createTier(1, "Bronze");
    BankTierRegistry registry = new BankTierRegistry(List.of(tier1));

    BankTierEntry result = registry.getBankTierById(999);

    assertNull(result);
  }

  private BankTierEntry createTier(int id, String name) {
    BankTierEntry tier = new BankTierEntry();
    tier.setId(id);
    tier.setName(name);
    tier.setLimit(1000L);
    tier.setInterest(0.01);
    tier.setCost(500L);
    return tier;
  }
}