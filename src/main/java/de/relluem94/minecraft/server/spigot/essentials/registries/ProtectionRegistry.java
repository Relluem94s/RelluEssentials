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

public class ProtectionRegistry {

  private final List<Material> protectionLocksList = new ArrayList<>();
  private final HashMap<Location, ProtectionEntry> protectionEntryMap = new HashMap<>();

  /**
   * Creates a new ProtectionAPI instance and populates the internal protection locks list and
   * protection entry map with the provided data.
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


  public List<ProtectionEntry> getProtectionEntriesOwnedBy(long playerId) {
    return protectionEntryMap.values().stream()
        .filter(entry -> entry.getCreatedBy() == playerId)
        .toList();
  }


  /**
   * Returns the map of all current protection entries.
   *
   * @return a {@link Map} mapping each protected {@link Location} to its corresponding
   * {@link ProtectionEntry}
   */
  public Map<Location, ProtectionEntry> getProtectionEntryList() {
    return Collections.unmodifiableMap(protectionEntryMap);
  }

  public boolean isProtectableMaterial(Material material) {
    return protectionLocksList.contains(material);
  }

  public void removeProtectionEntriesByIds(List<Long> protectionIds) {
    protectionEntryMap.entrySet().removeIf(
        entry -> protectionIds.contains((long) entry.getValue().getId())
    );
  }


  private static Location normalizeLocation(Location location) {
    return new Location(location.getWorld(), location.getX(), location.getY(), location.getZ());
  }
}