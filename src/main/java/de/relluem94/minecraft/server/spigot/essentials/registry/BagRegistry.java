package de.relluem94.minecraft.server.spigot.essentials.registry;

import de.relluem94.minecraft.server.spigot.essentials.model.pojo.BagTypeEntry;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.Getter;

/**
 * Registry for managing and looking up {@link BagTypeEntry} instances.
 */
@Getter
public class BagRegistry {

  private final List<BagTypeEntry> bagTypeEntries = new ArrayList<>();

  /**
   * Creates a new {@link BagRegistry} and registers all provided entries.
   *
   * @param bagTypes the initial list of {@link BagTypeEntry} to register
   */
  public BagRegistry(List<BagTypeEntry> bagTypes) {
    bagTypes.forEach(this::register);
  }

  /**
   * Registers a single {@link BagTypeEntry} if it is not already registered.
   *
   * @param bagTypeEntry the {@link BagTypeEntry} to register
   * @throws IllegalArgumentException if the entry is already registered
   */
  public void register(BagTypeEntry bagTypeEntry) {
    if (isRegistered(bagTypeEntry)) {
      throw new IllegalArgumentException("BagTypeEntry is already registered: " + bagTypeEntry);
    }
    bagTypeEntries.add(bagTypeEntry);
  }

  /**
   * Unregisters a {@link BagTypeEntry} from the registry.
   *
   * @param bagTypeEntry the {@link BagTypeEntry} to unregister
   * @throws IllegalArgumentException if the entry is not registered
   */
  public void unregister(BagTypeEntry bagTypeEntry) {
    if (!isRegistered(bagTypeEntry)) {
      throw new IllegalArgumentException("BagTypeEntry is not registered: " + bagTypeEntry);
    }
    bagTypeEntries.remove(bagTypeEntry);
  }

  /**
   * Finds a {@link BagTypeEntry} by its name.
   *
   * @param name the name to search for
   * @return an {@link Optional} containing the found {@link BagTypeEntry}, or empty if not found
   */
  public Optional<BagTypeEntry> findByName(String name) {
    return bagTypeEntries.stream()
        .filter(entry -> entry.getName().equalsIgnoreCase(name))
        .findFirst();
  }

  /**
   * Checks whether a {@link BagTypeEntry} is already registered.
   *
   * @param bagTypeEntry the {@link BagTypeEntry} to check
   * @return {@code true} if the entry is registered, {@code false} otherwise
   */
  public boolean isRegistered(BagTypeEntry bagTypeEntry) {
    return bagTypeEntries.contains(bagTypeEntry);
  }

  /**
   * Returns an unmodifiable view of all registered {@link BagTypeEntry} instances.
   *
   * @return an unmodifiable {@link List} of all registered {@link BagTypeEntry} instances
   */
  public List<BagTypeEntry> getAllEntries() {
    return List.copyOf(bagTypeEntries);
  }

  /**
   * Finds a {@link BagTypeEntry} by its unique identifier.
   *
   * @param id the unique identifier of the bag type to search for
   * @return an {@link Optional} containing the matching {@link BagTypeEntry}, or empty if not found
   */
  public Optional<BagTypeEntry> findById(int id) {
    return bagTypeEntries.stream()
        .filter(entry -> entry.getId() == id)
        .findFirst();
  }
}