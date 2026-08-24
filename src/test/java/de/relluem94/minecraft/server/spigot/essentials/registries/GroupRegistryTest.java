package de.relluem94.minecraft.server.spigot.essentials.registries;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.relluem94.minecraft.server.spigot.essentials.models.pojo.GroupEntry;
import de.relluem94.minecraft.server.spigot.essentials.repositories.GroupRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link GroupRegistry}.
 *
 * @author rellu
 */
class GroupRegistryTest {

  private GroupRepository groupRepository;
  private GroupRegistry groupRegistry;

  @BeforeEach
  void setUp() {
    groupRepository = mock(GroupRepository.class);
    groupRegistry = new GroupRegistry(groupRepository);
  }

  @Test
  void register_ShouldSaveEntry_WhenEntryIsNotPresent() {
    GroupEntry entry = new GroupEntry(1, "admin", "&cAdmin");
    when(groupRepository.findAll()).thenReturn(List.of());

    groupRegistry.register(entry);

    verify(groupRepository, times(1)).save(entry);
  }

  @Test
  void register_ShouldThrowException_WhenEntryIsAlreadyRegistered() {
    GroupEntry entry = new GroupEntry(1, "admin", "&cAdmin");
    when(groupRepository.findAll()).thenReturn(List.of(entry));

    assertThrows(IllegalArgumentException.class, () -> groupRegistry.register(entry));
    verify(groupRepository, never()).save(any());
  }

  @Test
  void unregister_ShouldDeleteEntry_WhenEntryIsRegistered() {
    GroupEntry entry = new GroupEntry(1, "admin", "&cAdmin");
    when(groupRepository.findAll()).thenReturn(List.of(entry));

    groupRegistry.unregister(entry);

    verify(groupRepository, times(1)).delete(entry);
  }

  @Test
  void unregister_ShouldThrowException_WhenEntryIsNotRegistered() {
    GroupEntry entry = new GroupEntry(1, "admin", "&cAdmin");
    when(groupRepository.findAll()).thenReturn(List.of());

    assertThrows(IllegalArgumentException.class, () -> groupRegistry.unregister(entry));
    verify(groupRepository, never()).delete(any());
  }

  @Test
  void contains_ShouldReturnTrue_WhenEntryExists() {
    GroupEntry entry = new GroupEntry(1, "admin", "&cAdmin");
    when(groupRepository.findAll()).thenReturn(List.of(entry));

    assertTrue(groupRegistry.contains(entry));
  }

  @Test
  void contains_ShouldReturnFalse_WhenEntryDoesNotExist() {
    GroupEntry entry = new GroupEntry(1, "admin", "&cAdmin");
    when(groupRepository.findAll()).thenReturn(List.of());

    assertFalse(groupRegistry.contains(entry));
  }

  @Test
  void containsByName_ShouldReturnTrue_WhenNameExists() {
    when(groupRepository.findByName("admin")).thenReturn(Optional.of(new GroupEntry(1, "admin", "&cAdmin")));

    assertTrue(groupRegistry.containsByName("admin"));
  }

  @Test
  void containsByName_ShouldReturnFalse_WhenNameDoesNotExist() {
    when(groupRepository.findByName("nonexistent")).thenReturn(Optional.empty());

    assertFalse(groupRegistry.containsByName("nonexistent"));
  }

  @Test
  void getAll_ShouldReturnAllEntries() {
    List<GroupEntry> entries = List.of(new GroupEntry(1, "admin", "&cAdmin"), new GroupEntry(2, "user", "&fUser"));
    when(groupRepository.findAll()).thenReturn(entries);

    assertEquals(entries, groupRegistry.getAll());
  }

  @Test
  void findById_ShouldReturnEntry_WhenIdExists() {
    GroupEntry entry = new GroupEntry(1, "admin", "&cAdmin");
    when(groupRepository.findById(1)).thenReturn(Optional.of(entry));

    Optional<GroupEntry> result = groupRegistry.findById(1);

    assertTrue(result.isPresent());
    assertEquals(entry, result.get());
  }

  @Test
  void findById_ShouldReturnEmpty_WhenIdDoesNotExist() {
    when(groupRepository.findById(99)).thenReturn(Optional.empty());

    Optional<GroupEntry> result = groupRegistry.findById(99);

    assertFalse(result.isPresent());
  }

  @Test
  void findByName_ShouldReturnEntry_WhenNameExists() {
    GroupEntry entry = new GroupEntry(1, "admin", "&cAdmin");
    when(groupRepository.findByName("admin")).thenReturn(Optional.of(entry));

    Optional<GroupEntry> result = groupRegistry.findByName("admin");

    assertTrue(result.isPresent());
    assertEquals(entry, result.get());
  }

  @Test
  void findByName_ShouldReturnEmpty_WhenNameDoesNotExist() {
    when(groupRepository.findByName("nonexistent")).thenReturn(Optional.empty());

    Optional<GroupEntry> result = groupRegistry.findByName("nonexistent");

    assertFalse(result.isPresent());
  }
}