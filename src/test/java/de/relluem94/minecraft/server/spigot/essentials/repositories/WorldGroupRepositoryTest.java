package de.relluem94.minecraft.server.spigot.essentials.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.relluem94.minecraft.server.spigot.essentials.models.pojo.PlayerEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.WorldEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.WorldGroupEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.WorldGroupInventoryEntry;
import de.relluem94.minecraft.server.spigot.essentials.persistence.dao.WorldGroupDao;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class WorldGroupRepositoryTest {

  @Mock
  private WorldGroupDao worldGroupDao;

  @Mock
  private PlayerEntry playerEntry;

  @Mock
  private WorldGroupEntry worldGroupEntry;

  @Mock
  private WorldGroupInventoryEntry worldGroupInventoryEntry;

  @Mock
  private WorldEntry worldEntry;

  private WorldGroupRepository worldGroupRepository;

  @BeforeEach
  void setUp() {
    worldGroupRepository = new WorldGroupRepository(worldGroupDao);
  }

  @Test
  void findAllWorldGroupsReturnsDaoResult() {
    List<WorldGroupEntry> expected = List.of(worldGroupEntry);
    when(worldGroupDao.findAllWorldGroups()).thenReturn(expected);

    List<WorldGroupEntry> result = worldGroupRepository.findAllWorldGroups();

    assertEquals(expected, result);
  }

  @Test
  void findAllWorldGroupsPropagatesException() {
    when(worldGroupDao.findAllWorldGroups()).thenThrow(new RuntimeException("dao failure"));

    assertThrows(RuntimeException.class, () -> worldGroupRepository.findAllWorldGroups());
  }

  @Test
  void findWorldsByGroupReturnsDaoResult() {
    List<WorldEntry> expected = List.of(worldEntry);
    when(worldGroupDao.findWorldsByGroup(worldGroupEntry)).thenReturn(expected);

    List<WorldEntry> result = worldGroupRepository.findWorldsByGroup(worldGroupEntry);

    assertEquals(expected, result);
  }

  @Test
  void findWorldsByGroupPropagatesException() {
    when(worldGroupDao.findWorldsByGroup(worldGroupEntry)).thenThrow(
        new RuntimeException("dao failure"));

    assertThrows(RuntimeException.class,
        () -> worldGroupRepository.findWorldsByGroup(worldGroupEntry));
  }

  @Test
  void findInventoryByGroupAndPlayerReturnsDaoResult() {
    when(worldGroupDao.findInventoryByGroupAndPlayer(playerEntry, worldGroupEntry)).thenReturn(
        worldGroupInventoryEntry);

    WorldGroupInventoryEntry result = worldGroupRepository.findInventoryByGroupAndPlayer(
        playerEntry, worldGroupEntry);

    assertEquals(worldGroupInventoryEntry, result);
  }

  @Test
  void findInventoryByGroupAndPlayerReturnsNullWhenDaoReturnsNull() {
    when(worldGroupDao.findInventoryByGroupAndPlayer(playerEntry, worldGroupEntry)).thenReturn(
        null);

    WorldGroupInventoryEntry result = worldGroupRepository.findInventoryByGroupAndPlayer(
        playerEntry, worldGroupEntry);

    assertNull(result);
  }

  @Test
  void findInventoryByGroupAndPlayerPropagatesException() {
    when(worldGroupDao.findInventoryByGroupAndPlayer(playerEntry, worldGroupEntry)).thenThrow(
        new RuntimeException("dao failure"));

    assertThrows(RuntimeException.class,
        () -> worldGroupRepository.findInventoryByGroupAndPlayer(playerEntry, worldGroupEntry));
  }

  @Test
  void saveInventoryDelegatesToDaoInsert() {
    worldGroupRepository.saveInventory(worldGroupInventoryEntry);

    verify(worldGroupDao).insertInventory(worldGroupInventoryEntry);
  }

  @Test
  void saveInventoryPropagatesException() {
    doThrow(new RuntimeException("dao failure")).when(worldGroupDao)
        .insertInventory(worldGroupInventoryEntry);

    assertThrows(RuntimeException.class,
        () -> worldGroupRepository.saveInventory(worldGroupInventoryEntry));
  }

  @Test
  void updateInventoryDelegatesToDaoUpdate() {
    worldGroupRepository.updateInventory(worldGroupInventoryEntry);

    verify(worldGroupDao).updateInventory(worldGroupInventoryEntry);
  }

  @Test
  void updateInventoryPropagatesException() {
    doThrow(new RuntimeException("dao failure")).when(worldGroupDao)
        .updateInventory(worldGroupInventoryEntry);

    assertThrows(RuntimeException.class,
        () -> worldGroupRepository.updateInventory(worldGroupInventoryEntry));
  }

  @Test
  void saveWorldGroupDelegatesToDaoInsert() {
    worldGroupRepository.saveWorldGroup(worldGroupEntry);

    verify(worldGroupDao).insertWorldGroup(worldGroupEntry);
  }

  @Test
  void saveWorldGroupPropagatesException() {
    doThrow(new RuntimeException("dao failure")).when(worldGroupDao)
        .insertWorldGroup(worldGroupEntry);

    assertThrows(RuntimeException.class,
        () -> worldGroupRepository.saveWorldGroup(worldGroupEntry));
  }

  @Test
  void findWorldGroupByNameReturnsDaoResult() {
    String groupName = "overworld";
    when(worldGroupDao.findWorldGroupByName(groupName)).thenReturn(worldGroupEntry);

    WorldGroupEntry result = worldGroupRepository.findWorldGroupByName(groupName);

    assertEquals(worldGroupEntry, result);
  }

  @Test
  void findWorldGroupByNameReturnsNullWhenDaoReturnsNull() {
    String groupName = "unknown";
    when(worldGroupDao.findWorldGroupByName(groupName)).thenReturn(null);

    WorldGroupEntry result = worldGroupRepository.findWorldGroupByName(groupName);

    assertNull(result);
  }

  @Test
  void findWorldGroupByNamePropagatesException() {
    String groupName = "overworld";
    when(worldGroupDao.findWorldGroupByName(groupName)).thenThrow(
        new RuntimeException("dao failure"));

    assertThrows(RuntimeException.class,
        () -> worldGroupRepository.findWorldGroupByName(groupName));
  }

  @Test
  void saveWorldDelegatesToDaoInsert() {
    worldGroupRepository.saveWorld(worldEntry);

    verify(worldGroupDao).insertWorld(worldEntry);
  }

  @Test
  void saveWorldPropagatesException() {
    doThrow(new RuntimeException("dao failure")).when(worldGroupDao).insertWorld(worldEntry);

    assertThrows(RuntimeException.class, () -> worldGroupRepository.saveWorld(worldEntry));
  }
}