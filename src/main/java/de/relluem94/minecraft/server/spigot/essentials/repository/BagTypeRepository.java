package de.relluem94.minecraft.server.spigot.essentials.repository;

import de.relluem94.minecraft.server.spigot.essentials.model.pojo.BagTypeEntry;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Repository for accessing and managing {@link BagTypeEntry} data from the database.
 */
public class BagTypeRepository {

  private final List<BagTypeEntry> bagTypeEntries;

  /**
   * Creates a new {@link BagTypeRepository} with the given list of {@link BagTypeEntry} instances.
   *
   * @param bagTypeEntries the initial list of {@link BagTypeEntry} instances
   */
  public BagTypeRepository(List<BagTypeEntry> bagTypeEntries) {
    this.bagTypeEntries = new ArrayList<>(bagTypeEntries);
  }

  /**
   * Retrieves all {@link BagTypeEntry} instances from the database.
   *
   * @return a {@link List} of all {@link BagTypeEntry} instances
   */
  public List<BagTypeEntry> findAll() {
    return List.copyOf(bagTypeEntries);
  }

  /**
   * Finds a {@link BagTypeEntry} by its unique identifier.
   *
   * @param id the unique identifier to search for
   * @return an {@link Optional} containing the found {@link BagTypeEntry}, or empty if not found
   */
  public Optional<BagTypeEntry> findById(int id) {
    return bagTypeEntries.stream()
        .filter(entry -> entry.getId() == id)
        .findFirst();
  }

  /**
   * Finds a {@link BagTypeEntry} by its name.
   *
   * @param name the name to search for
   * @return an {@link Optional} containing the found {@link BagTypeEntry}, or empty if not found
   */
  public Optional<BagTypeEntry> findByName(String name) {
    return bagTypeEntries.stream()
        .filter(entry -> entry.getName().equals(name))
        .findFirst();
  }

  /**
   * Saves a {@link BagTypeEntry} to the database.
   *
   * @param bagTypeEntry the {@link BagTypeEntry} to save
   * @return the saved {@link BagTypeEntry}
   */
  public BagTypeEntry save(BagTypeEntry bagTypeEntry) {
    bagTypeEntries.add(bagTypeEntry);
    return bagTypeEntry;
  }

  /**
   * Deletes a {@link BagTypeEntry} from the database.
   *
   * @param bagTypeEntry the {@link BagTypeEntry} to delete
   */
  public void delete(BagTypeEntry bagTypeEntry) {
    bagTypeEntries.remove(bagTypeEntry);
  }
}