package de.relluem94.minecraft.server.spigot.essentials.registry;

import de.relluem94.minecraft.server.spigot.essentials.model.pojo.BagTypeEntry;
import de.relluem94.minecraft.server.spigot.essentials.repository.BagTypeRepository;
import java.util.List;
import java.util.Optional;

/**
 * Registry for managing {@link BagTypeEntry} instances in memory at runtime.
 */
public class BagTypeRegistry {

  private final BagTypeRepository bagTypeRepository;

  public BagTypeRegistry(BagTypeRepository bagTypeRepository) {
    this.bagTypeRepository = bagTypeRepository;
  }

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
    bagTypeRepository.save(bagTypeEntry);
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

  public Optional<BagTypeEntry> findById(int id) {
    return bagTypeRepository.findById(id);
  }

  public Optional<BagTypeEntry> findByName(String name) {
    return bagTypeRepository.findByName(name);
  }

}