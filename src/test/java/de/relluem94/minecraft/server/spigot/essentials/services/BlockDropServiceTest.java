package de.relluem94.minecraft.server.spigot.essentials.services;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import de.relluem94.minecraft.server.spigot.essentials.repositories.CropRepository;
import de.relluem94.minecraft.server.spigot.essentials.repositories.DropRuleRepository;
import de.relluem94.rellulib.stores.DoubleStore;
import org.bukkit.Material;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BlockDropServiceTest {

  @Mock
  private DropRuleRepository dropRuleRepository;

  @Mock
  private CropRepository cropRepository;

  @Mock
  private DoubleStore<Integer, Integer> dropRuleRange;

  private BlockDropService blockDropService;

  @BeforeEach
  void setUp() {
    blockDropService = new BlockDropService(dropRuleRepository, cropRepository);
  }

  @Test
  void resolveDropAmountReturnsOriginalAmountWhenCurrentAmountIsNotOne() {
    int result = blockDropService.resolveDropAmount(Material.WHEAT, 5);
    assertAll(
        () -> assertEquals(5, result),
        () -> verifyNoInteractions(dropRuleRepository)
    );
  }

  @Test
  void resolveDropAmountReturnsOriginalAmountWhenNoDropRuleExists() {
    when(dropRuleRepository.hasDropRule(Material.WHEAT)).thenReturn(false);
    int result = blockDropService.resolveDropAmount(Material.WHEAT, 1);
    assertEquals(1, result);
  }

  @Test
  void resolveDropAmountReturnsCalculatedAmountWhenDropRuleExists() {
    when(dropRuleRepository.hasDropRule(Material.WHEAT)).thenReturn(true);
    when(dropRuleRepository.getDropRule(Material.WHEAT)).thenReturn(dropRuleRange);
    when(dropRuleRange.getSecondValue()).thenReturn(3);
    when(dropRuleRange.getValue()).thenReturn(2);

    int result = blockDropService.resolveDropAmount(Material.WHEAT, 1);

    assertAll(
        () -> assertTrue(result >= 2),
        () -> assertTrue(result <= 4)
    );
  }

  @Test
  void resolveDropAmountPropagatesExceptionFromDropRuleRepository() {
    when(dropRuleRepository.hasDropRule(Material.WHEAT)).thenReturn(true);
    when(dropRuleRepository.getDropRule(Material.WHEAT)).thenThrow(new RuntimeException("repository failure"));

    assertThrows(RuntimeException.class, () -> blockDropService.resolveDropAmount(Material.WHEAT, 1));
  }

  @Test
  void hasDropRuleReturnsTrueWhenRuleExists() {
    when(dropRuleRepository.hasDropRule(Material.WHEAT)).thenReturn(true);
    assertTrue(blockDropService.hasDropRule(Material.WHEAT));
  }

  @Test
  void hasDropRuleReturnsFalseWhenNoRuleExists() {
    when(dropRuleRepository.hasDropRule(Material.WHEAT)).thenReturn(false);
    assertFalse(blockDropService.hasDropRule(Material.WHEAT));
  }

  @Test
  void hasDropRulePropagatesExceptionFromRepository() {
    when(dropRuleRepository.hasDropRule(Material.WHEAT)).thenThrow(new RuntimeException("repository failure"));
    assertThrows(RuntimeException.class, () -> blockDropService.hasDropRule(Material.WHEAT));
  }

  @Test
  void isSeedReturnsTrueWhenMaterialIsSeed() {
    when(cropRepository.isSeed(Material.WHEAT_SEEDS)).thenReturn(true);
    assertTrue(blockDropService.isSeed(Material.WHEAT_SEEDS));
  }

  @Test
  void isSeedReturnsFalseWhenMaterialIsNotSeed() {
    when(cropRepository.isSeed(Material.WHEAT)).thenReturn(false);
    assertFalse(blockDropService.isSeed(Material.WHEAT));
  }

  @Test
  void isSeedPropagatesExceptionFromRepository() {
    when(cropRepository.isSeed(Material.WHEAT_SEEDS)).thenThrow(new RuntimeException("repository failure"));
    assertThrows(RuntimeException.class, () -> blockDropService.isSeed(Material.WHEAT_SEEDS));
  }

  @Test
  void getPlantForSeedReturnsCorrectPlantMaterial() {
    when(cropRepository.getPlant(Material.WHEAT_SEEDS)).thenReturn(Material.WHEAT);
    Material result = blockDropService.getPlantForSeed(Material.WHEAT_SEEDS);
    assertEquals(Material.WHEAT, result);
  }

  @Test
  void getPlantForSeedPropagatesExceptionFromRepository() {
    when(cropRepository.getPlant(Material.WHEAT_SEEDS)).thenThrow(new RuntimeException("repository failure"));
    assertThrows(RuntimeException.class, () -> blockDropService.getPlantForSeed(Material.WHEAT_SEEDS));
  }
}