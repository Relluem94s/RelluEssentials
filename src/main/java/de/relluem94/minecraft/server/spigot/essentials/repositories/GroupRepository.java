package de.relluem94.minecraft.server.spigot.essentials.repositories;

import de.relluem94.minecraft.server.spigot.essentials.models.pojo.GroupEntry;
import de.relluem94.minecraft.server.spigot.essentials.persistence.dao.GroupDao;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Repository for managing {@link GroupEntry} instances in memory,
 * backed by a {@link GroupDao} for persistence operations.
 *
 * @author rellu
 */
public class GroupRepository {

  private final List<GroupEntry> groupEntries;
  private final GroupDao groupDao;

  /**
   * Creates a new {@code GroupRepository} using the given {@link GroupDao}.
   * Loads all existing group entries from the database on initialization.
   *
   * @param groupDao the DAO used for database operations
   */
  public GroupRepository(GroupDao groupDao) {
    this.groupDao = groupDao;
    this.groupEntries = new ArrayList<>(groupDao.findAll());
  }

  /**
   * Returns an immutable copy of all currently loaded group entries.
   *
   * @return a list of all group entries
   */
  public List<GroupEntry> findAll() {
    return List.copyOf(groupEntries);
  }

  /**
   * Finds a group entry by its name (case-insensitive).
   *
   * @param name the name to search for
   * @return an {@link Optional} containing the matching entry, or empty if not found
   */
  public Optional<GroupEntry> findByName(String name) {
    return groupEntries.stream()
        .filter(entry -> entry.getName().equalsIgnoreCase(name))
        .findFirst();
  }

  /**
   * Finds a group entry by its unique ID.
   *
   * @param id the ID to search for
   * @return an {@link Optional} containing the matching entry, or empty if not found
   */
  public Optional<GroupEntry> findById(int id) {
    return groupEntries.stream()
        .filter(entry -> entry.getId() == id)
        .findFirst();
  }

  /**
   * Saves a {@link GroupEntry} to both the in-memory list and the database.
   *
   * @param groupEntry the entry to save
   * @return the saved group entry
   */
  public GroupEntry save(GroupEntry groupEntry) {
    groupDao.insert(groupEntry);
    groupEntries.add(groupEntry);
    return groupEntry;
  }

  /**
   * Removes a {@link GroupEntry} from the in-memory list.
   *
   * @param groupEntry the entry to delete
   */
  public void delete(GroupEntry groupEntry) {
    groupEntries.remove(groupEntry);
  }
}