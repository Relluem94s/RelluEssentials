package de.relluem94.minecraft.server.spigot.essentials.registries;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.BagEntry;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * In-memory registry for managing {@link BagEntry} instances at runtime, keyed by player id.
 */
public class BagRegistry {

  private final Multimap<Integer, BagEntry> bagsByPlayerId = ArrayListMultimap.create();

  /**
   * Registers a {@link BagEntry} into the in-memory registry.
   *
   * @param bagEntry the {@link BagEntry} to register
   */
  public void register(BagEntry bagEntry) {
    bagsByPlayerId.put(bagEntry.getPlayerId(), bagEntry);
  }

  /**
   * Registers all given {@link BagEntry} instances into the in-memory registry.
   *
   * @param bagEntries the collection of {@link BagEntry} instances to register
   */
  public void registerAll(Collection<BagEntry> bagEntries) {
    bagEntries.forEach(this::register);
  }

  /**
   * Unregisters a {@link BagEntry} from the in-memory registry.
   *
   * @param bagEntry the {@link BagEntry} to unregister
   */
  public void unregister(BagEntry bagEntry) {
    bagsByPlayerId.remove(bagEntry.getPlayerId(), bagEntry);
  }

  /**
   * Returns all registered {@link BagEntry} instances for the given player id.
   *
   * @param playerId the player id to look up
   * @return an unmodifiable {@link Collection} of {@link BagEntry} instances
   */
  public Collection<BagEntry> findAllByPlayerId(int playerId) {
    return List.copyOf(bagsByPlayerId.get(playerId));
  }

  /**
   * Finds a {@link BagEntry} by player id and bag type id.
   *
   * @param playerId  the player id to search for
   * @param bagTypeId the bag type id to search for
   * @return an {@link Optional} containing the matching entry, or empty if not found
   */
  public Optional<BagEntry> findByPlayerIdAndBagTypeId(int playerId, int bagTypeId) {
    return bagsByPlayerId.get(playerId).stream()
        .filter(entry -> entry.getBagType().getId() == bagTypeId)
        .findFirst();
  }

  /**
   * Checks whether a {@link BagEntry} exists for the given player id and bag type id.
   *
   * @param playerId  the player id to check
   * @param bagTypeId the bag type id to check
   * @return {@code true} if a matching entry exists, {@code false} otherwise
   */
  public boolean existsByPlayerIdAndBagTypeId(int playerId, int bagTypeId) {
    return findByPlayerIdAndBagTypeId(playerId, bagTypeId).isPresent();
  }

  /**
   * Checks whether any {@link BagEntry} exists for the given player id.
   *
   * @param playerId the player id to check
   * @return {@code true} if at least one entry exists, {@code false} otherwise
   */
  public boolean existsByPlayerId(int playerId) {
    return bagsByPlayerId.containsKey(playerId);
  }

  /**
   * Returns all registered {@link BagEntry} instances across all players.
   *
   * @return an unmodifiable {@link Collection} of all {@link BagEntry} instances
   */
  public Collection<BagEntry> findAll() {
    return List.copyOf(bagsByPlayerId.values());
  }
}