package de.relluem94.minecraft.server.spigot.essentials.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.relluem94.minecraft.server.spigot.essentials.models.pojo.GroupEntry;
import de.relluem94.minecraft.server.spigot.essentials.persistence.dao.GroupDao;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GroupRepositoryTest {

  @Mock
  private GroupDao groupDao;

  @Mock
  private GroupEntry firstGroupEntry;

  @Mock
  private GroupEntry secondGroupEntry;

  private GroupRepository groupRepository;

  @BeforeEach
  void setUp() {
    when(groupDao.findAll()).thenReturn(List.of(firstGroupEntry, secondGroupEntry));
    lenient().when(firstGroupEntry.getName()).thenReturn("admin");
    lenient().when(firstGroupEntry.getId()).thenReturn(1);
    lenient().when(secondGroupEntry.getName()).thenReturn("member");
    lenient().when(secondGroupEntry.getId()).thenReturn(2);
    groupRepository = new GroupRepository(groupDao);
  }

  @Test
  void constructorLoadsAllEntriesFromDao() {
    verify(groupDao, times(1)).findAll();
    assertEquals(2, groupRepository.findAll().size());
  }

  @Test
  void findAllReturnsImmutableCopyOfAllEntries() {
    List<GroupEntry> result = groupRepository.findAll();
    assertEquals(2, result.size());
    assertTrue(result.contains(firstGroupEntry));
    assertTrue(result.contains(secondGroupEntry));
  }

  @Test
  void findAllReturnsCopyNotOriginalList() {
    List<GroupEntry> result = groupRepository.findAll();
    assertThrows(UnsupportedOperationException.class, () -> result.add(mock(GroupEntry.class)));
  }

  @ParameterizedTest
  @ValueSource(strings = {"admin", "ADMIN", "Admin", "aDmIn"})
  void findByNameReturnsPresentOptionalCaseInsensitive(String nameVariant) {
    Optional<GroupEntry> result = groupRepository.findByName(nameVariant);
    assertTrue(result.isPresent());
    assertEquals(firstGroupEntry, result.get());
  }

  @Test
  void findByNameReturnsEmptyOptionalWhenNoMatch() {
    Optional<GroupEntry> result = groupRepository.findByName("nonexistent");
    assertTrue(result.isEmpty());
  }

  @Test
  void findByNameReturnsCorrectEntryForSecondGroup() {
    Optional<GroupEntry> result = groupRepository.findByName("member");
    assertTrue(result.isPresent());
    assertEquals(secondGroupEntry, result.get());
  }

  @Test
  void findByIdReturnsPresentOptionalForExistingId() {
    Optional<GroupEntry> result = groupRepository.findById(1);
    assertTrue(result.isPresent());
    assertEquals(firstGroupEntry, result.get());
  }

  @Test
  void findByIdReturnsCorrectEntryForSecondId() {
    Optional<GroupEntry> result = groupRepository.findById(2);
    assertTrue(result.isPresent());
    assertEquals(secondGroupEntry, result.get());
  }

  @Test
  void findByIdReturnsEmptyOptionalForNonExistentId() {
    Optional<GroupEntry> result = groupRepository.findById(999);
    assertTrue(result.isEmpty());
  }

  @Test
  void saveInsertsEntryIntoDaoAndAddsToInMemoryList() {
    GroupEntry newEntry = mock(GroupEntry.class);
    lenient().when(newEntry.getName()).thenReturn("moderator");

    GroupEntry savedEntry = groupRepository.save(newEntry);

    verify(groupDao, times(1)).insert(newEntry);
    assertEquals(newEntry, savedEntry);
    assertTrue(groupRepository.findAll().contains(newEntry));
  }

  @Test
  void saveReturnsTheSameEntryThatWasProvided() {
    GroupEntry newEntry = mock(GroupEntry.class);
    GroupEntry savedEntry = groupRepository.save(newEntry);
    assertSame(newEntry, savedEntry);
  }

  @Test
  void saveIncreasesInMemoryListSizeByOne() {
    GroupEntry newEntry = mock(GroupEntry.class);
    int sizeBeforeSave = groupRepository.findAll().size();
    groupRepository.save(newEntry);
    assertEquals(sizeBeforeSave + 1, groupRepository.findAll().size());
  }

  @Test
  void savePropagatesDaoException() {
    GroupEntry newEntry = mock(GroupEntry.class);
    doThrow(new RuntimeException("DB error")).when(groupDao).insert(newEntry);
    assertThrows(RuntimeException.class, () -> groupRepository.save(newEntry));
  }

  @Test
  void deleteRemovesEntryFromInMemoryList() {
    groupRepository.delete(firstGroupEntry);
    assertFalse(groupRepository.findAll().contains(firstGroupEntry));
  }

  @Test
  void deleteReducesInMemoryListSizeByOne() {
    int sizeBeforeDelete = groupRepository.findAll().size();
    groupRepository.delete(firstGroupEntry);
    assertEquals(sizeBeforeDelete - 1, groupRepository.findAll().size());
  }

  @Test
  void deleteNonExistentEntryDoesNotChangeListSize() {
    GroupEntry nonExistentEntry = mock(GroupEntry.class);
    int sizeBeforeDelete = groupRepository.findAll().size();
    groupRepository.delete(nonExistentEntry);
    assertEquals(sizeBeforeDelete, groupRepository.findAll().size());
  }

  @Test
  void deleteDoesNotRemoveOtherEntries() {
    groupRepository.delete(firstGroupEntry);
    assertTrue(groupRepository.findAll().contains(secondGroupEntry));
  }

  @Test
  void findByNameAfterDeleteReturnsEmpty() {
    groupRepository.delete(firstGroupEntry);
    Optional<GroupEntry> result = groupRepository.findByName("admin");
    assertTrue(result.isEmpty());
  }

  @Test
  void findByIdAfterDeleteReturnsEmpty() {
    groupRepository.delete(firstGroupEntry);
    Optional<GroupEntry> result = groupRepository.findById(1);
    assertTrue(result.isEmpty());
  }
}