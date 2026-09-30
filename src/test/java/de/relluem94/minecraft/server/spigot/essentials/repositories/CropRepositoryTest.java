package de.relluem94.minecraft.server.spigot.essentials.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import de.relluem94.minecraft.server.spigot.essentials.models.pojo.CropEntry;
import de.relluem94.minecraft.server.spigot.essentials.persistence.dao.CropDao;
import java.util.List;
import org.bukkit.Material;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CropRepositoryTest {

  @Mock
  private CropDao cropDao;

  @Mock
  private CropEntry cropEntryWheat;

  @Mock
  private CropEntry cropEntryCarrot;

  @BeforeEach
  void setUp() {
    when(cropDao.findAll()).thenReturn(List.of());
  }

  @Test
  void constructorLoadsAllCropEntriesFromDao() {
    when(cropEntryWheat.getSeed()).thenReturn(Material.WHEAT_SEEDS);
    when(cropEntryWheat.getPlant()).thenReturn(Material.WHEAT);
    when(cropDao.findAll()).thenReturn(List.of(cropEntryWheat));

    CropRepository repository = new CropRepository(cropDao);

    assertTrue(repository.isSeed(Material.WHEAT_SEEDS));
  }

  @Test
  void constructorLoadsMultipleCropEntriesFromDao() {
    when(cropEntryWheat.getSeed()).thenReturn(Material.WHEAT_SEEDS);
    when(cropEntryWheat.getPlant()).thenReturn(Material.WHEAT);
    when(cropEntryCarrot.getSeed()).thenReturn(Material.CARROT);
    when(cropEntryCarrot.getPlant()).thenReturn(Material.CARROTS);
    when(cropDao.findAll()).thenReturn(List.of(cropEntryWheat, cropEntryCarrot));

    CropRepository repository = new CropRepository(cropDao);

    assertTrue(repository.isSeed(Material.WHEAT_SEEDS));
    assertTrue(repository.isSeed(Material.CARROT));
  }

  @Test
  void constructorWithEmptyDaoResultsInNoSeeds() {
    when(cropDao.findAll()).thenReturn(List.of());

    CropRepository repository = new CropRepository(cropDao);

    assertFalse(repository.isSeed(Material.WHEAT_SEEDS));
  }

  @Test
  void constructorPropagatesDaoException() {
    when(cropDao.findAll()).thenThrow(new RuntimeException("DAO failure"));

    assertThrows(RuntimeException.class, () -> new CropRepository(cropDao));
  }

  @Test
  void registerAddsSeedToPlantMapping() {
    CropRepository repository = new CropRepository(cropDao);

    repository.register(Material.WHEAT_SEEDS, Material.WHEAT);

    assertTrue(repository.isSeed(Material.WHEAT_SEEDS));
  }

  @Test
  void registerOverwritesExistingMapping() {
    when(cropEntryWheat.getSeed()).thenReturn(Material.WHEAT_SEEDS);
    when(cropEntryWheat.getPlant()).thenReturn(Material.WHEAT);
    when(cropDao.findAll()).thenReturn(List.of(cropEntryWheat));

    CropRepository repository = new CropRepository(cropDao);
    repository.register(Material.WHEAT_SEEDS, Material.CARROTS);

    assertEquals(Material.CARROTS, repository.getPlant(Material.WHEAT_SEEDS));
  }

  @Test
  void registerMultipleMappingsAllResolvable() {
    CropRepository repository = new CropRepository(cropDao);

    repository.register(Material.WHEAT_SEEDS, Material.WHEAT);
    repository.register(Material.CARROT, Material.CARROTS);

    assertTrue(repository.isSeed(Material.WHEAT_SEEDS));
    assertTrue(repository.isSeed(Material.CARROT));
  }

  @Test
  void isSeedReturnsTrueForRegisteredSeed() {
    when(cropEntryWheat.getSeed()).thenReturn(Material.WHEAT_SEEDS);
    when(cropEntryWheat.getPlant()).thenReturn(Material.WHEAT);
    when(cropDao.findAll()).thenReturn(List.of(cropEntryWheat));

    CropRepository repository = new CropRepository(cropDao);

    assertTrue(repository.isSeed(Material.WHEAT_SEEDS));
  }

  @Test
  void isSeedReturnsFalseForUnregisteredSeed() {
    CropRepository repository = new CropRepository(cropDao);

    assertFalse(repository.isSeed(Material.WHEAT_SEEDS));
  }

  @ParameterizedTest
  @EnumSource(value = Material.class, names = {"WHEAT_SEEDS", "CARROT", "POTATO", "BEETROOT_SEEDS"})
  void isSeedReturnsFalseForAnyUnregisteredSeed(Material material) {
    CropRepository repository = new CropRepository(cropDao);

    assertFalse(repository.isSeed(material));
  }

  @Test
  void getPlantReturnsCorrectPlantForRegisteredSeed() {
    when(cropEntryWheat.getSeed()).thenReturn(Material.WHEAT_SEEDS);
    when(cropEntryWheat.getPlant()).thenReturn(Material.WHEAT);
    when(cropDao.findAll()).thenReturn(List.of(cropEntryWheat));

    CropRepository repository = new CropRepository(cropDao);

    assertEquals(Material.WHEAT, repository.getPlant(Material.WHEAT_SEEDS));
  }

  @Test
  void getPlantReturnsNullForUnregisteredSeed() {
    CropRepository repository = new CropRepository(cropDao);

    assertNull(repository.getPlant(Material.WHEAT_SEEDS));
  }

  @Test
  void getPlantReturnsCorrectPlantForEachSeedIndependently() {
    when(cropEntryWheat.getSeed()).thenReturn(Material.WHEAT_SEEDS);
    when(cropEntryWheat.getPlant()).thenReturn(Material.WHEAT);
    when(cropEntryCarrot.getSeed()).thenReturn(Material.CARROT);
    when(cropEntryCarrot.getPlant()).thenReturn(Material.CARROTS);
    when(cropDao.findAll()).thenReturn(List.of(cropEntryWheat, cropEntryCarrot));

    CropRepository repository = new CropRepository(cropDao);

    assertEquals(Material.WHEAT, repository.getPlant(Material.WHEAT_SEEDS));
    assertEquals(Material.CARROTS, repository.getPlant(Material.CARROT));
  }
}