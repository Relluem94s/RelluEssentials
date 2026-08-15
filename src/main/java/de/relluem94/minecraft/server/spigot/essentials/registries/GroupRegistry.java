package de.relluem94.minecraft.server.spigot.essentials.registries;

import de.relluem94.minecraft.server.spigot.essentials.models.pojo.GroupEntry;
import de.relluem94.minecraft.server.spigot.essentials.repositories.GroupRepository;
import java.util.List;
import java.util.Optional;

/**
 * Registry for managing active {@link GroupEntry} instances.
 * Delegates persistence operations to the underlying {@link GroupRepository}.
 *
 * @author rellu
 */
public class GroupRegistry {

  private final GroupRepository groupRepository;

  /**
   * Creates a new {@code GroupRegistry} backed by the given {@link GroupRepository}.
   *
   * @param groupRepository the repository used for storage operations
   */
  public GroupRegistry(GroupRepository groupRepository) {
    this.groupRepository = groupRepository;
  }

  /**
   * Registers a new {@link GroupEntry} if it is not already present.
   *
   * @param groupEntry the entry to register
   * @throws IllegalArgumentException if the entry is already registered
   */
  public void register(GroupEntry groupEntry) {
    if (contains(groupEntry)) {
      throw new IllegalArgumentException("GroupEntry is already registered: " + groupEntry);
    }
    groupRepository.save(groupEntry);
  }

  /**
   * Unregisters an existing {@link GroupEntry}.
   *
   * @param groupEntry the entry to unregister
   * @throws IllegalArgumentException if the entry is not registered
   */
  public void unregister(GroupEntry groupEntry) {
    if (!contains(groupEntry)) {
      throw new IllegalArgumentException("GroupEntry is not registered: " + groupEntry);
    }
    groupRepository.delete(groupEntry);
  }

  /**
   * Checks whether the given {@link GroupEntry} is currently registered.
   *
   * @param groupEntry the entry to check
   * @return {@code true} if registered, {@code false} otherwise
   */
  public boolean contains(GroupEntry groupEntry) {
    return groupRepository.findAll().contains(groupEntry);
  }

  /**
   * Checks whether a group with the given name is currently registered.
   *
   * @param name the name to check
   * @return {@code true} if a group with that name exists, {@code false} otherwise
   */
  public boolean containsByName(String name) {
    return groupRepository.findByName(name).isPresent();
  }

  /**
   * Returns all currently registered group entries.
   *
   * @return a list of all registered group entries
   */
  public List<GroupEntry> getAll() {
    return groupRepository.findAll();
  }

  /**
   * Finds a registered group entry by its unique ID.
   *
   * @param id the ID to search for
   * @return an {@link Optional} containing the matching entry, or empty if not found
   */
  public Optional<GroupEntry> findById(int id) {
    return groupRepository.findById(id);
  }

  /**
   * Finds a registered group entry by its name.
   *
   * @param name the name to search for
   * @return an {@link Optional} containing the matching entry, or empty if not found
   */
  public Optional<GroupEntry> findByName(String name) {
    return groupRepository.findByName(name);
  }
}