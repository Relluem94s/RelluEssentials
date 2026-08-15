package de.relluem94.minecraft.server.spigot.essentials.registries;

import de.relluem94.minecraft.server.spigot.essentials.models.pojo.BagTypeEntry;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * In-memory registry for managing {@link BagTypeEntry} instances at runtime.
 */
public class BagTypeRegistry {

  private final List<BagTypeEntry> bagTypeEntries = new ArrayList<>();

  /**
   * Registers a single {@link BagTypeEntry} if it is not already registered.
   *
   * @param bagTypeEntry the {@link BagTypeEntry} to register
   * @throws IllegalArgumentException if the entry is already registered
   */
  public void register(BagTypeEntry bagTypeEntry) {
    if (contains(bagTypeEntry)) {
      throw new IllegalArgumentException("BagTypeEntry is already registered: " + bagTypeEntry);
    }
    bagTypeEntries.add(bagTypeEntry);
  }

  /**
   * Registers all given {@link BagTypeEntry} instances.
   *
   * @param entries the list of {@link BagTypeEntry} instances to register
   */
  public void registerAll(List<BagTypeEntry> entries) {
    entries.forEach(this::register);
  }

  /**
   * Unregisters a {@link BagTypeEntry} from the registry.
   *
   * @param bagTypeEntry the {@link BagTypeEntry} to unregister
   * @throws IllegalArgumentException if the entry is not registered
   */
  public void unregister(BagTypeEntry bagTypeEntry) {
    if (!contains(bagTypeEntry)) {
      throw new IllegalArgumentException("BagTypeEntry is not registered: " + bagTypeEntry);
    }
    bagTypeEntries.remove(bagTypeEntry);
  }

  /**
   * Checks whether a {@link BagTypeEntry} is registered.
   *
   * @param bagTypeEntry the {@link BagTypeEntry} to check
   * @return {@code true} if the entry is registered, {@code false} otherwise
   */
  public boolean contains(BagTypeEntry bagTypeEntry) {
    return bagTypeEntries.contains(bagTypeEntry);
  }

  /**
   * Returns an unmodifiable view of all registered {@link BagTypeEntry} instances.
   *
   * @return an unmodifiable {@link List} of all registered {@link BagTypeEntry} instances
   */
  public List<BagTypeEntry> getAll() {
    return List.copyOf(bagTypeEntries);
  }

  /**
   * Finds a {@link BagTypeEntry} by its unique id.
   *
   * @param id the unique id to search for
   * @return an {@link Optional} containing the entry, or empty if not found
   */
  public Optional<BagTypeEntry> findById(int id) {
    return bagTypeEntries.stream()
        .filter(entry -> entry.getId() == id)
        .findFirst();
  }

  /**
   * Finds a {@link BagTypeEntry} by its exact name.
   *
   * @param name the name to search for
   * @return an {@link Optional} containing the entry, or empty if not found
   */
  public Optional<BagTypeEntry> findByName(String name) {
    return bagTypeEntries.stream()
        .filter(entry -> entry.getName().equals(name))
        .findFirst();
  }

  /**
   * Finds a {@link BagTypeEntry} by a partial name match against display name or internal name.
   *
   * @param partialName the partial name to search for
   * @return an {@link Optional} containing the first matching entry, or empty if not found
   */
  public Optional<BagTypeEntry> findByPartialName(String partialName) {
    return bagTypeEntries.stream()
        .filter(entry ->
            partialName.contains(entry.getDisplayName())
                || partialName.contains(entry.getName().toLowerCase())
                || entry.getDisplayName().contains(partialName)
                || entry.getName().toLowerCase().contains(partialName))
        .findFirst();
  }
}