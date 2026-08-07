package de.relluem94.minecraft.server.spigot.essentials.registries;

import de.relluem94.minecraft.server.spigot.essentials.models.pojo.BagTypeEntry;
import de.relluem94.minecraft.server.spigot.essentials.repositories.BagTypeRepository;
import java.util.List;
import java.util.Optional;

/**
 * Registry for managing {@link BagTypeEntry} instances in memory at runtime.
 */
public class BagTypeRegistry {

  private final BagTypeRepository bagTypeRepository;

  /**
   * Creates a new {@link BagTypeRegistry} backed by the given repository.
   *
   * @param bagTypeRepository the repository used to persist and retrieve entries
   */
  public BagTypeRegistry(BagTypeRepository bagTypeRepository) {
    this.bagTypeRepository = bagTypeRepository;
  }

  /**
   * Registers a single {@link BagTypeEntry} if it is not already registered.
   *
   * @param bagTypeEntry the {@link BagTypeEntry} to register
   * @throws IllegalArgumentException if the entry is already registered
   */
  @SuppressWarnings("unused")
  public void register(BagTypeEntry bagTypeEntry) {
    if (contains(bagTypeEntry)) {
      throw new IllegalArgumentException("BagTypeEntry is already registered: " + bagTypeEntry);
    }
    bagTypeRepository.save(bagTypeEntry);
  }

  /**
   * Unregisters a {@link BagTypeEntry} from the registry.
   *
   * @param bagTypeEntry the {@link BagTypeEntry} to unregister
   * @throws IllegalArgumentException if the entry is not registered
   */
  @SuppressWarnings("unused")
  public void unregister(BagTypeEntry bagTypeEntry) {
    if (!contains(bagTypeEntry)) {
      throw new IllegalArgumentException("BagTypeEntry is not registered: " + bagTypeEntry);
    }
    bagTypeRepository.delete(bagTypeEntry);
  }

  /**
   * Checks whether a {@link BagTypeEntry} is registered.
   *
   * @param bagTypeEntry the {@link BagTypeEntry} to check
   * @return {@code true} if the entry is registered, {@code false} otherwise
   */
  public boolean contains(BagTypeEntry bagTypeEntry) {
    return bagTypeRepository.findAll().contains(bagTypeEntry);
  }

  /**
   * Returns an unmodifiable view of all registered {@link BagTypeEntry} instances.
   *
   * @return an unmodifiable {@link List} of all registered {@link BagTypeEntry} instances
   */
  public List<BagTypeEntry> getAll() {
    return bagTypeRepository.findAll();
  }

  /**
   * Finds a {@link BagTypeEntry} by its unique id.
   *
   * @param id the unique id to search for
   * @return an {@link Optional} containing the entry, or empty if not found
   */
  public Optional<BagTypeEntry> findById(int id) {
    return bagTypeRepository.findById(id);
  }

  /**
   * Finds a {@link BagTypeEntry} by its name.
   *
   * @param name the name to search for
   * @return an {@link Optional} containing the entry, or empty if not found
   */
  @SuppressWarnings("unused")
  public Optional<BagTypeEntry> findByName(String name) {
    return bagTypeRepository.findByName(name);
  }

  public Optional<BagTypeEntry> findByPartialName(String name) {
    return bagTypeRepository.findAll().stream()
        .filter(bte -> name.contains(bte.getDisplayName())
            || name.contains(bte.getName().toLowerCase())
            || bte.getDisplayName().contains(name)
            || bte.getName().toLowerCase().contains(name))
        .findFirst();
  }


}