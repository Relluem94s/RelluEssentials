package de.relluem94.minecraft.server.spigot.essentials.registries;

import de.relluem94.minecraft.server.spigot.essentials.models.pojo.ProtectionEntry;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.ProtectionLockEntry;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.Location;
import org.bukkit.Material;

/**
 * Registry responsible for managing protection entries and protectable materials. It handles the
 * storage and retrieval of {@link ProtectionEntry} objects mapped to specific locations.
 */
public class ProtectionRegistry {

  private final List<Material> protectionLocksList = new ArrayList<>();
  private final HashMap<Location, ProtectionEntry> protectionEntryMap = new HashMap<>();

  /**
   * Creates a new {@link ProtectionRegistry} instance and populates the internal protection locks
   * list and protection entry map with the provided data.
   *
   * @param protectionLocksEntryList list of {@link ProtectionLockEntry} whose materials will be
   *                                 registered as protectable
   * @param protectionEntryMap       map of {@link Location} to {@link ProtectionEntry} representing
   *                                 existing protection entries
   */
  public ProtectionRegistry(List<ProtectionLockEntry> protectionLocksEntryList,
      Map<Location, ProtectionEntry> protectionEntryMap) {
    for (ProtectionLockEntry ple : protectionLocksEntryList) {
      addProtectionMaterial(ple.getValue());
    }
    this.protectionEntryMap.putAll(protectionEntryMap);
  }

  private static Location normalizeLocation(Location location) {
    return new Location(location.getWorld(), location.getX(), location.getY(), location.getZ());
  }

  /**
   * Returns the {@link ProtectionEntry} associated with the given {@link Location}.
   *
   * @param l the location to look up
   * @return the {@link ProtectionEntry} at the given location, or {@code null} if none exists
   */
  public ProtectionEntry getProtectionEntry(Location l) {
    return protectionEntryMap.get(normalizeLocation(l));
  }

  /**
   * Removes the {@link ProtectionEntry} associated with the given {@link Location}.
   *
   * @param l the location whose protection entry should be removed
   */
  public void removeProtectionEntry(Location l) {
    protectionEntryMap.remove(normalizeLocation(l));
  }

  /**
   * Associates the given {@link ProtectionEntry} with the specified {@link Location}. If an entry
   * already exists for that location, it will be overwritten.
   *
   * @param l  the location to protect
   * @param pe the {@link ProtectionEntry} to associate with the location
   */
  public void putProtectionEntry(Location l, ProtectionEntry pe) {
    protectionEntryMap.put(normalizeLocation(l), pe);
  }

  /**
   * Registers a {@link Material} as protectable via the API.
   *
   * @param m the material to add to the protection list
   */
  private void addProtectionMaterial(Material m) {
    protectionLocksList.add(m);
  }

  /**
   * Retrieves all protection entries that were created by a specific player.
   *
   * @param playerId the unique identifier of the player
   * @return a {@link List} of {@link ProtectionEntry} objects owned by the given player
   */
  public List<ProtectionEntry> getProtectionEntriesOwnedBy(long playerId) {
    return protectionEntryMap.values().stream()
        .filter(entry -> entry.getCreatedBy() == playerId)
        .toList();
  }

  /**
   * Returns the map of all current protection entries.
   *
   * @return an unmodifiable {@link Map} mapping each protected {@link Location} to its
   *     corresponding {@link ProtectionEntry}
   */
  public Map<Location, ProtectionEntry> getProtectionEntryList() {
    return Collections.unmodifiableMap(protectionEntryMap);
  }

  /**
   * Checks if a specific material is registered as a protectable material.
   *
   * @param material the material to check
   * @return {@code true} if the material is protectable, {@code false} otherwise
   */
  public boolean isProtectableMaterial(Material material) {
    return protectionLocksList.contains(material);
  }

  /**
   * Removes all protection entries that match the provided list of protection IDs.
   *
   * @param protectionIds a {@link List} of IDs representing the entries to be removed
   */
  public void removeProtectionEntriesByIds(List<Long> protectionIds) {
    protectionEntryMap.entrySet().removeIf(
        entry -> protectionIds.contains((long) entry.getValue().getId())
    );
  }
}