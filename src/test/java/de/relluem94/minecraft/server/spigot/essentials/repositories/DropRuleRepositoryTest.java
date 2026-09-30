package de.relluem94.minecraft.server.spigot.essentials.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import de.relluem94.minecraft.server.spigot.essentials.models.pojo.DropEntry;
import de.relluem94.minecraft.server.spigot.essentials.persistence.dao.DropDao;
import de.relluem94.rellulib.stores.DoubleStore;
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
class DropRuleRepositoryTest {

  @Mock
  private DropDao dropDao;

  @Mock
  private DropEntry dropEntityDiamond;

  @Mock
  private DropEntry dropEntityGold;

  @BeforeEach
  void setUp() {
    when(dropDao.findAll()).thenReturn(List.of());
  }

  @Test
  void constructorLoadsAllDropRulesFromDao() {
    when(dropEntityDiamond.getMaterial()).thenReturn(Material.DIAMOND_ORE);
    when(dropEntityDiamond.getMin()).thenReturn(1);
    when(dropEntityDiamond.getMax()).thenReturn(3);
    when(dropDao.findAll()).thenReturn(List.of(dropEntityDiamond));

    DropRuleRepository repository = new DropRuleRepository(dropDao);

    assertTrue(repository.hasDropRule(Material.DIAMOND_ORE));
  }

  @Test
  void constructorLoadsMultipleDropRulesFromDao() {
    when(dropEntityDiamond.getMaterial()).thenReturn(Material.DIAMOND_ORE);
    when(dropEntityDiamond.getMin()).thenReturn(1);
    when(dropEntityDiamond.getMax()).thenReturn(3);
    when(dropEntityGold.getMaterial()).thenReturn(Material.GOLD_ORE);
    when(dropEntityGold.getMin()).thenReturn(2);
    when(dropEntityGold.getMax()).thenReturn(5);
    when(dropDao.findAll()).thenReturn(List.of(dropEntityDiamond, dropEntityGold));

    DropRuleRepository repository = new DropRuleRepository(dropDao);

    assertTrue(repository.hasDropRule(Material.DIAMOND_ORE));
    assertTrue(repository.hasDropRule(Material.GOLD_ORE));
  }

  @Test
  void constructorWithEmptyDaoResultsInNoDropRules() {
    when(dropDao.findAll()).thenReturn(List.of());

    DropRuleRepository repository = new DropRuleRepository(dropDao);

    assertFalse(repository.hasDropRule(Material.DIAMOND_ORE));
  }

  @Test
  void hasDropRuleReturnsTrueForRegisteredMaterial() {
    when(dropEntityDiamond.getMaterial()).thenReturn(Material.DIAMOND_ORE);
    when(dropEntityDiamond.getMin()).thenReturn(1);
    when(dropEntityDiamond.getMax()).thenReturn(3);
    when(dropDao.findAll()).thenReturn(List.of(dropEntityDiamond));

    DropRuleRepository repository = new DropRuleRepository(dropDao);

    assertTrue(repository.hasDropRule(Material.DIAMOND_ORE));
  }

  @Test
  void hasDropRuleReturnsFalseForUnregisteredMaterial() {
    when(dropDao.findAll()).thenReturn(List.of());

    DropRuleRepository repository = new DropRuleRepository(dropDao);

    assertFalse(repository.hasDropRule(Material.DIAMOND_ORE));
  }

  @ParameterizedTest
  @EnumSource(value = Material.class, names = {"DIAMOND_ORE", "GOLD_ORE", "IRON_ORE"})
  void hasDropRuleReturnsFalseForAnyUnregisteredMaterial(Material material) {
    when(dropDao.findAll()).thenReturn(List.of());

    DropRuleRepository repository = new DropRuleRepository(dropDao);

    assertFalse(repository.hasDropRule(material));
  }

  @Test
  void getDropRuleReturnsCorrectMinValue() {
    when(dropEntityDiamond.getMaterial()).thenReturn(Material.DIAMOND_ORE);
    when(dropEntityDiamond.getMin()).thenReturn(1);
    when(dropEntityDiamond.getMax()).thenReturn(3);
    when(dropDao.findAll()).thenReturn(List.of(dropEntityDiamond));

    DropRuleRepository repository = new DropRuleRepository(dropDao);
    DoubleStore<Integer, Integer> dropRule = repository.getDropRule(Material.DIAMOND_ORE);

    assertEquals(1, dropRule.getValue());
  }

  @Test
  void getDropRuleReturnsCorrectMaxValue() {
    when(dropEntityDiamond.getMaterial()).thenReturn(Material.DIAMOND_ORE);
    when(dropEntityDiamond.getMin()).thenReturn(1);
    when(dropEntityDiamond.getMax()).thenReturn(3);
    when(dropDao.findAll()).thenReturn(List.of(dropEntityDiamond));

    DropRuleRepository repository = new DropRuleRepository(dropDao);
    DoubleStore<Integer, Integer> dropRule = repository.getDropRule(Material.DIAMOND_ORE);

    assertEquals(3, dropRule.getSecondValue());
  }

  @Test
  void getDropRuleReturnsNullForUnregisteredMaterial() {
    when(dropDao.findAll()).thenReturn(List.of());

    DropRuleRepository repository = new DropRuleRepository(dropDao);

    assertNull(repository.getDropRule(Material.DIAMOND_ORE));
  }

  @Test
  void getDropRuleReturnsCorrectRuleForEachMaterialIndependently() {
    when(dropEntityDiamond.getMaterial()).thenReturn(Material.DIAMOND_ORE);
    when(dropEntityDiamond.getMin()).thenReturn(1);
    when(dropEntityDiamond.getMax()).thenReturn(3);
    when(dropEntityGold.getMaterial()).thenReturn(Material.GOLD_ORE);
    when(dropEntityGold.getMin()).thenReturn(2);
    when(dropEntityGold.getMax()).thenReturn(5);
    when(dropDao.findAll()).thenReturn(List.of(dropEntityDiamond, dropEntityGold));

    DropRuleRepository repository = new DropRuleRepository(dropDao);
    DoubleStore<Integer, Integer> diamondRule = repository.getDropRule(Material.DIAMOND_ORE);
    DoubleStore<Integer, Integer> goldRule = repository.getDropRule(Material.GOLD_ORE);

    assertEquals(1, diamondRule.getValue());
    assertEquals(3, diamondRule.getSecondValue());
    assertEquals(2, goldRule.getValue());
    assertEquals(5, goldRule.getSecondValue());
  }

  @Test
  void constructorPropagatesDaoException() {
    when(dropDao.findAll()).thenThrow(new RuntimeException("DAO failure"));

    assertThrows(RuntimeException.class, () -> new DropRuleRepository(dropDao));
  }
}